package com.tongjo.girlswish.ui;
import android.os.Bundle;

import com.tongjo.girlswish.R;
import com.tongjo.girlswish.ui.BaseActivity;
/**
 * 男生还没有完成的心愿信息界面
 * @author dell
 *
 */

public class BoyUncompleteWishActivity extends BaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_boyuncompletewish);
		setCenterText("未完成的心愿");
	}
}
