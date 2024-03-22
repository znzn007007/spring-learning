package cn.nemo.springframework;

import cn.nemo.springframework.aop.AdvisedSupport;
import cn.nemo.springframework.aop.framework.AopProxy;
import cn.nemo.springframework.aop.framework.CglibAopProxy;
import cn.nemo.springframework.aop.framework.JdkDynamicAopProxy;

/**
 * @author zkl
 */
public class ProxyFactory {
	private AdvisedSupport advisedSupport;

	public ProxyFactory(AdvisedSupport advisedSupport) {
		this.advisedSupport = advisedSupport;
	}

	public Object getProxy() {
		return createAopProxy().getProxy();
	}

	private AopProxy createAopProxy() {
		if (advisedSupport.isProxyTargetClass()) {
			return new CglibAopProxy(advisedSupport);
		}
		return new JdkDynamicAopProxy(advisedSupport);
	}
}