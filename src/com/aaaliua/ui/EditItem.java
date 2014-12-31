package com.aaaliua.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Spinner;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.aaaliua.adapter.SimpleSpinnerAdapter;
import com.aaaliua.application.AppApplication;
import com.aaaliua.entity.ItemEntity;
import com.aaaliua.entity.ItemType;
import com.aaaliua.entity.UploadEntity;
import com.aaaliua.entity.UserEntity;
import com.aaaliua.event.Event;
import com.aaaliua.event.Event.NotifacationAddPic;
import com.aaaliua.event.Event.NotifacationPic;
import com.aaaliua.event.Event.NotifacationPics;
import com.aaaliua.event.Event.NotifacationType;
import com.aaaliua.event.Event.RegisterEvent;
import com.aaaliua.itemwork.MainActivity;
import com.aaaliua.itemwork.R;
import com.aaaliua.photo.Bimp;
import com.aaaliua.photo.PhotoActivity;
import com.aaaliua.user.LoginActivity;
import com.aaaliua.user.SelectSchoolActivity;
import com.aaaliua.user.UserActivity;
import com.aaaliua.utils.FileUtils;
import com.aaaliua.utils.ParseJson;
import com.aaaliua.utils.RequestUrl;
import com.aaaliua.utils.Toaster;
import com.aaaliua.utils.Validata;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.dazhongcun.baseactivity.BaseActionBarActivity;
import com.dazhongcun.widget.NoScorllGridView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import de.greenrobot.event.EventBus;

public class EditItem extends BaseActionBarActivity {

	@InjectView(R.id.Photogridview)
	NoScorllGridView noScorllGridView;
	@InjectView(R.id.done)
	View done;
	@InjectView(R.id.type)
	Spinner type;
	@InjectView(R.id.item)
	Spinner item;
	@InjectView(R.id.status)
	Spinner status;
	@InjectView(R.id.time)
	Spinner time;
	@InjectView(R.id.new_type)
	Spinner new_type;

	
	@InjectView(R.id.title)
	EditText title;
	@InjectView(R.id.number)
	EditText number;
	@InjectView(R.id.mai)
	EditText mai;
	
	@InjectView(R.id.desc)
	EditText desc;
	@InjectView(R.id.linkman)
	EditText linkman;
	@InjectView(R.id.linkphone)
	EditText linkphone;
	
	
	@InjectView(R.id.select_layout)
	View selectLayout;
	@InjectView(R.id.jie_layout)
	View jie_layout;
	@InjectView(R.id.jine_layout)
	View jine_layout;
	
	private GridAdapter adapter;
	private UploadEntity entity;
	
