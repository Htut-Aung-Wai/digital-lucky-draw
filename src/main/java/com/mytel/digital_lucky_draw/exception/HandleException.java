package com.mytel.digital_lucky_draw.exception;



import com.mytel.digital_lucky_draw.constant.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class HandleException extends ResponseEntityExceptionHandler {
    private final ResponseFactoryForException responseFactory;

    public HandleException(ResponseFactoryForException responseFactory) {
        this.responseFactory = responseFactory;
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String message = ex.getFieldErrors().stream()
                .map(error -> String.format("Field {%s} error: %s", error.getField(), error.getDefaultMessage())).collect(Collectors.joining("; "));
        log.error("Exception: {}", message);
        return responseFactory.badRequest(ErrorCode.HAVE_SOME_PROBLEM);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex, HttpHeaders headers,
            HttpStatus status, WebRequest request) {
        return responseFactory.badRequest(ErrorCode.HAVE_SOME_PROBLEM, ex.getMessage());
    }

    @ExceptionHandler(CommonException.class)
    public ResponseEntity<?> handleCommonException(CommonException e) {
        log.error("Error: ", e);
        if (ErrorCode.ERROR_404.equals(e.getErrorCode())) {
            return responseFactory.notFound(e.getErrorCode(), e.getMessage());
        }

        if (ErrorCode.ERROR_401.equals(e.getErrorCode())) {
            return responseFactory.notAuthorized(e.getErrorCode(), e.getMessage());
        }

        return responseFactory.badRequest(e.getErrorCode(), e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleExceptions(Exception e) {
        log.error("Error: ", e);
        return responseFactory.badRequest(ErrorCode.HAVE_SOME_PROBLEM, e.getMessage());
    }

}