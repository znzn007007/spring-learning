package cn.nemo.springframework.beans.factory.support;

import cn.nemo.springframework.core.io.DefaultResourceLoader;
import cn.nemo.springframework.core.io.ResourceLoader;

/**
 * @author zkl
 */
public abstract class AbstractBeanDefinitionReader implements BeanDefinitionReader {

	private final BeanDefinitionRegistry registry;

	private ResourceLoader resourceLoader;

	public AbstractBeanDefinitionReader(BeanDefinitionRegistry registry) {
		this.registry = registry;
		this.resourceLoader = new DefaultResourceLoader();
	}

	public AbstractBeanDefinitionReader(BeanDefinitionRegistry registry, ResourceLoader resourceLoader) {
		this.registry = registry;
		this.resourceLoader = resourceLoader;
	}

	@Override
	public BeanDefinitionRegistry getRegistry() {
		return this.registry;
	}

	@Override
	public ResourceLoader getResourceLoader() {
		return this.resourceLoader;
	}
}
