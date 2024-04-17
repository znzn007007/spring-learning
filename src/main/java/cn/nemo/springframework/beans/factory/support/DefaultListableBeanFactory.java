package cn.nemo.springframework.beans.factory.support;

import cn.nemo.springframework.beans.BeansException;
import cn.nemo.springframework.beans.factory.ConfigurableListableBeanFactory;
import cn.nemo.springframework.beans.factory.config.BeanDefinition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zkl
 */
public class DefaultListableBeanFactory extends AbstractAutowireCapableBeanFactory implements BeanDefinitionRegistry, ConfigurableListableBeanFactory {

	private Map<String, BeanDefinition> beanDefinitionMap = new HashMap<>();

	@Override
	public BeanDefinition getBeanDefinition(String beanName) throws BeansException {
		BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
		if (beanDefinition == null) {
			throw new BeansException("No bean named '" + beanName + "' is defined");
		}
		return beanDefinition;
	}

	@Override
	public void preInstantiateSingletons() throws BeansException {
		for (String s : beanDefinitionMap.keySet()) {
			getBean(s);
		}
	}

	@Override
	public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) {
		beanDefinitionMap.put(beanName, beanDefinition);
	}

	@Override
	public boolean containsBeanDefinition(String beanName) {
		return beanDefinitionMap.containsKey(beanName);
	}

	/**
	 * 按照类型返回 Bean 实例
	 */
	@Override
	public <T> Map<String, T> getBeansOfType(Class<T> type) throws BeansException {

		Map<String, T> result = new HashMap<>();
		for (Map.Entry<String, BeanDefinition> entry : beanDefinitionMap.entrySet()) {

			String beanName = entry.getKey();
			BeanDefinition beanDefinition = entry.getValue();
			Class beanClass = beanDefinition.getBeanClass();
			if (type.isAssignableFrom(beanClass)) {
				result.put(beanName, (T) getBean(beanName));
			}
		}
		return result;
	}

	/**
	 * Return the names of all beans defined in this registry.
	 * 返回注册表中所有的Bean名称
	 */
	@Override
	public String[] getBeanDefinitionNames() {
		return beanDefinitionMap.keySet().toArray(new String[0]);
	}

	@Override
	public <T> T getBean(Class<T> requiredType) throws BeansException {
		List<String> beanNames = new ArrayList<>();
		for (Map.Entry<String, BeanDefinition> entry : beanDefinitionMap.entrySet()) {
			Class beanClass = entry.getValue().getBeanClass();
			if (requiredType.isAssignableFrom(beanClass)) {
				beanNames.add(entry.getKey());
			}
		}
		if (1 == beanNames.size()) {
			return getBean(beanNames.get(0), requiredType);
		}

		throw new BeansException(requiredType + "expected single bean but found " + beanNames.size() + ": " + beanNames);
	}
}