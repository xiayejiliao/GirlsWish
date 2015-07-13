package com.tongjo.girlswish.ui;

import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import org.apache.http.Header;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.j256.ormlite.dao.Dao;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;
import com.tongjo.bean.TJResponse;
import com.tongjo.bean.TJUserInfo;
import com.tongjo.bean.TJWish;
import com.tongjo.girlswish.BaseApplication;
import com.tongjo.girlswish.R;
import com.tongjo.girlswish.model.UserSex;
import com.tongjo.girlswish.utils.AppConstant;
import com.tongjo.girlswish.utils.SpUtils;
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
	private TextView tv_info;
	private ImageView avatar;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mcontext = getActivity().getApplicationContext();

		View rootView = inflater.inflate(R.layout.fragment_me, container, false);
		viewpager = (ViewPager) rootView.findViewById(R.id.viewpage_fragme_wishs);
		tv_info = (TextView) rootView.findViewById(R.id.tv_fragme_info);
		tabPageIndicator = (TabPageIndicator) rootView.findViewById(R.id.indicator_fragme_wishs);
		viewpager.setAdapter(new MyPages());
		tabPageIndicator.setViewPager(viewpager);
		
		avatar = (ImageView)rootView.findViewById(R.id.iv_fragme_icon);
		avatar.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(),UserBasicInfoActivity.class);
				startActivity(intent);
			}
			
		});
		
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
				if (mywishs.getResult().getCode() == 1) {
					AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
					builder.setMessage(mywishs.getResult().getMessage()).setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							Intent intent = new Intent(getActivity(), LoginActivity.class);
							startActivityForResult(intent, AppConstant.FORRESULT_LOG);
						}
					}).setNegativeButton("No", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							dialog.cancel();
						}
					}).show();
					AlertDialog alert = builder.create();

				}
				if (mywishs.getResult().getCode() == 0) {
					List<TJWish> tjWishs=mywishs.getData().getWishList();
					System.out.println("777777777777");
					for (TJWish tjWish : tjWishs) {
						try {
							tjwishDao.createOrUpdate(tjWish);
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					try {
						System.out.println(tjwishDao.queryForAll().size());
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}

		@Override
		public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
			System.out.println("========");
		}
	};

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == AppConstant.FORRESULT_LOG && resultCode == AppConstant.FORRESULT_LOG_OK) {
			asyncHttpClient.get(AppConstant.URL_BASE + AppConstant.URL_WISHLIST, wishListResponse);
			tv_info.setVisibility(View.GONE);
		}
		if (requestCode == AppConstant.FORRESULT_LOG && resultCode == AppConstant.FORRESULT_LOG_CANCANL) {
			tv_info.setVisibility(View.VISIBLE);
			tv_info.setText("请登陆");
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
		private final String[] CONTENTMAN = new String[] { "未完成", "已完成", };
		private final String[] CONTENTWOMEN = new String[] { "未摘心愿", "已摘心愿", };

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
			UserSex sex = (UserSex) SpUtils.get(mcontext, AppConstant.USER_SEX, UserSex.MAN);
			if (sex == UserSex.MAN) {
				return CONTENTMAN[position];
			} else {
				return CONTENTWOMEN[position];
			}
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			// TODO Auto-generated method stub
			
			return new Fragment();
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			// TODO Auto-generated method stub
			super.destroyItem(container, position, object);
		}
	}
}
