package com.aaaliua.event;
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
    
   
}
