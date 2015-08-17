package com.tongjo.girlswish.test;

import org.apache.http.Header;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.tongjo.girlswish.R;
import com.tongjo.girlswish.ui.BaseActivity;
import com.tongjo.girlswish.utils.AppConstant;
import com.tongjo.girlswish.utils.ImageUtils;

/**
 * Activity测试用例，主要是为了显示一些自定义的组件
 * 
 * Copyright 2015
 * 
 * @author preparing
 * @date 2015-6-14
 */
public class TestActivity extends BaseActivity {
	private ImageView mImageView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test);

		mImageView = (ImageView) findViewById(R.id.test_image);
		Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.testimg);
		mImageView.setImageBitmap(ImageUtils.getRoundCornerDrawable(bmp, 360));

		/*InitActionBar();*/
		
		/**
		 * 设置titlebar的相关信息 title的左边可以是一个返回按钮，可以选择要不要有对应的文字
		 * title的左边也可以是一个图片或者一个文字，文字和图片由自己定义 title的中间是一个文字
		 * title的右边可以是一个图片或者一个文字，都是可以点击的
		 * 默认只显示左边的返回按钮，点击事件是结束当前Acitivity,可以自己重写点击事件
		 * */
		setCenterText("测试页面");

		// 第一组测试，直接使用返回按钮,leftBtn和backBtn只能显示一个
		/*setBackBtnText("返回");
		setRightBtn(R.drawable.btn_setting);*/

		// 第二组测试，不显示返回按钮，改用图片显示
		/*
		 * setLeftBtn(R.drawable.btn_back); setRightBtn(R.drawable.btn_setting);
		 */

		// 第三组测试，不显示返回按钮，改用文字显示
		/*
		 * setLeftBtn("返回"); setRightBtn("设置");
		 */

		/* setBackBtnText("返回"); */
		/*setbackBtnListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				
			}
		});
		testregister();*/
	}

	public void testregister() {
		RequestParams requestParams = new RequestParams();
		requestParams.put("tel", "15564557736");
		asyncHttpClient.get(AppConstant.URL_BASE+AppConstant.URL_GETCAPTCHA, requestParams,new TextHttpResponseHandler("UTF-8") {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) {
				// TODO Auto-generated method stub
				Log.d("testregister","getcode success"+ arg2);
				if(arg0==200){
					RequestParams requestParams1 = new RequestParams();
					requestParams1.put("password", "12345678");
					requestParams1.put("authcode", "1234");
					
					asyncHttpClient.post(AppConstant.URL_BASE+AppConstant.URL_REGISTER,requestParams1, new TextHttpResponseHandler("UTF-8") {
						
						@Override
						public void onSuccess(int arg0, Header[] arg1, String arg2) {
							// TODO Auto-generated method stub
							Log.d("testregister","register success"+ arg2);
						}
						
						@Override
						public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
							// TODO Auto-generated method stub
							Log.d("testregister","register fail code:"+arg0+  arg3.toString());
						}
					});
				}
				
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				
				
				Log.d("testregister","getcode fail:code:"+arg0+  arg3.toString());
			}
		});
	
	}
}
