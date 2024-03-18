package cn.nemo.springframework.beans.factory.xml;

import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.XmlUtil;
import cn.nemo.springframework.beans.BeansException;
import cn.nemo.springframework.beans.PropertyValue;
import cn.nemo.springframework.beans.factory.config.BeanDefinition;
import cn.nemo.springframework.beans.factory.config.BeanReference;
import cn.nemo.springframework.beans.factory.support.AbstractBeanDefinitionReader;
import cn.nemo.springframework.beans.factory.support.BeanDefinitionRegistry;
import cn.nemo.springframework.core.io.Resource;
import cn.nemo.springframework.core.io.ResourceLoader;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.io.InputStream;

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
		} catch (IOException | ClassNotFoundException e) {
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

	private void doLoadBeanDefinitions(InputStream is) throws ClassNotFoundException, BeansException {
		Document doc = XmlUtil.readXML(is);
		Element root = doc.getDocumentElement();
		NodeList childNodes = root.getChildNodes();

		for (int i = 0; i < childNodes.getLength(); i++) {
			Node item = childNodes.item(i);
			if (item instanceof Element bean && "bean".equals(item.getNodeName())) {
				// 获取 Class
				Class<?> clazz = Class.forName(bean.getAttribute("class"));
				String beanName = generateBeanName(bean, clazz);
				BeanDefinition beanDefinition = createBeanDefinition(bean, clazz);
				// 注册 BeanDefinition
				getRegistry().registerBeanDefinition(beanName, beanDefinition);
			}
		}
	}

	private String generateBeanName(Element bean, Class<?> clazz) throws BeansException {
		// 优先级 id > name
		String id = bean.getAttribute("id");
		String name = bean.getAttribute("name");
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
		beanDefinition.setInitMethodName(bean.getAttribute("init-method"));
		beanDefinition.setDestroyMethodName(bean.getAttribute("destroy-method"));
		// 读取属性并填充
		for (int j = 0; j < bean.getChildNodes().getLength(); j++) {
			Node node = bean.getChildNodes().item(j);
			if (node instanceof Element property && "property".equals(node.getNodeName())) {
				// 解析标签：property
				String attrName = property.getAttribute("name");
				String attrValue = property.getAttribute("value");
				String attrRef = property.getAttribute("ref");
				// 获取属性值：引入对象、值对象
				Object value = StrUtil.isNotEmpty(attrRef) ? new BeanReference(attrRef) : attrValue;
				PropertyValue propertyValue = new PropertyValue(attrName, value);
				beanDefinition.getPropertyValues().addPropertyValue(propertyValue);
			}
		}
		return beanDefinition;
	}
}
