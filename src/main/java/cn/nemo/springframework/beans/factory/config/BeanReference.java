package cn.nemo.springframework.beans.factory.config;

/**
 * bean的引用信息
 *
 * @author zkl
 */
public class BeanReference {
	private String beanName;

	public BeanReference(String beanName) {
		this.beanName = beanName;
	}

	public String getBeanName() {
		return beanName;
	}

	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}
}
