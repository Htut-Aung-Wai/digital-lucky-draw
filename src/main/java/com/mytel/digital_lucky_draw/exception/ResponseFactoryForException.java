package com.mytel.digital_lucky_draw.exception;


import com.mytel.digital_lucky_draw.response.Basic;
import com.mytel.digital_lucky_draw.utils.I18n;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class ResponseFactoryForException {

    public ResponseFactoryForException() { // Default constructor
    }

    public ResponseFactoryForException(String errInputInvalid, String message) {
    }

    public ResponseEntity<Basic> buildError(HttpStatus status, String errorCode) {
        Basic basic = new Basic();
        basic.setSuccess(false);
        basic.setResult(null);
        basic.setMessage(I18n.t(errorCode));
        basic.setCode(errorCode);
        return ResponseEntity.status(status).body(basic);
    }

    public ResponseEntity<Object> badRequest(String errorCode) {
        Basic basic = new Basic();
        basic.setSuccess(false);
        basic.setResult(null);
        basic.setMessage(I18n.t(errorCode));
        basic.setCode(errorCode);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(basic);
    }

    public ResponseEntity<Object> badRequest(Object result, String errorCode) {
        Basic basic = new Basic();
        basic.setSuccess(false);
        basic.setResult(result);
        basic.setMessage(I18n.t(errorCode));
        basic.setCode(errorCode);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(basic);
    }

    public ResponseEntity<Object> badRequest(String errorCode, String message) {
        Basic basic = new Basic();
        basic.setSuccess(false);
        basic.setResult(null);
        basic.setMessage(message);
        basic.setCode(errorCode);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(basic);
    }

    public ResponseEntity<Object> notFound(String errorCode, String message) {
        Basic basic = new Basic();
        basic.setSuccess(false);
        basic.setResult(null);
        basic.setMessage(message);
        basic.setCode(errorCode);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(basic);
    }

    public ResponseEntity<Object> notAuthorized(String errorCode, String message) {
        Basic basic = new Basic();
        basic.setSuccess(false);
        basic.setResult(null);
        basic.setMessage(message);
        basic.setCode(errorCode);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(basic);
    }

    public ResponseEntity<Basic> buildSuccess(Object result, String errorCode) {
        Basic basic = new Basic();
        basic.setSuccess(true);
        basic.setMessage(I18n.t(errorCode));
        basic.setResult(result);
        basic.setCode(errorCode);
        return ResponseEntity.status(HttpStatus.OK).body(basic);
    }
}
