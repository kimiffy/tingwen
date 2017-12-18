package com.tingwen.utils;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

@SuppressLint("SimpleDateFormat")
public class TimesUtil {
	public final static long ONE_DAY_TIME = 86400000;//一天时间的毫秒数
    public final static long ONE_HOUR_TIME = 3600000;//一个小时的毫秒数
    public final static long ONE_MINUTE_TIME = 60000;//一分钟的毫秒数
    public final static long ONE_SECOND_TIME = 1000;//一秒钟的毫秒数

	@SuppressLint("SimpleDateFormat")
	public static String setTimeFormat(int strtime) {
		Date date = new Date(strtime);
		SimpleDateFormat format = new SimpleDateFormat("mm:ss");
		return format.format(date);
	}

    /**
     * 获取指定时间戳的时分秒
     * @param time
     * @return
     */
    public static String getTimeHour(long time){
        Date date = new Date(time);
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        return format.format(date);

    }

    /**
     * 获取指定时间戳的时分
     * @param time
     * @return
     */
    public static String getTimeHour_(long time){
        Date date = new Date(time);
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        return format.format(date);

    }


    /**
     * 获取指定时间的时间差（指定之间限当天）
     * @param time
     * @return
     */
    public static int getMaxBar(long time,long time2){
        int position = 0;
        if(time2 == 0) {
            long current = System.currentTimeMillis();
            position = (int) (current - time);
        }else{
            position = (int) (time2 - time);
        }
        return position;
    }

//	@SuppressLint("SimpleDateFormat")
//	public static String setDataFormat(long strtime) {
//		Date date = new Date(strtime * 1000L);
//		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		return format.format(date);
//	}

    public static String getStringTime(long time) {
        String date_format = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat format = new SimpleDateFormat(date_format);
        return format.format(time);
    }


	public static boolean isMoreThanThreeDay(long time) {
		long currentTime = System.currentTimeMillis();
		if ((currentTime - time) > (ONE_DAY_TIME * 3)) {
			return true;
		}
		return false;
	}

	public static boolean isMoreThanFifteenDay(long time) {
		long currentTime = System.currentTimeMillis();
		if ((currentTime - time) > ONE_DAY_TIME * 3 * 5) {
			return true;
		}
		return false;
	}

	public static long getTimeMillis(String time) {
		long times = 0;
		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
            TimeZone zone= TimeZone.getTimeZone("GMT+8");
            simpleDateFormat.setTimeZone(zone);
            Date date = simpleDateFormat.parse(time);
			times = date.getTime();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return times;
	}

	public static String getTimesMessageByTime(String time) {
		long longTime = getTimeMillis(time);
        return getTimesMessageByTime_(longTime);
	}

    public static String getTimesMessageByTime_(long time) {
        String timeString;
        long longTime = time;
        long currentTime = System.currentTimeMillis();

        long dValue = (currentTime - longTime) / 1000;

        if (dValue < 60) {
            timeString = "刚刚";
        } else if (dValue < 24 * 60 * 60 && dValue > 60) {
            int hour = (int) (dValue / (60 * 60));
            int minue = (int) ((dValue % (60 * 60)) / 60);
            if (hour > 0) {
                timeString = hour + "小时前";
            } else {
                timeString = minue + "分钟前";
            }
        } else if (dValue > 24 * 60 * 60 && dValue < 24 * 60 * 60 * 2) {
            timeString = "昨天" + getTimeHour_(longTime);
        }else if(dValue > 24 * 60 * 60 * 2 && dValue < 24 * 60 * 60 * 4){
            int day = (int) (dValue / (24 * 60 * 60));
            timeString = day + "天前";
        }else if(dValue > 24 * 60 * 60 * 4 && dValue < 24 * 60 * 60 * 30) {
            timeString = getTimeMouth(longTime);
        }else{
            timeString = getStringTime(longTime);
        }
        return timeString;
    }

	public static String getCurrentTime() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(date);
	}

    /**
     * 获取当前时间（年月日）
     * @return
     */
    public static String getCurrentTimeYear() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    /**
     * 获取当前时间（年月日 时分）
     * @return
     */
    public static String getTimeMouth(long time) {
        Date date = new Date(time);
        SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm");
        return format.format(date);
    }

    /**
     * 2311月
     * @param time
     * @return
     */
    public static String getTimeMouth_(String time){
        long longTime = getTimeMillis(time);
        return getTimeMouth_(longTime);
    }
    public static String getTimeMouth_(long time){
        Date date = new Date(time);
        String strDay = "";
        String strMouth = "";
        int day = date.getDate();
        int mouth = date.getMonth() + 1;
        if(day < 10){
            strDay = "0"+day;
        }else{
            strDay = day+"";
        }
        if(mouth < 10){
            strMouth = "0" + mouth;
        }else{
            strMouth = mouth + "";
        }
        return strDay+ " " + strMouth+"月";
    }


    @SuppressLint("SimpleDateFormat")
    public static String setDataFormat(long strtime) {
        Date date = new Date(strtime * 1000L);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(date);
    }
}
