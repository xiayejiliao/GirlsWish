package com.tongjo.girlswish.ui;

import butterknife.Bind;
import butterknife.ButterKnife;

import com.tongjo.girlswish.R;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

public class MyPickWishActivity extends BaseActivity {
	@Bind(R.id.swipe_mypickwish)
	SwipeRefreshLayout mSwipeRefreshLayout;
	@Bind(R.id.rv_mypickwish)
	RecyclerView mRecyclerView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_pick_wish);
		ButterKnife.bind(this);
	}

}
