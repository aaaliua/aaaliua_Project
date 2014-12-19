package com.aaaliua.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.aaaliua.helper.CountDownButtonHelper;
import com.aaaliua.helper.CountDownButtonHelper.OnFinishListener;
import com.aaaliua.itemwork.R;
import com.dazhongcun.baseactivity.BaseActionBarActivity;

public class RegisterActivity extends BaseActionBarActivity{

	
	@InjectView(R.id.back) View back;
	@InjectView(R.id.next_password) View nextPassword;
	@InjectView(R.id.validate_layout) View validate_layout;
	@InjectView(R.id.validate_code) Button  validate_btn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_regist);
		ButterKnife.inject(this);
	}
	
	@OnClick(R.id.validate_code)
	public void onValidate(View v){
		CountDownButtonHelper helper = new CountDownButtonHelper(validate_btn,
				"发送验证码", 60, 1);
		helper.setOnFinishListener(new OnFinishListener() {

			@Override
			public void finish() {
				Toast.makeText(RegisterActivity.this, "倒计时结束",
						Toast.LENGTH_SHORT).show();
			}
		});
		helper.start();
	}
	
	@OnClick(R.id.back)
	public void onBackClick(View v){
		finish();
	}
	@OnClick(R.id.next_password)
	public void onNextPasswordClick(View v){
		startActivity(new Intent(this,ConfirmPasswordActivity.class));
		this.finish();
	}
}
