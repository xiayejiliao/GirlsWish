package com.tongjo.girlswish.ui;

import java.lang.reflect.Type;
import java.util.UUID;

import org.apache.http.Header;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
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
 * 男生完成的心愿信息界面
 * 
 * @author 16ren
 *
 */
public class BoyCompleteWishActivity extends BaseActivity {
	private Button bt_sendmes;
	private Button bt_del;
	private TextView tv_creatername;
	private TextView tv_createrchool;
	private TextView tv_context;
	private TextView tv_createtime;
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
		setContentView(R.layout.activity_boycompletewish);
		setCenterText("完成的心愿");
		bt_sendmes = (Button) findViewById(R.id.bt_boycompletewish_sendmes);
		bt_del = (Button) findViewById(R.id.bt_boycompletewish_sendmes);
		tv_creatername = (TextView) findViewById(R.id.tv_boycompletewish_creatername);
		tv_createrchool = (TextView) findViewById(R.id.tv_boycompletewish_createrschool);
		tv_context = (TextView) findViewById(R.id.tv_boycompletewish_content);
		tv_createtime = (TextView) findViewById(R.id.tv_boycompletewish_creattime);
		tv_createrphone = (TextView) findViewById(R.id.tv_boycompletewish_createrphone);
		iv_createricon = (ImageView) findViewById(R.id.iv_boycompletewish_createricon);
		bt_del.setOnClickListener(onClickListener);
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
				.build(); // 创建配置过得DisplayImageOption对象
		if (currentwish != null) {
			tv_context.setText(currentwish.getContent());
			tv_createtime.setText(currentwish.getCreatedTime());
			currentuser = currentwish.getCreatorUser();
			if (currentuser != null) {
				tv_createrchool.setText(currentuser.getSchool().getName());
				tv_creatername.setText(currentuser.getRealname());
				imageLoader.displayImage(currentwish.getPickerUser().getAvatarUrl(), iv_createricon, displayImageOptions, new SimpleImageLoadingListener());
			}
		}
	}

	private OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.bt_boycompletewish_del:
				RequestParams requestParams = new RequestParams();
				requestParams.put("_id", currentwish.get_id().toString());
				asyncHttpClient.post(AppConstant.URL_BASE + AppConstant.URL_WISHDEL, requestParams, delwish);
				break;
			case R.id.bt_boycompletewish_sendmes:
				sendSMS(tv_createrphone.getText().toString());
				break;

			default:
				break;
			}
		}
	};
	private TextHttpResponseHandler delwish = new TextHttpResponseHandler("UTF-8") {

		@Override
		public void onSuccess(int arg0, Header[] arg1, String arg2) {
			if (arg0 == 200) {
				Type type = new TypeToken<TJResponse<TJWish>>() {
				}.getType();
				TJResponse<TJWish> response = new Gson().fromJson(arg2, type);
				if (response.getResult().getCode() == 0) {
					ToastUtils.show(getApplicationContext(), "删除成功");
					DataContainer.mewishs.deleteByUUID(currentwish.get_id());
					Message message = new Message();
					message.what = AppConstant.MESSAGE_WHAT_GIRLWISH_DEL;
					message.obj = currentwish;
					EventBus.getDefault().post(message);
				} else {
					ToastUtils.show(getApplicationContext(), "删除失败:" + response.getResult().getMessage());
				}
			}

		}

		@Override
		public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
			// TODO Auto-generated method stub
			ToastUtils.show(getApplicationContext(), "删除失败:" + arg3.toString());
		}
	};

	private void sendSMS(String phone) {
		Uri smsToUri = Uri.parse("smsto:" + phone);
		Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);
		startActivity(intent);
	}
}
