package com.aaaliua.user;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.aaaliua.application.AppApplication;
import com.aaaliua.itemwork.R;
import com.aaaliua.utils.BaseMember;
import com.aaaliua.utils.ParseJson;
import com.aaaliua.utils.RequestUrl;
import com.aaaliua.utils.Toaster;
import com.dazhongcun.baseactivity.BaseActionBarActivity;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class ConfirmPasswordActivity extends BaseActionBarActivity{

	public static final String EXTR_PHONE = "PHONE";
	
	@InjectView(R.id.back) View back;
	@InjectView(R.id.next) View next;
	@InjectView(R.id.password) EditText pass;
	@InjectView(R.id.password2) EditText pass2;
	private String phone;
	
	private BaseMember member;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_confirm_password);
		ButterKnife.inject(this);
		phone = getIntent().getStringExtra(EXTR_PHONE);
		member = new BaseMember();
	}
	
	
	
	@OnClick(R.id.back)
	public void onBackClick(View v){
		finish();
	}
	@OnClick(R.id.next)
	public void onRegClick(View v){
		if(validInput()){
			confimPassword(v);
			
		}
	}
	
	private void confimPassword(View v){
		RequestParams params = new RequestParams();
		params.put("phone", phone);
		params.put("passwd", member.getCode());
		AppApplication.getHttpClient().post(RequestUrl.CONFIRMPASS_URL, params,new AsyncHttpResponseHandler(){

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
				
				startActivity(new Intent(ConfirmPasswordActivity.this,SelectSchoolActivity.class));
			}
			
		});
	}
	
	private boolean validInput() {
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
