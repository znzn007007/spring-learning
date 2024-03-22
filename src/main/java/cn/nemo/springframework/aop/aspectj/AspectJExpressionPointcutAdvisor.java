package cn.nemo.springframework.aop.aspectj;

import cn.nemo.springframework.aop.BeforeAdvice;
import cn.nemo.springframework.aop.Pointcut;
import cn.nemo.springframework.aop.PointcutAdvisor;
import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;

/**
 * @author zkl
 */
public class AspectJExpressionPointcutAdvisor implements PointcutAdvisor {

	// 切面
	private AspectJExpressionPointcut pointcut;
	// 具体的拦截方法
	private Advice advice;
	// 表达式
	private String expression;

	public void setExpression(String expression) {
		this.expression = expression;
	}

	/**
	 * Return the advice part of this aspect. An advice may be an
	 * interceptor, a before advice, a throws advice, etc.
	 *
	 * @return the advice that should apply if the pointcut matches
	 * @see MethodInterceptor
	 * @see BeforeAdvice
	 */
	@Override
	public Advice getAdvice() {
		return advice;
	}

	public void setAdvice(Advice advice) {
		this.advice = advice;
	}

	/**
	 * Get the Pointcut that drives this advisor.
	 */
	@Override
	public Pointcut getPointcut() {
		if (null == pointcut) {
			pointcut = new AspectJExpressionPointcut(expression);
		}
		return pointcut;
	}
}