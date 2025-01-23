package com.sky.annotation;

import com.sky.enumeration.OperationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于公共字段的自定义注解
 */
@Target(ElementType.METHOD) // 只能用在method上
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoFill {
    // 数据库类型 Update Insert
    OperationType value();
}
