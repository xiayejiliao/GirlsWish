package com.tongjo.girlswish.ui;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;

import org.apache.http.Header;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.TextHttpResponseHandler;
import com.tongjo.bean.TJResponse;
import com.tongjo.bean.TJUserInfo;
import com.tongjo.girlswish.R;
import com.tongjo.girlswish.utils.AppConstant;
import com.tongjo.girlswish.utils.ImageFileUtils;
import com.tongjo.girlswish.utils.ImageUtils;
import com.tongjo.girlswish.utils.SexUtils;
import com.tongjo.girlswish.utils.SpUtils;
import com.tongjo.girlswish.utils.StringUtils;
import com.tongjo.girlswish.utils.ToastUtils;
import com.tongjo.girlswish.widget.ButtonWithArrow;
import com.tongjo.girlwish.data.DataContainer;

/**
 * 用户基本信息页面，修改头像也统一在这边修改 Copyright 2015
 * 
 * @author preparing
 * @date 2015-7-12
 */
public class UserBasicInfoActivity extends BaseActivity {
	private final String TAG = "AddWishActivity";
	private ButtonWithArrow mNickName;
	private ButtonWithArrow mSex;
	private ButtonWithArrow mSchool;
	private ButtonWithArrow mPhone;
	private ImageView avatar;
	private Button exit;

	public static final String IMAGE_UNSPECIFIED = "image/*";
	public static final int TAKE = 0; // 拍摄图片
	public static final int PHOTO = 1; // 读取图片
	public static final int PHOTOZOOM = 2;// 缩放图片

