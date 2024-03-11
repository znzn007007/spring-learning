package cn.nemo.springframework.beans.factory;

import cn.nemo.springframework.beans.BeansException;

/**
 * Bean 对象的工厂
 *
 * @author zkl
 */
public interface BeanFactory {

	Object getBean(String name) throws BeansException;

	Object getBean(String name, Object... args) throws BeansException;
}
