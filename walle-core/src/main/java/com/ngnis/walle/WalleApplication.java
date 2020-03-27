package com.ngnis.walle;

import com.ngnis.walle.config.WalleAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * Web应用启动入口
 * 在 spring.factories 文件中指定了 {@link WalleAutoConfiguration} 的自动装配类
 * 可以通过 {@link EnableWalle} 装配:
 * <ul>
 * <li>{@link com.ngnis.walle.center.account.AccountCenter}</li>
 * <li>{@link com.ngnis.walle.center.board.GroupBoardCenter}</li>
 * </ul>
 * <p>
 * 或者获取通过 {@link EnableAccountCenter} 来单独装配：
 * <ul>
 * <li>{@link com.ngnis.walle.center.account.AccountCenter}</li>
 * </ul>
 * <p>
 * 或者获取通过 {@link EnableBoardCenter} 来单独装配：
 * <ul>
 * <li>{@link com.ngnis.walle.center.board.GroupBoardCenter}</li>
 * </ul>
 *
 * @author houyi.wh
 * @since 2018-09-09
 */
@MapperScan("com.ngnis.walle.datasource.db")
@EnableAspectJAutoProxy
@EnableWalle
@SpringBootApplication
public class WalleApplication {

    public static void main(String[] args) {
        SpringApplication.run(WalleApplication.class, args);
    }

}
