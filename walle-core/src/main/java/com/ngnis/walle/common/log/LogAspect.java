package com.ngnis.walle.common.log;

import cn.hutool.core.text.StrFormatter;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.ngnis.walle.common.result.BaseResult;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 日志切面
 *
 * @author houyi
 */
@Aspect
@Component
@Slf4j
public class LogAspect {

    /**
     * 切面方法,记录日志
     */
    @Around("execution(* com.ngnis.walle..*.*(..)) && @annotation(com.ngnis.walle.common.log.PrintLog)")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        Signature signature = joinPoint.getSignature();
        if (!(signature instanceof MethodSignature)) {
            return joinPoint.proceed();
        }
        Object object = null;
        MethodSignature methodSign = (MethodSignature) signature;
        Method method = methodSign.getMethod();
        PrintLog annotation = AnnotationUtils.getAnnotation(method, PrintLog.class);
        String methodName = StrUtil.isBlank(annotation.methodName()) ? signature.getName() : annotation.methodName();
        try {
            // 调用实际方法
            object = joinPoint.proceed();
            if (object instanceof BaseResult) {
                BaseResult baseResult = (BaseResult) object;
                if (baseResult.isSuccess()) {
                    GenericLogUtil.invokeSuccess(log, methodName, StrFormatter.format("param={}", JSON.toJSONString(args)), StrFormatter.format("baseResult={}", JSON.toJSONString(baseResult)));
                } else {
                    GenericLogUtil.invokeFail(log, methodName, StrFormatter.format("param={}", JSON.toJSONString(args)), StrFormatter.format("baseResult={}", JSON.toJSONString(baseResult)));
                }
            } else {
                GenericLogUtil.invokeSuccess(log, methodName, StrFormatter.format("param={}", JSON.toJSONString(args)), "");
            }
        } catch (Throwable e) {
            GenericLogUtil.invokeError(log, methodName, StrFormatter.format("param={}", JSON.toJSONString(args)), e);
        }
        return object;
    }


}