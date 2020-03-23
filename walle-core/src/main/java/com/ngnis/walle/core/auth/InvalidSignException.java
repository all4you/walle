package com.ngnis.walle.core.auth;

import com.ngnis.walle.common.result.ResultCode;

/**
 * 无效的sign
 *
 * @author houyi
 */
public class InvalidSignException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private int code;


    public InvalidSignException(ResultCode resultCode) {
        this(resultCode.getCode(), resultCode.getMessage());
    }

    public InvalidSignException(int code, String message) {
        super(message);
        this.setCode(code);
    }

    public InvalidSignException(int code, String message, Throwable cause) {
        super(message, cause);
        this.setCode(code);
    }

    public InvalidSignException(int code, Throwable cause) {
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
