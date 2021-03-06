package com.tongjo.girlswish.ui;

import java.lang.reflect.Type;

import javax.security.auth.PrivateCredentialPermission;

import org.apache.http.Header;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.tongjo.bean.TJResponse;
import com.tongjo.girlswish.BaseApplication;
import com.tongjo.girlswish.R;
import com.tongjo.girlswish.utils.AppConstant;
import com.tongjo.girlswish.utils.MyTimer;
import com.tongjo.girlswish.utils.RandomUtils;
import com.tongjo.girlswish.utils.SMSHelper;
import com.tongjo.girlswish.utils.SexUtils;
import com.tongjo.girlswish.utils.StringUtils;
import com.tongjo.girlswish.utils.ToastUtils;
import com.tongjo.girlswish.utils.MyTimer.TimerProgress;
import com.tongjo.girlswish.utils.SMSHelper.SMSTYPE;
import com.tongjo.girlswish.utils.SMSHelper.SendResult;
import com.tongjo.girlswish.widget.LinkTextView;
import com.umeng.analytics.MobclickAgent;

import android.R.integer;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

/**
 * 1.注册，接收短信验证码验证手机 2.提交注册账号、密码 3.跳转到个人信息完善界面RegisterActivity2
 * 
 * @author 16ren
 * @data 2015/7/17
 */
public class RegisterActivity extends AppCompatActivity {
	private final static String TAG = "RegisterActivity1";
	private EditText et_phone;
	private EditText et_password;
	private EditText et_captcha;
	private TextView bt_getcaptcha;
	private Button bt_sure;
	private String sendphone;
	private AsyncHttpClient asyncHttpClient;
	private ProgressDialog progressdialog;
	private ActionBar mActionBar;

	private String random;
	private RadioGroup mRadioGroup;
	private int sex;
	private boolean issexchoose=false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		mActionBar = getSupportActionBar();
		mActionBar.setTitle("登录");
		mActionBar.setDisplayHomeAsUpEnabled(true);
		et_phone = (EditText) findViewById(R.id.et_register_phone);
		et_password = (EditText) findViewById(R.id.et_register_pass);
		et_captcha = (EditText) findViewById(R.id.et_register_captcha);
		bt_getcaptcha = (TextView) findViewById(R.id.bt_register_getcaptcha);
		bt_sure = (Button) findViewById(R.id.bt_register_sure);
		mRadioGroup=(RadioGroup)findViewById(R.id.rg_register_sex);
		bt_getcaptcha.setOnClickListener(onClickListener);
		bt_sure.setOnClickListener(onClickListener);
		
		mRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				issexchoose=true;
				if(checkedId==R.id.rb_register_men){
					sex=1;
				}
				if(checkedId==R.id.rb_register_women){
					sex=0;
				}
			}
		});

		
		asyncHttpClient = ((BaseApplication) getApplication()).getAsyncHttpClient();
		
		

	}
	@Override
	public void onResume() {
		super.onResume();
		//友盟页面统计
		MobclickAgent.onPageStart("注册页面");
		//友盟用户活跃统计
		MobclickAgent.onResume(this);
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("注册页面");
		//友盟用户活跃统计
		MobclickAgent.onPause(this);
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		startActivity(new Intent(this, LoginActivity.class));
		super.onBackPressed();
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			startActivity(new Intent(this, LoginActivity.class));
			finish();
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.bt_register_getcaptcha:
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
				asyncHttpClient.get(AppConstant.URL_BASE + AppConstant.URL_GETCAPTCHA, requestParams, httpgetcaptcha);
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
			case R.id.bt_register_sure:
				String captcha = et_captcha.getText().toString();
				String password = et_password.getText().toString();
				if (!issexchoose) {
					ToastUtils.show(getApplicationContext(), "请选择性别");
					return;
				}
				if (StringUtils.isEmpty(password)) {
					ToastUtils.show(getApplicationContext(), "密码空");
					return;
				}
				if (password.length() < 6) {
					ToastUtils.show(getApplicationContext(), "密码太弱");
					return;
				}
				if (StringUtils.isEmpty(captcha)) {
					ToastUtils.show(getApplicationContext(), "验证码空");
					return;
				}
				RequestParams requestParams1 = new RequestParams();
				requestParams1.put("tel", sendphone);
				requestParams1.put("password", password);
				requestParams1.put("authcode", captcha);
				requestParams1.put("gender", sex);
				progressdialog = ProgressDialog.show(RegisterActivity.this, "提示", "注册中", false, false);
				asyncHttpClient.post(AppConstant.URL_BASE + AppConstant.URL_REGISTER, requestParams1, httpregister);
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
			ToastUtils.show(getApplicationContext(), "获取失败:" + arg3.toString());
		}
	};
	private TextHttpResponseHandler httpregister = new TextHttpResponseHandler("UTF-8") {

		@Override
		public void onSuccess(int arg0, Header[] arg1, String arg2) {
			progressdialog.dismiss();
			if (arg0 == 200) {
				Type type = new TypeToken<TJResponse<Object>>() {
				}.getType();
				TJResponse<Object> response = new Gson().fromJson(arg2, type);
				if (response.getResult().getCode() == 0) {
					Intent intent = new Intent(RegisterActivity.this, MyInfoEditActivity.class);
					intent.putExtra("sex", sex);
					startActivity(intent);
					RegisterActivity.this.finish();
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
			progressdialog.dismiss();
			ToastUtils.show(getApplicationContext(), "注册失败：--" + arg3.toString());
		}
	};

	/**
	 * 验证手机格式
	 */
	private static boolean isMobileNO(String mobiles) {
		/*
		 * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
		 * 联通：130、131、132、152、155、156、185、186 电信：133、153、180、189、（1349卫通）
		 * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
		 */
		String telRegex = "[1][358]\\d{9}";// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
		if (TextUtils.isEmpty(mobiles))
			return false;
		else
			return mobiles.matches(telRegex);
	}

}
