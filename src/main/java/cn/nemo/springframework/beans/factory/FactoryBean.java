package cn.nemo.springframework.beans.factory;

/**
 * @author zkl
 */
public interface FactoryBean<T> {
	T getObejct() throws Exception;

	Class<?> getObjectType();

	boolean isSingleton();
}