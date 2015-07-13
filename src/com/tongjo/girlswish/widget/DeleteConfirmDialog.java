package com.tongjo.girlswish.widget;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.tongjo.girlswish.R;

public class DeleteConfirmDialog extends DialogFragment {	
	private static DialogClickListener mListener;
	private String title;
	
	private TextView TitleView;
	private Button button1;
	private Button button2;
	
	public DeleteConfirmDialog() {}

	public static DeleteConfirmDialog newInstance(String title,String positive,String negative,DialogClickListener listener) {
		DeleteConfirmDialog frag = new DeleteConfirmDialog();
		Bundle b = new Bundle();
		b.putString("title", title);
		b.putString("positive", positive);
		b.putString("negative", negative);
		frag.setArguments(b);
		mListener = listener;
		return frag;
	}

	public void setTitle(String mTitle){
		title = mTitle;
		if (title != null && title.length() > 0) {
			TitleView.setText(title);
		}else{
			TitleView.setText("无标题");
		}
	}
	
	/**
	 * 设置第一个按钮显示的文字
	 * @param text
	 */
	public void setPositiveBtnText(String text){
		if (text != null && text.length() > 0) {
			button2.setText(text);
		}
	}
	
	/**
	 * 设置第一个按钮显示的文字
	 * @param text
	 */
	public void setNegativeBtnText(String text){
		if (text != null && text.length() > 0) {
			button1.setText(text);
		}
	}
	
	public String getTitle(){
		return this.title;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		final Dialog dialog = new Dialog(getActivity(),R.style.MsgDialogStyle);
		LayoutInflater inflater = LayoutInflater.from(getActivity());
		View view = inflater.inflate(R.layout.dialog_delete, null, false);

		TitleView = (TextView) view.findViewById(R.id.dialog_title);
		String titleStr = getArguments().getString("title");
		String positive = getArguments().getString("positive");
		String negative = getArguments().getString("negative");
		title = titleStr;
		setTitle(title);

		button1 =(Button) view.findViewById(R.id.dialog_button1);
		button2 = (Button) view.findViewById(R.id.dialog_button2);
		setPositiveBtnText(positive);
		setNegativeBtnText(negative);
		
		button1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				if (mListener != null) {
					mListener.doPositiveClick();
				}
			}
		});

		button2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				if (mListener != null) {
					mListener.doNegativeClick();
				}
			}
		});
		dialog.setContentView(view);
		return dialog;
	}
	
	/**set a interface for other class to use*/
	public void setDialogClickListener(DialogClickListener listener) {
		mListener = listener;
	}
	
	public static interface DialogClickListener {
		public void doPositiveClick();
		public void doNegativeClick();
	}
}