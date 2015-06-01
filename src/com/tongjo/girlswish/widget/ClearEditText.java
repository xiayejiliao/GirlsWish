package com.tongjo.girlswish.widget;

import com.tongjo.girlswish.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

/**
 * 右边带有清除已输入信息的EditText 使用方法和EditText一直，不过目前支持的XML中属性配置有限，后续增加
 * 使用时记得加入组件的报名：xmlns:tongjo="http://schemas.android.com/apk/res/com.tongjo.girlswish"
 * 可以使用的属性有textSize,textcolor,hint,password
 * Copyright 2015
 * 
 * @author preparing
 * @date 2015-5-31
 */
public class ClearEditText extends LinearLayout implements TextWatcher {
	private final static String TAG = "ClearEditText";
	private Context mContext;
	private Button mDelete;
	private EditText mEditText;

	public ClearEditText(Context context) {
		super(context);
		mContext = context;
	}

	public ClearEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		init(attrs);
	}

	private void init(AttributeSet attrs) {
		View mView = LayoutInflater.from(mContext).inflate(
				R.layout.widget_clearedittext, this, true);
		mDelete = (Button) mView.findViewById(R.id.clear_delete);
		mEditText = (EditText) mView.findViewById(R.id.clear_edittext);
		mEditText.addTextChangedListener(this);
		if (mEditText.getText().toString().trim().equals("")) {
			mDelete.setVisibility(View.GONE);
		} else {
			mDelete.setVisibility(View.VISIBLE);
		}
		mDelete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (mEditText != null) {
					mEditText.setText("");
				}
			}
		});

		TypedArray typeArray = mContext.obtainStyledAttributes(attrs,
				R.styleable.ClearEditText);

		int N = typeArray.getIndexCount();
		for (int i = 0; i < N; i++) {
			int attr = typeArray.getIndex(i);
			switch (attr) {
			case R.styleable.ClearEditText_textColor: {
				int textColor = typeArray.getColor(
						R.styleable.ClearEditText_textColor, Color.BLACK);
				mEditText.setTextColor(textColor);
				break;
			}
			case R.styleable.ClearEditText_textSize: {
				float textSize = typeArray.getDimension(
						R.styleable.ClearEditText_textSize, 24);
				mEditText.setTextSize(textSize);
				break;
			}
			case R.styleable.ClearEditText_hint: {
				int resouceId = -1;
				resouceId = typeArray.getResourceId(
						R.styleable.ClearEditText_hint, 0);
				mEditText.setHint(resouceId > 0 ? typeArray.getResources()
						.getText(resouceId) : typeArray
						.getString(R.styleable.ClearEditText_hint));
				break;
			}
			case R.styleable.ClearEditText_password: {
				boolean password = typeArray.getBoolean(
						R.styleable.ClearEditText_password, false);
				setPassword(password);
				break;
			}
			}
		}

		typeArray.recycle();
	}

	public String getText() {
		if (mEditText != null) {
			return mEditText.getText().toString().trim();
		} else {
			return "";
		}
	}

	public void setText(String args) {
		if (mEditText != null) {
			mEditText.setText(args);
		}
	}

	/** 设置明文显示还是密文显示 */
	public void setPassword(boolean bool) {
		if (mEditText != null) {
			if (bool)
				mEditText.setTransformationMethod(PasswordTransformationMethod
						.getInstance());
			else {
				mEditText
						.setTransformationMethod(HideReturnsTransformationMethod
								.getInstance());
			}
		}
	}

	/** 一下三个函数为TextWatcher中继承的函数 */
	@Override
	public void afterTextChanged(Editable arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
			int arg3) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		if (mEditText.getText().toString().trim().length() > 0) {
			mDelete.setVisibility(View.VISIBLE);
		} else {
			mDelete.setVisibility(View.GONE);
		}
	}
}