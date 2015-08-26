package com.tongjo.girlswish.ui;

import java.lang.reflect.Field;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TabLayout.OnTabSelectedListener;
import android.support.design.widget.TabLayout.Tab;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tongjo.girlswish.R;
import com.tongjo.girlswish.event.UnReadSetEvent;
import com.tongjo.girlswish.event.UserIconChange;
import com.tongjo.girlswish.event.UserLogout;
import com.tongjo.girlswish.event.UserNicknameChange;
import com.tongjo.girlswish.event.UserSchoolnameChange;
import com.tongjo.girlswish.ui.MainTabActivity.MyOnPageChangeListener;
import com.tongjo.girlswish.utils.AppConstant;
import com.tongjo.girlswish.utils.SpUtils;
import com.tongjo.girlswish.widget.CircleImageView;
import com.umeng.analytics.MobclickAgent;

import de.greenrobot.event.EventBus;

/**
 * 
 * @Description: 主界面包括策划导航，和标签导航
 * @author 16ren
 * @date 2015年8月20日 下午5:38:10
 * 
 */
public class MainActivity extends AppCompatActivity {
	// 策划框架 v4包里面的
	private DrawerLayout mDrawer;
	// 自定义actionbar
	private Toolbar toolbar;
	private ActionBar actionBar;
	// 策划界面
	private NavigationView navigationView;
	// 同步侧滑栏和actionbar
	private ActionBarDrawerToggle actionBarDrawerToggle;

	// mViewPager导航标签
	private TabLayout mTabLayout;
	// 策划栏的头部分
	private LinearLayout drawerheader;
	// 侧滑栏 头部 我的头像
	private CircleImageView iv_icon;
	// 侧滑栏 头部 我的昵称
	private TextView tv_nick;
	// 侧滑栏 头部 我的学校
	private TextView tv_school;

	// 添加心愿按钮
	private FloatingActionButton mFloatBtn = null;
	// 主页导航
	private ViewPager viewPager = null;
	private SampleFragmentPagerAdapter mPagerAdapter = null;
	private TabLayout tabLayout = null;

	private final static int count = 2;

	// 三个Tab 每个Tab都是一个单独的Layout
	private RelativeLayout section1;
	private RelativeLayout section2;

	// Tab上的文字，主要是需要改变字体的颜色
	private TextView textView1;
	private TextView textView2;

	// 游标
	private View cursor;

	// 标题上的通知文字
	private TextView alertView1;
	private TextView alertView2;

	// 定义颜色变化的起始值和结束值
	private final int startColorR = 0x66;
	private final int startColorG = 0x66;
	private final int startColorB = 0x66;
	private final int endColorR = 0x2C;
	private final int endColorG = 0xA7;
	private final int endColorB = 0xDB;

	// 用于区分Scroll类别
	private static boolean isClick = false;
	
	private float offset = 0;	
	private int currentPage = 0;

	/**
	 * Used to store the last screen title. For use in
	 * {@link #restoreActionBar()}.
	 */

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		actionBar = getSupportActionBar();

		actionBar.setTitle("Wishes");
		// actionBar.setHomeAsUpIndicator(R.drawable.ic_reorder_white_24dp);
		// 设置返回按钮
		actionBar.setDisplayHomeAsUpEnabled(true);

		iv_icon = (CircleImageView) findViewById(R.id.iv_drawer_icon);
		tv_nick = (TextView) findViewById(R.id.tv_drawer_nick);
		tv_school = (TextView) findViewById(R.id.tv_drawer_schoolname);

