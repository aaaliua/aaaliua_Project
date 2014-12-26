package com.aaaliua.user;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.aaaliua.application.AppApplication;
import com.aaaliua.database.BaseAppDbHelper;
import com.aaaliua.entity.BasicMember;
import com.aaaliua.entity.MakeEntity;
import com.aaaliua.entity.Status;
import com.aaaliua.entity.UserEntity;
import com.aaaliua.event.Event;
import com.aaaliua.event.Event.RegisterEvent;
import com.aaaliua.itemwork.R;
import com.aaaliua.utils.ParseJson;
import com.aaaliua.utils.RequestUrl;
import com.aaaliua.utils.Toaster;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.dazhongcun.baseactivity.BaseActionBarActivity;
import com.dazhongcun.utils.StringUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import de.greenrobot.event.EventBus;

public class SelectSchoolActivity extends BaseActionBarActivity {

	@InjectView(R.id.back)
	View back;
	@InjectView(R.id.query)
	EditText query;
	@InjectView(R.id.position)
	View position;
//	@InjectView(R.id.hint)
//	TextView hint;
	@InjectView(R.id.recycler_view)
	RecyclerView mRecyclerView;
	private LinearLayoutManager mLayoutManager;
	public LocationClient mLocationClient = null;
	public BDLocationListener myListener = new MyLocationListener();

	public int number = 1;
	private RecyclerViewAdapter adapter;
	private List<MakeEntity> makes;
	public boolean loadFlag = false;
	boolean loading = false;
	
	private String queryString;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_select_school);
		ButterKnife.inject(this);
		 // Register
        EventBus.getDefault().register(this);
		
		
		mRecyclerView.setHasFixedSize(true);
		mLayoutManager = new LinearLayoutManager(this);
		mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		mRecyclerView.setLayoutManager(mLayoutManager);

		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);//设置定位模式
		
		mLocationClient = new LocationClient(getApplicationContext());
		mLocationClient.registerLocationListener(myListener);
		mLocationClient.setLocOption(option);
		mLocationClient.start();
		
		query.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_ENTER) {
					if(StringUtils.isEmpty(query.getText().toString())){
						Toaster.showOneToast("请输入关键字");
					}else{
						queryString = query.getText().toString();
						getdata(queryString);
					}
//					Toast.makeText(SelectSchoolActivity.this,
//							query.getText().toString(), Toast.LENGTH_LONG)
//							.show();
				}
