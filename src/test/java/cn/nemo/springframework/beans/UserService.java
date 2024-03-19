package cn.nemo.springframework.beans;

import cn.nemo.springframework.beans.factory.*;
import cn.nemo.springframework.context.ApplicationContext;

/**
 * @author zkl
 */
public class UserService implements InitializingBean, DisposableBean, BeanNameAware, BeanClassLoaderAware, ApplicationContextAware, BeanFactoryAware {


	private ApplicationContext applicationContext;
	private BeanFactory beanFactory;

	private String company;
	private String location;
	private IUserDao userDao;

	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	public BeanFactory getBeanFactory() {
		return beanFactory;
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}

	@Override
	public void setBeanName(String name) {
		System.out.println("Bean Name is：" + name);
	}

	@Override
	public void setBeanClassLoader(ClassLoader classLoader) {
		System.out.println("ClassLoader：" + classLoader);
	}

	public UserService() {
	}

	public UserService(IUserDao userDao) {
		this.userDao = userDao;
	}

	public String queryUserInfo(String uid) {
		return userDao.queryUserName(uid) + ", 公司：" + company + ", 地点" + location;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	/**
	 * Bean 销毁调用
	 */
	@Override
	public void destroy() throws Exception {
		System.out.println("执行：UserService.destroy");
	}

	/**
	 * Bean 处理了属性填充后调用
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		System.out.println("执行：UserService.afterPropertiesSet");
	}
}
