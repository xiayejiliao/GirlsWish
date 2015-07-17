package com.tongjo.girlswish.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Handler.Callback;
import android.util.Log;

/**
 * 短息发送工具
 * 
 * @author 16ren
 *
 */
public class SMSHelper extends Thread {
	private final String TAG = "SMSHelper";
	private final int WHAT = 3265;
	private SendResult sendResult;
	private Handler handler;
	private String verify;
	private String phone;

	public SMSHelper() {
		super();
		handler = new Handler(Looper.myLooper(), new Callback() {

			@Override
			public boolean handleMessage(Message msg) {
				if (msg.what == WHAT) {
					String result = (String) msg.obj;
					String[] temp = result.split(",");
					Log.d(TAG, result);
					if (sendResult != null) {
						sendResult.onResult(Integer.valueOf(temp[0]), temp[1], temp[5], Integer.valueOf(temp[2]), Integer.valueOf(temp[3]));
					}
				}
				return false;
			}
		});
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			String result = sendmesg(verify, phone);
			handler.obtainMessage(WHAT, result).sendToTarget();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			String result = "1324,0,1,0,0," + e.toString();
			handler.obtainMessage(WHAT, result).sendToTarget();
			e.printStackTrace();
		}
		super.run();
	}

	public void send(String verify, String phone, SendResult sendResult) {
		this.verify = verify;
		this.phone = phone;
		this.sendResult = sendResult;
		this.start();
	};

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
		URL url = new URL(sb.toString());
		Log.d(TAG, sb.toString());
		// 打开url连接
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();

		// 设置url请求方式 ‘get’ 或者 ‘post’
		connection.setRequestMethod("GET");

		// 发送
		InputStream is = url.openStream();
		// 返回结果为‘0，20140009090990,1，提交成功’ 发送成功 具体见说明文档
		String returnStr = convertStreamToString(is);

		return returnStr;
	}

	private String convertStreamToString(InputStream is) {
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

	public interface SendResult {
		public void onResult(int state, String serial, String mesg, int invalid, int valid);
	}
}
