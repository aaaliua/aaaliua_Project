package com.aaaliua.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.aaaliua.itemwork.R;
import com.dazhongcun.baseactivity.BaseActionBarActivity;

public class ConfirmPasswordActivity extends BaseActionBarActivity{

	
	@InjectView(R.id.back) View back;
	@InjectView(R.id.next) View next;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_confirm_password);
		ButterKnife.inject(this);
	}
	
	
	
	@OnClick(R.id.back)
	public void onBackClick(View v){
		finish();
	}
	@OnClick(R.id.next)
	public void onRegClick(View v){
		startActivity(new Intent(this,SelectSchoolActivity.class));
	}
}
