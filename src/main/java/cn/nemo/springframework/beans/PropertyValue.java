package cn.nemo.springframework.beans;

import cn.nemo.springframework.beans.factory.config.BeanReference;

/**
 * @author zkl
 */
public class PropertyValue {
	private final String name;
	private final Object value;

	public PropertyValue(String name, BeanReference value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public Object getValue() {
		return value;
	}
}