package cn.nemo.springframework.context.annotation;

import cn.hutool.core.util.StrUtil;
import cn.nemo.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import cn.nemo.springframework.beans.factory.config.BeanDefinition;
import cn.nemo.springframework.beans.factory.support.BeanDefinitionRegistry;

/**
 * @author zkl
 */
public class ClassPathBeanDefinitionScanner extends ClassPathScanningCandidateComponentProvider {

	private BeanDefinitionRegistry registry;

	public ClassPathBeanDefinitionScanner(BeanDefinitionRegistry registry) {
		this.registry = registry;
	}

	public void doScan(String... basePackages) {

		for (String basePackage : basePackages) {
			for (BeanDefinition beanDefinition : findCandidateComponents(basePackage)) {
				Scope scope = beanDefinition.getBeanClass().getAnnotation(Scope.class);
				if (scope != null && StrUtil.isNotEmpty(scope.value())) {
					beanDefinition.setScope(scope.value());
				}
				registry.registerBeanDefinition(determineBeanName(beanDefinition), beanDefinition);
			}
		}

		registry.registerBeanDefinition("cn.nemo.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor", new BeanDefinition(AutowiredAnnotationBeanPostProcessor.class));
	}

	private String determineBeanName(BeanDefinition beanDefinition) {

		Class<?> beanClass = beanDefinition.getBeanClass();
		Component component = beanClass.getAnnotation(Component.class);
		String value = component.value();
		return StrUtil.isEmpty(value) ? StrUtil.lowerFirst(beanClass.getSimpleName()) : value;
	}
}
