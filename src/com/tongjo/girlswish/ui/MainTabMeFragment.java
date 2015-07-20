package com.tongjo.girlswish.ui;

import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.http.Header;

import android.R.menu;
import android.R.raw;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.j256.ormlite.dao.Dao;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.tongjo.bean.TJResponse;
import com.tongjo.bean.TJUserInfo;
import com.tongjo.bean.TJWish;
import com.tongjo.bean.TJWishList;
import com.tongjo.girlswish.BaseApplication;
import com.tongjo.girlswish.R;
import com.tongjo.girlswish.model.LoginState;
import com.tongjo.girlswish.model.UserSex;
import com.tongjo.girlswish.utils.AppConstant;
import com.tongjo.girlswish.utils.SpUtils;
import com.tongjo.girlswish.utils.StringUtils;
import com.tongjo.girlswish.utils.ToastUtils;
import com.tongjo.girlswish.widget.CircleImageView;
import com.tongjo.girlwish.data.DataContainer;

import de.greenrobot.event.EventBus;

/**
 * 我的对应的fragment Copyright 2015
 * 
 * @author preparing
 * @date 2015-6-14
 */
public class MainTabMeFragment extends BaseFragment {
	private final static String TAG = "MainTabMeFragment";
	private final static int MESSAGE_WHAT_UPDATE_INFO = 123;
	private final static int MESSAGE_WHAT_UPDATE_WHISH = 1234;

	private Context mcontext;
	private TextView tv_info;
	private ImageView avatar;
	private TextView tv_name;
	private TextView tv_school;
	private CircleImageView iv_icon;
	private List<TJWish> tjWishs;
	private int sex;
	private int loginstate;
	private DisplayImageOptions displayImageOptions;
	private BaseFragment wishsFragment;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mcontext = getActivity().getApplicationContext();

