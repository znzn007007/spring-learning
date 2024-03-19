package cn.nemo.springframework.beans.factory;

import cn.nemo.springframework.beans.BeansException;
import cn.nemo.springframework.context.ApplicationContext;

/**
 * 实现此接口，既能感知到所属的 ApplicationContext
 *
 * @author zkl
 */
public interface ApplicationContextAware extends Aware {
	void setApplicationContext(ApplicationContext applicationContext) throws BeansException;
}
