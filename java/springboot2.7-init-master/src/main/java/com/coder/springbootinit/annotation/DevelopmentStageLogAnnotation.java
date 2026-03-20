package com.coder.springbootinit.annotation;

import com.coder.springbootinit.model.enums.DevelopmentStageOperationEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 发展阶段操作日志注解
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DevelopmentStageLogAnnotation {

    /**
     * 操作类型
     */
    DevelopmentStageOperationEnum operationType();

    /**
     * 操作描述
     */
    String description() default "";
}
