package com.ngnis.walle.common.log;

import cn.hutool.core.text.StrFormatter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;

/**
 * 日志分隔符格式
 * 2019-05-14 21:59:32.351|INFO|45950|camera-1|c.a.c.s.s.r.i.ReportServiceImpl|saveCameraReportInfo finished|save success|reportUuid=5cdac9a6b64aca95db7a8996, reportType=CAMERA
 * <p>
 *
 * @author houyi
 */
@Slf4j
public class LogUtil {

    /**
     * 打印日志，格式如下：
     * 执行了什么操作|得到了什么结果|对应的参数
     *
     * @param logger  logger
     * @param operate 执行了什么操作，不能为空
     * @param result  得到了什么结果，可能为空
     * @param param   对应的参数，可能为空
     */
    public static void debug(Logger logger, String operate, String result, String param) {
        logger.debug("{}|{}|{}", operate, result, param);
    }

    /**
     * 打印日志，格式如下：
     * 执行了什么操作|得到了什么结果|对应的参数
     *
     * @param logger  logger
     * @param operate 执行了什么操作，不能为空
     * @param result  得到了什么结果，可能为空
     * @param param   对应的参数，可能为空
     */
    public static void info(Logger logger, String operate, String result, String param) {
        logger.info("{}|{}|{}", operate, result, param);
    }

    /**
     * 打印日志，格式如下：
     * 执行了什么操作|得到了什么结果|对应的参数
     *
     * @param logger  logger
     * @param operate 执行了什么操作，不能为空
     * @param result  得到了什么结果，可能为空
     * @param param   对应的参数，可能为空
     */
    public static void warn(Logger logger, String operate, String result, String param) {
        logger.warn("{}|{}|{}", operate, result, param);
    }


    /**
     * 打印日志，格式如下：
     * 执行了什么操作|得到了什么结果|对应的参数
     *
     * @param logger  logger
     * @param operate 执行了什么操作，不能为空
     * @param result  得到了什么结果，可能为空
     * @param param   对应的参数，可能为空
     * @param e       出现的异常
     */
    public static void error(Logger logger, String operate, String result, String param, Throwable e) {
        logger.error(StrFormatter.format("{}|{}|{}", operate, result, param), e);
    }


    public static void main(String[] args) {
        try {
            int i = 1 / 0;
        } catch (Exception e) {
            LogUtil.error(log, "makeError", "", null, e);
        }
    }

}
