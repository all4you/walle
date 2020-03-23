package com.ngnis.walle.api;

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
        this.setErrorCode(200);
        this.setErrorMsg("SUCCESS");
    }

    public void setErrorMessage(int code, String message) {
        this.success = false;
        this.setErrorCode(code);
        this.setErrorMsg(message);
    }

}
