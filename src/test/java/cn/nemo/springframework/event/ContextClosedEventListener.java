package cn.nemo.springframework.event;

import cn.nemo.springframework.context.ApplicationListener;
import cn.nemo.springframework.context.event.ContextClosedEvent;

import java.util.Date;

/**
 * @author zkl
 */
public class ContextClosedEventListener implements ApplicationListener<ContextClosedEvent> {
	/**
	 * Handle an application event.
	 *
	 * @param event the event to respond to
	 */
	@Override
	public void onApplicationEvent(ContextClosedEvent event) {
		System.out.println("收到：" + event.getSource() + "消息;时间：" + new Date());
		System.out.println("context closed");
	}
}
