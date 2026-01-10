package com.jsdc.lsoa.log;


import java.lang.annotation.*;

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LogInfo {

    /**
     * 操作类型
     * 例如：修改
     */
    String type() default "";


    /**
     * 模块名称
     */
    String mode() default "";

}
