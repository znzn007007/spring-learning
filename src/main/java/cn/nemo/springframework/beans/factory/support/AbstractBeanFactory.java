package cn.nemo.springframework.beans.factory.support;

import cn.nemo.springframework.beans.BeansException;
import cn.nemo.springframework.beans.factory.FactoryBean;
import cn.nemo.springframework.beans.factory.config.BeanDefinition;
import cn.nemo.springframework.beans.factory.config.BeanPostProcessor;
import cn.nemo.springframework.beans.factory.config.ConfigurableBeanFactory;
import cn.nemo.springframework.util.ClassUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zkl
 */
public abstract class AbstractBeanFactory extends FactoryBeanRegistrySupport implements ConfigurableBeanFactory {

	/**
	 * ClassLoader to resolve bean class names with, if necessary
	 */
	private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();

	/**
	 * BeanPostProcessors to apply in createBean
	 */
	private final List<BeanPostProcessor> beanPostProcessors = new ArrayList<>();

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

	private <T> T doGetBean(String name, Object[] args) throws BeansException {
		Object bean = getSingleton(name);
		if (bean != null) {
			// 如果是 FactoryBean，则需要调用 FactoryBean#getObject
			return (T) getObjectForBeanInstance(bean, name);
		}
		BeanDefinition beanDefinition = getBeanDefinition(name);
		bean = createBean(name, beanDefinition, args);
		return (T) getObjectForBeanInstance(bean, name);
	}

	private Object getObjectForBeanInstance(Object bean, String name) {
		Object result = bean;
		if (bean instanceof FactoryBean<?>) {
			result = getCachedObjectForFactoryBean(name);
			if (result == null) {
				FactoryBean<?> factoryBean = (FactoryBean<?>) bean;
				result = getObjectFromFactoryBean(factoryBean, name);
			}
		}
		return result;
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

	public ClassLoader getBeanClassLoader() {
		return this.beanClassLoader;
	}
}
