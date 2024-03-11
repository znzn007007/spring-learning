package cn.nemo.springframework.beans.factory.config;

/**
 * @author zkl
 */
public interface SingletonBeanRegistry {
	Object getSingleton(String beanName);
}
