package com.ngnis.walle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * Web应用启动入口
 * 可以通过 {@link EnableWalle} 注解触发 {@link WalleAutoConfiguration} 的类
 * 但是也可以不使用，因为已经在 spring.factories 文件中指定了
 *
 * @author houyi.wh
 * @since 2018-09-09
 */
@MapperScan("com.ngnis.walle.datasource.db")
@ComponentScan(basePackages = "com.ngnis.walle")
@SpringBootApplication
public class WalleApplication {

    public static void main(String[] args) {
        SpringApplication.run(WalleApplication.class, args);
    }

}
