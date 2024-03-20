package cn.nemo.springframework.context.event;

import cn.nemo.springframework.beans.factory.BeanFactory;
import cn.nemo.springframework.context.ApplicationEvent;
import cn.nemo.springframework.context.ApplicationListener;

/**
 * @author zkl
 */
public class SimpleApplicationEventMulticaster extends AbstractApplicationEventMulticaster {

	public SimpleApplicationEventMulticaster(BeanFactory beanFactory) {
		setBeanFactory(beanFactory);
	}

	/**
	 * Multicast the given application event to appropriate listeners.
	 *
	 * @param event the event to multicast
	 */
	@Override
	public void multicastEvent(ApplicationEvent event) {

		for (final ApplicationListener listener : getApplicationListeners(event)) {

			listener.onApplicationEvent(event);
		}
	}
}
