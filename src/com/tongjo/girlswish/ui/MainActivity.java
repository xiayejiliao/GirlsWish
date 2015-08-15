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

public class MainActivity extends AppCompatActivity {
	private DrawerLayout mDrawer;
	private Toolbar toolbar;
	private ActionBar actionBar;
	private NavigationView navigationView;
	private ActionBarDrawerToggle actionBarDrawerToggle;
	private ViewPager mViewPager;
	private TabLayout mTabLayout;
	private LinearLayout drawerheader;
	private CircleImageView iv_icon;
	private TextView tv_nick;
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
		navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
			@Override
			public boolean onNavigationItemSelected(MenuItem menuItem) {
				switch (menuItem.getItemId()) {
				case R.id.nav_pushwish:
					System.out.println("++++++");
					break;
				case R.id.nav_pickwish:

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
		Picasso.with(this).load(iconurl).into(iv_icon);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		EventBus.getDefault().unregister(this);
		super.onDestroy();
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
