package com.ngnis.walle.core.sender;

import lombok.Data;

/**
 * 钉钉机器人发送消息后返回的响应对象
 * @author houyi.wh
 * @since 2018-09-09
 */
@Data
public class DingTalkResponse {
    private String errmsg;
    private int errcode;

}