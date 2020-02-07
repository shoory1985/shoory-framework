package com.shoory.framework.starter.utils;

import java.util.Random;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class SaltyFactory {
	@Bean
	public SaltyFactory getSaltyFactory() {
		return new SaltyFactory();
	}
	
	private final Random random = new Random();
	
	public String produceMixed(int length) {
		String val = "";
		for (int i = 0; i < length; i++) {
			// 输出字母还是数字
			String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
			// 字符串　　
			if ("char".equalsIgnoreCase(charOrNum)) {
				// 取得大写字母还是小写字母　　
				int choice = random.nextInt(2) % 2 == 0 ? 65 : 97;
				val += (char) (choice + random.nextInt(26));
			} else if ("num".equalsIgnoreCase(charOrNum)) {
				// 数字　　
				val += String.valueOf(random.nextInt(6));
			}
		}
		return val;
	}
	
	public String produceNum(int length) {
		return String.valueOf(Math.round(((int)(Math.random() * (9 * Math.pow(10, length - 1))) + Math.pow(10, length - 1))));
	}
}
