package cn.nemo.springframework.beans.factory.support;

import cn.nemo.springframework.beans.BeansException;
import cn.nemo.springframework.beans.factory.ApplicationContextAware;
import cn.nemo.springframework.beans.factory.config.BeanPostProcessor;
import cn.nemo.springframework.context.ApplicationContext;

/**
 * @author zkl
 */
public class ApplicationContextAwareProcessor implements BeanPostProcessor {

	private ApplicationContext applicationContext;

	public ApplicationContextAwareProcessor(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	/**
	 * 在 Bean 对象执行初始化方法之前，执行此方法
	 *
	 * @param bean
	 * @param beanName
	 */
	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		if (bean instanceof ApplicationContextAware) {
			((ApplicationContextAware) bean).setApplicationContext(applicationContext);
		}
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
		return bean;
	}
}
