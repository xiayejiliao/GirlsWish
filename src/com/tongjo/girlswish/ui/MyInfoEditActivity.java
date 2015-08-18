package com.tongjo.girlswish.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;

import org.apache.http.Header;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.tongjo.bean.TJResponse;
import com.tongjo.bean.TJSchool;
import com.tongjo.bean.TJUserInfo;
import com.tongjo.girlswish.R;
import com.tongjo.girlswish.ui.TakePicturePopup.ChoicedItem;
import com.tongjo.girlswish.ui.TakePicturePopup.onChoiced;
import com.tongjo.girlswish.utils.AppConstant;
import com.tongjo.girlswish.utils.FileUtils;
import com.tongjo.girlswish.utils.ImageFileUtils;
import com.tongjo.girlswish.utils.ImageUtils;
import com.tongjo.girlswish.utils.SpUtils;
import com.tongjo.girlswish.utils.StringUtils;
import com.tongjo.girlswish.utils.ToastUtils;

import de.greenrobot.event.EventBus;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
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
	private int REQUEST_CAMERA = 3621;
	private int SELECT_FILE = 6523;
	private EditText et_name;
	private EditText et_school;
	private Button bt_next;
	private ImageView iv_icon;
	private ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_myinfoedit);
		et_name = (EditText) findViewById(R.id.et_myinfo_name);
		et_school = (EditText) findViewById(R.id.et_myinfo_school);
		bt_next = (Button) findViewById(R.id.bt_myinfo_next);
		iv_icon = (ImageView) findViewById(R.id.iv_myinfo_personico);

		bt_next.setOnClickListener(this);
		iv_icon.setOnClickListener(this);
		et_school.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (hasFocus) {
					Intent intent = new Intent(MyInfoEditActivity.this, RegisterSchollChooseActivity.class);
					startActivityForResult(intent, AppConstant.STARTFORCODE_REGISTER_SCHOOL);
				}
			}
		});
		;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_myinfo_next:
			String name = et_name.getText().toString();
			String school = et_school.getText().toString();
			if (StringUtils.isEmpty(name)) {
				ToastUtils.show(getApplicationContext(), "姓名空");
				return;
			}
			if (StringUtils.isEmpty(school)) {
				ToastUtils.show(getApplicationContext(), "学校空");
				return;
			}
			RequestParams requestParams = new RequestParams();
			requestParams.put("nickname", name);
			requestParams.put("schoolId", school);
			asyncHttpClient.post(AppConstant.URL_BASE + AppConstant.URL_PROFILE, requestParams, httpprofile);
			break;
		case R.id.et_myinfo_school:
			/*
			 * Intent intent = new Intent(MyInfoEditActivity.this,
			 * RegisterSchollChooseActivity.class);
			 * startActivityForResult(intent,
			 * AppConstant.STARTFORCODE_REGISTER_SCHOOL);
			 */
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
					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE_SECURE);
					File f = new File(android.os.Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "temp.jpg");
					intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
					startActivityForResult(intent, REQUEST_CAMERA);
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
		if (resultCode == RESULT_OK) {
			if (requestCode == REQUEST_CAMERA) {
				File f = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString());
				for (File temp : f.listFiles()) {
					if (temp.getName().equals("temp.jpg")) {
						f = temp;
						break;
					}
				}
				Bitmap bm;
				bm = BitmapFactory.decodeFile(f.getAbsolutePath());
				bm = Bitmap.createScaledBitmap(bm, iv_icon.getWidth(), iv_icon.getHeight(), true);
				bm = ImageUtils.getRoundedCornerBitmap(bm);
				iv_icon.setImageBitmap(bm);

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
				Bitmap bm = BitmapFactory.decodeFile(imgDecodableString);
				bm = ImageUtils.scaleImageTo(bm, iv_icon.getWidth(), iv_icon.getHeight());
				bm = ImageUtils.getRoundedCornerBitmap(bm);
				iv_icon.setImageBitmap(bm);
			} else if (requestCode == AppConstant.STARTFORCODE_REGISTER_SCHOOL) {
				String school = data.getStringExtra("schoolname");
				et_school.setText(school);
			}
		}
	}

	private TextHttpResponseHandler httpprofile = new TextHttpResponseHandler("UTF-8") {

		@Override
		public void onSuccess(int arg0, Header[] arg1, String arg2) {
			// TODO Auto-generated method stub
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
						TJSchool userSchool = userInfo.getSchool();
						if (userSchool != null) {
							SpUtils.put(getApplicationContext(), AppConstant.USER_SCHOOLID, userSchool.get_id().toString());
							SpUtils.put(getApplicationContext(), AppConstant.USER_SCHOOLNAME, userSchool.getName());
							SpUtils.put(getApplicationContext(), AppConstant.USER_SCHOOLCOORDINATES, userSchool.getCoordinates());
						}
					}
					Intent intent = new Intent(MyInfoEditActivity.this, LoginActivity.class);
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
			ToastUtils.show(getApplicationContext(), "完善信息失败" + arg3.toString());
		}
	};
}
