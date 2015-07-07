package com.tongjo.girlswish.ui;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tongjo.girlswish.R;
import com.viewpagerindicator.TabPageIndicator;

/**
 * 我的对应的fragment Copyright 2015
 * 
 * @author preparing
 * @date 2015-6-14
 */
public class MainTabMeFragment extends BaseFragment {
	private ViewPager viewpager;
	private TabPageIndicator tabPageIndicator;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_me, container, false);
		viewpager = (ViewPager) rootView.findViewById(R.id.viewpage_fragme_wishs);
		tabPageIndicator=(TabPageIndicator)rootView.findViewById(R.id.indicator_fragme_wishs);
		viewpager.setAdapter(new MyPages());
		tabPageIndicator.setViewPager(viewpager);
		return rootView;
	}

	class MyPages extends PagerAdapter {
		private final String[] CONTENT = new String[] { "未摘心愿", "已摘心愿", "Albums", "Songs", "Playlists", "Genres" };

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 2;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			// TODO Auto-generated method stub
			return CONTENT[position];
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			// TODO Auto-generated method stub
			return new TextView(getActivity());
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			// TODO Auto-generated method stub
			super.destroyItem(container, position, object);
		}
	}
}
