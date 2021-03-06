package com.aaaliua.itemwork;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.aaaliua.application.AppApplication;
import com.aaaliua.chatutils.ChatHistoryAllActivity;
import com.aaaliua.chatutils.ChatUtils;
import com.aaaliua.database.BaseAppDbHelper;
import com.aaaliua.entity.UserEntity;
import com.aaaliua.event.Event;
import com.aaaliua.event.Event.ChangeInfo;
import com.aaaliua.event.Event.Logout;
import com.aaaliua.event.Event.RegisterEvent;
import com.aaaliua.fragment.ContentFragment;
import com.aaaliua.ui.AddItem;
import com.aaaliua.user.LoginActivity;
import com.aaaliua.user.UserActivity;
import com.aaaliua.utils.CommonUtils;
import com.aaaliua.utils.Toaster;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.dazhongcun.baseactivity.BaseFragmentActionBarActivity;
import com.dazhongcun.widget.CircleImageView;
import com.dazhongcun.widget.FragmentViewPagerAdapter;
import com.dazhongcun.widget.PagerSlidingTabStrip;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.NotificationCompat;
import com.easemob.chat.EMMessage.ChatType;
import com.easemob.chat.EMMessage.Type;
import com.easemob.util.EasyUtils;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import de.greenrobot.event.EventBus;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
	final String[] TITLES = { "全部", "图书", "手机电脑", "学习用品" };
	private List<Fragment> fragments;

	
	public static MainActivity activityInstance = null;
	protected NotificationManager notificationManager;
	private NewMessageBroadcastReceiver msgReceiver;
	
	
	@InjectView(R.id.unread_msg_number)
	TextView countview;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ButterKnife.inject(this);
		
		activityInstance = this;
		notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		// 注册一个接收消息的BroadcastReceiver
				msgReceiver = new NewMessageBroadcastReceiver();
				IntentFilter intentFilter = new IntentFilter(EMChatManager.getInstance().getNewMessageBroadcastAction());
				intentFilter.setPriority(3);
				registerReceiver(msgReceiver, intentFilter);
		
		if (VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
			// 透明状态栏
			// getWindow().addFlags(
			// WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS); // 顶部
			// 透明导航栏
			// getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);//底部
		} else if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {

		}
		//Register
		EventBus.getDefault().register(this);
		
		fragments = new ArrayList<Fragment>();
		for (int i = 0; i < TITLES.length; i++) {
			fragments.add(ContentFragment.newInstance(i));
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

		// new Thread(new Runnable() {
		// public void run() {
		// try {
		// MainActivity.this.run("http://192.168.2.200:8080/AppInterface/");
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		// }
		// }).start();
		
		if (LoginActivity.getTokenID() == null
				|| "".equals(LoginActivity.getTokenID())) {
			
		}else{
			BaseAppDbHelper<UserEntity> dbHelper = new BaseAppDbHelper<UserEntity>();
			UserEntity dblogin = new UserEntity();
			dblogin = dbHelper.queryObjForEq(UserEntity.class,
					UserEntity.JSON_uid, LoginActivity.getTokenID());
			if(dblogin != null){
				ChatUtils.login(dblogin.getUid(), dblogin.getPasswd());
			}
		}
		
		
		EventBus.getDefault().post(new ChangeInfo());
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		// Unregister
		EventBus.getDefault().unregister(this);
	}
	public void onEventMainThread(ChangeInfo event) {
		BaseAppDbHelper<UserEntity> dbHelper = new BaseAppDbHelper<UserEntity>();
		UserEntity dblogin = new UserEntity();
		if(TextUtils.isEmpty(LoginActivity.getTokenID())){
			
		}else{
			dblogin = dbHelper.queryObjForEq(UserEntity.class,
					UserEntity.JSON_uid, LoginActivity.getTokenID());
			AppApplication.getImageLoader().displayImage(dblogin.getIcon(),userImage);
		}
	}
	
	public void onEventMainThread(RegisterEvent event) {
		if (event != null && event.getFlag() == true) {
			fragments = new ArrayList<Fragment>();
			for (int i = 0; i < TITLES.length; i++) {
				fragments.add(ContentFragment.newInstance(i));
			}

			mViewPager.setAdapter(new MyframPagerAdapter(
					getSupportFragmentManager(), mViewPager, fragments));

			mSlidingTabLayout.setViewPager(mViewPager);
			mSlidingTabLayout.setIndicatorColor(getResources().getColor(
					R.color.tabbar_color));
			mSlidingTabLayout.setDividerColor(getResources().getColor(
					R.color.white));
			mSlidingTabLayout.setIndicatorHeight(0);
			mSlidingTabLayout.setTextColorResource(R.color.tabbar_color);
			mSlidingTabLayout.setTextFocusColorResource(R.color.black);
			mSlidingTabLayout.setUnderlineColorResource(R.color.divide_line);
		}
		
	
		
	}

	public String run(String url) throws IOException {
		OkHttpClient client = new OkHttpClient();
		Request request = new Request.Builder().url(url).build();

		Response response = client.newCall(request).execute();
		return response.body().string();
	}
	
	@OnClick(R.id.message)
	public void toMsg(View v){
		startActivity(new Intent(this, ChatHistoryAllActivity.class));
	}

	@OnClick(R.id.user_img)
	public void toUser(View v) {
		if (LoginActivity.getTokenID() == null
				|| "".equals(LoginActivity.getTokenID())) {

			startActivity(new Intent(this, LoginActivity.class));
		} else {
			startActivity(new Intent(this, UserActivity.class));
		}
	}

	@OnClick(R.id.add)
	public void tologin(View v) {
		if (LoginActivity.getTokenID() == null
				|| "".equals(LoginActivity.getTokenID())) {
			new MaterialDialog.Builder(this)
					.title("登录")
					.content("您尚未登录，无法发布物品信息,是否登录?")
					.theme(Theme.LIGHT)
					// the default is light, so you don't
					// need this line
					.positiveText(R.string.accept)
					// the default is 'Accept'
					.positiveColor(
							getResources().getColor(R.color.tabbar_color))
					.negativeText(R.string.decline) // leaving this
													// line out
													// will remove
													// the negative
													// button
					.callback(new MaterialDialog.Callback() {

						@Override
						public void onPositive(MaterialDialog dialog) {
							startActivity(new Intent(MainActivity.this,
									LoginActivity.class));
							dialog.dismiss();
						}

						@Override
						public void onNegative(MaterialDialog dialog) {
							dialog.dismiss();
						}
					}).build().show();
		} else if (LoginActivity.getUser(LoginActivity.getTokenID()).getSchoo() == null
				|| "".equals(LoginActivity.getUser(LoginActivity.getTokenID())
						.getSchoo())) {
			Toaster.showOneToast("您尚未选择学校!");

		} else {
			startActivity(new Intent(MainActivity.this, AddItem.class));
		}

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

	
	
	
	
	/**
	 * 新消息广播接收者
	 * 
	 * 
	 */
	private class NewMessageBroadcastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			// 主页面收到消息后，主要为了提示未读，实际消息内容需要到chat页面查看

			String from = intent.getStringExtra("from");
			// 消息id
			String msgId = intent.getStringExtra("msgid");
			EMMessage message = EMChatManager.getInstance().getMessage(msgId);
			// 2014-10-22 修复在某些机器上，在聊天页面对方发消息过来时不立即显示内容的bug
//			if (MainActivity.activityInstance != null) {
//				if (message.getChatType() == ChatType.GroupChat) {
//					if (message.getTo().equals(MainActivity.activityInstance.getToChatUsername()))
//						return;
//				} else {
//					if (from.equals(MainActivity.activityInstance.getToChatUsername()))
//						return;
//				}
//			}
			
			// 注销广播接收者，否则在ChatActivity中会收到这个广播
			abortBroadcast();
			
			notifyNewMessage(message);

			// 刷新bottom bar消息未读数
			updateUnreadLabel();
			EventBus.getDefault().post(new Event.NotifacationMSG());

		}
	}
	
	/**
	 * 刷新未读消息数
	 */
	public void updateUnreadLabel() {
		int count = getUnreadMsgCountTotal();
		if (count > 0) {
			countview.setText(String.valueOf(count));
			countview.setVisibility(View.VISIBLE);
		} else {
			countview.setVisibility(View.INVISIBLE);
		}
	}

	public int getUnreadMsgCountTotal() {
		int unreadMsgCountTotal = 0;
		unreadMsgCountTotal = EMChatManager.getInstance().getUnreadMsgsCount();
		return unreadMsgCountTotal;
	}
	
	
	 private static final int notifiId = 11;
	   /**
     * 当应用在前台时，如果当前消息不是属于当前会话，在状态栏提示一下
     * 如果不需要，注释掉即可
     * @param message
     */
    protected void notifyNewMessage(EMMessage message) {
        //如果是设置了不提醒只显示数目的群组(这个是app里保存这个数据的，demo里不做判断)
        //以及设置了setShowNotificationInbackgroup:false(设为false后，后台时sdk也发送广播)
        if(!EasyUtils.isAppRunningForeground(this)){
            return;
        }
        
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(getApplicationInfo().icon)
                .setWhen(System.currentTimeMillis()).setAutoCancel(true);
        
        String ticker = CommonUtils.getMessageDigest(message, this);
        if(message.getType() == Type.TXT)
            ticker = ticker.replaceAll("\\[.{2,3}\\]", "[表情]");
        //设置状态栏提示
        mBuilder.setTicker(message.getFrom()+": " + ticker);
        mBuilder.setContentTitle(message.getFrom());
        mBuilder.setContentText(ticker);

        Notification notification = mBuilder.build();
        notificationManager.notify(notifiId, notification);
//        notificationManager.cancel(notifiId);
    }
}
