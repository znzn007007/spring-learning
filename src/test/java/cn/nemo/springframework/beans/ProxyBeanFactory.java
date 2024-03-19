package cn.nemo.springframework.beans;

import cn.nemo.springframework.beans.factory.FactoryBean;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zkl
 */
public class ProxyBeanFactory implements FactoryBean<IUserDao> {
	@Override
	public IUserDao getObejct() throws Exception {
		InvocationHandler handler = (proxy, method, args) -> {

			Map<String, String> hashMap = new HashMap<>();
			hashMap.put("10001", "aaa");
			hashMap.put("10002", "bbb");
			hashMap.put("10003", "ccc");

			return "你被代理了 " + method.getName() + "：" + hashMap.get(args[0].toString());
		};
		return (IUserDao) Proxy.newProxyInstance(Thread.currentThread()
				.getContextClassLoader(), new Class[]{IUserDao.class}, handler);
	}

	@Override
	public Class<?> getObjectType() {
		return IUserDao.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}
}
