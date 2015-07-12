package com.tongjo.girlswish.ui;

import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import org.apache.http.Header;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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
import com.tongjo.girlswish.utils.AppConstant;
import com.tongjo.girlswish.utils.ToastUtils;
import com.tongjo.girlswish.widget.RefreshableView;
import com.tongjo.girlswish.widget.RefreshableView.PullToRefreshListener;
import com.tongjo.girlswish.widget.SlideListView;
import com.tongjo.girlswish.widget.SlideListView.RemoveDirection;
import com.tongjo.girlswish.widget.SlideListView.RemoveListener;
import com.tongjo.girlwish.data.DataContainer;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class MainTabInfoFragment extends BaseFragment {
	private static String TAG = "MainTabInfoFragment";
	private SlideListView mListView;
	private MainTabInfoAdapter mListAdapter = null;
	private RefreshableView mRefreshableView;
	private Dao<TJMessage, UUID> mTJMessageDao;
	WishDialogFragment menuDialog = null;
	boolean mIsRefreshing;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_info, container, false);
		initView(view);
		return view;
	}

	private void initView(View view) {
		mListView = (SlideListView) view.findViewById(R.id.info_listview);
		mRefreshableView = (RefreshableView) view
				.findViewById(R.id.info_refreshable_view);
		mListAdapter = new MainTabInfoAdapter(getActivity(),
				DataContainer.MessageList);
		// MockData();
		mListView.setAdapter(mListAdapter);
		getMessageData();

		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

			}
		});
		mListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (!mIsRefreshing) {
					menuDialog = new WishDialogFragment(position);
					menuDialog.show(getFragmentManager(), "WishDialogFragment");
					return false;
				}
				return true;
			}
		});
		mListView.setRemoveListener(new RemoveListener() {

			public void removeItem(RemoveDirection direction, int position) {

			}
		});
		mRefreshableView.setOnRefreshListener(new PullToRefreshListener() {
			@Override
			public void onRefresh() {
				try {
					mIsRefreshing = true;
					if (menuDialog != null && menuDialog.isVisible()) {
						menuDialog.dismiss();
					}
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				mRefreshableView.finishRefreshing();
				mIsRefreshing = false;
			}
		}, 0);
		// 注册上下文菜单
		// registerForContextMenu(view);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		if (mIsRefreshing) {
			return;
		}
		// 添加菜单项
		menu.add(0, Menu.FIRST, 0, "删除");
		super.onCreateContextMenu(menu, v, menuInfo);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		Toast.makeText(this.getActivity(),
				"content" + item.getItemId() + info.position, Toast.LENGTH_LONG)
				.show();
		if (item.getItemId() == Menu.FIRST) {

		}
		return super.onContextItemSelected(item);
	}

	private void MockData() {

		for (int i = 0; i < 5; i++) {
			TJMessage message = new TJMessage();
			message.setType(1);
			message.setTime("12-8");
			message.setContent("青岛爆炸满月：大量鱼虾死亡");
			DataContainer.MessageList.add(message);
		}
		mListAdapter.setList(DataContainer.MessageList);
	}

	private List<TJMessage> selectData() {
		try {
			mTJMessageDao = new OrmLiteHelper(this.getActivity())
					.getTJMessageDao();
			QueryBuilder<TJMessage, UUID> builder = mTJMessageDao
					.queryBuilder();
			builder.orderBy("time", false);
			PreparedQuery<TJMessage> preparedQuery = builder.prepare();
			return mTJMessageDao.query(preparedQuery);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取消息列表
	 */
	public void getMessageData() {
		RequestParams requestParams = new RequestParams();
		requestParams.add("page", "0");
		asyncHttpClient.get(AppConstant.URL_BASE + AppConstant.URL_MESSAGE,
				requestParams, new TextHttpResponseHandler("UTF-8") {

					@Override
					public void onSuccess(int arg0, Header[] arg1, String arg2) {
						mRefreshableView.finishRefreshing();
						if (arg2 == null) {
							ToastUtils.show(getActivity(), "消息列表获取失败:");
							return;
						}
						if (arg0 == 200) {
							Type type = new TypeToken<TJResponse<TJMessageList>>() {
							}.getType();
							TJResponse<TJMessageList> response = new Gson()
									.fromJson(arg2, type);
							if (response == null
									|| response.getResult() == null
									|| response.getData() == null) {
								ToastUtils.show(getActivity(), "消息列表获取失败:");
								return;
							}
							if (response.getResult().getCode() == 0) {
								if (response.getData().getMessageList() != null) {
									Log.d(TAG, response.getData()
											.getMessageList().toString());
									DataContainer.MessageList
											.addAll((List<TJMessage>) response
													.getData().getMessageList());
									mListAdapter.notifyDataSetChanged();
								}
							} else {
								ToastUtils.show(getActivity(), "消息列表获取失败:"
										+ response.getResult().getMessage());
							}
						} else {
							ToastUtils.show(getActivity(), "消息列表获取失败" + arg0);
						}
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, String arg2,
							Throwable arg3) {
						mRefreshableView.finishRefreshing();
						Toast.makeText(getActivity(), "消息列表获取失败" + arg0,
								Toast.LENGTH_LONG).show();
					}
				});
	}

	public void deleteMessage(int itemId) {
		if (itemId >= 0 && itemId < DataContainer.MessageList.size()) {
			DataContainer.MessageList.remove(itemId);
		}
	}

	public class WishDialogFragment extends DialogFragment {
		private Integer mPosition;
		private TextView mDeleteTextView;

		public WishDialogFragment(Integer positon) {
			this.mPosition = positon;
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
			View view = inflater
					.inflate(R.layout.fragment_info_menu, container);
			mDeleteTextView = (TextView) view
					.findViewById(R.id.infomenu_delete);
			mDeleteTextView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					deleteMessage(mPosition);
				}
			});
			return view;
		}
	}

}
