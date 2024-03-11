package cn.nemo.springframework.beans;

/**
 * @author zkl
 */
public class BeansException extends Exception {
	public BeansException(String message) {
		super(message);
	}

	public BeansException(String message, Throwable cause) {
		super(message, cause);
	}
}
