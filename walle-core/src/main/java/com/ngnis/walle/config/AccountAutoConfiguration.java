package com.ngnis.walle.config;

import com.ngnis.walle.center.account.AccountCenter;
import com.ngnis.walle.service.account.DefaultAccountCenter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * @author houyi
 */
public class AccountAutoConfiguration {

    /**
     * 创建DefaultAccountCenter
     */
    @Bean
    @ConditionalOnMissingBean(AccountCenter.class)
    public AccountCenter defaultAccountCenter() {
        return new DefaultAccountCenter();
    }


}
