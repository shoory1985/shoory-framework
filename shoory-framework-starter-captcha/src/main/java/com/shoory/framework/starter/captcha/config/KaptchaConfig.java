package com.shoory.framework.starter.captcha.config;

import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.util.Config;

@Configuration
public class KaptchaConfig {

	@Bean
	public Producer setConfig() {
		Properties properties = new Properties();
		properties.setProperty("kaptcha.border", "no");
		properties.setProperty("kaptcha.textproducer.font.color", "0,138,245");
		properties.setProperty("kaptcha.image.width", "110");
		properties.setProperty("kaptcha.image.height", "40");
		properties.setProperty("kaptcha.textproducer.font.size", "38");
		properties.setProperty("kaptcha.session.key", "CaptchaCode");
		properties.setProperty("kaptcha.textproducer.char.length", "4");
		properties.setProperty("kaptcha.textproducer.font.names", "宋体,楷体,微软雅黑");
		Config config = new Config(properties);
		Producer captchaProducer = config.getProducerImpl();
		return captchaProducer;
	}
}
