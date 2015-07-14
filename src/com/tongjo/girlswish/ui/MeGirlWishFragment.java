package com.tongjo.girlswish.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.tongjo.bean.TJWish;
import com.tongjo.girlswish.R;
import com.tongjo.girlswish.model.UserSex;
import com.tongjo.girlswish.ui.MeBoyWishFragment.ViewHolder;
import com.tongjo.girlswish.utils.AppConstant;
import com.tongjo.girlswish.utils.RandomUtils;
import com.tongjo.girlswish.utils.TimeUtils;

import de.greenrobot.event.EventBus;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

public class MeGirlWishFragment extends BaseFragment {
	private final static String TAG="MeGirlWishFragment";
	private Context mcontext;
	private List<TJWish> allwishs;
	private List<TJWish> pickewishs;
	private List<TJWish> unpickedwishs;
	private ListView listView;
	private RadioGroup radioGroup;
	private DisplayImageOptions displayImageOptions;

	public MeGirlWishFragment(List<TJWish> wishs) {
		super();
		this.allwishs = wishs;
		pickewishs = new ArrayList<TJWish>();
		unpickedwishs = new ArrayList<TJWish>();
		if (wishs == null) {
			return;
		}
		for (int i = 0; i < wishs.size(); i++) {
			TJWish wish = wishs.get(i);
			if (wish.getIsPicked() == 0) {
				pickewishs.add(wish);
			} else {
				unpickedwishs.add(wish);
			}
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mcontext = activity.getApplicationContext();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		displayImageOptions = new DisplayImageOptions.Builder().showStubImage(R.drawable.testimg) // 设置图片下载期间显示的图片
				.showImageForEmptyUri(R.drawable.testimg) // 设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(R.drawable.testimg) // 设置图片加载或解码过程中发生错误显示的图片
				.cacheInMemory(true) // 设置下载的图片是否缓存在内存中
				.cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
				.displayer(new RoundedBitmapDisplayer(20))// 设置成圆角图片
				.build(); // 创建配置过得DisplayImageOption对象

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_megirlwish, null);
		listView = (ListView) view.findViewById(R.id.lv_fragmegirlwhish_wishs);
		radioGroup = (RadioGroup) view.findViewById(R.id.rg_fragmegirlwish_chose);
		radioGroup.setOnCheckedChangeListener(onCheckedChangeListener);
		listView.setOnItemLongClickListener(onItemLongClickListener);
		switch (radioGroup.getCheckedRadioButtonId()) {
		case R.id.rb_fragmegirlwish_picked:
			listView.setAdapter(new GirlWishAdapter(pickewishs));
			break;
		case R.id.rb_fragmegirlwish_unpick:
			listView.setAdapter(new GirlWishAdapter(unpickedwishs));
			break;
		default:
			break;
		}
		
		return view;
	}
	private OnItemLongClickListener onItemLongClickListener=new OnItemLongClickListener() {

		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
			Message message=new Message();
			message.what=AppConstant.MESSAGE_WHAT_MEWISHLONGCLICK;
			message.obj=parent.getItemAtPosition(position);
			EventBus.getDefault().post(message);
			return true;
		}
	};
	private OnCheckedChangeListener onCheckedChangeListener = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			switch (checkedId) {
			case R.id.rb_fragmegirlwish_picked:
				listView.setAdapter(new GirlWishAdapter(pickewishs));
				break;
			case R.id.rb_fragmegirlwish_unpick:
				listView.setAdapter(new GirlWishAdapter(unpickedwishs));
				break;

			default:
				break;
			}

		}
	};

	class GirlWishAdapter extends BaseAdapter {

		private List<TJWish> wishs;
		private ImageLoadingListener animateFirstDisplayListener = new AnimateFirstDisplayListener();

		public GirlWishAdapter(List<TJWish> wishs) {
			super();
			this.wishs = wishs;
		}

		@Override
		public int getCount() {
			if (wishs == null) {
				return 0;
			} else {
				return wishs.size();
			}
		}

		@Override
		public Object getItem(int position) {
			if (wishs == null) {
				return null;
			} else {
				return wishs.get(position);
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
			ViewHolder viewHolder = null;
			if (convertView == null) {
				viewHolder = new ViewHolder();
				convertView = layoutInflater.inflate(R.layout.listitem_mewish, null);
				viewHolder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_mewishitem_icon);
				viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_mewishitem_name);
				viewHolder.tv_school = (TextView) convertView.findViewById(R.id.tv_mewishitem_school);
				viewHolder.tv_time = (TextView) convertView.findViewById(R.id.tv_mewishitem_time);
				viewHolder.view_color = (View) convertView.findViewById(R.id.tv_mewishitem_color);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			TJWish tjWish = wishs.get(position);
			viewHolder.tv_name.setText(tjWish.getCreatorUser().getRealname());
			viewHolder.tv_school.setText(tjWish.getCreatorUser().getSchool().getName());
			String current=TimeUtils.getCurrentTimeInString();
			viewHolder.tv_time.setText(TimeUtils.minuteCompare(current, tjWish.getPickedTime())+"min");
			viewHolder.view_color.setBackgroundColor(Color.parseColor("#"+tjWish.getBackgroundColor()));
			imageLoader.displayImage(tjWish.getCreatorUser().getAvatarUrl(), viewHolder.iv_icon, displayImageOptions, animateFirstDisplayListener);
			return convertView;
		}

	}

	public final class ViewHolder {
		public ImageView iv_icon;
		public TextView tv_name;
		public TextView tv_school;
		public TextView tv_time;
		public View view_color;
	}

	private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

		static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				// 是否第一次显示
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					// 图片淡入效果
					FadeInBitmapDisplayer.animate(imageView, 500);
					displayedImages.add(imageUri);
				}
			}
		}
	}
}
