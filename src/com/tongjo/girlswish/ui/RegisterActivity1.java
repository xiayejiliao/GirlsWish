package com.tongjo.girlswish.ui;


import org.apache.http.Header;

import com.loopj.android.http.TextHttpResponseHandler;
import com.tongjo.girlswish.R;
import com.tongjo.girlswish.utils.MyTimer;
import com.tongjo.girlswish.utils.MyTimer.TimerProgress;

import android.app.Activity;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register1);
		et_phone = (EditText) findViewById(R.id.et_register1_phone);
		et_password = (EditText) findViewById(R.id.et_register1_pass);
		et_captcha = (EditText) findViewById(R.id.et_register1_captcha);
		bt_getcaptcha = (Button) findViewById(R.id.bt_register1_getcaptcha);
		bt_sure = (Button) findViewById(R.id.bt_register1_sure);
		bt_getcaptcha.setOnClickListener(onClickListener);
		bt_sure.setOnClickListener(onClickListener);
		new MyTimer(30).setTimerProgress(new TimerProgress() {
			
			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProgress(int toals, int remaining) {
				// TODO Auto-generated method stub
		
			}
			
			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				
			}
		}).start();
	
	}

	private OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.bt_register1_getcaptcha:
				
				break;
			case R.id.bt_register1_sure:
				String phone = et_phone.getText().toString();
				String password = et_password.getText().toString();

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

		}

		@Override
		public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
			// TODO Auto-generated method stub

		}
	};
	private TextHttpResponseHandler httpsure = new TextHttpResponseHandler("UTF-8") {

		@Override
		public void onSuccess(int arg0, Header[] arg1, String arg2) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
			// TODO Auto-generated method stub

		}
	};
	

}
