package com.aaaliua.ui;

import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutCompat.LayoutParams;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.aaaliua.adapter.Post_Msg_List_adapter;
import com.aaaliua.application.AppApplication;
import com.aaaliua.entity.ItemEntity;
import com.aaaliua.entity.PostUser;
import com.aaaliua.entity.Status;
import com.aaaliua.entity.ValidateCode;
import com.aaaliua.event.Event;
import com.aaaliua.event.Event.Delete;
import com.aaaliua.itemwork.MainActivity;
import com.aaaliua.itemwork.R;
import com.aaaliua.user.LoginActivity;
import com.aaaliua.utils.ImageOptions;
import com.aaaliua.utils.ParseJson;
import com.aaaliua.utils.RequestUrl;
import com.aaaliua.utils.Toaster;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.baoyz.pg.PG;
import com.dazhongcun.baseactivity.BaseActionBarActivity;
import com.dazhongcun.baseactivity.BaseActivity;
import com.dazhongcun.widget.CircleImageView;
import com.dazhongcun.widget.NoScorllListView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import de.greenrobot.event.EventBus;

public class ItemInfoActivity extends BaseActivity implements OnClickListener {

	@InjectView(R.id.title)
	TextView title;

	@InjectView(R.id.scrollview)
	View scroll;

	@InjectView(R.id.pager)
	ViewPager pagerImage;
	@InjectView(R.id.postList)
	NoScorllListView postList; // 评论列表
	@InjectView(R.id.empty)
	RelativeLayout empty;

	@InjectView(R.id.info)
	TextView info;
	@InjectView(R.id.zan)
	TextView zan;
	@InjectView(R.id.desc)
	TextView desc;
	@InjectView(R.id.username)
	TextView username;

	@InjectView(R.id.sch)
	TextView sch;
	@InjectView(R.id.nickname)
	TextView nickname;
	@InjectView(R.id.icon)
	CircleImageView img;
	
	@InjectView(R.id.bottom)
	View bom;
	@InjectView(R.id.setting)
	View settingView;
	@InjectView(R.id.send)
	Button send;
	@InjectView(R.id.content)
	EditText content;

