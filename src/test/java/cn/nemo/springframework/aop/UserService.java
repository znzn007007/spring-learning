package cn.nemo.springframework.aop;

import cn.nemo.springframework.IUserService;

import java.util.Random;

/**
 * 作者：DerekYRC https://github.com/DerekYRC/mini-spring
 */
public class UserService implements IUserService {

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

}
