package cn.nemo.springframework.context.annotation;

import java.lang.annotation.*;

/**
 * @author zkl
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Scope {
	String value() default "singleton";
}