package com.aaaliua.user;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.EditText;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.aaaliua.itemwork.R;
import com.dazhongcun.baseactivity.BaseActionBarActivity;

public class SelectSchoolActivity extends BaseActionBarActivity{

	
	@InjectView(R.id.back) View back;
	@InjectView(R.id.query) EditText query;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_select_school);
		ButterKnife.inject(this);
		
		query.setOnKeyListener(new OnKeyListener() {
			
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if(keyCode == KeyEvent.KEYCODE_ENTER){
					Toast.makeText(SelectSchoolActivity.this, query.getText().toString(), Toast.LENGTH_LONG).show();
				}
				return true;
			}
		});
	}
	
	@OnClick(R.id.back)
	public void onBackClick(View v){
		finish();
	}
}
