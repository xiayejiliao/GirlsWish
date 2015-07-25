package com.tongjo.girlswish.ui;

import java.lang.reflect.Type;
import java.util.UUID;

import org.apache.http.Header;

import android.content.Intent;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.tongjo.bean.TJResponse;
import com.tongjo.bean.TJUserInfo;
import com.tongjo.bean.TJWish;
import com.tongjo.girlswish.R;
import com.tongjo.girlswish.ui.BaseActivity;
import com.tongjo.girlswish.utils.AppConstant;
import com.tongjo.girlswish.utils.ToastUtils;
import com.tongjo.girlwish.data.DataContainer;

import de.greenrobot.event.EventBus;

/**
 * 男生还没有完成的心愿信息界面
 * 
 * @author dell
 *
 */

public class BoyUncompleteWishActivity extends BaseActivity {
	private final static String TAG="BoyUncompleteWishActivity";
	private Button bt_sendmes;
	private Button bt_putback;
	private TextView tv_creatername;
	private TextView tv_createrchool;
	private TextView tv_context;
	private TextView tv_picktime;
	private TextView tv_createrphone;
	private ImageView iv_createricon;
	private TJWish currentwish;
	private UUID currentwishuuid;
	private TJUserInfo currentuser;
	private DisplayImageOptions displayImageOptions;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_boyuncompletewish);
		setCenterText("未完成的心愿");
		bt_sendmes = (Button) findViewById(R.id.bt_boyuncompletewish_sendmes);
		bt_putback = (Button) findViewById(R.id.bt_boyuncompletewish_putback);
		tv_creatername = (TextView) findViewById(R.id.tv_boyuncompletewish_creatername);
		tv_createrchool = (TextView) findViewById(R.id.tv_boyuncompletewish_createrschool);
		tv_context = (TextView) findViewById(R.id.tv_boyuncompletewish_content);
		tv_picktime = (TextView) findViewById(R.id.tv_boyuncompletewish_pickime);
		tv_createrphone = (TextView) findViewById(R.id.tv_boyuncompletewish_createrphone);
		iv_createricon = (ImageView) findViewById(R.id.iv_boyuncompletewish_createricon);
		bt_putback.setOnClickListener(onClickListener);
		bt_sendmes.setOnClickListener(onClickListener);
		String uu = getIntent().getStringExtra("wishuuid");
		currentwishuuid = UUID.fromString(uu);
		currentwish = DataContainer.mewishs.getByUUID(currentwishuuid);

		displayImageOptions = new DisplayImageOptions.Builder().showStubImage(R.drawable.testimg) // 设置图片下载期间显示的图片
				.showImageForEmptyUri(R.drawable.testimg) // 设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(R.drawable.testimg) // 设置图片加载或解码过程中发生错误显示的图片
				.cacheInMemory(true) // 设置下载的图片是否缓存在内存中
				.cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
				.displayer(new RoundedBitmapDisplayer(20))// 设置成圆角图片
				.imageScaleType(ImageScaleType.EXACTLY)//缩放模式按比例缩放
				.build(); // 创建配置过得DisplayImageOption对象
		if (currentwish != null) {
			tv_context.setText(currentwish.getContent());
			tv_picktime.setText(currentwish.getCreatedTime());
			currentuser = currentwish.getCreatorUser();
			if (currentuser != null) {
				tv_createrchool.setText(currentuser.getSchool().getName());
				tv_creatername.setText(currentuser.getRealname());
				tv_createrphone.setText(currentuser.getTel());
				imageLoader.displayImage(currentuser.getAvatarUrl(), iv_createricon, displayImageOptions, new SimpleImageLoadingListener());
			}
		}
		Log.d(TAG, currentwish.toString());

	}

	private OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.bt_boyuncompletewish_putback:
				RequestParams updateParams = new RequestParams();
				updateParams.put("_id", currentwishuuid);
				updateParams.put("content", tv_context.getText().toString());
				updateParams.put("isCompleted", 0);
				updateParams.put("isPicked", 0);
				updateParams.put("backgroundColor", currentwish.getBackgroundColor());
				asyncHttpClient.post(AppConstant.URL_BASE + AppConstant.URL_WISHUPDATE, updateParams, putback);
				break;
			case R.id.bt_boyuncompletewish_sendmes:
				sendSMS(tv_createrphone.getText().toString());
				break;

			default:
				break;
			}
		}
	};
	private TextHttpResponseHandler putback = new TextHttpResponseHandler("UTF-8") {

		@Override
		public void onSuccess(int arg0, Header[] arg1, String arg2) {
			// TODO Auto-generated method stub
			if (arg0 == 200) {
				Type type = new TypeToken<TJResponse<TJWish>>() {
				}.getType();
				TJResponse<TJWish> response = new Gson().fromJson(arg2, type);
				if (response.getResult().getCode() == 0) {
					DataContainer.mewishs.deleteByUUID(currentwishuuid);
					Message mes = new Message();
					mes.what = AppConstant.MESSAGE_WHAT_GIRLWISH_DEL;
					mes.obj = response.getData();
					EventBus.getDefault().post(mes);
					ToastUtils.show(getApplicationContext(), "放回成功");
				} else {
					ToastUtils.show(getApplicationContext(), "放回失败:" + response.getResult().getMessage());
				}
			}
		}

		@Override
		public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
			// TODO Auto-generated method stub

		}
	};

	private void sendSMS(String phone) {
		Uri smsToUri = Uri.parse("smsto:" + phone);
		Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);
		startActivity(intent);
	}
}
