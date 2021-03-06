package com.tongjo.girlswish.ui;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.http.Header;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import u.aly.v;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.tongjo.bean.TJMessage;
import com.tongjo.bean.TJResponse;
import com.tongjo.bean.TJWish;
import com.tongjo.emchat.MessageUtils;
import com.tongjo.girlswish.BaseApplication;
import com.tongjo.girlswish.R;
import com.tongjo.girlswish.event.WishDelete;
import com.tongjo.girlswish.utils.AppConstant;
import com.tongjo.girlswish.utils.TimeUtils;
import com.tongjo.girlswish.utils.ToastUtils;
import com.tongjo.girlwish.data.DataContainer;

import de.greenrobot.event.EventBus;

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

	public MyPickWishAdapter(Context mcontext, List<TJWish> list) {
		super();
		Mcontext = mcontext;
		this.wish = list;

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
	public void onBindViewHolder(final ViewHolder holder, int arg1) {
		// TODO Auto-generated method stub
		final TJWish tjWish = wish.get(arg1);

		if (tjWish.getCreatorUser() != null) {
			holder.tvNick.setText(tjWish.getCreatorUser().getNickname());
			if (tjWish.getCreatorUser().getSchool() != null) {
				holder.tvSchool.setText(tjWish.getCreatorUser().getSchool().getName());
			}
			int sex= tjWish.getCreatorUser().getGender();
			if(sex==0){
				holder.ivsex.setImageResource(R.drawable.women);
			}else if ( sex==1) {
				holder.ivsex.setImageResource(R.drawable.men);
			}
			holder.tvtime.setText(TimeUtils.getdefaulttime(TimeUtils.DEFAULT_DATE_FORMAT, tjWish.getPickedTime()));
			ImageLoader.getInstance().displayImage(tjWish.getCreatorUser().getAvatarUrl(), holder.ivicon);
			holder.ivtalk.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO 启动聊天窗口
					if (tjWish.getCreatorUser() != null && tjWish.getCreatorUser().getHxid() != null) {
						Intent intent = new Intent(Mcontext, ChatActivity.class);
						intent.putExtra("toUserHxid", tjWish.getCreatorUser().getHxid());
						// 聊天记录加入列表
						/*SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
						TJMessage message = new TJMessage();
						message.setHxid(tjWish.getCreatorUser().getHxid());
						message.setCreatedTime(sdf.format(new Date()));
						message.setTitle(tjWish.getCreatorUser().getNickname());
						message.setRead(true);
						message.setAvatarUrl(tjWish.getCreatorUser().getAvatarUrl());
						message.setType(AppConstant.MSG_TYPE_CHAT);
						MessageUtils.addChatMessage(Mcontext, message);*/
						DataContainer.userInfoMap.put(tjWish.getCreatorUser().getHxid(), tjWish.getCreatorUser());
						Mcontext.startActivity(intent);
					} else {
						ToastUtils.show(Mcontext, R.string.wish_no_creater);
					}
				}
			});
			
			holder.ivicon.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) { 
					// 跳转到个人信息详情页面
					Intent intent = new Intent(Mcontext, OtherInfoActivity.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable("user", tjWish.getCreatorUser());
					intent.putExtras(bundle);
					Mcontext.startActivity(intent);
				}
			});
		}

		if (tjWish.getPickerUser() != null) {
			holder.tvtime.setText(TimeUtils.getdefaulttime(TimeUtils.DEFAULT_DATE_FORMAT, tjWish.getPickedTime()));
		}
	/*	String temp = "     ";
		SpannableString content = new SpannableString(temp + tjWish.getContent());
		content.setSpan(new UnderlineSpan(), temp.length(), content.length(), 0);*/
		holder.tvcontent.setText("      "+tjWish.getContent());

	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup arg0, final int postion) {
		View v = LayoutInflater.from(arg0.getContext()).inflate(R.layout.listitem_mypickwish, arg0, false);
		ViewHolder viewHolder = new ViewHolder(v);

		return viewHolder;
	}

	// 移除item
	public void removeAt(int position) {
		wish.remove(position);
		notifyItemRemoved(position);
		notifyItemRangeChanged(position, wish.size());
	}

	public class ViewHolder extends RecyclerView.ViewHolder {
		// Your holder should contain a member variable
		// for any view that will be set as you render a row
		public ImageView ivicon;
		public ImageView ivtalk;
		public ImageView ivsex;
		public TextView tvNick;
		public TextView tvSchool;
		public TextView tvcontent;
		public TextView tvtime;
		public RelativeLayout rLayout;

		// We also create a constructor that accepts the entire item row
		// and does the view lookups to find each subview
		public ViewHolder(View itemView) {
			super(itemView);
			ivtalk = (ImageView) itemView.findViewById(R.id.iv_mypickwish_talk);
			ivicon = (ImageView) itemView.findViewById(R.id.iv_mypickwish_icon);
			ivsex = (ImageView) itemView.findViewById(R.id.iv_mypickwish_sex);
			tvNick = (TextView) itemView.findViewById(R.id.tv_mypickwish_nick);
			tvSchool = (TextView) itemView.findViewById(R.id.tv_mypickwish_school);
			tvcontent = (TextView) itemView.findViewById(R.id.tv_mypickwish_content);
			tvtime = (TextView) itemView.findViewById(R.id.tv_mypickwish_time);
			// item 长按弹出删除对话框
			itemView.setOnLongClickListener(new OnLongClickListener() {

				@Override
				public boolean onLongClick(View v) {
					// TODO Auto-generated method stub
					new AlertDialog.Builder(Mcontext).setTitle("删除").setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							// continue with delete
							AsyncHttpClient asyncHttpClient = ((BaseApplication) Mcontext.getApplicationContext()).getAsyncHttpClient();
							RequestParams params = new RequestParams();
							params.put("_id", wish.get(getPosition()).get_id().toString());
							asyncHttpClient.post(AppConstant.URL_BASE + AppConstant.URL_WISHDEL, params, new TextHttpResponseHandler("UTF-8") {

								@Override
								public void onSuccess(int arg0, Header[] arg1, String arg2) {
									if (arg0 == 200) {
										Type type = new TypeToken<TJResponse<Object>>() {
										}.getType();
										TJResponse<Object> response = new Gson().fromJson(arg2, type);
										if (response.getResult().getCode() == 0) {
											EventBus.getDefault().post(new WishDelete(wish.get(getPosition())));
											removeAt(getPosition());
										} else {
											ToastUtils.show(Mcontext, "删除失败" + response.getResult().getMessage());
										}
									} else {
										ToastUtils.show(Mcontext, arg0);
									}

								}

								@Override
								public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
									// TODO Auto-generated method stub
									ToastUtils.show(Mcontext, arg0 + arg3.toString());
								}
							});

						}
					}).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							// do nothing
							dialog.dismiss();
						}
					}).setIcon(android.R.drawable.ic_dialog_alert).show();
					return false;
				}
			});
		}
	}

}
