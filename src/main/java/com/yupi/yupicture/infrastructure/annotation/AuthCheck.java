package com.yupi.yupicture.infrastructure.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD) // 注解的生效范围 针对方法的注解
@Retention(RetentionPolicy.RUNTIME) // 在运行时生效
public @interface AuthCheck {

    /**
     * 必须有某个角色
     */
    String mustRole() default "";
}