//				if (keyCode == KeyEvent.KEYCODE_BACK) {
//					onBackPressed();
//				}
				return false;
			}
		});
		
		
		
		
		
		mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(RecyclerView recyclerView,
					int newState) {
				super.onScrollStateChanged(recyclerView, newState);
			}

			@Override
			public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
				System.out.println(loadFlag);
				if(makes == null)
					return;
				if (!loading
						&& mLayoutManager.findLastVisibleItemPosition() == makes
								.size() - 1 && loadFlag) {
					loading = true;
					MakeEntity obj = new MakeEntity();
					obj.setLoadtype(MakeEntity.LOADMORE);
					adapter.add(obj);
					// 去加载更多
					loadMore();
				}

			}
		});
		
	}

	@OnClick(R.id.back)
	public void onBackClick(View v) {
		
		finish();
	}
	@OnClick(R.id.position)
	public void onPositionClick(View v) {
		if(mLocationClient != null){
			query.setText("");
			mLocationClient.start();
		}
	}

	
	
	private void loadMore() {
		RequestParams params = new RequestParams();
		params.put("pageNow", (number += 1) + "");
		params.put("sh_shool", queryString);
		
		AppApplication.getHttpClient().get(RequestUrl.LOCATION_SEARCH, params,
				new AsyncHttpResponseHandler() {

					@Override
					public void onStart() {
						// 网络请求开始
					}

					@Override
					@Deprecated
					public void onSuccess(String content) {
						parseJson(content, true);
					}

					@Override
					@Deprecated
					public void onFailure(Throwable error) {
						// 设置list的Emptyview
						number -= 1;
						adapter.remove(adapter.getItemCount() - 1);
						loading = false;
						loadFlag = false;
						Toaster.showOneToast("请求下一页数据异常");
					}

					@Override
					public void onFinish() {

					}

				});
	}
	
	
	private void getdata(String query) {
		number = 1;
		RequestParams params = new RequestParams();
		params.put("sh_shool", query);
		params.put("pageNow", number+"");
		
		AppApplication.getHttpClient().get(RequestUrl.LOCATION_SEARCH, params,
				new AsyncHttpResponseHandler() {

					@Override
					public void onStart() {
						// 网络请求开始
					}

					@Override
					@Deprecated
					public void onSuccess(String content) {
						parseJson(content, false);
					}

					@Override
					@Deprecated
					public void onFailure(Throwable error) {
						// 设置list的Emptyview
						number -= 1;
						if(adapter != null && adapter.getItemCount() >0)
						adapter.remove(adapter.getItemCount() - 1);
						loading = false;
						loadFlag = false;
						Toaster.showOneToast("请求搜索数据异常");
					}

					@Override
					public void onFinish() {

					}

				});
	}
	
	
	//定位监听
	public class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null) {
				Toaster.showOneToast("定位你失败");
				mLocationClient.stop();
				return;
			}
			getLocationList(location.getLatitude(), location.getLongitude());
			mLocationClient.stop();
		}

	}

	// 传经纬度给服务器 让服务器给我们列表 维度 经度
	private void getLocationList(double latitude, double longitude) {
//		hint.setText(latitude + "----------" + longitude);
		RequestParams params = new RequestParams();
		params.put("wei", latitude + "");
		params.put("jin", longitude + "");
		AppApplication.getHttpClient().get(RequestUrl.LOCATION_SELECT_URL, params,
				new AsyncHttpResponseHandler() {
					private boolean isSuc = false;
					final ProgressDialog progressDialog = new ProgressDialog(
							SelectSchoolActivity.this);
					@Override  
					public void onStart() {
						// 网络请求开始
						progressDialog.setMessage("正在拉取数据");
						progressDialog.setCancelable(false);
						progressDialog.show();
					}

					@Override
					@Deprecated
					public void onSuccess(String content) {
						parseLocation(content);
						isSuc = true;
					}

					@Override
					@Deprecated
					public void onFailure(Throwable error) {
					}

					@Override
					public void onFinish() {
						if (!isSuc) {
							Toaster.showDefToast(SelectSchoolActivity.this, "请求定位数据失败");
						}

						if (progressDialog != null
								&& progressDialog.isShowing()) {
							progressDialog.dismiss();
						}
					}

				});
		
		
	}
	
	private void parseLocation(String json){
		makes = ParseJson.getMakeEntityList(json);
		if (makes != null) {
			if (makes.size() == BasicMember.size) {
				loadFlag = true;
			} else {
				loadFlag = false;
			}
			if (makes.size() == 0) {
				MakeEntity obj = new MakeEntity();
				obj.setLoadtype(MakeEntity.NODATA);
				makes.add(obj);
			}
			adapter = new RecyclerViewAdapter(this, makes,
					this);
			mRecyclerView.setAdapter(adapter);

		} else {
			Status st = ParseJson.getStatus(json);
			Toaster.showOneToast(st.getMsg());
		}
	}
	
	
	private void parseJson(String json, boolean isload) {
		if (isload) {
			List<MakeEntity> more = ParseJson.getMakeEntityList(json);
			if (more != null) {
				if (more.size() == BasicMember.size) {
					loadFlag = true;
				} else {
					loadFlag = false;
				}
				adapter.remove(adapter.getItemCount() - 1);
				adapter.addAll(more);
				loading = false;
				// Toaster.showOneToast("数据添加");
			} else {
				adapter.remove(adapter.getItemCount() - 1);
				Toaster.showOneToast("数据加载完成");
			}

		} else {
			makes = ParseJson.getMakeEntityList(json);
			if (makes != null) {
				if (makes.size() == BasicMember.size) {
					loadFlag = true;
				} else {
					loadFlag = false;
				}
				if (makes.size() == 0) {
					MakeEntity obj = new MakeEntity();
					obj.setLoadtype(MakeEntity.NODATA);
					makes.add(obj);
				}
				adapter = new RecyclerViewAdapter(this, makes,
						this);
				mRecyclerView.setAdapter(adapter);

			} else {
				Status st = ParseJson.getStatus(json);
				Toaster.showOneToast(st.getMsg());
			}

		}

	}
	
	

	public class RecyclerViewAdapter extends
			RecyclerView.Adapter<RecyclerView.ViewHolder> {
		private final Context mContext;
		private List<MakeEntity> datas;
		public final Activity ac;

		public RecyclerViewAdapter(Context context, List<MakeEntity> datas,
				Activity ac) {
			mContext = context;
			this.datas = datas;
			this.ac = ac;
		}

		@Override
		public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
				int viewType) {
			switch (viewType) {
			case MakeEntity.LOADMORE:
				View v = LayoutInflater.from(parent.getContext()).inflate(
						R.layout.load_more, parent, false);
				ProgressBar bar = (ProgressBar) v.findViewById(R.id.progress);
				return new RecyclerView.ViewHolder(v) {
				};
			case MakeEntity.ITEM:
				View view = (View) LayoutInflater.from(parent.getContext())
						.inflate(R.layout.list_item, parent, false);
				return new ListViewHolder(mContext, view, ac);
			case MakeEntity.NODATA:

				View nodata = (View) LayoutInflater.from(parent.getContext())
						.inflate(R.layout.nodate, parent, false);
				return new RecyclerView.ViewHolder(nodata) {
				};
			default:
				View views = (View) LayoutInflater.from(parent.getContext())
						.inflate(R.layout.list_item, parent, false);
				return new ListViewHolder(mContext, views, ac);
			}

		}

		@Override
		public int getItemViewType(int position) {
			return datas.get(position).getLoadtype();
		}

		@Override
		public void onBindViewHolder(RecyclerView.ViewHolder viewHolder,
				final int position) {
			final MakeEntity ent = datas.get(position);
			switch (ent.getLoadtype()) {
			case MakeEntity.LOADMORE:

				break;
			case MakeEntity.NODATA:

				break;
			case MakeEntity.ITEM:
				ListViewHolder viewHolders = (ListViewHolder) viewHolder;
				viewHolders.makeName.setText(ent.getName());
				viewHolders.makeAddress.setText(ent.getAddress());
				UserEntity dblogin = new UserEntity();
				dblogin = dbHelper.queryObjForEq(UserEntity.class,
						UserEntity.JSON_uid, LoginActivity.getTokenID());
				if (dblogin != null) {
					if(ent.getId().equals(dblogin.getSchoo())){
						viewHolders.makeIn.setTextColor(getResources().getColor(R.color.tabbar_color));
						viewHolders.makeIn.setText("已加入");
						viewHolders.inlayout.setOnClickListener(null);
					}else{
						viewHolders.makeName.setTextColor(getResources().getColor(R.color.textcolor));
						viewHolders.makeAddress.setTextColor(getResources().getColor(R.color.textcolor));
						viewHolders.makeIn.setTextColor(getResources().getColor(R.color.textcolor));
						
						viewHolders.inlayout.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								if(LoginActivity.getTokenID() == null || "".equals(LoginActivity.getTokenID())){
									
									Toaster.showDefToast(SelectSchoolActivity.this, "未找到用户");
									return;
								}
								new MaterialDialog.Builder(ac)
										.title("确认加入？")
										.content("确定加入 "+ent.getName()+"?")
										.theme(Theme.LIGHT)
										// the default is light, so you don't
										// need this line
										.positiveText(R.string.accept)
										// the default is 'Accept'
										.positiveColor(
												mContext.getResources().getColor(
														R.color.tabbar_color))
										.negativeText(R.string.decline) // leaving this
																		// line out
																		// will remove
																		// the negative
																		// button
										.callback(new MaterialDialog.Callback() {

											@Override
											public void onPositive(MaterialDialog dialog) {
//												change(getObj(position).getId());
												inSchool(ent);
												dialog.dismiss();
											}

											@Override
											public void onNegative(MaterialDialog dialog) {
												dialog.dismiss();
											}
										}).build().show();
							}
						});
					}
				}else{
					
				}
				break;
			default:
				ListViewHolder viewHolderd = (ListViewHolder) viewHolder;
				viewHolderd.makeName.setText(ent.getName());
				viewHolderd.makeAddress.setText(ent.getAddress());
				break;
			}

		}

		@Override
		public int getItemCount() {
			return datas.size();
		}

		public void remove(int i) {
			datas.remove(i);
			notifyItemRemoved(i);
		}

		public void add(MakeEntity entitye) {
			this.datas.add(entitye);
			notifyItemInserted(datas.size() - 1);
		}

		public void addAll(List<MakeEntity> list) {
			this.datas.addAll(list);
			notifyDataSetChanged();
		}

		public MakeEntity getObj(int position) {
			return datas.get(position);
		}

		public class ListViewHolder extends RecyclerView.ViewHolder {

			public View mTextView;
			private final Context mContext;

			private final Activity ac;

			public TextView makeName;
			public View inlayout;
			public TextView makeAddress;
			public TextView makeIn;

			public ListViewHolder(Context mCon, View v, Activity acd) {
				super(v);
				mTextView = v;
				mContext = mCon;
				this.ac = acd;

				this.makeName = (TextView) v.findViewById(R.id.name);
				this.inlayout =  v.findViewById(R.id.inlayout);
				this.makeAddress = (TextView) v.findViewById(R.id.address);
				this.makeIn = (TextView) v.findViewById(R.id.in);

			}

		}

	}
	
	private BaseAppDbHelper<UserEntity> dbHelper = new BaseAppDbHelper<UserEntity>();
	private void inSchool(final MakeEntity entity){
		RequestParams params = new RequestParams();
		params.put("uid", LoginActivity.getTokenID());
		params.put("sh_id", entity.getId());
		AppApplication.getHttpClient().get(RequestUrl.IN_SCHOOL, params,
				new AsyncHttpResponseHandler() {
					private boolean isSuc = false;
					final ProgressDialog progressDialog = new ProgressDialog(
							SelectSchoolActivity.this);
					@Override
					public void onStart() {
						// 网络请求开始
						progressDialog.setMessage("正在加入该学校");
						progressDialog.setCancelable(false);
						progressDialog.show();
					}

					@Override
					@Deprecated
					public void onSuccess(String content) {
						try {
							JSONObject obj = new JSONObject(content);
							String code = obj.optString("status");
							if("0".equals(code)){
								Toaster.showDefToast(SelectSchoolActivity.this, "请求加入成功");
								UserEntity dblogin = new UserEntity();
								dblogin = dbHelper.queryObjForEq(UserEntity.class,
										UserEntity.JSON_uid, LoginActivity.getTokenID());
								if (dblogin != null) {
									dblogin.setSchoo(entity.getId());
									dblogin.setSchoo_name(entity.getName());
									// 如果有localID就跟新数据库 否则插入数据库
									dbHelper.createOrUpdate(dblogin);
									//选择学校成功  关闭登录页面，关闭注册页面，关闭确认密码页，关闭选择学校页 进入个人中心
									EventBus.getDefault().post(new Event.RegisterEvent(true));
									startActivity(new Intent(SelectSchoolActivity.this, UserActivity.class));
								}
							}else{
								Toaster.showDefToast(SelectSchoolActivity.this, "请求加入失败，服务器异常");
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
						
						
						isSuc = true;
					}

					@Override
					@Deprecated
					public void onFailure(Throwable error) {
					}

					@Override
					public void onFinish() {
						if (!isSuc) {
							Toaster.showDefToast(SelectSchoolActivity.this, "请求加入失败");
						}

						if (progressDialog != null
								&& progressDialog.isShowing()) {
							progressDialog.dismiss();
						}
					}

				});
	}

	
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		// Unregister
		EventBus.getDefault().unregister(this);
		mLocationClient.stop();
	}
	
	   /** 在主线程接收ItemListEvent事件，必须是public void */
    public void onEventMainThread(RegisterEvent event) {
    	if(event != null && event.getFlag() == true){
    		this.finish();
    	}
    }
}
