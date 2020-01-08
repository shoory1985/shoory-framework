package com.shoory.framework.starter.wechat;


import org.apache.commons.lang3.StringUtils;
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


	public String getOpenId(String code) {
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
		return null;
	}
}