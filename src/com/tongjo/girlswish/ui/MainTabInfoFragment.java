package com.tongjo.girlswish.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tongjo.girlswish.R;
import com.tongjo.girlswish.widget.RefreshableView;
import com.tongjo.girlswish.widget.RefreshableView.PullToRefreshListener;
import com.tongjo.girlswish.widget.SlideListView;
import com.tongjo.girlswish.widget.SlideListView.RemoveDirection;
import com.tongjo.girlswish.widget.SlideListView.RemoveListener;

import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class MainTabInfoFragment extends BaseFragment {
	SlideListView mListView;
	SimpleAdapter mListAdapter;
	RefreshableView mRefreshableView;
	boolean mIsRefreshing;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_info, container, false);
		initView(view);
		return view;
	}

	private void initView(View view) {
		mListView = (SlideListView) view.findViewById(R.id.list);
		mRefreshableView = (RefreshableView) view.findViewById(R.id.refreshable_view);
		mListAdapter = new SimpleAdapter(this.getActivity(), this.getData(),
				R.layout.info_list_item, new String[] { "icon", "title", "msg",
						"time" }, new int[] { R.id.info_list_icon,
						R.id.info_list_title, R.id.info_list_msg,
						R.id.info_list_time });
		mListView.setAdapter(mListAdapter);

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
				return false;
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
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				mRefreshableView.finishRefreshing();
				mIsRefreshing = false;
			}
		}, 0);
		// 注册上下文菜单
		registerForContextMenu(mListView);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		if(mIsRefreshing){
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

	private List<Map<String, Object>> getData() {

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		for (int i = 0; i < 20; i++) {
			if (i % 3 == 0) {
				map.put("icon", R.drawable.default_qq_avatar);
				map.put("title", "腾讯新闻");
				map.put("msg", "青岛爆炸满月：大量鱼虾死亡");
				map.put("time", "晚上18:18");
			} else {
				map.put("icon", R.drawable.wechat_icon);
				map.put("title", "微信团队");
				map.put("msg", "欢迎你使用微信");
				map.put("time", "12月18日");
			}
			list.add(map);
		}

		return list;
	}
}
