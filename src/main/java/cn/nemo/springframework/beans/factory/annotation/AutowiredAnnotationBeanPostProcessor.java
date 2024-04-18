package cn.nemo.springframework.beans.factory.annotation;

import cn.hutool.core.bean.BeanUtil;
import cn.nemo.springframework.beans.BeansException;
import cn.nemo.springframework.beans.PropertyValues;
import cn.nemo.springframework.beans.factory.BeanFactory;
import cn.nemo.springframework.beans.factory.BeanFactoryAware;
import cn.nemo.springframework.beans.factory.ConfigurableListableBeanFactory;
import cn.nemo.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import cn.nemo.springframework.util.ClassUtils;

import java.lang.reflect.Field;

/**
 * @author zkl
 */
public class AutowiredAnnotationBeanPostProcessor implements InstantiationAwareBeanPostProcessor, BeanFactoryAware {
	private ConfigurableListableBeanFactory beanFactory;

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = (ConfigurableListableBeanFactory) beanFactory;
	}

	@Override
	public PropertyValues postProcessPropertyValues(PropertyValues pvs, Object bean, String beanName) throws BeansException {

		Class<?> clazz = bean.getClass();
		clazz = ClassUtils.isCglibProxyClass(clazz) ? clazz.getSuperclass() : clazz;
		Field[] declaredFields = clazz.getDeclaredFields();
		for (Field field : declaredFields) {
			handleValue(bean, field);
			Autowired autowired = field.getAnnotation(Autowired.class);
			if (autowired != null) {
				Class<?> fieldType = field.getType();
				Qualifier qualifier = field.getAnnotation(Qualifier.class);
				Object dependentBean;
				if (qualifier != null) {
					String dependentName = qualifier.value();
					dependentBean = beanFactory.getBean(dependentName, fieldType);
				} else {
					dependentBean = beanFactory.getBean(fieldType);
				}
				BeanUtil.setFieldValue(bean, field.getName(), dependentBean);
			}
		}

		return pvs;
	}

	private void handleValue(Object bean, Field field) {
		Value annotation = field.getAnnotation(Value.class);
		if (annotation != null) {
			String value = annotation.value();
			value = beanFactory.resolveEmbeddedValue(value);
			BeanUtil.setFieldValue(bean, field.getName(), value);
		}
	}


	@Override
	public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
		return null;
	}

	@Override
	public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
		return true;
	}

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		return null;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		return null;
	}
}
