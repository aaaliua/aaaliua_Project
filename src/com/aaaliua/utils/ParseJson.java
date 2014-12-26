package com.aaaliua.utils;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.aaaliua.entity.BaseType;
import com.aaaliua.entity.ItemType;
import com.aaaliua.entity.MakeEntity;
import com.aaaliua.entity.Status;
import com.aaaliua.entity.UserEntity;
import com.aaaliua.entity.ValidateCode;

public class ParseJson {

	public static final String SUCCESSFUL ="0";
	
	
	
	//错误状态解析
		public static Status getStatus(String jsonStr){
			Status st = new Status();
			try {
				JSONObject object = new JSONObject(jsonStr);
				st.setStatus(object.optString(Status.JSON_STATUS));
				st.setCode(object.optString(Status.JSON_CODE));
				st.setMsg(object.optString(Status.JSON_MSG));
				st.setTime(object.optString(Status.JSON_TIME));
				return st;
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}
	
		
		//解析发布类型
		public static BaseType parseType(String json){
			BaseType type = null ;
			try {
				
				JSONObject obj = new JSONObject(json);
				String requestcode = obj.optString("status");
				if(SUCCESSFUL.equals(requestcode)){
					type = new BaseType();
					JSONObject entobj = obj.getJSONObject(UserEntity.JSON_MAP);
					//解析单位
					JSONArray unitarr = entobj.getJSONArray(BaseType.JSON_UNIT);
					List<ItemType> unlist = new ArrayList<ItemType>();
					ItemType unselect = new ItemType();
					unselect.setId("-1");
					unselect.setName("请选择:");
					unlist.add(unselect);
					for(int i = 0;i<unitarr.length();i++){
						ItemType tp = new ItemType();
						JSONObject unobj = unitarr.getJSONObject(i);
						tp.setId(unobj.optString(ItemType.JSON_ID));
						tp.setName(unobj.optString(ItemType.JSON_NAME));
						unlist.add(tp);
					}
					type.setUnits(unlist);
					//解析状态
					JSONArray pubarray = entobj.getJSONArray(BaseType.JSON_PUBLISH_TYPE);
					List<ItemType> publist = new ArrayList<ItemType>();
					publist.add(unselect);
					for(int i = 0;i<pubarray.length();i++){
						ItemType tp = new ItemType();
						JSONObject unobj = pubarray.getJSONObject(i);
						tp.setId(unobj.optString(ItemType.JSON_ID));
						tp.setName(unobj.optString(ItemType.JSON_NAME));
						publist.add(tp);
					}
					type.setPublish_type(publist);
					//解析类型
					JSONArray typearray = entobj.getJSONArray(BaseType.JSON_TYPE);
					List<ItemType> typelist = new ArrayList<ItemType>();
					typelist.add(unselect);
					for(int i = 0;i<typearray.length();i++){
						ItemType tp = new ItemType();
						JSONObject unobj = typearray.getJSONObject(i);
						tp.setId(unobj.optString(ItemType.JSON_ID));
						tp.setName(unobj.optString(ItemType.JSON_NAME));
						typelist.add(tp);
					}
					type.setType(typelist);
					//解析新旧
					JSONArray newarray = entobj.getJSONArray(BaseType.JSON_NEW_TYPE);
					List<ItemType> newlist = new ArrayList<ItemType>();
					newlist.add(unselect);
					for(int i = 0;i<newarray.length();i++){
						ItemType tp = new ItemType();
						JSONObject unobj = newarray.getJSONObject(i);
						tp.setId(unobj.optString(ItemType.JSON_ID));
						tp.setName(unobj.optString(ItemType.JSON_NAME));
						newlist.add(tp);
					}
					type.setNew_type(newlist);
					//解析年月
					JSONArray monarray = entobj.getJSONArray(BaseType.JSON_MONTH_NUM);
					List<ItemType> monlist = new ArrayList<ItemType>();
					monlist.add(unselect);
					for(int i = 0;i<monarray.length();i++){
						ItemType tp = new ItemType();
						JSONObject unobj = monarray.getJSONObject(i);
						tp.setId(unobj.optString(ItemType.JSON_ID));
						tp.setName(unobj.optString(ItemType.JSON_NAME));
						monlist.add(tp);
					}
					type.setMonth_num(monlist);
				}else{
					
					Toaster.showOneToast(obj.optString("message"));
					type = null;
				}
				
			} catch (JSONException e) {
				e.printStackTrace();
				type = null;
			}
			
			return type;
		}
	
	public static ValidateCode parseCode(String json){
		ValidateCode code = new ValidateCode();
		try {
			JSONObject obj = new JSONObject(json);
			code.setCode(obj.optString("status"));
			if(SUCCESSFUL.equals(code.getCode())){
				code.setCodename(obj.optString("data"));
			}else{
				
				Toaster.showOneToast(obj.optString("message"));
				code = null;
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
			code = null;
		}
		return code;
	}
	
	
	public static UserEntity PARSEJSON_USER(String json){
		System.out.println(json);
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
				ent.setSchoo_name(entobj.optString(UserEntity.JSON_school_name));
				ent.setSchoo_address(entobj.optString(UserEntity.JSON_school_address));
				ent.setAddtime(entobj.optString(UserEntity.JSON_addtime));
			}else{
				Toaster.showOneToast(obj.optString("message"));
				ent = null;
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
			ent = null;
		}
		
		return ent;
	}
	
	
	
	//列表
		public static List<MakeEntity> getMakeEntityList(String jsonStr){
			List<MakeEntity> lists= new ArrayList<MakeEntity>();
			try {
				JSONObject json = new JSONObject(jsonStr);
				String code = json.optString(Status.JSON_STATUS);
				if("0".equals(code)){
					JSONArray objarr = json.getJSONArray("data");
					for(int i = 0 ;i<objarr.length();i++){
						JSONObject obj = objarr.getJSONObject(i);
						MakeEntity entity = new MakeEntity();
						entity.setId(obj.optString(MakeEntity.MAKE_ID));
						entity.setName(obj.optString(MakeEntity.MAKE_NAME));
						entity.setCity(obj.optString(MakeEntity.MAKE_CITY));
						entity.setAddress(obj.optString(MakeEntity.MAKE_ADDRESS));
						entity.setLoadtype(MakeEntity.ITEM);
						lists.add(entity);
					}
					
					return lists;
				}else{
						Toaster.showOneToast(json.optString("message"));
					return null;
				}
				
			} catch (Exception e) {
				e.printStackTrace();
				
			}
			return null;
		}
}
