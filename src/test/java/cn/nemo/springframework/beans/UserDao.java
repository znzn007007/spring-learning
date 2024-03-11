package cn.nemo.springframework.beans;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zkl
 */
public class UserDao {
	private static Map<String, String> hashMap = new HashMap<>();

	static {
		hashMap.put("10001", "aaa");
		hashMap.put("10002", "bbb");
		hashMap.put("10003", "ccc");
	}

	public String queryUserName(String uId) {
		return hashMap.get(uId);
	}
}