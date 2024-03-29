package cn.nemo.springframework.beans.factory.config;

/**
 * 单例注册
 *
 * @author zkl
 */
public interface SingletonBeanRegistry {

	Object getSingleton(String beanName);

	void registerSingleton(String beanName, Object singletonObject);

	/**
	 * 销毁单例对象
	 */
	void destroySingletons();
}
