package com.tongjo.girlswish.ui;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.apache.http.Header;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

import butterknife.Bind;
import butterknife.ButterKnife;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.tongjo.bean.TJResponse;
import com.tongjo.bean.TJSchool;
import com.tongjo.bean.TJUserInfo;
import com.tongjo.bean.TJWish;
import com.tongjo.bean.TJWishList;
import com.tongjo.girlswish.R;
import com.tongjo.girlswish.ui.MainTabMeFragment.Wishs;
import com.tongjo.girlswish.utils.AppConstant;
import com.tongjo.girlswish.utils.SpUtils;
import com.tongjo.girlswish.utils.ToastUtils;
import com.tongjo.girlwish.data.DataContainer;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MyPushWishActivity extends BaseActivity {

	private final int WISHUPDATE = 5234;
	// 使用butterknife
	// eclipse 配置请看http://jakewharton.github.io/butterknife/ide-eclipse.html

	private SwipeRefreshLayout mSwipeRefreshLayout;

	private RecyclerView mRecyclerView;

	private RecyclerView.LayoutManager mLayoutManager;

	private final Logger log = LoggerFactory.getLogger(MyPickWishActivity.class);

	private int mysex;
	private String myid;

	private TextView tv;

	private ActionBar mActionBar;
	
	private TextView tv_info;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_push_wish);
		
		mSwipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.swipe_mypushwish);
		mRecyclerView=(RecyclerView)findViewById(R.id.rv_mypushwish);
		tv_info=(TextView)findViewById(R.id.tv_mypushwish_info);
		
		mActionBar = getSupportActionBar();
		mActionBar.setTitle("许下的心愿");
		mActionBar.setDisplayHomeAsUpEnabled(true);

		mSwipeRefreshLayout.setOnRefreshListener(mOnRefreshListener);
		mSwipeRefreshLayout.setColorScheme(android.R.color.holo_blue_bright, android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);
		mSwipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				updatepickwish();
			}
		});
		mLayoutManager = new LinearLayoutManager(this);
		mRecyclerView.setLayoutManager(mLayoutManager);

		myid = (String) SpUtils.get(getApplicationContext(), AppConstant.USER_ID, "");
		mysex = (Integer) SpUtils.get(getApplicationContext(), AppConstant.USER_SEX, 1);

		updatepickwish();
	}

	private void updatepickwish() {

		RequestParams requestParams = new RequestParams();
		requestParams.put("type", "created");
		asyncHttpClient.get(AppConstant.URL_BASE + AppConstant.URL_WISHLIST, requestParams, wishListResponse);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onResume(this);
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);
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

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if (msg.what == WISHUPDATE) {
				tv_info.setVisibility(View.GONE);
				List<TJWish> list = (List<TJWish>) msg.obj;
				mRecyclerView.setAdapter(new MyPushWishAdapter(MyPushWishActivity.this, list));
			}
		}

	};
	private OnRefreshListener mOnRefreshListener = new OnRefreshListener() {

		@Override
		public void onRefresh() {
			// TODO Auto-generated method stub

		}
	};

	private TextHttpResponseHandler wishListResponse = new TextHttpResponseHandler("UTF-8") {

		@Override
		public void onSuccess(int arg0, Header[] arg1, String arg2) {
			mSwipeRefreshLayout.setRefreshing(false);
			if (arg0 == 200) {
				Type type = new TypeToken<TJResponse<Wishs>>() {
				}.getType();
				TJResponse<Wishs> wishs = new Gson().fromJson(arg2, type);
				if (wishs.getResult().getCode() == 0) {
					List<TJWish> list = wishs.getData().getWishList();
					
				/*	TJSchool tjSchool = new TJSchool();
					tjSchool.setName("同济");
					tjSchool.setCoordinates("sfdsf");
					TJUserInfo tjUserInfo = new TJUserInfo();
					tjUserInfo.setAvatarUrl("http://ww2.sinaimg.cn/large/7295a660gw1e2kyveifmbj.jpg");
					tjUserInfo.set_id(UUID.fromString(myid));
					tjUserInfo.setGender(1);
					tjUserInfo.setNickname("张三");
					tjUserInfo.setTel("13262221362");
					tjUserInfo.setSchool(tjSchool);
					for (int i = 0; i < 5; i++) {
						TJWish tjWish = new TJWish();
						tjWish.setContent("fdkjsfkdjfkdsjf");
						tjWish.setPickerUser(tjUserInfo);
						tjWish.setCreatorUser(tjUserInfo);
						list.add(tjWish);
					}*/
					if (list == null) {
						tv_info.setVisibility(View.VISIBLE);
						tv_info.setText("没有发表心愿");
						return;
					}
					if (list.size() == 0) {

						tv_info.setVisibility(View.VISIBLE);
						tv_info.setText("没有发表心愿");
						return;
					}
					handler.obtainMessage(WISHUPDATE, list).sendToTarget();

				} else {
					ToastUtils.show(MyPushWishActivity.this, wishs.getResult().getCode() + ":" + wishs.getResult().getMessage());
				}
			} else {
				ToastUtils.show(MyPushWishActivity.this, arg0);
			}
		}

		@Override
		public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
			ToastUtils.show(MyPushWishActivity.this, arg3.toString());
			mSwipeRefreshLayout.setRefreshing(false);
		}
	};

}
