package cn.nemo.springframework.context.annotation;

import java.lang.annotation.*;

/**
 * @author zkl
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Component {

	String value() default "";
}