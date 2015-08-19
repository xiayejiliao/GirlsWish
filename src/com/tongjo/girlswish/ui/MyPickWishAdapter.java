package com.tongjo.girlswish.ui;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import u.aly.v;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.tongjo.bean.TJWish;
import com.tongjo.girlswish.R;
/**
 * 
* @Description: 使用 recyclerview
* @author 16ren 
* @date 2015年8月18日 下午3:49:24 
*
 */
class MyPickWishAdapter extends RecyclerView.Adapter<MyPickWishAdapter.ViewHolder> {
	private Logger logger = LoggerFactory.getLogger(MyPickWishAdapter.class);
	private Context Mcontext;
	private List<TJWish> wish;
	private DisplayImageOptions displayImageOptions;

	public MyPickWishAdapter(Context mcontext, List<TJWish> list) {
		super();
		Mcontext = mcontext;
		this.wish = list;
		displayImageOptions = new DisplayImageOptions.Builder().showStubImage(R.drawable.ic_broken_image_black_24dp) // 设置图片下载期间显示的图片
				.showImageForEmptyUri(R.drawable.ic_broken_image_black_24dp) // 设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(R.drawable.ic_broken_image_black_24dp) // 设置图片加载或解码过程中发生错误显示的图片
				.cacheInMemory(false) // 设置下载的图片是否缓存在内存中
				.cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
				.displayer(new RoundedBitmapDisplayer(180))// 设置成圆角图片
				.imageScaleType(ImageScaleType.EXACTLY).build(); // 创建配置过得DisplayImageOption对象

	}

	@Override
	public int getItemCount() {
		if (wish == null) {
			return 0;
		} else {
			return wish.size();
		}
	}

	@Override
	public void onBindViewHolder(final ViewHolder arg0, int arg1) {
		// TODO Auto-generated method stub
		final TJWish tjWish = wish.get(arg1);
		arg0.tvNick.setText(tjWish.getCreatorUser().getNickname());
		arg0.tvSchool.setText(tjWish.getCreatorUser().getSchool().getName());
		ImageLoader.getInstance().displayImage(tjWish.getCreatorUser().getAvatarUrl(), arg0.ivicon, displayImageOptions);
		String temp="     ";
		SpannableString content = new SpannableString(temp+tjWish.getContent());
		content.setSpan(new UnderlineSpan(),temp.length(), content.length(), 0);
		arg0.tvcontent.setText(content);
		arg0.ivtalk.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 启动聊天窗口
				Intent intent=new Intent(Mcontext, ChatActivity.class);
				intent.putExtra("chatToUser", tjWish.getCreatorUser());
				Mcontext.startActivity(intent);
			}
		});
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
		View v = LayoutInflater.from(arg0.getContext()).inflate(R.layout.listitem_mywish, arg0, false);
		ViewHolder viewHolder = new ViewHolder(v);
		return viewHolder;
	}

	public static class ViewHolder extends RecyclerView.ViewHolder {
		// Your holder should contain a member variable
		// for any view that will be set as you render a row
		public ImageView ivicon;
		public ImageView ivtalk;
		public TextView tvNick;
		public TextView tvSchool;
		public TextView tvcontent;
		public RelativeLayout rLayout;

		// We also create a constructor that accepts the entire item row
		// and does the view lookups to find each subview
		public ViewHolder(View itemView) {
			super(itemView);
			ivtalk = (ImageView) itemView.findViewById(R.id.iv_mypickwish_talk);
			ivicon = (ImageView) itemView.findViewById(R.id.iv_mypickwish_icon);
			tvNick = (TextView) itemView.findViewById(R.id.tv_mypickwish_nick);
			tvSchool = (TextView) itemView.findViewById(R.id.tv_mypickwish_school);
			tvcontent = (TextView) itemView.findViewById(R.id.tv_mypickwish_content);
			rLayout = (RelativeLayout) itemView.findViewById(R.id.rl_mypickwish_item);
		}
	}

}
