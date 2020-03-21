package com.ngnis.walle.config;

import com.ngnis.walle.config.properties.WalleProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 将那些通过 @ConfigurationProperties 注解修饰的类注册为 spring 中的 bean
 *
 * @author houyi
 */
@Configuration
@EnableConfigurationProperties({
        WalleProperties.class,
})
public class PropertiesConfiguration {

}
