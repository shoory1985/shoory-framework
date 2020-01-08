package com.shoory.framework.starter.wechatmp;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.config.impl.WxMaDefaultConfigImpl;
import cn.binarywang.wx.miniapp.message.WxMaMessageRouter;

@Configuration
@ConditionalOnProperty(prefix = "wechatmp",name = {"appId","appSecret"},matchIfMissing = false)
public class WechatMpConfig {


	@Value("${wechatmp.appId}")
	private String appId;

	@Value("${wechatmp.appSecret}")
	private String appSecret;
	

	@Bean
	public WxMaService service() {
		WxMaDefaultConfigImpl config = new WxMaDefaultConfigImpl();
		config.setAppid(appId);
		config.setSecret(appSecret);
		config.setToken("");
		config.setAesKey("");
		WxMaService service  = new WxMaServiceImpl();
		service.setWxMaConfig(config);
		return service;
	}

	
}