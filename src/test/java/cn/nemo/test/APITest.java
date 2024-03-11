package cn.nemo.test;

import cn.nemo.springframework.beans.*;
import cn.nemo.springframework.beans.factory.config.BeanDefinition;
import cn.nemo.springframework.beans.factory.config.BeanReference;
import cn.nemo.springframework.beans.factory.support.DefaultListableBeanFactory;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.NoOp;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;

/**
 * @author zkl
 */
public class APITest {

	@Test
	void test() throws BeansException {
		DefaultListableBeanFactory factory = new DefaultListableBeanFactory();

		factory.registerBeanDefinition("userDao", new BeanDefinition(UserDao.class));

		PropertyValues propertyValues = new PropertyValues();
		propertyValues.addPropertyValue(new PropertyValue("userDao", new BeanReference("userDao")));
		BeanDefinition definition = new BeanDefinition(UserService.class, propertyValues);
		factory.registerBeanDefinition("userService", definition);

		UserService userService = (UserService) factory.getBean("userService");
		userService.queryUserInfo("10001");

	}

	@Test
	public void test_newInstance() throws IllegalAccessException, InstantiationException {
		UserService userService = UserService.class.newInstance();
		System.out.println(userService);
	}

	@Test
	public void test_constructor() throws Exception {
		Class<UserService> userServiceClass = UserService.class;
		Constructor<UserService> declaredConstructor = userServiceClass.getDeclaredConstructor(String.class);
		UserService userService = declaredConstructor.newInstance("小傅哥");
		System.out.println(userService);
	}

	@Test
	public void test_parameterTypes() throws Exception {
		Class<UserService> beanClass = UserService.class;
		Constructor<?>[] declaredConstructors = beanClass.getDeclaredConstructors();
		Constructor<?> constructor = declaredConstructors[0];
		Constructor<UserService> declaredConstructor = beanClass.getDeclaredConstructor(constructor.getParameterTypes());
		UserService userService = declaredConstructor.newInstance("小傅哥");
		System.out.println(userService);
	}

	@Test
	public void test_cglib() {
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(UserService.class);
		enhancer.setCallback(new NoOp() {
			@Override
			public int hashCode() {
				return super.hashCode();
			}
		});
		Object obj = enhancer.create(new Class[]{String.class}, new Object[]{"小傅哥"});
		System.out.println(obj);
	}
}
