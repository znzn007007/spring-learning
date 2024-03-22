package cn.nemo.test;

import cn.nemo.springframework.ProxyFactory;
import cn.nemo.springframework.aop.*;
import cn.nemo.springframework.aop.aspectj.AspectJExpressionPointcut;
import cn.nemo.springframework.aop.aspectj.AspectJExpressionPointcutAdvisor;
import cn.nemo.springframework.aop.framework.CglibAopProxy;
import cn.nemo.springframework.aop.framework.JdkDynamicAopProxy;
import cn.nemo.springframework.aop.framework.ReflectiveMethodInvocation;
import cn.nemo.springframework.aop.framework.adapter.MethodBeforeAdviceInterceptor;
import cn.nemo.springframework.context.support.ClassPathXmlApplicationContext;
import org.aopalliance.intercept.MethodInterceptor;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;


/**
 * @author zkl
 */
public class AOPTest {

	@Test
	public void test_xml() {
		ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring-aop.xml");
		IUserService userService = applicationContext.getBean("userService", IUserService.class);
		System.out.println("测试结果：" + userService.queryUserInfo());
	}

	@Test
	public void test_beforeAdvice() {

		IUserService userService = new UserService();
		// 组装代理信息
		AdvisedSupport advisedSupport = new AdvisedSupport();
		advisedSupport.setTargetSource(new TargetSource(userService));
		advisedSupport.setMethodMatcher(new AspectJExpressionPointcut("execution(* cn.nemo.springframework.aop.IUserService.*(..))"));
		UserServiceBeforeAdvice beforeAdvice = new UserServiceBeforeAdvice();
		MethodBeforeAdviceInterceptor interceptor = new MethodBeforeAdviceInterceptor(beforeAdvice);
		advisedSupport.setMethodInterceptor(interceptor);

		IUserService proxy = (IUserService) new ProxyFactory(advisedSupport).getProxy();
		System.out.println("测试结果：" + proxy.queryUserInfo());
	}

	@Test
	public void test_advisor() {
		// 目标对象
		IUserService userService = new UserService();

		AspectJExpressionPointcutAdvisor advisor = new AspectJExpressionPointcutAdvisor();
		advisor.setExpression("execution(* cn.nemo.springframework.aop.IUserService.*(..))");
		advisor.setAdvice(new MethodBeforeAdviceInterceptor(new UserServiceBeforeAdvice()));

		ClassFilter classFilter = advisor.getPointcut().getClassFilter();
		if (classFilter.matches(userService.getClass())) {
			AdvisedSupport advisedSupport = new AdvisedSupport();

			TargetSource targetSource = new TargetSource(userService);
			advisedSupport.setTargetSource(targetSource);
			advisedSupport.setMethodInterceptor((MethodInterceptor) advisor.getAdvice());
			advisedSupport.setMethodMatcher(advisor.getPointcut().getMethodMatcher());
			advisedSupport.setProxyTargetClass(true); // false/true，JDK动态代理、CGlib动态代理

			IUserService proxy = (IUserService) new ProxyFactory(advisedSupport).getProxy();
			System.out.println("测试结果：" + proxy.queryUserInfo());
		}
	}

	@Test
	void test_dynamic() {
		IUserService service = new UserService();

		AdvisedSupport advisedSupport = new AdvisedSupport();
		advisedSupport.setMethodInterceptor(new UserServiceInterceptor());
		advisedSupport.setTargetSource(new TargetSource(service));
		advisedSupport.setMethodMatcher(new AspectJExpressionPointcut("execution(* cn.nemo.springframework.aop.IUserService.*(..))"));

		// 代理对象(JdkDynamicAopProxy)
		IUserService proxy_jdk = (IUserService) new JdkDynamicAopProxy(advisedSupport).getProxy();
		// 测试调用
		System.out.println("测试结果：" + proxy_jdk.queryUserInfo());

		// 代理对象(CglibAopProxy)
		IUserService proxy_cglib = (IUserService) new CglibAopProxy(advisedSupport).getProxy();
		// 测试调用
		System.out.println("测试结果：" + proxy_cglib.register("花花"));
	}

	@Test
	public void test_aop() throws NoSuchMethodException {
		AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut("execution(* cn.nemo.springframework.beans.UserService.*(..))");
		Class<UserService> clazz = UserService.class;
		Method method = clazz.getDeclaredMethod("queryUserInfo");

		System.out.println(pointcut.matches(clazz));
		System.out.println(pointcut.matches(method, clazz));

		// true、true
	}

	@Test
	public void test_proxy_method() {
		// 目标对象(可以替换成任何的目标对象)
		Object targetObj = new UserService();
		// AOP 代理
		IUserService proxy = (IUserService) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
				targetObj.getClass().getInterfaces(), new InvocationHandler() {
					// 方法匹配器
					MethodMatcher methodMatcher = new AspectJExpressionPointcut("execution(* cn.nemo.springframework.aop.IUserService.*(..))");

					@Override
					public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
						if (methodMatcher.matches(method, targetObj.getClass())) {
							// 方法拦截器
							MethodInterceptor methodInterceptor = invocation -> {
								long start = System.currentTimeMillis();
								try {
									return invocation.proceed();
								} finally {
									System.out.println("监控 - Begin By AOP");
									System.out.println("方法名称：" + invocation.getMethod().getName());
									System.out.println("方法耗时：" + (System.currentTimeMillis() - start) + "ms");
									System.out.println("监控 - End\r\n");
								}
							};
							// 反射调用
							return methodInterceptor.invoke(new ReflectiveMethodInvocation(targetObj, method, args));
						}
						return method.invoke(targetObj, args);
					}
				});
		String result = proxy.queryUserInfo();
		System.out.println("测试结果：" + result);
		result = proxy.register("aaa");
		System.out.println("测试结果：" + result);
	}
}
