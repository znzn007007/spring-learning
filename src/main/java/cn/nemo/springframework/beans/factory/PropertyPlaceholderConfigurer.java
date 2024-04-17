package cn.nemo.springframework.beans.factory;

import cn.nemo.springframework.beans.BeansException;
import cn.nemo.springframework.beans.PropertyValue;
import cn.nemo.springframework.beans.PropertyValues;
import cn.nemo.springframework.beans.factory.config.BeanDefinition;
import cn.nemo.springframework.beans.factory.config.BeanFactoryPostProcessor;
import cn.nemo.springframework.core.io.DefaultResourceLoader;
import cn.nemo.springframework.core.io.Resource;
import cn.nemo.springframework.util.StringValueResolver;

import java.io.IOException;
import java.util.Properties;

/**
 * @author zkl
 */
public class PropertyPlaceholderConfigurer implements BeanFactoryPostProcessor {


	/**
	 * Default placeholder prefix: {@value}
	 */
	public static final String DEFAULT_PLACEHOLDER_PREFIX = "${";

	/**
	 * Default placeholder suffix: {@value}
	 */
	public static final String DEFAULT_PLACEHOLDER_SUFFIX = "}";

	private String location;

	/**
	 * 在所有的 BeanDefinition 加载完成后，实例化 Bean 对象之前，提供修改 BeanDefinition 属性的机制
	 *
	 * @param beanFactory
	 * @throws BeansException
	 */
	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

		try {
			DefaultResourceLoader resourceLoader = new DefaultResourceLoader();
			Resource resource = resourceLoader.getResource(location);
			// 占位符替换属性值
			Properties properties = new Properties();
			properties.load(resource.getInputStream());

			for (String beanName : beanFactory.getBeanDefinitionNames()) {

				BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanName);
				PropertyValues propertyValues = beanDefinition.getPropertyValues();
				for (PropertyValue propertyValue : propertyValues.getPropertyValues()) {
					Object value = propertyValue.getValue();
					if (value instanceof String) {
						String strVal = (String) value;
						propertyValues.addPropertyValue(new PropertyValue(propertyValue.getName(), resolvePlaceholder(strVal, properties)));
					}
				}
			}

			// 向容器中添加字符串解析器，供解析@Value注解使用
			StringValueResolver valueResolver = new PlaceholderResolvingStringValueResolver(properties);
			beanFactory.addEmbeddedValueResolver(valueResolver);

		} catch (IOException e) {
			throw new BeansException("Could not load properties", e);
		}
	}

	private String resolvePlaceholder(String strVal, Properties properties) {
		StringBuilder buffer = new StringBuilder(strVal);
		int startIndex = strVal.indexOf(DEFAULT_PLACEHOLDER_PREFIX);
		int stopIndex = strVal.indexOf(DEFAULT_PLACEHOLDER_SUFFIX);
		if (startIndex != -1 && stopIndex != -1 && startIndex < stopIndex) {
			String propKey = strVal.substring(startIndex + 2, stopIndex);
			String propVal = properties.getProperty(propKey);
			buffer.replace(startIndex, stopIndex + 1, propVal);
		}
		return buffer.toString();
	}

	public void setLocation(String location) {
		this.location = location;
	}

	private class PlaceholderResolvingStringValueResolver implements StringValueResolver {

		private final Properties properties;

		public PlaceholderResolvingStringValueResolver(Properties properties) {
			this.properties = properties;
		}

		@Override
		public String resolveStringValue(String strVal) {
			return PropertyPlaceholderConfigurer.this.resolvePlaceholder(strVal, properties);
		}
	}
}
