package com.shoory.framework.starter.sms.tencentcloud;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.github.qcloudsms.httpclient.HTTPException;
import com.shoory.framework.starter.sms.SmsSender;

@Component
public class TencentCloudSmsSender implements SmsSender {
	@Value("${tencentcloud.sms.appid}")
	private int appid;
	@Value("${tencentcloud.sms.appkey}")
	private String appkey;

	@Override
	public boolean sendSms(String nationCode, String phoneNumber, String templateId, String[] params, String smsSign) {
			  SmsSingleSender ssender = new SmsSingleSender(appid, appkey);
			  try {
				SmsSingleSenderResult result = ssender.sendWithParam(nationCode, phoneNumber, 
						  Integer.valueOf(templateId), params, smsSign, "", "");
				return true;
			} catch (NumberFormatException | JSONException | HTTPException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
	}

}
