package com.tongjo.girlswish.ui;

import com.tongjo.girlswish.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 心愿墙对应的fragment
 * Copyright 2015 
 * @author preparing
 * @date 2015-6-14
 */
public class MainTabWishFragment extends BaseFragment{
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        return rootView;
    }
}
