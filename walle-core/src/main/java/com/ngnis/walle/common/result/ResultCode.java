package com.ngnis.walle.common.result;

public enum ResultCode {
    SUCCESS(200, "SUCCESS"),
    INTERNAL_ERROR(301, "INTERNAL_ERROR"),
    PAGE_NOT_FOUND(404, "PAGE_NOT_FOUND"),
    BIZ_FAIL(3001, "BIZ_FAIL"),
    INVALID_TOKEN(3002, "INVALID_TOKEN"),
    RECORD_ALREADY_EXISTS(4001, "RECORD_ALREADY_EXISTS"),
    RESOURCE_NOT_FOUND(4004, "RESOURCE_NOT_FOUND"),
    PARAM_INVALID(4002, "PARAM_INVALID");

    private int code;
    private String message;

    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }
}