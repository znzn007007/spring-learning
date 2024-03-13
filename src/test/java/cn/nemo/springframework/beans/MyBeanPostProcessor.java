package cn.nemo.springframework.beans;

import cn.nemo.springframework.beans.factory.config.BeanPostProcessor;

/**
 * @author zkl
 */
public class MyBeanPostProcessor implements BeanPostProcessor {

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		System.out.println("postProcessBeforeInitialization");
		if ("userService".equals(beanName)) {
			UserService userService = (UserService) bean;
			userService.setLocation("改为：北京");
		}
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		System.out.println("postProcessAfterInitialization");
		return bean;
	}
}
