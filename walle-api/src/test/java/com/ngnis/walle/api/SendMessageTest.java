package com.ngnis.walle.api;

import com.alibaba.fastjson.JSONObject;
import org.junit.Test;

/**
 * @author houyi
 */
public class SendMessageTest {


    @Test
    public void sendGroupMsg() {
        WalleConfig config = WalleConfig.builder()
                .endPoint("http://127.0.0.1:7001")
                .accessKey("m74hscNSZPVWo3tK")
                .secretKey("QsfigP8ibVhgFv5QmcPHkwsV")
                .build();
        // 创建 WalleClient
        WalleClient walleClient = new WalleHttpClient(config);
        // 设置请求参数
        GroupMessageDTO messageDTO = new GroupMessageDTO();
        // 模板编号
        messageDTO.setBoardCode("device_offline");
        // 携带的数据
        JSONObject data = new JSONObject();
        data.put("level", "high");
        messageDTO.setData(data.toJSONString());
        // 发送请求
        BaseResult result = walleClient.sendGroupMessage(messageDTO);
        System.out.println(result);
    }

}
