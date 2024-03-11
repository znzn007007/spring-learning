package cn.nemo.springframework.beans.factory.support;

import cn.nemo.springframework.beans.BeansException;
import cn.nemo.springframework.beans.factory.config.BeanDefinition;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * @author zkl
 */
public class SimpleInstantiationStrategy implements InstantiationStrategy {
	@Override
	public Object instantiate(BeanDefinition beanDefinition, String beanName, Constructor ctor, Object[] args) throws BeansException {
		Class clazz = beanDefinition.getBeanClass();
		try {
			if (ctor != null) {
				return clazz.getDeclaredConstructor(ctor.getParameterTypes()).newInstance(args);
			} else {
				return clazz.getDeclaredConstructor().newInstance();
			}
		} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException |
		         InvocationTargetException e) {
			throw new BeansException("Failed to instantiate [" + clazz.getName() + "]", e);
		}
	}
}