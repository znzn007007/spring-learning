package cn.nemo.springframework.beans.factory.support;

import cn.hutool.core.bean.BeanUtil;
import cn.nemo.springframework.beans.BeansException;
import cn.nemo.springframework.beans.PropertyValue;
import cn.nemo.springframework.beans.PropertyValues;
import cn.nemo.springframework.beans.factory.config.AutowireCapableBeanFactory;
import cn.nemo.springframework.beans.factory.config.BeanDefinition;
import cn.nemo.springframework.beans.factory.config.BeanPostProcessor;
import cn.nemo.springframework.beans.factory.config.BeanReference;

import java.lang.reflect.Constructor;

/**
 * @author zkl
 */
public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory implements AutowireCapableBeanFactory {
	private InstantiationStrategy instantiationStrategy = new CglibSubclassingInstantiationStrategy();

	@Override
	protected Object createBean(String beanName, BeanDefinition beanDefinition, Object[] args) throws BeansException {
		Object bean = null;
		try {
			bean = createBeanInstance(beanName, beanDefinition, args);
			applyPropertyValues(beanName, bean, beanDefinition);
			// 执行 Bean 的初始化方法和 BeanPostProcessor 的前置和后置处理方法
			bean = initializeBean(beanName, bean, beanDefinition);
		} catch (BeansException e) {
			throw new BeansException("Instantiation of bean failed", e);
		}
		addSingleton(beanName, bean);
		return bean;
	}

	private Object initializeBean(String beanName, Object bean, BeanDefinition beanDefinition) throws BeansException {

		// 1. 执行 BeanPostProcessor Before 处理
		Object wrappedBean = applyBeanPostProcessorsBeforeInitialization(bean, beanName);

		// 待完成内容：invokeInitMethods(beanName, wrappedBean, beanDefinition);
		invokeInitMethods(beanName, wrappedBean, beanDefinition);

		// 2. 执行 BeanPostProcessor After 处理
		wrappedBean = applyBeanPostProcessorsAfterInitialization(bean, beanName);
		return wrappedBean;
	}

	@Override
	public Object applyBeanPostProcessorsAfterInitialization(Object bean, String beanName) throws BeansException {
		Object result = bean;
		for (BeanPostProcessor processor : getBeanPostProcessors()) {
			Object current = processor.postProcessAfterInitialization(bean, beanName);
			if (current == null) {
				return result;
			}
			result = current;
		}
		return result;
	}

	private void invokeInitMethods(String beanName, Object wrappedBean, BeanDefinition beanDefinition) {

	}

	@Override
	public Object applyBeanPostProcessorsBeforeInitialization(Object bean, String beanName) throws BeansException {
		Object result = bean;
		for (BeanPostProcessor processor : getBeanPostProcessors()) {
			Object current = processor.postProcessBeforeInitialization(bean, beanName);
			if (current == null) {
				return result;
			}
			result = current;
		}
		return result;
	}

	private Object createBeanInstance(String beanName, BeanDefinition beanDefinition, Object[] args) throws BeansException {
		Constructor<?> toUse = null;
		Class<?> beanClass = beanDefinition.getBeanClass();
		for (Constructor<?> constructor : beanClass.getConstructors()) {
			if (args != null && constructor.getParameterTypes().length == args.length) {
				toUse = constructor;
				break;
			}
		}
		return instantiationStrategy.instantiate(beanDefinition, beanName, toUse, args);
	}

	private void applyPropertyValues(String beanName, Object bean, BeanDefinition beanDefinition) throws BeansException {

		try {
			PropertyValues propertyValues = beanDefinition.getPropertyValues();
			for (PropertyValue propertyValue : propertyValues.getPropertyValues()) {
				String name = propertyValue.getName();
				Object value = propertyValue.getValue();
				if (value instanceof BeanReference) {
					BeanReference beanReference = (BeanReference) value;
					value = getBean(beanReference.getBeanName());
				}
				BeanUtil.setFieldValue(bean, name, value);
			}
		} catch (Exception e) {
			throw new BeansException("Error setting property values：" + beanName);
		}
	}

	public InstantiationStrategy getInstantiationStrategy() {
		return instantiationStrategy;
	}

	public void setInstantiationStrategy(InstantiationStrategy instantiationStrategy) {
		this.instantiationStrategy = instantiationStrategy;
	}
}
