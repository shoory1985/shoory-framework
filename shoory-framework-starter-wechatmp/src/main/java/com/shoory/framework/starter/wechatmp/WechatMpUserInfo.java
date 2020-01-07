package com.shoory.framework.starter.wechatmp;

import cn.binarywang.wx.miniapp.bean.WxMaUserInfo.Watermark;
import lombok.Data;

@Data
public class WechatMpUserInfo {
	private String openId;
	private String nickName;
	private String gender;
	private String language;
	private String city;
	private String province;
	private String country;
	private String avatarUrl;
	private String unionId;
	private Watermark watermark;
}
