package com.ngnis.walle.common;

import cn.hutool.core.text.StrFormatter;
import org.slf4j.Logger;

/**
 * 统一日志埋点
 *
 * @author houyi
 */
public class GenericLogUtil {

    public static void invokeAbort(Logger log, String methodName, String invokeParam, String abortReason) {
        LogUtil.info(log,
                StrFormatter.format("{};invoke;abort", methodName),
                StrFormatter.format("{}", abortReason),
                invokeParam);
    }

    public static void invokeSuccess(Logger log, String methodName, String invokeParam, String invokeResult) {
        LogUtil.info(log,
                StrFormatter.format("{};invoke;success", methodName),
                StrFormatter.format("{}", invokeResult),
                invokeParam);
    }

    public static void debugSuccess(Logger log, String methodName, String invokeParam, String invokeResult) {
        LogUtil.debug(log,
                StrFormatter.format("{};invoke;success", methodName),
                StrFormatter.format("{}", invokeResult),
                invokeParam);
    }

    public static void invokeFail(Logger log, String methodName, String invokeParam, String errorMsg) {
        LogUtil.warn(log,
                StrFormatter.format("{};invoke;fail", methodName),
                StrFormatter.format("errorMsg={}", errorMsg),
                invokeParam);
    }

    public static void invokeEmptyResult(Logger log, String methodName, String invokeParam, String invokeResult) {
        LogUtil.warn(log,
                StrFormatter.format("{};invoke;emptyResult", methodName),
                StrFormatter.format("{}", invokeResult),
                invokeParam);
    }

    public static void invokeError(Logger log, String methodName, String invokeParam, Throwable e) {
        LogUtil.error(log,
                StrFormatter.format("{};invoke;error", methodName),
                StrFormatter.format("cause={}", e.getMessage()),
                invokeParam, e);
    }

    public static void invokeBlocked(Logger log, String methodName, String invokeParam, Throwable e) {
        LogUtil.error(log,
                StrFormatter.format("{};invoke;blocked by sentinel", methodName),
                StrFormatter.format("cause={}", e.getMessage()),
                invokeParam, e);
    }

}
