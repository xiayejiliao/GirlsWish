package com.tongjo.girlswish.widget;

import com.tongjo.girlswish.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

/**
 * 作为一个可以存放多个按钮的的Layout 自动匹配上、下、左、右对应的边框
 * 使用时直接将这个当成ViewGroup,里面所有的一级View自动进行背景填充与边框填充
 * 背景修改与替换会在后续完成
 *  Copyright 2015
 * @author preparing
 * @date 2015-5-31
 */
public class ArrayLayout extends LinearLayout {
	private Context mContext;

	public ArrayLayout(Context context) {
		super(context);
		mContext = context;
	}

	public ArrayLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		mContext = context;
	}

	
	public ArrayLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		this.setBackground(context.getResources().getDrawable(
				R.drawable.rc_co6666_pd1111_lwhite));
	}
	

	/**
	 * XML文件加载完成后的回调函数
	 * 不能直接写在构造函数了，因为在构造函数里面，返回的getChildCount()为0
	 * 也不能写在onLayout（）和onDraw（）中，会造成死循环
	 * 注意LayoutInflater.from(mContext).inflate(R.layout.view_line,null)与LayoutInflater.from(mContext).inflate(R.layout.view_line,this，true)的区别
	 * 后一个是指定了父类的，直接就添加到ViewGroup中的，不需要再添加view
	 */
	@Override
	public void onFinishInflate(){
		//用于记录已经添加的View的个数，因为前面添加后，后面的View个数其实也变了
		int addViewCount = 0;
		int childCount = this.getChildCount();
		System.out.println("childCount:" + childCount);
		if (childCount == 0) {
			return;
		} else if (childCount == 1) {
			this.getChildAt(0).setBackground(
					mContext.getResources().getDrawable(
							R.drawable.btn_rectangle_top_bottom));
		} else if (childCount > 1) {
			for (int i = 0; i < childCount; i++) {
				if (i == 0) {
					this.getChildAt(i+addViewCount).setBackground(
							mContext.getResources().getDrawable(
									R.drawable.btn_rectangle_top));
				} else if (i == childCount - 1) {
					View mView = LayoutInflater.from(mContext).inflate(
							R.layout.view_line,null);
					this.addView(mView, i+addViewCount);
					addViewCount ++;
					this.getChildAt(i+addViewCount).setBackground(
							mContext.getResources().getDrawable(
									R.drawable.btn_rectangle_bottom));
					
				} else {
					View mView = LayoutInflater.from(mContext).inflate(
							R.layout.view_line,null);
					this.addView(mView, i+addViewCount);
					addViewCount ++;
					this.getChildAt(i+addViewCount).setBackground(
							mContext.getResources().getDrawable(
									R.drawable.btn_rectangle));
					
				}
			}
		}
	}
}
