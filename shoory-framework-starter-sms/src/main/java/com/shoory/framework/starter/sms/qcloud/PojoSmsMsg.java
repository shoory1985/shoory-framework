package com.shoory.framework.starter.sms.qcloud;

import java.util.List;

import lombok.Data;

@Data
public class PojoSmsMsg {
	private String tpl_id;
	private List<String> params;
	private String sig;
	private PojoTel tel;
	private int time;
	private String extend = "";
	private String ext = "";
}
