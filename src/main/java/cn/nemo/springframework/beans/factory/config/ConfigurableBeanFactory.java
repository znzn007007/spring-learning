package cn.nemo.springframework.beans.factory.config;

import cn.nemo.springframework.beans.factory.HierarchicalBeanFactory;

/**
 * Configuration interface to be implemented by most bean factories. Provides
 * facilities to configure a bean factory, in addition to the bean factory
 * client methods in the {@link cn.nemo.springframework.beans.factory.BeanFactory}
 * interface.
 */
public interface ConfigurableBeanFactory extends HierarchicalBeanFactory, SingletonBeanRegistry {

	String SCOPE_SINGLETON = "singleton";

	String SCOPE_PROTOTYPE = "prototype";

	void addBeanPostProcessor(BeanPostProcessor beanPostProcessor);
}