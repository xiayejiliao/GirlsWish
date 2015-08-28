package com.tongjo.girlswish.ui;

import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.apache.http.Header;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.easemob.EMEventListener;
import com.easemob.EMNotifierEvent;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMMessage;
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
import com.tongjo.db.OrmLiteHelper;
import com.tongjo.emchat.GWHXSDKHelper;
import com.tongjo.emchat.HXSDKHelper;
import com.tongjo.girlswish.BaseApplication;
import com.tongjo.girlswish.R;
import com.tongjo.girlswish.ui.SystemMsgAdapter.MItemClickListener;
import com.tongjo.girlswish.ui.SystemMsgAdapter.MItemLongPressListener;
import com.tongjo.girlswish.utils.AppConstant;
import com.tongjo.girlswish.utils.CollectionUtils;
import com.tongjo.girlswish.utils.StringUtils;
import com.tongjo.girlswish.utils.ToastUtils;
import com.tongjo.girlwish.data.DataContainer;
import com.umeng.analytics.MobclickAgent;

public class SystemMsgActivity extends AppCompatActivity implements EMEventListener {
	private final static int MEG_WHAT_TOATS = 10010;
	private static String TAG = "MainTabInfoFragment";
	// private SlideListView mListView;
	private SystemMsgAdapter mListAdapter = null;
	// private RefreshableView mRefreshableView;
	private PullToRefreshListView mListView = null;
	private ViewGroup mEmptyView = null;
	private MsgDialogFragment menuDialog = null;
	private boolean mIsRefreshing;

