package com.shoory.framework.starter.wechat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpUser;

@Component
@ConditionalOnBean(value = WechatConfig.class)
public class WechatComponent {

	@Autowired
	private WxMpService service;

	public WxMpUser getUserInfo(String code) throws WxErrorException {
		WxMpOAuth2AccessToken accessToken = service.oauth2getAccessToken(code);
		return service.oauth2getUserInfo(accessToken, null);
		
	}
}