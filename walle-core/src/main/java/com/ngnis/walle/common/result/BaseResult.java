package com.ngnis.walle.common.result;

import lombok.Data;

import java.io.Serializable;

/**
 * @author houyi
 */
@Data
public class BaseResult implements Serializable {

    private boolean success;
    private int errorCode;
    private String errorMsg;

    public BaseResult() {
        this.success = true;
        this.setErrorCode(ResultCode.SUCCESS.getCode());
        this.setErrorMsg(ResultCode.SUCCESS.getMessage());
    }

    public void setErrorMessage(int code, String message) {
        this.success = false;
        this.setErrorCode(code);
        this.setErrorMsg(message);
    }

}
