package cn.nemo.springframework.beans.factory;

/**
 * bean初始化
 *
 * @author zkl
 */
public interface InitializingBean {
	/**
	 * Bean 处理了属性填充后调用
	 *
	 * @throws Exception
	 */
	void afterPropertiesSet() throws Exception;
}
