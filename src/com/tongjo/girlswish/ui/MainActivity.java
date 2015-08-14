package com.tongjo.girlswish.ui;

import com.tongjo.girlswish.R;

import android.app.Activity;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
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
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
	private DrawerLayout mDrawer;
	private Toolbar toolbar;
	private ActionBar actionBar;
	private NavigationView navigationView;
	private ActionBarDrawerToggle actionBarDrawerToggle;
	private ViewPager mViewPager;
	private TabLayout mTabLayout;

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

		navigationView = (NavigationView) findViewById(R.id.naviagionview);
		setupDrawerContent(navigationView);

		// Animate the Hamburger Icon
		actionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open, R.string.drawer_close);
		mDrawer.setDrawerListener(actionBarDrawerToggle);

		// Get the ViewPager and set it's PagerAdapter so that it can display
		// items
		ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
		viewPager.setAdapter(new SampleFragmentPagerAdapter(getSupportFragmentManager()));

		// Give the TabLayout the ViewPager
		TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
		tabLayout.setupWithViewPager(viewPager);

		// 设置主界面
		// getFragmentManager().beginTransaction().replace(R.id.container,new
		// android.app.Fragment()).commit();
	}

	private void setupDrawerContent(NavigationView navigationView) {
		navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
			@Override
			public boolean onNavigationItemSelected(MenuItem menuItem) {
				switch (menuItem.getItemId()) {
				case R.id.nav_first_fragment:
					System.out.println("++++++");
					break;
				case R.id.nav_second_fragment:
					System.out.println("------");

					break;
				}
				menuItem.setChecked(true);
				setTitle(menuItem.getTitle());
				mDrawer.closeDrawers();
				return true;
			}
		});
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
		private String tabTitles[] = new String[] { "心愿墙","消息", };
		private Context mContext;

		public SampleFragmentPagerAdapter(FragmentManager fm) {
			super(fm);
			// TODO Auto-generated constructor stub
		}

		@Override
		public Fragment getItem(int arg0) {
			// TODO Auto-generated method stub
			Fragment fragment = new Fragment();
			return fragment;
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
}
