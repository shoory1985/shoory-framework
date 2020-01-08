package com.shoory.framework.starter.wechat;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;

@Configuration
@ConditionalOnProperty(prefix = "wechat",name = {"appId","appSecret"},matchIfMissing = false)
public class WechatConfig {

	@Value("${wechat.appId}")
	private String appId;

	@Value("${wechat.appSecret}")
	private String appSecret;
	
	@Bean
	public WxMpService service() {
		WxMpDefaultConfigImpl configStorage = new WxMpDefaultConfigImpl();
		configStorage.setAppId(this.appId);
		configStorage.setSecret(this.appSecret);
		configStorage.setToken("");
		configStorage.setAesKey("");
		WxMpService service = new WxMpServiceImpl();
		service.setWxMpConfigStorage(configStorage);
		return service;
	}

	
}