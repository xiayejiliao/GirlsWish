package com.tongjo.girlswish.ui;

import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

import com.tongjo.girlswish.R;
import com.tongjo.girlswish.model.NavDrawerItem;
import com.tongjo.girlswish.widget.NavDrawerListAdapter;

public class MainActivity extends ActionBarActivity {
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private CharSequence mTitle;
	private CharSequence mDrawerTitle;
	private ActionBarDrawerToggle mDrawerToggle;
	private Toolbar topToolBar;
	
    // slide menu items
    private String[] navMenuTitles;
    private TypedArray navMenuIcons; 
    private ArrayList<NavDrawerItem> navDrawerItems;
    private NavDrawerListAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mTitle = mDrawerTitle = getTitle();
		//toolBar
		topToolBar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(topToolBar);
		
		//Drawer
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.drawer_view);
		LayoutInflater inflater = getLayoutInflater();
		View listHeaderView = inflater.inflate(R.layout.navdrawer_list_header, null,
				false);
		mDrawerList.addHeaderView(listHeaderView);		
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_labels);
        navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);      
        navDrawerItems = new ArrayList<NavDrawerItem>();
        // adding nav drawer items to array
       navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1)));
       navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1)));
        // Recycle the typed array
        navMenuIcons.recycle();       
        // setting the nav drawer list adapter
        adapter = new NavDrawerListAdapter(getApplicationContext(),
                navDrawerItems, R.layout.navdrawer_list_item);
        mDrawerList.setAdapter(adapter);        
		mDrawerToggle = new ActionBarDrawerToggle(MainActivity.this,
				mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {
			/** Called when a drawer has settled in a completely closed state. */
			public void onDrawerClosed(View view) {
				super.onDrawerClosed(view);
				getSupportActionBar().setTitle(mTitle);
				invalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()
			}
			/** Called when a drawer has settled in a completely open state. */
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				getSupportActionBar().setTitle(mDrawerTitle);
				invalidateOptionsMenu();
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);
		mDrawerToggle.setDrawerIndicatorEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		mDrawerList
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						selectItemFragment(position);
					}
				});
	}

	private void selectItemFragment(int position) {
		System.out.println(position);
		Fragment fragment = null;
		FragmentManager fragmentManager = getSupportFragmentManager();
		switch (position) {
		default:
		case 1:
			fragment = new HomeFragment();
			break;
		case 2:
			fragment = new HomeFragment();
			break;
		}
		fragmentManager.beginTransaction()
				.replace(R.id.frame_container, fragment).commit();
		mDrawerList.setItemChecked(position, true);
		setTitle(navMenuTitles[position - 1]);
		mDrawerLayout.closeDrawer(mDrawerList);
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getSupportActionBar().setTitle(mTitle);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}