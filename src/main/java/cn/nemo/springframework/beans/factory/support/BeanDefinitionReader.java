package cn.nemo.springframework.beans.factory.support;

import cn.nemo.springframework.beans.BeansException;
import cn.nemo.springframework.core.io.Resource;
import cn.nemo.springframework.core.io.ResourceLoader;

/**
 * @author zkl
 */
public interface BeanDefinitionReader {

	BeanDefinitionRegistry getRegistry();

	ResourceLoader getResourceLoader();

	void loadBeanDefinitions(Resource resource) throws BeansException;

	void loadBeanDefinitions(Resource... resources) throws BeansException;

	void loadBeanDefinitions(String location) throws BeansException;

	void loadBeanDefinitions(String... location) throws BeansException;
}
