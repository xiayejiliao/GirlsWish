package com.tongjo.girlswish.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.provider.ContactsContract.Contacts.Data;

/**
 * TimeUtils
 * 
 * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a> 2013-8-24
 */
public class TimeUtils {

	// "yyyy-MM-dd HH:mm:ss"
	public static final SimpleDateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	// "yyyy-MM-dd"
	public static final SimpleDateFormat DATE_FORMAT_DATE = new SimpleDateFormat("yyyy-MM-dd");

	private TimeUtils() {
		throw new AssertionError();
	}

	/**
	 * long time to string
	 * 
	 * @param timeInMillis
	 * @param dateFormat
	 * @return
	 */
	public static String getTime(long timeInMillis, SimpleDateFormat dateFormat) {
		return dateFormat.format(new Date(timeInMillis));
	}

	/**
	 * long time to string, format is {@link #DEFAULT_DATE_FORMAT}
	 * 
	 * @param timeInMillis
	 * @return
	 */
	public static String getTime(long timeInMillis) {
		return getTime(timeInMillis, DEFAULT_DATE_FORMAT);
	}

	/**
	 * get current time in milliseconds
	 * 
	 * @return
	 */
	public static long getCurrentTimeInLong() {
		return System.currentTimeMillis();
	}

	/**
	 * get current time in milliseconds, format is {@link #DEFAULT_DATE_FORMAT}
	 * 
	 * @return
	 */
	public static String getCurrentTimeInString() {
		return getTime(getCurrentTimeInLong());
	}

	/**
	 * get current time in milliseconds
	 * 
	 * @return
	 */
	public static String getCurrentTimeInString(SimpleDateFormat dateFormat) {
		return getTime(getCurrentTimeInLong(), dateFormat);
	}

	/** "yyyy-MM-dd HH:mm:ss"格式时间比较 */
	public static long minuteCompare(String time1, String time2) {
		try {
			Date date1 = DEFAULT_DATE_FORMAT.parse(time1);
			Date date2 = DEFAULT_DATE_FORMAT.parse(time2);
			long l = date1.getTime() - date2.getTime();
			long day = l / (24 * 60 * 60 * 1000);
			long hour = (l / (60 * 60 * 1000) - day * 24);
			long min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);
			return min;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * 长整型 时间戳 转 字符串
	 * 
	 * @param longNum
	 *            时间戳, long型数据
	 * @param dateFormat
	 *            日期格式 ,字符串数据,如传入为null则默认为"yyyy年MM月dd日HH:mm:ss"
	 * @return 时间戳对应的字符串
	 * @author timloong@foxmail.com
	 */
	public static String longToString(long longNum, String dateFormat) {
		// 判断日期格式,为空则默认定义
		if (dateFormat == null) {
			dateFormat = "yyyy年MM月dd日HH:mm:ss";
		}
		// 根据格式创建格式转换对象
		SimpleDateFormat format = new SimpleDateFormat(dateFormat);
		// 根据时间戳得到日期对象
		Date date = new Date(longNum);
		// 将日期传入格式转换对象进行格式化,并返回
		return format.format(date);
	}

	/**
	 * 
	 * @param date
	 *            ,Date对象,为null则获取系统当前日期.
	 * @param dateFormat
	 *            日期格式,为null则默认为"yyyy年MM月dd日HH:mm:ss".
	 * @return 返回 Date 日期对象对应的字符串
	 */
	public static String dateToString(Date date, SimpleDateFormat dateFormat) {
		// 传入Date对象为null则默认获取系统当前时间
		if (date == null) {
			date = new Date();
		}
		// 返回 Date对应的字符串
		return dateFormat.format(date);
	}

	/**
	 * Date日期对象转换为时间戳.
	 * 
	 * @param date
	 *            ,Date日期对象.
	 * @return Date对象对应的时间戳.
	 */
	public static long datetoLong(Date date) {
		// Date对象为null则获取系统当前Date
		if (date == null) {
			date = new Date();
		}
		// 返回Date对象对应的时间戳
		return date.getTime();
	}

	/**
	 * 时间字符串解析为Date对象
	 * 
	 * @param dateFormat
	 *            与字符串对应的格式 ,如 "yyyy年MM月dd日HH:mm:ss",可增删某段,或者修改汉字占位符
	 * @param timeString
	 *            时间字符串
	 * @return Date对象
	 */
	public static Date stringtoDate(SimpleDateFormat dateFormat, String timeString) {
		// 时间字符串不为null则转换
		if (timeString != null) {
			try {
				// 解析时间字符串并返回
				return dateFormat.parse(timeString);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 
	 * @Title: stringtoLong
	 * @Description: TODO
	 * @param dateFormat
	 * @param timeString
	 * @return long
	 * @throws
	 */
	public static long stringtoLong(SimpleDateFormat dateFormat, String timeString) {
		return datetoLong(stringtoDate(dateFormat, timeString));
	}

	/**
	 * 距离现在时刻天数计算
	 * 
	 * @param time
	 *            原始时刻时间戳
	 * @return 天数
	 */
	public static int dayCompute(long time) {
		// 获取系统现在时间
		Date date = new Date();
		// 获取系统时间对应的时间戳
		long time2 = date.getTime();
		// 返回距离天数
		return (int) ((time2 - time) / (1000 * 60 * 60 * 24));

	}

	/**
	 * 
	 * @Title: getdefaulttime
	 * @Description: 返回X分钟，X小时，X天，大于7天
	 * @param dateFormat
	 * @param timeString
	 * @return String
	 * @throws
	 */
	/**
	 * 
	 * @Title: getdefaulttime
	 * @Description: 杩斿洖X鍒嗛挓锛孹灏忔椂锛孹澶╋紝澶т簬7澶?
	 * @param dateFormat
	 * @param timeString
	 * @return String
	 * @throws
	 */
	public static String getdefaulttime(SimpleDateFormat dateFormat, String timeString) {
		if (StringUtils.isEmpty(timeString)) {
			return "--";
		}

		long comparetime = stringtoLong(dateFormat, timeString);

		long currenttime = getCurrentTimeInLong();
		long todayzero = getTodayZeroTime();
		long yesterdayzero = getYesterdZeroTime();
		long sevenbeforezero = getSevenbeforeZeroTime();
		if (comparetime >= currenttime) {
			return "--";
		}
		if (comparetime < currenttime && comparetime >= todayzero) {
			SimpleDateFormat format = new SimpleDateFormat("HH:mm");
			return format.format(new Date(comparetime));
		}
		if (comparetime < todayzero && comparetime >= yesterdayzero) {
			return "昨天";
		}
		if (comparetime < yesterdayzero && comparetime >= sevenbeforezero) {
			SimpleDateFormat format = new SimpleDateFormat("EE");
			return format.format(new Date(comparetime));
		}
		if (comparetime < sevenbeforezero) {
			SimpleDateFormat format = new SimpleDateFormat("M/dd");
			return format.format(new Date(comparetime));
		}
		return "--";
	}

	public static long getTodayZeroTime() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		return calendar.getTime().getTime();
	}

	public static long getYesterdZeroTime() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		return calendar.getTime().getTime();
	}

	public static long getSevenbeforeZeroTime() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -7);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		return calendar.getTime().getTime();
	}

}