	ItemEntity ents;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.additem);
		ButterKnife.inject(this);
		ents = getIntent().getParcelableExtra("item");
		if(ents != null){
			String[] str = ents.getImage().split("\\|");
			for(int i =0;i<str.length;i++){
				if(str[i] == null && "".equals(str[i])){
					continue;
				}else{
					
					//文件如果存在
					String filepath = AppApplication.getImageLoader().getDiscCache().get(str[i]).getPath();
					if(new File(filepath).exists()){
						File file = new File(
								AppApplication.mSdcardImageCamera + File.separator,
								filepath.substring(filepath.lastIndexOf("/") + 1) + ".JPEG");
						//开始copy
						try {
							FileUtils.copyFile(new File(filepath), file);
							Bimp.drr.add(file.toString());
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					
				}
			}
			
			
			title.setText(ents.getTitle());
			number.setText(ents.getNum());
			desc.setText(ents.getDescription());
			linkman.setText(ents.getUsername());
			linkphone.setText(ents.getContact());
		}
		// Register
		EventBus.getDefault().register(this);
		entity = new UploadEntity();
		initScrollView();
		getType();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// Unregister
		EventBus.getDefault().unregister(this);
	}

	/** 在主线程接收ItemListEvent事件，必须是public void */
	public void onEventMainThread(NotifacationPic event) {
		if (event != null) {
			adapter.update();
		}
	}
	//处理完图片后的操作  上传操作
	public void onEventMainThread(NotifacationAddPic event) {
		if (event != null) {
			if(servicePath.size() == bmpPaths.size()){
				System.out.println(servicePath.toString() +"------------"+ servicePath.size());
				StringBuffer buf = new StringBuffer();
				for(int i =0;i<servicePath.size();i++){
					if(i != 0){
						buf.append("|");
					}
					buf.append(servicePath.get(i));
				}
//				for(String path : servicePath){
//					buf.append("|");
//					buf.append(path);
//				}
				entity.setImage(buf.toString());
				
			//	开始上传
				upload(entity);
			}
			
		}
	}

	//请求完类型后的总线
	public void onEventMainThread(NotifacationType event) {
		if (event != null) {
//			System.out.println(event.getType());

			ArrayAdapter<ItemType> typeadapter;
			typeadapter = new ArrayAdapter<ItemType>(this,
					R.layout.simple_spinner, event.getType().getType());
			typeadapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			type.setAdapter(typeadapter);
			type.setSelection(Integer.valueOf(ents.getType()));
			type.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> parent, View view,
						int position, long id) {
					ItemType item = (ItemType) parent
							.getItemAtPosition(position);
					entity.setType(item.getId());
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {

				}
			});

			ArrayAdapter<ItemType> Unitdapter;
			Unitdapter = new ArrayAdapter<ItemType>(this,
					R.layout.simple_spinner, event.getType().getUnits());
			Unitdapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			item.setAdapter(Unitdapter);
			item.setSelection(Integer.valueOf(ents.getUnit()));
			item.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> parent, View view,
						int position, long id) {
					ItemType item = (ItemType) parent
							.getItemAtPosition(position);
					entity.setUnit(item.getId());
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {

				}
			});

			ArrayAdapter<ItemType> Publishdapter;
			Publishdapter = new ArrayAdapter<ItemType>(this,
					R.layout.simple_spinner, event.getType().getPublish_type());
			Publishdapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			status.setAdapter(Publishdapter);
			
			status.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> parent, View view,
						int position, long id) {
					ItemType item = (ItemType) parent
							.getItemAtPosition(position);
					entity.setPublish_type(item);
					if("借".equals(item.getName())){
						selectLayout.setVisibility(View.VISIBLE);
						jie_layout.setVisibility(View.VISIBLE);
						jine_layout.setVisibility(View.GONE);
					}else if("卖".equals(item.getName())){
						selectLayout.setVisibility(View.VISIBLE);
						jine_layout.setVisibility(View.VISIBLE);
						jie_layout.setVisibility(View.GONE);
					}else {
						selectLayout.setVisibility(View.GONE);
					}
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {

				}
			});
			status.setSelection(Integer.valueOf(ents.getPublish_type()));
			mai.setText(ents.getMoney());

			ArrayAdapter<ItemType> newdapter;
			newdapter = new ArrayAdapter<ItemType>(this,
					R.layout.simple_spinner, event.getType().getNew_type());
			newdapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			new_type.setAdapter(newdapter);
			new_type.setSelection(Integer.valueOf(ents.getNew_type()));
			new_type.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> parent, View view,
						int position, long id) {
					ItemType item = (ItemType) parent
							.getItemAtPosition(position);
					entity.setNew_type(item.getId());
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {

				}
			});

			ArrayAdapter<ItemType> Monthdapter;
			Monthdapter = new ArrayAdapter<ItemType>(this,
					R.layout.simple_spinner, event.getType().getMonth_num());
			Monthdapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			time.setAdapter(Monthdapter);
			time.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> parent, View view,
						int position, long id) {
					ItemType item = (ItemType) parent
							.getItemAtPosition(position);
					entity.setMonth_num(item.getId());
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {

				}
			});
		}
	}

	@OnClick(R.id.back)
	public void onBackClick(View v) {
		onBackPressed();
	}
	
	@Override
	public void onBackPressed() {
		new MaterialDialog.Builder(this).title("放弃")
		.content("确定放弃吗?")
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
				Bimp.bmp.clear();
				Bimp.drr.clear();
				Bimp.max = 0;
				Bimp.act_bool = true;
				FileUtils.deleteDir();
				EditItem.this.finish();
				dialog.dismiss();
			}

			@Override
			public void onNegative(MaterialDialog dialog) {
				dialog.dismiss();
			}
		}).build().show();
	}
	
	
	private void upload(UploadEntity ent){
		
		RequestParams params = new RequestParams();
		params.put("id", ents.getId());
		params.put(UploadEntity.POST_IMAGE,ent.getImage());
		params.put(UploadEntity.POST_TITLE, ent.getTitle());
		params.put(UploadEntity.POST_TYPE, ent.getType());
		params.put(UploadEntity.POST_NUM, ent.getNum()+"");
		params.put(UploadEntity.POST_UNIT, ent.getUnit());
		params.put(UploadEntity.POST_PUBLISH_TYPE, ent.getPublish_type().getId());
		params.put(UploadEntity.POST_MONTH_NUM, ent.getMonth_num());
		params.put(UploadEntity.POST_MONEY, ent.getMoney()+"");
		params.put(UploadEntity.POST_NEW_TYPE, ent.getNew_type());
		params.put(UploadEntity.POST_DESCRIPTION, ent.getDesc());
		params.put(UploadEntity.POST_USERNAME, ent.getUsername());
		params.put(UploadEntity.POST_CONTACT, ent.getContact());
		
		AppApplication.getHttpClient().post(RequestUrl.UPLOAD_edit,params,
				new AsyncHttpResponseHandler() {
					final ProgressDialog progressDialog = new ProgressDialog(
							EditItem.this);

					@Override
					@Deprecated
					public void onFailure(Throwable error) {
						super.onFailure(error);
						Toaster.showOneToast("发布失败");
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
						progressDialog.setMessage("正在发布");
						progressDialog.setCancelable(false);
						progressDialog.show();
					}

					@Override
					@Deprecated
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						try {
							System.out.println(content);
							JSONObject obj = new JSONObject(content);
							String code = obj.optString("status");
							if("0".equals(code)){
								Toaster.showOneToast(obj.optString("data"));
								//发布成功后 删除缓存
								Bimp.bmp.clear();
								Bimp.drr.clear();
								Bimp.max = 0;
								Bimp.act_bool = true;
								FileUtils.deleteDir();
								EditItem.this.finish();
								EventBus.getDefault().post(new Event.Delete());
							}else{
								Toast.makeText(EditItem.this, "服务器返回了异常:"+statusCode, Toast.LENGTH_LONG).show();
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

				});
	}
	
	
	
	
	
	private List<String> servicePath;
	private List<String> bmpPaths;
	//点击上传按钮   请先验证各个必要的操作
	@OnClick(R.id.done)
	public void onDoneClick(View v) {
		
		
		new MaterialDialog.Builder(this).title("发布")
		.content("确定发布?")
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
				entity.setTitle(title.getText().toString().trim());
				entity.setNum("".equals(number.getText().toString().trim())?0:Integer.valueOf(number.getText().toString().trim()));
				entity.setMoney("".equals(mai.getText().toString().trim())?0:Double.valueOf(mai.getText().toString().trim()));
				entity.setDesc(desc.getText().toString().trim());
				entity.setUsername(linkman.getText().toString().trim());
				entity.setContact(linkphone.getText().toString().trim());
				if(Validata.validataPost(entity) == false){
					return;
				}
				//处理bitmap  获取处理后的路径
				bmpPaths = new ArrayList<String>();
				for (String path : Bimp.drr) {
					
					try {
						Bitmap bm = Bimp.revitionImageSizebmp(path);
						String newStr = path.substring(path.lastIndexOf("/") + 1,
								path.lastIndexOf("."));
						String url = FileUtils.saveBitmapCallback(bm, "" + newStr);
						bmpPaths.add(url);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
				System.out.println("onDoneClick---------"+bmpPaths.size()+"------"+bmpPaths.toString());
				
				servicePath = new ArrayList<String>();
				for (int i = 0; i < bmpPaths.size(); i++) {
					try {
						getPath(bmpPaths.get(i),i);
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
				}
				dialog.dismiss();
			}

			@Override
			public void onNegative(MaterialDialog dialog) {
				dialog.dismiss();
			}
		}).build().show();
		
	}

	
	private synchronized void getPath(String path, final int i) throws FileNotFoundException {
		File file = new File(path);
		if (file.exists() && file.length() > 0) {
			RequestParams params = new RequestParams();
			params.put("filename", file);
			// 上传文件
			AppApplication.getHttpClient().post(RequestUrl.GET_UPLOADFILE, params,
					new AsyncHttpResponseHandler() {
				
				final ProgressDialog progressDialog = new ProgressDialog(
						EditItem.this);
						@Override
						@Deprecated
						public void onFailure(int statusCode, Throwable error,
								String content) {
							super.onFailure(statusCode, error, content);
							 Toaster.showOneToast(statusCode+"onFailure");
						}
						
						@Override
						@Deprecated
						public void onSuccess(int statusCode, String content) {
							super.onSuccess(statusCode, content);
//							Toaster.showOneToast(statusCode+"onSuccess"+content);
							try {
								JSONObject obj = new JSONObject(content);
								String code = obj.optString("status");
								if("0".equals(code)){
									 String path = obj.optString("data");
//									 Toaster.showOneToast(path);
									 servicePath.add(path);
									 EventBus.getDefault().post(new NotifacationAddPic());
								}else{
									Toast.makeText(EditItem.this, "服务器返回了异常:"+statusCode, Toast.LENGTH_LONG).show();
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
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
						public void onProgress(int bytesWritten, int totalSize) {
							super.onProgress(bytesWritten, totalSize);
						}

						@Override
						public void onRetry() {
							super.onRetry();
						}

						@Override
						public void onStart() {
							super.onStart();
							progressDialog.setMessage("正在上传图片:"+i);
							progressDialog.setCancelable(false);
							progressDialog.show();
						}
				
					});
		} else {
			Toast.makeText(this, "文件不存在", Toast.LENGTH_LONG).show();
		}
	}

	private void getType() {

		AppApplication.getHttpClient().get(RequestUrl.GET_TYPE,
				new AsyncHttpResponseHandler() {
					final ProgressDialog progressDialog = new ProgressDialog(
							EditItem.this);

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
						progressDialog.setMessage("正在拉取数据类型");
						progressDialog.setCancelable(false);
						progressDialog.show();
					}

					@Override
					@Deprecated
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						EventBus.getDefault().post(
								new NotifacationType(ParseJson
										.parseType(content)));
					}

				});

	}

	private void initScrollView() {
		noScorllGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		adapter = new GridAdapter(this);
		adapter.update();
		noScorllGridView.setAdapter(adapter);

		noScorllGridView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// 判断是否点击的是最后一个
				if (arg2 == Bimp.bmp.size()) {

					new MaterialDialog.Builder(EditItem.this)
							.title("选择")
							.content("选择")
							.theme(Theme.LIGHT)
							// the default is light, so you don't
							// need this line
							.positiveText("拍照")
							// the default is 'Accept'
							.positiveColor(
									getResources().getColor(
											R.color.tabbar_color))
							.negativeText("相册") // leaving this
												// line out
												// will remove
												// the negative
												// button
							.callback(new MaterialDialog.Callback() {

								@Override
								public void onPositive(MaterialDialog dialog) {
									// 拍照选择
									photo();
									dialog.dismiss();
								}

								@Override
								public void onNegative(MaterialDialog dialog) {
									// 从相册选择
									Intent it = new Intent(EditItem.this,
											GetPicActivity.class);
									startActivity(it);
									dialog.dismiss();
								}
							}).build().show();

				} else {
					// 图片画廊
					Intent intent = new Intent(EditItem.this,
							PhotoActivity.class);
					intent.putExtra("ID", arg2);
					startActivity(intent);
				}
			}
		});
	}

	private static String pathpic = "";
	private static final int TAKE_PICTURE = 0x000000;

	// 拍照方法
	public void photo() {
		Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		File file = new File(
				AppApplication.mSdcardImageCamera + File.separator,
				String.valueOf(System.currentTimeMillis()) + ".jpg");
		pathpic = file.getPath();
		Uri imageUri = Uri.fromFile(file);
		openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		startActivityForResult(openCameraIntent, TAKE_PICTURE);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case TAKE_PICTURE:
			if (Bimp.drr.size() < Bimp.MAXSIZE && resultCode == -1) {
				Bimp.drr.add(pathpic);
				// 拍照后 是否需要执行 根据restart来做相应的操作 否则会执行两次 报数组越界
				if (adapter != null) {
					adapter.update();
				}
			}
			break;

		}
	}

	@SuppressLint("HandlerLeak")
	public class GridAdapter extends BaseAdapter {
		private LayoutInflater inflater; // 视图容器
		private int selectedPosition = -1;// 选中的位置
		private boolean shape;

		public boolean isShape() {
			return shape;
		}

		public void setShape(boolean shape) {
			this.shape = shape;
		}

		public GridAdapter(Context context) {
			inflater = LayoutInflater.from(context);
		}

		public void update() {
			loading();
		}

		public int getCount() {
			if (Bimp.bmp.size() != Bimp.MAXSIZE) {
				return (Bimp.bmp.size() + 1);
			} else {
				return (Bimp.bmp.size());
			}
		}

		public Object getItem(int arg0) {

			return null;
		}

		public long getItemId(int arg0) {

			return 0;
		}

		public void setSelectedPosition(int position) {
			selectedPosition = position;
		}

		public int getSelectedPosition() {
			return selectedPosition;
		}

		/**
		 * ListView Item设置
		 */
		public View getView(int position, View convertView, ViewGroup parent) {
			final int coord = position;
			ViewHolder holder = null;
			if (convertView == null) {

				convertView = inflater.inflate(R.layout.item_published_grida,
						parent, false);
				holder = new ViewHolder();
				holder.image = (ImageView) convertView
						.findViewById(R.id.item_grida_image);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			if (position == Bimp.bmp.size()) {
				// holder.image.setImageBitmap(BitmapFactory.decodeResource(
				// getResources(), R.drawable.btn_tianjia_normal));
				holder.image.setImageResource(R.drawable.bt_tianjia);
				if (position == Bimp.MAXSIZE) {
					holder.image.setVisibility(View.GONE);
				}
			} else {
				holder.image.setImageBitmap(Bimp.bmp.get(position));
			}

			return convertView;
		}

		public class ViewHolder {
			public ImageView image;
		}

		Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1:
					adapter.notifyDataSetChanged();
					break;
				}
				super.handleMessage(msg);
			}
		};

		public void loading() {
			new Thread(new Runnable() {
				public void run() {
					while (true) {
						if (Bimp.max == Bimp.drr.size()) {
							Message message = new Message();
							message.what = 1;
							handler.sendMessage(message);
							break;
						} else {
							try {
								String path = Bimp.drr.get(Bimp.max);
								if (!new File(path).exists()) {
									return;
								}
//								System.out.println(path);
								Bitmap bm = Bimp.revitionImageSize(path);
								Bimp.bmp.add(bm);
//								String newStr = path.substring(
//										path.lastIndexOf("/") + 1,
//										path.lastIndexOf("."));
//								FileUtils.saveBitmap(bm, "" + newStr);  //保存到本地
								// Bimp.max += 1;
								Message message = new Message();
								message.what = 1;
								handler.sendMessage(message);
							} catch (IOException e) {

								e.printStackTrace();
							}
						}
					}
				}
			}).start();
		}
	}

}
