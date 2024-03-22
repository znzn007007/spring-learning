package cn.nemo.springframework.context;

import cn.nemo.springframework.beans.BeansException;
import cn.nemo.springframework.beans.factory.Aware;

/**
 * 实现此接口，既能感知到所属的 ApplicationContext
 *
 * @author zkl
 */
public interface ApplicationContextAware extends Aware {
	void setApplicationContext(ApplicationContext applicationContext) throws BeansException;
}
