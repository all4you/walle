package com.ngnis.walle.config;

import com.ngnis.walle.center.board.GroupBoardCenter;
import com.ngnis.walle.center.msg.MessageCenter;
import com.ngnis.walle.core.sender.Sender;
import com.ngnis.walle.service.board.Spanner;
import com.ngnis.walle.service.msg.DefaultMessageCenter;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;


/**
 * @author houyi
 */
@AutoConfigureAfter(GroupBoardCenter.class)
public class MessageCenterAutoConfig {

    /**
     * 创建DefaultMessageCenter
     */
    @Bean
    @ConditionalOnMissingBean(MessageCenter.class)
    public MessageCenter defaultMessageCenter(GroupBoardCenter groupBoardCenter, Spanner spanner, Sender sender) {
        return new DefaultMessageCenter(groupBoardCenter, spanner, sender);
    }


}
