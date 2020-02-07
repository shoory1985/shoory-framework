package com.shoory.framework.starter.sms;

public interface SmsComponent {
	public void sendSms(String nationCode, String phoneNumber, String tplId, String[] params) throws Exception;
	public void sendVoice(String nationCode, String phoneNumber, String tplId, int playtimes) throws Exception;
}
