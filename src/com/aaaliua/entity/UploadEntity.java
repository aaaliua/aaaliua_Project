package com.aaaliua.entity;

public class UploadEntity {

	public static final String POST_SCHOOL = "school";
	public static final String POST_UID = "uid";
	public static final String POST_IMAGE   = "image";
	public static final String POST_TITLE = "title";
	public static final String POST_TYPE    = "type";
	public static final String POST_NUM = "num";
	public static final String POST_UNIT = "unit";
	public static final String POST_PUBLISH_TYPE = "publish_type";
	public static final String POST_MONTH_NUM = "month_num";
	public static final String POST_MONEY = "money";
	public static final String POST_NEW_TYPE = "new_type";
	public static final String POST_DESCRIPTION = "description";
	public static final String POST_USERNAME= "username";
	public static final String POST_CONTACT = "contact";
	
	private String school;
	private String uid;
	private String image;
	private String title;
	private String type;
	private int num;
	private String unit;
	private ItemType publish_type;
	private String month_num;
	private double money;
	private String new_type;
	private String desc;
	private String username;
	private String contact;
	
	
	
	
	public double getMoney() {
		return money;
	}
	public void setMoney(double money) {
		this.money = money;
	}
	public ItemType getPublish_type() {
		return publish_type;
	}
	public void setPublish_type(ItemType publish_type) {
		this.publish_type = publish_type;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public String getSchool() {
		return school;
	}
	public void setSchool(String school) {
		this.school = school;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getMonth_num() {
		return month_num;
	}
	public void setMonth_num(String month_num) {
		this.month_num = month_num;
	}
	public String getNew_type() {
		return new_type;
	}
	public void setNew_type(String new_type) {
		this.new_type = new_type;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getContact() {
		return contact;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}
	
	
	
	
}
