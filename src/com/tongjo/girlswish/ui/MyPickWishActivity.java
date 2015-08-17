package com.tongjo.girlswish.ui;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.http.Header;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

import butterknife.Bind;
import butterknife.ButterKnife;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.TextHttpResponseHandler;
import com.tongjo.bean.TJResponse;
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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;

public class MyPickWishActivity extends BaseActivity {

	private final int WISHUPDATE = 5234;
	// 使用butterknife
	// eclipse 配置请看http://jakewharton.github.io/butterknife/ide-eclipse.html
	@Bind(R.id.swipe_mypickwish)
	SwipeRefreshLayout mSwipeRefreshLayout;
	@Bind(R.id.rv_mypickwish)
	RecyclerView mRecyclerView;
	
	private RecyclerView.LayoutManager mLayoutManager;

	private final Logger log = LoggerFactory.getLogger(MyPickWishActivity.class);

	private int mysex;
	private String myid;

	private TextView tv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_pick_wish);
		ButterKnife.bind(this);
		mSwipeRefreshLayout.setOnRefreshListener(mOnRefreshListener);
		mSwipeRefreshLayout.setColorScheme(android.R.color.holo_blue_bright, android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);
		
		mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

		myid = (String) SpUtils.get(getApplicationContext(), AppConstant.USER_ID, "");
		mysex = (Integer) SpUtils.get(getApplicationContext(), AppConstant.USER_SEX, 1);
		asyncHttpClient.get(AppConstant.URL_BASE + AppConstant.URL_WISHLIST, wishListResponse);
		
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

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if(msg.what==WISHUPDATE){
				List<TJWish> list=(List<TJWish>) msg.obj;
				mRecyclerView.setAdapter(new MyPickWishAdapter(MyPickWishActivity.this, list));
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
			if (arg0 == 200) {
				Type type = new TypeToken<TJResponse<Wishs>>() {
				}.getType();
				TJResponse<Wishs> wishs = new Gson().fromJson(arg2, type);
				if (wishs.getResult().getCode() == 0) {
					List<TJWish> list = wishs.getData().getWishList();
					if (list == null) {
						return;
					}
					if (list.size() == 0) {
						return;
					}

					/*for (Iterator iterator = list.iterator(); iterator.hasNext();) {
						TJWish tjWish = (TJWish) iterator.next();
						if (!tjWish.getPickerUser().get_id().toString().endsWith(myid)) {
							iterator.remove();
						}
					}*/
					handler.obtainMessage(WISHUPDATE, list).sendToTarget();

				} else {
					ToastUtils.show(MyPickWishActivity.this, wishs.getResult().getCode() + ":" + wishs.getResult().getMessage());
				}
			} else {
				ToastUtils.show(MyPickWishActivity.this, arg0);
			}
		}

		@Override
		public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
			ToastUtils.show(MyPickWishActivity.this, arg3.toString());
		}
	};

}
