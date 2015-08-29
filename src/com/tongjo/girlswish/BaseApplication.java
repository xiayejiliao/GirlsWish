package com.tongjo.girlswish;

import java.io.File;
import java.util.UUID;

import com.easemob.chat.EMChat;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.SyncHttpClient;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.tongjo.bean.TJLocalUserInfo;
import com.tongjo.bean.TJUserInfo;
import com.tongjo.db.OrmLiteHelper;
import com.tongjo.emchat.GWHXSDKHelper;
import com.tongjo.girlswish.model.LoginState;
import com.tongjo.girlswish.model.UserSex;
import com.tongjo.girlswish.utils.AppConstant;
import com.tongjo.girlswish.utils.SpUtils;
import com.tongjo.girlwish.data.DataContainer;
import com.umeng.analytics.MobclickAgent;

import android.app.Application;
import android.graphics.Bitmap;
import android.util.Log;

/**
 * 基本的Application，暂时无代码，但这个后面肯定是需要用到的 Copyright 2015
 * 
 * @author preparing
 * @date 2015-5-31
 */
public class BaseApplication extends Application {
	private final String TAG = "BaseApplication";
	// 异步网络请求
	private AsyncHttpClient asyncHttpClient;
	// 同步网络请求
	private SyncHttpClient syncHttpClient;
	// 持久化在 SharedPreferences里的cookie，每个app只创建一个
	private PersistentCookieStore persistentCookieStore;

	// 环信
	public static GWHXSDKHelper hxSDKHelper = new GWHXSDKHelper();

	@Override
	public void onCreate() {
		asyncHttpClient = new AsyncHttpClient();
		syncHttpClient = new SyncHttpClient();
		persistentCookieStore = new PersistentCookieStore(getApplicationContext());
		syncHttpClient.setCookieStore(persistentCookieStore);
		asyncHttpClient.setCookieStore(persistentCookieStore);
		OpenHelperManager.getHelper(getApplicationContext(), OrmLiteHelper.class);
		super.onCreate();

		DataContainer.userInfo = LoadLocalUserInfo();
		initImageLoader();
		/** 初始化环信 */
		hxSDKHelper.onInit(this.getApplicationContext());
		//关闭友盟默认的页面统计，友盟的默认统计只统计activity
		MobclickAgent.openActivityDurationTrack(false);
		//设置友盟异常捕获,默认是开启异常捕获的
		//MobclickAgent.setCatchUncaughtExceptions(true);
	}

	private void initImageLoader() {
		DisplayImageOptions options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.image_imageloading) // 设置图片在下载期间显示的图片
				.showImageForEmptyUri(R.drawable.default_image)// 设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(R.drawable.image_imageloaderror) // 设置图片加载/解码过程中错误时候显示的图片
				.cacheInMemory(false)// 设置下载的图片是否缓存在内存中
				.cacheOnDisc(true)// 设置下载的图片是否缓存在SD卡中
				.considerExifParams(true) // 是否考虑JPEG图像EXIF参数（旋转，翻转）
				.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)// 设置图片以如何的编码方式显示
				.bitmapConfig(Bitmap.Config.RGB_565)// 设置图片的解码类型
				// .decodingOptions()//设置图片的解码配置
				// .delayBeforeLoading(int delayInMillis)//延时加载
				// 设置图片加入缓存前，对bitmap进行设置
				// .preProcessor(BitmapProcessor preProcessor)
				.resetViewBeforeLoading(true)// 设置图片在下载前是否重置
				.displayer(new RoundedBitmapDisplayer(180))// 是否设置为圆角，弧度为多少
				.build();

		File cacheDir = StorageUtils.getOwnCacheDirectory(getApplicationContext(), "imageloader/Cache");
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext()).memoryCacheExtraOptions(480, 800) // 即保存的每个缓存文件的最大长宽
				.threadPoolSize(4)// 线程池内加载的数量
				.threadPriority(Thread.NORM_PRIORITY - 2) // 优先级
				.denyCacheImageMultipleSizesInMemory().memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024)) // 内存缓存实现
				// 内存缓存大小
				.memoryCacheSize(2 * 1024 * 1024)
				// 磁盘缓存大小
				.discCacheSize(50 * 1024 * 1024).discCacheFileNameGenerator(new Md5FileNameGenerator()).tasksProcessingOrder(QueueProcessingType.LIFO).discCache(new UnlimitedDiskCache(cacheDir))
				// 配置默认的DisplayImageOptions，这个可以在以后使用时单独为每个显示任务配置。
				.defaultDisplayImageOptions(options)
				.build();
		ImageLoader.getInstance().init(config);
	}

	/**
	 * 从本地加载用户数据 设置多个try/catch在于防止前面一个数据读取出错，后面的数据全部无法读取
	 * 
	 * @return
	 */
	public TJLocalUserInfo LoadLocalUserInfo() {
		TJLocalUserInfo user = new TJLocalUserInfo();
		try {
			String tel = (String) SpUtils.get(getApplicationContext(), AppConstant.USER_PHONE, "12");
			user.setTel(tel);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			String login = (String) SpUtils.get(getApplicationContext(), AppConstant.USER_LOGINSTATE, LoginState.LOGIN);
			user.setLoginStatus(login);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			String userId = (String) SpUtils.get(getApplicationContext(), AppConstant.USER_ID, "7d1bc94c-1019-11e5-bb7f-525400271610");
			user.set_id(UUID.fromString(userId));
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			int sex = (Integer) SpUtils.get(getApplicationContext(), AppConstant.USER_SEX, 0);
			user.setGender(sex);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			String name = (String) SpUtils.get(getApplicationContext(), AppConstant.USER_NAME, "Name");
			user.setNickname(name);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			String school = (String) SpUtils.get(getApplicationContext(), AppConstant.USER_SCHOOLNAME, "School");
			user.setSchoolName(school);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			String avatar = (String) SpUtils.get(getApplicationContext(), AppConstant.USER_ICONURL, "");
			user.setAvatarUrl(avatar);

		} catch (Exception e) {
			e.printStackTrace();
		}
		Log.d(TAG, user.toString());
		return user;
	}

	public AsyncHttpClient getAsyncHttpClient() {
		return asyncHttpClient;
	}

	public void setAsyncHttpClient(AsyncHttpClient asyncHttpClient) {
		this.asyncHttpClient = asyncHttpClient;
	}

	public SyncHttpClient getSyncHttpClient() {
		return syncHttpClient;
	}

	public void setSyncHttpClient(SyncHttpClient syncHttpClient) {
		this.syncHttpClient = syncHttpClient;
	}

	public PersistentCookieStore getPersistentCookieStore() {
		return persistentCookieStore;
	}

	public void setPersistentCookieStore(PersistentCookieStore persistentCookieStore) {
		this.persistentCookieStore = persistentCookieStore;
	}
}
