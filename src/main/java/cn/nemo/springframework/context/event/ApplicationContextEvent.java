package cn.nemo.springframework.context.event;

import cn.nemo.springframework.context.ApplicationContext;
import cn.nemo.springframework.context.ApplicationEvent;

/**
 * 定义事件的抽象类，所有的事件包括关闭、刷新，以及用户自己实现的事件，都需要继承这个类
 *
 * @author zkl
 */
public class ApplicationContextEvent extends ApplicationEvent {
	/**
	 * Constructs a prototypical Event.
	 *
	 * @param source the object on which the Event initially occurred
	 * @throws IllegalArgumentException if source is null
	 */
	public ApplicationContextEvent(Object source) {
		super(source);
	}

	/**
	 * Get the <code>ApplicationContext</code> that the event was raised for.
	 */
	public final ApplicationContext getApplicationContext() {
		return (ApplicationContext) getSource();
	}
}
