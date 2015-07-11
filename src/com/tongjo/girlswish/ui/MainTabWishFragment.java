package com.tongjo.girlswish.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ListView;

import com.tongjo.bean.TJWish;
import com.tongjo.girlswish.R;
import com.tongjo.girlswish.ui.MainTabWishAdapter.MItemClickListener;
import com.tongjo.girlswish.ui.MainTabWishAdapter.MItemLongPressListener;
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
	private RefreshableView mRefreshView = null;
	private ListView mListView = null;
	private MainTabWishAdapter mAdapter = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_wish, container,
				false);
		
		InitView(rootView);
		MockData();
		return rootView;
	}
	
	public void InitView(View view){
		mRefreshView = (RefreshableView)view.findViewById(R.id.refresh_view);
		mListView = (ListView)view.findViewById(R.id.listview); 
		mAdapter = new MainTabWishAdapter(getActivity(),DataContainer.WishList);
		mListView.setAdapter(mAdapter);
		mAdapter.setMItemClickListener(new MItemClickListener(){

			@Override
			public void MItemClick(View v, int position) {
				WishDialogFragment dialog  = new WishDialogFragment();
				dialog.show(getFragmentManager(), "WishDialogFragment");  
			}
			
		});
		
		mAdapter.setMItemLongPressListener(new MItemLongPressListener(){

			@Override
			public void MItemLongPress(View v, int position) {
				final CustomeProgressDialog dialog = new CustomeProgressDialog(getActivity(),"正在加载中");
				dialog.show();
				new Handler().postDelayed(new Runnable(){

					@Override
					public void run() {
						dialog.dismiss();
					}
					
				}, 3000);
			}
			
		});
		
		mRefreshView.setOnRefreshListener(new PullToRefreshListener() {
			@Override
			public void onRefresh() {
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				mRefreshView.finishRefreshing();
			}
		}, 0);
		
	}
	
	public void MockData(){
		for(int i = 0; i< 5; i++){
			TJWish wish = new TJWish();
			DataContainer.WishList.add(wish);
		}
		mAdapter.setList(DataContainer.WishList);
	}
	
	@SuppressLint("ValidFragment")
	public class WishDialogFragment extends DialogFragment{
		/*@Override  
	    public Dialog onCreateDialog(Bundle savedInstanceState)  {  
	        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());  
	        LayoutInflater inflater = getActivity().getLayoutInflater();  
	        View view = inflater.inflate(R.layout.fragment_wishdetail, null);  
	        builder.setView(view);         
	        return builder.create();  
	    }  */
		
		@Override  
	    public View onCreateView(LayoutInflater inflater, ViewGroup container,  
	            Bundle savedInstanceState)  {  
			getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);  
	        View view = inflater.inflate(R.layout.fragment_wishdetail, container);  
	        return view;  
	    }  
	}
}
