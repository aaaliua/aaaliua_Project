package com.aaaliua.user;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.aaaliua.itemwork.R;
import com.aaaliua.utils.Toaster;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.dazhongcun.baseactivity.BaseActionBarActivity;

public class SelectSchoolActivity extends BaseActionBarActivity{

	
	@InjectView(R.id.back) View back;
	@InjectView(R.id.query) EditText query;
	@InjectView(R.id.hint) TextView hint;
	public LocationClient mLocationClient = null;
	public BDLocationListener myListener = new MyLocationListener();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_select_school);
		ButterKnife.inject(this);
		
		
		 mLocationClient = new LocationClient(getApplicationContext());   
		 mLocationClient.registerLocationListener(myListener);
		 mLocationClient.start();
		 
		 query.setOnKeyListener(new OnKeyListener() {
			
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if(keyCode == KeyEvent.KEYCODE_ENTER){
					Toast.makeText(SelectSchoolActivity.this, query.getText().toString(), Toast.LENGTH_LONG).show();
				}
				if(keyCode == KeyEvent.KEYCODE_BACK){
					onBackPressed();
				}
				return true;
			}
		});
	}
	
	@OnClick(R.id.back)
	public void onBackClick(View v){
		finish();
	}
	
	
	
	public class MyLocationListener implements BDLocationListener{

		@Override
		public void onReceiveLocation(BDLocation location) {
			if(location == null){
				Toaster.showOneToast("定位你失败");
				return;
			}
			getLocationList(location.getLatitude(),location.getLongitude());	
		}
		
	}
	
	
	//传经纬度给服务器 让服务器给我们列表   维度 经度
	private void getLocationList(double latitude,double longitude){
		hint.setText(latitude + "----------" + longitude);
	}
}
