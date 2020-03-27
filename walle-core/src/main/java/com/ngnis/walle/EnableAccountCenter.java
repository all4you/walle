package com.ngnis.walle;

import com.ngnis.walle.config.AccountAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 开启并注入默认的AccountCenter
 *
 * @author houyi.wh
 * @since 2018-09-09
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({AccountAutoConfiguration.class})
public @interface EnableAccountCenter {

}
