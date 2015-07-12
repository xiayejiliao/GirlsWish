package com.tongjo.girlswish.ui;

import java.lang.reflect.Field;
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;

import com.tongjo.girlswish.R;

/**
 * 应用程序主界面，含有多个fragment
 * 
 * 目前主要是fragment加载以及底下tab栏的切换
 * 
 * 后续应该会有其他功能加入
 * 
 * Copyright 2015
 * @author preparing
 * @date 2015-6-14
 */
public class MainTabActivity extends BaseFragmentActivity {

	private final String TAG = "MainTabActivity";

	private final int count = 3;
	private SectionsPagerAdapter mSectionsPagerAdapter;
	private ViewPager mViewPager;

	// 三个Tab 每个Tab都是一个单独的Layout
	private RelativeLayout section1;
	private RelativeLayout section2;
	private RelativeLayout section3;

	// Tab上的文字，主要是需要改变字体的颜色
	private TextView textView1;
	private TextView textView2;
	private TextView textView3;

	// tab上的图片，主要是需要改变图片的颜色
	private ImageView imageView1;
	private ImageView imageView2;
	private ImageView imageView3;

	// 标题上的通知文字
	private TextView alertView1;
	private TextView alertView2;
	private TextView alertView3;

	// 定义颜色变化的起始值和结束值
	private final int startColor = 0xffffff;
	private final int endColor = 0x00bb00;

