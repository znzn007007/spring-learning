package cn.nemo.springframework.beans;

import cn.nemo.springframework.beans.factory.DisposableBean;
import cn.nemo.springframework.beans.factory.InitializingBean;

/**
 * @author zkl
 */
public class UserService implements InitializingBean, DisposableBean {


	private String company;

	private String location;

	private UserDao userDao;

	public UserService() {
	}

	public UserService(UserDao userDao) {
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
