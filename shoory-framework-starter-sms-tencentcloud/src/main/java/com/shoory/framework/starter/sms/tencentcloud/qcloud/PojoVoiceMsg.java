package com.shoory.framework.starter.sms.tencentcloud.qcloud;

import java.util.List;

import lombok.Data;

@Data
public class PojoVoiceMsg {
	private String msg;
	private int playtimes;
	private String sig;
	private PojoTel tel;
	private int time;
	private String ext = "";
}
