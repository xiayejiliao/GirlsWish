package com.tongjo.girlswish.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import org.apache.http.Header;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.tongjo.bean.TJResponse;
import com.tongjo.bean.TJSchool;
import com.tongjo.bean.TJUserInfo;
import com.tongjo.db.OrmLiteHelper;
import com.tongjo.girlswish.BaseApplication;
import com.tongjo.girlswish.R;
import com.tongjo.girlswish.model.LoginState;
import com.tongjo.girlswish.model.UserSex;
import com.tongjo.girlswish.utils.AppConstant;
import com.tongjo.girlswish.utils.FileUtils;
import com.tongjo.girlswish.utils.SpUtils;
import com.tongjo.girlswish.utils.StringUtils;
import com.tongjo.girlswish.utils.ToastUtils;
import com.tongjo.girlswish.widget.LinkTextView;

import de.greenrobot.event.EventBus;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class LoginActivity extends BaseActivity implements OnClickListener {
	private final String TAG = "LoginActivity";
	private Button bt_login;
	private EditText et_phone;
	private EditText et_pass;
	private ImageView iv_personico;
	private LinkTextView ltv_forgetpass;
	private LinkTextView ltv_register;
	private CheckBox cb_isremenberpass;
	private DisplayImageOptions displayImageOptions;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		setCenterText("登陆");
		bt_login = (Button) findViewById(R.id.bt_login_sure);
		et_phone = (EditText) findViewById(R.id.et_login_name);
		et_pass = (EditText) findViewById(R.id.et_login_pass);
		ltv_forgetpass = (LinkTextView) findViewById(R.id.ltv_login_forget);
		ltv_register = (LinkTextView) findViewById(R.id.ltv_login_register);
		iv_personico = (ImageView) findViewById(R.id.iv_login_personico);
		cb_isremenberpass = (CheckBox) findViewById(R.id.cb_login_isremenberpass);

		bt_login.setOnClickListener(this);
		iv_personico.setOnClickListener(this);
		ltv_forgetpass.setOnClickListener(this);
		ltv_register.setOnClickListener(this);

		et_phone.setText((String) SpUtils.get(getApplicationContext(), AppConstant.USER_PHONE, ""));

		/*
		 * setbackBtnListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { // TODO Auto-generated method
		 * stub setResult(AppConstant.FORRESULT_LOG_CANCANL, new Intent());
		 * finish(); } });
		 */
		int isremenberpassword = (Integer) SpUtils.get(getApplicationContext(), AppConstant.USER_ISREMEMBERPASSWORD, 0);
		int isremenberphone = (Integer) SpUtils.get(getApplicationContext(), AppConstant.USER_ISREMEMBERPHONE, 0);
		if (isremenberphone == 1) {
			String tel = (String) SpUtils.get(getApplicationContext(), AppConstant.USER_PHONE, "10086");
			et_phone.setText(tel);
		} else {
			et_phone.getEditableText().clear();
		}

		if (isremenberpassword == 1) {
			cb_isremenberpass.setChecked(true);
			String pass = (String) SpUtils.get(getApplicationContext(), AppConstant.USER_PASSWORD, "10086");
			et_pass.setText(pass);
		} else {
			cb_isremenberpass.setChecked(false);
		}
		Log.d(TAG,""+ isremenberpassword);
		Log.d(TAG,""+ isremenberphone);
		displayImageOptions = new DisplayImageOptions.Builder().showStubImage(R.drawable.image_imageloading) // 设置图片下载期间显示的图片
				.showImageForEmptyUri(R.drawable.image_imageloadneterror) // 设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(R.drawable.image_imageloaderror) // 设置图片加载或解码过程中发生错误显示的图片
				.cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
				.displayer(new RoundedBitmapDisplayer(180))// 设置成圆角图片
				.imageScaleType(ImageScaleType.EXACTLY)// 缩放模式按比例缩放
				.build(); // 创建配置过得DisplayImageOption对象
		imageLoader.displayImage("drawable://" + R.drawable.icon_lunch, iv_personico, displayImageOptions);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_login_sure:
			RequestParams requestParams = new RequestParams();
			final String tel = et_phone.getText().toString();
			final String password = et_pass.getText().toString();
			if (StringUtils.isEmpty(tel) || StringUtils.isEmpty(password)) {
				ToastUtils.show(getApplicationContext(), "请完善登陆信息");
				return;
			}
			requestParams.add("tel", tel);
			requestParams.add("password", password);
			final ProgressDialog dialog = ProgressDialog.show(this, "提示", "正在登陆中");

			asyncHttpClient.post(AppConstant.URL_BASE + AppConstant.URL_LOGIN, requestParams, new TextHttpResponseHandler("UTF-8") {

				@Override
				public void onSuccess(int arg0, Header[] arg1, String arg2) {
					// TODO Auto-generated method stu
					Log.d(TAG, arg2);
					if (arg0 == 200) {
						Type type = new TypeToken<TJResponse<TJUserInfo>>() {
						}.getType();
						TJResponse<TJUserInfo> response = new Gson().fromJson(arg2, type);
						//登录成功
						if (response.getResult().getCode() == 0) {
							//设置记住账号
							SpUtils.put(getApplicationContext(), AppConstant.USER_ISREMEMBERPHONE, 1);
							SpUtils.put(getApplicationContext(), AppConstant.USER_PHONE, tel);
							
							//设置是否记住密码
							if (cb_isremenberpass.isChecked()) {
								SpUtils.put(getApplicationContext(), AppConstant.USER_ISREMEMBERPASSWORD, 1);
								SpUtils.put(getApplicationContext(), AppConstant.USER_PASSWORD, password);
							}
							//持久化个人信息到getSharedPreferences
							TJUserInfo userInfo = response.getData();
							if (userInfo != null) {
								SpUtils.put(getApplicationContext(), AppConstant.USER_PHONE, tel);
								SpUtils.put(getApplicationContext(), AppConstant.USER_PASSWORD, password);
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
								imageLoader.displayImage(userInfo.getAvatarUrl(), iv_personico, displayImageOptions);
							}
							//通过EvenBus发送登录成功事件
							Message message=new Message();
							message.what=AppConstant.MESSAGE_WHAT_USER_LOGIN;
							EventBus.getDefault().post(message);
							dialog.dismiss();

						} else {
							Toast.makeText(getApplicationContext(), "登陆失败:" + response.getResult().getMessage(), Toast.LENGTH_LONG).show();
							dialog.dismiss();
						}
					} else {
						ToastUtils.show(getApplicationContext(), "登陆失败" + arg0);
					}
				}

				@Override
				public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
					Toast.makeText(getApplicationContext(), "登陆失败" + arg0, Toast.LENGTH_LONG).show();
					dialog.dismiss();
				}
			});
			break;
		case R.id.ltv_login_forget:
			Intent intent1=new Intent(LoginActivity.this, ResetPassActivity1.class);
			startActivity(intent1);
			break;
		case R.id.ltv_login_register:
			Intent intent2=new Intent(LoginActivity.this, RegisterSexChooseActivity.class);
			startActivity(intent2);
			break;
		default:
			break;
		}

	}
}
