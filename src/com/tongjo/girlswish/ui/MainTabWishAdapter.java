package com.tongjo.girlswish.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.Html;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.tongjo.bean.TJWish;
import com.tongjo.girlswish.R;
import com.tongjo.girlswish.utils.ImageUtils;
import com.tongjo.girlswish.utils.StringUtils;
import com.tongjo.girlswish.utils.TimeUtils;
import com.tongjo.girlswish.widget.CircleImageView;

/**
 * 心愿墙页面对应的Adapter
 * 
 * @author fuzhen
 * 
 */
public class MainTabWishAdapter extends BaseAdapter {
	private final String TAG = "MainTabWishAdapter";
	private Context mContext;
	private List<TJWish> mList = new ArrayList<TJWish>();

	private MItemClickListener ICListener;
	private MItemLongPressListener LPListener;
	private GestureDetector gestureDetector;

	private View curView = null;
	private int curPosition = 0;
	private DisplayImageOptions displayImageOptions;

	public MainTabWishAdapter(Context context) {
		super();
		gestureDetector = new GestureDetector(context, new MyGestureListener());
		this.mContext = context;
		displayImageOptions = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.testimg) // 设置图片下载期间显示的图片
				.showImageForEmptyUri(R.drawable.testimg) // 设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(R.drawable.testimg) // 设置图片加载或解码过程中发生错误显示的图片
				.cacheInMemory(false) // 设置下载的图片是否缓存在内存中
				.cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
				.displayer(new RoundedBitmapDisplayer(180))// 设置成圆角图片
				.imageScaleType(ImageScaleType.EXACTLY).build(); // 创建配置过得DisplayImageOption对象
	}

	public MainTabWishAdapter(Context context, List<TJWish> list) {
		super();
		gestureDetector = new GestureDetector(context, new MyGestureListener());
		this.mContext = context;
		this.mList = list;
		displayImageOptions = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.testimg) // 设置图片下载期间显示的图片
				.showImageForEmptyUri(R.drawable.testimg) // 设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(R.drawable.testimg) // 设置图片加载或解码过程中发生错误显示的图片
				.cacheInMemory(false) // 设置下载的图片是否缓存在内存中
				.cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
				.displayer(new RoundedBitmapDisplayer(180))// 设置成圆角图片
				.imageScaleType(ImageScaleType.EXACTLY).build(); // 创建配置过得DisplayImageOption对象
	}

	public void setList(List<TJWish> list) {
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
	public View getView(final int arg0, View convertView, ViewGroup arg2) {
		ViewHolder holder = null;
		if (convertView == null) {

			holder = new ViewHolder();
			LayoutInflater mInflater = LayoutInflater.from(mContext);
			convertView = mInflater.inflate(R.layout.listitem_wish_new, null);

			holder.sex = (ImageView) convertView.findViewById(R.id.wish_sex);
			holder.avatar = (ImageView) convertView
					.findViewById(R.id.wish_avatar);
			holder.content = (TextView) convertView
					.findViewById(R.id.wish_content);

			holder.nickname = (TextView) convertView
					.findViewById(R.id.wish_username);
			holder.publicTime = (TextView) convertView
					.findViewById(R.id.public_time);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		TJWish wish = mList.get(arg0);
		if (wish != null) {
			if (wish.getCreatorUser() != null
					&& wish.getCreatorUser().getSchool() != null
					&& wish.getCreatorUser().getSchool().getName() != null) {
				holder.nickname.setText(wish.getCreatorUser().getSchool().getName());
			}else{
				holder.nickname.setText("学校未知");
			}
			if (wish.getCreatedTime() != null) {
				System.out.println(wish.getCreatedTime());
				holder.publicTime.setText(TimeUtils.getdefaulttime(
						TimeUtils.DEFAULT_DATE_FORMAT, wish.getCreatedTime())+"前");
			}else{
				holder.publicTime.setText("未知");
			}

			if (wish.getContent() != null) {
				holder.content.setText("\t\t\t\t" + wish.getContent());
			}else{
				holder.content.setText("");
			} 

			if (wish.getCreatorUser() != null
					&& wish.getCreatorUser().getGender() == 0) {
				holder.sex.setImageResource(R.drawable.women);
			} else {
				holder.sex.setImageResource(R.drawable.men);
			}
			/*
			 * if (wish.getCreatorUser() != null &&
			 * wish.getCreatorUser().getSchool() != null &&
			 * wish.getCreatorUser().getSchool().getName() != null) {
			 * holder.school.setText(wish.getCreatorUser().getSchool()
			 * .getName()); } if
			 * (!StringUtils.isEmpty(wish.getBackgroundColor())) { int color =
			 * 0; try { color = Integer.parseInt(wish.getBackgroundColor(), 16);
			 * } catch (Exception e) { e.printStackTrace(); }
			 * 
			 * int red = color >> 4; int green = (color >> 2) % 256; int blue =
			 * color % 256; //holder.bottomBg.setBackgroundColor(Color.rgb(red,
			 * green, blue));
			 * holder.bottomBg.setBackgroundColor(Color.parseColor
			 * ("#"+wish.getBackgroundColor())); }
			 */
			if (wish.getCreatorUser() != null && !StringUtils.isEmpty(wish.getCreatorUser().getAvatarUrl())) {
				ImageLoader.getInstance().displayImage(
						wish.getCreatorUser().getAvatarUrl(), holder.avatar,
						displayImageOptions);
			}else{
				Bitmap bitmap =  BitmapFactory.decodeResource(mContext.getResources(),R.drawable.defaultavatar);
				holder.avatar.setImageBitmap(ImageUtils.getRoundCornerDrawable(bitmap,360));
			}
		}

		convertView.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				curPosition = arg0;
				curView = v;
				// 直接的手势操作
				if (gestureDetector.onTouchEvent(event)) {
					return true;
				}
				return false;
			}
		});

		return convertView;
	}

	public final class ViewHolder {
		public ImageView avatar;
		public ImageView sex;
		public TextView content;
		public TextView nickname;
		public TextView publicTime;
	}

	/** Item点击对应的事件 */
	public void setMItemClickListener(MItemClickListener listener) {
		ICListener = listener;
	}

	public interface MItemClickListener {
		public void MItemClick(View v, int position);
	}

	/** Item长按对应的事件 */
	public void setMItemLongPressListener(MItemLongPressListener listener) {
		LPListener = listener;
	}

	public interface MItemLongPressListener {
		public void MItemLongPress(View v, int position);
	}

	/** 手势操作，并通过自定义接口将该操作由使用时定义 */
	public class MyGestureListener extends SimpleOnGestureListener {
		@Override
		public boolean onSingleTapUp(MotionEvent ev) {
			if (ICListener != null)
				ICListener.MItemClick(curView, curPosition);
			return true;
		}

		@Override
		public void onShowPress(MotionEvent ev) {
			System.out.println("onShowPress");
		}

		@Override
		public void onLongPress(MotionEvent ev) {
			if (LPListener != null)
				LPListener.MItemLongPress(curView, curPosition);
		}

		// 手势移动操作，移动时触发
		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			System.out.println("onScroll");
			return true;
		}

		@Override
		public boolean onDown(MotionEvent ev) {
			System.out.println("onDown");
			return true;
		}

		// 手势移动操作，UP时才会触发
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			System.out.println("onFling");
			return false;
		}

		@Override
		public boolean onDoubleTap(MotionEvent event) {
			System.out.println("onDoubleTap");
			return true;
		}
	}

}