		View rootView = inflater.inflate(R.layout.fragment_me, container, false);
		tv_info = (TextView) rootView.findViewById(R.id.tv_fragme_info);
		tv_name = (TextView) rootView.findViewById(R.id.tv_fragme_name);
		tv_school = (TextView) rootView.findViewById(R.id.tv_fragme_school);
		iv_icon = (CircleImageView) rootView.findViewById(R.id.iv_fragme_icon);
		avatar = (ImageView) rootView.findViewById(R.id.iv_fragme_icon);
		avatar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), UserBasicInfoActivity.class);
				startActivityForResult(intent, AppConstant.FORRESULT_MEINFO);
			}
		});
		updateinfo();
		Log.d(TAG, "oncreatview");
		return rootView;
	}

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case MESSAGE_WHAT_UPDATE_INFO:
				updateinfo();
				break;
			case MESSAGE_WHAT_UPDATE_WHISH:
				updateWish(tjWishs);
				break;

			default:
				break;
			}
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		displayImageOptions = new DisplayImageOptions.Builder().showStubImage(R.drawable.testimg) // 设置图片下载期间显示的图片
				.showImageForEmptyUri(R.drawable.testimg) // 设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(R.drawable.testimg) // 设置图片加载或解码过程中发生错误显示的图片
				.cacheInMemory(true) // 设置下载的图片是否缓存在内存中
				.cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
				.displayer(new RoundedBitmapDisplayer(180))// 设置成圆角图片
				.imageScaleType(ImageScaleType.EXACTLY)
				.build(); // 创建配置过得DisplayImageOption对象
		EventBus.getDefault().register(this);
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);

		Log.d(TAG, "on attach");
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		Log.d(TAG, "isVisibleToUser: " + isVisibleToUser);
		if (isVisibleToUser == true) {
			asyncHttpClient.get(AppConstant.URL_BASE + AppConstant.URL_WISHLIST, wishListResponse);
			ImageLoader.getInstance().loadImage((String) SpUtils.get(mcontext, AppConstant.USER_ICONURL, ""), new SimpleImageLoadingListener() {
				@Override
				public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
					// TODO Auto-generated method stub
					super.onLoadingComplete(imageUri, view, loadedImage);
					iv_icon.setImageBitmap(loadedImage);
				}
			});
			tv_name.setText((String) SpUtils.get(mcontext, AppConstant.USER_NAME, ""));
			tv_school.setText((String) SpUtils.get(mcontext, AppConstant.USER_SCHOOLNAME, ""));
		} else {

		}
	}

	private TextHttpResponseHandler wishListResponse = new TextHttpResponseHandler("UTF-8") {

		@Override
		public void onSuccess(int arg0, Header[] arg1, String arg2) {
			if (arg0 == 200) {
				Type type = new TypeToken<TJResponse<Wishs>>() {
				}.getType();
				TJResponse<Wishs> mywishs = new Gson().fromJson(arg2, type);
				if (mywishs.getResult().getCode() == 1) {
					AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
					builder.setMessage(mywishs.getResult().getMessage()).setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							Intent intent = new Intent(getActivity(), LoginActivity.class);
							startActivityForResult(intent, AppConstant.FORRESULT_LOG);
						}
					}).setNegativeButton("No", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							dialog.cancel();
						}
					}).show();
					AlertDialog alert = builder.create();

				}
				if (mywishs.getResult().getCode() == 0) {
					tjWishs = mywishs.getData().getWishList();
					Log.d(TAG, "wishs size:" + tjWishs.size());
				/*	for (int i = 0; i < tjWishs.size(); i++) {
						TJWish tjWish= tjWishs.get(i);
						Log.d(TAG, "wish" + tjWish.getPickerUser()==null?"null":tjWish.toString());
					}
			*/
					DataContainer.mewishs=new TJWishList(tjWishs);
					handler.obtainMessage(MESSAGE_WHAT_UPDATE_WHISH).sendToTarget();
				}
			}
		}

		@Override
		public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {

		}
	};

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == AppConstant.FORRESULT_LOG && resultCode == AppConstant.FORRESULT_LOG_OK) {
			asyncHttpClient.get(AppConstant.URL_BASE + AppConstant.URL_WISHLIST, wishListResponse);
			tv_info.setVisibility(View.GONE);
			handler.obtainMessage(MESSAGE_WHAT_UPDATE_INFO).sendToTarget();
		}
		if (requestCode == AppConstant.FORRESULT_LOG && resultCode == AppConstant.FORRESULT_LOG_CANCANL) {
			tv_info.setVisibility(View.VISIBLE);
			tv_info.setText("请登陆");
		}
		if (requestCode == AppConstant.FORRESULT_MEINFO && requestCode == AppConstant.FORRESULT_MEINFO_LOGOUT) {
			handler.obtainMessage(MESSAGE_WHAT_UPDATE_INFO).sendToTarget();
			tjWishs.clear();
			handler.obtainMessage(MESSAGE_WHAT_UPDATE_WHISH).sendToTarget();
			Log.d(TAG, "logout");
		}
	};

	private void updateWish(List<TJWish> tjWishs) {
		int sex = (Integer) SpUtils.get(mcontext, AppConstant.USER_SEX, 0);
		switch (sex) {
		case 0:
			setGirlWish(tjWishs);
			break;
		case 1:
			setBoyWish(tjWishs);
			break;

		default:
			break;
		}
	}

	private void updateinfo() {
		String name = (String) SpUtils.get(mcontext, AppConstant.USER_NAME, "--");
		String school = (String) SpUtils.get(mcontext, AppConstant.USER_SCHOOLNAME, "----");
		String iconurl = (String) SpUtils.get(mcontext, AppConstant.USER_ICONURL, "----");
		tv_name.setText(name);
		tv_school.setText(school);
		imageLoader.displayImage(iconurl, iv_icon, displayImageOptions);

	}

	private void setGirlWish(List<TJWish> tjWishs) {
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.replace(R.id.fragment_mewhis, new MeGirlWishFragment(tjWishs));
		transaction.commit();
	}

	private void setBoyWish(List<TJWish> tjWishs) {
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.replace(R.id.fragment_mewhis, new MeBoyWishFragment(tjWishs));
		transaction.commit();
	}

	class Wishs {
		private List<TJWish> wishList;

		public Wishs(List<TJWish> wishList) {
			super();
			this.wishList = wishList;
		}

		public List<TJWish> getWishList() {
			return wishList;
		}

		public void setWishList(List<TJWish> wishList) {
			this.wishList = wishList;
		}

		@Override
		public String toString() {
			return "Wishs [wishList=" + wishList + "]";
		}

	}

	public void onEventMainThread(Message msg) {
		TJWish tjWish = null;
		switch (msg.what) {
		case AppConstant.MESSAGE_WHAT_GIRLWISH_CLICK_FINISH:
			tjWish = (TJWish) msg.obj;
			Intent intent = new Intent(getActivity(), GirlFinishWishActivity.class);
			intent.putExtra("wishuuid", tjWish.get_id().toString());
			startActivity(intent);
			break;
		case AppConstant.MESSAGE_WHAT_GIRLWISH_CLICK_PCIK:
			tjWish = (TJWish) msg.obj;
			Intent intent1 = new Intent(getActivity(), GirlUnderwayWishActivity.class);
			intent1.putExtra("wishuuid", tjWish.get_id().toString());
			System.out.println(msg.obj);
			startActivity(intent1);
			break;
		case AppConstant.MESSAGE_WHAT_GIRLWISH_CLICK_UNPICK:
			tjWish = (TJWish) msg.obj;
			Intent intent2 = new Intent(getActivity(), GirlUnpickedWishActivity.class);
			intent2.putExtra("wishuuid", tjWish.get_id().toString());
			startActivity(intent2);
			break;
		case AppConstant.MESSAGE_WHAT_BOYWISH_CLICK_COMPLETE:
			tjWish = (TJWish) msg.obj;
			Intent intent3 = new Intent(getActivity(), BoyCompleteWishActivity.class);
			intent3.putExtra("wishuuid", tjWish.get_id().toString());
			startActivity(intent3);
			break;
		case AppConstant.MESSAGE_WHAT_BOYWISH_CLICK_UNCOMPLETE:
			tjWish = (TJWish) msg.obj;
			Intent intent4 = new Intent(getActivity(), BoyUncompleteWishActivity.class);
			intent4.putExtra("wishuuid", tjWish.get_id().toString());
			startActivity(intent4);
			break;
		default:
			break;
		}

	}
}
