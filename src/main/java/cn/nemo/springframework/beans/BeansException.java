package cn.nemo.springframework.beans;

/**
 * @author zkl
 */
public class BeansException extends RuntimeException {
	public BeansException(String message) {
		super(message);
	}

	public BeansException(String message, Throwable cause) {
		super(message, cause);
	}
}
