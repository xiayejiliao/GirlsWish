package com.tongjo.girlswish.ui;

import java.lang.reflect.Type;
import java.sql.SQLException;
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
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.tongjo.bean.TJMessage;
import com.tongjo.bean.TJMessageList;
import com.tongjo.bean.TJResponse;
import com.tongjo.db.OrmLiteHelper;
import com.tongjo.girlswish.R;
import com.tongjo.girlswish.ui.MainTabInfoAdapter.MItemClickListener;
import com.tongjo.girlswish.ui.MainTabInfoAdapter.MItemLongPressListener;
import com.tongjo.girlswish.utils.AppConstant;
import com.tongjo.girlswish.utils.ToastUtils;
import com.tongjo.girlwish.data.DataContainer;

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
	private final static int MEG_WHAT_TOATS=10010;
	private static String TAG = "MainTabInfoFragment";
	//private SlideListView mListView;
	private MainTabInfoAdapter mListAdapter = null;
	//private RefreshableView mRefreshableView;
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
		mEmptyView = (ViewGroup)view.findViewById(R.id.info_empty_view);
		mListView = (PullToRefreshListView) view.findViewById(R.id.info_listview);
		//mRefreshableView = (RefreshableView) view.findViewById(R.id.info_refreshable_view);
		mListAdapter = new MainTabInfoAdapter(getActivity(), DataContainer.MessageList);
		//MockData();
		selectData();
		mListView.setAdapter(mListAdapter);
		updateUi();

		mListAdapter.setMItemClickListener(new MItemClickListener() {

			@Override
			public void MItemClick(View v, int position) {
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
		
		mListView.setMode(Mode.BOTH);
		mListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(getActivity(),
						System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
								| DateUtils.FORMAT_SHOW_DATE
								| DateUtils.FORMAT_ABBREV_ALL);
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

	private void MockData() {

		for (int i = 0; i < 5; i++) {
			TJMessage message = new TJMessage();
			message.setCreatedTime("12-8");
			message.setContent("青岛爆炸满月：大量鱼虾死亡");
			DataContainer.MessageList.add(message);
		}
		mListAdapter.setList(DataContainer.MessageList);
	}

	private void selectData() {
		try {
			Dao<TJMessage, UUID> mTJMessageDao = new OrmLiteHelper(this.getActivity()).getTJMessageDao();
			QueryBuilder<TJMessage, UUID> builder = mTJMessageDao.queryBuilder();
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

	public void addMessage(List<TJMessage> messageList) {
		Dao<TJMessage, UUID> mTJMessageDao = new OrmLiteHelper(this.getActivity()).getTJMessageDao();
		for (TJMessage message : messageList) {
			try {
				mTJMessageDao.createIfNotExists(message);
			} catch (SQLException e) {
				Log.e(TAG, e.getStackTrace().toString());
			}
		}
	}

	public void updateUi(){
		if(DataContainer.WishList.size() <1){
			mEmptyView.setVisibility(View.VISIBLE);
		}else{
			mEmptyView.setVisibility(View.GONE);
			mListAdapter.notifyDataSetChanged();
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
					handler.obtainMessage(MEG_WHAT_TOATS,"消息列表获取失败:").sendToTarget();
					return;
				}
				if (arg0 == 200) {
					Type type = new TypeToken<TJResponse<TJMessageList>>() {
					}.getType();
					TJResponse<TJMessageList> response = new Gson().fromJson(arg2, type);
					if (response == null || response.getResult() == null || response.getData() == null) {
						handler.obtainMessage(MEG_WHAT_TOATS,"消息列表获取失败:").sendToTarget();
						return;
					}
					if (response.getResult().getCode() == 0) {
						if (response.getData().getNotices() != null) {
							Log.d(TAG, response.getData().getNotices().toString());
							//加入数据库
							addMessage((List<TJMessage>) response.getData().getNotices());
							//加入内存List
							DataContainer.MessageList.addAll((List<TJMessage>) response.getData().getNotices());
							updateUi();
						}
					} else {
						handler.obtainMessage(MEG_WHAT_TOATS,"消息列表获取失败:" + response.getResult().getMessage()).sendToTarget();
					}
				} else {
					handler.obtainMessage(MEG_WHAT_TOATS,"消息列表获取失败:"  + arg0).sendToTarget();
				}
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				mListView.onRefreshComplete();
				handler.obtainMessage(MEG_WHAT_TOATS,"消息列表获取失败" + arg0).sendToTarget();
			}
		});
	}
	private Handler handler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case MEG_WHAT_TOATS:
				ToastUtils.show(getContext(), (String)msg.obj);
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
					menuDialog.dismiss();
				}
			});
			return view;
		}
	}

}
