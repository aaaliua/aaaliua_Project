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
	//编辑
	public static final String UPLOAD_edit = BASE_URL + "/message/editmessage";
	//获取数据
	public static final String GET_DATA = BASE_URL + "/message/messageByType";
	//获取评论
	public static final String GET_MSG = BASE_URL + "/message/getdiscuss";
	//评论
	public static final String POST_MSG = BASE_URL + "/message/dodiscuss";
	//赞
	public static final String POST_zan = BASE_URL + "/message/praise";
	//我的列表
	public static final String MY_LIST = BASE_URL + "/message/personMessageList";
	//删除
	public static final String delete_info = BASE_URL + "/message/deleteMessage";
	//改变状态 status
	public static final String CHANGE_STATUS = BASE_URL + "/message/changeState";
	//修改用户资料
	public static final String EDIT_USER = BASE_URL + "/register/editUser";
}
