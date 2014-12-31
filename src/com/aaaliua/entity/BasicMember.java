package com.aaaliua.entity;

public class BasicMember {
	public static final int size = 20;
	
	
	
	public static final String post_age = "age";
	public static final String post_sex = "sex";
	public static final String post_nickname = "nickname";
	public static final String post_icon = "icon";
	public static final String post_phone = "icon";
	
	
	private String icon;
	private String nickname;
	private String linkphone;
	private String age;
	private String mail;
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getLinkphone() {
		return linkphone;
	}
	public void setLinkphone(String linkphone) {
		this.linkphone = linkphone;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	
	
	
}
