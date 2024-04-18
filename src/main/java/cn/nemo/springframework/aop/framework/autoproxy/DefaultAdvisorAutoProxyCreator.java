package cn.nemo.springframework.aop.framework.autoproxy;

import cn.nemo.springframework.ProxyFactory;
import cn.nemo.springframework.aop.*;
import cn.nemo.springframework.aop.aspectj.AspectJExpressionPointcutAdvisor;
import cn.nemo.springframework.beans.BeansException;
import cn.nemo.springframework.beans.PropertyValues;
import cn.nemo.springframework.beans.factory.BeanFactory;
import cn.nemo.springframework.beans.factory.BeanFactoryAware;
import cn.nemo.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import cn.nemo.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;

import java.util.Collection;

/**
 * @author zkl
 */
public class DefaultAdvisorAutoProxyCreator implements InstantiationAwareBeanPostProcessor, BeanFactoryAware {

	private DefaultListableBeanFactory beanFactory;

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = (DefaultListableBeanFactory) beanFactory;
	}

	/**
	 * 在 Bean 对象执行初始化方法之前，执行此方法
	 *
	 * @param bean
	 * @param beanName
	 */
	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}

	/**
	 * 在 Bean 对象执行初始化方法之后，执行此方法
	 *
	 * @param bean
	 * @param beanName
	 */
	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

		Class<?> beanClass = bean.getClass();
		if (isInfrastructureClass(beanClass)) {
			return null;
		}

		Collection<AspectJExpressionPointcutAdvisor> advisors = beanFactory.getBeansOfType(AspectJExpressionPointcutAdvisor.class)
				.values();
		for (AspectJExpressionPointcutAdvisor advisor : advisors) {
			ClassFilter classFilter = advisor.getPointcut().getClassFilter();
			if (!classFilter.matches(beanClass)) {
				continue;
			}
			AdvisedSupport advisedSupport = new AdvisedSupport();
			TargetSource targetSource;
			try {
				targetSource = new TargetSource(beanClass.getDeclaredConstructor().newInstance());
			} catch (Exception e) {
				throw new BeansException("create target source failed: ", e);
			}
			advisedSupport.setTargetSource(targetSource);
			advisedSupport.setMethodInterceptor((MethodInterceptor) advisor.getAdvice());
			advisedSupport.setMethodMatcher(advisor.getPointcut().getMethodMatcher());
			advisedSupport.setProxyTargetClass(false);
			return new ProxyFactory(advisedSupport).getProxy();
		}
		return bean;
	}

	@Override
	public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
		return null;
	}

	@Override
	public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
		return false;
	}

	/**
	 * Post-process the given property values before the factory applies them
	 * to the given bean. Allows for checking whether all dependencies have been
	 * satisfied, for example based on a "Required" annotation on bean property setters.
	 * <p>
	 * 在 Bean 对象实例化完成后，设置属性操作之前执行此方法
	 *
	 * @param pvs
	 * @param bean
	 * @param beanName
	 * @return
	 * @throws BeansException
	 */
	@Override
	public PropertyValues postProcessPropertyValues(PropertyValues pvs, Object bean, String beanName) throws BeansException {
		return null;
	}

	private boolean isInfrastructureClass(Class<?> beanClass) {

		return Advice.class.isAssignableFrom(beanClass) || Pointcut.class.isAssignableFrom(beanClass) || Advisor.class.isAssignableFrom(beanClass);
	}
}
