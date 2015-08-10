package com.tongjo.girlswish.ui;

import java.util.ArrayList;
import java.util.List;

import u.aly.v;

import com.tongjo.girlswish.R;
import com.viewpagerindicator.CirclePageIndicator;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
* @Description: 引导界面
* @author 16ren 
* @date 2015年8月1日 下午2:59:45 
*
 */
public class GuideActivity extends FragmentActivity {
	private ViewPager viewpager;
	private CirclePageIndicator circlePageIndicator;
	private Button bt_next;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide);
		viewpager = (ViewPager) findViewById(R.id.page_guide);
		circlePageIndicator = (CirclePageIndicator) findViewById(R.id.indicator_guide);
		bt_next = (Button) findViewById(R.id.bt_guide_next);
		viewpager.setAdapter(new GuideAdapter(this));
		circlePageIndicator.setViewPager(viewpager);
		viewpager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				circlePageIndicator.setCurrentItem(arg0);
				System.out.println(viewpager.getAdapter().getCount());
				if ((viewpager.getAdapter().getCount() - 1) == arg0) {
					bt_next.setVisibility(View.VISIBLE);
				} else {
					bt_next.setVisibility(View.GONE);
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});
		bt_next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	/**
	* @ClassName: GuideAdapter 
	* @Description: TODO
	* @author 16ren 
	* @date 2015年8月1日 下午2:57:57 
	*
	 */
	class GuideAdapter extends PagerAdapter {
		private final int[] pics = { R.drawable.guide1, R.drawable.guide2, R.drawable.guide3, R.drawable.guide4 };
		private List<View> views;
		private Context mcontext;

		public GuideAdapter(Context context) {
			super();
			mcontext = context;
			views = new ArrayList<View>();
			LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
			for (int i = 0; i < pics.length; i++) {
				ImageView iv = new ImageView(mcontext);
				iv.setLayoutParams(mParams);
				iv.setImageResource(pics[i]);
				views.add(iv);
			}
		}

		@Override
		public int getCount() {
			if (views != null) {
				return views.size();
			}
			return 0;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return (arg0 == arg1);
		}

		@Override
		public Object instantiateItem(View container, int position) {
			((ViewPager) container).addView(views.get(position), 0);

			return views.get(position);
		}

		@Override
		public void destroyItem(View container, int position, Object object) {
			// TODO Auto-generated method stub
			((ViewPager) container).removeView(views.get(position));
		}
	}
}
