package cn.nemo.springframework.context;

import java.util.EventObject;

/**
 * 后续所有事件的类都需要继承这个类
 *
 * @author zkl
 */
public abstract class ApplicationEvent extends EventObject {
	/**
	 * Constructs a prototypical Event.
	 *
	 * @param source the object on which the Event initially occurred
	 * @throws IllegalArgumentException if source is null
	 */
	public ApplicationEvent(Object source) {
		super(source);
	}
}
