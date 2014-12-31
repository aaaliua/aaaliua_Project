package com.aaaliua.user;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.aaaliua.application.AppApplication;
import com.aaaliua.database.BaseAppDbHelper;
import com.aaaliua.entity.UserEntity;
import com.aaaliua.event.Event.ChangeInfo;
import com.aaaliua.event.Event.Logout;
import com.aaaliua.event.Event.RegisterEvent;
import com.aaaliua.itemwork.R;
import com.aaaliua.utils.CommonUtils;
import com.aaaliua.utils.ImageOptions;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.dazhongcun.baseactivity.BaseActionBarActivity;
import com.dazhongcun.widget.CircleImageView;

import de.greenrobot.event.EventBus;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.TextView;

public class UserActivity extends BaseActionBarActivity {

	@InjectView(R.id.back)
	View back;
	@InjectView(R.id.name)
	TextView name;
	@InjectView(R.id.phone)
	TextView phone;
	@InjectView(R.id.school_name)
	TextView school_name;
	@InjectView(R.id.user_img)
	CircleImageView headImageView;
	@InjectView(R.id.version)
	TextView version;
	private BaseAppDbHelper<UserEntity> dbHelper = new BaseAppDbHelper<UserEntity>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_info);
		ButterKnife.inject(this);
		setInfo();
		// Register
		EventBus.getDefault().register(this);
		
		version.setText(String.format(getResources().getString(R.string.version), CommonUtils.getVersionName(this)));
	}

	public BaseAppDbHelper<UserEntity> getdb() {
		if (dbHelper != null) {
			return dbHelper;
		} else {
			return new BaseAppDbHelper<UserEntity>();
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// Unregister
		EventBus.getDefault().unregister(this);
	}

	/** 在主线程接收ItemListEvent事件，必须是public void */
	public void onEventMainThread(RegisterEvent event) {
		if (event != null && event.getFlag() == true) {
			setInfo();
		}
	}
	public void onEventMainThread(ChangeInfo event) {
	 setInfo();
	}
	
	private void setInfo(){
		if (dbHelper != null) {
			UserEntity dblogin = new UserEntity();
			dblogin = dbHelper.queryObjForEq(UserEntity.class,
					UserEntity.JSON_uid, LoginActivity.getTokenID());
			if(dblogin == null)
				return;
			name.setText(dblogin.getNickname());
			phone.setText(dblogin.getPhone());
			school_name.setText(dblogin.getSchoo_name());
			AppApplication.getImageLoader().displayImage(dblogin.getIcon(), headImageView,ImageOptions.defaultOptions);
		}
	}
	
	public void onEventMainThread(Logout event) {
		if(event != null){
			this.finish();
		}
	}

	@OnClick(R.id.selectSchool)
	public void onSelectClick(View v) {
		startActivity(new Intent(this, SelectSchoolActivity.class));
	}
	@OnClick(R.id.touserEdit)
	public void onTouserEditClick(View v) {
		startActivity(new Intent(this, EditInfo.class));
	}
	@OnClick(R.id.mylist)
	public void onListClick(View v) {
		startActivity(new Intent(this, MyList.class));
	}

	@OnClick(R.id.logout)
	public void onLogOutClick(View v) {
		new MaterialDialog.Builder(this).title("确认？")
				.content("确认退出?")
				.theme(Theme.LIGHT)
				// the default is light, so you don't
				// need this line
				.positiveText(R.string.accept)
				// the default is 'Accept'
				.positiveColor(getResources().getColor(R.color.tabbar_color))
				.negativeText(R.string.decline) // leaving this
												// line out
												// will remove
												// the negative
												// button
				.callback(new MaterialDialog.Callback() {

					@Override
					public void onPositive(MaterialDialog dialog) {
						LoginActivity.setTokenID("");
						EventBus.getDefault().post(new Logout());
//						UserActivity.this.finish();
						dialog.dismiss();
					}

					@Override
					public void onNegative(MaterialDialog dialog) {
						dialog.dismiss();
					}
				}).build().show();
	}

	@OnClick(R.id.back)
	public void onBackClick(View v) {
		finish();
	}
	@OnClick(R.id.aboutlayout)
	public void onAboutClick(View v) {
		Intent it = new Intent(this,HtmlView.class);
		it.putExtra("code", 0);
		startActivity(it);
	}
	@OnClick(R.id.xieyilayout)
	public void onXieyiClick(View v) {
		Intent it = new Intent(this,HtmlView.class);
		it.putExtra("code", 1);
		startActivity(it);
	}
}
