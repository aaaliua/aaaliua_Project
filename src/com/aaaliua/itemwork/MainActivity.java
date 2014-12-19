package com.aaaliua.itemwork;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.aaaliua.fragment.ContentFragment;
import com.aaaliua.user.LoginActivity;
import com.aaaliua.user.UserActivity;
import com.dazhongcun.baseactivity.BaseFragmentActionBarActivity;
import com.dazhongcun.widget.CircleImageView;
import com.dazhongcun.widget.FragmentViewPagerAdapter;
import com.dazhongcun.widget.PagerSlidingTabStrip;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.content.Intent;
import android.os.Bundle;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;

public class MainActivity extends BaseFragmentActionBarActivity {

	@InjectView(R.id.sliding_tabs)
	PagerSlidingTabStrip mSlidingTabLayout;
	@InjectView(R.id.viewpager)
	ViewPager mViewPager;

	@InjectView(R.id.user_img)
	CircleImageView userImage;
	
	@InjectView(R.id.add)
	ImageView add;
	
	@InjectView(R.id.tab)
	RelativeLayout tab;
	final String[] TITLES = { "热门推荐", "图书", "手机电脑", "学习用品", "其他"};
	private List<Fragment> fragments;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ButterKnife.inject(this);

		if (VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
			// 透明状态栏
//			getWindow().addFlags(
//					WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS); // 顶部
			// 透明导航栏
//			 getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);//底部
		} else if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {

		}
		
		fragments = new ArrayList<Fragment>();
		for (int i = 0; i < TITLES.length; i++) {
			fragments.add(new ContentFragment());
		}
		
		
		mViewPager.setAdapter(new MyframPagerAdapter(
				getSupportFragmentManager(), mViewPager, fragments));

		mSlidingTabLayout.setViewPager(mViewPager);
		mSlidingTabLayout.setIndicatorColor(getResources().getColor(
				R.color.tabbar_color));
		mSlidingTabLayout.setDividerColor(getResources()
				.getColor(R.color.white));
		mSlidingTabLayout.setIndicatorHeight(0);
		mSlidingTabLayout.setTextColorResource(R.color.tabbar_color);
		mSlidingTabLayout.setTextFocusColorResource(R.color.black);
		mSlidingTabLayout.setUnderlineColorResource(R.color.divide_line);
		
//		 new Thread(new Runnable() {
//				public void run() {
//					try {
//						MainActivity.this.run("http://192.168.2.200:8080/AppInterface/");
//					} catch (IOException e) {
//						e.printStackTrace();
//					}
//				}
//			}).start();
		
	}
	
	
	public String run(String url) throws IOException {
		OkHttpClient client = new OkHttpClient();
	    Request request = new Request.Builder()
	        .url(url)
	        .build();

	    Response response = client.newCall(request).execute();
	    return response.body().string();
	  }
	
	
	@OnClick(R.id.user_img)
	public void toUser(View v){
		
		startActivity(new Intent(this,LoginActivity.class));
	}
	@OnClick(R.id.add)
	public void tologin(View v){
	}
	
	// viewpag适配器
	public class MyframPagerAdapter extends FragmentViewPagerAdapter {

		public MyframPagerAdapter(FragmentManager fragmentManager,
				ViewPager viewPager, List<Fragment> fragments) {
			super(fragmentManager, viewPager, fragments);
		}

		// 为指示器显示标题使用
		@Override
		public CharSequence getPageTitle(int position) {
			return TITLES[position];
		}

	}
	
}
