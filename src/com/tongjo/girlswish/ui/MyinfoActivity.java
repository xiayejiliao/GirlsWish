package com.tongjo.girlswish.ui;

import java.io.File;
import java.lang.reflect.Type;

import org.apache.http.Header;

import u.aly.bu;

import com.easemob.chat.core.ac;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.tongjo.bean.TJResponse;
import com.tongjo.bean.TJUserInfo;
import com.tongjo.girlswish.BaseApplication;
import com.tongjo.girlswish.R;
import com.tongjo.girlswish.utils.AppConstant;
import com.tongjo.girlswish.utils.ImageUtils;
import com.tongjo.girlswish.utils.SpUtils;
import com.tongjo.girlswish.utils.ToastUtils;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MyinfoActivity extends Activity implements OnClickListener {
	private final static int SCHOOL = 123;
	private final static int CAMER = 456;
	private final static int GALLERY = 789;
	private ImageView iv_icon;
	private TextView tv_nicker;
	private TextView tv_school;
	private AsyncHttpClient asyncHttpClient;
	private Uri mImageCaptureUri;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_myinfo);
		iv_icon = (ImageView) findViewById(R.id.iv_myinfo_icon);
		tv_nicker = (TextView) findViewById(R.id.tv_myinfo_nicker);
		tv_school = (TextView) findViewById(R.id.tv_myinfo_school);
		tv_nicker.setOnClickListener(this);
		tv_school.setOnClickListener(this);
		iv_icon.setOnClickListener(this);
		asyncHttpClient = ((BaseApplication) getApplication()).getAsyncHttpClient();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if (requestCode == SCHOOL) {
				String schoolname = data.getStringExtra("schoolname");
				tv_school.setText(schoolname);
				RequestParams params = new RequestParams();
				params.put("nickname", tv_nicker.getText());
				params.put("schoolId", tv_school.getText());
				asyncHttpClient.post(AppConstant.URL_BASE + AppConstant.URL_USEREDIT, updatenikeandschool);
			}
			if (requestCode == CAMER) {
				String path = mImageCaptureUri.getPath();
				Bitmap bitmap = BitmapFactory.decodeFile(path);
				//bitmap = ImageUtils.scaleImageTo(bitmap, iv_icon.getWidth(), iv_icon.getHeight());
				bitmap = com.easemob.util.ImageUtils.getRoundedCornerBitmap(bitmap);
				iv_icon.setImageBitmap(bitmap);
				RequestParams params = new RequestParams();
				params.put("image", bitmap);
				asyncHttpClient.post(AppConstant.URL_BASE + AppConstant.URL_UPLOADICON, params, updateavatar);
			}
			if (requestCode == GALLERY) {
				mImageCaptureUri = data.getData();
				String path = getRealPathFromURI(mImageCaptureUri); // from
				if (path == null)
					path = mImageCaptureUri.getPath(); // from File Manager
				if (path != null) {
					Bitmap bitmap = BitmapFactory.decodeFile(path);
					//iv_icon.setImageBitmap(bitmap);
					//bitmap = ImageUtils.scaleImageTo(bitmap, iv_icon.getWidth(), iv_icon.getHeight());
					bitmap = com.easemob.util.ImageUtils.getRoundedCornerBitmap(bitmap);
					iv_icon.setImageBitmap(bitmap);
					RequestParams params = new RequestParams();
					params.put("image", bitmap);
					asyncHttpClient.post(AppConstant.URL_BASE + AppConstant.URL_UPLOADICON, params, updateavatar);
				}
			}
		}
	}

	private void geticon() {
		String[] item = { "相册", "照相机", "取消" };
		Builder builder = new Builder(this);
		builder.setItems(item, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				switch (which) {
				case 1:
					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE_SECURE);
					File file = new File(Environment.getExternalStorageDirectory(), "tmp_avatar_" + String.valueOf(System.currentTimeMillis()) + ".jpg");
					mImageCaptureUri = Uri.fromFile(file);
					try {
						intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
						intent.putExtra("return-data", true);
						startActivityForResult(intent, CAMER);
					} catch (Exception e) {
						e.printStackTrace();
					}
					break;
				case 0:
					Intent imageIntent = new Intent();
					imageIntent.setType("image/*");
					imageIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
					startActivityForResult(Intent.createChooser(imageIntent, "选择图片"), GALLERY);
					break;
				case 2:
					dialog.dismiss();
					break;
				default:
					break;
				}
			}
		});
		builder.show();
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
				params.put("nickname", tv_nicker.getText());
				params.put("schoolId", tv_school.getText());
				asyncHttpClient.post(AppConstant.URL_BASE + AppConstant.URL_USEREDIT, updatenikeandschool);
			}
		});
		builder.setNegativeButton("取消", null);
		builder.show();
	}

	private TextHttpResponseHandler updatenikeandschool = new TextHttpResponseHandler("UTF-8") {

		@Override
		public void onSuccess(int arg0, Header[] arg1, String arg2) {
			if (arg0 == 200) {
				Type type = new TypeToken<TJResponse<TJUserInfo>>() {
				}.getType();
				TJResponse<TJUserInfo> response = new Gson().fromJson(arg2, type);
				if (response.getResult().getCode() == 0) {
					ToastUtils.show(MyinfoActivity.this, "修改成功");
					SpUtils.put(getApplicationContext(), AppConstant.USER_NICKNAME, tv_nicker.getText());
					SpUtils.put(getApplicationContext(), AppConstant.USER_SCHOOLNAME, tv_school.getText());
				} else {
					ToastUtils.show(MyinfoActivity.this, "修改失败" + response.getResult().getMessage());
				}
			} else {
				ToastUtils.show(MyinfoActivity.this, "(" + arg0 + ")");
			}
		}

		@Override
		public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
			// TODO Auto-generated method stub
			ToastUtils.show(MyinfoActivity.this, "(" + arg1 + ")" + arg3.toString());
		}
	};
	private TextHttpResponseHandler updateavatar = new TextHttpResponseHandler("UTF-8") {

		@Override
		public void onSuccess(int arg0, Header[] arg1, String arg2) {
			if (arg0 == 200) {
				Type type = new TypeToken<TJResponse<String>>() {
				}.getType();
				TJResponse<String> response = new Gson().fromJson(arg2, type);
				if (response.getResult().getCode() == 0) {
					ToastUtils.show(MyinfoActivity.this, "修改头像成功");
					SpUtils.put(getApplicationContext(), AppConstant.USER_ICONURL, response.getData().toString());
				} else {
					ToastUtils.show(MyinfoActivity.this, "修改头像失败" + response.getResult().getMessage());
				}
			} else {
				ToastUtils.show(MyinfoActivity.this, "(" + arg0 + ")");
			}
		}

		@Override
		public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
			// TODO Auto-generated method stub
			ToastUtils.show(MyinfoActivity.this, "(" + arg1 + ")" + arg3.toString());
		}
	};
}
