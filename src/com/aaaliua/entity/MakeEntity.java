package com.aaaliua.entity;

public class MakeEntity {

	public static final String JSON_MAP = "data";

	public static final int ITEM = 0X111;
	public static final int LOADMORE = 0X112;
	public static final int NODATA = 0X113;

	
	public static final String MAKE_ID = "sh_id";
	public static final String MAKE_CITY = "sh_city";
	public static final String MAKE_NAME = "sh_shool";
	public static final String MAKE_ADDRESS = "address";

	private String id;
	private String city;
	private String name;
	private String address;
	private int loadtype;
	
	
	
	
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public int getLoadtype() {
		return loadtype;
	}
	public void setLoadtype(int loadtype) {
		this.loadtype = loadtype;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	
	
	
}
