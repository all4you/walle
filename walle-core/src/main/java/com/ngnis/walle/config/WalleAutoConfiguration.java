package com.ngnis.walle.config;

import com.ngnis.walle.core.board.DataBaseBoardFactory;
import com.ngnis.walle.core.board.GroupBoardFactory;
import com.ngnis.walle.core.board.MemoryBoardFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * 自动配置Bean的对象
 *
 * @author houyi.wh
 * @since 2018-09-09
 */
@Slf4j
@Configuration
public class WalleAutoConfiguration {


    /**
     * 全局跨域配置方案，通过CorsFilter过滤器实现∂
     * 关于跨域问题总结在这里：
     * https://github.com/all4you/lememo/blob/master/java/CORS/what_cause_cors.md
     */
    @Bean
    public FilterRegistrationBean corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("http://127.0.0.1:9528");
        config.addAllowedOrigin("http://localhost:9528");
        config.addAllowedMethod("*");
        config.addAllowedHeader("*");
        UrlBasedCorsConfigurationSource configSource = new UrlBasedCorsConfigurationSource();
        configSource.registerCorsConfiguration("/walle/**", config);
        FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(configSource));
        bean.setOrder(0);
        return bean;
    }


    @Bean
    @ConditionalOnMissingBean(RestTemplate.class)
    public RestTemplate restTemplate() {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(5000);
        requestFactory.setReadTimeout(5000);
        return new RestTemplate(requestFactory);
    }

    /**
     * 默认创建MemoryBoardFactory
     */
    @Bean
    @ConditionalOnMissingBean(GroupBoardFactory.class)
    @ConditionalOnProperty(name = "walle.common.board-factory", havingValue = "memory", matchIfMissing = true)
    public GroupBoardFactory memoryBoardFactory() {
        return new MemoryBoardFactory();
    }

    /**
     * 创建DataBaseBoardFactory
     */
    @Bean
    @ConditionalOnMissingBean(GroupBoardFactory.class)
    @ConditionalOnProperty(name = "walle.common.board-factory", havingValue = "db")
    public GroupBoardFactory databaseBoardFactory() {
        return new DataBaseBoardFactory();
    }


}
