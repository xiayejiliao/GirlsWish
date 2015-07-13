package com.tongjo.girlswish.ui;

import java.lang.reflect.Type;
import java.util.List;

import org.apache.http.Header;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.tongjo.bean.TJResponse;
import com.tongjo.bean.TJWish;
import com.tongjo.bean.TJWishList;
import com.tongjo.girlswish.R;
import com.tongjo.girlswish.ui.MainTabWishAdapter.MItemClickListener;
import com.tongjo.girlswish.ui.MainTabWishAdapter.MItemLongPressListener;
import com.tongjo.girlswish.utils.AppConstant;
import com.tongjo.girlswish.utils.StringUtils;
import com.tongjo.girlswish.utils.ToastUtils;
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
	private WishDialogFragment dialog = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_wishs, container,false);


		InitView(rootView);
		/* MockData(); */
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
				Log.d(TAG, String.valueOf(position));
				dialog = new WishDialogFragment(position);
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

	/**
	 * 获取心愿列表
	 */
	public void getWishData() {
		RequestParams requestParams = new RequestParams();
		requestParams.add("page", "0");
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
							TJResponse<TJWishList> response = null;
							try {
								response = new Gson().fromJson(arg2, type);
							} catch (Exception e) {
								e.printStackTrace();
							}

							if (response == null
									|| response.getResult() == null
									|| response.getData() == null) {
								ToastUtils.show(getActivity(), "心愿列表获取失败:");
								return;
							}
							if (response.getResult().getCode() == 0) {
								if (response.getData().getWishes() != null) {
									Log.d(TAG, response.getData().getWishes()
											.toString());
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

	/**
	 * 摘取心愿
	 * 
	 * @param wishId
	 */
	public void pickWish(String wishId) {
		RequestParams requestParams = new RequestParams();
		requestParams.add("_id", wishId);
		asyncHttpClient.post(AppConstant.URL_BASE + AppConstant.URL_PICKWISH,
				requestParams, new TextHttpResponseHandler("UTF-8") {

					@Override
					public void onSuccess(int arg0, Header[] arg1, String arg2) {
						if (dialog != null) {
							dialog.dismiss();
						}
						if (arg2 == null) {
							ToastUtils.show(getActivity(), "摘取心愿失败");
							return;
						}
						Log.d(TAG, arg2);
						if (arg0 == 200) {

							Type type = new TypeToken<TJResponse<Object>>() {
							}.getType();
							TJResponse<Object> response = null;
							try {
								response = new Gson().fromJson(arg2, type);
							} catch (Exception e) {
								e.printStackTrace();
							}
							if (response == null
									|| response.getResult() == null
									|| response.getData() == null) {
								ToastUtils.show(getActivity(), "摘取心愿失败");
								return;
							}
							if (response.getResult().getCode() == 0) {
								ToastUtils.show(getActivity(), "摘取心愿成功" + arg0);
							} else if (response.getResult().getCode() == 0) {
								ToastUtils.show(getActivity(), "没有登录，需要登录"
										+ arg0);
							} else {
								ToastUtils.show(getActivity(), "摘取心愿失败:"
										+ response.getResult().getMessage());
							}
						}
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, String arg2,
							Throwable arg3) {
						if (dialog != null) {
							dialog.dismiss();
						}
						ToastUtils.show(getActivity(), "摘取心愿失败" + arg0);
					}
				});
	}

	@SuppressLint("ValidFragment")
	public class WishDialogFragment extends DialogFragment {
		public int mPosition;
		protected TextView userName;
		protected TextView schoolName;
		protected ImageView avatar;
		protected ImageView pick;
		protected TextView content;
		protected ViewGroup bottomBg;

		public WishDialogFragment(int position) {
			super();
			this.mPosition = position;
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
			View view = inflater.inflate(R.layout.fragment_wishdetail,
					container);
			userName = (TextView) view.findViewById(R.id.wish_username);
			schoolName = (TextView) view.findViewById(R.id.wish_schoolname);
			avatar = (ImageView) view.findViewById(R.id.wish_avatar);
			pick = (ImageView) view.findViewById(R.id.wish_pick);
			content = (TextView) view.findViewById(R.id.wish_content);
			bottomBg = (ViewGroup) view.findViewById(R.id.wish_bottom);

			final TJWish wish = DataContainer.WishList.get(mPosition);
			if (wish != null) {
				if (wish.getContent() != null) {
					content.setText(wish.getContent());
				}

				if (wish.getCreatorUser() != null
						&& wish.getCreatorUser().getNickname() != null) {
					userName.setText(wish.getCreatorUser().getNickname());
				}
				if (wish.getCreatorUser() != null
						&& wish.getCreatorUser().getSchool() != null
						&& wish.getCreatorUser().getSchool().getName() != null) {
					schoolName.setText(wish.getCreatorUser().getSchool()
							.getName());
				}
				if (!StringUtils.isEmpty(wish.getBackgroundColor())) {
					int color = 0;
					try {
						color = Integer.parseInt(wish.getBackgroundColor(), 16);
					} catch (Exception e) {
						e.printStackTrace();
					}

					int red = color >> 4;
					int green = (color >> 2) % 256;
					int blue = color % 256;
					bottomBg.setBackgroundColor(Color.rgb(red, green, blue));
				}
			}

			pick.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (wish != null && wish.get_id() != null) {
						pickWish(wish.get_id().toString());
					} else {

					}
				}

			});

			return view;
		}
	}
}
