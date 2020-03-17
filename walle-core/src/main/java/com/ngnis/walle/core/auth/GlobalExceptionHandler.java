package com.ngnis.walle.core.auth;

import cn.hutool.core.util.StrUtil;
import com.ngnis.walle.common.result.BaseResult;
import com.ngnis.walle.common.result.ResultCode;
import com.ngnis.walle.common.result.BaseResult;
import com.ngnis.walle.common.result.ResultCode;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 全局异常拦截器
 *
 * @author houyi
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public Object handleException(Exception e) {
        BaseResult baseResult = new BaseResult();
        int errorCode;
        String errorMsg;
        if (e instanceof InvalidTokenException) {
            errorCode = ((InvalidTokenException) e).getCode();
            errorMsg = e.getMessage();
        } else {
            errorCode = ResultCode.BIZ_FAIL.getCode();
            errorMsg = StrUtil.isBlank(e.getMessage()) ? ResultCode.BIZ_FAIL.getMessage() : e.getMessage();
        }
        baseResult.setErrorMessage(errorCode, errorMsg);
        return baseResult;
    }

}