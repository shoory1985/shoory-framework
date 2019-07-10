package com.shoory.framework.starter.utils;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class ServerTransactionFactory {
	@Bean
	public ServerTransactionFactory getServerTransactionFactory() {
		return new ServerTransactionFactory();
	}
	
	private int requestCount = 0;
	private final String hostAddress;
	private final String shortHostAddress;
	private final int processId;
	
	@Autowired
	private DateUtils dateUtils;
	
	public ServerTransactionFactory() {
		this.hostAddress = initHostAddress();
		this.shortHostAddress = initShortHostAddress();
		this.processId = initProcessID();
	}
	 
	
	private String initHostAddress() {
		InetAddress address = null;
		try {
			address = InetAddress.getLocalHost();
			return address.getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return "0.0.0.0";
	}

	private String initShortHostAddress() {
		String hostAddress = getHostAddress();
		String[] array = hostAddress.replace(".", "=").split("=");
		return array[array.length - 1];
	}

	private int initProcessID() {
		RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
		return Integer.valueOf(runtimeMXBean.getName().split("@")[0]).intValue();
	}
	

	public String produce() {
		String result = dateUtils.formatDateTimeSSS(new Date());
		if (requestCount >= 999999999) {
			requestCount = 0;
		}
		requestCount ++;
		result += "-" + String.valueOf(requestCount);
		result += "-" + getShortHostAddress() + "-" + getProcessId();
		
		return result;
	}


	public int getRequestCount() {
		return requestCount;
	}


	public String getHostAddress() {
		return hostAddress;
	}


	public String getShortHostAddress() {
		return shortHostAddress;
	}


	public int getProcessId() {
		return processId;
	}
	
	

}
