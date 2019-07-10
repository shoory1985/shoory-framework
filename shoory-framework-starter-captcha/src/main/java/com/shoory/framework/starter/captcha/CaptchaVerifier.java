package com.shoory.framework.starter.captcha;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shoory.framework.starter.pojo.PojoEmailCaptcha;
import com.shoory.framework.starter.pojo.PojoMobileCaptcha;
import com.shoory.framework.starter.redis.RedisComponent;

/**
 * 验证码检验员
 * 
 * @author raymond
 *
 */
@Component
public class CaptchaVerifier {
	@Autowired
	private RedisComponent redisComponent;
	@Autowired
	private CaptchaUtils captchaUtils;

	/**
	 * 验证
	 * 
	 * @param captchaId
	 * @param captchaCode
	 * @return
	 */
	public boolean verify(String captchaId, String captchaCode) {
		String remainsKey = captchaUtils.getRemainsKey(captchaId);
		String capthcaCodeKey = captchaUtils.getCaptchaCodeKey(captchaId);

		Long remains;
		String capthcaCodeFromRedis;
		// 检查remains（原子操作）
		if (null == (remains = redisComponent.decrease(remainsKey)) || remains < 0) {
			// remains没有或耗尽（虽然不是减到0原子操作，但本次为负也不继续往下执行，同样不会有问题）
		} else if (null == (capthcaCodeFromRedis = redisComponent.findByKey(capthcaCodeKey, String.class))) {
			// capthcaCode没找到（为null）
		} else if (!capthcaCodeFromRedis.equalsIgnoreCase(captchaCode)) {
			// 验证码不匹配
		} else {
			// 验证码正确
			// 验证成功也删除缓存
			redisComponent.deleteByKey(remainsKey);
			redisComponent.deleteByKey(capthcaCodeKey);

			return true;
		}

		if (remains != null && remains <= 0) {
			// remains耗尽也删除缓存
			redisComponent.deleteByKey(remainsKey);
			redisComponent.deleteByKey(capthcaCodeKey);
		}

		return false;
	}

	// 检查邮件验证码
	public boolean checkEmailCaptcha(String captchaId, String captchaCode, String email) {
		// 检查是否存在
		if (!redisComponent.exists(captchaId)) {
			return false;
		}

		// 检查格式
		PojoEmailCaptcha pojo = redisComponent.findByKey(captchaId, PojoEmailCaptcha.class);
		if (pojo == null || pojo.getCaptchaCode() == null) {
			return false;
		}

		// 检查验证码是否正确
		if (!pojo.getCaptchaCode().equalsIgnoreCase(captchaCode) || !pojo.getEmail().equals(email)) {
			// 验证码错误
			pojo.setCheckCount(pojo.getCheckCount() - 1);
			// 检查次数是否用尽
			if (pojo.getCheckCount() <= 0) {
				// 删除缓存
				redisComponent.deleteByKey(captchaId);
			} else {
				// 更新缓存
				redisComponent.save(captchaId, pojo, (pojo.getExpiredTime() - System.currentTimeMillis()) / 1000);
			}
			return false;
		}

		return true;

	}

	// 检查手机验证码
	public boolean checkMobileCaptcha(String captchaId, String captchaCode, String countryCode, String mobile) {
		// 检查是否存在
		if (!redisComponent.exists(captchaId)) {
			return false;
		}

		// 检查格式
		PojoMobileCaptcha pojo = redisComponent.findByKey(captchaId, PojoMobileCaptcha.class);
		if (pojo == null || pojo.getCaptchaCode() == null) {
			return false;
		}

		// 检查验证码,国家代码,手机号是否正确
		if (!pojo.getCaptchaCode().equals(captchaCode) || !pojo.getCountryCode().equals(countryCode)
				|| !pojo.getMobile().equals(mobile)) {
			// 验证码错误
			pojo.setCheckCount(pojo.getCheckCount() - 1);
			// 检查次数是否用尽
			if (pojo.getCheckCount() <= 0) {
				// 删除缓存
				redisComponent.deleteByKey(captchaId);
			} else {
				// 更新缓存
				redisComponent.save(captchaId, pojo, (pojo.getExpiredTime() - System.currentTimeMillis()) / 1000);
			}
			return false;
		}

		// 验证码正确
		// 删除缓存
		redisComponent.deleteByKey(captchaId);

		return true;
	}
}