	// use for dialog
	private final static int avatarDialog = 0x10;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_userbasicinfo);

		setTitle(getResources().getString(R.string.basicinfo));
		InitView();
		InitData();
	}

	public void InitView() {
		mNickName = (ButtonWithArrow) findViewById(R.id.userbasicinfo_nickname);
		mSex = (ButtonWithArrow) findViewById(R.id.userbasicinfo_sex);
		mSchool = (ButtonWithArrow) findViewById(R.id.userbasicinfo_school);
		mPhone = (ButtonWithArrow) findViewById(R.id.userbasicinfo_phone);
		avatar = (ImageView) findViewById(R.id.avatar);
		exit = (Button) findViewById(R.id.exit);
		avatar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				DialogFragment newFragment = MyDialogFragment
						.newInstance(avatarDialog);
				newFragment.show(getFragmentManager(), "dialog");
			}

		});
		exit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				asyncHttpClient.post(AppConstant.URL_BASE+AppConstant.URL_LOGOUT, logouTextHttpResponseHandler);
				
			}
		});
	}
	private TextHttpResponseHandler logouTextHttpResponseHandler=new TextHttpResponseHandler("UTF-8") {
		
		@Override
		public void onSuccess(int arg0, Header[] arg1, String arg2) {
			// TODO Auto-generated method stub
			if(arg0==200){
				Type type = new TypeToken<TJResponse<TJUserInfo>>() {
				}.getType();
				TJResponse<TJUserInfo> response= new Gson().fromJson(arg2, type);
				if(response.getResult().getCode()==0){
					SpUtils.put(getApplicationContext(), AppConstant.USER_ID, "");
					SpUtils.put(getApplicationContext(), AppConstant.USER_SEX, 0);
					SpUtils.put(getApplicationContext(), AppConstant.USER_PHONE, "");
					SpUtils.put(getApplicationContext(), AppConstant.USER_NAME, "");
					SpUtils.put(getApplicationContext(), AppConstant.USER_LOGINSTATE,0);
					SpUtils.put(getApplicationContext(), AppConstant.USER_ICONURL,"");
					SpUtils.put(getApplicationContext(), AppConstant.USER_SCHOOLNAME,"");
					setResult(AppConstant.FORRESULT_MEINFO_LOGOUT);
					finish();
					
				}
			}
		}
		
		@Override
		public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
			// TODO Auto-generated method stub
			
		}
	};
	public void InitData() {
		if (DataContainer.userInfo != null) {
			if (!StringUtils.isBlank(DataContainer.userInfo.getNickname())) {
				mNickName.setRightText(DataContainer.userInfo.getNickname());
			}
			if (!StringUtils.isBlank(DataContainer.userInfo.getTel())) {
				mPhone.setRightText(DataContainer.userInfo.getTel());
			}
			mSex.setRightText(SexUtils.getSexString(DataContainer.userInfo
					.getGender()));

			if (!StringUtils.isBlank(DataContainer.userInfo.getSchoolName())) {
				mSchool.setRightText(DataContainer.userInfo.getSchoolName());
			}

			if (!StringUtils.isBlank(DataContainer.userInfo.getAvatarUrl())) {
				Bitmap bit = ImageFileUtils
						.readBitmapFromLocal(DataContainer.userInfo
								.getAvatarUrl());
				if (bit != null) {
					avatar.setImageBitmap(ImageUtils.getRoundCornerDrawable(
							bit, 2000));
				}
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// 拍摄图片的返回结果
		if (requestCode == TAKE) {
			if (resultCode == RESULT_OK) {
				Bundle bundle = data.getExtras();
				Bitmap bitmap = (Bitmap) bundle.get("data");
				try {
					if (bitmap != null) {
						ImageFileUtils.SaveBitmap(bitmap);
					} else {
						Toast.makeText(UserBasicInfoActivity.this, "没有图片",
								Toast.LENGTH_LONG).show();
					}
					File temp = new File(ImageFileUtils.current_picturepath);
					startPhotoZoom(Uri.fromFile(temp));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		// 读取图片的返回结果
		if (requestCode == PHOTO) {
			if (resultCode == RESULT_OK) {
				Uri uri = data.getData();
				startPhotoZoom(uri);
			}
		}
		// 处理图片返回
		else if (requestCode == PHOTOZOOM) {
			if (resultCode == RESULT_OK) {
				Bundle extras = data.getExtras();
				if (extras != null) {
					Bitmap bit = extras.getParcelable("data");

					if (bit != null) {
						try {
							ImageFileUtils.SaveBitmap(bit);
						} catch (IOException e) {
							e.printStackTrace();
						}

					} else {
						ToastUtils.show(UserBasicInfoActivity.this, "没有图片");
					}
				}
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	public void startPhotoZoom(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, IMAGE_UNSPECIFIED);
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 256);
		intent.putExtra("outputY", 256);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, PHOTOZOOM);
	}

	/**
	 * dialog class
	 * */
	public static class MyDialogFragment extends DialogFragment {
		public static MyDialogFragment newInstance(int title) {
			MyDialogFragment frag = new MyDialogFragment();
			Bundle args = new Bundle();
			args.putInt("title", title);
			frag.setArguments(args);
			return frag;
		}

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			int title = getArguments().getInt("title");
			switch (title) {
			case avatarDialog: {
				Builder builder = new AlertDialog.Builder(getActivity());
				builder.setTitle("设置用户头像");
				String[] list = { "拍照", "从相册中选择" };
				builder.setSingleChoiceItems(list, -1, avatarListener);
				return builder.create();
			}
			}
			return null;
		}

		// avatarDialog click
		private DialogInterface.OnClickListener avatarListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int position) {
				if (position == 0) {
					Intent intent = new Intent(
							android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
					intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
					startActivityForResult(intent, TAKE);
				} else if (position == 1) {
					Intent intent = new Intent();
					intent.setType("image/*");
					/* 使用Intent.ACTION_GET_CONTENT这个Action */
					intent.setAction(Intent.ACTION_GET_CONTENT);
					/* 取得相片后返回本画面 */
					startActivityForResult(intent, PHOTO);
				}
				dialog.dismiss();
			}
		};

	}

}
