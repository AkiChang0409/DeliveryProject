package com.sky.annotation;

import com.sky.enumeration.OperationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义注解，用于标识需要进行自动填充的方法
 * 该注解可以用在方法上，并且在运行时保留
 */
@Target(ElementType.METHOD)  // 指定该注解只能用于方法上
@Retention(RetentionPolicy.RUNTIME)  // 指定该注解在运行时仍然保留
public @interface AutoFill {
    /**
     * 操作类型，用于标识是插入还是更新操作
     * @return 操作类型
     */
    OperationType value();
}
