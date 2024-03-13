package cn.nemo.springframework.beans.factory.support;

import cn.nemo.springframework.beans.BeansException;
import cn.nemo.springframework.beans.factory.config.BeanDefinition;
import cn.nemo.springframework.beans.factory.config.BeanPostProcessor;
import cn.nemo.springframework.beans.factory.config.ConfigurableBeanFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zkl
 */
public abstract class AbstractBeanFactory extends DefaultSingletonBeanRegistry implements ConfigurableBeanFactory {

	/**
	 * BeanPostProcessors to apply in createBean
	 */
	private final List<BeanPostProcessor> beanPostProcessors = new ArrayList<BeanPostProcessor>();

	@Override
	public Object getBean(String name) throws BeansException {
		return doGetBean(name, null);
	}

	@Override
	public Object getBean(String name, Object... args) throws BeansException {
		return doGetBean(name, args);
	}

	@Override
	public <T> T getBean(String name, Class<T> requiredType) throws BeansException {
		return (T) getBean(name);
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

	@Override
	public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor) {
		this.beanPostProcessors.remove(beanPostProcessor);
		this.beanPostProcessors.add(beanPostProcessor);
	}

	/**
	 * Return the list of BeanPostProcessors that will get applied
	 * to beans created with this factory.
	 */
	public List<BeanPostProcessor> getBeanPostProcessors() {
		return this.beanPostProcessors;
	}
}
