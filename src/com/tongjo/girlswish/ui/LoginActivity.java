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
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.tongjo.bean.TJResponse;
import com.tongjo.bean.TJUserInfo;
import com.tongjo.db.OrmLiteHelper;
import com.tongjo.girlswish.BaseApplication;
import com.tongjo.girlswish.R;
import com.tongjo.girlswish.model.LoginState;
import com.tongjo.girlswish.utils.AppConstant;
import com.tongjo.girlswish.utils.SpUtils;
import com.tongjo.girlswish.widget.LinkTextView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class LoginActivity extends BaseActivity implements OnClickListener {

	private Button bt_login;
	private EditText et_phone;
	private EditText et_pass;
	private ImageButton iv_personico;
	private LinkTextView ltv_forgetpass;
	private LinkTextView ltv_register;
	private SyncHttpClient syncHttpClient;
	private AsyncHttpClient asyncHttpClient;
	private Dao<TJUserInfo, Integer> tjuserinfoDao;
	private OrmLiteHelper ormLiteHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		bt_login = (Button) findViewById(R.id.bt_login_sure);
		et_phone = (EditText) findViewById(R.id.et_login_name);
		et_pass = (EditText) findViewById(R.id.et_login_pass);
		ltv_forgetpass = (LinkTextView) findViewById(R.id.ltv_login_forget);
		ltv_register = (LinkTextView) findViewById(R.id.ltv_login_register);
		iv_personico = (ImageButton) findViewById(R.id.iv_login_personico);

		bt_login.setOnClickListener(this);
		iv_personico.setOnClickListener(this);
		ltv_forgetpass.setOnClickListener(this);
		ltv_register.setOnClickListener(this);
		et_phone.setText((String) SpUtils.get(getApplicationContext(), AppConstant.USER_PHONE, ""));

		// 异步网络请求客户端
		asyncHttpClient = ((BaseApplication) getApplication()).getAsyncHttpClient();

		// 初始化数据数据持久化
		ormLiteHelper = OpenHelperManager.getHelper(getApplicationContext(), OrmLiteHelper.class);
		tjuserinfoDao = ormLiteHelper.getTJUserInfoDao();

		// 配置图像下载工具
		ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(this);
		ImageLoader.getInstance().init(configuration);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_login_sure:
			RequestParams requestParams = new RequestParams();
			final String tel = et_phone.getText().toString();
			final String password = et_pass.getText().toString();
			requestParams.add("tel", tel);
			requestParams.add("password", password);
			asyncHttpClient.post(AppConstant.URL_BASE + AppConstant.URL_LOGIN, requestParams, new TextHttpResponseHandler("UTF-8") {

				@Override
				public void onSuccess(int arg0, Header[] arg1, String arg2) {
					// TODO Auto-generated method stub
					if (arg0 == 200) {
						Type type = new TypeToken<TJResponse<TJUserInfo>>() {
						}.getType();
						TJResponse<TJUserInfo> response = new Gson().fromJson(arg2, type);
						if (response.getResult().getCode() == 0) {
							try {
								// 保存用户数据到sqllite
								tjuserinfoDao.createIfNotExists(response.getData());
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							// 将手机号、密码、登陆状态，登陆用户的uuid保存到SharedPreferences
							SpUtils.put(getApplicationContext(), AppConstant.USER_PHONE, tel);
							SpUtils.put(getApplicationContext(), AppConstant.USER_PASSWORD, password);
							SpUtils.put(getApplicationContext(), AppConstant.USER_LOGINSTATE, LoginState.LOGIN);
							SpUtils.put(getApplicationContext(), AppConstant.USER_ID, response.getData().get_id().toString());
							final String iconpath = getFilesDir().toString() + "/image/usericon.jpg";
							SpUtils.put(getApplicationContext(), AppConstant.USER_ICONPATH, iconpath);

							// 登陆成功后下载图像并保存在iconpath
							ImageLoader.getInstance().loadImage(response.getData().getAvatarUrl(), new ImageSize(200, 200), new SimpleImageLoadingListener() {
								public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
									iv_personico.setImageBitmap(loadedImage);
									// 保存图像
									CompressFormat format = Bitmap.CompressFormat.JPEG;
									try {
										File file = new File(iconpath);
										if (file.exists())
											file.delete();
										file.createNewFile();
										OutputStream stream = new FileOutputStream(file);
										loadedImage.compress(format, 100, stream);
										stream.close();
									} catch (IOException e) {
										Intent intent = new Intent();
										intent.putExtra(AppConstant.USER_ICONPATH, "");
										setResult(AppConstant.FORRESULT_LOG_OK, intent);
										finish();
										e.printStackTrace();
									}
									Intent intent = new Intent();
									intent.putExtra(AppConstant.USER_ICONPATH, iconpath);
									setResult(AppConstant.FORRESULT_LOG_OK, intent);
									finish();
								};

								public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
									Intent intent = new Intent();
									intent.putExtra(AppConstant.USER_ICONPATH, "");
									setResult(AppConstant.FORRESULT_LOG_OK, intent);
									finish();
								};
							});

						} else {
							Toast.makeText(getApplicationContext(), "登陆失败:" + response.getResult().getMessage(), Toast.LENGTH_LONG).show();
						}
					} else {

					}
				}

				@Override
				public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
					Toast.makeText(getApplicationContext(), "登陆失败" + arg0, Toast.LENGTH_LONG).show();
				}
			});
			break;

		default:
			break;
		}

	}
}
