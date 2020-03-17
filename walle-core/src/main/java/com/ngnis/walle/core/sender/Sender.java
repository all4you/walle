package com.ngnis.walle.core.sender;

import com.ngnis.walle.common.result.PojoResult;
import com.ngnis.walle.core.board.Address;
import com.ngnis.walle.core.message.Message;
import com.ngnis.walle.common.result.PojoResult;
import com.ngnis.walle.core.board.Address;
import com.ngnis.walle.core.message.Message;

/**
 * 钉钉群机器人内容发送器
 *
 * @author houyi.wh
 * @since 2018-09-09
 */
public interface Sender {

    /**
     * 发送内容
     *
     * @param address      要发送的机器人的地址
     * @param message      要发送的内容，格式必须要符合钉钉
     * @param responseType 返回的结果类型
     * @return 返回的结果
     */
    <T> PojoResult<T> send(Address address, Message message, Class<T> responseType);

}
