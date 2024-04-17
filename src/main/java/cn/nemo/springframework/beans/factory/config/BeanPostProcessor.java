package cn.nemo.springframework.beans.factory.config;

import cn.nemo.springframework.beans.BeansException;

/**
 * @author zkl
 */
public interface BeanPostProcessor {

	/**
	 * 这个方法在Bean实例化并且属性设置完成之后,但在任何初始化回调(如InitializingBean的afterPropertiesSet()方法或自定义的init方法)之前被调用。
	 * 这个方法允许你对Bean进行自定义的初始化逻辑,比如修改Bean的属性值。
	 */
	Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException;

	/**
	 * 这个方法在Bean实例化之后,但在任何初始化回调之前被调用。它允许你对刚刚实例化的Bean进行一些额外的处理或者配置。
	 */
	Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException;
}