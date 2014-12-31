package com.aaaliua.chatutils;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;


public class ChatUtils {
	public static void login(final String username,String pass){
		EMChatManager.getInstance().login(username, pass, new EMCallBack() {
			
			@Override
			public void onError(int arg0, String arg1) {
			}

			@Override
			public void onProgress(int arg0, String arg1) {
			}

			@Override
			public void onSuccess() {
			}
			
		});
	}

	
}
