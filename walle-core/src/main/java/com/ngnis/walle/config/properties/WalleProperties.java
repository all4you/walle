package com.ngnis.walle.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author houyi.wh
 * @since 2018-09-09
 */
@Data
@ConfigurationProperties("walle.common")
public class WalleProperties {

    /**
     * 使用哪种BoardFactory实现类
     * memory：MemoryBoardFactory
     * db：DataSourceBoardFactory
     */
    private String boardFactory;

}
