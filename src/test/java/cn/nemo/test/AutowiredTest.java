package cn.nemo.test;

import cn.nemo.springframework.IUserService;
import cn.nemo.springframework.context.support.ClassPathXmlApplicationContext;
import org.junit.jupiter.api.Test;

/**
 * @author zkl
 */
public class AutowiredTest {

	@Test
	void test_scan() {
		ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring-autowired.xml");
		IUserService userService = applicationContext.getBean("userService", IUserService.class);
		System.out.println("result: " + userService.queryUserInfo());
	}
}