	// 用于区分Scroll类别
	private static boolean isClick = false;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_maintab);

		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());
		mViewPager = (ViewPager) findViewById(R.id.viewpager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
		mViewPager.setOnPageChangeListener(new MyOnPageChangeListener());
		// 设置缓存界面个数，防止反复Create
		mViewPager.setOffscreenPageLimit(4);

		InitTitleBar();
		
		InitView();

		setViewPagerScrollSpeed();
		isClick = true;
		mViewPager.setCurrentItem(0);
		setSelectTextColor(0);
		setSelectImage(0);
	}

	public void InitTitleBar(){
		hideBackBtn();
		setRightBtn(R.drawable.add);
		getRightBtnImageView().setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),AddWishActivity.class);
				startActivity(intent);
			}
			
		});
	}
	
	/** 加载基本的View文件 */
	private void InitView() {
		section1 = (RelativeLayout) findViewById(R.id.viewpage_title_section1);
		section1.setOnClickListener(new MyOnClickListener(1));
		section2 = (RelativeLayout) findViewById(R.id.viewpage_title_section2);
		section2.setOnClickListener(new MyOnClickListener(2));
		section3 = (RelativeLayout) findViewById(R.id.viewpage_title_section3);
		section3.setOnClickListener(new MyOnClickListener(3));

		textView1 = (TextView) findViewById(R.id.viewpage_title_text1);
		textView2 = (TextView) findViewById(R.id.viewpage_title_text2);
		textView3 = (TextView) findViewById(R.id.viewpage_title_text3);

		imageView1 = (ImageView) findViewById(R.id.viewpage_title_img1);
		imageView2 = (ImageView) findViewById(R.id.viewpage_title_img2);
		imageView3 = (ImageView) findViewById(R.id.viewpage_title_img3);

		alertView1 = (TextView) findViewById(R.id.viewpage_title_alert1);
		alertView2 = (TextView) findViewById(R.id.viewpage_title_alert2);
		alertView3 = (TextView) findViewById(R.id.viewpage_title_alert3);
	}

	/** 设置当前显示的文字颜色 ,文字选中的颜色和底部游标的颜色是一致的 */
	public void setSelectTextColor(int tab) {
		switch (tab) {
		case 0:
			textView1.setTextColor(getResources().getColor(R.color.lgreen));
			textView2.setTextColor(getResources().getColor(R.color.white));
			textView3.setTextColor(getResources().getColor(R.color.white));
			break;
		case 1:
			textView1.setTextColor(getResources().getColor(R.color.white));
			textView2.setTextColor(getResources().getColor(R.color.lgreen));
			textView3.setTextColor(getResources().getColor(R.color.white));
			break;
		case 2:
			textView1.setTextColor(getResources().getColor(R.color.white));
			textView2.setTextColor(getResources().getColor(R.color.white));
			textView3.setTextColor(getResources().getColor(R.color.lgreen));
			break;
		}
	}

	/**
	 * 设置滚动时底下文字颜色的变化过程
	 * @param oldTab
	 * @param oldColor
	 * @param newTab
	 * @param newColor
	 */
	public void setGradientText(int oldTab, int oldColor, int newTab,
			int newColor) {
		/*System.out.println(Integer.toHexString(oldColor));*/

		int oldblue = oldColor % 256;
		int oldgreen = ((int) (oldColor >> 8)) % 256;
		int oldred = ((int) (oldColor >> 16)) % 256;

		int newblue = newColor % 256;
		int newgreen = ((int) (newColor >> 8)) % 256;
		int newred = ((int) (newColor >> 16)) % 256;

		switch (oldTab) {
		case 0:
			textView1.setTextColor(Color.rgb(oldred, oldgreen, oldblue));
			break;
		case 1:
			textView2.setTextColor(Color.rgb(oldred, oldgreen, oldblue));
			break;
		case 2:
			textView3.setTextColor(Color.rgb(oldred, oldgreen, oldblue));
			break;
		}

		switch (newTab) {
		case 0:
			textView1.setTextColor(Color.rgb(newred, newgreen, newblue));
			break;
		case 1:
			textView2.setTextColor(Color.rgb(newred, newgreen, newblue));
			break;
		case 2:
			textView3.setTextColor(Color.rgb(newred, newgreen, newblue));
			break;
		}
	}

	/** 设置当前显示的图片 */
	public void setSelectImage(int tab) {
		switch (tab) {
		case 0:
			imageView1.setImageResource(R.drawable.info_p);
			imageView2.setImageResource(R.drawable.info_n);
			imageView3.setImageResource(R.drawable.info_n);
			break;
		case 1:
			imageView1.setImageResource(R.drawable.info_n);
			imageView2.setImageResource(R.drawable.info_p);
			imageView3.setImageResource(R.drawable.info_n);
			break;
		case 2:
			imageView1.setImageResource(R.drawable.info_n);
			imageView2.setImageResource(R.drawable.info_n);
			imageView3.setImageResource(R.drawable.info_p);
			break;
		}
	}

	/**
	 * 设置切换时tab图片的颜色变化
	 * 
	 * 这边还需要设计师提高一套图片才能实现
	 * 
	 * @param tab
	 */
	public void setGradientImage(int oldTab,int newTab,int percent) {
		
	}
	
	/** Tab上的Tab点击监听 */
	private class MyOnClickListener implements OnClickListener {
		private int index = 0;

		public MyOnClickListener(int i) {
			if (i > count) {
				index = count;
			} else {
				index = i;
			}
		}

		@Override
		public void onClick(View v) {
			isClick = true;
			mViewPager.setCurrentItem(index - 1);
		}

	}

	/** 在这边进行Fragment的加载 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		/** 增加一个对数组保存Fragment对象 */
		private HashMap<String, Fragment> fraList = new HashMap<String, Fragment>();

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
			if (fraList == null) {
				fraList = new HashMap<String, Fragment>();
			}
		}

		public Fragment getFragment(String key) {
			if (fraList == null) {
				return null;
			}
			Fragment fra = fraList.get(key);
			return fra;
		}

		@Override
		public Fragment getItem(int position) {
			/**
			 * bundle 用于向Fragment传递数据
			 */
			if (position == 0) {
				Fragment fragment = new MainTabInfoFragment();
				Bundle args = new Bundle();
				fragment.setArguments(args);
				fraList.put("fragment0", fragment);
				return fragment;
			} else if (position == 1) {
				Fragment fragment = new MainTabWishFragment();
				Bundle args = new Bundle();
				fragment.setArguments(args);
				fraList.put("fragment1", fragment);
				return fragment;
			} else if (position == 2) {
				Fragment fragment = new MainTabMeFragment();
				Bundle args = new Bundle();
				fragment.setArguments(args);
				fraList.put("fragment2", fragment);
				return fragment;
			}
			return null;
		}

		@Override
		public int getCount() {
			return count;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			switch (position) {
			case 0:
				return getString(R.string.title_section1).toUpperCase();
			case 1:
				return getString(R.string.title_section2).toUpperCase();
			case 2:
				return getString(R.string.title_section3).toUpperCase();
			}
			return null;
		}
	}

	/** 页面切换时的动作 */
	public class MyOnPageChangeListener implements OnPageChangeListener {
		private float currentPercentage = 0;

		public void onPageScrollStateChanged(int arg0) {
		}

		/** 三个参数分别为当前的页面，当前页面偏移百分比，当前页面偏移的像素 */
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			/*System.out.println("onPageScrolled:" + arg0 + ";" + arg1 + ";"
					+ currIndex);*/

			/** 为了避开最后一次改变后的触发，采用这种判断方式 */
			if (arg1 < 0.0001 || arg1 > 0.9999) {
				return;
			}

			/** 设置滑动时颜色的渐变效果 */
			// 往右滑动的情况
			if (arg1 > currentPercentage) {
				int oldoffset = (int) ((0xff - 0x00) * arg1);
				int newoffset = 0xff - oldoffset;

				int oldoffset2 = (int) ((0xff - 0xbb) * arg1);
				int newoffset2 = 0xff - oldoffset2;

				int oldColor = (((oldoffset << 8) + (0xbb + oldoffset2)) << 8)
						+ oldoffset;
				int newColor = (((newoffset << 8) + newoffset2) << 8)
						+ newoffset;
				/* System.out.println(Integer.toHexString(newColor)); */
				setGradientText(arg0, oldColor, arg0 + 1, newColor);
				currentPercentage = arg1;
			}
			// 往左滑动情况
			else if (currentPercentage > arg1) {
				int oldoffset = (int) ((0xff - 0x00) * (1 - arg1));
				int newoffset = 0xff - oldoffset;

				int oldoffset2 = (int) ((0xff - 0xbb) * (1 - arg1));
				int newoffset2 = 0xff - oldoffset2;

				int oldColor = (((oldoffset << 8) + (0xbb + oldoffset2)) << 8)
						+ oldoffset;
				int newColor = (((newoffset << 8) + newoffset2) << 8)
						+ newoffset;
				/* System.out.println(Integer.toHexString(oldColor)); */
				setGradientText(arg0 + 1, oldColor, arg0, newColor);
				currentPercentage = arg1;
			}
		}

		// 实测这边不会在onPageScrolled执行完成后在执行，一般80%左右这边就触发了
		public void onPageSelected(int arg0) {
			setSelectImage(arg0);
			setSelectTextColor(arg0);
		}
	}

	/**
	 * 设置ViewPager的滑动速度
	 * 
	 * */
	private void setViewPagerScrollSpeed() {
		try {
			Field mScroller = null;
			mScroller = ViewPager.class.getDeclaredField("mScroller");
			mScroller.setAccessible(true);
			FixedSpeedScroller scroller = new FixedSpeedScroller(
					mViewPager.getContext());
			mScroller.set(mViewPager, scroller);
		} catch (NoSuchFieldException e) {

		} catch (IllegalArgumentException e) {

		} catch (IllegalAccessException e) {

		}
	}

	/**
	 * 用于设置viewpager手动切换时的切换速度
	 * 这边需要分开判断，手动切换页面时应该时没有滑动效果
	 * 在屏幕上滑动切换时应该是有切换效果的
	 * 
	 *  Copyright 2015
	 * 
	 * @author preparing
	 * @date 2015-6-14
	 */
	public class FixedSpeedScroller extends Scroller {
		private int mDuration = 0;
		private int mScrollerDuration = 300;

		public FixedSpeedScroller(Context context) {
			super(context);
		}

		public FixedSpeedScroller(Context context, Interpolator interpolator) {
			super(context, interpolator);
		}

		public FixedSpeedScroller(Context context, Interpolator interpolator,
				boolean flywheel) {
			super(context, interpolator, flywheel);
		}

		@Override
		public void startScroll(int startX, int startY, int dx, int dy,
				int duration) {
			if (isClick) {
				super.startScroll(startX, startY, dx, dy, mDuration);
			} else {
				super.startScroll(startX, startY, dx, dy, mScrollerDuration);
			}

		}

		@Override
		public void startScroll(int startX, int startY, int dx, int dy) {
			if (isClick) {
				super.startScroll(startX, startY, dx, dy, mDuration);
			} else {
				super.startScroll(startX, startY, dx, dy, mScrollerDuration);
			}
		}

		/**
		 * 通过查看源代码，这边在滚动时会回调，滚动结束的标志也基本是在这边设置的
		 * 其他一些手动设置滚动结束的情况还不清楚是否会执行该函数，暂时先使用该方法
		 */
		@Override
		public boolean computeScrollOffset(){
			boolean bool = super.computeScrollOffset();
			if(isFinished()){
				isClick = false;
			}
			return bool;
		}

	}
}