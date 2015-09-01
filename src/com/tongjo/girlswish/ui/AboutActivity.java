package com.tongjo.girlswish.ui;

import u.aly.bt;

import com.tongjo.girlswish.R;
import com.tongjo.girlswish.widget.ButtonWithArrow;
import com.umeng.analytics.MobclickAgent;

import android.R.anim;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;

public class AboutActivity extends AppCompatActivity implements OnClickListener {
	private ButtonWithArrow btfun;
	private ButtonWithArrow btterms;
	private ActionBar mActionBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		mActionBar=getSupportActionBar();
		mActionBar.setDisplayHomeAsUpEnabled(true);
		mActionBar.setTitle("关于");
		
		btfun = (ButtonWithArrow) findViewById(R.id.bt_about_fun);
		btterms = (ButtonWithArrow) findViewById(R.id.bt_about_term);
		btfun.setOnClickListener(this);
		btterms.setOnClickListener(this);

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
		Intent intent = new Intent(AboutActivity.this, WebviewActivity.class);
		switch (v.getId()) {
		case R.id.bt_about_fun:
			intent.putExtra(WebviewActivity.WEBURL, "http://www.wishes520.com/about.html");
			intent.putExtra(WebviewActivity.TITLE, "功能介绍");
			startActivity(intent);
			break;
		case R.id.bt_about_term:
			intent.putExtra(WebviewActivity.WEBURL, "http://www.wishes520.com/xieyi.html");
			intent.putExtra(WebviewActivity.TITLE, "服务条款");
			startActivity(intent);
			break;

		default:
			break;
		}
	}

}
