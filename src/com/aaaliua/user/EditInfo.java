package com.aaaliua.user;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Random;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.widget.LinearLayoutCompat.LayoutParams;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.aaaliua.application.AppApplication;
import com.aaaliua.database.BaseAppDbHelper;
import com.aaaliua.entity.BasicMember;
import com.aaaliua.entity.UserEntity;
import com.aaaliua.entity.ValidateCode;
import com.aaaliua.event.Event;
import com.aaaliua.event.Event.NotifacationAddPic;
import com.aaaliua.itemwork.R;
import com.aaaliua.photo.Bimp;
import com.aaaliua.ui.AddItem;
import com.aaaliua.ui.ItemInfoActivity;
import com.aaaliua.utils.ImageOptions;
import com.aaaliua.utils.ParseJson;
import com.aaaliua.utils.RequestUrl;
import com.aaaliua.utils.Toaster;
import com.dazhongcun.baseactivity.BaseActionBarActivity;
import com.dazhongcun.baseactivity.BaseActivity;
import com.dazhongcun.utils.StringUtils;
import com.dazhongcun.utils.Utils;
import com.dazhongcun.widget.CircleImageView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import de.greenrobot.event.EventBus;

public class EditInfo extends BaseActivity {

	@InjectView(R.id.name)
	EditText name;
	@InjectView(R.id.age)
	EditText age;
	@InjectView(R.id.user_img)
	CircleImageView img;

	private BaseAppDbHelper<UserEntity> dbHelper = new BaseAppDbHelper<UserEntity>();
	private BasicMember basicMember;

	// 文件路径
	String fileName;

