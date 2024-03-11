package cn.nemo.springframework.beans.factory.support;

import cn.nemo.springframework.beans.factory.config.BeanDefinition;

/**
 * @author zkl
 */
public interface BeanDefinitionRegistry {
	void registerBeanDefinition(String beanName, BeanDefinition beanDefinition);
}