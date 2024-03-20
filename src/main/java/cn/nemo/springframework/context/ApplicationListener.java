package cn.nemo.springframework.context;

import java.util.EventListener;

/**
 * @author zkl
 */
public interface ApplicationListener<E extends ApplicationEvent> extends EventListener {

	/**
	 * Handle an application event.
	 *
	 * @param event the event to respond to
	 */
	void onApplicationEvent(E event);
}
