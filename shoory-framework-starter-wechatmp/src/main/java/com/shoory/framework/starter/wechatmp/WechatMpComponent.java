package com.shoory.framework.starter.wechatmp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;
import me.chanjar.weixin.common.error.WxErrorException;

@Component
@ConditionalOnBean(value= WechatMpConfig.class)
public class WechatMpComponent {
	@Autowired
	private WxMaService service; 
	
	public WxMaJscode2SessionResult code2Session(String code) throws WxErrorException {
		return service.jsCode2SessionInfo(code);
	}

	public WxMaUserInfo getUserInfo(String sessionKey, String encryptedData, String ivStr) {
		WxMaUserInfo userInfo = service.getUserService().getUserInfo(sessionKey, encryptedData, ivStr);
		return userInfo;
	}
	
	public WxMaPhoneNumberInfo getMobile(String sessionKey, String encryptedData, String ivStr) {
		return	service.getUserService().getPhoneNoInfo(sessionKey, encryptedData, ivStr);
	}

}