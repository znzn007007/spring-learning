package cn.nemo.springframework.beans.factory.support;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.nemo.springframework.beans.BeansException;
import cn.nemo.springframework.beans.PropertyValue;
import cn.nemo.springframework.beans.PropertyValues;
import cn.nemo.springframework.beans.factory.*;
import cn.nemo.springframework.beans.factory.config.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * @author zkl
 */
public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory implements AutowireCapableBeanFactory {

	private InstantiationStrategy instantiationStrategy = new CglibSubclassingInstantiationStrategy();

	@Override
	protected Object createBean(String beanName, BeanDefinition beanDefinition, Object[] args) throws BeansException {
		Object bean = null;
		try {
			// 判断是否返回代理 Bean 对象
			bean = resolveBeforeInstantiation(beanName, beanDefinition);
			if (null != bean) {
				return bean;
			}

			bean = createBeanInstance(beanName, beanDefinition, args);

			// 处理循环依赖，将实例化后的Bean对象提前放入缓存中暴露出来
			if (beanDefinition.isSingleton()) {
				Object finalBean = bean;
				addSingletonFactory(beanName, () -> getEarlyBeanReference(beanName, beanDefinition, finalBean));
			}
			// 实例化后判断
			boolean continueWithPropertyPopulation = applyBeanPostProcessorsAfterInstantiation(beanName, bean);
			if (!continueWithPropertyPopulation) {
				return bean;
			}

			// 在设置 Bean 属性之前，允许 BeanPostProcessor 修改属性值
			applyBeanPostProcessorsBeforeApplyingPropertyValues(beanName, bean, beanDefinition);
			// 给 Bean 填充属性
			applyPropertyValues(beanName, bean, beanDefinition);
			// 执行 Bean 的初始化方法和 BeanPostProcessor 的前置和后置处理方法
			bean = initializeBean(beanName, bean, beanDefinition);
		} catch (BeansException e) {
			throw new BeansException("Instantiation of bean failed: " + beanName, e);
		}
		// 注册实现了 DisposableBean 接口的 Bean 对象
		registerDisposableBeanIfNecessary(beanName, bean, beanDefinition);

		Object exposedObject = bean;
		if (beanDefinition.isSingleton()) {
			// 获取代理对象
			exposedObject = getSingleton(beanName);
			registerSingleton(beanName, exposedObject);
		}
		return exposedObject;
	}

	/**
	 * Bean 实例化后对于返回 false 的对象，不在执行后续设置 Bean 对象属性的操作
	 *
	 * @param beanName
	 * @param bean
	 * @return
	 */
	private boolean applyBeanPostProcessorsAfterInstantiation(String beanName, Object bean) {
		boolean continueWithPropertyPopulation = true;
		for (BeanPostProcessor beanPostProcessor : getBeanPostProcessors()) {
			if (beanPostProcessor instanceof InstantiationAwareBeanPostProcessor) {
				boolean result = ((InstantiationAwareBeanPostProcessor) beanPostProcessor).postProcessAfterInstantiation(bean, beanName);
				if (!result) {
					continueWithPropertyPopulation = false;
					break;
				}
			}
		}
		return continueWithPropertyPopulation;
	}

	private Object getEarlyBeanReference(String beanName, BeanDefinition beanDefinition, Object bean) {
		Object result = bean;
		for (BeanPostProcessor beanPostProcessor : getBeanPostProcessors()) {
			if (beanPostProcessor instanceof InstantiationAwareBeanPostProcessor) {
				result = ((InstantiationAwareBeanPostProcessor) beanPostProcessor).getEarlyBeanReference(bean, beanName);
				if (null != result) {
					return result;
				}
			}
		}
		return result;
	}

	private void applyBeanPostProcessorsBeforeApplyingPropertyValues(String beanName, Object bean, BeanDefinition beanDefinition) {

		for (BeanPostProcessor beanPostProcessor : getBeanPostProcessors()) {
			if (beanPostProcessor instanceof InstantiationAwareBeanPostProcessor) {
				PropertyValues pvs = ((InstantiationAwareBeanPostProcessor) beanPostProcessor).postProcessPropertyValues(beanDefinition.getPropertyValues(), bean, beanName);
				if (pvs != null) {
					for (PropertyValue propertyValue : pvs.getPropertyValues()) {
						beanDefinition.getPropertyValues().addPropertyValue(propertyValue);
					}
				}
			}
		}
	}

	private Object resolveBeforeInstantiation(String beanName, BeanDefinition beanDefinition) {
		Object bean = applyBeanPostProcessorsBeforeInstantiation(beanDefinition.getBeanClass(), beanName);
		if (null != bean) {
			bean = applyBeanPostProcessorsAfterInitialization(bean, beanName);
		}
		return bean;
	}

	private Object applyBeanPostProcessorsBeforeInstantiation(Class<?> beanClass, String beanName) {
		for (BeanPostProcessor processor : getBeanPostProcessors()) {
			if (processor instanceof InstantiationAwareBeanPostProcessor) {
				return ((InstantiationAwareBeanPostProcessor) processor).postProcessBeforeInstantiation(beanClass, beanName);
			}
		}
		return null;
	}

	private void registerDisposableBeanIfNecessary(String beanName, Object bean, BeanDefinition beanDefinition) {
		// 非 Singleton 类型的 Bean 不执行销毁方法
		if (!beanDefinition.isSingleton()) {
			return;
		}
		if (bean instanceof DisposableBean || StrUtil.isNotEmpty(beanDefinition.getDestroyMethodName())) {
			registerDisposableBean(beanName, new DisposableBeanAdapter(bean, beanName, beanDefinition));
		}
	}

	private Object initializeBean(String beanName, Object bean, BeanDefinition beanDefinition) throws BeansException {

		// invokeAwareMethods
		if (bean instanceof Aware) {
			if (bean instanceof BeanFactoryAware) {
				((BeanFactoryAware) bean).setBeanFactory(this);
			}
			if (bean instanceof BeanClassLoaderAware) {
				((BeanClassLoaderAware) bean).setBeanClassLoader(getBeanClassLoader());
			}
			if (bean instanceof BeanNameAware) {
				((BeanNameAware) bean).setBeanName(beanName);
			}
		}

		// 1. 执行 BeanPostProcessor Before 处理
		Object wrappedBean = applyBeanPostProcessorsBeforeInitialization(bean, beanName);

		// 执行 Bean 对象的初始化方法
		try {
			invokeInitMethods(beanName, wrappedBean, beanDefinition);
		} catch (Exception e) {
			throw new BeansException("Invocation of init method of bean[" + beanName + "] failed", e);
		}

		// 2. 执行 BeanPostProcessor After 处理
		wrappedBean = applyBeanPostProcessorsAfterInitialization(bean, beanName);
		return wrappedBean;
	}

	@Override
	public Object applyBeanPostProcessorsAfterInitialization(Object bean, String beanName) throws BeansException {
		Object result = bean;
		for (BeanPostProcessor processor : getBeanPostProcessors()) {
			Object current = processor.postProcessAfterInitialization(bean, beanName);
			if (current == null) {
				return result;
			}
			result = current;
		}
		return result;
	}

	private void invokeInitMethods(String beanName, Object bean, BeanDefinition beanDefinition) throws Exception {
		// 1. 实现接口 InitializingBean
		if (bean instanceof InitializingBean) {
			((InitializingBean) bean).afterPropertiesSet();
		}

		// 2. 配置信息 init-method {判断是为了避免二次执行销毁}
		String initMethodName = beanDefinition.getInitMethodName();
		if (StrUtil.isNotEmpty(initMethodName)) {
			Method initMethod = beanDefinition.getBeanClass().getMethod(initMethodName);
			initMethod.invoke(bean);
		}
	}

	@Override
	public Object applyBeanPostProcessorsBeforeInitialization(Object bean, String beanName) throws BeansException {
		Object result = bean;
		for (BeanPostProcessor processor : getBeanPostProcessors()) {
			Object current = processor.postProcessBeforeInitialization(bean, beanName);
			if (current == null) {
				return result;
			}
			result = current;
		}
		return result;
	}

	private Object createBeanInstance(String beanName, BeanDefinition beanDefinition, Object[] args) throws BeansException {
		Constructor<?> toUse = null;
		Class<?> beanClass = beanDefinition.getBeanClass();
		for (Constructor<?> constructor : beanClass.getConstructors()) {
			if (args != null && constructor.getParameterTypes().length == args.length) {
				toUse = constructor;
				break;
			}
		}
		return instantiationStrategy.instantiate(beanDefinition, beanName, toUse, args);
	}

	private void applyPropertyValues(String beanName, Object bean, BeanDefinition beanDefinition) throws BeansException {

		try {
			PropertyValues propertyValues = beanDefinition.getPropertyValues();
			for (PropertyValue propertyValue : propertyValues.getPropertyValues()) {
				String name = propertyValue.getName();
				Object value = propertyValue.getValue();
				if (value instanceof BeanReference) {
					BeanReference beanReference = (BeanReference) value;
					value = getBean(beanReference.getBeanName());
				}
				BeanUtil.setFieldValue(bean, name, value);
			}
		} catch (Exception e) {
			throw new BeansException("Error setting property values：" + beanName, e);
		}
	}

	public InstantiationStrategy getInstantiationStrategy() {
		return instantiationStrategy;
	}

	public void setInstantiationStrategy(InstantiationStrategy instantiationStrategy) {
		this.instantiationStrategy = instantiationStrategy;
	}
}
