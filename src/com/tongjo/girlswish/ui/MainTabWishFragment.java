package com.tongjo.girlswish.ui;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.List;

import org.apache.http.Header;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.tongjo.bean.TJResponse;
import com.tongjo.bean.TJUserInfo;
import com.tongjo.bean.TJWish;
import com.tongjo.bean.TJWishList;
import com.tongjo.girlswish.R;
import com.tongjo.girlswish.model.LoginState;
import com.tongjo.girlswish.model.UserSex;
import com.tongjo.girlswish.ui.MainTabWishAdapter.MItemClickListener;
import com.tongjo.girlswish.ui.MainTabWishAdapter.MItemLongPressListener;
import com.tongjo.girlswish.utils.AppConstant;
import com.tongjo.girlswish.utils.SpUtils;
import com.tongjo.girlswish.utils.StringUtils;
import com.tongjo.girlswish.utils.ToastUtils;
import com.tongjo.girlswish.widget.CustomeProgressDialog;
import com.tongjo.girlswish.widget.RefreshableView;
import com.tongjo.girlswish.widget.RefreshableView.PullToRefreshListener;
import com.tongjo.girlwish.data.DataContainer;

/**
 * 心愿墙对应的fragment Copyright 2015
 * 
 * @author preparing
 * @date 2015-6-14
 */
public class MainTabWishFragment extends BaseFragment {
	private final String TAG = "MainTabWishFragment";
	private RefreshableView mRefreshView = null;
	private ListView mListView = null;
	private MainTabWishAdapter mAdapter = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_wish, container,
				false);

		InitView(rootView);
		/*MockData();*/
		 getWishData(); 
		return rootView;
	}

	public void InitView(View view) {
		mRefreshView = (RefreshableView) view.findViewById(R.id.refresh_view);
		mListView = (ListView) view.findViewById(R.id.listview);
		mAdapter = new MainTabWishAdapter(getActivity(), DataContainer.WishList);
		mListView.setAdapter(mAdapter);
		mAdapter.setMItemClickListener(new MItemClickListener() {

			@Override
			public void MItemClick(View v, int position) {
				WishDialogFragment dialog = new WishDialogFragment();
				dialog.show(getFragmentManager(), "WishDialogFragment");
			}

		});

		mAdapter.setMItemLongPressListener(new MItemLongPressListener() {

			@Override
			public void MItemLongPress(View v, int position) {
				getWishData();
			}

		});

		mRefreshView.setOnRefreshListener(new PullToRefreshListener() {
			@Override
			public void onRefresh() {
				/*
				 * Looper.prepare(); getWishData();
				 */
			}
		}, AppConstant.REFRESH_WISH);

	}

	public void MockData() {
		for (int i = 0; i < 5; i++) {
			TJWish wish = new TJWish();
			DataContainer.WishList.add(wish);
		}
		mAdapter.setList(DataContainer.WishList);
	}

	public void getWishData() {
		RequestParams requestParams = new RequestParams();
		requestParams.add("page", "1");
		asyncHttpClient.get(AppConstant.URL_BASE + AppConstant.URL_WISH,
				requestParams, new TextHttpResponseHandler("UTF-8") {

					@Override
					public void onSuccess(int arg0, Header[] arg1, String arg2) {
						mRefreshView.finishRefreshing();
						if (arg2 == null) {
							ToastUtils.show(getActivity(), "心愿列表获取失败:");
							return;
						}
						Log.d(TAG, arg2);
						if (arg0 == 200) {
							Type type = new TypeToken<TJResponse<TJWishList>>() {
							}.getType();
							TJResponse<TJWishList> response = new Gson()
									.fromJson(arg2, type);
							if (response == null
									|| response.getResult() == null
									|| response.getData() == null) {
								ToastUtils.show(getActivity(), "心愿列表获取失败:");
								return;
							}
							if (response.getResult().getCode() == 0) {
								if (response.getData().getWishes() != null) {
									Log.d(TAG, response
											.getData().getWishes().toString());
									DataContainer.WishList
											.addAll((List<TJWish>) response
													.getData().getWishes());
									mAdapter.notifyDataSetChanged();
								}
							} else {
								ToastUtils.show(getActivity(), "心愿列表获取失败:"
										+ response.getResult().getMessage());
							}
						} else {
							ToastUtils.show(getActivity(), "心愿列表获取失败" + arg0);
						}
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, String arg2,
							Throwable arg3) {
						mRefreshView.finishRefreshing();
						Toast.makeText(getActivity(), "心愿列表获取失败" + arg0,
								Toast.LENGTH_LONG).show();
					}
				});
	}

	@SuppressLint("ValidFragment")
	public class WishDialogFragment extends DialogFragment {
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
			View view = inflater.inflate(R.layout.fragment_wishdetail,
					container);
			return view;
		}
	}
}
