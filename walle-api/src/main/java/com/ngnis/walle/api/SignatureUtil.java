package com.ngnis.walle.api;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.digest.HMac;
import cn.hutool.crypto.digest.HmacAlgorithm;
import lombok.extern.slf4j.Slf4j;

import java.net.URLEncoder;

/**
 * 根据SecretKey计算加密后的签名
 *
 * @author houyi
 */
@Slf4j
public class SignatureUtil {

    public static String sign(long timestamp, String secret) {
        String sign = "";
        try {
            String dataToSign = timestamp + "\n" + secret;

            HMac hMac = SecureUtil.hmac(HmacAlgorithm.HmacSHA256, secret);
            byte[] bytes = hMac.digest(dataToSign);
            String base64Encoded = Base64.encode(bytes);
            sign = URLEncoder.encode(base64Encoded, CharsetUtil.UTF_8);

        } catch (Exception e) {
            log.error(";generateSignError;timestamp={}, secret={}", timestamp, secret);
        }
        return sign;
    }


}
