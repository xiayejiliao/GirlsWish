package com.tongjo.girlswish.ui;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.Header;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.tongjo.bean.TJResponse;
import com.tongjo.bean.TJSchool;
import com.tongjo.bean.TJUserInfo;
import com.tongjo.girlswish.R;
import com.tongjo.girlswish.event.UserIconChange;
import com.tongjo.girlswish.ui.MyinfoActivity.AvatarUrl;
import com.tongjo.girlswish.ui.TakePicturePopup.ChoicedItem;
import com.tongjo.girlswish.ui.TakePicturePopup.onChoiced;
import com.tongjo.girlswish.utils.AppConstant;
import com.tongjo.girlswish.utils.FileUtils;
import com.tongjo.girlswish.utils.ImageFileUtils;
import com.tongjo.girlswish.utils.ImageUtils;
import com.tongjo.girlswish.utils.SpUtils;
import com.tongjo.girlswish.utils.StringUtils;
import com.tongjo.girlswish.utils.ToastUtils;
import com.umeng.analytics.MobclickAgent;

import de.greenrobot.event.EventBus;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;

/**
 * 注册完善个人资料，包括昵称，学校
 * 
 * @author 16ren
 *
 */
public class MyInfoEditActivity extends BaseActivity implements OnClickListener {
	private final int REQUEST_CAMERA = 3621;
	private final int SELECT_FILE = 6523;
	private final int SCHOOLCHOOSE = 6541;
	private EditText et_name;
	private EditText et_school;
	private Button bt_next;
	private ImageView iv_icon;
	private ProgressDialog progressDialog;
	private int sex;
	private boolean isupdateicon = false;
	private boolean ischoosesex = false;

	private String schoolid;

