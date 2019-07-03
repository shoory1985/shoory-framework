package com.shoory.framework.starter.captcha;

import org.springframework.stereotype.Component;

/**
 * 验证码工具
 * @author rui.xu
 *
 */
@Component
public class CaptchaUtils {
	
	String getCaptchaCodeKey(String captchaId) {
		return "Captcha-"+captchaId+"-Code";
	}
	
	String getRemainsKey(String captchaId) {
		return "Captcha-"+captchaId+"-Remains";
	}
}
