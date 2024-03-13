package cn.nemo.springframework.beans;

import cn.nemo.springframework.beans.factory.ConfigurableListableBeanFactory;
import cn.nemo.springframework.beans.factory.config.BeanDefinition;
import cn.nemo.springframework.beans.factory.config.BeanFactoryPostProcessor;

/**
 * @author zkl
 */
public class MyBeanFactoryPostProcessor implements BeanFactoryPostProcessor {
	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		System.out.println("postProcessBeanFactory");
		BeanDefinition beanDefinition = beanFactory.getBeanDefinition("userService");
		PropertyValues propertyValues = beanDefinition.getPropertyValues();

		propertyValues.addPropertyValue(new PropertyValue("company", "改为：字节跳动"));
	}
}
