package com.ngnis.walle.config;

import com.ngnis.walle.center.msg.MessageCenter;
import com.ngnis.walle.service.msg.DefaultMessageCenter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;


/**
 * @author houyi
 */
public class MessageCenterAutoConfig {

    /**
     * 创建DefaultMessageCenter
     */
    @Bean
    @ConditionalOnMissingBean(MessageCenter.class)
    public MessageCenter defaultMessageCenter() {
        return new DefaultMessageCenter();
    }


}
