package com.aaaliua.entity;

/**
 * 用户评论的实体类
 * @author LT
 *
 */
public class PostUser {

	public static final String POST_ID = "id"; // 用户ID
	public static final String POST_MESSAGE_ID = "message_id"; // 用户姓名
	public static final String POST_UID = "uid"; // 用户头像
	public static final String POST_CONTENT = "content"; // 用户评论的内容
	public static final String POST_ADDTIME = "addtime"; // 用户评论的时间
	public static final String POST_ICON = "icon"; // 学校
	public static final String POST_SH_SHOOL = "sh_shool"; //别名

	
	public static final String POST_NICKNAME = "nickname";
	
	
	private String id;
	private String msgid;
	private String uid;
	private String content;
	private String addtime;
	private String icon;
	private String nickname;
	
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
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMsgid() {
		return msgid;
	}
	public void setMsgid(String msgid) {
		this.msgid = msgid;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getAddtime() {
		return addtime;
	}
	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}


	
}
