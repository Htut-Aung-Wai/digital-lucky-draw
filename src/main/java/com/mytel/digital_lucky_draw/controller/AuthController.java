package com.mytel.digital_lucky_draw.controller;


import com.mytel.digital_lucky_draw.constant.ErrorCode;
import com.mytel.digital_lucky_draw.dto.LoginRequest;
import com.mytel.digital_lucky_draw.dto.LoginResponse;
import com.mytel.digital_lucky_draw.response.ResponseFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
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

    /**
     * Đăng nhập bằng username/password. Nếu đúng, tạo session và trả Set-Cookie (JSESSIONID).
     * Frontend gọi với credentials: 'include' để nhận cookie; các request sau tự gửi cookie.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request, HttpServletRequest httpRequest) {
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

    /**
     * Đăng xuất: xóa session. Frontend gọi với credentials: 'include' để gửi cookie cần xóa.
     */
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
