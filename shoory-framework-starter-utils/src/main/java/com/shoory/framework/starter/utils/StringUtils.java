package com.shoory.framework.starter.utils;

public class StringUtils extends org.apache.commons.lang.StringUtils {
	public static String beanName(Class<?> clazz) {
		return clazz != null 
				? clazz.getSimpleName().substring(0, 1).toLowerCase() + clazz.getSimpleName().substring(1)
				: "";
	}
}
