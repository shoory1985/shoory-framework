package com.shoory.framework.starter.service;

import java.net.URL;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Component
public class LocaleComponent {
	private Map<String, String> messages;
	
	public LocaleComponent() {
		//
		URL url = this.getClass().getResource("i18n");
		
	}
	
	public String get(String code, String lang) {
		return messages.get(lang + ":" + code);
	}
}
