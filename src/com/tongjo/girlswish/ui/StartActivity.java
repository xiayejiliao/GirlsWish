package com.tongjo.girlswish.ui;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;
import com.tongjo.girlswish.R;
import com.tongjo.girlswish.utils.AppConstant;
import com.tongjo.girlswish.utils.MyTimer;
import com.tongjo.girlswish.utils.MyTimer.TimerProgress;
import com.tongjo.girlswish.utils.SpUtils;
import com.tongjo.girlswish.utils.TimeUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class StartActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);
		int loginstate = (Integer) SpUtils.get(getApplicationContext(), AppConstant.USER_LOGINSTATE, 0);
		if (loginstate == 0) {
			new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						Thread.currentThread().sleep(2000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					startActivity(new Intent(StartActivity.this, LoginActivity.class));
					StartActivity.this.finish();
				}
			}).start();
		} else if (loginstate == 1) {
			String hxid = (String) SpUtils.get(getApplicationContext(), AppConstant.USER_HXID, "");
			String hxpassword = (String) SpUtils.get(getApplicationContext(), AppConstant.USER_HXPASSWORD, "");
			loginEMChat(hxid, hxpassword);
			new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						Thread.currentThread().sleep(2000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					startActivity(new Intent(StartActivity.this, MainActivity.class));
					StartActivity.this.finish();
				}
			}).start();
		}
	}

	private void loginEMChat(String username, String password) {
		EMChatManager.getInstance().login(username, password, new EMCallBack() {// 回调
					@Override
					public void onSuccess() {
						runOnUiThread(new Runnable() {
							public void run() {
								EMGroupManager.getInstance().loadAllGroups();
								EMChatManager.getInstance().loadAllConversations();
								Log.d("main", "登陆聊天服务器成功！");
							}
						});
					}

					@Override
					public void onProgress(int progress, String status) {

					}

					@Override
					public void onError(int code, String message) {
						Log.d("main", "登陆聊天服务器失败！");
					}
				});
	}
}
