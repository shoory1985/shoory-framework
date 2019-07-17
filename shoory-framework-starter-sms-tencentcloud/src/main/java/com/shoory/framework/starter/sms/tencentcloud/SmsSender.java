package com.shoory.framework.starter.sms.tencentcloud;

import java.io.BufferedReader;
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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.shoory.framework.starter.sms.tencentcloud.qcloud.PojoSmsMsg;
import com.shoory.framework.starter.sms.tencentcloud.qcloud.PojoTel;
import com.shoory.framework.starter.sms.tencentcloud.qcloud.PojoVoiceMsg;
import com.shoory.framework.starter.utils.PojoUtils;

@Component
public class SmsSender {
	
	@Value("${sms.appid}")
	private long APPID;
	
	@Value("${sms.appkey}")
	private String APPKEY;
	
	@Value("${sms.sms_url}")
	private String SMS_URL;
	
	@Value("${sms.voice_url}")
	private String VOICE_URL;
	
	@Autowired
	private PojoUtils pojoUtils;
	
	@Bean
	public SmsSender sender(){		
		return new SmsSender();
	}
	
	/*
	 * 普通单发短信接口，明确指定内容，如果有多个签名，请在内容中以【】的方式添加到信息内容中，否则系统将使用默认签名
	 * @param type 短信类型，0 为普通短信，1 营销短信
	 * @param nationCode 国家码，如 86 为中国
	 * @param phoneNumber 不带国家码的手机号
	 * @param msg 信息内容，必须与申请的模板格式一致，否则将返回错误
	 * @param extend 扩展码，可填空
	 * @param ext 服务端原样返回的参数，可填空
	 * @return {@link}SmsSingleSenderResult
	 * @throws Exception
	 */
	public void sendSms(
			String nationCode,
			String phoneNumber,
			String tplId,String[] params) throws Exception {

		// 按照协议组织 post 请求包体
        long random = this.getRandom();
        long curTime = System.currentTimeMillis() / 1000;

		PojoSmsMsg data = new PojoSmsMsg();


        data.setTpl_id(tplId);
        List<String> places = Arrays.asList(params);
        data.setParams(places);
        data.setSig(this.strToHash(String.format(
        		"appkey=%s&random=%d&time=%d&mobile=%s",
        		APPKEY, random, curTime, phoneNumber)));
        data.setTel(new PojoTel(nationCode, phoneNumber));
        data.setTime((int)curTime);

        // 与上面的 random 必须一致
		String wholeUrl = String.format("%s?sdkappid=%d&random=%d", SMS_URL, APPID, random);
        HttpURLConnection conn = this.getPostHttpConn(wholeUrl);

        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream(), "utf-8");
        wr.write(pojoUtils.toJson(data));
        wr.flush();

        //System.out.println(data.toString());

        // 显示 POST 请求返回的内容
        StringBuilder sb = new StringBuilder();
        int httpRspCode = conn.getResponseCode();
        if (httpRspCode == HttpURLConnection.HTTP_OK) {
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            br.close();
            //JSONObject json = new JSONObject(sb.toString());
            
            System.out.println("sms success result:"+sb.toString());
        } else {
        	String errMsg = "http error " + httpRspCode + " " + conn.getResponseMessage();
        	
        	System.out.println("sms failed result:" + errMsg);
        }

	}
	
	/*
     * 发送语音短信
     * @param nationCode 国家码，如 86 为中国
     * @param phoneNumber 不带国家码的手机号
     * @param msg 消息类型
     * @param playtimes 播放次数
     * @param ext 服务端原样返回的参数，可填空
     * @return {@link}SmsVoiceVerifyCodeSenderResult
     * @throws Exception
     */
    public void sendVoice(
    		String nationCode,
    		String phoneNumber,
    		String msg,
    		int playtimes) throws Exception {

        long random = this.getRandom();
        long curTime = System.currentTimeMillis()/1000;

        ArrayList<String> phoneNumbers = new ArrayList<String>();
    	phoneNumbers.add(phoneNumber);

		// 按照协议组织 post 请求包体
		PojoVoiceMsg data = new PojoVoiceMsg();

        data.setTel(new PojoTel(nationCode, phoneNumber));
        data.setMsg(msg);
        data.setPlaytimes(playtimes);
        data.setSig(this.calculateSigForTempl(APPKEY, random, curTime, phoneNumbers));
        data.setTime((int)curTime);

        // 与上面的 random 必须一致
		String wholeUrl = String.format("%s?sdkappid=%d&random=%d", VOICE_URL, APPID,random);
        HttpURLConnection conn = this.getPostHttpConn(wholeUrl);

        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream(), "utf-8");
        wr.write(pojoUtils.toJson(data));
        wr.flush();

        // 显示 POST 请求返回的内容
        StringBuilder sb = new StringBuilder();
        int httpRspCode = conn.getResponseCode();

        if (httpRspCode == HttpURLConnection.HTTP_OK) {
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            br.close();
            //JSONObject json = new JSONObject(sb.toString());

            System.out.println("voice success result:"+sb.toString());
        } else {
        	
        	String errmsg = "http error " + httpRspCode + " " + conn.getResponseMessage();
        	
        	System.out.println("voice failed result:" + errmsg);
        }
    }
    
    
    protected String strToHash(String str) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        byte[] inputByteArray = str.getBytes();
        messageDigest.update(inputByteArray);
        byte[] resultByteArray = messageDigest.digest();
        return byteArrayToHex(resultByteArray);
    }

    public String byteArrayToHex(byte[] byteArray) {
        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        char[] resultCharArray = new char[byteArray.length * 2];
        int index = 0;
        for (byte b : byteArray) {
            resultCharArray[index++] = hexDigits[b >>> 4 & 0xf];
            resultCharArray[index++] = hexDigits[b & 0xf];
        }
        return new String(resultCharArray);
    }

    public int getRandom() {
    	
    	Random random = new Random();
    	
    	return random.nextInt(999999)%900000+100000;
    }

    public HttpURLConnection getPostHttpConn(String url) throws Exception {
        URL object = new URL(url);
        HttpURLConnection conn;
        conn = (HttpURLConnection) object.openConnection();
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Accept", "application/json");
        conn.setRequestMethod("POST");
        return conn;
	}


    public String calculateSigForTempl(
    		String appkey,
    		long random,
    		long curTime,
    		ArrayList<String> phoneNumbers) throws NoSuchAlgorithmException {
        String phoneNumbersString = phoneNumbers.get(0);
        for (int i = 1; i < phoneNumbers.size(); i++) {
            phoneNumbersString += "," + phoneNumbers.get(i);
        }
        return strToHash(String.format(
        		"appkey=%s&random=%d&time=%d&mobile=%s",
        		appkey, random, curTime, phoneNumbersString));
    }

}
