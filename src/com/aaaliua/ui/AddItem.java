package com.aaaliua.ui;

import java.io.File;
import java.io.IOException;

import android.annotation.SuppressLint;
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
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.AdapterView.OnItemClickListener;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.aaaliua.application.AppApplication;
import com.aaaliua.event.Event.NotifacationPic;
import com.aaaliua.event.Event.RegisterEvent;
import com.aaaliua.itemwork.MainActivity;
import com.aaaliua.itemwork.R;
import com.aaaliua.photo.Bimp;
import com.aaaliua.photo.PhotoActivity;
import com.aaaliua.user.LoginActivity;
import com.aaaliua.utils.FileUtils;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.dazhongcun.baseactivity.BaseActionBarActivity;
import com.dazhongcun.widget.NoScorllGridView;

import de.greenrobot.event.EventBus;

public class AddItem extends BaseActionBarActivity {

	
	@InjectView(R.id.Photogridview)
	NoScorllGridView noScorllGridView;
	
	private GridAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.additem);
		ButterKnife.inject(this);
		 // Register
        EventBus.getDefault().register(this);
        initScrollView();
	}
	
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		// Unregister
		EventBus.getDefault().unregister(this);
	}
	
	   /** 在主线程接收ItemListEvent事件，必须是public void */
    public void onEventMainThread(NotifacationPic event) {
    	if(event != null){
    		adapter.update();
    	}
    }
	
    @OnClick(R.id.back)
    public void onBackClick(View v){
    	Bimp.bmp.clear();
		Bimp.drr.clear();
		Bimp.max = 0;
		Bimp.act_bool = true;
		FileUtils.deleteDir();
		onBackPressed();
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
					
					new MaterialDialog.Builder(AddItem.this).title("登录")
					.content("选择")
					.theme(Theme.LIGHT)
					// the default is light, so you don't
					// need this line
					.positiveText("拍照")
					// the default is 'Accept'
					.positiveColor(getResources().getColor(R.color.tabbar_color))
					.negativeText("相册") // leaving this
													// line out
													// will remove
													// the negative
													// button
					.callback(new MaterialDialog.Callback() {

						@Override
						public void onPositive(MaterialDialog dialog) {
							//拍照选择
							photo();
							dialog.dismiss();
						}

						@Override
						public void onNegative(MaterialDialog dialog) {
							//从相册选择
							Intent it = new Intent(AddItem.this,GetPicActivity.class);
							startActivity(it);
							dialog.dismiss();
						}
					}).build().show();
					
				} else {
				 //图片画廊
					Intent intent = new Intent(AddItem.this,
							PhotoActivity.class);
					intent.putExtra("ID", arg2);
					startActivity(intent);
				}
			}
		});
	}
	
	private static String pathpic = "";
	private static final int TAKE_PICTURE = 0x000000;
	//拍照方法
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
				//拍照后   是否需要执行 根据restart来做相应的操作  否则会执行两次  报数组越界
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
			if(Bimp.bmp.size() != Bimp.MAXSIZE){
				return (Bimp.bmp.size() + 1);
			}else{
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
//				holder.image.setImageBitmap(BitmapFactory.decodeResource(
//						getResources(), R.drawable.btn_tianjia_normal));
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
								if(!new File(path).exists()){
									return;  
								}
								System.out.println(path);
								Bitmap bm = Bimp.revitionImageSize(path);
								Bimp.bmp.add(bm);
								String newStr = path.substring(
										path.lastIndexOf("/") + 1,
										path.lastIndexOf("."));
								FileUtils.saveBitmap(bm, "" + newStr);
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
