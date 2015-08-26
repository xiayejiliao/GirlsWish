package com.tongjo.girlswish.ui;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.Header;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.tongjo.bean.TJResponse;
import com.tongjo.bean.TJUserInfo;
import com.tongjo.girlswish.BaseApplication;
import com.tongjo.girlswish.R;
import com.tongjo.girlswish.ui.TakePicturePopup.onChoiced;
import com.tongjo.girlswish.utils.AppConstant;
import com.tongjo.girlswish.utils.MyTimer;
import com.tongjo.girlswish.utils.MyTimer.TimerProgress;
import com.tongjo.girlswish.utils.RandomUtils;
import com.tongjo.girlswish.utils.SMSHelper;
import com.tongjo.girlswish.utils.SMSHelper.SMSTYPE;
import com.tongjo.girlswish.utils.SMSHelper.SendResult;
import com.tongjo.girlswish.utils.SpUtils;
import com.tongjo.girlswish.utils.StringUtils;
import com.tongjo.girlswish.utils.ToastUtils;
import com.umeng.analytics.MobclickAgent;

import android.R.bool;
import android.R.integer;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * @author 16ren 更改密码时，获取手机验证码，验证手机
 */
public class ResetPassActivity extends AppCompatActivity {
	private final static String TAG = "ResetPassActivity1";
	public final static int MESSAGE_TIMERUNING = 100;
	public final static int MESSAGE_TIMESTOP = 200;
	private TextView bt_getcaptcha;
	private Button bt_sure;
	private EditText et_phone;
	private EditText et_captcha;
	private EditText et_newpassword;
	private String captcha = "1234";
	private String sendphone;
	private String nespassword;

	private ActionBar mActionBar;
	private AsyncHttpClient asyncHttpClient;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_resetpass);
		mActionBar = getSupportActionBar();
		mActionBar.setTitle("返回");
		mActionBar.setDisplayHomeAsUpEnabled(true);
		bt_getcaptcha = (TextView) findViewById(R.id.bt_resetpass_getcaptcha);
		bt_sure = (Button) findViewById(R.id.bt_resetpass_sure);
		et_phone = (EditText) findViewById(R.id.et_resetpass_phone);
		et_captcha = (EditText) findViewById(R.id.et_resetpass_captcha);
		et_newpassword = (EditText) findViewById(R.id.et_resetpass_newpass);
		bt_getcaptcha.setOnClickListener(onClickListener);
		bt_sure.setOnClickListener(onClickListener);
		asyncHttpClient = ((BaseApplication) getApplication()).getAsyncHttpClient();

	}
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("设置密码界面");
		//友盟用户活跃统计
		MobclickAgent.onResume(this);
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("设置密码界面");
		//友盟用户活跃统计
		MobclickAgent.onPause(this);
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if(item.getItemId()==android.R.id.home){
			finish();
		}
		return super.onOptionsItemSelected(item);
	}
	private OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.bt_resetpass_getcaptcha:
				sendphone = et_phone.getText().toString();
				if (StringUtils.isEmpty(sendphone)) {
					ToastUtils.show(getApplicationContext(), "号码为空");
					return;
				}
				if (!isMobileNO(sendphone)) {
					ToastUtils.show(getApplicationContext(), "手机号格式不正确");
					return;
				}
				RequestParams requestParams = new RequestParams();
				requestParams.put("tel", sendphone);
				asyncHttpClient.get(AppConstant.URL_BASE + AppConstant.URL_GETCAPTCHA, requestParams, httpgetcaptch);
				new MyTimer().begin(25, new TimerProgress() {

					@Override
					public void onStart() {
						// TODO Auto-generated method stub
						bt_getcaptcha.setTextColor(Color.BLUE);
						bt_getcaptcha.setEnabled(false);
						et_phone.setEnabled(false);
					}

					@Override
					public void onProgress(int toals, int remaining) {
						// TODO Auto-generated method stub
						bt_getcaptcha.setText(remaining + " s");
					}

					@Override
					public void onFinish() {
						// TODO Auto-generated method stub
						// bt_getcaptcha.setTextColor(Color.parseColor("#333333"));
						bt_getcaptcha.setTextColor(Color.BLACK);
						bt_getcaptcha.setEnabled(true);
						et_phone.setEnabled(true);
						bt_getcaptcha.setText("重新获取");
					}
				});
				break;
			case R.id.bt_resetpass_sure:
				captcha = et_captcha.getText().toString();
				nespassword = et_newpassword.getText().toString();
				if (StringUtils.isEmpty(captcha)) {
					ToastUtils.show(getApplicationContext(), "验证码为空");
					return;
				}
				if (StringUtils.isEmpty(nespassword)) {
					ToastUtils.show(getApplicationContext(), "密码为空");
					return;
				}
				if (StringUtils.length(nespassword) < 6) {
					ToastUtils.show(getApplicationContext(), "密码太弱");
					return;
				}
				RequestParams requestParams1 = new RequestParams();
				requestParams1.put("password", nespassword);
				requestParams1.put("authcode", captcha);
				asyncHttpClient.post(AppConstant.URL_BASE + AppConstant.URL_RESETPASSWORD, requestParams1, httpresetpass);
				break;
			default:
				break;
			}
		}
	};

	private TextHttpResponseHandler httpgetcaptch = new TextHttpResponseHandler("UTF-8") {

		@Override
		public void onSuccess(int arg0, Header[] arg1, String arg2) {
			// TODO Auto-generated method stub
			if (arg0 == 200) {
				Type type = new TypeToken<TJResponse<Object>>() {
				}.getType();
				TJResponse<Object> response = new Gson().fromJson(arg2, type);
				if (response.getResult().getCode() == 0) {
					//ToastUtils.show(getApplicationContext(), "获取成功");
					
				} else {
					//ToastUtils.show(getApplicationContext(), "获取失败:" + response.getResult().getMessage());
				}
			} else {
				//ToastUtils.show(getApplicationContext(), "获取失败:" + arg0);
			}
		}

		@Override
		public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
			// TODO Auto-generated method stub

		}
	};
	private TextHttpResponseHandler httpresetpass = new TextHttpResponseHandler("UTf-8") {

		@Override
		public void onSuccess(int arg0, Header[] arg1, String arg2) {
			// TODO Auto-generated method stub
			if (arg0 == 200) {
				Type type = new TypeToken<TJResponse<Object>>() {
				}.getType();
				TJResponse<Object> response = new Gson().fromJson(arg2, type);
				if (response.getResult().getCode() == 0) {
					ToastUtils.show(getApplicationContext(), "修改成功");
					ResetPassActivity.this.finish();
					
				} else {
					ToastUtils.show(getApplicationContext(), "修改失败：" + response.getResult().getMessage());
				}
			} else {
				ToastUtils.show(getApplicationContext(), "修改失败：" + arg0);
			}
		}

		@Override
		public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
			// TODO Auto-generated method stub
			ToastUtils.show(getApplicationContext(), "修改失败：--" + arg3.toString());
		}
	};

	public static boolean isMobileNO(String mobiles) {
		if (StringUtils.isEmpty(mobiles)) {
			return false;
		}
		Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}
}
