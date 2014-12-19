package com.aaaliua.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.aaaliua.itemwork.R;
import com.dazhongcun.baseactivity.BaseActionBarActivity;

public class LoginActivity extends BaseActionBarActivity{

	
	@InjectView(R.id.back) View back;
	@InjectView(R.id.login) View login;
	@InjectView(R.id.register_btn) View register;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_login);
		ButterKnife.inject(this);
	}
	
	
	
	@OnClick(R.id.back)
	public void onBackClick(View v){
		finish();
	}
	@OnClick(R.id.login)
	public void onloginClick(View v){
		startActivity(new Intent(this,UserActivity.class));
	}
	@OnClick(R.id.register_btn)
	public void onRegClick(View v){
		startActivity(new Intent(this,RegisterActivity.class));
	}
}
