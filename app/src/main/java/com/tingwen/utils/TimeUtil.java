package com.tingwen.utils;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * @author danding 2014-8-11
 *         名称：TimeUtil.java
 *         描述：时间转化类
 */
public class TimeUtil {

    public static String dateFormat(double dou) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String format = sdf.format(dou);
        return format;
    }

    public static String getShortTime(String time) {
        String shortstring = null;
        long now = Calendar.getInstance().getTimeInMillis();
        Date date = getDateByString(time);
        if (date == null) return shortstring;
        long deltime = (now - date.getTime()) / 1000;

        if (deltime < 60) {
            shortstring = deltime + "秒前";
        } else if (deltime > 60 && deltime < 60 * 60) {
            shortstring = (int) (deltime / (60)) + "分钟前";
        } else if (deltime < 24 * 60 * 60 && deltime > 60 * 60) {
            shortstring = (int) (deltime / (60 * 60)) + "小时前";
        } else if (deltime > 24 * 60 * 60 && deltime < 24 * 60 * 60 * 2) {
            shortstring = "昨天" + getTime(time);
        } else if (deltime > 24 * 60 * 60 * 2 && deltime < 24 * 60 * 60 * 4) {
            int day = (int) (deltime / (24 * 60 * 60));
            shortstring = day + "天前";
        } else if (deltime > 24 * 60 * 60 * 4 && deltime < 24 * 60 * 60 * 100) {
            shortstring = getDateTime(time);
        } else {
            shortstring = getDate(time);
        }

        return shortstring;
    }


    public static String getShortTime(long data) {
        String shortstring;
        String time = getStringTime(data);
        long now = Calendar.getInstance().getTimeInMillis();
        Date date = getDateByString(time);
        long deltime = (now - date.getTime()) / 1000;
        if (deltime < 60) {
            shortstring = deltime + "秒前";
        } else if (deltime > 60 && deltime < 60 * 60) {
            shortstring = (int) (deltime / (60)) + "分钟前";
        } else if (deltime < 24 * 60 * 60 && deltime > 60 * 60) {
            shortstring = (int) (deltime / (60 * 60)) + "小时前";
        } else if (deltime > 24 * 60 * 60 && deltime < 24 * 60 * 60 * 2) {
            shortstring = "昨天" + getTime(time);
        } else if (deltime > 24 * 60 * 60 * 2 && deltime < 24 * 60 * 60 * 4) {
            int day = (int) (deltime / (24 * 60 * 60));
            shortstring = day + "天前";
        } else if (deltime > 24 * 60 * 60 * 4 && deltime < 24 * 60 * 60 * 365) {
            shortstring = getDateTime(time);
        } else {
            shortstring = getDate(time);
        }

        return shortstring;
    }

    /**
     * @param date
     * @return
     * @描述：获取年月日
     * @时间 2014-12-15
     */
    public static String getDateTime(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");
        return sdf.format(strToDate(date));
    }

    public static Date getDateByString(String time) {
        Date date = null;
        if (time == null) return date;
        String date_format = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat format = new SimpleDateFormat(date_format);
        try {
            date = format.parse(time);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }


    /**
     * @param date
     * @return
     * @描述：获取年月日
     * @时间 2014-12-15
     */
    public static String getDate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(strToDate(date));
    }

    /**
     * @param date
     * @return
     * @描述：获取日期
     * @时间 2015-1-11
     */
    public static String getDateDetail(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
        return sdf.format(strToDate(date));
    }

    /**
     * @param date
     * @return
     * @描述：获取时间
     * @时间 2014-12-15
     */
    public static String getTime(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        return sdf.format(strToDate(date));
    }

    /**
     * 将字符串转化为时间
     *
     * @param str
     * @return
     */
    public static Date strToDate(String str) {
        // sample：Tue May 31 17:46:55 +0800 2011
        // E：周 MMM：字符串形式的月，如果只有两个M，表示数值形式的月 Z表示时区（＋0800）
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date result = null;
        try {
            result = sdf.parse(str);
        } catch (Exception e) {
        }
        return result;

    }

    /**
     * @param date
     * @return
     * @描述：获取时间
     * @时间 2014-12-15
     */
    public static String getTimeAiyi(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return sdf.format(strToDate(date));
    }


    /**
     * @param ld
     * @return 2014-9-19
     * @描述：将毫秒值转为时间
     */
    public static String second2Time(long ld) {
        Date date = new Date(ld);
        //标准日历系统类
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        //java.text.SimpleDateFormat，设置时间格式
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        //得到毫秒值转化的时间
        String time = format.format(gc.getTime());
        return time;
    }

    public static String getShortTime2(long data) {
        String shortstring = null;
        String time = getStringTime(data);
        long now = Calendar.getInstance().getTimeInMillis();
        Date date = getDateByString(time);
        if (date == null) return shortstring;
        long deltime = (now - date.getTime()) / 1000;
        if (deltime > 365 * 24 * 60 * 60) {
            shortstring = getStringTime2(data);
        } else if (deltime > 24 * 60 * 60) {
            shortstring = getStringTime2(data);
        } else if (deltime > 60 * 60) {
            shortstring = (int) (deltime / (60 * 60)) + "小时前";
        } else if (deltime > 60) {
            shortstring = (int) (deltime / (60)) + "分前";
        } else if (deltime > 1) {
            shortstring = deltime + "秒前";
        } else {
            shortstring = "1秒前";
        }
        return shortstring;
    }

    public static String getShortTime3(long data) {
        String shortstring = null;
        String time = getStringTime(data);
        long now = Calendar.getInstance().getTimeInMillis();
        Date date = getDateByString(time);
        if (date == null) return shortstring;
        long deltime = (now - date.getTime()) / 1000;
        if (deltime > 365 * 24 * 60 * 60) {
            shortstring = getStringTime3(data);
        } else if (deltime > 24 * 60 * 60) {
            shortstring = getStringTime3(data);
        } else if (deltime > 60 * 60) {
            shortstring = (int) (deltime / (60 * 60)) + "小时前";
        } else if (deltime > 60) {
            shortstring = (int) (deltime / (60)) + "分前";
        } else if (deltime > 1) {
            shortstring = deltime + "秒前";
        } else {
            shortstring = "1秒前";
        }
        return shortstring;
    }

    public static String getStringTime(long time) {
        String date_format = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat format = new SimpleDateFormat(date_format);
        return format.format(time);
    }

    public static String getStringTime2(long time) {
        String date_format = "yy/MM/dd";
        SimpleDateFormat format = new SimpleDateFormat(date_format);
        return format.format(time);
    }

    public static String getStringTime3(long time) {
        String date_format = "yyyy.MM.dd";
        SimpleDateFormat format = new SimpleDateFormat(date_format);
        return format.format(time);
    }


    public static String getTimeMouth_(String time) {
        long longTime = getTimeMillis(time);
        return getTimeMouth_(longTime);
    }

    public static String getTimeMouth_(long time) {
        Date date = new Date(time);
        String strDay = "";
        String strMouth = "";
        int day = date.getDate();
        int mouth = date.getMonth() + 1;
        if (day < 10) {
            strDay = "0" + day;
        } else {
            strDay = day + "";
        }
        if (mouth < 10) {
            strMouth = "0" + mouth;
        } else {
            strMouth = mouth + "";
        }
        return strDay + " " + strMouth + "月";
    }

    public static long getTimeMillis(String time) {
        long times = 0;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss");
            TimeZone zone = TimeZone.getTimeZone("GMT+8");
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
        return getShortTime(longTime);
    }

    @SuppressLint("SimpleDateFormat")
    public static String setTimeFormat(int strtime) {
        Date date = new Date(strtime);
        SimpleDateFormat format = new SimpleDateFormat("mm:ss");
        return format.format(date);
    }

}
