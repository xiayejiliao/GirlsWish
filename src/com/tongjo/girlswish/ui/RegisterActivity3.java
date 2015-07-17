package com.tongjo.girlswish.ui;

import java.lang.reflect.Type;

import org.apache.http.Header;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.tongjo.bean.TJResponse;
import com.tongjo.bean.TJSchool;
import com.tongjo.bean.TJUserInfo;
import com.tongjo.girlswish.R;
import com.tongjo.girlswish.utils.AppConstant;
import com.tongjo.girlswish.utils.MyTimer;
import com.tongjo.girlswish.utils.SMSHelper;
import com.tongjo.girlswish.utils.SexUtils;
import com.tongjo.girlswish.utils.SpUtils;
import com.tongjo.girlswish.utils.StringUtils;
import com.tongjo.girlswish.utils.ToastUtils;
import com.tongjo.girlswish.utils.MyTimer.TimerProgress;
import com.tongjo.girlswish.utils.SMSHelper.SendResult;
import com.tongjo.girlswish.widget.LinkTextView;

import android.R.integer;
import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * 上传图片
 * 
 * @author 16ren
 * @date 2015/7/17
 *
 */
public class RegisterActivity3 extends BaseActivity implements OnClickListener {
	private final static String TAG = "RegisterActivity3";
	private Button bt_getpic;
	private Button bt_uploadpic;
	private ImageView iv_icon;
	private DisplayImageOptions displayImageOptions;
	
	public static final String IMAGE_UNSPECIFIED = "image/*";
	public static final int TAKE = 0; // 拍摄图片
	public static final int PHOTO = 1; // 读取图片
	public static final int PHOTOZOOM = 2;// 缩放图片

	// use for dialog
	private final static int avatarDialog = 0x10;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register3);
		setCenterText("注册");
		bt_getpic = (Button) findViewById(R.id.bt_register3_geticon);
		bt_uploadpic = (Button) findViewById(R.id.bt_register3_sure);
		iv_icon = (ImageView) findViewById(R.id.iv_register3_icon);
		bt_getpic.setOnClickListener(this);
		bt_uploadpic.setOnClickListener(this);
		displayImageOptions = new DisplayImageOptions.Builder().showStubImage(R.drawable.image_imageloading) // 设置图片下载期间显示的图片
				.showImageForEmptyUri(R.drawable.image_imageloadneterror) // 设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(R.drawable.image_imageloaderror) // 设置图片加载或解码过程中发生错误显示的图片
				.cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
				.displayer(new RoundedBitmapDisplayer(180))// 设置成圆角图片
				.imageScaleType(ImageScaleType.EXACTLY)// 缩放模式按比例缩放
				.build(); // 创建配置过得DisplayImageOption对象
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bt_register3_geticon:
			Intent intent = new Intent("com.android.camera.action.CROP");
			//intent.setDataAndType(uri, IMAGE_UNSPECIFIED);
			intent.putExtra("crop", "true");
			// aspectX aspectY 是宽高的比例
			intent.putExtra("aspectX", 1);
			intent.putExtra("aspectY", 1);
			// outputX outputY 是裁剪图片宽高
			intent.putExtra("outputX", 256);
			intent.putExtra("outputY", 256);
			intent.putExtra("return-data", true);
			startActivityForResult(intent, AppConstant.STARTFORCODE_REGISTER_PIC);
			break;
		case R.id.bt_register3_sure:
			break;
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == AppConstant.STARTFORCODE_REGISTER_PIC && requestCode == AppConstant.RESULTCODE_REGISTER_PIC) {

		}
	}
}
