package com.shoory.framework.starter.sms;

public interface SmsSender {
	public boolean sendSms(String nationCode, String phoneNumber, String tplId, String[] params, String smsSign);
}
