package com.tongjo.girlswish.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.tongjo.bean.TJUserInfo;
import com.tongjo.girlswish.R;
import com.tongjo.girlswish.utils.ImageUtils;
import com.tongjo.girlswish.utils.StringUtils;
import com.tongjo.girlswish.utils.ToastUtils;
import com.tongjo.girlswish.widget.ButtonWithArrow;

/**
 * 其他人的个人信息展示页面
 * 
 * @author fuzhen
 * 
 */
public class OtherInfoActivity extends AppCompatActivity {

	private Toolbar toolbar;
	private ImageView avatar;
	private ImageView sex;
	private TextView nickname;
	private ButtonWithArrow school;
	private Button send;

	private TJUserInfo info = null;
	
	private DisplayImageOptions displayImageOptions;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_otherinfo);

		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			info = (TJUserInfo) getIntent().getExtras().get("user");
		}
		
		InitView();
		InitData();
	}

	/**
	 * 初始化View
	 */
	public void InitView() {

		toolbar = (Toolbar) findViewById(R.id.toolbar);
		toolbar.setTitle("返回");

		// 设置导航按钮
		toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
		toolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				OtherInfoActivity.this.finish();
			}
		});

		avatar = (ImageView) findViewById(R.id.otherinfo_avatar);
		sex = (ImageView) findViewById(R.id.otherinfo_sex);
		nickname = (TextView) findViewById(R.id.otherinfo_name);
		school = (ButtonWithArrow) findViewById(R.id.otherinfo_school);
		school.getLeftTextView().setTextColor(
				getResources().getColor(R.color.lblack));
		
		send = (Button)findViewById(R.id.otherinfo_send);
		send.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(info == null){
					ToastUtils.show(getApplication(), "没有用户信息");
					return;
				}
				Intent intent = new Intent(OtherInfoActivity.this,ChatActivity.class);
				intent.putExtra("toUserHxid", info.getHxid());
				startActivity(intent);
			}
		});
		
		if(info == null){
			send.setEnabled(false);
			avatar.setEnabled(false);
		}
		
		avatar.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(info == null){
					return;
				}
				Intent intent = new Intent(OtherInfoActivity.this, SpaceImageDetailActivity.class);
				intent.putExtra("image", info.getAvatarUrl());
				int[] location = new int[2];
				avatar.getLocationOnScreen(location);
				intent.putExtra("locationX", location[0]);
				intent.putExtra("locationY", location[1]);

				intent.putExtra("width", avatar.getWidth());
				intent.putExtra("height", avatar.getHeight());
				startActivity(intent);
				overridePendingTransition(0, 0);
			}
		});
	}

	public void InitData() {

		displayImageOptions = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.testimg) // 设置图片下载期间显示的图片
				.showImageForEmptyUri(R.drawable.testimg) // 设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(R.drawable.testimg) // 设置图片加载或解码过程中发生错误显示的图片
				.cacheInMemory(false) // 设置下载的图片是否缓存在内存中
				.cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
				.displayer(new RoundedBitmapDisplayer(180))// 设置成圆角图片
				.imageScaleType(ImageScaleType.EXACTLY).build(); // 创建配置过得DisplayImageOption对象

		if (info != null) {
			nickname.setText(info.getNickname());

			if (info.getGender() == 0) {
				sex.setImageResource(R.drawable.women);
			} else {
				sex.setImageResource(R.drawable.men);
			}

			school.setLeftText("学校");
			if (info.getSchool() != null) { 
				school.setRightText(info.getSchool().getName());
			} else {
				school.setRightText("未知");
			}

			if (!StringUtils.isEmpty(info.getAvatarUrl())) {
				ImageLoader.getInstance().displayImage(info.getAvatarUrl(),
						avatar, displayImageOptions);
			} else {
				Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
						R.drawable.defaultavatar);
				avatar.setImageBitmap(ImageUtils.getRoundCornerDrawable(bitmap,
						360));
			}
		}

	}
}
