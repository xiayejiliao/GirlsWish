package com.tongjo.girlswish.ui;

import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.http.Header;

import android.R.raw;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.tongjo.bean.TJResponse;
import com.tongjo.bean.TJUserInfo;
import com.tongjo.bean.TJWish;
import com.tongjo.girlswish.BaseApplication;
import com.tongjo.girlswish.R;
import com.tongjo.girlswish.model.LoginState;
import com.tongjo.girlswish.model.UserSex;
import com.tongjo.girlswish.utils.AppConstant;
import com.tongjo.girlswish.utils.SpUtils;
import com.tongjo.girlswish.utils.StringUtils;
import com.tongjo.girlswish.utils.ToastUtils;

/**
 * 我的对应的fragment Copyright 2015
 * 
 * @author preparing
 * @date 2015-6-14
 */
public class MainTabMeFragment extends BaseFragment {
	private final static int MESSAGE_WHAT_UPDATE_INFO = 123;
	private final static int MESSAGE_WHAT_UPDATE_WHISH = 1234;

	private static final String TAG = "MainTabMeFragment";
	private Context mcontext;
	private TextView tv_info;
	private ImageView avatar;
	private TextView tv_name;
	private TextView tv_school;
	private ImageView iv_icon;
	private List<TJWish> tjWishs;
	private RadioGroup radioGroup;
	private RadioButton RadioButtonfirst;
	private RadioButton RadioButtonsecond;
	private int sex;
	private int loginstate;
	List<TJWish> wishfirst = new ArrayList<TJWish>();
	List<TJWish> wishsecond = new ArrayList<TJWish>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mcontext = getActivity().getApplicationContext();

		View rootView = inflater.inflate(R.layout.fragment_me, container, false);
		tv_info = (TextView) rootView.findViewById(R.id.tv_fragme_info);
		radioGroup=(RadioGroup)rootView.findViewById(R.id.rg_fragme_chose);
		RadioButtonfirst=(RadioButton)rootView.findViewById(R.id.rb_fragme_first);
		RadioButtonsecond=(RadioButton)rootView.findViewById(R.id.rb_fragme_second);
		tv_name=(TextView)rootView.findViewById(R.id.tv_fragme_name);
		tv_school=(TextView)rootView.findViewById(R.id.tv_fragme_school);
		iv_icon=(ImageView)rootView.findViewById(R.id.iv_fragme_icon);
		
		avatar = (ImageView) rootView.findViewById(R.id.iv_fragme_icon);
		avatar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), UserBasicInfoActivity.class);
				startActivity(intent);
			}
		});
		initRadioGroup();
		return rootView;
	}
	private void initinfo(){
		if(loginstate==1){
			String uuidString=(String) SpUtils.get(mcontext, AppConstant.USER_ID, "11");
			try {
			TJUserInfo tjUserInfo=	tjuserinfoDao.queryForId(UUID.fromString(uuidString));
			tv_name.setText(tjUserInfo.getRealname());
			tv_name.setText(tjUserInfo.getSchool().getName());
			String iconurl= tjUserInfo.getAvatarUrl();
			ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(mcontext);
			ImageLoader.getInstance().init(configuration);
			ImageLoader.getInstance().displayImage(iconurl, iv_icon);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	private void initRadioGroup() {
		if (sex == 0) {
			RadioButtonfirst.setText("未摘取");
			RadioButtonsecond.setText("已摘取");
		} else if (sex == 1) {
			RadioButtonfirst.setText("未完成");
			RadioButtonsecond.setText("已完成");
		}
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				System.out.println("chechk");
				loginstate= (Integer) SpUtils.get(mcontext, AppConstant.USER_LOGINSTATE, 0);
				System.out.println("loginstate:"+loginstate);
				if (loginstate != 1) {
					return;
				}
				switch (checkedId) {
				case R.id.rb_fragme_first:
					setFragment(new MeWishFragment(wishfirst));
					break;
				case R.id.rb_fragme_second:
					setFragment(new MeWishFragment(wishsecond));
					break;

				default:
					break;
				}
			}
		});
	}

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case MESSAGE_WHAT_UPDATE_INFO:

				break;
			case MESSAGE_WHAT_UPDATE_WHISH:
				if(!radioGroup.isActivated()){
					radioGroup.check(R.id.rb_fragme_first);
				}
				switch (radioGroup.getCheckedRadioButtonId()) {
				case R.id.rb_fragme_first:
					System.out.println("frist");
					setFragment(new MeWishFragment(wishfirst));
					break;
				case R.id.rb_fragme_second:
					System.out.println("second");
					setFragment(new MeWishFragment(wishsecond));
					break;
				default:
					break;
				}
				break;

			default:
				break;
			}
		}

	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		sex = 1;
		loginstate = 0;
		

	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (isVisibleToUser == true) {
			asyncHttpClient.get(AppConstant.URL_BASE + AppConstant.URL_WISHLIST, wishListResponse);
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
					wishfirst.clear();
					wishsecond.clear();
					if (tjWishs != null) {
						if (sex == 0) {
							for (TJWish tjWish : tjWishs) {
								if (tjWish.getIsPicked() == 0) {
									wishfirst.add(tjWish);
								} else {
									wishsecond.add(tjWish);
								}
							}
						} else if (sex == 1) {
							for (TJWish tjWish : tjWishs) {
								if (tjWish.getIsCompleted() == 0) {
									wishfirst.add(tjWish);
								} else {
									wishsecond.add(tjWish);
								}
							}
						}
					}
					System.out.println("777777777777");
					handler.obtainMessage(MESSAGE_WHAT_UPDATE_WHISH).sendToTarget();
				}
			}
		}

		@Override
		public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
			System.out.println("========");
		}
	};

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == AppConstant.FORRESULT_LOG && resultCode == AppConstant.FORRESULT_LOG_OK) {
			asyncHttpClient.get(AppConstant.URL_BASE + AppConstant.URL_WISHLIST, wishListResponse);
			tv_info.setVisibility(View.GONE);
		}
		if (requestCode == AppConstant.FORRESULT_LOG && resultCode == AppConstant.FORRESULT_LOG_CANCANL) {
			tv_info.setVisibility(View.VISIBLE);
			tv_info.setText("请登陆");
		}
	};

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

	private void setFragment(MeWishFragment meWishFragment) {
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.replace(R.id.fragment_mewhis, meWishFragment);
		transaction.commit();
	}
}
