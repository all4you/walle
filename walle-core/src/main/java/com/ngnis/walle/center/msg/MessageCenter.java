package com.ngnis.walle.center.msg;

import com.ngnis.walle.common.result.BaseResult;

/**
 * @author houyi
 */
public interface MessageCenter {

    /**
     * 发送群消息
     */
    BaseResult sendGroupMessage(SendGroupMessageDTO dto);

}
