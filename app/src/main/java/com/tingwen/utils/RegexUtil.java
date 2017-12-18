package com.tingwen.utils;

/**
 * 
 * 名称：RegexUtil.java
 * 描述：正则表达式验证(手机号码) *
 *
 */
public class RegexUtil {
	/**
	 * 电话号码匹配(包括固话和手机号码)
	 */
	public static String regex_phone = "(0\\d{2,3}\\d{7,8})|(1[34578]\\d{9})";
	/**
	 * 密码匹配
	 */
	public static String regex_psw = "\\w{6,16}";
	/**
	 * 邮政编码匹配
	 */
	public static String regex_ems = "\\d{6}";
	/**
	 * 邮箱匹配
	 */
	public static String regex_email = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
	/**
	 * 密码匹配
	 */
	public static String regex_user = "^[a-zA-Z]\\w{6,16}$";
	public static String reg_url ="^([a-z]|[A-Z]|[0-9]|[\u2E80-\u9FFF]){3,}|@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[\\w](?:[\\w-]*[\\w])?|[wap.]{4}|[www.]{4}|[blog.]{5}|[bbs.]{4}|[.com]{4}|[.cn]{3}|[.net]{4}|[.org]{4}|[http://]{7}|[ftp://]{6}$";
	/**
	 * 昵称
	 */
	public static String regex_nick = "[\u4e00-\u9fa5\\w]+";
	/**
	 * 描述：验证是否正确的昵称
	 * 日期：2014-12-8
	 * @param str
	 * @return
	 */
	public static boolean isNick(String str) {
		boolean isNick = false;
		if (str.matches(regex_nick)) {
			isNick = true;
		}
		return isNick;
	}
	/**
	 * 描述：验证是否正确的手机号码
	 * 日期：2014-12-8
	 * @param str
	 * @return
	 */
	public static boolean isPhoneNumber(String str) {
		boolean isPhoneNumber = false;
		if (str.matches(regex_phone)) {
			isPhoneNumber = true;
		}
		return isPhoneNumber;
	}
	/**
	 * 描述：是否正确的邮箱地址
	 * 日期：2014-12-8
	 * @param str
	 * @return
	 */
	public static boolean isEmail(String str) {
		boolean isEmail = false;
		if (str.matches(regex_email)) {
			isEmail = true;
		}
		return isEmail;
	}
	/**
	 * 描述：验证是否是正确的密码格式
	 * 日期：2014-12-8
	 * @param str
	 * @return
	 */
	public static boolean isPassword(String str){
		boolean isPassword = false;
		if(str.matches(regex_psw)){
			isPassword = true;
		}
		return isPassword;
	}
	/**
	 * 描述：是否正确的邮政编码
	 * 日期：2014-12-8
	 * @param str
	 * @return
	 */
	public static boolean isEMS(String str){
		boolean isEMS = false;
		if(str.matches(regex_ems)){
			isEMS = true;
		}
		return isEMS;
	}
	/**
	 * 描述：是否正确的用户名
	 * 日期：2014-12-8
	 * @param str
	 * @return
	 */
	public static boolean isUserName(String str){
		boolean isUserName = false;
		if(str.matches(regex_user)){
			isUserName = true;
		}
		return isUserName;
	}
}
