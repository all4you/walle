package com.ngnis.walle.core;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.text.StrFormatter;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.digest.HMac;
import cn.hutool.crypto.digest.HmacAlgorithm;
import com.ngnis.walle.common.GenericLogUtil;
import com.ngnis.walle.common.GenericLogUtil;
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
            GenericLogUtil.invokeError(log, "generateSign", StrFormatter.format("timestamp={}, secret={}", timestamp, secret), e);
        }
        return sign;
    }


    public static String createKey(long timestamp, String secret, int keyLen) {
        String dataToSign = timestamp + "\n" + secret;

        HMac hMac = SecureUtil.hmac(HmacAlgorithm.HmacSHA256, secret);
        byte[] bytes = hMac.digest(dataToSign);
        String key = Base64.encodeUrlSafe(bytes)
                .replace("-", "")
                .replace("_", "");
        // 对key的长度进行截取
        if (key.length() > keyLen) {
            key = key.substring(0, keyLen);
        }
        return key;
    }


    public static void main(String[] args) {
        long timestamp = DateUtil.current(false);
        int keyLen = 100;
        String accessKey = createKey(timestamp, "secret", keyLen);
        System.out.println("accessKey1=" + accessKey);
        accessKey = createKey(timestamp, "12345523253", keyLen);
        System.out.println("accessKey2=" + accessKey);
        accessKey = createKey(timestamp, "sreewet1523235", keyLen);
        System.out.println("accessKey3=" + accessKey);
    }

}
