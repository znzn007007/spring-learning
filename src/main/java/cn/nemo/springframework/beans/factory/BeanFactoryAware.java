package cn.nemo.springframework.beans.factory;

import cn.nemo.springframework.beans.BeansException;

/**
 * 实现此接口，既能感知到所属的 BeanFactory
 *
 * @author zkl
 */
public interface BeanFactoryAware extends Aware {

	void setBeanFactory(BeanFactory beanFactory) throws BeansException;
}