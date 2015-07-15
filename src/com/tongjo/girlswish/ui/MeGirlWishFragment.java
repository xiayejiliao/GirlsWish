package com.tongjo.girlswish.ui;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.tongjo.bean.TJUserInfo;
import com.tongjo.bean.TJWish;
import com.tongjo.girlswish.R;
import com.tongjo.girlswish.model.UserSex;
import com.tongjo.girlswish.ui.MeBoyWishFragment.ViewHolder;
import com.tongjo.girlswish.utils.AppConstant;
import com.tongjo.girlswish.utils.RandomUtils;
import com.tongjo.girlswish.utils.TimeUtils;
import com.tongjo.girlwish.data.DataContainer;

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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

public class MeGirlWishFragment extends BaseFragment {
	private final static String TAG = "MeGirlWishFragment";
	private Context mcontext;
	private List<TJWish> allwishs; // 所有心愿
	private List<TJWish> pickewishs;// 被摘进行的心愿
	private List<TJWish> unpickedwishs; // 没有被摘的心愿
	private List<TJWish> finshWishs;// 结束的心愿
	private ListView listView;
	private RadioGroup radioGroup;
	private DisplayImageOptions displayImageOptions;

	public MeGirlWishFragment(List<TJWish> wishs) {
		super();
		setWishs(wishs);
	}

	public void setWishs(List<TJWish> wishs) {
		this.allwishs = wishs;
		pickewishs = new ArrayList<TJWish>();
		unpickedwishs = new ArrayList<TJWish>();
		finshWishs = new ArrayList<TJWish>();
		if (wishs == null) {
			return;
		}
		// 分类心愿 ispicked=0 没有被摘 iscompleted=0没有完成
		for (int i = 0; i < wishs.size(); i++) {
			TJWish wish = wishs.get(i);
			if (wish.getIsPicked() == 0) {
				unpickedwishs.add(wish);
			} else {
				if (wish.getIsCompleted() == 0) {
					pickewishs.add(wish);
				} else {
					finshWishs.add(wish);
				}

			}
		}
	}
	public void updateViewFromContainer() {
		List<TJWish> list=DataContainer.mewishs.getAll();
		setWishs(list);
		updateWiew();
	}
	
	public void updateViewFromlist(List<TJWish> wishs) {
		setWishs(wishs);
		updateWiew();
	}

	private void updateWiew() {
		switch (radioGroup.getCheckedRadioButtonId()) {
		case R.id.rb_fragmegirlwish_picked:
			listView.setAdapter(new GirlWishAdapter(pickewishs));
			break;
		case R.id.rb_fragmegirlwish_unpick:
			listView.setAdapter(new GirlWishAdapter(unpickedwishs));
			break;
		case R.id.rb_fragmegirlwish_finish:
			listView.setAdapter(new GirlWishAdapter(finshWishs));
			break;

		default:
			break;
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
		EventBus.getDefault().register(this);
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_megirlwish, null);
		listView = (ListView) view.findViewById(R.id.lv_fragmegirlwhish_wishs);
		radioGroup = (RadioGroup) view.findViewById(R.id.rg_fragmegirlwish_chose);
		radioGroup.setOnCheckedChangeListener(onCheckedChangeListener);
		listView.setOnItemClickListener(onItemClickListener);
		updateWiew();
		return view;
	}

	private OnItemClickListener onItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			Message msg = new Message();
			switch (radioGroup.getCheckedRadioButtonId()) {
			case R.id.rb_fragmegirlwish_picked:
				msg.what = AppConstant.MESSAGE_WHAT_GIRLWISH_CLICK_PCIK;
				break;
			case R.id.rb_fragmegirlwish_unpick:
				msg.what = AppConstant.MESSAGE_WHAT_GIRLWISH_CLICK_UNPICK;
				break;
			case R.id.rb_fragmegirlwish_finish:
				msg.what = AppConstant.MESSAGE_WHAT_GIRLWISH_CLICK_FINISH;
				break;
			default:
				break;
			}
			msg.obj = parent.getItemAtPosition(position);
			EventBus.getDefault().post(msg);
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
			case R.id.rb_fragmegirlwish_finish:
				listView.setAdapter(new GirlWishAdapter(finshWishs));
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
			String current = TimeUtils.getCurrentTimeInString();
			viewHolder.tv_time.setText(TimeUtils.minuteCompare(current, tjWish.getPickedTime()) + "min");
			viewHolder.view_color.setBackgroundColor(Color.parseColor("#" + tjWish.getBackgroundColor()));
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
	
	public void onEventMainThread(Message msg) {
		switch (msg.what) {
		case AppConstant.MESSAGE_WHAT_GIRLWISH_UPDATE:
			updateViewFromContainer();
			break;
		case AppConstant.MESSAGE_WHAT_GIRLWISH_DEL:
			updateViewFromContainer();
			break;
		default:
			break;
		}
	}
}
