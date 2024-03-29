package cn.nemo.springframework.component;

import cn.nemo.springframework.IUserService;
import cn.nemo.springframework.context.annotation.Component;

import java.util.Random;

/**
 * @author zkl
 */
@Component("userService")
public class UserService implements IUserService {

	private String token;

	public String queryUserInfo() {
		try {
			Thread.sleep(new Random(1).nextInt(100));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return "aaa，100001，深圳";
	}

	public String register(String userName) {
		try {
			Thread.sleep(new Random(1).nextInt(100));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return "注册用户：" + userName + " success！";
	}

	@Override
	public String toString() {
		return "UserService#token = { " + token + " }";
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}