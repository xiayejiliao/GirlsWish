package com.tongjo.girlswish.ui;

import java.util.ArrayList;
import java.util.List;

import com.tongjo.bean.TJMessage;
import com.tongjo.girlswish.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 消息页面对应的Adapter
 * 
 */
public class MainTabInfoAdapter extends BaseAdapter {
	private Context mContext;
	private List<TJMessage> mList = new ArrayList<TJMessage>();

	public MainTabInfoAdapter(Context context) {
		super();
		this.mContext = context;
	}

	public MainTabInfoAdapter(Context context, List<TJMessage> list) {
		super();
		this.mContext = context;
		this.mList = list;
	}

	public void setList(List<TJMessage> list) {
		this.mList = list;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		if (mList == null) {
			return 0;
		}
		return mList.size();
	}

	@Override
	public Object getItem(int arg0) {
		if (mList == null) {
			return null;
		}
		if (arg0 < 0 || arg0 >= mList.size()) {
			return null;
		}
		return mList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		if (mList == null) {
			return 0;
		}
		if (arg0 < 0 || arg0 >= mList.size()) {
			return 0;
		}
		return arg0;
	}

	@Override
	public View getView(int arg0, View convertView, ViewGroup arg2) {
		ViewHolder holder = null;
		if (convertView == null) {

			holder = new ViewHolder();
			LayoutInflater mInflater = LayoutInflater.from(mContext);
			convertView = mInflater.inflate(R.layout.listitem_info, null);

			holder.iconImageView = (ImageView) convertView
					.findViewById(R.id.info_list_icon);
			holder.titleTextVIew = (TextView) convertView
					.findViewById(R.id.info_list_title);
			holder.msgTextView = (TextView) convertView
					.findViewById(R.id.info_list_msg);
			holder.timeTextView = (TextView) convertView
					.findViewById(R.id.info_list_time);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		TJMessage message = mList.get(arg0);
		holder.iconImageView.setImageResource(R.drawable.wish);
		holder.titleTextVIew.setText("您的心愿被摘啦");
		holder.msgTextView.setText(message.getContent());
		holder.timeTextView.setText(message.getTime());
		if (arg0 % 4 == 0) {
			convertView.setBackgroundColor(mContext.getResources().getColor(
					R.color.addwish_blueColor));
		} else if (arg0 % 4 == 1) {
			convertView.setBackgroundColor(mContext.getResources().getColor(
					R.color.addwish_redColor));
		} else if (arg0 % 4 == 2) {
			convertView.setBackgroundColor(mContext.getResources().getColor(
					R.color.addwish_greenColor));
		} else {
			convertView.setBackgroundColor(mContext.getResources().getColor(
					R.color.addwish_yellowColor));
		}
		return convertView;
	}

	public final class ViewHolder {
		public ImageView iconImageView;
		public TextView titleTextVIew;
		public TextView msgTextView;
		public TextView timeTextView;
	}

}
