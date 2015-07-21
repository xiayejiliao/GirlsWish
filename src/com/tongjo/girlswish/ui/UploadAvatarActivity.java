package com.tongjo.girlswish.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Type;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.utils.ImageSizeUtils;
import com.tongjo.bean.TJResponse;
import com.tongjo.bean.TJSchool;
import com.tongjo.bean.TJUserInfo;
import com.tongjo.girlswish.R;
import com.tongjo.girlswish.utils.AppConstant;
import com.tongjo.girlswish.utils.ImageUtils;
import com.tongjo.girlswish.utils.MyTimer;
import com.tongjo.girlswish.utils.SMSHelper;
import com.tongjo.girlswish.utils.SexUtils;
import com.tongjo.girlswish.utils.SpUtils;
import com.tongjo.girlswish.utils.StringUtils;
import com.tongjo.girlswish.utils.ToastUtils;
import com.tongjo.girlswish.utils.MyTimer.TimerProgress;
import com.tongjo.girlswish.utils.SMSHelper.SendResult;
import com.tongjo.girlswish.widget.CircleImageView;
import com.tongjo.girlswish.widget.LinkTextView;

import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.MediaStore;
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
public class UploadAvatarActivity extends BaseActivity implements OnClickListener {
	private final static String TAG = "RegisterActivity3";
	private Button bt_getpic;
	private Button bt_uploadpic;
	private CircleImageView iv_icon;
	private DisplayImageOptions displayImageOptions;
	private Button selectImageBtn;
	private Bitmap iconBitmap = null;
	private Uri iconUri;

	private File sdcardTempFile;
	private AlertDialog dialog;
	private int crop = 180;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_uploadavatar);
		setCenterText("注册");
		bt_getpic = (Button) findViewById(R.id.bt_register3_geticon);
		bt_uploadpic = (Button) findViewById(R.id.bt_register3_sure);
		iv_icon = (CircleImageView) findViewById(R.id.iv_register3_icon);
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
			if (dialog == null) {
				dialog = new AlertDialog.Builder(this).setItems(new String[] { "相机", "相册" }, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (which == 0) {
							Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");

							startActivityForResult(intent, AppConstant.STARTFORCODE_REGISTER_TACKPICFORMCAMERA);
						} else {
							Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
							intent.setType("image/*");
							startActivityForResult(intent, AppConstant.STARTFORCODE_REGISTER_TACKPICFORMLOCAL);
						}
					}
				}).create();
			}
			if (!dialog.isShowing()) {
				dialog.show();
			}
			break;
		case R.id.bt_register3_sure:
			RequestParams requestParams = new RequestParams();
			requestParams.put("image", iconBitmap);
			asyncHttpClient.post(AppConstant.URL_BASE + AppConstant.URL_UPLOADICON, requestParams, uploadpic);
			break;
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == AppConstant.STARTFORCODE_REGISTER_TACKPICFORMCAMERA && resultCode == resultCode) {
			Uri uri = data.getData();
			if (uri == null) {
				Bundle bundle = data.getExtras();
				if (bundle != null) {
					iconBitmap = (Bitmap) bundle.get("data"); // get bitmap
					iv_icon.setImageBitmap(iconBitmap);
				} else {
					Toast.makeText(getApplicationContext(), "err****", Toast.LENGTH_LONG).show();
					return;
				}
			} else {
				iconUri = data.getData();
				ContentResolver cr = this.getContentResolver();
				try {
					iconBitmap = BitmapFactory.decodeStream(cr.openInputStream(iconUri));
					iv_icon.setImageBitmap(iconBitmap);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		} else if (requestCode == AppConstant.STARTFORCODE_REGISTER_TACKPICFORMLOCAL && resultCode == RESULT_OK) {
			iconUri = data.getData();
			ContentResolver cr = this.getContentResolver();
			try {
				iconBitmap = BitmapFactory.decodeStream(cr.openInputStream(iconUri));
				iv_icon.setImageBitmap(iconBitmap);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	private TextHttpResponseHandler uploadpic = new TextHttpResponseHandler("UTF-8") {

		@Override
		public void onSuccess(int arg0, Header[] arg1, String arg2) {
			// TODO Auto-generated method stub
			if (arg0 == 200) {
				Type type = new TypeToken<TJResponse<Object>>() {
				}.getType();
				TJResponse<Object> response = new Gson().fromJson(arg2, type);
				if (response.getResult().getCode() == 0) {
					Object object = response.getData();
					try {
						JSONObject jsonObject = new JSONObject(object.toString());
						String avatarUrl = jsonObject.getString("jsonObject");
						SpUtils.put(getApplicationContext(), AppConstant.USER_ICONURL, avatarUrl);
						startActivity(new Intent(UploadAvatarActivity.this, LoginActivity.class));
						UploadAvatarActivity.this.finish();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					ToastUtils.show(getApplicationContext(), "上传失败" + response.getResult().getMessage());
				}
			} else {
				ToastUtils.show(getApplicationContext(), "上传失败" + arg0);
			}
		}

		@Override
		public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
			// TODO Auto-generated method stub

		}
	};
}
