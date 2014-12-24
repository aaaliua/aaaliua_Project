package com.aaaliua.user;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.aaaliua.application.AppApplication;
import com.aaaliua.entity.UserEntity;
import com.aaaliua.entity.ValidateCode;
import com.aaaliua.event.Event;
import com.aaaliua.event.Event.Forget;
import com.aaaliua.event.Event.RegisterEvent;
import com.aaaliua.helper.CountDownButtonHelper;
import com.aaaliua.helper.CountDownButtonHelper.OnFinishListener;
import com.aaaliua.itemwork.R;
import com.aaaliua.utils.BaseMember;
import com.aaaliua.utils.ParseJson;
import com.aaaliua.utils.RequestUrl;
import com.aaaliua.utils.Toaster;
import com.dazhongcun.baseactivity.BaseActionBarActivity;
import com.dazhongcun.utils.StringUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import de.greenrobot.event.EventBus;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

@SuppressLint("NewApi")
public class ForgetPassword extends BaseActionBarActivity {

	@InjectView(R.id.phone) EditText phone;
	@InjectView(R.id.validate_code) Button  validate_btn;
	@InjectView(R.id.password)EditText pass;
	@InjectView(R.id.password2) EditText pass2;
	@InjectView(R.id.code) EditText clientCode;
	private CountDownButtonHelper helper;
	private BaseMember member;
	private ValidateCode code;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_reset_password);
		ButterKnife.inject(this);
		member = new BaseMember();
		// Register
        EventBus.getDefault().register(this);
	}
	
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		// Unregister
		EventBus.getDefault().unregister(this);
	}
	
	   /** 在主线程接收ItemListEvent事件，必须是public void */
    public void onEventMainThread(Forget event) {
    	if(event != null){
    		this.finish();
    	}
    }
	
	
	@OnClick(R.id.validate_code)
	public void onValidate(View v){
		
		if(phone.getText().toString().isEmpty()){
			Toaster.showOneToast("请输入手机号");
		}else if(StringUtils.isMobileNO(phone.getText().toString())){
			
			helper = new CountDownButtonHelper(validate_btn,
					"发送验证码", 60, 1);
			helper.setOnFinishListener(new OnFinishListener() {

				@Override
				public void finish() {
//					Toast.makeText(RegisterActivity.this, "倒计时结束",
//							Toast.LENGTH_SHORT).show();
				}
			});
			RequestParams params = new RequestParams();
			params.put("phone", phone.getText().toString());
			AppApplication.getHttpClient().post(RequestUrl.FORGET_PASSWORD, params,new AsyncHttpResponseHandler(){

				@Override
				@Deprecated
				public void onFailure(Throwable error) {
					super.onFailure(error);
				}

				@Override
				public void onFinish() {
					super.onFinish();
				}

				@Override
				public void onStart() {
					super.onStart();
				}

				@Override
				@Deprecated
				public void onSuccess(int statusCode, String content) {
					super.onSuccess(statusCode, content);
					code = ParseJson.parseCode(content);
					if(code != null){
						Toast.makeText(ForgetPassword.this, content, Toast.LENGTH_LONG).show();
						member.setPhone(phone.getText().toString().trim());
						member.setCode(code.getCodename());
						Toaster.showOneToast("发送成功!");
						helper.start();
					}else{
						System.out.println(content);
//						Toaster.showOneToast(content);
					}
				}
				
			});
			
		}else{
			Toaster.showOneToast("请输入正确的手机号");
		}
		
	}
	
	@OnClick(R.id.back)
	public void onBackClick(View v){
		finish();
	}
	@OnClick(R.id.next_password)
	public void donePassword(View v){
		if(validInput()){
			getData();
		}
	}
	
	private void getData(){
			RequestParams params = new RequestParams();
			params.put("phone", member.getPhone());
			params.put("passwd", member.getPass());
			AppApplication.getHttpClient().post(RequestUrl.LOGIN_FORGET, params,new AsyncHttpResponseHandler(){

				@Override
				@Deprecated
				public void onFailure(Throwable error) {
					super.onFailure(error);
				}

				@Override
				public void onFinish() {
					super.onFinish();
				}

				@Override
				public void onStart() {
					super.onStart();
				}

				@Override
				@Deprecated
				public void onSuccess(int statusCode, String content) {
					super.onSuccess(statusCode, content);
					try {
						JSONObject obj = new JSONObject(content);
						String code = obj.optString("status");
						if("0".equals(code)){
							Toaster.showDefToast(ForgetPassword.this, "重置成功，请使用新密码登录...");
							EventBus.getDefault().post(new Forget());
						}else{
							Toaster.showDefToast(ForgetPassword.this, "请求失败，服务器异常");
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				
			});
		}
	
	
	private boolean validInput() {
		if (TextUtils.isEmpty(phone.getText().toString().trim())) {
			Toaster.showToast(this,
					"手机号不允许为空");
			phone.requestFocus();
			return false;
		} 
		
		
		if(member.getCode() != null){
			if(clientCode.getText().toString().trim().isEmpty()){
				Toaster.showOneToast("请输入验证码");
				clientCode.requestFocus();
				return false;
			}else if(member.getCode().equals(clientCode.getText().toString().trim())){
				//验证两者验证码是否一样   一样就去输入密码
//				Intent it = new Intent(this,ConfirmPasswordActivity.class);
//				it.putExtra(ConfirmPasswordActivity.EXTR_PHONE, member.getPhone());
//				startActivity(it);
//				this.finish();
			}else{
				Toaster.showOneToast("验证码输入不正确");
				clientCode.requestFocus();
				return false;
			}
			
		}else{
			Toaster.showOneToast("请输入手机号获取验证码");
			clientCode.requestFocus();
			return false;
		}
		
		
		
		
		member.setPass(pass.getText().toString().trim());
		member.setRepass(pass2.getText().toString().trim());
		if (TextUtils.isEmpty(member.getPass())) {
			Toaster.showToast(this,
					this.getResources().getString(R.string.reg_pwd_input_empty));
			pass.requestFocus();
			return false;
		} else if (member.getPass().length() < 6) {
			Toaster.showToast(this,
					this.getResources().getString(R.string.reg_pwd_input_err));
			pass.setText("");
			pass.requestFocus();
			return false;
		}

		if (TextUtils.isEmpty(member.getRepass())) {
			Toaster.showToast(
					this,
					this.getResources().getString(
							R.string.reg_repwd_input_empty));
			pass2.requestFocus();
			return false;
		}
		if (!member.getPass().equals(member.getRepass())) {
			Toaster.showToast(this,
					this.getResources().getString(R.string.reg_pwd_err));
			pass.setText("");
			pass2.setText("");
			pass.requestFocus();
			return false;
		}
		
		
		return true;
	}
	
	
}
