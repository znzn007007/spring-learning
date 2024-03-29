package cn.nemo.springframework.aop;

/**
 * @author zkl
 */
public interface Pointcut {
	/**
	 * Return the ClassFilter for this pointcut.
	 *
	 * @return the ClassFilter (never <code>null</code>)
	 */
	ClassFilter getClassFilter();

	/**
	 * Return the MethodMatcher for this pointcut.
	 *
	 * @return the MethodMatcher (never <code>null</code>)
	 */
	MethodMatcher getMethodMatcher();
}
