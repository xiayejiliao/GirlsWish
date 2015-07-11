package com.tongjo.girlswish.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
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

import com.tongjo.bean.TJWish;
import com.tongjo.girlswish.R;

/**
 * 心愿墙页面对应的Adapter
 * 
 * @author fuzhen
 * 
 */
public class MainTabWishAdapter extends BaseAdapter {
	private Context mContext;
	private List<TJWish> mList = new ArrayList<TJWish>();

	private MItemClickListener ICListener;
	private MItemLongPressListener LPListener;
	private GestureDetector gestureDetector;

	private View curView = null;
	private int curPosition = 0;

	public MainTabWishAdapter(Context context) {
		super();
		gestureDetector = new GestureDetector(context, new MyGestureListener());
		this.mContext = context;
	}

	public MainTabWishAdapter(Context context, List<TJWish> list) {
		super();
		gestureDetector = new GestureDetector(context, new MyGestureListener());
		this.mContext = context;
		this.mList = list;
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
	public View getView(int arg0, View convertView, ViewGroup arg2) {
		ViewHolder holder = null;
		if (convertView == null) {

			holder = new ViewHolder();
			LayoutInflater mInflater = LayoutInflater.from(mContext);
			convertView = mInflater.inflate(R.layout.listitem_wish, null);

			holder.logo = (ImageView) convertView.findViewById(R.id.wish_logo);
			holder.avatar = (ImageView) convertView
					.findViewById(R.id.wish_avatar);
			holder.content = (TextView) convertView
					.findViewById(R.id.wish_content);
			holder.school = (TextView) convertView
					.findViewById(R.id.wish_schoolname);
			holder.nickname = (TextView) convertView
					.findViewById(R.id.wish_username);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		convertView.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
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
		public ImageView logo;
		public ImageView avatar;
		public TextView content;
		public TextView school;
		public TextView nickname;
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
