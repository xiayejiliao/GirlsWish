package com.tongjo.girlswish.ui;

import java.util.ArrayList;
import java.util.List;

import com.tongjo.bean.TJWish;
import com.tongjo.girlswish.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 心愿墙页面对应的Adapter
 * @author fuzhen
 *
 */
public class MainTabWishAdapter extends BaseAdapter{
	private Context mContext;
	private List<TJWish> mList = new ArrayList<TJWish>();
	
	public MainTabWishAdapter(Context context){
		super();
		this.mContext = context;
	}
	
	public MainTabWishAdapter(Context context,List<TJWish> list){
		super();
		this.mContext = context;
		this.mList = list;
	}
	
	public void setList(List<TJWish> list){
		this.mList = list;
		this.notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		if(mList == null){
			return 0;
		}
		return mList.size();
	}

	@Override
	public Object getItem(int arg0) {
		if(mList == null){
			return null;
		}
		if(arg0 < 0 || arg0 >= mList.size()){
			return null;
		}
		return mList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		if(mList == null){
			return 0;
		}
		if(arg0 < 0  || arg0 >= mList.size()){
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
            convertView = mInflater.inflate(R.layout.listitem_wish, null);  

            holder.logo = (ImageView) convertView.findViewById(R.id.wish_logo);  
            holder.avatar = (ImageView) convertView.findViewById(R.id.wish_avatar);  
            holder.content = (TextView) convertView.findViewById(R.id.wish_content);  
            holder.school = (TextView) convertView.findViewById(R.id.wish_schoolname);  
            holder.nickname = (TextView) convertView.findViewById(R.id.wish_username);  
            convertView.setTag(holder);  

        } else {
            holder = (ViewHolder) convertView.getTag();  
        } 
		return convertView;
	}

	public final class ViewHolder {
		public ImageView logo;
        public ImageView avatar;  
        public TextView content;  
        public TextView school;  
        public TextView nickname;  
    }  
	
}
