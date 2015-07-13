package com.tongjo.girlswish.ui;

import com.tongjo.bean.TJWish;
import com.tongjo.girlswish.R;
import com.tongjo.girlswish.widget.CustomeProgressDialog;
import com.tongjo.girlswish.widget.RefreshableView;
import com.tongjo.girlwish.data.DataContainer;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
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
		View rootView = inflater.inflate(R.layout.fragment_boywish, container,
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
		mListView.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				WishDialogFragment dialog  = new WishDialogFragment();
				dialog.show(getFragmentManager(), "WishDialogFragment");  
			}
		});
		
		mListView.setOnItemLongClickListener(new OnItemLongClickListener(){

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				
				final CustomeProgressDialog dialog = new CustomeProgressDialog(getActivity(),"正在加载中");
				dialog.show();
				new Handler().postDelayed(new Runnable(){

					@Override
					public void run() {
						dialog.dismiss();
					}
					
				}, 3000);
				
				return false;
			}
			
		});
		
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
