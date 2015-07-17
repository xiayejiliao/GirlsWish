package com.tongjo.girlswish.ui;

import java.lang.reflect.Type;

import org.apache.http.Header;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.tongjo.bean.TJResponse;
import com.tongjo.girlswish.R;
import com.tongjo.girlswish.utils.AppConstant;
import com.tongjo.girlswish.utils.MyTimer;
import com.tongjo.girlswish.utils.SMSHelper;
import com.tongjo.girlswish.utils.SexUtils;
import com.tongjo.girlswish.utils.StringUtils;
import com.tongjo.girlswish.utils.ToastUtils;
import com.tongjo.girlswish.utils.MyTimer.TimerProgress;
import com.tongjo.girlswish.utils.SMSHelper.SendResult;
import com.tongjo.girlswish.widget.LinkTextView;

import android.R.integer;
import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class RegisterActivity1 extends BaseActivity {
	private final static String TAG = "RegisterActivity1";
	private EditText et_phone;
	private EditText et_password;
	private EditText et_captcha;
	private Button bt_getcaptcha;
	private Button bt_sure;
	private String sendphone;
	private LinkTextView linkTextView;
	private int sex;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register1);
		setCenterText("注册");
		et_phone = (EditText) findViewById(R.id.et_register1_phone);
		et_password = (EditText) findViewById(R.id.et_register1_pass);
		et_captcha = (EditText) findViewById(R.id.et_register1_captcha);
		bt_getcaptcha = (Button) findViewById(R.id.bt_register1_getcaptcha);
		bt_sure = (Button) findViewById(R.id.bt_register1_sure);
		linkTextView = (LinkTextView) findViewById(R.id.ltv_register1_login);
		bt_getcaptcha.setOnClickListener(onClickListener);
		bt_sure.setOnClickListener(onClickListener);
		linkTextView.setOnClickListener(onClickListener);
		sex= getIntent().getIntExtra("sex",-1);
		System.out.println(sex);
	}

	private OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.bt_register1_getcaptcha:
				sendphone = et_phone.getText().toString();
				if (StringUtils.isEmpty(sendphone)) {
					ToastUtils.show(getApplicationContext(), "号码为空");
					return;
				}
				if (sendphone.length() != 11) {
					ToastUtils.show(getApplicationContext(), "手机号长度不正确");
					return;
				}
				RequestParams requestParams = new RequestParams();
				requestParams.put("tel", sendphone);
				asyncHttpClient.get(AppConstant.URL_BASE + AppConstant.URL_GETCAPTCHA, requestParams, httpgetcaptcha);
				break;
			case R.id.bt_register1_sure:
				String captcha = et_captcha.getText().toString();
				String password = et_password.getText().toString();
				if (StringUtils.isEmpty(password)) {
					ToastUtils.show(getApplicationContext(), "密码空");
					return;
				}
				if (password.length() < 6) {
					ToastUtils.show(getApplicationContext(), "密码太弱");
					return;
				}
				RequestParams requestParams1 = new RequestParams();
				requestParams1.put("password", password);
				requestParams1.put("authcode", captcha);
				asyncHttpClient.post(AppConstant.URL_BASE + AppConstant.URL_REGISTER, requestParams1, httpregister);
				break;
			case R.id.ltv_register1_login:
				Intent intent = new Intent(RegisterActivity1.this, LoginActivity.class);
				startActivity(intent);
				RegisterActivity1.this.finish();
				break;
			default:
				break;
			}
		}
	};

	private TextHttpResponseHandler httpgetcaptcha = new TextHttpResponseHandler("UTF-8") {

		@Override
		public void onSuccess(int arg0, Header[] arg1, String arg2) {
			// TODO Auto-generated method stub
			if (arg0 == 200) {
				Type type = new TypeToken<TJResponse<Object>>() {
				}.getType();
				TJResponse<Object> response = new Gson().fromJson(arg2, type);
				if (response.getResult().getCode() == 0) {
					new SMSHelper().send("1234", sendphone, new SendResult() {

						@Override
						public void onResult(int state, String serial, String mesg, int invalid, int valid) {
							// TODO Auto-generated method stub
							if (state == 0) {
								ToastUtils.show(getApplicationContext(), "获取成功,等待接收");
								new MyTimer().begin(25, new TimerProgress() {

									@Override
									public void onStart() {
										// TODO Auto-generated method stub
										bt_getcaptcha.setTextColor(Color.YELLOW);
										bt_getcaptcha.setEnabled(false);
									}

									@Override
									public void onProgress(int toals, int remaining) {
										// TODO Auto-generated method stub
										bt_getcaptcha.setText(remaining + "后重新获取");
									}

									@Override
									public void onFinish() {
										// TODO Auto-generated method stub
										bt_getcaptcha.setTextColor(Color.parseColor("#333333"));;
										bt_getcaptcha.setEnabled(true);
										bt_getcaptcha.setText("获取验证码");
									}
								});
							} else {
								ToastUtils.show(getApplicationContext(), "获取失败," + mesg);
							}
						}
					});
				} else {
					ToastUtils.show(getApplicationContext(), "获取失败:" + response.getResult().getMessage());
				}
			} else {
				ToastUtils.show(getApplicationContext(), "获取失败:" + arg0);
			}
		}

		@Override
		public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
			// TODO Auto-generated method stub
			ToastUtils.show(getApplicationContext(), "获取失败:" + arg3.toString());
		}
	};
	private TextHttpResponseHandler httpregister = new TextHttpResponseHandler("UTF-8") {

		@Override
		public void onSuccess(int arg0, Header[] arg1, String arg2) {
			if (arg0 == 200) {
				Type type = new TypeToken<TJResponse<Object>>() {
				}.getType();
				TJResponse<Object> response = new Gson().fromJson(arg2, type);
				if (response.getResult().getCode() == 0) {
					Intent intent = new Intent(RegisterActivity1.this, RegisterActivity2.class);
					startActivity(intent);
					RegisterActivity1.this.finish();
				} else {
					ToastUtils.show(getApplicationContext(), "注册失败：" + response.getResult().getMessage());
				}
			} else {
				ToastUtils.show(getApplicationContext(), "注册失败：" + arg0);
			}

		}

		@Override
		public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
			// TODO Auto-generated method stub

			ToastUtils.show(getApplicationContext(), "注册失败：--" + arg3.toString());
		}
	};

}
