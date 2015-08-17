package com.tongjo.girlswish.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Environment;

/**
 * 专门处理和图片有关的文件存储于读取 Copyright 2015
 * 
 * @author preparing
 * @date 2015-7-13
 */
public class ImageFileUtils {
	private static final String JPEG_FILE_PREFIX = "IMG_";
	private static final String JPEG_FILE_SUFFIX = ".jpg";
	public static final String picturepath = AppConstant.path;
	public static File file;
	public static String current_picturepath = null;

	// 创建一个存储图片的文件
	public static File picturefilecreate(String title, String attribute) {
		File file = null;
		// 没有SD卡，使用默认的
		try {
			if (!hasSdcard())
				file = File.createTempFile(title, attribute);
			else if (isFileExit(picturepath)) {
				file = File.createTempFile(title, attribute, ImageFileUtils.file);
			} else {
				file = File.createTempFile(title, attribute);
			}
		} catch (IOException e) {
			System.out.println("In Create File:");
			e.printStackTrace();
		}
		return file;
	}

	// 存储bitmap型的图片文件到本地
	public static void SaveBitmap(Bitmap bitmap) throws IOException {
		System.out.println("开始创建图片文件");
		// 第一个参数为开头部分，后面为格式，比如：image_212367.jpg
		File file = picturefilecreate("image_", ".jpg");
		FileOutputStream fOut = null;
		try {
			fOut = new FileOutputStream(file);
			// 图片压缩
			bitmap.compress(Bitmap.CompressFormat.JPEG, 70, fOut);
			try {
				fOut.flush();
				// 如果写入成功，就记住文件名
				current_picturepath = file.getAbsolutePath();
				// InsertBitmap(current_picturepath, context);
			} catch (IOException e) {
				e.printStackTrace();
				current_picturepath = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			current_picturepath = null;
		}
		try {
			if (fOut != null) {
				fOut.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 判断SD卡是否存在
	public static boolean hasSdcard() {
		String status = Environment.getExternalStorageState();
		if (status.equals(Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}

	// 判断文件是否存在
	public static boolean isFileExit(String filepath) {
		try {
			file = new File(filepath);
			if (!file.exists()) {
				file.mkdirs();
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static void clear() {
		current_picturepath = null;
	}

	/**
	 * 从本地读取图片
	 * 
	 * @param url
	 * @return
	 */
	public static Bitmap readBitmapFromLocal(String path) {
		Bitmap bitmap = null;
		if (path == null) {
			return null;
		}
		try {
			bitmap = BitmapFactory.decodeFile(path);
			System.out.println("获取到的图片的长度为:" + bitmap.getHeight());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bitmap;
	}

	/**
	 * @throws IOException 
	 * 
	 * @Title: createImageFile
	 * @Description: TODO
	 * @return
	 * @throws IOException
	 *             File
	 * @throws
	 */
	public static File createImageFile() throws IOException{
		// Create an image file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		String imageFileName = JPEG_FILE_PREFIX + timeStamp + "_";
		File albumF = null;
		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
				albumF = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
			} else {
				albumF = Environment.getExternalStorageDirectory();
			}
		} else {
			albumF = Environment.getDataDirectory();
		}
		File imageF = File.createTempFile(imageFileName, JPEG_FILE_SUFFIX, albumF);
		return imageF;
	}
}
