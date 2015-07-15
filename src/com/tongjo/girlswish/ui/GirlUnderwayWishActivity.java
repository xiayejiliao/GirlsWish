package com.tongjo.girlswish.ui;

import com.tongjo.girlswish.R;

import android.os.Bundle;
/**
 * 女生正在进行中心愿的界面
 * @author 16ren
 *
 */
public class GirlUnderwayWishActivity extends BaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_grilunderwaywish);
		setCenterText("正在进行的心愿");
	}
}
