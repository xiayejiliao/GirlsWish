package com.tongjo.girlswish.ui;

import java.lang.reflect.Type;
import java.util.List;

import org.apache.http.Header;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;
import com.tongjo.bean.TJResponse;
import com.tongjo.bean.TJUserInfo;
import com.tongjo.bean.TJWish;
import com.tongjo.girlswish.BaseApplication;
import com.tongjo.girlswish.R;
import com.tongjo.girlswish.utils.AppConstant;
import com.tongjo.girlswish.utils.ToastUtils;
import com.viewpagerindicator.TabPageIndicator;

/**
 * 我的对应的fragment Copyright 2015
 * 
 * @author preparing
 * @date 2015-6-14
 */
public class MainTabMeFragment extends BaseFragment {
	private static final String TAG = "MainTabMeFragment";
	private ViewPager viewpager;
	private TabPageIndicator tabPageIndicator;
	private AsyncHttpClient asyncHttpClient;
	private Context mcontext;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mcontext = getActivity().getApplicationContext();

		View rootView = inflater.inflate(R.layout.fragment_me, container, false);
		viewpager = (ViewPager) rootView.findViewById(R.id.viewpage_fragme_wishs);
		tabPageIndicator = (TabPageIndicator) rootView.findViewById(R.id.indicator_fragme_wishs);
		viewpager.setAdapter(new MyPages());
		tabPageIndicator.setViewPager(viewpager);

		return rootView;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		asyncHttpClient = ((BaseApplication) getActivity().getApplication()).getAsyncHttpClient();

	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (isVisibleToUser == true) {
			asyncHttpClient.get(AppConstant.URL_BASE + AppConstant.URL_WISHLIST, wishListResponse);
		} else {

		}
	}

	private TextHttpResponseHandler wishListResponse = new TextHttpResponseHandler("UTF-8") {

		@Override
		public void onSuccess(int arg0, Header[] arg1, String arg2) {
			if (arg0 == 200) {
				Type type = new TypeToken<TJResponse<Wishs>>() {
				}.getType();
				TJResponse<Wishs> mywishs = new Gson().fromJson(arg2, type);
				if(mywishs.getResult().getCode()==1){
					ToastUtils.show(getActivity(),mywishs.getResult().getMessage());
				}
			}
		}

		@Override
		public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
			System.out.println("========");
		}
	};

	class Wishs {
		private List<TJWish> wishList;

		public Wishs(List<TJWish> wishList) {
			super();
			this.wishList = wishList;
		}

		public List<TJWish> getWishList() {
			return wishList;
		}

		public void setWishList(List<TJWish> wishList) {
			this.wishList = wishList;
		}

		@Override
		public String toString() {
			return "Wishs [wishList=" + wishList + "]";
		}

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
