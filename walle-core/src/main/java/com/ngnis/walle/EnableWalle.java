package com.ngnis.walle;

import com.ngnis.walle.config.WalleAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 通过该注解开启Walle
 * @author houyi.wh
 * @since 2018-09-09
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({WalleAutoConfiguration.class})
public @interface EnableWalle {

}
