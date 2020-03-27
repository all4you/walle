package com.ngnis.walle.config;

import com.ngnis.walle.center.board.GroupBoardCenter;
import com.ngnis.walle.service.board.DefaultGroupBoardCenter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;


/**
 * @author houyi
 */
public class BoardAutoConfiguration {

    /**
     * 创建DefaultGroupBoardCenter
     */
    @Bean
    @ConditionalOnMissingBean(GroupBoardCenter.class)
    public GroupBoardCenter defaultGroupBoardCenter() {
        return new DefaultGroupBoardCenter();
    }


}
