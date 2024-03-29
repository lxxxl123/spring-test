package com.chen.interceptor.aspect.loggerprefix;

import java.lang.annotation.*;

/**
 * 配合{@link com.haday.qms.interceptor.holder.LoggerPrefixHolder}使用 , 可在当前线程增加日志前缀
 * @author chenwh3
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface LoggerPrefix {

    boolean showMethod() default false;
    String value() default "";
    boolean uuid() default true;
}
