package com.tongjo.girlswish.ui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import butterknife.Bind;
import butterknife.ButterKnife;




import com.tongjo.girlswish.R;
import com.tongjo.girlswish.utils.AppConstant;
import com.tongjo.girlswish.utils.SpUtils;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;

public class MyPickWishActivity extends BaseActivity {
	private  SwipeRefreshLayout mSwipeRefreshLayout;
	@Bind(R.id.rv_mypickwish) RecyclerView mRecyclerView;
	
	private final Logger log = LoggerFactory.getLogger(MyPickWishActivity.class);

	
	private int mysex;
	private String myid;
	
	private TextView tv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_pick_wish);
		ButterKnife.bind(this);
		mSwipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.swipe_mypickwish);
		mSwipeRefreshLayout.setOnRefreshListener(mOnRefreshListener);
		mSwipeRefreshLayout.setColorScheme(android.R.color.holo_blue_bright, android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);

		myid = (String) SpUtils.get(getApplicationContext(), AppConstant.USER_ID, "");
		mysex = (Integer) SpUtils.get(getApplicationContext(), AppConstant.USER_SEX, 1);
		log.info("id:{} sex{}",myid,mysex);


	}

	private OnRefreshListener mOnRefreshListener = new OnRefreshListener() {

		@Override
		public void onRefresh() {
			// TODO Auto-generated method stub

		}
	};

}