	private ActionBar mActionBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_systemmsg);
		mActionBar = getSupportActionBar();
		mActionBar.setTitle(R.string.mess);
		mActionBar.setDisplayHomeAsUpEnabled(true);
		initView();
	}


	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("系统消息");
		//友盟用户活跃统计
		MobclickAgent.onPause(this);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void initView() {
		mEmptyView = (ViewGroup) this.findViewById(R.id.info_empty_view);
		mListView = (PullToRefreshListView) this.findViewById(R.id.info_listview);
		// mRefreshableView = (RefreshableView)
		// view.findViewById(R.id.info_refreshable_view);
		mListAdapter = new SystemMsgAdapter(this.getApplicationContext(), DataContainer.SystemMsgList);
		mListView.setEmptyView(mEmptyView);
		mListView.setAdapter(mListAdapter);
		selectData();

		mListAdapter.setMItemClickListener(new MItemClickListener() {

			@Override
			public void MItemClick(View v, int position) {
				TJMessage message = DataContainer.SystemMsgList.get(position);
				if (message != null) {
					if (message.getType() >= 0) {
						Intent intent = new Intent();
						switch (message.getType()) {
						// 打开web页面
						case AppConstant.MSG_TYPE_NOTICE:
							if (!StringUtils.isEmpty(message.getNoticeUrl())) {
								intent.setClass(SystemMsgActivity.this, WebviewActivity.class);
								intent.putExtra(WebviewActivity.WEBURL, message.getNoticeUrl());
								startActivity(intent);
							}
							break;
						// 心愿被摘
						case AppConstant.MSG_TYPE_WISH_PICKED:
							/*if (message.getWishId() == null) {
								ToastUtils.show(SystemMsgActivity.this, "心愿不存在，请刷新");
							} else {
								intent.setClass(SystemMsgActivity.this, GirlUnderwayWishActivity.class);
								intent.putExtra("wishuuid", message.getWishId());
								startActivity(intent);
							}*/
							intent.setClass(SystemMsgActivity.this, MyPushWishActivity.class);
							startActivity(intent);
							break;
						// 心愿完成
						case AppConstant.MSG_TYPE_WISH_FINISH:
							/*if (message.getWish() == null) {
								ToastUtils.show(SystemMsgActivity.this, "心愿不存在，请刷新");
							} else {
								intent.setClass(SystemMsgActivity.this, GirlFinishWishActivity.class);
								intent.putExtra("wishuuid", message.getWishId());
								startActivity(intent);
							}*/
							intent.setClass(SystemMsgActivity.this, MyPickWishActivity.class);
							startActivity(intent);
							break;
						}
						message.setRead(true);
						updateMessage(message);
						if (mListAdapter != null) {
							mListAdapter.notifyDataSetChanged();
						}
					}
				}
			}

		});
		mListAdapter.setMItemLongPressListener(new MItemLongPressListener() {

			@Override
			public void MItemLongPress(View v, int position) {
				if (!mIsRefreshing) {
					menuDialog = new MsgDialogFragment(position);
					menuDialog.show(SystemMsgActivity.this.getSupportFragmentManager(), "MsgDialogFragment");
				}
			}

		});

		mListView.setMode(Mode.DISABLED);
		mListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(SystemMsgActivity.this, System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);

				mIsRefreshing = true;
				if (menuDialog != null && menuDialog.isVisible()) {
					menuDialog.dismiss();
				}
				getMessageData();
				mIsRefreshing = false;
			}
		});

		// 鏆傛棤娑堟伅鐐瑰嚮鍔犺浇
		mEmptyView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				getMessageData();
			}
		});
	}

	private void selectData() {
		try {
			DataContainer.SystemMsgList.clear();
			Dao<TJMessage, UUID> mTJMessageDao = new OrmLiteHelper(this).getTJMessageDao();
			QueryBuilder<TJMessage, UUID> builder = mTJMessageDao.queryBuilder();
			Where<TJMessage, UUID> where = builder.where();
			where.in("type", Arrays.asList(AppConstant.MSG_TYPE_NOTICE, AppConstant.MSG_TYPE_WISH_PICKED, AppConstant.MSG_TYPE_WISH_PICKED));
			builder.setWhere(where);
			builder.orderBy("createdTime", false);
			PreparedQuery<TJMessage> preparedQuery = builder.prepare();
			DataContainer.SystemMsgList.addAll(mTJMessageDao.query(preparedQuery));
			if (mListAdapter != null) {
				mListAdapter.notifyDataSetChanged();
			}
		} catch (SQLException e) {
			Log.e(TAG, e.getStackTrace().toString());
		}
	}

	public void deleteMessage(int itemId) {

		if (itemId >= 0 && itemId < DataContainer.SystemMsgList.size()) {
			try {
				Dao<TJMessage, UUID> mTJMessageDao = new OrmLiteHelper(this).getTJMessageDao();
				mTJMessageDao.delete(DataContainer.SystemMsgList.get(itemId));
				DataContainer.SystemMsgList.remove(itemId);
				mListAdapter.notifyDataSetChanged();
			} catch (SQLException e) {
				Log.e(TAG, e.getStackTrace().toString());
			}
		}

	}

	public void updateMessage(TJMessage message) {
		try {
			Dao<TJMessage, UUID> mTJMessageDao = new OrmLiteHelper(this).getTJMessageDao();
			mTJMessageDao.update(message);
		} catch (SQLException e) {
			Log.e(TAG, e.getStackTrace().toString());
		}
	}

	public void addMessage(List<TJMessage> systemMsgList) {
		if (CollectionUtils.isEmpty(systemMsgList)) {
			return;
		}
		try {
			Dao<TJMessage, UUID> mTJMessageDao = new OrmLiteHelper(this).getTJMessageDao();
			for (TJMessage message : systemMsgList) {
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
			message.setContent(systemMsgList.get(systemMsgList.size() - 1).getContent());
			message.setCreatedTime(systemMsgList.get(systemMsgList.size() - 1).getCreatedTime());
			message.setRead(false);
			message.setType(AppConstant.MSG_TYPE_SYSTEM);
			Log.e(TAG, message.toString());
			mTJMessageDao.update(message);
		} catch (SQLException e) {
			Log.e(TAG, e.getStackTrace().toString());
		}
	}

	/**
	 * 获取系统消息
	 */
	public void getMessageData() {
		RequestParams requestParams = new RequestParams();
		requestParams.add("page", "0");
		((BaseApplication) this.getApplication()).getAsyncHttpClient().get(AppConstant.URL_BASE + AppConstant.URL_MESSAGE, requestParams, new TextHttpResponseHandler("UTF-8") {

			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) {
				Log.d(TAG, "arg0:" + arg0 + "arg2:" + arg2);
				mListView.onRefreshComplete();
				if (arg2 == null) {
					handler.obtainMessage(MEG_WHAT_TOATS, "获取系统消息失败:").sendToTarget();
					return;
				}
				if (arg0 == 200) {
					Type type = new TypeToken<TJResponse<TJMessageList>>() {
					}.getType();
					TJResponse<TJMessageList> response = new Gson().fromJson(arg2, type);
					if (response == null || response.getResult() == null || response.getData() == null) {
						handler.obtainMessage(MEG_WHAT_TOATS, "获取系统消息失败:").sendToTarget();
						return;
					}
					if (response.getResult().getCode() == 0) {
						if (response.getData().getNotices() != null) {
							Log.d(TAG, response.getData().getNotices().toString());
							addMessage((List<TJMessage>) response.getData().getNotices());
							refreshUI();
						}
					} else {
						handler.obtainMessage(MEG_WHAT_TOATS, "获取系统消息失败:" + response.getResult().getMessage()).sendToTarget();
					}
				} else {
					handler.obtainMessage(MEG_WHAT_TOATS, "获取系统消息失败:" + arg0).sendToTarget();
				}
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				mListView.onRefreshComplete();
				handler.obtainMessage(MEG_WHAT_TOATS, "获取系统消息失败" + arg0).sendToTarget();
			}
		});
	}

	/**
	 * 获取系统消息监听
	 * 
	 * see {@link EMNotifierEvent}
	 */
	@Override
	public void onEvent(EMNotifierEvent event) {
		switch (event.getEvent()) {
		case EventNewMessage: {
			HXSDKHelper.getInstance().getNotifier().onNewMsg((EMMessage) event.getData());
			refreshUI();
			break;
		}
		case EventOfflineMessage: {
			List<EMMessage> messages = (List<EMMessage>) event.getData();
			HXSDKHelper.getInstance().getNotifier().onNewMesg(messages);
			refreshUI();
			break;
		}
		default:
			break;
		}
	}

	/* 刷新页面 */
	private void refreshUI() {
		if (mListAdapter != null) {
			mListAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("系统消息");
		//友盟用户统计
		MobclickAgent.onResume(this);
		refreshUI();
		GWHXSDKHelper sdkHelper = (GWHXSDKHelper) GWHXSDKHelper.getInstance();
		if (sdkHelper == null) {
			System.out.println("test null");
		}
		sdkHelper.pushActivity(this);
		// register the event listener when enter the foreground
		EMChatManager.getInstance().registerEventListener(
				this,
				new EMNotifierEvent.Event[] { EMNotifierEvent.Event.EventNewMessage, EMNotifierEvent.Event.EventOfflineMessage, EMNotifierEvent.Event.EventDeliveryAck,
						EMNotifierEvent.Event.EventReadAck });
	}

	@Override
	public void onStop() {
		// unregister this event listener when this activity enters the
		// background
		EMChatManager.getInstance().unregisterEventListener(this);

		GWHXSDKHelper sdkHelper = (GWHXSDKHelper) GWHXSDKHelper.getInstance();

		// 鎶婃activity 浠巉oreground activity 鍒楄〃閲岀Щ闄�
		sdkHelper.popActivity(this);

		super.onStop();
	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case MEG_WHAT_TOATS:
				ToastUtils.show(SystemMsgActivity.this, (String) msg.obj);
				break;

			default:
				break;
			}
		}

	};

	public class MsgDialogFragment extends DialogFragment {
		private Integer mPosition;
		private TextView mDeleteTextView;

		public MsgDialogFragment(Integer positon) {
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
