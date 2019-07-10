package com.shoory.framework.starter.wechat;

import lombok.Data;

@Data
public class WechatUserInfo {

	private String unionId;

	private String openid;

	private String nickname;

	private String sex;

	private String province;

	private String city;

	private String country;

	private String headImageUrl;
}