		navigationView = (NavigationView) findViewById(R.id.naviagionview);
		// 侧滑栏 条目点击
		navigationView
				.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
					@Override
					public boolean onNavigationItemSelected(MenuItem menuItem) {
						switch (menuItem.getItemId()) {
						case R.id.nav_pushwish:
							startActivity(new Intent(MainActivity.this,
									MyPushWishActivity.class));
							break;
						case R.id.nav_pickwish:
							startActivity(new Intent(MainActivity.this,
									MyPickWishActivity.class));
							break;
						case R.id.nav_setting:
							startActivity(new Intent(MainActivity.this,
									SettingActivity.class));
							break;
						}
						menuItem.setChecked(true);
						mDrawer.closeDrawers();
						return true;
					}
				});

		drawerheader = (LinearLayout) findViewById(R.id.drawer_header);
		// 侧滑栏 头部点击
		drawerheader.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(MainActivity.this,
						MyinfoActivity.class));
				mDrawer.closeDrawers();
			}
		});
		// Animate the Hamburger Icon
		actionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawer,
				toolbar, R.string.drawer_open, R.string.drawer_close);
		mDrawer.setDrawerListener(actionBarDrawerToggle);

		InitView();
		// 设置主页viewpager
		viewPager = (ViewPager) findViewById(R.id.viewpager);
		mPagerAdapter = new SampleFragmentPagerAdapter(
				getSupportFragmentManager(), this);
		viewPager.setAdapter(mPagerAdapter);
		viewPager.setOnPageChangeListener(new MyOnPageChangeListener());

		setViewPagerScrollSpeed();
		isClick = true;

		// 注册evenbus的事件订阅
		EventBus.getDefault().register(this);

		initDrawerheader();

		mFloatBtn = (FloatingActionButton) findViewById(R.id.floatbtn);
		mFloatBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this,
						AddWishActivity.class);
				startActivity(intent);
			}

		});

	}

	@Override
	protected void onStart() {
		initDrawerheader();
		super.onStart();
	}

	@Override
	public void onResume() {
		super.onResume();
		// 友盟用户活跃统计
		MobclickAgent.onResume(this);
	}

	@Override
	public void onPause() {
		super.onPause();
		// 友盟用户活跃统计
		MobclickAgent.onPause(this);
	}

	// 初始化抽屉导航的header（个人信息）
	public void initDrawerheader() {
		tv_nick.setText(SpUtils.get(getApplicationContext(),
				AppConstant.USER_NICKNAME, "姓名").toString());
		tv_school.setText(SpUtils.get(getApplicationContext(),
				AppConstant.USER_SCHOOLNAME, "学校").toString());
		Integer sex = (Integer) SpUtils.get(getApplicationContext(),
				AppConstant.USER_SEX, 1);
		String iconurl = SpUtils.get(getApplicationContext(),
				AppConstant.USER_ICONURL, "").toString();
		if (iconurl != null && !iconurl.equals("")) {
			Picasso.with(this).load(iconurl).into(iv_icon);
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		EventBus.getDefault().unregister(this);
		super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case android.R.id.home:
			mDrawer.openDrawer(GravityCompat.START);
			break;
		default:
			break;
		}
		if (actionBarDrawerToggle.onOptionsItemSelected(item))
			return true;
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onPostCreate(savedInstanceState);
		actionBarDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
		actionBarDrawerToggle.onConfigurationChanged(newConfig);
	}

	// 主页 心愿墙和消息 pageradapter
	public class SampleFragmentPagerAdapter extends FragmentStatePagerAdapter {
		final int PAGE_COUNT = 2;
		private String tabTitles[] = new String[] { "心愿墙", "消息", };
		private Context mContext;
		private boolean msgIconVisible = false;

		public SampleFragmentPagerAdapter(FragmentManager fm) {
			super(fm);
			// TODO Auto-generated constructor stub
		}

		public SampleFragmentPagerAdapter(FragmentManager fm, Context mContext) {
			super(fm);
			this.mContext = mContext;
		}

		@Override
		public Fragment getItem(int arg0) {
			// TODO Auto-generated method stub
			if (arg0 == 0) {
				return new MainTabWishFragment();
			} else {
				return new MainTabInfoFragment();
			}

		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return PAGE_COUNT;
		}

		@SuppressLint("NewApi")
		@Override
		public CharSequence getPageTitle(int position) {
			// TODO Auto-generated method stub
			if (position == 0 || !msgIconVisible) {
				return tabTitles[position];
			} else {
				Drawable image = mContext.getResources().getDrawable(
						R.drawable.bg_alert_info, null);
				image.setBounds(0, 0, 20, 20);
				SpannableString sb = new SpannableString(tabTitles[position]
						+ " ");
				ImageSpan imageSpan = new ImageSpan(image,
						ImageSpan.ALIGN_BASELINE);
				int start = tabTitles[position].length();
				sb.setSpan(imageSpan, start, start + 1,
						Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
				return sb;
			}
		}

		public void toggleIcon(boolean msgIconVisible) {
			this.msgIconVisible = msgIconVisible;
		}
	}

	public void onEventMainThread(UserNicknameChange event) {
		tv_nick.setText(event.getName());
	}

	public void onEventMainThread(UserSchoolnameChange event) {
		tv_school.setText(event.getSchoolname());
	}

	public void onEventMainThread(UserIconChange event) {
		Picasso.with(this).load(event.getIconurl()).into(iv_icon);
	}

	public void onEventMainThread(UserLogout event) {
		tv_nick.setText("");
		tv_school.setText("");
		Picasso.with(this).load(R.drawable.addavatar).into(iv_icon);
		finish();
	}

	public void onEventMainThread(UnReadSetEvent event) {
		if (mPagerAdapter != null && tabLayout != null) {
			mPagerAdapter.toggleIcon(event.isUnread());
			// viewPager.destroyDrawingCache();
			mPagerAdapter.saveState();
			// mPagerAdapter.notifyDataSetChanged();
			// tabLayout.getTabAt(1).setText(mPagerAdapter.getPageTitle(1));
		}
	}

	/** 加载基本的View文件 */
	private void InitView() {
		section1 = (RelativeLayout) findViewById(R.id.viewpage_title_section1);
		section1.setOnClickListener(new MyOnClickListener(1));
		section2 = (RelativeLayout) findViewById(R.id.viewpage_title_section2);
		section2.setOnClickListener(new MyOnClickListener(2));

		textView1 = (TextView) findViewById(R.id.viewpage_title_text1);
		textView2 = (TextView) findViewById(R.id.viewpage_title_text2);

		alertView1 = (TextView) findViewById(R.id.viewpage_title_alert1);
		alertView2 = (TextView) findViewById(R.id.viewpage_title_alert2);

		cursor = (View) findViewById(R.id.viewpage_title_cursor);
		
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenW = dm.widthPixels;// 获取分辨率宽度
		offset = screenW / count;// 计算偏移量
	}

	/**
	 * 设置滚动时底下文字颜色的变化过程
	 * 
	 * @param oldTab
	 * @param oldColor
	 * @param newTab
	 * @param newColor
	 */
	public void setGradientText(int oldTab, int oldColor, int newTab,
			int newColor) {
		/* System.out.println(Integer.toHexString(oldColor)); */

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
		}

		switch (newTab) {
		case 0:
			textView1.setTextColor(Color.rgb(newred, newgreen, newblue));
			break;
		case 1:
			textView2.setTextColor(Color.rgb(newred, newgreen, newblue));
			break;

		}
	}

	/** 页面切换时的动作 */
	public class MyOnPageChangeListener implements OnPageChangeListener {
		private float currentPercentage = 0;

		private float lastPosition = 0;
		public void onPageScrollStateChanged(int arg0) {
		}

		/** 三个参数分别为当前的页面，当前页面偏移百分比，当前页面偏移的像素 */
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			Animation animation = new TranslateAnimation(lastPosition, offset*arg0+offset*arg1, 0, 0);//显然这个比较简洁，只有一行代码。
			animation.setFillAfter(true);// True:图片停在动画结束位置
			animation.setDuration(300);
			lastPosition = offset*arg0+offset*arg1;
			cursor.startAnimation(animation);
			
			
			  System.out.println("onPageScrolled:" + arg0 + ";" + arg1 + ";" +
					  currentPage+";"+offset*arg1);
			 
			/** 为了避开最后一次改变后的触发，采用这种判断方式 */
			if (arg1 < 0.0001 || arg1 > 0.9999) {
				return;
			}

			/** 设置滑动时颜色的渐变效果 */
			// 往右滑动的情况
			if (arg1 > currentPercentage) {
				int oldoffsetR = (int) ((endColorR - startColorR) * arg1);
				int newoffsetR = endColorR - oldoffsetR;

				int oldoffsetG = (int) ((endColorG - startColorG) * arg1);
				int newoffsetG = endColorG - oldoffsetG;

				int oldoffsetB = (int) ((endColorB - startColorB) * arg1);
				int newoffsetB = endColorB - oldoffsetB;

				int oldColor = (((oldoffsetR << 8) + (0xbb + oldoffsetG)) << 8)
						+ oldoffsetB;
				int newColor = (((newoffsetR << 8) + newoffsetG) << 8)
						+ newoffsetB;
				/* System.out.println(Integer.toHexString(newColor)); */
				/*setGradientText(arg0, oldColor, arg0 + 1, newColor);*/
				currentPercentage = arg1;
			}
			// 往左滑动情况
			else if (currentPercentage > arg1) {
				int oldoffsetR = (int) ((endColorR - startColorR) * (1 - arg1));
				int newoffsetR = endColorR - oldoffsetR;

				int oldoffsetG = (int) ((endColorG - startColorG) * (1 - arg1));
				int newoffsetG = endColorG - oldoffsetG;

				int oldoffsetB = (int) ((endColorB - startColorB) * (1 - arg1));
				int newoffsetB = endColorB - oldoffsetB;

				int oldColor = (((oldoffsetR << 8) + (0xbb + oldoffsetG)) << 8)
						+ oldoffsetB;
				int newColor = (((newoffsetR << 8) + newoffsetG) << 8)
						+ newoffsetB;
				/* System.out.println(Integer.toHexString(oldColor)); */
				/*setGradientText(arg0 + 1, oldColor, arg0, newColor);*/
				currentPercentage = arg1;
			}
		}

		// 实测这边不会在onPageScrolled执行完成后在执行，一般80%左右这边就触发了
		public void onPageSelected(int arg0) {
			currentPage = arg0;
			setSelectTextColor(arg0);
			switch (arg0) {
			case 0:
				
				break;
			case 1:
				
				break;
			case 2:
				break;

			default:
				break;
			}

		}
	}

	/** 设置当前显示的文字颜色 ,文字选中的颜色和底部游标的颜色是一致的 */
	public void setSelectTextColor(int tab) {
		switch (tab) {
		case 0:
			textView1.setTextColor(getResources().getColor(R.color.colorPrimary));
			textView2.setTextColor(getResources().getColor(R.color.lblack));
			break;
		case 1:
			textView1.setTextColor(getResources().getColor(R.color.lblack));
			textView2.setTextColor(getResources().getColor(R.color.colorPrimary));
			break;
		}
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
			viewPager.setCurrentItem(index - 1);
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
					viewPager.getContext());
			mScroller.set(viewPager, scroller);
		} catch (NoSuchFieldException e) {

		} catch (IllegalArgumentException e) {

		} catch (IllegalAccessException e) {

		}
	}

	/**
	 * 用于设置viewpager手动切换时的切换速度 这边需要分开判断，手动切换页面时应该时没有滑动效果 在屏幕上滑动切换时应该是有切换效果的
	 * 
	 * Copyright 2015
	 * 
	 * @author preparing
	 * @date 2015-6-14
	 */
	public class FixedSpeedScroller extends Scroller {
		private int mDuration = 100;
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
		public boolean computeScrollOffset() {
			boolean bool = super.computeScrollOffset();
			if (isFinished()) {
				isClick = false;
			}
			return bool;
		}
	}
}
