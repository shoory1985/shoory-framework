package com.shoory.framework.starter.sms;

public interface SmsComponent {
	public boolean sendSms(String nationCode, String phoneNumber, String tplId, String[] params, String smsSign);
}
