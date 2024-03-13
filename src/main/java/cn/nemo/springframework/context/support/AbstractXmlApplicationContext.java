package cn.nemo.springframework.context.support;

import cn.nemo.springframework.beans.BeansException;
import cn.nemo.springframework.beans.factory.support.DefaultListableBeanFactory;
import cn.nemo.springframework.beans.factory.xml.XmlBeanDefinitionReader;

/**
 * @author zkl
 */
public abstract class AbstractXmlApplicationContext extends AbstractRefreshableApplicationContext {
	@Override
	protected void loadBeanDefinitions(DefaultListableBeanFactory beanFactory) throws BeansException {
		XmlBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(beanFactory, this);
		String[] configLocations = getConfigLocations();
		if (null != configLocations) {
			beanDefinitionReader.loadBeanDefinitions(configLocations);
		}
	}

	protected abstract String[] getConfigLocations();
}
