package com.tongjo.girlswish.ui;

import java.util.List;

import com.tongjo.bean.TJWish;
import com.tongjo.girlswish.R;
import com.tongjo.girlswish.model.UserSex;



import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class MeWishFragment extends BaseFragment {
	private List<TJWish> tJWishs;
	private ListView lv_mewhis;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view=inflater.inflate(R.id.lv_fragmewhish_wish, null);
		return view;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	public MeWishFragment(List<TJWish> tJWishs) {
		super();
		this.tJWishs = tJWishs;
	}

}
