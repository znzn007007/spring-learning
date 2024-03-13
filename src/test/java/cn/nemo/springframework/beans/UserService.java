package cn.nemo.springframework.beans;

/**
 * @author zkl
 */
public class UserService {


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
}
