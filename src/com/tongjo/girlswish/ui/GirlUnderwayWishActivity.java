package com.tongjo.girlswish.ui;

import java.util.UUID;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.tongjo.bean.TJWish;
import com.tongjo.girlswish.R;
import com.tongjo.girlwish.data.DataContainer;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 女生正在进行中心愿的界面
 * 
 * @author 16ren
 *
 */
public class GirlUnderwayWishActivity extends BaseActivity {
	private Button bt_sure;
	private TextView tv_pickername;
	private TextView tv_pickerschool;
	private TextView tv_context;
	private TextView tv_picktime;
	private ImageView iv_pickericon;
	private TJWish currentwish;
	private UUID currentwishuuid;
	private DisplayImageOptions displayImageOptions;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_grilunderwaywish);
		setCenterText("正在进行的心愿");
		bt_sure = (Button) findViewById(R.id.bt_grilunderwaywish_sure);
		tv_pickername = (TextView) findViewById(R.id.tv_grilunderwaywish_pickername);
		tv_pickerschool = (TextView) findViewById(R.id.tv_grilunderwaywish_pickerschool);
		tv_context = (TextView) findViewById(R.id.tv_grilunderwaywish_context);
		tv_picktime = (TextView) findViewById(R.id.tv_grilunderwaywish_picktime);
		iv_pickericon = (ImageView) findViewById(R.id.iv_grilunderwaywish_pickericon);
		iv_pickericon.setOnClickListener(onClickListener);
		bt_sure.setOnClickListener(onClickListener);

		String uu = getIntent().getStringExtra("wishuuid");
		currentwishuuid = UUID.fromString(uu);
		currentwish = DataContainer.mewishs.getByUUID(currentwishuuid);
		displayImageOptions = new DisplayImageOptions.Builder().showStubImage(R.drawable.testimg) // 设置图片下载期间显示的图片
				.showImageForEmptyUri(R.drawable.testimg) // 设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(R.drawable.testimg) // 设置图片加载或解码过程中发生错误显示的图片
				.cacheInMemory(true) // 设置下载的图片是否缓存在内存中
				.cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
				.displayer(new RoundedBitmapDisplayer(20))// 设置成圆角图片
				.build(); // 创建配置过得DisplayImageOption对象
		if (currentwish != null) {
			System.out.println(currentwish.toString());
			
			//imageLoader.displayImage(currentwish.getPickerUser().getAvatarUrl(), iv_pickericon, displayImageOptions, new SimpleImageLoadingListener());
			tv_context.setText(currentwish.getContent());
			//tv_pickername.setText(currentwish.getPickerUser().getRealname());
			//tv_pickerschool.setText(currentwish.getPickerUser().getSchool().getName());
			tv_picktime.setText(currentwish.getPickedTime());
		}
	}

	private OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.iv_grilunderwaywish_pickericon:

				break;
			case R.id.bt_grilunderwaywish_sure:

				break;
			default:
				break;
			}
		}
	};
}
