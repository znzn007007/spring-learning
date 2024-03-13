package cn.nemo.springframework.context.support;

import cn.nemo.springframework.beans.BeansException;
import cn.nemo.springframework.beans.factory.ConfigurableListableBeanFactory;
import cn.nemo.springframework.beans.factory.support.DefaultListableBeanFactory;

/**
 * @author zkl
 */
public abstract class AbstractRefreshableApplicationContext extends AbstractApplicationContext {
	private DefaultListableBeanFactory beanFactory;

	@Override
	protected void refreshBeanFactory() throws BeansException {
		DefaultListableBeanFactory beanFactory = createBeanFactory();
		loadBeanDefinitions(beanFactory);
		this.beanFactory = beanFactory;
	}

	private DefaultListableBeanFactory createBeanFactory() {
		return new DefaultListableBeanFactory();
	}

	protected abstract void loadBeanDefinitions(DefaultListableBeanFactory beanFactory) throws BeansException;

	@Override
	protected ConfigurableListableBeanFactory getBeanFactory() {
		return beanFactory;
	}
}
