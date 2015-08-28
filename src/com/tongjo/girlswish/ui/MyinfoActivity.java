package com.tongjo.girlswish.ui;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import u.aly.bu;

import com.easemob.chat.core.ac;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.squareup.picasso.Picasso;
import com.tongjo.bean.TJResponse;
import com.tongjo.bean.TJUserInfo;
import com.tongjo.girlswish.BaseApplication;
import com.tongjo.girlswish.R;
import com.tongjo.girlswish.event.UserIconChange;
import com.tongjo.girlswish.event.UserNicknameChange;
import com.tongjo.girlswish.event.UserSchoolnameChange;
import com.tongjo.girlswish.utils.AppConstant;
import com.tongjo.girlswish.utils.CircleTransform;
import com.tongjo.girlswish.utils.ImageUtils;
import com.tongjo.girlswish.utils.SpUtils;
import com.tongjo.girlswish.utils.StringUtils;
import com.tongjo.girlswish.utils.ToastUtils;
import com.tongjo.girlswish.widget.CircleImageView;
import com.umeng.analytics.MobclickAgent;

import de.greenrobot.event.EventBus;
import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MyinfoActivity extends AppCompatActivity implements OnClickListener {
	private final static int SCHOOL = 123;
	private final static int CAMER = 456;
	private final static int GALLERY = 789;
	private final static int NEWICON = 745;
	private int REQUEST_CAMERA = 3621;
	private int SELECT_FILE = 6523;
	private ImageView iv_icon;
	private TextView tv_nicker;
	private TextView tv_school;
	private TextView tv_sex;
	private TextView tv_phone;
	private AsyncHttpClient asyncHttpClient;
	private Uri mImageCaptureUri;
	private ActionBar actionBar;
	// 照相机拍照后 ，图片保存的位置
	private String mCurrentPhotoPath;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_myinfo);

		actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setTitle("主页");

		iv_icon = (ImageView) findViewById(R.id.iv_myinfo_icon);
		tv_nicker = (TextView) findViewById(R.id.tv_myinfo_nicker);
		tv_school = (TextView) findViewById(R.id.tv_myinfo_school);
		tv_phone = (TextView) findViewById(R.id.tv_myinfo_phone);
		tv_sex = (TextView) findViewById(R.id.tv_myinfo_sex);
		tv_nicker.setOnClickListener(this);
		tv_school.setOnClickListener(this);
		iv_icon.setOnClickListener(this);
		asyncHttpClient = ((BaseApplication) getApplication()).getAsyncHttpClient();

		tv_nicker.setText(SpUtils.get(getApplicationContext(), AppConstant.USER_NICKNAME, "姓名").toString());
		tv_school.setText(SpUtils.get(getApplicationContext(), AppConstant.USER_SCHOOLNAME, "学校").toString());
		Integer sex = (Integer) SpUtils.get(getApplicationContext(), AppConstant.USER_SEX, 1);
		if (sex.equals(1)) {
			tv_sex.setText("男");
		} else {
			tv_sex.setText("女");
		}

		tv_phone.setText(SpUtils.get(getApplicationContext(), AppConstant.USER_PHONE, "电话").toString());
		String iconurl = SpUtils.get(getApplicationContext(), AppConstant.USER_ICONURL, "").toString();
		if (!StringUtils.isEmpty(iconurl)) {
			ImageLoader.getInstance().displayImage(iconurl, iv_icon);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("个人信息");
		// 友盟用户活跃统计
		MobclickAgent.onResume(this);
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("个人信息");
		// 友盟用户活跃统计
		MobclickAgent.onPause(this);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;
		default:
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_myinfo_icon:
			geticon();
			break;
		case R.id.tv_myinfo_nicker:
			changenicker();
			break;
		case R.id.tv_myinfo_school:
			Intent schoolintent = new Intent(this, RegisterSchollChooseActivity.class);
			startActivityForResult(schoolintent, SCHOOL);
			break;
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if (requestCode == SCHOOL) {
				String schoolname = data.getStringExtra("schoolname");
				String schoolid = data.getStringExtra("schoolid");
				tv_school.setText(schoolname);
				RequestParams params = new RequestParams();
				params.put("schoolId", schoolid);
				asyncHttpClient.post(AppConstant.URL_BASE + AppConstant.URL_USEREDIT, params, updateschool);
			}
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
						MobclickAgent.reportError(getApplicationContext(), "照相失败" + failReason.getCause().toString());
						System.out.println(failReason.getCause().toString());
					}
				});
			}

		}
	}

	private void geticon() {
		final String[] items = { "相册", "照相机", "取消" };
		Builder builder = new Builder(this);
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
							MobclickAgent.reportError(MyinfoActivity.this, e);
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

	// Add the Photo to a Gallery
	private void galleryAddPic() {
		Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
		File f = new File(mCurrentPhotoPath);
		Uri contentUri = Uri.fromFile(f);
		mediaScanIntent.setData(contentUri);
		this.sendBroadcast(mediaScanIntent);
	}

	public String getRealPathFromURI(Uri contentUri) {
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor cursor = managedQuery(contentUri, proj, null, null, null);
		if (cursor == null)
			return null;
		int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}

	private void changenicker() {
		final EditText nicker = new EditText(this);
		Builder builder = new Builder(this);
		builder.setTitle("请输入新昵称");
		builder.setView(nicker);
		builder.setIcon(android.R.drawable.ic_dialog_info);
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				tv_nicker.setText(nicker.getText().toString());
				RequestParams params = new RequestParams();
				params.put("nickname", nicker.getText().toString());
				asyncHttpClient.post(AppConstant.URL_BASE + AppConstant.URL_USEREDIT, params, updatenike);
			}
		});
		builder.setNegativeButton("取消", null);
		builder.show();
	}

	private TextHttpResponseHandler updatenike = new TextHttpResponseHandler("UTF-8") {

		@Override
		public void onSuccess(int arg0, Header[] arg1, String arg2) {
			if (arg0 == 200) {
				Type type = new TypeToken<TJResponse<TJUserInfo>>() {
				}.getType();
				TJResponse<TJUserInfo> response = new Gson().fromJson(arg2, type);
				if (response.getResult().getCode() == 0) {
					ToastUtils.show(MyinfoActivity.this, "修改昵称成功");
					SpUtils.put(getApplicationContext(), AppConstant.USER_NICKNAME, response.getData().getNickname());
					EventBus.getDefault().post(new UserNicknameChange(response.getData().getNickname()));
				} else {
					ToastUtils.show(MyinfoActivity.this, "修改昵称失败" + response.getResult().getMessage());
				}
			} else {
				ToastUtils.show(MyinfoActivity.this, "(" + arg0 + ")");
			}
		}

		@Override
		public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
			ToastUtils.show(MyinfoActivity.this, "(" + arg1 + ")" + arg3.toString());
		}
	};
	private TextHttpResponseHandler updateschool = new TextHttpResponseHandler("UTF-8") {

		@Override
		public void onSuccess(int arg0, Header[] arg1, String arg2) {
			if (arg0 == 200) {
				Type type = new TypeToken<TJResponse<TJUserInfo>>() {
				}.getType();
				TJResponse<TJUserInfo> response = new Gson().fromJson(arg2, type);
				if (response.getResult().getCode() == 0) {
					ToastUtils.show(MyinfoActivity.this, "修改学校成功");
					SpUtils.put(getApplicationContext(), AppConstant.USER_SCHOOLNAME, response.getData().getSchool().getName());
					SpUtils.put(getApplicationContext(), AppConstant.USER_SCHOOLID, response.getData().getSchool().get_id().toString());
					EventBus.getDefault().post(new UserSchoolnameChange(response.getData().getSchool().getName()));
				} else {
					ToastUtils.show(MyinfoActivity.this, "修改学校失败" + response.getResult().getMessage());
				}
			} else {
				ToastUtils.show(MyinfoActivity.this, "(" + arg0 + ")");
			}
		}

		@Override
		public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
			ToastUtils.show(MyinfoActivity.this, "(" + arg1 + ")" + arg3.toString());
		}
	};
	private TextHttpResponseHandler updateavatar = new TextHttpResponseHandler("UTF-8") {

		@Override
		public void onSuccess(int arg0, Header[] arg1, String arg2) {
			if (arg0 == 200) {
				Type type = new TypeToken<TJResponse<AvatarUrl>>() {
				}.getType();
				TJResponse<AvatarUrl> response = new Gson().fromJson(arg2, type);
				if (response.getResult().getCode() == 0) {
					ToastUtils.show(MyinfoActivity.this, "修改头像成功");
					AvatarUrl avatarUrl = response.getData();
					SpUtils.put(getApplicationContext(), AppConstant.USER_ICONURL, avatarUrl.getAvatarUrl());
					EventBus.getDefault().post(new UserIconChange(avatarUrl.getAvatarUrl()));

				} else {
					ToastUtils.show(MyinfoActivity.this, "修改头像失败" + response.getResult().getMessage());
				}
			} else {
				ToastUtils.show(MyinfoActivity.this, "(" + arg0 + ")");
				System.out.println("(" + arg0 + ")");
			}
		}

		@Override
		public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
			// TODO Auto-generated method stub
			ToastUtils.show(MyinfoActivity.this, "(" + arg0 + ")" + arg3.toString());
			System.out.println("(" + arg0 + ")" + arg3.toString());
		}
	};

	class AvatarUrl {
		private String avatarUrl;

		public String getAvatarUrl() {
			return avatarUrl;
		}

		public void setAvatarUrl(String avatarUrl) {
			this.avatarUrl = avatarUrl;
		}

		public AvatarUrl() {
			super();
		}
	}
}