	// 照相机拍照后 ，图片保存的位置
	private String mCurrentPhotoPath;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_myinfoedit);
		et_name = (EditText) findViewById(R.id.et_myinfo_name);
		et_school = (EditText) findViewById(R.id.et_myinfo_school);
		bt_next = (Button) findViewById(R.id.bt_myinfo_next);
		iv_icon = (ImageView) findViewById(R.id.iv_myinfo_personico);

		sex = getIntent().getIntExtra("sex", -1);
		bt_next.setOnClickListener(this);
		iv_icon.setOnClickListener(this);
		et_school.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (hasFocus) {
					Intent intent = new Intent(MyInfoEditActivity.this, RegisterSchollChooseActivity.class);
					startActivityForResult(intent, SCHOOLCHOOSE);
				}
			}
		});

	}

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("资料完善");
		// 友盟用户活跃统计
		MobclickAgent.onResume(this);
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("资料完善");
		// 友盟用户活跃统计
		MobclickAgent.onPause(this);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_myinfo_next:
			String name = et_name.getText().toString();
			String school = et_school.getText().toString();
			/*
			 * if(isupdateicon==false){ ToastUtils.show(getApplicationContext(),
			 * "请上传头像"); return; } if (ischoosesex == false) {
			 * ToastUtils.show(getApplicationContext(), "选择性别"); return; } if
			 * (StringUtils.isEmpty(name)) {
			 * ToastUtils.show(getApplicationContext(), "姓名空"); return; }
			 */
			/*
			 * if (StringUtils.isEmpty(school)) {
			 * ToastUtils.show(getApplicationContext(), "学校空"); return; }
			 */

			RequestParams requestParams = new RequestParams();
			requestParams.put("nickname", name);
			requestParams.put("schoolId", schoolid);
			if (sex != -1) {
				requestParams.put("gender", sex);
			}
			asyncHttpClient.post(AppConstant.URL_BASE + AppConstant.URL_PROFILE, requestParams, httpprofile);
			progressDialog = new ProgressDialog(MyInfoEditActivity.this);
			progressDialog.show();
			break;
		case R.id.et_myinfo_school:
			Intent intent = new Intent(MyInfoEditActivity.this, RegisterSchollChooseActivity.class);
			startActivityForResult(intent, SCHOOLCHOOSE);
			break;
		case R.id.iv_myinfo_personico:
			selectImage();
			break;
		default:
			break;
		}
	}

	private void selectImage() {
		final CharSequence[] items = { "照相机", "相册", "取消" };

		AlertDialog.Builder builder = new AlertDialog.Builder(MyInfoEditActivity.this);
		builder.setTitle("Add Photo!");
		builder.setItems(items, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int item) {
				if (items[item].equals("照相机")) {
					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					if (intent.resolveActivity(getPackageManager()) != null) {
						try {
							File f = createImageFile();
							// 指定了MediaStore.EXTRA_OUTPUT后，返回的intent会是null
							intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
							Bundle outState = new Bundle();
							outState.putString("mCurrentPhotoPath", mCurrentPhotoPath);
							onSaveInstanceState(outState);
							startActivityForResult(intent, REQUEST_CAMERA);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							MobclickAgent.reportError(MyInfoEditActivity.this, e);
							e.printStackTrace();
						}
					}

				} else if (items[item].equals("相册")) {
					Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
					intent.setType("image/*");
					startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
				} else if (items[item].equals("取消")) {
					dialog.dismiss();
				}
			}
		});
		builder.show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		System.out.println(resultCode);
		System.out.println(requestCode);
		if (resultCode == RESULT_OK) {
			if (requestCode == REQUEST_CAMERA) {
				ImageLoader.getInstance().displayImage("file://" + mCurrentPhotoPath, iv_icon, new SimpleImageLoadingListener() {
					@Override
					public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
						super.onLoadingComplete(imageUri, view, loadedImage);
						// 上传头像
						ByteArrayOutputStream out = new ByteArrayOutputStream();
						loadedImage.compress(Bitmap.CompressFormat.PNG, 85, out);
						RequestParams params = new RequestParams();
						params.put("image", new ByteArrayInputStream(out.toByteArray()));
						asyncHttpClient.post(AppConstant.URL_BASE + AppConstant.URL_UPLOADICON, params, updateavatar);
					}

					@Override
					public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
						super.onLoadingFailed(imageUri, view, failReason);
						ToastUtils.show(getApplicationContext(), failReason.toString());
						MobclickAgent.reportError(getApplicationContext(), "照相失败" + failReason.getCause().toString());
						System.out.println(failReason.getCause().toString());
					}
				});
				galleryAddPic();

			} else if (requestCode == SELECT_FILE) {
				Uri selectedImage = data.getData();
				String[] filePathColumn = { MediaStore.Images.Media.DATA };
				// Get the cursor
				Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
				// Move to first row
				cursor.moveToFirst();
				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
				String imgDecodableString = cursor.getString(columnIndex);
				cursor.close();
				ImageLoader.getInstance().displayImage("file://" + imgDecodableString, iv_icon, new SimpleImageLoadingListener() {
					@Override
					public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
						super.onLoadingComplete(imageUri, view, loadedImage);
						// 上传头像
						ByteArrayOutputStream out = new ByteArrayOutputStream();
						loadedImage.compress(Bitmap.CompressFormat.PNG, 85, out);
						RequestParams params = new RequestParams();
						params.put("image", new ByteArrayInputStream(out.toByteArray()));
						asyncHttpClient.post(AppConstant.URL_BASE + AppConstant.URL_UPLOADICON, params, updateavatar);
					}

					@Override
					public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
						super.onLoadingFailed(imageUri, view, failReason);
						ToastUtils.show(getApplicationContext(), failReason.toString());
						MobclickAgent.reportError(getApplicationContext(), "获取图片失败" + failReason.getCause().toString());
						System.out.println(failReason.getCause().toString());
					}
				});
			} else if (requestCode == SCHOOLCHOOSE) {
				String schoolname = data.getStringExtra("schoolname");
				schoolid = data.getStringExtra("schoolid");
				et_school.setText(schoolname);
			}
		}
	}

	//获取一个图片保存路径
	private File createImageFile() throws IOException {
		// Create an image file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		String imageFileName = "JPEG_" + timeStamp + "_";
		File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
		File image = File.createTempFile(imageFileName, /* prefix */
				".jpg", /* suffix */
				storageDir /* directory */
		);
		// Save a file: path for use with ACTION_VIEW intents
		mCurrentPhotoPath = image.getAbsolutePath();
		return image;
	}
	//Add the Photo to a Gallery
	private void galleryAddPic() {
		Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
		File f = new File(mCurrentPhotoPath);
		Uri contentUri = Uri.fromFile(f);
		mediaScanIntent.setData(contentUri);
		this.sendBroadcast(mediaScanIntent);
	}

	private TextHttpResponseHandler httpprofile = new TextHttpResponseHandler("UTF-8") {

		@Override
		public void onSuccess(int arg0, Header[] arg1, String arg2) {
			// TODO Auto-generated method stub
			progressDialog.dismiss();
			if (arg0 == 200) {
				Type type = new TypeToken<TJResponse<TJUserInfo>>() {
				}.getType();
				TJResponse<TJUserInfo> response = new Gson().fromJson(arg2, type);
				// 完善信息成功
				if (response.getResult().getCode() == 0) {

					TJUserInfo userInfo = response.getData();
					if (userInfo != null) {
						SpUtils.put(getApplicationContext(), AppConstant.USER_LOGINSTATE, 1);
						SpUtils.put(getApplicationContext(), AppConstant.USER_ID, userInfo.get_id().toString());
						SpUtils.put(getApplicationContext(), AppConstant.USER_NAME, userInfo.getRealname());
						SpUtils.put(getApplicationContext(), AppConstant.USER_NICKNAME, userInfo.getNickname());
						SpUtils.put(getApplicationContext(), AppConstant.USER_SEX, userInfo.getGender());
						SpUtils.put(getApplicationContext(), AppConstant.USER_ICONURL, userInfo.getAvatarUrl());
						SpUtils.put(getApplicationContext(), AppConstant.USER_EMAIL, userInfo.getEmail());
						SpUtils.put(getApplicationContext(), AppConstant.USER_PHONE, userInfo.getTel());
						TJSchool userSchool = userInfo.getSchool();
						loginEMChat(userInfo.getHxid(), userInfo.getHxpassword());
						if (userSchool != null) {
							SpUtils.put(getApplicationContext(), AppConstant.USER_SCHOOLID, userSchool.get_id().toString());
							SpUtils.put(getApplicationContext(), AppConstant.USER_SCHOOLNAME, userSchool.getName());
							SpUtils.put(getApplicationContext(), AppConstant.USER_SCHOOLCOORDINATES, userSchool.getCoordinates());
						}
					}
					Intent intent = new Intent(MyInfoEditActivity.this, MainActivity.class);
					startActivity(intent);
					MyInfoEditActivity.this.finish();

				} else {
					Toast.makeText(getApplicationContext(), "完善信息失败:" + response.getResult().getMessage(), Toast.LENGTH_LONG).show();
				}
			} else {
				ToastUtils.show(getApplicationContext(), "完善信息失败" + arg0);
			}
		}

		@Override
		public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
			// TODO Auto-generated method stub
			progressDialog.dismiss();
			ToastUtils.show(getApplicationContext(), "完善信息失败" + arg3.toString());
		}
	};
	private TextHttpResponseHandler updateavatar = new TextHttpResponseHandler("UTF-8") {

		@Override
		public void onSuccess(int arg0, Header[] arg1, String arg2) {
			System.out.println(arg2);
			if (arg0 == 200) {
				Type type = new TypeToken<TJResponse<AvatarUrl>>() {
				}.getType();
				TJResponse<AvatarUrl> response = new Gson().fromJson(arg2, type);
				if (response.getResult().getCode() == 0) {
					ToastUtils.show(MyInfoEditActivity.this, "修改头像成功");
					AvatarUrl avatarUrl = response.getData();
					SpUtils.put(getApplicationContext(), AppConstant.USER_ICONURL, avatarUrl.getAvatarUrl());
					isupdateicon = true;
					EventBus.getDefault().post(new UserIconChange(avatarUrl.getAvatarUrl()));

				} else {
					ToastUtils.show(MyInfoEditActivity.this, "修改头像失败" + response.getResult().getMessage());
				}
			} else {
				ToastUtils.show(MyInfoEditActivity.this, "(" + arg0 + ")");
				System.out.println("(" + arg0 + ")");
			}
		}

		@Override
		public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
			// TODO Auto-generated method stub
			ToastUtils.show(MyInfoEditActivity.this, "(" + arg0 + ")" + arg3.toString());
			System.out.println("(" + arg0 + ")" + arg3.toString());
		}
	};

	private void loginEMChat(String username, String password) {
		EMChatManager.getInstance().login(username, password, new EMCallBack() {// 回调
					@Override
					public void onSuccess() {
						runOnUiThread(new Runnable() {
							public void run() {
								EMGroupManager.getInstance().loadAllGroups();
								EMChatManager.getInstance().loadAllConversations();
								Log.d("main", "登陆聊天服务器成功！");
							}
						});
					}

					@Override
					public void onProgress(int progress, String status) {

					}

					@Override
					public void onError(int code, String message) {
						Log.d("main", "登陆聊天服务器失败！");
					}
				});
	}
}
