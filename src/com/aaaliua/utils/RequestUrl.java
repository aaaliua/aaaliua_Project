package com.aaaliua.utils;

public class RequestUrl {

	public static final String PRODUCT_URL = "http://www.wuyouquan.net/";
	public static final String DEBUG_URL = "http://www.wuyouquan.net/";
	public static final String BASE_URL = DEBUG_URL;
	
	
	//参数键名:phone
	public static final String REGISTER_URL = BASE_URL + "/register/index";
	// 参数键名:手机号 phone 密码 passwd 
	public static final String CONFIRMPASS_URL = BASE_URL + "/register/step2";
	//定位经纬度获取列表
	public static final String LOCATION_SELECT_URL = BASE_URL + "/register/step3";
}
