package com.shoory.framework.starter.wechatmp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;
import cn.binarywang.wx.miniapp.config.impl.WxMaDefaultConfigImpl;
import cn.binarywang.wx.miniapp.message.WxMaMessageRouter;

@Component
@ConditionalOnBean(value= WechatMpConfig.class)
public class WechatMpComponent {
	@Autowired
	private WxMaService service; 
	
	public String getOpenId(String code) {
		String openId = null;
		try {
			WxMaJscode2SessionResult result = service.jsCode2SessionInfo(code);
			if (result != null) {
				openId = result.getOpenid();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return openId;
	}

	public WechatMpOpenIdAndUnionId getOpenIdAndUnionId(String code) {
		WechatMpOpenIdAndUnionId wechatMpOpenIdAndUnionId = null;
		try {
			WxMaJscode2SessionResult result = service.jsCode2SessionInfo(code);
			wechatMpOpenIdAndUnionId = new WechatMpOpenIdAndUnionId();
			wechatMpOpenIdAndUnionId.setOpenId(result.getOpenid());
			wechatMpOpenIdAndUnionId.setUnionId(result.getUnionid());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return wechatMpOpenIdAndUnionId;
	}

	public WechatMpUserInfo getUserInfo(String sessionKey, String encryptedData, String ivStr) {
		WxMaUserInfo userInfo = service.getUserService().getUserInfo(sessionKey, encryptedData, ivStr);
		WechatMpUserInfo wechatMpUserInfo = new WechatMpUserInfo();
		wechatMpUserInfo.setAvatarUrl(userInfo.getAvatarUrl());
		wechatMpUserInfo.setCity(userInfo.getCity());
		wechatMpUserInfo.setCountry(userInfo.getCountry());
		wechatMpUserInfo.setGender(userInfo.getGender());
		wechatMpUserInfo.setLanguage(userInfo.getLanguage());
		wechatMpUserInfo.setNickName(userInfo.getNickName());
		wechatMpUserInfo.setOpenId(userInfo.getOpenId());
		wechatMpUserInfo.setProvince(userInfo.getProvince());
		wechatMpUserInfo.setUnionId(userInfo.getUnionId());
		wechatMpUserInfo.setWatermark(userInfo.getWatermark());
		return wechatMpUserInfo;
	}
	

	public WxMaPhoneNumberInfo getMobile(String sessionKey, String encryptedData, String ivStr) {
		return	service.getUserService().getPhoneNoInfo(sessionKey, encryptedData, ivStr);
	}

}