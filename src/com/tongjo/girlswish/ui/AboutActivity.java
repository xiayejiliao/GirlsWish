package com.tongjo.girlswish.ui;

import com.tongjo.girlswish.R;
import com.umeng.analytics.MobclickAgent;

import android.R.anim;
import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;

public class AboutActivity extends AppCompatActivity {
	private ActionBar mActionBar;
	private WebView mWebView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		mActionBar = getSupportActionBar();
		mActionBar.setDisplayHomeAsUpEnabled(true);
		mActionBar.setTitle("关于");
		mWebView = (WebView) findViewById(R.id.wv_about);
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.loadUrl("file:///android_asset/about.html");
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
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("关于界面");
		MobclickAgent.onResume(this);
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("关于界面");
		MobclickAgent.onPause(this);
	}
}
