package com.ngnis.walle.core.auth;

import com.ngnis.walle.common.result.ResultCode;
import com.ngnis.walle.common.result.ResultCode;

/**
 * 无效的token
 *
 * @author houyi
 */
public class InvalidTokenException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private int code;


    public InvalidTokenException(ResultCode resultCode) {
        this(resultCode.getCode(), resultCode.getMessage());
    }

    public InvalidTokenException(int code, String message) {
        super(message);
        this.setCode(code);
    }

    public InvalidTokenException(int code, String message, Throwable cause) {
        super(message, cause);
        this.setCode(code);
    }

    public InvalidTokenException(int code, Throwable cause) {
        super(cause);
        this.setCode(code);
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }
}
