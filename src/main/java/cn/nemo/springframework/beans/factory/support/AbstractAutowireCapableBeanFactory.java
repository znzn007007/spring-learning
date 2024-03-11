package cn.nemo.springframework.beans.factory.support;

import cn.hutool.core.bean.BeanUtil;
import cn.nemo.springframework.beans.BeansException;
import cn.nemo.springframework.beans.PropertyValue;
import cn.nemo.springframework.beans.PropertyValues;
import cn.nemo.springframework.beans.factory.config.BeanDefinition;
import cn.nemo.springframework.beans.factory.config.BeanReference;

import java.lang.reflect.Constructor;

/**
 * @author zkl
 */
public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory {
	private InstantiationStrategy instantiationStrategy = new CglibSubclassingInstantiationStrategy();

	@Override
	protected Object createBean(String beanName, BeanDefinition beanDefinition, Object[] args) throws BeansException {
		Object bean = null;
		try {
			bean = createBeanInstance(beanName, beanDefinition, args);
			applyPropertyValues(beanName, bean, beanDefinition);
		} catch (BeansException e) {
			throw new BeansException("Instantiation of bean failed", e);
		}
		addSingleton(beanName, bean);
		return bean;
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
