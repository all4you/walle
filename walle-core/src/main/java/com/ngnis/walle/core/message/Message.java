package com.ngnis.walle.core.message;

/**
 * 消息接口
 *
 * @author houyi.wh
 * @since 2018-09-09
 */
public interface Message {

    /**
     * 消息的类型
     *
     * @return 消息的类型
     */
    MessageType messageType();

    /**
     * 消息是否合法
     *
     * @return true：合法 false：不合法
     */
    boolean valid();

    /**
     * 返回json字符串类型的消息内容
     *
     * @return 消息内容
     */
    String content();
}
