package com.aaaliua.utils;

import org.json.JSONException;
import org.json.JSONObject;

import com.aaaliua.entity.UserEntity;
import com.aaaliua.entity.ValidateCode;

public class ParseJson {

	public static final String SUCCESSFUL ="0";
	
	public static ValidateCode parseCode(String json){
		ValidateCode code = new ValidateCode();
		try {
			JSONObject obj = new JSONObject(json);
			code.setCode(obj.optString("status"));
			if(SUCCESSFUL.equals(code.getCode())){
				code.setCodename(obj.optString("data"));
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
			code = null;
		}
		return code;
	}
	
	
	public static UserEntity PARSEJSON_USER(String json){
		UserEntity ent = null;
		try {
			JSONObject obj = new JSONObject(json);
			String requestcode = obj.optString("status");
			if(SUCCESSFUL.equals(requestcode)){
				JSONObject entobj = obj.getJSONObject(UserEntity.JSON_MAP);
				ent = new UserEntity();
				ent.setUid(entobj.optString(UserEntity.JSON_uid));
				ent.setQq(entobj.optString(UserEntity.JSON_qq));
				ent.setWeix(entobj.optString(UserEntity.JSON_weix));
				ent.setWeib(entobj.optString(UserEntity.JSON_weib));
				ent.setPhone(entobj.optString(UserEntity.JSON_phone));
				ent.setPasswd(entobj.optString(UserEntity.JSON_passwd));
				ent.setNickname(entobj.optString(UserEntity.JSON_nickname));
				ent.setAge(entobj.optString(UserEntity.JSON_age));
				ent.setSex(entobj.optString(UserEntity.JSON_sex));
				ent.setIcon(entobj.optString(UserEntity.JSON_icon));
				ent.setSchoo(entobj.optString(UserEntity.JSON_school));
				ent.setAddtime(entobj.optString(UserEntity.JSON_addtime));
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
			ent = null;
		}
		
		return ent;
	}
}
