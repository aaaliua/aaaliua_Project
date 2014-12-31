package com.aaaliua.user;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.aaaliua.application.AppApplication;
import com.aaaliua.entity.ValidateCode;
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

@SuppressLint("NewApi")
public class RegisterActivity extends BaseActionBarActivity{

	
	@InjectView(R.id.back) View back;
	@InjectView(R.id.next_password) View nextPassword;
	@InjectView(R.id.validate_layout) View validate_layout;
	@InjectView(R.id.validate_code) Button  validate_btn;
	@InjectView(R.id.toxieyi)View xieyiView;
	@InjectView(R.id.select_xieyi)CheckBox selectXieyi;
	
	@InjectView(R.id.phone) EditText phone;
	@InjectView(R.id.code) EditText clientCode;
	private ValidateCode code;
	private CountDownButtonHelper helper;
	
	private BaseMember member;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_regist);
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
    public void onEventMainThread(RegisterEvent event) {
    	if(event != null && event.getFlag() == true){
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
			AppApplication.getHttpClient().post(RequestUrl.REGISTER_URL, params,new AsyncHttpResponseHandler(){

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
						Toast.makeText(RegisterActivity.this, content, Toast.LENGTH_LONG).show();
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
	@OnClick(R.id.toxieyi)
	public void onXieyiClick(View v){
		Intent it = new Intent(this,HtmlView.class);
		it.putExtra("code", 1);
		startActivity(it);
	}
	@OnClick(R.id.next_password)
	public void onNextPasswordClick(View v){
		if(selectXieyi.isChecked() == false){
			Toaster.showOneToast("若要注册账户，需要同意本软件的使用协议");
		}else{
			if(member.getCode() != null){
				if(clientCode.getText().toString().trim().isEmpty()){
					Toaster.showOneToast("请输入验证码");
				}else if(member.getCode().equals(clientCode.getText().toString().trim())){
					//验证两者验证码是否一样   一样就去输入密码
					Intent it = new Intent(this,ConfirmPasswordActivity.class);
					it.putExtra(ConfirmPasswordActivity.EXTR_PHONE, member.getPhone());
					startActivity(it);
					this.finish();
				}else{
					Toaster.showOneToast("验证密码输入不正确");
				}
				
			}else{
				Toaster.showOneToast("请输入手机号获取验证码");
			}
		}
		
		
	}
}
