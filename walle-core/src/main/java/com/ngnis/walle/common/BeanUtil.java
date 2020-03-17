package com.ngnis.walle.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Bean的工具类
 * @author houyi.wh
 * @since 2018-09-16
 */
@Slf4j
@Component
public class BeanUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        BeanUtil.applicationContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext(){
        return applicationContext;
    }

    /**
     * 通过name获取Bean
     * @param name bean的名称
     * @return Bean
     */
    public static Object getBean(String name){
        Object bean = null;
        try {
            bean = applicationContext.getBean(name);
        } catch (Exception e){
            log.error("getBean error,with name={}",name,e);
        }
        return bean;
    }

    /**
     * 通过class获取Bean
     * @param clazz bean的类型
     * @param <T> 类型
     * @return Bean
     */
    public static <T> T getBean(Class<T> clazz){
        T bean = null;
        try {
            bean = applicationContext.getBean(clazz);
        } catch (Exception e){
            log.error("getBean error,with class={}",clazz,e);
        }
        return bean;
    }

    /**
     * 通过name,以及Clazz返回指定的Bean
     * @param name bean的名称
     * @param clazz bean的类型
     * @param <T> 类型
     * @return Bean
     */
    public static <T> T getBean(String name,Class<T> clazz){
        T bean = null;
        try {
            bean = applicationContext.getBean(name,clazz);
        } catch (Exception e){
            log.error("getBean error,with name={},class={}",name,clazz,e);
        }
        return bean;
    }

}
