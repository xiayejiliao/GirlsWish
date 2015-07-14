package com.tongjo.girlswish.ui;

import java.util.List;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.tongjo.bean.TJWish;
import com.tongjo.girlswish.R;
import com.tongjo.girlswish.model.UserSex;
import com.tongjo.girlswish.utils.RandomUtils;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class MeWishFragment extends BaseFragment {
	private List<TJWish> tJWishs;
	private ListView lv_mewhis;
	private Context mcontext;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_mewish, null);
		lv_mewhis = (ListView) view.findViewById(R.id.lv_fragmewhish_wish);
		lv_mewhis.setAdapter(new MyAdapter());
		lv_mewhis.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				return false;
			}
		});
		return view;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mcontext = getActivity().getApplicationContext();

	}

	public MeWishFragment(List<TJWish> tJWishs) {
		super();
		this.tJWishs = tJWishs;
	}

	class MyAdapter extends BaseAdapter {

		public MyAdapter() {
			super();
			System.out.println("myadapter size:" + tJWishs.size());
		}

		@Override
		public int getCount() {
			if (tJWishs == null) {
				return 0;
			} else {
				return tJWishs.size();
			}
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			if (tJWishs == null) {
				return null;
			} else {
				return tJWishs.get(position);
			}
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			LayoutInflater layoutInflater = LayoutInflater.from(mcontext);
			ViewHolder viewHolder = new ViewHolder();
			if (convertView == null) {
				convertView = layoutInflater.inflate(R.layout.listitem_mewish, null);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			viewHolder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_mewishitem_icon);
			viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_mewishitem_name);
			viewHolder.tv_school = (TextView) convertView.findViewById(R.id.tv_mewishitem_school);
			viewHolder.tv_time = (TextView) convertView.findViewById(R.id.tv_mewishitem_time);
			viewHolder.view_color = (View) convertView.findViewById(R.id.view_mewishitem_color);
			TJWish tjWish = tJWishs.get(position);
			viewHolder.tv_name.setText(tjWish.getCreatorUser().getRealname());
			viewHolder.tv_school.setText(tjWish.getCreatorUser().getSchool().getName());
			viewHolder.tv_school.setText(tjWish.getCreatedTime());
			viewHolder.view_color.setBackgroundColor(RandomUtils.getRandom(0, 225));
			return convertView;

		}
	}

	class ViewHolder {
		ImageView iv_icon;
		TextView tv_name;
		TextView tv_school;
		TextView tv_time;
		View view_color;
	}
}
