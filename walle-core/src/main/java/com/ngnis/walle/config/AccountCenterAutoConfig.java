package com.ngnis.walle.config;

import com.ngnis.walle.center.account.AccountCenter;
import com.ngnis.walle.core.auth.TokenFactory;
import com.ngnis.walle.service.account.AccountService;
import com.ngnis.walle.service.account.DefaultAccountCenter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * @author houyi
 */
public class AccountCenterAutoConfig {

    /**
     * 创建DefaultAccountCenter
     */
    @Bean
    @ConditionalOnMissingBean(AccountCenter.class)
    public AccountCenter defaultAccountCenter(AccountService accountService, TokenFactory tokenFactory) {
        return new DefaultAccountCenter(accountService, tokenFactory);
    }


}
