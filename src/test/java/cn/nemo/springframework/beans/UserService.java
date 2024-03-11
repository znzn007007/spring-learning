package cn.nemo.springframework.beans;

/**
 * @author zkl
 */
public class UserService {

	private UserDao userDao;

	public UserService() {
	}

	public UserService(UserDao userDao) {
		this.userDao = userDao;
	}

	public void queryUserInfo(String uid) {
		System.out.println("query user info: " + userDao.queryUserName(uid));
	}
}
