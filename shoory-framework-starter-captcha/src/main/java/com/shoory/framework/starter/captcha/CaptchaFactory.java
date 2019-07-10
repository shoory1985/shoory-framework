package com.shoory.framework.starter.captcha;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.code.kaptcha.Producer;
import com.shoory.framework.starter.pojo.PojoImageCaptcha;
import com.shoory.framework.starter.redis.RedisComponent;

/**
 * 验证码工厂
 * @author rui.xu
 *
 */
@Component
public class CaptchaFactory {
	@Autowired
	private Producer captchaProducer;
	@Autowired
	private CaptchaUtils captchaUtils;

	@Autowired
	private RedisComponent redisComponent;

	/**
	 * 
	 * @param remains
	 * @param timeoutInMills
	 * @return
	 */
	public PojoImageCaptcha produce(int remains, long timeoutInMills) {
		// 没次数不生成
		if (remains <= 0) {
			return null;
		}
		// 没时间也不生成
		if (timeoutInMills <= 0) {
			return null;
		}

		// ID
		String captchaId = UUID.randomUUID().toString();
		// 验证码
		String captchaCode = captchaProducer.createText();
		//生成图
		String imageBase64Str = makeJpg(captchaCode);
		if (imageBase64Str == null) {
			return null;
		}

		long expiredTime = System.currentTimeMillis() + timeoutInMills;
		//入库
		redisComponent.save(captchaUtils.getCaptchaCodeKey(captchaId), captchaCode, timeoutInMills, TimeUnit.MILLISECONDS);
		redisComponent.save(captchaUtils.getRemainsKey(captchaId), remains, timeoutInMills, TimeUnit.MILLISECONDS);
		
		//出单
		PojoImageCaptcha captchaOut = new PojoImageCaptcha();
		captchaOut.setCaptchaId(captchaId);
		captchaOut.setExpiredTime(expiredTime);
		captchaOut.setRemains(remains);
		captchaOut.setImageBase64Str("data:image/jpg;base64," + makeJpg(captchaCode));

		return captchaOut;
	}


	private String makeJpg(String captchaCode) {
		BufferedImage bi = captchaProducer.createImage(captchaCode);
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		try {
			ImageIO.write(bi, "jpg", byteArrayOutputStream);
			return Base64.encodeBase64String(byteArrayOutputStream.toByteArray());
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				byteArrayOutputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
