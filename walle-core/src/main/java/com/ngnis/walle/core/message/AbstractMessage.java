package com.ngnis.walle.core.message;

import com.alibaba.fastjson.JSONObject;
import com.ngnis.walle.common.bean.BeanValidator;
import com.ngnis.walle.common.result.BaseResult;
import com.ngnis.walle.common.result.BaseResult;
import lombok.Data;

/**
 * @author houyi
 */
@Data
public abstract class AbstractMessage implements Message {

    private String msgtype = this.messageType().getType();

    @Override
    public boolean valid() {
        BaseResult baseResult = BeanValidator.validate(this);
        return baseResult.isSuccess();
    }

    @Override
    public String content() {
        if (!valid()) {
            return null;
        }
        return JSONObject.toJSONString(this);
    }

}
