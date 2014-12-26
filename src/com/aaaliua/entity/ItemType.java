package com.aaaliua.entity;

import java.io.Serializable;

public class ItemType implements Serializable{

	public static final String JSON_ID = "id";
	public static final String JSON_NAME = "name";
	
	
	private String id;
	private String name;
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
	
	@Override
	public String toString() {
		return name;
	}
	
}
