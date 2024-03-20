package cn.nemo.test;

import cn.nemo.springframework.context.support.ClassPathXmlApplicationContext;
import cn.nemo.springframework.event.CustomEvent;
import org.junit.jupiter.api.Test;

/**
 * @author zkl
 */
public class EventTest {

	@Test
	void test() {
		ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring-event.xml");
		applicationContext.publishEvent(new CustomEvent(applicationContext, 1019129009086763L, "成功了！"));

		applicationContext.registerShutdownHook();
	}
}
