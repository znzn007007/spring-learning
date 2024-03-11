package cn.nemo.springframework.beans.factory.support;

import cn.nemo.springframework.beans.factory.config.SingletonBeanRegistry;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zkl
 */
public class DefaultSingletonBeanRegistry implements SingletonBeanRegistry {

	private Map<String, Object> singletonObjects = new HashMap<>();

	@Override
	public Object getSingleton(String beanName) {
		return singletonObjects.get(beanName);
	}

	public void addSingleton(String beanName, Object singletonObject) {

		singletonObjects.put(beanName, singletonObject);
	}
}
