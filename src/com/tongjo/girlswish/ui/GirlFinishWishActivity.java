package com.tongjo.girlswish.ui;

import com.tongjo.girlswish.R;

import android.os.Bundle;

/**
 * 女孩已经完成心愿的界面
 * @author dell
 *
 */
public class GirlFinishWishActivity extends BaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_girlfinishwish);
		setCenterText("已完成的心愿");
	}
}
