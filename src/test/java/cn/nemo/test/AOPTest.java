package cn.nemo.test;

import cn.nemo.springframework.aop.*;
import cn.nemo.springframework.aop.aspectj.AspectJExpressionPointcut;
import cn.nemo.springframework.aop.framework.CglibAopProxy;
import cn.nemo.springframework.aop.framework.JdkDynamicAopProxy;
import cn.nemo.springframework.aop.framework.ReflectiveMethodInvocation;
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
