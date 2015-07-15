package com.tongjo.girlswish.ui;
import android.os.Bundle;

import com.tongjo.girlswish.R;
import com.tongjo.girlswish.ui.BaseActivity;

/**
 * 男生完成的心愿信息界面
 * @author 16ren
 *
 */
public class BoyCompleteWishActivity extends BaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_boycompletewish);
		setCenterText("完成的心愿");
	}
}
