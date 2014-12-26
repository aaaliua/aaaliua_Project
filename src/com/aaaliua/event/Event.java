package com.aaaliua.event;

import java.util.List;

import com.aaaliua.entity.BaseType;

public class Event {
	 /** 列表加载事件 */
    public static class RegisterEvent {
        private boolean flag;

        public RegisterEvent(Boolean flag) {
            this.flag = flag;
        }

        public boolean getFlag() {
            return flag;
        }
    }
    
    public static class Logout{
    	
    }
    public static class Forget{
    	
    }
    public static class NotifacationPic{
    	
    }
    public static class NotifacationType{
    	private BaseType type;
    	public NotifacationType(BaseType type){
    		this.type = type;
    	}
    	public BaseType getType(){
    		return type;
    	}
    	
    }
    public static class NotifacationPics{
    	private List<String> picPath;
    	public NotifacationPics(List<String> picPath){
    		this.picPath = picPath;
    	}
    	public List<String> getPics(){
    		return picPath;
    	}
    	
    }
    public static class NotifacationAddPic{
//    	private String picPath;
//    	public NotifacationAddPic(String picPath){
//    		this.picPath = picPath;
//    	}
//    	public String getPics(){
//    		return picPath;
//    	}
    	
    }
    
   
}
