package com.tongjo.girlswish.ui;

import java.lang.reflect.Type;
import java.util.List;

import org.apache.http.Header;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.tongjo.bean.TJResponse;
import com.tongjo.bean.TJWish;
import com.tongjo.bean.TJWishList;
import com.tongjo.girlswish.R;
import com.tongjo.girlswish.event.WishDelete;
import com.tongjo.girlswish.event.WishPush;
import com.tongjo.girlswish.ui.MainTabWishAdapter.MItemClickListener;
import com.tongjo.girlswish.ui.MainTabWishAdapter.MItemLongPressListener;
import com.tongjo.girlswish.utils.AppConstant;
import com.tongjo.girlswish.utils.StringUtils;
import com.tongjo.girlswish.utils.ToastUtils;
import com.tongjo.girlswish.widget.DeleteConfirmDialog.DialogClickListener;
import com.tongjo.girlwish.data.DataContainer;

import de.greenrobot.event.EventBus;

/**
 * 心愿墙对应的fragment Copyright 2015
 * 
 * @author preparing
 * @date 2015-6-14
 */
public class MainTabWishFragment extends BaseFragment {
	private final String TAG = "MainTabWishFragment";
	private PullToRefreshListView mListView = null;
	private MainTabWishAdapter mAdapter = null;
	private WishDialogFragment dialog = null;
	private ViewGroup mEmptyView = null;

	private static final String wishwomen = "0";
	private static final String wishmen = "1";
	private static final String wishall = "2";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		EventBus.getDefault().register(this);
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		EventBus.getDefault().unregister(this);
		super.onDestroy();
	}

	 @Override
	    public void setUserVisibleHint(boolean isVisibleToUser) {
	        super.setUserVisibleHint(isVisibleToUser);
	        if (isVisibleToUser) {
	        	/*getWishData(wishall);*/
	        }
	    }
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_wishs, container,
				false);

		InitView(rootView);
		updateUi(true);
		/* MockData(); */
		getWishData(wishall);
		return rootView;
	}

	/**
	 * 添加心愿选择的菜单
	 */
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
		inflater.inflate(R.menu.main, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.action_all:
			getWishData(wishall);
			break;
		case R.id.action_men:
			getWishData(wishmen);
			break;
		case R.id.action_women:
			getWishData(wishwomen);
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	public void onEventMainThread(WishDelete event) {
		getWishData(wishall);
	}

	public void onEventMainThread(WishPush event) {
		getWishData(wishall);
	}

	public void InitView(View view) {
		mListView = (PullToRefreshListView) view.findViewById(R.id.listview);
		mEmptyView = (ViewGroup) view.findViewById(R.id.empty_view);
		mAdapter = new MainTabWishAdapter(getActivity(), DataContainer.WishList);
		mListView.setAdapter(mAdapter);
		mListView.setEmptyView(mEmptyView);
		mAdapter.setMItemClickListener(new MItemClickListener() {

			@Override
			public void MItemClick(View v, int position) {
				Log.d(TAG, String.valueOf(position));
				/*
				 * dialog = new WishDialogFragment(position);
				 * dialog.show(getFragmentManager(), "WishDialogFragment");
				 */
				final TJWish wish = DataContainer.WishList.get(position);
				AlertDialog.Builder builder = new AlertDialog.Builder(
						getActivity());
				builder.setTitle("摘取心愿");
				builder.setMessage("确定要摘取心愿");
				builder.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								if (wish != null && wish.get_id() != null) {
									pickWish(wish.get_id().toString());
								} else {
									Toast.makeText(getActivity(), "系统出错!",
											Toast.LENGTH_LONG).show();
								}
							}
						});
				builder.setNegativeButton("取消",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {

							}
						});
				builder.show();
			}

		});

		mAdapter.setMItemLongPressListener(new MItemLongPressListener() {

			@Override
			public void MItemLongPress(View v, int position) {
				/*
				 * DeleteConfirmDialog newFragment = DeleteConfirmDialog
				 * .newInstance("确定删除心愿", "删除心愿", null, mListener);
				 * newFragment.show(getActivity().getFragmentManager(),
				 * "dialog");
				 */
			}

		});

		mListView.setMode(Mode.BOTH);
		mListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(getActivity(),
						System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
								| DateUtils.FORMAT_SHOW_DATE
								| DateUtils.FORMAT_ABBREV_ALL);
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);

				getWishData(wishall);
			}
		});

	}

	public void updateUi(boolean isEmpty) {
		mAdapter.setList(DataContainer.WishList);
		mAdapter.notifyDataSetChanged();
	}

	/** 长按按钮弹出的对话框的按键操作 */
	private DialogClickListener mListener = new DialogClickListener() {
		@Override
		public void doPositiveClick() {

		}

		@Override
		public void doNegativeClick() {

		}
	};

	/**
	 * 获取心愿列表
	 */
	public void getWishData(String gender) {
		RequestParams requestParams = new RequestParams();
		requestParams.add("gender", gender);
		asyncHttpClient.get(AppConstant.URL_BASE + AppConstant.URL_WISH,requestParams,
				new TextHttpResponseHandler("UTF-8") {

					@Override
					public void onSuccess(int arg0, Header[] arg1, String arg2) {
						mListView.onRefreshComplete();
						if (arg2 == null) {
							ToastUtils.show(getActivity(), "心愿列表获取失败");
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
								ToastUtils.show(getActivity(), "心愿列表获取失败");
								return;
							}
							if (response.getResult().getCode() == 0) {
								if (response.getData().getWishes() != null) {
									Log.d(TAG, response.getData().getWishes()
											.toString());
									DataContainer.WishList.clear();
									DataContainer.WishList
											.addAll((List<TJWish>) response
													.getData().getWishes());
									updateUi(false);
									/*ToastUtils.show(getActivity(), "心愿列表获取成功");*/
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
						mListView.onRefreshComplete();
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
								getWishData(wishall);
							} else if (response.getResult().getCode() == 1) {
								ToastUtils.show(getActivity(), "没有登录，需要登录"
										+ arg0);
							} 
							//2次数超过限制
							else if(response.getResult().getCode() == 2){
								ToastUtils.show(getActivity(), "摘取心愿失败:"
										+ response.getResult().getMessage());
							}
							
							//3信息不完善
							else if(response.getResult().getCode() == 3){
								ToastUtils.show(getActivity(), "摘取心愿失败:"
										+ response.getResult().getMessage());
								
								Intent intent = new Intent(getActivity(),MyinfoActivity.class);
								startActivity(intent);
								
							}else{
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
