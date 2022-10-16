package com.marxist.leftwing_community.util;

import java.lang.annotation.*;

/**
 * 注解类：操作日志
 */
@Target(ElementType.METHOD) //注解放置的目标位置,METHOD是可注解在方法级别上
@Retention(RetentionPolicy.RUNTIME) //注解在哪个阶段执行
@Documented
public @interface OperateLog {
    String operateDesc() default "";  // 操作说明
}
