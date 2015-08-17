package com.tongjo.girlswish.ui;

import java.lang.reflect.Type;

import org.apache.http.Header;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;
import com.tongjo.bean.TJResponse;
import com.tongjo.bean.TJUserInfo;
import com.tongjo.girlswish.BaseApplication;
import com.tongjo.girlswish.R;
import com.tongjo.girlswish.event.UserLogout;
import com.tongjo.girlswish.utils.AppConstant;
import com.tongjo.girlswish.utils.SpUtils;
import com.tongjo.girlswish.utils.ToastUtils;
import com.tongjo.girlswish.widget.ButtonWithArrow;
import com.umeng.analytics.MobclickAgent;

import de.greenrobot.event.EventBus;
import android.R.anim;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class SettingActivity extends AppCompatActivity implements OnClickListener {
	private ButtonWithArrow bt_resetpass;
	private ButtonWithArrow bt_about;
	private ButtonWithArrow bt_suggest;
	private Button bt_exit;
	private AsyncHttpClient mAsyncHttpClient;
	private ActionBar mActionBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		mActionBar=getSupportActionBar();
		mActionBar.setDisplayHomeAsUpEnabled(true);
		mActionBar.setTitle("设置");
		bt_resetpass = (ButtonWithArrow) findViewById(R.id.bt_setting_resetpass);
		bt_about = (ButtonWithArrow) findViewById(R.id.bt_setting_about);
		bt_exit = (Button) findViewById(R.id.bt_setting_exit);
		bt_suggest = (ButtonWithArrow) findViewById(R.id.bt_setting_suggest);
		bt_resetpass.setOnClickListener(this);
		bt_about.setOnClickListener(this);
		bt_exit.setOnClickListener(this);
		bt_suggest.setOnClickListener(this);

		mAsyncHttpClient = ((BaseApplication) getApplication()).getAsyncHttpClient();
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onResume(this);//友盟页面统计
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);//友盟页面统计
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
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bt_setting_resetpass:
			startActivity(new Intent(SettingActivity.this, ResetPassActivity.class));
			break;
		case R.id.bt_setting_about:
			startActivity(new Intent(SettingActivity.this, AboutActivity.class));
			break;
		case R.id.bt_setting_suggest:
			startActivity(new Intent(SettingActivity.this, SuggestActivity.class));
			break;
		case R.id.bt_setting_exit:
			mAsyncHttpClient.post(AppConstant.URL_BASE + AppConstant.URL_LOGOUT, logouTextHttpResponseHandler);
			break;

		default:
			break;
		}
	}

	private TextHttpResponseHandler logouTextHttpResponseHandler = new TextHttpResponseHandler("UTF-8") {

		@Override
		public void onSuccess(int arg0, Header[] arg1, String arg2) {
			// TODO Auto-generated method stub
			if (arg0 == 200) {
				Type type = new TypeToken<TJResponse<TJUserInfo>>() {
				}.getType();
				TJResponse<TJUserInfo> response = new Gson().fromJson(arg2, type);
				if (response.getResult().getCode() == 0) {
					SpUtils.put(getApplicationContext(), AppConstant.USER_ID, "");
					SpUtils.put(getApplicationContext(), AppConstant.USER_SEX, 0);
					SpUtils.put(getApplicationContext(), AppConstant.USER_PHONE, "");
					SpUtils.put(getApplicationContext(), AppConstant.USER_NAME, "");
					SpUtils.put(getApplicationContext(), AppConstant.USER_LOGINSTATE, 0);
					SpUtils.put(getApplicationContext(), AppConstant.USER_ICONURL, "");
					SpUtils.put(getApplicationContext(), AppConstant.USER_SCHOOLNAME, "");
					ToastUtils.show(SettingActivity.this, "退出当前账号");
					EventBus.getDefault().post(new UserLogout());
					startActivity(new Intent(SettingActivity.this,LoginActivity.class));
					finish();

				} else {
					ToastUtils.show(SettingActivity.this, response.getResult().getCode() + response.getResult().getMessage());
				}
			} else {
				ToastUtils.show(SettingActivity.this, arg0 + arg2);
			}
		}

		@Override
		public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
			// TODO Auto-generated method stub
			ToastUtils.show(SettingActivity.this, arg0 + arg3.toString());
		}
	};

}
