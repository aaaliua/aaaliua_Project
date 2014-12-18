package com.aaaliua.user;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.aaaliua.itemwork.R;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

public class UserActivity extends ActionBarActivity{

	@InjectView(R.id.back) View back;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_info);
		ButterKnife.inject(this);
	}
	
	
	@OnClick(R.id.back)
	public void onBackClick(View v){
		finish();
	}
}
