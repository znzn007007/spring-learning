package cn.nemo.springframework.autowired;

import cn.nemo.springframework.IUserService;
import cn.nemo.springframework.beans.factory.annotation.Autowired;
import cn.nemo.springframework.beans.factory.annotation.Value;
import cn.nemo.springframework.context.annotation.Component;

import java.util.Random;

/**
 * @author zkl
 */
@Component("userService")
public class UserService implements IUserService {

	@Autowired
	private UserDao userDao;

	@Value("${token}")
	private String token;


	public String queryUserInfo() {
		try {
			Thread.sleep(new Random(1).nextInt(100));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return userDao.queryUserName("10001") + token;
	}

	public String register(String userName) {
		try {
			Thread.sleep(new Random(1).nextInt(100));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return "注册用户：" + userName + " success！";
	}
}
