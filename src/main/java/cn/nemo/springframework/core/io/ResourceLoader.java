package cn.nemo.springframework.core.io;

/**
 * @author zkl
 */
public interface ResourceLoader {
	String CLASSPATH_URL_PREFIX = "classpath:";

	Resource getResource(String location);
}
