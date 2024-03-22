package cn.nemo.springframework.aop;

import java.lang.reflect.Method;

/**
 * @author zkl
 */
public interface MethodMatcher {
	/**
	 * Perform static checking whether the given method matches. If this
	 *
	 * @return whether this method matches statically
	 */
	boolean matches(Method method, Class<?> targetClass);
}
