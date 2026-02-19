package com.mytel.digital_lucky_draw.exception;



import com.mytel.digital_lucky_draw.utils.I18n;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommonException extends RuntimeException {
    private String errorCode;
    private String message;

    public CommonException(Long requestId, String errorCode) {
        this.errorCode = errorCode;
        this.message = I18n.t(errorCode);
    }

    public CommonException(String errorCode) {
        this.errorCode = errorCode;
        this.message = I18n.t(errorCode);
    }

    public CommonException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.message = message;
    }
}

