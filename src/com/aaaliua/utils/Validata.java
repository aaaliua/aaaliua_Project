package com.aaaliua.utils;

import android.text.TextUtils;

import com.aaaliua.entity.UploadEntity;
import com.aaaliua.photo.Bimp;

public class Validata {

	public static boolean validataPost(UploadEntity ent){
		if(Bimp.drr.size() == 0){
			Toaster.showOneToast("至少上传一张图片");
			return false;
		}
		if(TextUtils.isEmpty(ent.getTitle())){
			Toaster.showOneToast("请输入标题");
			return false;
		}
		
		if(ent.getType()==null || "".equals(ent.getType()) || "-1".equals(ent.getType())){
			Toaster.showOneToast("请选择类型");
			return false;
		}
		if(ent.getNum() <= 0){
			Toaster.showOneToast("数量必须大于等于1");
			return false;
		}
		if(ent.getUnit()==null || "".equals(ent.getUnit()) || "-1".equals(ent.getUnit())){
			Toaster.showOneToast("请选择单位");
			return false;
		}
		if(ent.getPublish_type()==null || "".equals(ent.getPublish_type().getId()) || "-1".equals(ent.getPublish_type().getId())){
			Toaster.showOneToast("请选择状态");
			return false;
		}else if("卖".equals(ent.getPublish_type().getName())){
			if(ent.getMoney() <= 0){
				Toaster.showOneToast("卖出必须大于等于1毛钱");
				return false;
			}
		}
		if(ent.getNew_type()==null || "".equals(ent.getNew_type()) || "-1".equals(ent.getNew_type())){
			Toaster.showOneToast("请选择新旧程度");
			return false;
		}
		
		
		
		
		
		return true;
	}
}
