package cn.nemo.springframework.beans.factory.xml;

import cn.hutool.core.util.StrUtil;
import cn.nemo.springframework.beans.BeansException;
import cn.nemo.springframework.beans.PropertyValue;
import cn.nemo.springframework.beans.factory.config.BeanDefinition;
import cn.nemo.springframework.beans.factory.config.BeanReference;
import cn.nemo.springframework.beans.factory.support.AbstractBeanDefinitionReader;
import cn.nemo.springframework.beans.factory.support.BeanDefinitionRegistry;
import cn.nemo.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import cn.nemo.springframework.core.io.Resource;
import cn.nemo.springframework.core.io.ResourceLoader;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author zkl
 */
public class XmlBeanDefinitionReader extends AbstractBeanDefinitionReader {

	public XmlBeanDefinitionReader(BeanDefinitionRegistry registry) {
		super(registry);
	}

	public XmlBeanDefinitionReader(BeanDefinitionRegistry registry, ResourceLoader resourceLoader) {
		super(registry, resourceLoader);
	}

	@Override
	public void loadBeanDefinitions(Resource resource) throws BeansException {
		try (InputStream is = resource.getInputStream()) {
			doLoadBeanDefinitions(is);
		} catch (IOException | ClassNotFoundException | DocumentException e) {
			throw new BeansException("IOException parsing XML document from " + resource, e);
		}
	}

	@Override
	public void loadBeanDefinitions(Resource... resources) throws BeansException {
		for (Resource resource : resources) {
			loadBeanDefinitions(resource);
		}
	}

	@Override
	public void loadBeanDefinitions(String location) throws BeansException {
		ResourceLoader resourceLoader = getResourceLoader();
		Resource resource = resourceLoader.getResource(location);
		loadBeanDefinitions(resource);
	}

	@Override
	public void loadBeanDefinitions(String... locations) throws BeansException {
		for (String location : locations) {
			loadBeanDefinitions(location);
		}
	}

	private void doLoadBeanDefinitions(InputStream is) throws ClassNotFoundException, BeansException, DocumentException {
		SAXReader reader = new SAXReader();
		Document document = reader.read(is);
		Element root = document.getRootElement();

		Element componentScan = root.element("component-scan");
		if (componentScan != null) {
			String scanPath = componentScan.attributeValue("base-package");
			if (StrUtil.isEmpty(scanPath)) {
				throw new BeansException("The value of base-package attribute can not be empty or null");
			}
			scanPackage(scanPath);
		}

		List<Element> beanList = root.elements("bean");
		for (Element bean : beanList) {
			// 获取 Class
			Class<?> clazz = Class.forName(bean.attributeValue("class"));
			String beanName = generateBeanName(bean, clazz);
			BeanDefinition beanDefinition = createBeanDefinition(bean, clazz);
			if (getRegistry().containsBeanDefinition(beanName)) {
				throw new BeansException("Duplicate beanName[" + beanName + "] is not allowed");
			}
			// 注册 BeanDefinition
			getRegistry().registerBeanDefinition(beanName, beanDefinition);
		}
	}

	private void scanPackage(String scanPath) {
		String[] basePackages = StrUtil.splitToArray(scanPath, ",");
		ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(getRegistry());
		scanner.doScan(basePackages);
	}


	private String generateBeanName(Element bean, Class<?> clazz) throws BeansException {
		// 优先级 id > name
		String id = bean.attributeValue("id");
		String name = bean.attributeValue("name");
		String beanName = StrUtil.isNotEmpty(id) ? id : name;
		if (StrUtil.isEmpty(beanName)) {
			beanName = StrUtil.lowerFirst(clazz.getSimpleName());
		}
		if (getRegistry().containsBeanDefinition(beanName)) {
			throw new BeansException("Duplicate beanName[" + beanName + "] is not allowed");
		}
		return beanName;
	}

	private BeanDefinition createBeanDefinition(Element bean, Class<?> clazz) {

		// 定义Bean
		BeanDefinition beanDefinition = new BeanDefinition(clazz);
		beanDefinition.setInitMethodName(bean.attributeValue("init-method"));
		beanDefinition.setDestroyMethodName(bean.attributeValue("destroy-method"));
		String scope = bean.attributeValue("scope");
		if (StrUtil.isNotEmpty(scope)) {
			beanDefinition.setScope(scope);
		}
		for (Element property : bean.elements("property")) {
			// 解析标签：property
			String attrName = property.attributeValue("name");
			String attrValue = property.attributeValue("value");
			String attrRef = property.attributeValue("ref");
			// 获取属性值：引入对象、值对象
			Object value = StrUtil.isNotEmpty(attrRef) ? new BeanReference(attrRef) : attrValue;
			PropertyValue propertyValue = new PropertyValue(attrName, value);
			beanDefinition.getPropertyValues().addPropertyValue(propertyValue);
		}
		// 读取属性并填充
		return beanDefinition;
	}
}