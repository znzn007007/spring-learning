package cn.nemo.springframework.circle;


import cn.nemo.springframework.context.support.ClassPathXmlApplicationContext;
import org.junit.jupiter.api.Test;

/**
 * 循环依赖测试
 */
public class CircleTest {

    @Test
    public void test_circular() {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring-circle.xml");
        Husband husband = applicationContext.getBean("husband", Husband.class);
        Wife wife = applicationContext.getBean("wife", Wife.class);
        System.out.println("老公的媳妇：" + husband.queryWife());
        System.out.println("媳妇的老公：" + wife.queryHusband());
    }

}
