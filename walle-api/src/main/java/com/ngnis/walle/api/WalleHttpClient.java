package com.ngnis.walle.api;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * @author houyi
 */
public class WalleHttpClient implements WalleClient {

    private WalleConfig config;

    public WalleHttpClient(WalleConfig config) {
        if (config == null) {
            throw new IllegalArgumentException("config can't be null");
        }
        if (StrUtil.isBlank(config.getEndPoint())) {
            throw new IllegalArgumentException("endPoint can't be blank");
        }
        if (StrUtil.isBlank(config.getAccessKey())) {
            throw new IllegalArgumentException("accessKey can't be blank");
        }
        if (StrUtil.isBlank(config.getSecretKey())) {
            throw new IllegalArgumentException("secretKey can't be blank");
        }
        this.config = config;
    }

    @Override
    public BaseResult sendGroupMessage(GroupMessageDTO dto) {
        BaseResult baseResult = new BaseResult();
        String boardCode = dto == null ? null : dto.getBoardCode();
        if (StrUtil.isBlank(boardCode)) {
            baseResult.setErrorMessage(3001, "模板编码不能为空");
            return baseResult;
        }
        long timestamp = DateUtil.current(false);
        String accessKey = config.getAccessKey();
        String secretKey = config.getSecretKey();
        String sign = SignatureUtil.sign(timestamp, secretKey);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("boardCode", dto.getBoardCode());
        if (dto.getData() != null) {
            jsonObject.put("data", dto.getData().toJSONString());
        }
        String body = jsonObject.toJSONString();
        String url = config.getEndPoint() + "/walle/api/groupMsg/send";
        // 创建请求
        HttpRequest httpRequest = HttpRequest.post(url);
        httpRequest.header("accessKey", accessKey);
        httpRequest.header("timestamp", String.valueOf(timestamp));
        httpRequest.header("sign", sign);
        httpRequest.body(body);
        // 发送请求
        String result = httpRequest.execute().body();
        baseResult = JSON.parseObject(result, BaseResult.class);

        return baseResult;
    }



}
