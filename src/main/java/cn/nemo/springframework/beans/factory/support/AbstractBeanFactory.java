package cn.nemo.springframework.beans.factory.support;

import cn.nemo.springframework.beans.BeansException;
import cn.nemo.springframework.beans.factory.BeanFactory;
import cn.nemo.springframework.beans.factory.config.BeanDefinition;

/**
 * @author zkl
 */
public abstract class AbstractBeanFactory extends DefaultSingletonBeanRegistry implements BeanFactory {

	@Override
	public Object getBean(String name) throws BeansException {
		return doGetBean(name, null);
	}

	@Override
	public Object getBean(String name, Object... args) throws BeansException {
		return doGetBean(name, args);
	}

	private Object doGetBean(String name, Object[] args) throws BeansException {
		Object bean = getSingleton(name);
		if (bean != null) {
			return bean;
		}
		BeanDefinition beanDefinition = getBeanDefinition(name);
		return createBean(name, beanDefinition, args);
	}

	protected abstract BeanDefinition getBeanDefinition(String beanName) throws BeansException;

	protected abstract Object createBean(String beanName, BeanDefinition beanDefinition, Object[] args) throws BeansException;
}
