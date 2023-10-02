package team.aphroraletters.library.annotation;

import java.lang.annotation.*;

/**
 * 日志记录注解
 */
@Target(ElementType.METHOD) // 注解作用与方法上
@Retention(RetentionPolicy.RUNTIME) // 注解在运行时阶段执行
@Documented
public @interface OperateLog {
    String operateDesc() default "";  // 操作说明
}
