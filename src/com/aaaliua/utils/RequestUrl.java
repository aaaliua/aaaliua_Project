package com.aaaliua.utils;

public class RequestUrl {

	public static final String PRODUCT_URL = "http://www.wuyouquan.net";
	public static final String DEBUG_URL = "http://www.wuyouquan.net";
	public static final String BASE_URL = DEBUG_URL;
	
	
	//参数键名:phone
	public static final String REGISTER_URL = BASE_URL + "/register/index";
	// 参数键名:手机号 phone 密码 passwd 
	public static final String CONFIRMPASS_URL = BASE_URL + "/register/step2";
	//定位经纬度获取列表
	public static final String LOCATION_SELECT_URL = BASE_URL + "/register/step3";
	public static final String IN_SCHOOL = BASE_URL + "/register/step4";
	//登录
	public static final String LOGIN = BASE_URL + "/login/index";
	//忘记密码
	public static final String LOGIN_FORGET = BASE_URL + "/login/forget2";
	//搜索
	public static final String LOCATION_SEARCH = BASE_URL + "/register/search";
	//重置密码获取验证码
	public static final String FORGET_PASSWORD = BASE_URL + "/login/forget1";
	//重置密码
	public static final String FORGET_PASSWORD2 = BASE_URL + "/login/forget2";
	//类型
	public static final String GET_TYPE = BASE_URL + "/message/gopublish";
	//上传图片获取路径
	public static final String GET_UPLOADFILE = BASE_URL + "/message/upload";
	//发布
	public static final String UPLOAD = BASE_URL + "/message/publish";
}
