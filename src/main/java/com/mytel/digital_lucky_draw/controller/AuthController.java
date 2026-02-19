package com.mytel.digital_lucky_draw.controller;


import com.mytel.digital_lucky_draw.constant.ErrorCode;
import com.mytel.digital_lucky_draw.dto.LoginRequest;
import com.mytel.digital_lucky_draw.dto.LoginResponse;
import com.mytel.digital_lucky_draw.response.ResponseFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final ResponseFactory responseFactory;

    @Value("${app.redirect.url}")
    private String redirectUrl;



    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request, HttpServletRequest httpRequest) {
        // Check if user is already authenticated
        Authentication existingAuth = SecurityContextHolder.getContext().getAuthentication();

        // If already authenticated with the same user, return success
        if (existingAuth != null && existingAuth.isAuthenticated()
                && !(existingAuth instanceof AnonymousAuthenticationToken)
                && existingAuth.getName().equals(request.getUserName())) {
            LoginResponse response = LoginResponse.builder()
                    .username(request.getUserName())
                    .authenticated(true)
                    .message("Already logged in")
                    .redirectUrl(redirectUrl)
                    .build();
            return responseFactory.buildSuccess(HttpStatus.OK, response, ErrorCode.CODE_200, response.getMessage());
        }

        // If authenticated with different user, logout first
        if (existingAuth != null && existingAuth.isAuthenticated()
                && !(existingAuth instanceof AnonymousAuthenticationToken)) {
            try {
                httpRequest.logout();
            } catch (Exception ignored) {
                // Ignore logout errors
            }
        }

        // Now perform login
        try {
            httpRequest.login(request.getUserName(), request.getPassword());
        } catch (BadCredentialsException | ServletException e) {
            return responseFactory.buildError(
                    HttpStatus.UNAUTHORIZED,
                    null,
                    ErrorCode.ERROR_403,
                    "Invalid username or password"
            );
        }

        LoginResponse response = LoginResponse.builder()
                .username(request.getUserName())
                .authenticated(true)
                .message("Login successful")
                .redirectUrl(redirectUrl)
                .build();
        return responseFactory.buildSuccess(HttpStatus.OK, response, ErrorCode.CODE_200, response.getMessage());
    }




    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest httpRequest) {
        try {
            httpRequest.logout();
        } catch (Exception ignored) {
            // not logged in
        }
        return responseFactory.buildSuccess(HttpStatus.OK, null, ErrorCode.CODE_200, "Logged out");
    }
}
