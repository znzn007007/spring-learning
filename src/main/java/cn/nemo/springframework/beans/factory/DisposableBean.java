package cn.nemo.springframework.beans.factory;

/**
 * 销毁bean
 *
 * @author zkl
 */
public interface DisposableBean {
	/**
	 * Bean 销毁调用
	 *
	 * @throws Exception
	 */
	void destroy() throws Exception;
}