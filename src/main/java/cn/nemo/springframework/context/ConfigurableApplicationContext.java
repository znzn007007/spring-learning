package cn.nemo.springframework.context;

import cn.nemo.springframework.beans.BeansException;

/**
 * @author zkl
 */
public interface ConfigurableApplicationContext extends ApplicationContext {
	/**
	 * 刷新容器
	 */
	void refresh() throws BeansException;
}