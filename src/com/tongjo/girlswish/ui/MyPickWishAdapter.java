package com.tongjo.girlswish.ui;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tongjo.bean.TJWish;
import com.tongjo.girlswish.R;

class MyPickWishAdapter extends RecyclerView.Adapter<MyPickWishAdapter.ViewHolder> {
	private Logger logger=LoggerFactory.getLogger(MyPickWishAdapter.class);
	private Context Mcontext;
	private List<TJWish> wish;

	public MyPickWishAdapter(Context mcontext, List<TJWish> list) {
		super();
		Mcontext = mcontext;
		this.wish = list;
	}

	@Override
	public int getItemCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void onBindViewHolder(ViewHolder arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	public static class ViewHolder extends RecyclerView.ViewHolder {
		// Your holder should contain a member variable
		// for any view that will be set as you render a row
		public TextView tvName;
		public TextView tvHometown;

		// We also create a constructor that accepts the entire item row
		// and does the view lookups to find each subview
		public ViewHolder(View itemView) {
			super(itemView);
		}
	}

}
