package com.aaaliua.entity;

import java.io.Serializable;
import java.util.List;

public class BaseType implements Serializable{

	public static final String JSON_UNIT = "unit";
	public static final String JSON_PUBLISH_TYPE = "publish_type";
	public static final String JSON_TYPE = "type";
	public static final String JSON_NEW_TYPE = "new_type";
	public static final String JSON_MONTH_NUM = "month_num";
	
	
	private List<ItemType> units;
	private List<ItemType> publish_type;
	private List<ItemType> type;
	private List<ItemType> new_type;
	private List<ItemType> month_num;
	public List<ItemType> getUnits() {
		return units;
	}
	public void setUnits(List<ItemType> units) {
		this.units = units;
	}
	public List<ItemType> getPublish_type() {
		return publish_type;
	}
	public void setPublish_type(List<ItemType> publish_type) {
		this.publish_type = publish_type;
	}
	public List<ItemType> getType() {
		return type;
	}
	public void setType(List<ItemType> type) {
		this.type = type;
	}
	public List<ItemType> getNew_type() {
		return new_type;
	}
	public void setNew_type(List<ItemType> new_type) {
		this.new_type = new_type;
	}
	public List<ItemType> getMonth_num() {
		return month_num;
	}
	public void setMonth_num(List<ItemType> month_num) {
		this.month_num = month_num;
	}
	
	
	
}