	private static final int PHOTO_GRAPH = 1;// 拍照
	private static final int LOCAL_PHOTO = 2; // 本地
	private static final String IMAGE_UNSPECIFIED = "image/*";
	private static final int PHOTO_REQUEST_CUT = 3;// 结果

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_edit);
		ButterKnife.inject(this);
		basicMember = new BasicMember();

		setInfo();

	}

	@OnClick(R.id.chageIcon)
	public void chage(View v) {
		new PopupWindowPic(this, v);
	}

	@OnClick(R.id.post)
	public void post(View view) {
		if (validInput()) {
			chageInfo();
		}
	}

	private void chageInfo() {

		basicMember.setNickname(name.getText().toString().trim());
		basicMember.setAge(age.getText().toString().trim());

		RequestParams params = new RequestParams();
		params.put(BasicMember.post_icon, basicMember.getIcon());
		params.put(BasicMember.post_nickname, basicMember.getNickname());
		params.put(BasicMember.post_age, basicMember.getAge());
		AppApplication.getHttpClient().post(RequestUrl.EDIT_USER, params,
				new AsyncHttpResponseHandler() {
					final ProgressDialog progressDialog = new ProgressDialog(
							EditInfo.this);

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
						progressDialog.setMessage("正在修改");
						progressDialog.setCancelable(false);
						progressDialog.show();
					}

					@Override
					@Deprecated
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						ValidateCode vo = ParseJson.parseCode(content);
						if (vo != null) {
							UserEntity dblogin = dbHelper.queryObjForEq(
									UserEntity.class, UserEntity.JSON_uid,
									LoginActivity.getTokenID());
							dblogin.setNickname(basicMember.getNickname());
							dblogin.setIcon(basicMember.getIcon());
							dblogin.setAge(basicMember.getAge());
							dbHelper.createOrUpdate(dblogin);
							EventBus.getDefault().post(new Event.ChangeInfo());
							EditInfo.this.finish();
						}
						
						

					}

				});
	}

	private boolean validInput() {
		if (TextUtils.isEmpty(name.getText().toString().trim())) {
			Toaster.showToast(this, "请输入");
			name.requestFocus();
			return false;
		}
//		if (TextUtils.isEmpty(phone.getText().toString().trim())) {
//			Toaster.showToast(this, "请输入联系方式");
//			phone.requestFocus();
//			return false;
//		} else if (!StringUtils.isMobileNO(phone.getText().toString())) {
//			Toaster.showToast(this, "请输入正确的手机号");
//			phone.requestFocus();
//			return false;
//		}
		if (TextUtils.isEmpty(age.getText().toString().trim())) {
			Toaster.showToast(this, "请输入年龄");
			age.requestFocus();
			return false;
		}

		return true;
	}

	private void setInfo() {
		if (dbHelper != null) {
			UserEntity dblogin = new UserEntity();
			dblogin = dbHelper.queryObjForEq(UserEntity.class,
					UserEntity.JSON_uid, LoginActivity.getTokenID());
			if (dblogin == null)
				return;
			name.setText(dblogin.getNickname());
//			phone.setText(dblogin.getPhone());
			age.setText(dblogin.getAge());
			AppApplication.getImageLoader().displayImage(dblogin.getIcon(),
					img, ImageOptions.defaultOptions);

			basicMember.setNickname(dblogin.getNickname());
			basicMember.setLinkphone(dblogin.getPhone());
			basicMember.setAge(dblogin.getAge());
			basicMember.setIcon(dblogin.getIcon());
		}
	}

	class PopupWindowPic extends PopupWindow {

		public PopupWindowPic(Context context, View v) {
			View view = View.inflate(context, R.layout.item_popupwindows, null);
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

			Button camera = (Button) view
					.findViewById(R.id.item_popupwindows_camera);
			Button phot = (Button) view
					.findViewById(R.id.item_popupwindows_Photo);
			Button can = (Button) view
					.findViewById(R.id.item_popupwindows_cancel);

			camera.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// 拍照
					doTakePhoto();
					dismiss();
				}
			});
			phot.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// 选择本地图片
					doLocalPhoto();
					dismiss();
				}
			});

			can.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					dismiss();
				}
			});
		}
	}

	private void doLocalPhoto() {
		Intent intent = new Intent(Intent.ACTION_PICK, null);
		intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				IMAGE_UNSPECIFIED);
		startActivityForResult(intent, LOCAL_PHOTO);
	}

	private void doTakePhoto() {
		fileName = getUUID();
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(
					Environment.getExternalStorageDirectory(), fileName
							+ ".jpg")));
			startActivityForResult(intent, PHOTO_GRAPH);
		} else {
			Toast.makeText(this, "未检测到sdcard", Toast.LENGTH_SHORT).show();
		}

	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == PHOTO_GRAPH && this.RESULT_OK == resultCode) {
			File picture = new File(Environment.getExternalStorageDirectory()
					+ "/" + fileName + ".jpg");
			startPhotoZoom(Uri.fromFile(picture));
		} else if (requestCode == LOCAL_PHOTO && RESULT_OK == resultCode) {
			Uri uri = data.getData();
			startPhotoZoom(uri);
		} else if (requestCode == PHOTO_REQUEST_CUT && RESULT_OK == resultCode) {
			if (data != null)
				// 上传图片
				uploadPic(data);
		}
	}

	private void uploadPic(Intent data) {
		Bundle bundle = data.getExtras();
		if (bundle != null) {
			Bitmap photo = bundle.getParcelable("data");
			if (photo != null) {
				String path = saveMyBitmap(photo);
				try {
					postImage(path);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void postImage(String path) throws FileNotFoundException {
		File file = new File(path);
		if (file.exists() && file.length() > 0) {
			RequestParams params = new RequestParams();
			params.put("filename", file);
			// 上传文件
			AppApplication.getHttpClient().post(RequestUrl.GET_UPLOADFILE,
					params, new AsyncHttpResponseHandler() {

						final ProgressDialog progressDialog = new ProgressDialog(
								EditInfo.this);

						@Override
						@Deprecated
						public void onFailure(int statusCode, Throwable error,
								String content) {
							super.onFailure(statusCode, error, content);
							Toaster.showOneToast(statusCode + "onFailure");
						}

						@Override
						@Deprecated
						public void onSuccess(int statusCode, String content) {
							super.onSuccess(statusCode, content);
							// Toaster.showOneToast(statusCode+"onSuccess"+content);
							try {
								JSONObject obj = new JSONObject(content);
								String code = obj.optString("status");
								if ("0".equals(code)) {
									String path = obj.optString("data");
									basicMember.setIcon(path);
									AppApplication.getImageLoader()
											.displayImage(path, img);
								} else {
									Toast.makeText(EditInfo.this,
											"服务器返回了异常:" + statusCode,
											Toast.LENGTH_LONG).show();
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
							progressDialog.setMessage("正在上传图片");
							progressDialog.setCancelable(false);
							progressDialog.show();
						}

					});
		} else {
			Toast.makeText(this, "文件不存在", Toast.LENGTH_LONG).show();
		}
	}

	public String saveMyBitmap(Bitmap mBitmap) {
		File f = new File(AppApplication.mSdcardImageCamera + File.separator,
				String.valueOf(System.currentTimeMillis()) + ".jpg");
		try {
			f.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			// DebugMessage.put("在保存图片时出错："+e.toString());
		}
		FileOutputStream fOut = null;
		try {
			fOut = new FileOutputStream(f);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
		try {
			fOut.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			fOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return f.toString();
	}

	private void startPhotoZoom(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// crop为true是设置在开启的intent中设置显示的view可以剪裁
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", Utils.dip2px(this, 180));
		intent.putExtra("outputY", Utils.dip2px(this, 180));
		intent.putExtra("return-data", true);
		intent.putExtra("noFaceDetection", true);
		startActivityForResult(intent, PHOTO_REQUEST_CUT);
	}

	private String getUUID() {
		Calendar calendar = Calendar.getInstance();
		long millis = calendar.getTimeInMillis();
		Random random = new Random(9);
		String temp = "";
		for (int i = 0; i < 5; i++) {
			temp += random.nextInt();
		}
		String name = "wy" + millis + temp;
		return name;
	}
}
