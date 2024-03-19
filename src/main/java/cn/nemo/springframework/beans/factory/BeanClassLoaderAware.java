package cn.nemo.springframework.beans.factory;

/**
 * 实现此接口，既能感知到所属的 ClassLoader
 *
 * @author zkl
 */
public interface BeanClassLoaderAware extends Aware {
	void setBeanClassLoader(ClassLoader classLoader);
}
