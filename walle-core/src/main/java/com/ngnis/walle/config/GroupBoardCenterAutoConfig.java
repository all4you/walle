package com.ngnis.walle.config;

import com.ngnis.walle.center.board.GroupBoardCenter;
import com.ngnis.walle.service.board.DefaultGroupBoardCenter;
import com.ngnis.walle.service.board.GroupBoardService;
import com.ngnis.walle.service.board.Spanner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;


/**
 * @author houyi
 */
public class GroupBoardCenterAutoConfig {

    /**
     * 创建DefaultGroupBoardCenter
     */
    @Bean
    @ConditionalOnMissingBean(GroupBoardCenter.class)
    public GroupBoardCenter defaultGroupBoardCenter(Spanner spanner, GroupBoardService boardService) {
        return new DefaultGroupBoardCenter(spanner, boardService);
    }


}
