package com.mytel.digital_lucky_draw.controller;


import com.mytel.digital_lucky_draw.constant.ErrorCode;
import com.mytel.digital_lucky_draw.response.ResponseFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/test")
public class TestController {

    private final ResponseFactory responseFactory;

    @PostMapping
    public ResponseEntity<?> test() {

        return responseFactory.buildSuccess(HttpStatus.OK, null, ErrorCode.CODE_200, "Test success");
    }
}
