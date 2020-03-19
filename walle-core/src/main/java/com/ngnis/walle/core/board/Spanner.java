package com.ngnis.walle.core.board;

import com.ngnis.walle.common.result.BaseResult;
import com.ngnis.walle.core.message.Message;

import java.util.Map;

/**
 * 模板工具
 *
 * @author houyi
 */
public interface Spanner {

    /**
     * 检查模板是否合格
     * 主要是根据不同的消息类型来校验
     */
    BaseResult check(GroupBoard board);

    /**
     * 将模板转换成Message
     */
    Message make(GroupBoard board, Map<String, Object> data);

}