	ItemEntity ent;
	String[] imgs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_info);
		ButterKnife.inject(this);
		postList.setEmptyView(empty);
		ent = getIntent().getParcelableExtra("item");
		if (ent != null) {
			title.setText(ent.getTitle());
			desc.setText(ent.getDescription());
			imgs = ent.getImage().split("\\|");
			pagerImage.setOffscreenPageLimit(4);
			pagerImage.setAdapter(new MyLoopViewPagerAdatper());
			zan.setText(ent.getPraise());
			AppApplication.getImageLoader().displayImage(ent.getIcon(), img,ImageOptions.icon_Options);
			nickname.setText(ent.getNickname());
			sch.setText(ent.getSh_shool());
			
			if (ent.getUid().equals(LoginActivity.getTokenID())) {
				settingView.setVisibility(View.VISIBLE);
				settingView.setOnClickListener(this);
			} else {
				settingView.setVisibility(View.GONE);
			}

			zan.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (LoginActivity.getTokenID() == null
							|| "".equals(LoginActivity.getTokenID())) {
						new MaterialDialog.Builder(ItemInfoActivity.this)
								.title("登录")
								.content("您尚未登录，无法赞,是否登录?")
								.theme(Theme.LIGHT)
								// the default is light, so you don't
								// need this line
								.positiveText(R.string.accept)
								// the default is 'Accept'
								.positiveColor(
										getResources().getColor(
												R.color.tabbar_color))
								.negativeText(R.string.decline) // leaving this
																// line out
																// will remove
																// the negative
																// button
								.callback(new MaterialDialog.Callback() {

									@Override
									public void onPositive(MaterialDialog dialog) {
										startActivity(new Intent(
												ItemInfoActivity.this,
												LoginActivity.class));
										dialog.dismiss();
									}

									@Override
									public void onNegative(MaterialDialog dialog) {
										dialog.dismiss();
									}
								}).build().show();
					} else {
						RequestParams params = new RequestParams();
						params.put("id", ent.getId());
						params.put("uid", LoginActivity.getTokenID());
						AppApplication.getHttpClient().post(
								RequestUrl.POST_zan, params,
								new AsyncHttpResponseHandler() {

									final ProgressDialog progressDialog = new ProgressDialog(
											ItemInfoActivity.this);

									@Override
									@Deprecated
									public void onFailure(Throwable error) {
										super.onFailure(error);
										Toaster.showOneToast("赞失败");
									}

									@Override
									public void onFinish() {
										super.onFinish();
										if (progressDialog != null
												&& progressDialog.isShowing()) {
											progressDialog.dismiss();
										}
									}

									@Override
									public void onStart() {
										super.onStart();
										progressDialog.setMessage("正在赞");
										progressDialog.setCancelable(false);
										progressDialog.show();
									}

									@Override
									@Deprecated
									public void onSuccess(int statusCode,
											String content) {
										super.onSuccess(statusCode, content);
										System.out.println(content);
										Status st = ParseJson
												.getStatus(content);
										if ("0".equals(st.getStatus())) {
											Toaster.showOneToast("赞成功");
										}

									}

								});
					}
				}
			});
			username.setText(ent.getUsername());

			if ("1".equals(ent.getPublish_type())) {
				// 借 送 卖
				info.setText("借");
			} else if ("2".equals(ent.getPublish_type())) {
				info.setText("送");
			} else if ("3".equals(ent.getPublish_type())) {
				String unit = "个";
				if ("1".equals(ent.getUnit())) {
					unit = "个";
				} else if ("2".equals(ent.getUnit())) {
					unit = "本";
				} else if ("3".equals(ent.getUnit())) {
					unit = "套";
				} else {
					unit = "个";
				}
				info.setText(ent.getMoney() + "元/" + unit);

			} else {
				info.setVisibility(View.GONE);
			}

			// 获取评论信息
			 // Register
	        EventBus.getDefault().register(this);
			getMSG();

		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		// Unregister
		EventBus.getDefault().unregister(this);
	}
	
	   /** 在主线程接收ItemListEvent事件，必须是public void */
    public void onEventMainThread(Delete event) {
    	if(event != null){
    		finish();
    	}
    }
    
    @OnClick(R.id.link)
    public void link(View v){
    	
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
							startActivity(new Intent(ItemInfoActivity.this,
									LoginActivity.class));
							dialog.dismiss();
						}

						@Override
						public void onNegative(MaterialDialog dialog) {
							dialog.dismiss();
						}
					}).build().show();
		} else{
			if(TextUtils.isEmpty(ent.getContact())){
	    		//直接去聊天界面
	    	}else{
	    		//让用户选择去打电话还是聊天
	    		new MaterialDialog.Builder(this)
				.title("选择")
				.content("请选择联系该用户的方式!")
				.theme(Theme.LIGHT)
				// the default is light, so you don't
				// need this line
				.positiveText("聊天")
				// the default is 'Accept'
				.positiveColor(
						getResources().getColor(R.color.tabbar_color))
				.negativeText("打电话") // leaving this
												// line out
												// will remove
												// the negative
												// button
				.callback(new MaterialDialog.Callback() {

					@Override
					public void onPositive(MaterialDialog dialog) {
						//聊天
						dialog.dismiss();
					}

					@Override
					public void onNegative(MaterialDialog dialog) {
						//打电话
						dialog.dismiss();
					}
				}).build().show();
	    	}
		}
    	
    	
    	
    	
    }

	private void getMSG() {
		RequestParams params = new RequestParams();
		params.put("id", ent.getId());
		params.put("pageNow", "1");
		AppApplication.getHttpClient().post(RequestUrl.GET_MSG, params,
				new AsyncHttpResponseHandler() {

					final ProgressDialog progressDialog = new ProgressDialog(
							ItemInfoActivity.this);

					@Override
					@Deprecated
					public void onFailure(Throwable error) {
						super.onFailure(error);
					}

					@Override
					public void onFinish() {
						super.onFinish();
						if (progressDialog != null
								&& progressDialog.isShowing()) {
							progressDialog.dismiss();
						}
					}

					@Override
					public void onStart() {
						super.onStart();
						progressDialog.setMessage("正在拉取数据");
						progressDialog.setCancelable(true);
						progressDialog.show();
					}

					@Override
					@Deprecated
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						System.out.println(content);
						parsePost(content);
					}

				});
	}

	private List<PostUser> postLists;

	private void parsePost(String json) {
		postLists = ParseJson.getPostUserList(json);
		if (postLists != null && postLists.size() > 0) {
			postList.setAdapter(new Post_Msg_List_adapter(this, postLists, true));

			// morePost.setOnClickListener(new OnClickListener() {
			//
			// @Override
			// public void onClick(View v) {
			// Intent it = new Intent(BookInfo.this, BookPostList.class);
			// it.putExtra(BookPostList.EXTRA_NUMBER, id);
			// it.putExtra(BookPostList.EXTRA_TYPE, 1+""); //图书
			// BookInfo.this.startActivity(it);
			// }
			// });
		}

	}

	private class MyLoopViewPagerAdatper extends PagerAdapter {

		@Override
		public int getCount() {
			return imgs.length;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == (View) object;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			// super.destroyItem(container, position, object);
			container.removeView((View) object);
		}

		@Override
		public Object instantiateItem(ViewGroup container, final int position) {
			// return super.instantiateItem(container, position);
			// 通过imageload 显示网络图片
			ImageView img = new ImageView(ItemInfoActivity.this);
			img.setScaleType(ScaleType.FIT_CENTER);
			AppApplication.getImageLoader().displayImage(imgs[position], img,
					ImageOptions.options, null);

			container.addView(img);
			return img;
		}
	}

	@OnClick(R.id.send)
	public void onSendClick(View view) {
		if (LoginActivity.getTokenID() == null
				|| "".equals(LoginActivity.getTokenID())) {
			new MaterialDialog.Builder(this)
					.title("登录")
					.content("您尚未登录，无法发布评论,是否登录?")
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
							startActivity(new Intent(ItemInfoActivity.this,
									LoginActivity.class));
							dialog.dismiss();
						}

						@Override
						public void onNegative(MaterialDialog dialog) {
							dialog.dismiss();
						}
					}).build().show();
		} else {
			if (TextUtils.isEmpty(content.getText().toString().trim())) {
				Toaster.showOneToast("请填写评论信息");
				return;
			} else {

				RequestParams params = new RequestParams();
				params.put("id", ent.getId());
				params.put("uid", LoginActivity.getTokenID());
				params.put("content", content.getText().toString().trim());
				AppApplication.getHttpClient().post(RequestUrl.POST_MSG,
						params, new AsyncHttpResponseHandler() {

							final ProgressDialog progressDialog = new ProgressDialog(
									ItemInfoActivity.this);

							@Override
							@Deprecated
							public void onFailure(Throwable error) {
								super.onFailure(error);
								Toaster.showOneToast("评论失败");
							}

							@Override
							public void onFinish() {
								super.onFinish();
								if (progressDialog != null
										&& progressDialog.isShowing()) {
									progressDialog.dismiss();
								}
							}

							@Override
							public void onStart() {
								super.onStart();
								progressDialog.setMessage("正在评论");
								progressDialog.setCancelable(false);
								progressDialog.show();
							}

							@Override
							@Deprecated
							public void onSuccess(int statusCode, String content) {
								super.onSuccess(statusCode, content);
								System.out.println(content);
								Status st = ParseJson.getStatus(content);
								if ("0".equals(st.getStatus())) {
									Toaster.showOneToast("评论成功");
									ItemInfoActivity.this.content.setText("");
									getMSG();
								}

							}

						});
			}
		}
	}

	@OnClick(R.id.back)
	public void onBackClick(View v) {
		finish();
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == settingView.getId()) {
			// 弹出dialog
			new PopupWindowPic(this, scroll);
		}
	}

	class PopupWindowPic extends PopupWindow {

		public PopupWindowPic(Context context, View v) {
			View view = View.inflate(context, R.layout.item_popupwindows_book,
					null);
			view.startAnimation(AnimationUtils.loadAnimation(context,
					R.anim.fade_ins));
			LinearLayout llLayout = (LinearLayout) view
					.findViewById(R.id.ll_popup);
			llLayout.startAnimation(AnimationUtils.loadAnimation(context,
					R.anim.push_bottom_in_2));
			// 设置宽高
			setWidth(LayoutParams.FILL_PARENT);
			setHeight(LayoutParams.FILL_PARENT);
			setBackgroundDrawable(new BitmapDrawable());
			setFocusable(true);
			setOutsideTouchable(true); // 外部触摸不隐藏
			setContentView(view);
			showAtLocation(v, Gravity.BOTTOM, 0, 0);
			update();

			View donelayout = view.findViewById(R.id.donelayout);
			Button done = (Button) view.findViewById(R.id.done);
			Button edit = (Button) view.findViewById(R.id.edit);
			Button repost = (Button) view.findViewById(R.id.repost);
			Button delete = (Button) view.findViewById(R.id.delete);
			Button cancel = (Button) view.findViewById(R.id.cancel);

			done.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					new MaterialDialog.Builder(ItemInfoActivity.this)
					.title("下架？")
					.content("下架?")
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
							changeStatus("2");
							dialog.dismiss();
						}

						@Override
						public void onNegative(MaterialDialog dialog) {
							dialog.dismiss();
						}
					}).build().show();
					
					dismiss();
				}
			});
			repost.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					new MaterialDialog.Builder(ItemInfoActivity.this)
					.title("重新发布？")
					.content("重新发布?")
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
							changeStatus("1");
							dialog.dismiss();
						}

						@Override
						public void onNegative(MaterialDialog dialog) {
							dialog.dismiss();
						}
					}).build().show();
					
					dismiss();
				}
			});
			
			edit.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent it = new Intent(ItemInfoActivity.this,EditItem.class);
					it.putExtra("item", PG.convertParcelable(ent));
					startActivity(it);
				}
			});
			
			
			delete.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					new MaterialDialog.Builder(ItemInfoActivity.this)
					.title("删除？")
					.content("删除确认?")
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
							Delete();
							dialog.dismiss();
						}

						@Override
						public void onNegative(MaterialDialog dialog) {
							dialog.dismiss();
						}
					}).build().show();
					
					dismiss();
				}
			});
			
			cancel.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					dismiss();
				}
			});
		}

	}
	
	private void changeStatus(String status){
		
		AsyncHttpClient mClient = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		params.put("id", ent.getId());
		params.put("status", status);
		mClient.post(RequestUrl.CHANGE_STATUS, params,
				new AsyncHttpResponseHandler() {

					final ProgressDialog progressDialog = new ProgressDialog(
							ItemInfoActivity.this);

					@Override
					@Deprecated
					public void onFailure(Throwable error) {
						super.onFailure(error);
					}

					@Override
					public void onFinish() {
						super.onFinish();
						if (progressDialog != null
								&& progressDialog.isShowing()) {
							progressDialog.dismiss();
						}
					}

					@Override
					public void onStart() {
						super.onStart();
						progressDialog.setMessage("正在改变状态");
						progressDialog.setCancelable(true);
						progressDialog.show();
					}

					@Override
					@Deprecated
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						System.out.println(content);
						ValidateCode vo = ParseJson.parseCode(content);
						if(vo != null){
							Toaster.showOneToast("改变成功");
							ItemInfoActivity.this.finish();
						}
					}

				});
	}

	private void Delete() {
		AsyncHttpClient mClient = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		params.put("id", ent.getId());
		mClient.post(RequestUrl.delete_info, params,
				new AsyncHttpResponseHandler() {

					final ProgressDialog progressDialog = new ProgressDialog(
							ItemInfoActivity.this);

					@Override
					@Deprecated
					public void onFailure(Throwable error) {
						super.onFailure(error);
					}

					@Override
					public void onFinish() {
						super.onFinish();
						if (progressDialog != null
								&& progressDialog.isShowing()) {
							progressDialog.dismiss();
						}
					}

					@Override
					public void onStart() {
						super.onStart();
						progressDialog.setMessage("正在删除");
						progressDialog.setCancelable(true);
						progressDialog.show();
					}

					@Override
					@Deprecated
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						System.out.println(content);
						ValidateCode vo = ParseJson.parseCode(content);
						if(vo != null){
							Toaster.showOneToast("删除成功");
							ItemInfoActivity.this.finish();
							EventBus.getDefault().post(new Event.Delete());
						}
					}

				});
	}
}
