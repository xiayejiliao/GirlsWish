package com.tongjo.girlswish.ui;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.apache.http.Header;

import com.loopj.android.http.TextHttpResponseHandler;
import com.tongjo.girlswish.R;
import com.tongjo.girlswish.ui.TakePicturePopup.onChoiced;
import com.tongjo.girlswish.utils.RandomUtils;
import com.tongjo.girlswish.utils.StringUtils;
import com.tongjo.girlswish.utils.ToastUtils;

import android.R.bool;
import android.R.integer;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

/**
 * 
 * @author 16ren 更改密码时，获取手机验证码，验证手机
 */
public class ResetPassActivity1 extends BaseActivity {
	private final static String TAG = "ResetPassActivity1";
	public final static int MESSAGE_TIMERUNING = 100;
	public final static int MESSAGE_TIMESTOP = 200;
	private Button bt_getcaptcha;
	private Button bt_sure;
	private EditText et_phone;
	private EditText et_captcha;
	private ImageView iv_logo;
	private String captcha;
	private TimeRun timeRun;
	private boolean istimerun;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_resetpass1);
		bt_getcaptcha = (Button) findViewById(R.id.bt_resetpass1_getcaptcha);
		bt_sure = (Button) findViewById(R.id.bt_resetpass1_sure);
		et_phone = (EditText) findViewById(R.id.et_resetpass1_phone);
		et_captcha = (EditText) findViewById(R.id.et_resetpass1_captcha);
		iv_logo = (ImageView) findViewById(R.id.iv_resetpass1_logo);
		bt_getcaptcha.setOnClickListener(onClickListener);
		bt_sure.setOnClickListener(onClickListener);

	}

	private OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.bt_resetpass1_getcaptcha:
				final String phone = et_phone.getText().toString();
				if (StringUtils.isEmpty(phone)) {
					ToastUtils.show(getApplicationContext(), "号码为空");
					return;
				}
				if (phone.length() != 11) {
					ToastUtils.show(getApplicationContext(), "手机号长度不正确");
					return;
				}

				int random = RandomUtils.getRandom(1000, 9999);
				captcha = String.valueOf(random);
			
				timeRun = new TimeRun();
				handler.postDelayed(timeRun, 1000);
				istimerun = true;

				break;
			case R.id.bt_resetpass1_sure:
				String temp = et_captcha.getText().toString();
				if (StringUtils.isEmpty(temp)) {
					ToastUtils.show(getApplicationContext(), "验证码为空");
				}
				if (captcha.equals(temp)) {
					Intent intent = new Intent(ResetPassActivity1.this, ResetPassActivity2.class);
					startActivity(intent);
					if (istimerun == true) {
						handler.removeCallbacks(timeRun);
						handler.obtainMessage(MESSAGE_TIMESTOP).sendToTarget();
						;
					}
					ResetPassActivity1.this.finish();

				} else {
					ToastUtils.show(getApplicationContext(), "验证码错误");
				}
				break;
			default:
				break;
			}
		}
	};

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case MESSAGE_TIMERUNING:
				bt_getcaptcha.setClickable(false);
				bt_getcaptcha.setText(msg.obj + "后重新获取");
				handler.postDelayed(timeRun, 1000);
				break;
			case MESSAGE_TIMESTOP:
				istimerun = false;
				handler.removeCallbacks(timeRun);
				bt_getcaptcha.setClickable(true);
				bt_getcaptcha.setText("获取验证码");
				break;

			default:
				break;
			}
		}

	};

	private class TimeRun implements Runnable {
	
		private int times = 20;
		@Override
		public void run() {
			Log.d(TAG, "times:" + times);
			times--;
			if (times == 0) {
				handler.obtainMessage(MESSAGE_TIMESTOP).sendToTarget();
			} else {
				handler.obtainMessage(MESSAGE_TIMERUNING, "" + times).sendToTarget();
			}
		}
		
	}

	private TextHttpResponseHandler httpgetcaptch = new TextHttpResponseHandler("UTF-8") {

		@Override
		public void onSuccess(int arg0, Header[] arg1, String arg2) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
			// TODO Auto-generated method stub

		}
	};
	private TextHttpResponseHandler httpconfirm = new TextHttpResponseHandler("UTf-8") {

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
