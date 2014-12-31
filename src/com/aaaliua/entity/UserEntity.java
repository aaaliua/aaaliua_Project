package com.aaaliua.entity;

import com.baoyz.pg.Parcelable;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * 用户基本信息
 * @author LT
 *
 */
@Parcelable
@DatabaseTable(tableName = "userentity")
public class UserEntity {

	
	public static final String l_ID = "id";
	
	public static final  String JSON_MAP = "data";
	public static final String JSON_uid = "uid";
	public static final String JSON_qq = "qq";
	public static final String JSON_weix = "weix";
	public static final String JSON_weib = "weib";
	public static final String JSON_phone = "phone";
	public static final String JSON_passwd = "passwd";
	public static final String JSON_nickname = "nickname";
	public static final String JSON_age = "age";
	public static final String JSON_sex = "sex";
	public static final String JSON_icon = "icon";
	public static final String JSON_school = "school";
	public static final String JSON_school_name = "sh_shool";
	public static final String JSON_school_address = "address";
	public static final String JSON_addtime = "addtime";
	
	@DatabaseField(generatedId = true, useGetSet = true, columnName = l_ID)
	private long id;
	@DatabaseField(useGetSet = true, columnName = JSON_uid)
	private String uid;
	@DatabaseField(useGetSet = true, columnName = JSON_qq)
	private String qq;
	@DatabaseField(useGetSet = true, columnName = JSON_weix)
	private String weix;
	@DatabaseField(useGetSet = true, columnName = JSON_weib)
	private String weib;
	@DatabaseField(useGetSet = true, columnName = JSON_phone)
	private String phone;
	@DatabaseField(useGetSet = true, columnName = JSON_passwd)
	private String passwd;
	@DatabaseField(useGetSet = true, columnName = JSON_nickname)
	private String nickname;
	@DatabaseField(useGetSet = true, columnName = JSON_age)
	private String age;
	@DatabaseField(useGetSet = true, columnName = JSON_sex)
	private String sex;
	@DatabaseField(useGetSet = true, columnName = JSON_icon)
	private String icon;
	@DatabaseField(useGetSet = true, columnName = JSON_school)
	private String schoo;
	@DatabaseField(useGetSet = true, columnName = JSON_addtime)
	private String addtime;
	@DatabaseField(useGetSet = true, columnName = JSON_school_name)
	private String schoo_name;
	@DatabaseField(useGetSet = true, columnName = JSON_school_address)
	private String schoo_address;
	
	
	
	
	
	
	public String getSchoo_address() {
		return schoo_address;
	}
	public void setSchoo_address(String schoo_address) {
		this.schoo_address = schoo_address;
	}
	public String getSchoo_name() {
		return schoo_name;
	}
	public void setSchoo_name(String schoo_name) {
		this.schoo_name = schoo_name;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getWeib() {
		return weib;
	}
	public void setWeib(String weib) {
		this.weib = weib;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getQq() {
		return qq;
	}
	public void setQq(String qq) {
		this.qq = qq;
	}
	public String getWeix() {
		return weix;
	}
	public void setWeix(String weix) {
		this.weix = weix;
	}
	
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getPasswd() {
		return passwd;
	}
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getSchoo() {
		return schoo;
	}
	public void setSchoo(String schoo) {
		this.schoo = schoo;
	}
	public String getAddtime() {
		return addtime;
	}
	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}
	
	
	
}
