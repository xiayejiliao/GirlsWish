package com.tongjo.girlswish.ui;

import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.apache.http.Header;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.tongjo.bean.TJMessage;
import com.tongjo.bean.TJMessageList;
import com.tongjo.bean.TJResponse;
import com.tongjo.bean.TJUserInfo;
import com.tongjo.db.OrmLiteHelper;
import com.tongjo.emchat.UserUtils;
import com.tongjo.emchat.UserUtils.UserGetLisener;
import com.tongjo.girlswish.R;
import com.tongjo.girlswish.event.NewMsgEvent;
import com.tongjo.girlswish.event.UnReadSetEvent;
import com.tongjo.girlswish.ui.MainTabInfoAdapter.MItemClickListener;
import com.tongjo.girlswish.ui.MainTabInfoAdapter.MItemLongPressListener;
import com.tongjo.girlswish.utils.AppConstant;
import com.tongjo.girlswish.utils.CollectionUtils;
import com.tongjo.girlswish.utils.ToastUtils;
import com.tongjo.girlwish.data.DataContainer;
import com.umeng.analytics.MobclickAgent;

import de.greenrobot.event.EventBus;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainTabInfoFragment extends BaseFragment {
	private final static int MEG_WHAT_TOATS = 10010;
	private static String TAG = "MainTabInfoFragment";
	// private SlideListView mListView;
	private MainTabInfoAdapter mListAdapter = null;
	// private RefreshableView mRefreshableView;
	private PullToRefreshListView mListView = null;
	private ViewGroup mEmptyView = null;
	private WishDialogFragment menuDialog = null;

	boolean mIsRefreshing;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_info, container, false);
		initView(view);
		return view;
	}

	private void initView(View view) {
		mEmptyView = (ViewGroup) view.findViewById(R.id.info_empty_view);
		mListView = (PullToRefreshListView) view.findViewById(R.id.info_listview);
		// mRefreshableView = (RefreshableView)
		// view.findViewById(R.id.info_refreshable_view);
		mListAdapter = new MainTabInfoAdapter(getActivity(), DataContainer.MessageList);
		selectData();
		mListView.setEmptyView(mEmptyView);
		mListView.setAdapter(mListAdapter);

		mListAdapter.setMItemClickListener(new MItemClickListener() {

			@Override
			public void MItemClick(View v, int position) {
				TJMessage message = DataContainer.MessageList.get(position);
				if (message != null) {
					if (message.getType() != 0) {
						Intent intent = new Intent();
						switch (message.getType()) {
						// 聊天消息跳转到聊天界面
						case AppConstant.MSG_TYPE_CHAT:
							intent.putExtra("toUserHxid", message.getHxid());
							intent.setClass(MainTabInfoFragment.this.getActivity(), ChatActivity.class);
							break;
						// 系统消息跳转到系统消息界面
						case AppConstant.MSG_TYPE_SYSTEM:
							intent.setClass(MainTabInfoFragment.this.getActivity(), SystemMsgActivity.class);
							break;
						}
						message.setRead(true);
						refreshUI(false);
						updateMessage(message);
						startActivity(intent);
					}
				}
			}

		});

		mListAdapter.setMItemLongPressListener(new MItemLongPressListener() {

			@Override
			public void MItemLongPress(View v, int position) {
				if (!mIsRefreshing) {
					menuDialog = new WishDialogFragment(position);
					menuDialog.show(getFragmentManager(), "WishDialogFragment");
				}
			}

		});

		mListView.setMode(Mode.DISABLED);
		mListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(getActivity(), System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);

				mIsRefreshing = true;
				if (menuDialog != null && menuDialog.isVisible()) {
					menuDialog.dismiss();
				}
				getMessageData();
				mIsRefreshing = false;
			}
		});
		// 注册上下文菜单
		// registerForContextMenu(view);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		Toast.makeText(this.getActivity(), "content" + item.getItemId() + info.position, Toast.LENGTH_LONG).show();
		if (item.getItemId() == Menu.FIRST) {

		}
		return super.onContextItemSelected(item);
	}

	private void selectData() {
		try {
			DataContainer.MessageList.clear();
			Dao<TJMessage, UUID> mTJMessageDao = new OrmLiteHelper(this.getActivity()).getTJMessageDao();
			QueryBuilder<TJMessage, UUID> builder = mTJMessageDao.queryBuilder();
			Where<TJMessage, UUID> where = builder.where();
			// 消息界面只显示聊天消息（和一个人的聊天为一条记录）和系统消息
			where.in("type", Arrays.asList(AppConstant.MSG_TYPE_CHAT, AppConstant.MSG_TYPE_SYSTEM));
			builder.setWhere(where);
			builder.orderBy("createdTime", false);
			PreparedQuery<TJMessage> preparedQuery = builder.prepare();
			DataContainer.MessageList.addAll(mTJMessageDao.query(preparedQuery));
			mListAdapter.notifyDataSetChanged();
		} catch (SQLException e) {
			Log.e(TAG, e.getStackTrace().toString());
		}
	}

	public void deleteMessage(int itemId) {

		if (itemId >= 0 && itemId < DataContainer.MessageList.size()) {
			try {
				Dao<TJMessage, UUID> mTJMessageDao = new OrmLiteHelper(this.getActivity()).getTJMessageDao();
				mTJMessageDao.delete(DataContainer.MessageList.get(itemId));
				DataContainer.MessageList.remove(itemId);
				mListAdapter.notifyDataSetChanged();
			} catch (SQLException e) {
				Log.e(TAG, e.getStackTrace().toString());
			}
		}

	}

	public void updateMessage(TJMessage message) {
		try {
			Dao<TJMessage, UUID> mTJMessageDao = new OrmLiteHelper(this.getActivity()).getTJMessageDao();
			mTJMessageDao.update(message);
		} catch (SQLException e) {
			Log.e(TAG, e.getStackTrace().toString());
		}
	}

	public void addMessage(List<TJMessage> messageList) {
		if (CollectionUtils.isEmpty(messageList)) {
			return;
		}
		try {
			Dao<TJMessage, UUID> mTJMessageDao = new OrmLiteHelper(this.getActivity()).getTJMessageDao();
			for (TJMessage message : messageList) {
				message.setRead(false);
				mTJMessageDao.createIfNotExists(message);
			}
			QueryBuilder<TJMessage, UUID> builder = mTJMessageDao.queryBuilder();
			Where<TJMessage, UUID> where = builder.where();
			where.in("type", Arrays.asList(AppConstant.MSG_TYPE_SYSTEM));
			builder.setWhere(where);
			PreparedQuery<TJMessage> preparedQuery = builder.prepare();
			List<TJMessage> querMessageList = mTJMessageDao.query(preparedQuery);
			TJMessage message;
			if (querMessageList.size() <= 0) {
				message = new TJMessage();
				message.set_id(UUID.fromString(AppConstant.MSG_SYSTEM_UUID));
				message.setTitle("系统消息");
				mTJMessageDao.createIfNotExists(message);
			} else {
				message = querMessageList.get(0);
			}
			message.setContent(messageList.get(messageList.size() - 1).getContent());
			message.setCreatedTime(messageList.get(messageList.size() - 1).getCreatedTime());
			message.setRead(false);
			message.setType(AppConstant.MSG_TYPE_SYSTEM);
			mTJMessageDao.update(message);
		} catch (SQLException e) {
			Log.d(TAG, e.getStackTrace().toString());
		}

	}

	/**
	 * 获取消息列表
	 */
	public void getMessageData() {
		RequestParams requestParams = new RequestParams();
		requestParams.add("page", "0");
		asyncHttpClient.get(AppConstant.URL_BASE + AppConstant.URL_MESSAGE, requestParams, new TextHttpResponseHandler("UTF-8") {

			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) {
				Log.d(TAG, "arg0:" + arg0 + "arg2:" + arg2);
				mListView.onRefreshComplete();
				if (arg2 == null) {
					handler.obtainMessage(MEG_WHAT_TOATS, "消息列表获取失败:").sendToTarget();
					return;
				}
				if (arg0 == 200) {
					Type type = new TypeToken<TJResponse<TJMessageList>>() {
					}.getType();
					TJResponse<TJMessageList> response = new Gson().fromJson(arg2, type);
					if (response == null || response.getResult() == null || response.getData() == null) {
						handler.obtainMessage(MEG_WHAT_TOATS, "消息列表获取失败:").sendToTarget();
						return;
					}
					if (response.getResult().getCode() == 0) {
						if (response.getData().getNotices() != null) {
							Log.d(TAG, response.getData().getNotices().toString());
							// 加入数据库
							addMessage((List<TJMessage>) response.getData().getNotices());
							refreshUI(false);
						}
					} else {
						handler.obtainMessage(MEG_WHAT_TOATS, "消息列表获取失败:" + response.getResult().getMessage()).sendToTarget();
					}
				} else {
					handler.obtainMessage(MEG_WHAT_TOATS, "消息列表获取失败:" + arg0).sendToTarget();
				}
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				mListView.onRefreshComplete();
				handler.obtainMessage(MEG_WHAT_TOATS, "消息列表获取失败" + arg0).sendToTarget();
			}
		});
	}

	//@Override
    /*public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            //相当于Fragment的onResume
        	// 监听消息列表的变化
    	    EventBus.getDefault().register(this);
        } else {
            //相当于Fragment的onPause
        	EventBus.getDefault().unregister(this);
        }
    }*/
	
	@Override
	public void onResume() {
		super.onResume();
		refreshUI(true);
		//友盟页面统计
		MobclickAgent.onPageStart("消息列表");
		// 监听消息列表的变化
	    EventBus.getDefault().register(this);
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		EventBus.getDefault().unregister(this);
		MobclickAgent.onPageEnd("消息列表");
		super.onPause();
	}
	@Override
	public void onStop() {
		//EventBus.getDefault().unregister(this);
		super.onStop();
	}

	public void onEventMainThread(NewMsgEvent event) {
		refreshUI(true);
	}
	
	/* 有新消息到来刷新页面 */
	private void refreshUI(boolean refreshUserInfo) {
		if (mListAdapter != null) {
			mListAdapter.notifyDataSetChanged();
		}
		boolean hasUnReadMsg = false;
		List<TJMessage> md = DataContainer.MessageList;
		for (final TJMessage message : DataContainer.MessageList) {
			if (!message.isRead()) {
				hasUnReadMsg = true;
			}
			if (refreshUserInfo && message.getHxid() != null) {
				UserUtils.getUserByHxidFromNetOrMem(this.getContext(), message.getHxid(), new UserGetLisener() {

					@Override
					public void onGetUser(TJUserInfo userInfo) {
						DataContainer.userInfoMap.put(userInfo.getHxid(), userInfo);
						message.setTitle(userInfo.getNickname());
						message.setAvatarUrl(userInfo.getAvatarUrl());
						message.setUserId(userInfo.get_id().toString());
						MainTabInfoFragment.this.getActivity().runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								if (mListAdapter != null) {
									mListAdapter.notifyDataSetChanged();
								}
							}
						});
						updateMessage(message);
					}
				});
			}
		}
		UnReadSetEvent event = new UnReadSetEvent();
		event.setUnread(hasUnReadMsg);
		EventBus.getDefault().post(event);
	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case MEG_WHAT_TOATS:
				ToastUtils.show(getContext(), (String) msg.obj);
				break;

			default:
				break;
			}
		}

	};

	public class WishDialogFragment extends DialogFragment {
		private Integer mPosition;
		private TextView mDeleteTextView;

		public WishDialogFragment(Integer positon) {
			this.mPosition = positon;
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
			View view = inflater.inflate(R.layout.fragment_info_menu, container);
			mDeleteTextView = (TextView) view.findViewById(R.id.infomenu_delete);
			mDeleteTextView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					deleteMessage(mPosition);
					mListAdapter.notifyDataSetChanged();
					menuDialog.dismiss();
				}
			});
			return view;
		}
	}
}
