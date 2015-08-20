package com.tongjo.girlswish.ui;

import com.squareup.picasso.Picasso;
import com.tongjo.girlswish.R;
import com.tongjo.girlswish.event.UserIconChange;
import com.tongjo.girlswish.event.UserLogout;
import com.tongjo.girlswish.event.UserNicknameChange;
import com.tongjo.girlswish.event.UserSchoolnameChange;
import com.tongjo.girlswish.utils.AppConstant;
import com.tongjo.girlswish.utils.SpUtils;
import com.tongjo.girlswish.widget.CircleImageView;

import de.greenrobot.event.EventBus;
import android.app.Activity;
import android.support.design.widget.NavigationView;
import android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TabLayout.OnTabSelectedListener;
import android.support.design.widget.TabLayout.Tab;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 
* @Description: 主界面包括策划导航，和标签导航
* @author 16ren 
* @date 2015年8月20日 下午5:38:10 
*
 */
public class MainActivity extends AppCompatActivity {
	//策划框架  v4包里面的
	private DrawerLayout mDrawer;
	//自定义actionbar
	private Toolbar toolbar;
	private ActionBar actionBar;
	//策划界面
	private NavigationView navigationView;
	//同步侧滑栏和actionbar
	private ActionBarDrawerToggle actionBarDrawerToggle;
	//主界面心愿墙列表和消息列表
	private ViewPager mViewPager;
	//mViewPager导航标签
	private TabLayout mTabLayout;
	//策划栏的头部分 
	private LinearLayout drawerheader;
	//侧滑栏 头部 我的头像
	private CircleImageView iv_icon;
	//侧滑栏 头部 我的昵称
	private TextView tv_nick;
	//侧滑栏 头部 我的学校
	private TextView tv_school;

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
		// actionBar.setHomeAsUpIndicator(R.drawable.ic_reorder_white_24dp);
		// 设置返回按钮
		actionBar.setDisplayHomeAsUpEnabled(true);

		iv_icon = (CircleImageView) findViewById(R.id.iv_drawer_icon);
		tv_nick = (TextView) findViewById(R.id.tv_drawer_nick);
		tv_school = (TextView) findViewById(R.id.tv_drawer_schoolname);

		navigationView = (NavigationView) findViewById(R.id.naviagionview);
		//侧滑栏 条目点击
		navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
			@Override
			public boolean onNavigationItemSelected(MenuItem menuItem) {
				switch (menuItem.getItemId()) {
				case R.id.nav_pushwish:
					startActivity(new Intent(MainActivity.this, MyPushWishActivity.class));
					break;
				case R.id.nav_pickwish:
					startActivity(new Intent(MainActivity.this, MyPickWishActivity.class));
					break;
				case R.id.nav_setting:
					startActivity(new Intent(MainActivity.this, SettingActivity.class));
					break;
				}
				menuItem.setChecked(true);
				mDrawer.closeDrawers();
				return true;
			}
		});

		drawerheader = (LinearLayout) findViewById(R.id.drawer_header);
		//侧滑栏 头部点击 
		drawerheader.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(MainActivity.this, MyinfoActivity.class));
				mDrawer.closeDrawers();
			}
		});
		// Animate the Hamburger Icon
		actionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open, R.string.drawer_close);
		mDrawer.setDrawerListener(actionBarDrawerToggle);

		// 设置主页viewpager
		ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
		viewPager.setAdapter(new SampleFragmentPagerAdapter(getSupportFragmentManager()));

		// 设置主页viewpage 导航标签
		TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
		tabLayout.setupWithViewPager(viewPager);
		// 注册evenbus的事件订阅
		EventBus.getDefault().register(this);

		initDrawerheader();

	}

	@Override
	protected void onStart() {
		initDrawerheader();
		super.onStart();
	}

	// 初始化抽屉导航的header（个人信息）
	public void initDrawerheader() {
		tv_nick.setText(SpUtils.get(getApplicationContext(), AppConstant.USER_NICKNAME, "姓名").toString());
		tv_school.setText(SpUtils.get(getApplicationContext(), AppConstant.USER_SCHOOLNAME, "学校").toString());
		Integer sex = (Integer) SpUtils.get(getApplicationContext(), AppConstant.USER_SEX, 1);
		String iconurl = SpUtils.get(getApplicationContext(), AppConstant.USER_ICONURL, "").toString();
		if (iconurl != null&&!iconurl.equals("")) {
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

	//主页 心愿墙和消息 pageradapter
	public class SampleFragmentPagerAdapter extends FragmentPagerAdapter {
		final int PAGE_COUNT = 2;
		private String tabTitles[] = new String[] { "心愿墙", "消息", };
		private Context mContext;

		public SampleFragmentPagerAdapter(FragmentManager fm) {
			super(fm);
			// TODO Auto-generated constructor stub
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

		@Override
		public CharSequence getPageTitle(int position) {
			// TODO Auto-generated method stub
			return tabTitles[position];
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
	}
}
