package com.ngnis.walle;

import com.ngnis.walle.config.AccountCenterAutoConfig;
import com.ngnis.walle.config.GroupBoardCenterAutoConfig;
import com.ngnis.walle.config.MessageCenterAutoConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 通过该注解开启所有
 *
 * @author houyi.wh
 * @since 2018-09-09
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({
        AccountCenterAutoConfig.class,
        GroupBoardCenterAutoConfig.class,
        MessageCenterAutoConfig.class
})
public @interface EnableWalle {

}
