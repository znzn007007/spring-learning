package cn.nemo.springframework.beans.factory.config;

import cn.nemo.springframework.beans.BeansException;
import cn.nemo.springframework.beans.PropertyValues;

/**
 * @author zkl
 */
public interface InstantiationAwareBeanPostProcessor extends BeanPostProcessor {

	/**
	 * Apply this BeanPostProcessor <i>before the target bean gets instantiated</i>.
	 * The returned bean object may be a proxy to use instead of the target bean,
	 * effectively suppressing default instantiation of the target bean.
	 * <p>
	 * 在 Bean 对象实例化之前，执行此方法
	 *
	 * @param beanClass
	 * @param beanName
	 * @return
	 * @throws BeansException
	 */
	Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException;

	/**
	 * Perform operations after the bean has been instantiated, via a constructor or factory method,
	 * but before Spring property population (from explicit properties or autowiring) occurs.
	 * <p>This is the ideal callback for performing field injection on the given bean instance.
	 * See Spring's own {@link cn.nemo.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor}
	 * for a typical example.
	 * <p>
	 * 这个方法在Bean完成初始化之后被调用。它允许你对已经初始化好的Bean进行最后的修改或定制。
	 *
	 * @param bean
	 * @param beanName
	 * @return
	 * @throws BeansException
	 */
	boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException;

	/**
	 * Post-process the given property values before the factory applies them
	 * to the given bean. Allows for checking whether all dependencies have been
	 * satisfied, for example based on a "Required" annotation on bean property setters.
	 * <p>
	 * 在 Bean 对象实例化完成后，设置属性操作之前执行此方法
	 *
	 * @param pvs
	 * @param bean
	 * @param beanName
	 * @return
	 * @throws BeansException
	 */
	PropertyValues postProcessPropertyValues(PropertyValues pvs, Object bean, String beanName) throws BeansException;

	/**
	 * 在 Spring 中由 SmartInstantiationAwareBeanPostProcessor#getEarlyBeanReference 提供
	 * @param bean
	 * @param beanName
	 * @return
	 */
	default Object getEarlyBeanReference(Object bean, String beanName) {
		return bean;
	}

}