package com.tongjo.girlswish.ui;

import com.tongjo.girlswish.R;
import com.tongjo.girlswish.utils.ToastUtils;
import com.tongjo.girlswish.widget.ButtonWithArrow;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class SettingActivity extends BaseActivity implements OnClickListener {
	private ButtonWithArrow bt_resetpass;
	private ButtonWithArrow bt_about;
	private Button bt_exit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		bt_resetpass = (ButtonWithArrow) findViewById(R.id.bt_setting_resetpass);
		bt_about = (ButtonWithArrow) findViewById(R.id.bt_setting_about);
		bt_exit = (Button) findViewById(R.id.bt_setting_exit);
		bt_resetpass.setOnClickListener(this);
		bt_about.setOnClickListener(this);
		bt_exit.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bt_setting_resetpass:
			startActivity(new Intent(SettingActivity.this, ResetPassActivity.class));
			break;
		case R.id.bt_setting_about:
			ToastUtils.show(getApplicationContext(), "about");
			break;
			
		case R.id.bt_setting_exit:
			android.os.Process.killProcess(android.os.Process.myPid()) ;
			//System.exit(0);
			break;
			
		default:
			break;
		}
	}
}
