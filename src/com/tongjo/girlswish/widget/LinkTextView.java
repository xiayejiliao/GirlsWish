package com.tongjo.girlswish.widget;

import com.tongjo.girlswish.R;

import android.R.integer;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.widget.TextView;
import android.view.View;
import android.view.View.OnClickListener;;
/**
 *带下划线的TextView
 *在布局文件中使用，
 *linktext属性配置带下划线的文本
 *clickcolor配置点击后的文本颜色
 * 
 * @author 16ren
 * @date 2015-6-12
 */
public class LinkTextView extends TextView {
	private int clickcolor;
	private int color;
	private String text;
	private SpannableString spannableString;
	public LinkTextView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context, attrs);
	}
	public LinkTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	public LinkTextView(Context context) {
		super(context);
		getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		// TODO Auto-generated constructor stub
	}
	/*设置文本，及点击后的颜色*/
	public void setLinkText(String text, final int clickcolor){
		this.text=text;
		this.clickcolor=clickcolor;
		spannableString=new SpannableString(text);
		color=getCurrentTextColor();
		spannableString.setSpan(new ClickableSpan() {
		
			@Override
			public void onClick(View widget) {
				spannableString.setSpan(new ForegroundColorSpan(clickcolor), 0, spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
				setText(spannableString);
			}
		}, 0, spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
		spannableString.setSpan(new ForegroundColorSpan(color), 0, spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
		this.setText(spannableString);
	}
	private void init(Context context,AttributeSet attrs){
		TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.LinkTextView); 
		text= a.getString(R.styleable.LinkTextView_linktext);
		clickcolor= a.getColor(R.styleable.LinkTextView_clickcolor, Color.BLACK);
		a.recycle();
		color=getCurrentTextColor();
		spannableString=new SpannableString(text);
		spannableString.setSpan(new ClickableSpan() {
			@Override
			public void onClick(View widget) {
				spannableString.setSpan(new ForegroundColorSpan(clickcolor), 0, spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
				setText(spannableString);
			}
		}, 0, spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
		spannableString.setSpan(new ForegroundColorSpan(color), 0, spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
		this.setText(spannableString);
		setMovementMethod(LinkMovementMethod.getInstance());
	}
	
}
