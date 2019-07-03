package com.shoory.framework.starter.wechat;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.google.common.collect.Maps;

import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpUser;

@Component
public class WechatComponent {

	private WxMpService service;

	@Bean
	public WxMpService service() {
		WxMpInMemoryConfigStorage configStorage = new WxMpInMemoryConfigStorage();
		configStorage.setAppId(this.appId);
		configStorage.setSecret(this.appSecret);
		configStorage.setToken("");
		configStorage.setAesKey("");
		service = new WxMpServiceImpl();
		service.setWxMpConfigStorage(configStorage);
		return service;
	}
	@Value("${wechat.appId}")
	private String appId;

	@Value("${wechat.appSecret}")
	private String appSecret;

	public String getOpenId(String code) {
		if(service==null) {
			this.service();
		}
		String openId=null;
		try {
			WxMpOAuth2AccessToken accessToken = service.oauth2getAccessToken(code);
			WxMpUser user = service.oauth2getUserInfo(accessToken, null);

			openId=user.getOpenId();
		} catch (WxErrorException e) {
			e.printStackTrace();
		}
		return openId;
	}

	public WechatUserInfo getUserInfo(String code) {
		if(service==null) {
			this.service();
		}
		WechatUserInfo userInfo=null;
		try {
			WxMpOAuth2AccessToken accessToken = service.oauth2getAccessToken(code);
			WxMpUser user = service.oauth2getUserInfo(accessToken, null);
			userInfo=new WechatUserInfo();
			userInfo.setCity(user.getCity());
			userInfo.setCountry(user.getCountry());
			userInfo.setHeadImageUrl(user.getHeadImgUrl());
			userInfo.setNickname(user.getNickname());
			userInfo.setOpenid(user.getOpenId());
			userInfo.setProvince(user.getProvince());
			userInfo.setSex(user.getSex()==1?"男":"女");
			if(StringUtils.isNotBlank(user.getUnionId())) {
				userInfo.setUnionId(user.getUnionId());
			}else {
				userInfo.setUnionId("");
			}
		} catch (WxErrorException e) {
			e.printStackTrace();
		}

		return userInfo;
	}

	public WechatOpenIdAndUnionId getOpenIdAndUnionId(String code) {
		if(service==null) {
			this.service();
		}
		return null;
	}
}