package com.tongjo.girlswish.ui;

import com.tongjo.bean.TJWish;
import com.tongjo.girlswish.R;
import com.tongjo.girlswish.widget.RefreshableView;
import com.tongjo.girlwish.data.DataContainer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * 心愿墙对应的fragment Copyright 2015
 * 
 * @author preparing
 * @date 2015-6-14
 */
public class MainTabWishFragment extends BaseFragment {
	private RefreshableView mRefreshView = null;
	private ListView mListView = null;
	private MainTabWishAdapter mAdapter = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_wish, container,
				false);
		
		InitView(rootView);
		MorkData();
		return rootView;
	}
	
	public void InitView(View view){
		mRefreshView = (RefreshableView)view.findViewById(R.id.refresh_view);
		mListView = (ListView)view.findViewById(R.id.listview); 
		mAdapter = new MainTabWishAdapter(getActivity(),DataContainer.WishList);
		mListView.setAdapter(mAdapter);
		
	}
	
	public void MorkData(){
		for(int i = 0; i< 5; i++){
			TJWish wish = new TJWish();
			DataContainer.WishList.add(wish);
		}
		mAdapter.setList(DataContainer.WishList);
	}
}
