package com.shoory.framework.starter.service;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;


/**
 * 
 * 普通类调用Spring bean对象：
 * 
 * 说明：
 * 
 * 1、此类需要放到App.Java同包或者子包下才能被扫描，否则失效。
 * 
 * @author Administrator
 * 
 */

@Component
public class SpringUtil implements ApplicationContextAware {
	private static ApplicationContext applicationContext = null;

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		if (SpringUtil.applicationContext == null) {
			SpringUtil.applicationContext = applicationContext;

			System.out.print("获取上下文" + applicationContext.getId());
		}
	}

	// 获取applicationContext
	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	// 通过name获取 Bean.
	public static Object getBean(String name) {
		return applicationContext.getBean(name);
	}

	// 通过class获取Bean.
	public static <T> T getBean(Class<T> clazz) {
		return applicationContext.getBean(clazz);
	}

	// 通过name,以及Clazz返回指定的Bean
	public static <T> T getBean(String name, Class<T> clazz) {
		return applicationContext.getBean(name, clazz);
	}

}
