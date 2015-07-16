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
				new MesgSendThread(phone, captcha).start();
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

	class MesgSendThread extends Thread {
		private String phone;
		private String captcha;

		public MesgSendThread(String phone, String captcha) {
			super();
			this.phone = phone;
			this.captcha = captcha;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				sendmesg(captcha, phone);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			super.run();
		}
	}

	private String sendmesg(String verify, String phone) throws Exception {
		String content = "您的验证码是:" + verify;
		String sign = "竹舟科技";

		// 创建StringBuffer对象用来操作字符串
		StringBuffer sb = new StringBuffer("http://sms.1xinxi.cn/asmx/smsservice.aspx?");

		// 向StringBuffer追加用户名
		sb.append("name=18905195926");
		// 向StringBuffer追加密码（登陆网页版，在管理中心--基本资料--接口密码，是28位的）
		sb.append("&pwd=1AB4497354CF02C92929E714FBDB");

		// 向StringBuffer追加手机号码
		sb.append("&mobile=" + phone);

		// 向StringBuffer追加消息内容转URL标准码
		sb.append("&content=" + URLEncoder.encode(content, "UTF-8"));

		// 追加发送时间，可为空，为空为及时发送
		sb.append("&stime=");

		// 加签名
		sb.append("&sign=" + URLEncoder.encode(sign, "UTF-8"));

		// type为固定值pt extno为扩展码，必须为数字 可为空
		sb.append("&type=pt&extno=");
		// 创建url对象
		// String temp = new String(sb.toString().getBytes("GBK"),"UTF-8");
		System.out.println("sb:" + sb.toString());
		URL url = new URL(sb.toString());

		// 打开url连接
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();

		// 设置url请求方式 ‘get’ 或者 ‘post’
		connection.setRequestMethod("GET");

		// 发送
		InputStream is = url.openStream();

		String returnStr = convertStreamToString(is);

		// 返回结果为‘0，20140009090990,1，提交成功’ 发送成功 具体见说明文档
		return returnStr;
	}

	public static String convertStreamToString(InputStream is) {
		StringBuilder sb1 = new StringBuilder();
		byte[] bytes = new byte[4096];
		int size = 0;

		try {
			while ((size = is.read(bytes)) > 0) {
				String str = new String(bytes, 0, size, "UTF-8");
				sb1.append(str);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb1.toString();
	}

}
