package cn.nemo.test;

import cn.nemo.springframework.beans.UserService;
import cn.nemo.springframework.context.support.ClassPathXmlApplicationContext;
import org.junit.jupiter.api.Test;
import org.openjdk.jol.info.ClassLayout;

/**
 * @author zkl
 */
public class FactoryBeanTest {

	@Test
	public void test_prototype() {
		// 1.初始化 BeanFactory
		ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring-bean-factory.xml");
		applicationContext.registerShutdownHook();

		// 2. 获取Bean对象调用方法
		UserService userService01 = applicationContext.getBean("userService", UserService.class);
		UserService userService02 = applicationContext.getBean("userService", UserService.class);

		// 3. 配置 scope="prototype/singleton"
		System.out.println(userService01);
		System.out.println(userService02);

		// 4. 打印十六进制哈希
		System.out.println(userService01 + " 十六进制哈希：" + Integer.toHexString(userService01.hashCode()));
		System.out.println(ClassLayout.parseInstance(userService01).toPrintable());


		System.out.println(userService01.queryUserInfo("10001"));
	}
}
