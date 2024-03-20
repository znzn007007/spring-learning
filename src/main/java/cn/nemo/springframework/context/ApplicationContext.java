package cn.nemo.springframework.context;

import cn.nemo.springframework.beans.factory.ListableBeanFactory;

/**
 * 配置容器的核心接口
 *
 * @author zkl
 */
public interface ApplicationContext extends ListableBeanFactory, ApplicationEventPublisher {
}