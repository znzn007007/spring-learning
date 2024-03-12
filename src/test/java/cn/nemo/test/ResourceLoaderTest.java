package cn.nemo.test;

import cn.hutool.core.io.IoUtil;
import cn.nemo.springframework.beans.BeansException;
import cn.nemo.springframework.beans.UserService;
import cn.nemo.springframework.beans.factory.support.DefaultListableBeanFactory;
import cn.nemo.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import cn.nemo.springframework.core.io.DefaultResourceLoader;
import cn.nemo.springframework.core.io.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author zkl
 */
public class ResourceLoaderTest {

	private DefaultResourceLoader resourceLoader;

	@BeforeEach
	public void init() {
		resourceLoader = new DefaultResourceLoader();
	}

	@Test
	public void test_xml() throws BeansException {
		// 1.初始化 BeanFactory
		DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

		// 2. 读取配置文件&注册Bean
		XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);
		reader.loadBeanDefinitions("classpath:spring.xml");

		// 3. 获取Bean对象调用方法
		UserService userService = (UserService) beanFactory.getBean("userService");
		userService.queryUserInfo("10001");
	}

	@Test
	public void test_classpath() throws IOException {
		Resource resource = resourceLoader.getResource("classpath:important.properties");
		InputStream inputStream = resource.getInputStream();
		String content = IoUtil.readUtf8(inputStream);
		System.out.println(content);
	}

	@Test
	public void test_file() throws IOException {
		Resource resource = resourceLoader.getResource("src/test/resources/important.properties");
		InputStream inputStream = resource.getInputStream();
		String content = IoUtil.readUtf8(inputStream);
		System.out.println(content);
	}

	@Test
	public void test_url() throws IOException {
		// 网络原因可能导致GitHub不能读取，可以放到自己的Gitee仓库。读取后可以从内容中搜索关键字；OLpj9823dZ
		Resource resource = resourceLoader.getResource("https://github.com/fuzhengwei/small-spring/blob/main/important.properties");
		InputStream inputStream = resource.getInputStream();
		String content = IoUtil.readUtf8(inputStream);
		System.out.println(content);
	}
}
