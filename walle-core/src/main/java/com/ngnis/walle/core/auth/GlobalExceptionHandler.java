package com.ngnis.walle.core.auth;

import cn.hutool.core.text.StrFormatter;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.ngnis.walle.common.log.GenericLogUtil;
import com.ngnis.walle.common.result.BaseResult;
import com.ngnis.walle.common.result.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 全局异常拦截器
 *
 * @author houyi
 */
@Slf4j
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
        } else if (e instanceof InvalidSignException) {
            errorCode = ((InvalidSignException) e).getCode();
            errorMsg = e.getMessage();
        } else {
            errorCode = ResultCode.BIZ_FAIL.getCode();
            errorMsg = StrUtil.isBlank(e.getMessage()) ? ResultCode.BIZ_FAIL.getMessage() : e.getMessage();
        }
        baseResult.setErrorMessage(errorCode, errorMsg);
        GenericLogUtil.invokeError(log, "handleException", StrFormatter.format("baseResult={}", JSON.toJSONString(baseResult)), e);
        return baseResult;
    }

}