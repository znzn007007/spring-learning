package cn.nemo.test;

import cn.nemo.springframework.beans.BeansException;
import cn.nemo.springframework.beans.MyBeanFactoryPostProcessor;
import cn.nemo.springframework.beans.MyBeanPostProcessor;
import cn.nemo.springframework.beans.UserService;
import cn.nemo.springframework.beans.factory.support.DefaultListableBeanFactory;
import cn.nemo.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import cn.nemo.springframework.context.support.ClassPathXmlApplicationContext;
import org.junit.jupiter.api.Test;

/**
 * @author zkl
 */
public class ContextProcessTest {

	@Test
	public void test_BeanFactoryPostProcessorAndBeanPostProcessor() throws BeansException {
		// 1.初始化 BeanFactory
		DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

		// 2. 读取配置文件&注册Bean
		XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);
		reader.loadBeanDefinitions("classpath:spring.xml");

		// 3. BeanDefinition 加载完成 & Bean实例化之前，修改 BeanDefinition 的属性值
		MyBeanFactoryPostProcessor beanFactoryPostProcessor = new MyBeanFactoryPostProcessor();
		beanFactoryPostProcessor.postProcessBeanFactory(beanFactory);

		// 4. Bean实例化之后，修改 Bean 属性信息
		MyBeanPostProcessor beanPostProcessor = new MyBeanPostProcessor();
		beanFactory.addBeanPostProcessor(beanPostProcessor);

		// 5. 获取Bean对象调用方法
		UserService userService = beanFactory.getBean("userService", UserService.class);
		String result = userService.queryUserInfo("10001");
		System.out.println("测试结果：" + result);
	}

	@Test
	public void test_xml() throws BeansException {
		// 1.初始化 BeanFactory
		ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring-enhanced.xml");

		// 2. 获取Bean对象调用方法
		UserService userService = applicationContext.getBean("userService", UserService.class);
		String result = userService.queryUserInfo("10001");
		System.out.println("测试结果：" + result);
	}
}
