package cn.nemo.springframework.event;

import cn.nemo.springframework.context.ApplicationListener;
import cn.nemo.springframework.context.event.ContextRefreshedEvent;

import java.util.Date;

/**
 * @author zkl
 */
public class ContextRefreshedEventListener implements ApplicationListener<ContextRefreshedEvent> {
	/**
	 * Handle an application event.
	 *
	 * @param event the event to respond to
	 */
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		System.out.println("收到：" + event.getSource() + "消息;时间：" + new Date());
		System.out.println("context refreshed");
	}
}
