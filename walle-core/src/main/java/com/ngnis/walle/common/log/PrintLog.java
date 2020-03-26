package com.ngnis.walle.common.log;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface PrintLog {

    /**
     * 方法名
     */
    String methodName() default "";
    
}