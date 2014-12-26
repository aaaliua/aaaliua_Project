package com.aaaliua.user;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.aaaliua.application.AppApplication;
import com.aaaliua.database.BaseAppDbHelper;
import com.aaaliua.entity.BasicMember;
import com.aaaliua.entity.UserEntity;
import com.aaaliua.event.Event;
import com.aaaliua.event.Event.RegisterEvent;
import com.aaaliua.itemwork.R;
import com.aaaliua.utils.BaseMember;
import com.aaaliua.utils.ParseJson;
import com.aaaliua.utils.RequestUrl;
import com.aaaliua.utils.Toaster;
import com.dazhongcun.baseactivity.BaseActionBarActivity;
import com.dazhongcun.utils.PreferencesUtils;
import com.dazhongcun.utils.StringUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import de.greenrobot.event.EventBus;

public class LoginActivity extends BaseActionBarActivity {

	@InjectView(R.id.back)
	View back;
	@InjectView(R.id.login)
	View login;
	@InjectView(R.id.register_btn)
	View register;
	@InjectView(R.id.phone)
	EditText phone;
	@InjectView(R.id.password)
	EditText password;

	
	private BaseMember member ;
	private BaseAppDbHelper<UserEntity> dbHelper = new BaseAppDbHelper<UserEntity>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_login);
		ButterKnife.inject(this);
		 // Register
        EventBus.getDefault().register(this);
        member  = new BaseMember();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// Unregister
		EventBus.getDefault().unregister(this);
	}
	
	   /** 在主线程接收ItemListEvent事件，必须是public void */
    public void onEventMainThread(RegisterEvent event) {
    	if(event != null && event.getFlag() == true){
    		this.finish();
    	}
    }

	@OnClick(R.id.back)
	public void onBackClick(View v) {
		finish();
	}

	@OnClick(R.id.login)
	public void onloginClick(View v) {
//		startActivity(new Intent(this, UserActivity.class));
		if(validInput()){
			login();
		}
	}
	private void login(){
		RequestParams params = new RequestParams();
		params.put("user", member.getPhone());
		params.put("passwd", member.getPass());
		AppApplication.getHttpClient().post(RequestUrl.LOGIN, params,new AsyncHttpResponseHandler(){
			final ProgressDialog progressDialog = new ProgressDialog(
					LoginActivity.this);
			@Override
			@Deprecated
			public void onFailure(Throwable error) {
				super.onFailure(error);
			}

			@Override
			public void onFinish() {
				super.onFinish();

				if (progressDialog != null
						&& progressDialog.isShowing()) {
					progressDialog.dismiss();
				}
			}

			@Override
			public void onStart() {
				super.onStart();
				progressDialog.setMessage("正在登录");
				progressDialog.setCancelable(false);
				progressDialog.show();
			}

			@Override
			@Deprecated
			public void onSuccess(int statusCode, String content) {
				super.onSuccess(statusCode, content);
				UserEntity bean = ParseJson.PARSEJSON_USER(content);
				if(bean == null){
					return;
				}
				LoginActivity.setTokenID(bean.getUid());
				UserEntity dblogin = new UserEntity();
				dblogin = dbHelper.queryObjForEq(UserEntity.class,
						UserEntity.JSON_uid, bean.getUid());
				if (dblogin == null) {
					// 没有的话就创建对象 插入一条数据
					int i = dbHelper.create(bean);
				} else {
					// 如果有的话 吧数据库中的ID引用到请求后解析完成后的对象
					bean.setId(dblogin.getId());
					// 如果有localID就跟新数据库 否则插入数据库
					dbHelper.createOrUpdate(bean);
				}
				
				EventBus.getDefault().post(new Event.RegisterEvent(true));
				startActivity(new Intent(LoginActivity.this, UserActivity.class));
				
			}
			
		});
	}
	
	private boolean validInput() {
		member.setPhone(phone.getText().toString().trim());
		member.setPass(password.getText().toString().trim());
		if (TextUtils.isEmpty(member.getPhone())) {
			Toaster.showToast(this,"请输入手机号");
			phone.requestFocus();
			return false;
		} else if(!StringUtils.isMobileNO(phone.getText().toString())){
			Toaster.showToast(this,"请输入正确的手机号");
			phone.requestFocus();
			return false;
		}
		if(TextUtils.isEmpty(member.getPass())){
			Toaster.showToast(this,"请输入密码");
			password.requestFocus();
			return false;
		}else if (member.getPass().length() < 6) {
			Toaster.showToast(this,
					this.getResources().getString(R.string.reg_pwd_input_err));
			password.setText("");
			password.requestFocus();
			return false;
		}

		return true;
	}
	
	

	@OnClick(R.id.register_btn)
	public void onRegClick(View v) {
		startActivity(new Intent(this, RegisterActivity.class));
	}

	@OnClick(R.id.forgetPassword)
	public void onForgetPasswordClick(View v) {
		startActivity(new Intent(this, ForgetPassword.class));
	}

	public static void setTokenID(String tokenid) {
		PreferencesUtils.setStringPreferences(AppApplication.getContext(),
				AppApplication.PREFERENCE_NAME, UserEntity.JSON_uid, tokenid);
	}

	// 获取用户的Tokenid 为空则尚未登录
	public static String getTokenID() {
		return PreferencesUtils.getStringPreference(
				AppApplication.getContext(), AppApplication.PREFERENCE_NAME,
				UserEntity.JSON_uid, "");
	}
	
	public static BaseAppDbHelper<UserEntity> statusdbHelper =null;
	public static BaseAppDbHelper<UserEntity> getDBHelper(){
		if(statusdbHelper == null){
			statusdbHelper = new BaseAppDbHelper<UserEntity>();
			return statusdbHelper;
		}else{
			return statusdbHelper;
		}
	}
	
	public static UserEntity getUser(String id){
		UserEntity ent = new UserEntity();
		ent = getDBHelper().queryObjForEq(UserEntity.class,
				UserEntity.JSON_uid, id);
		if(ent != null){
			return ent;
		}else{
			return null;
		}
	}

}
