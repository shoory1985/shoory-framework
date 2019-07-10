package com.shoory.framework.starter.email.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PojoAttachment {
	private String name;
	private String contentType;
	private byte[] bytes;
}