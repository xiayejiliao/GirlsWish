package com.tongjo.girlswish.ui;

import com.tongjo.girlswish.R;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

public class WorkingDialog extends DialogFragment {
	private String info;
	private TextView tv_info;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE); 
		View view=inflater.inflate(R.layout.fragment_working, container);
		tv_info=(TextView) view.findViewById(R.id.tv_fragwork_info);
		tv_info.setText(info);
		return view;
	}
	
	public WorkingDialog() {
		super();
	}

	public WorkingDialog(String mesg) {
		super();
		this.info = mesg;
	}
	public void setInfo(String mesg) {
		this.info=mesg;
		if(tv_info!=null){
			tv_info.setText(info);
		}
	}
}
