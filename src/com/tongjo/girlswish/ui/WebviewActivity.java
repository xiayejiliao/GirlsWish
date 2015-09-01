package com.tongjo.girlswish.ui;

import com.tongjo.girlswish.R;
import com.tongjo.girlswish.utils.StringUtils;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;

/*
 * 16ren
 *传入url  显示 一个web
 * 	intent.putExtra(WebviewActivity.WEBURL, "www.baidu.com");
 */
public class WebviewActivity extends AppCompatActivity {

	public final static String WEBURL = "weburl";
	public final static String TITLE = "title";
	private ActionBar mActionBar;
	private String url;
	private String title;
	private WebView mWebView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_webview);

		// 设置返回按钮
		mActionBar = getSupportActionBar();
		mActionBar.setDisplayHomeAsUpEnabled(true);

		//获取网址和title
		url = getIntent().getStringExtra(WEBURL);
		title = getIntent().getStringExtra(TITLE);
		//设置title
		mActionBar.setTitle(title);

		//加载网页
		mWebView = (WebView) findViewById(R.id.webview_systeminfo);
		if (!StringUtils.isEmpty(url)) {
			mWebView.loadUrl(url);
		}

	}

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("web页面");
		// 友盟用户活跃统计
		MobclickAgent.onResume(this);
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("web页面");
		// 友盟用户活跃统计
		MobclickAgent.onPause(this);
	}

	public void ShowWeb(String url) {
		if (mWebView != null) {
			if (!StringUtils.isEmpty(url)) {
				mWebView.loadUrl(url);
			}
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

}
