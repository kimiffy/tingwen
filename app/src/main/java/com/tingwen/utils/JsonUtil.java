package com.tingwen.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * json转换工具类
 */
public class JsonUtil {
	private static final String LOG_JSON_ERROR = "com.tingwen.json.util.JsonError";

	private static final String BYTE = "java.lang.Byte";
	private static final String INTEGER = "java.lang.Integer";
	private static final String SHORT = "java.lang.Short";
	private static final String LONG = "java.lang.Long";
	private static final String BOOLEAN = "java.lang.Boolean";
	private static final String CHAR = "java.lang.Character";
	private static final String FLOAT = "java.lang.Float";
	private static final String DOUBLE = "java.lang.Double";

	private static final String VALUE_BYTE = "byte";
	private static final String VALUE_INTEGER = "int";
	private static final String VALUE_SHORT = "short";
	private static final String VALUE_LONG = "long";
	private static final String VALUE_BOOLEAN = "boolean";
	private static final String VALUE_CHAR = "char";
	private static final String VALUE_FLOAT = "float";
	private static final String VALUE_DOUBLE = "double";

    private static final String SEP1 = "#";
    private static final String SEP2 = "|";
    private static final String SEP3 = "=";

	public static <T> T toObject(String json, Class<T> cls) {
		T obj = null;
		try {
			JSONObject jsonObject = new JSONObject(json);
			obj = cls.newInstance();
			Field[] fields = cls.getDeclaredFields();
			for (Field field : fields) {
				if (Modifier.isFinal(field.getModifiers())
						|| Modifier.isPrivate(field.getModifiers())) {
					continue;
				}
				try {
					String key = field.getName();
					if (jsonObject.get(key) == JSONObject.NULL
							|| jsonObject.get(key) == null) {
						field.set(obj, "");
					} else {
						Object value = getValue4Field(jsonObject.get(key),
								jsonObject.get(key).getClass().getName());
						field.set(obj, value);
					}
				} catch (JSONException e) {
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}

	public static <T> List<T> toObjectList(String json, Class<T> cls) {
		List<T> list = new ArrayList<>();
		try {
			List<String> jsonStrList = toJsonStrList(json);
			for (String jsonStr : jsonStrList) {
				T obj = toObject(jsonStr, cls);
				list.add(obj);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

//    public static <T> List<T> toObjectList_Comment(String json, Class<T> cls) {
//		List<T> list = new ArrayList<T>();
//		try {
//			List<String> jsonStrList = toJsonStrList(json);
//			for (String jsonStr : jsonStrList) {
//				T obj = toObject(jsonStr, cls);
//				CommentBean comment = (CommentBean) obj;
//				if (!comment.equals("0")) {
//					if (!comment.getTo_user_nicename().equals("")) {
//						comment.setContent("回复@" + comment.getTo_user_nicename() + ": " + comment.getContent()) ;
//						comment.setEndIndex(comment.getTo_user_nicename().length() + 3);
//					} else {
//                        comment.setContent("回复@" + comment.getTo_user_login() + ": " + comment.getContent()) ;
//                        comment.setEndIndex(comment.getTo_user_login().length() + 3);
//					}
//
//				}
//				if(!comment.getAvatar().equals("")) {
//					if(!comment.getAvatar().contains("http")) {
//                        comment.setAvatar(UrlProvider.URL_IMAGE_USER + comment.getAvatar());
//					}
//				}
//				list.add(obj);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return list;
//	}

	public static List<String> toJsonStrList(String json) {
		List<String> strList = new ArrayList<>();
		try {
			JSONArray jsonArray = new JSONArray(json);
			for (int i = 0; i < jsonArray.length(); i++) {
				String jsonStr = jsonArray.getString(i);
				strList.add(jsonStr);
        }
		} catch (JSONException e) {
		}
		return strList;
	}

	private static Object getValue4Field(Object orginalValue, String typeName) {

		Object value = orginalValue.toString();
		if (typeName.equals(BYTE) || typeName.equals(VALUE_BYTE)) {
			value = Byte.class.cast(orginalValue);
		}
		if (typeName.equals(INTEGER) || typeName.equals(VALUE_INTEGER)) {
			value = Integer.class.cast(orginalValue);
		}
		if (typeName.equals(SHORT) || typeName.equals(VALUE_SHORT)) {
			value = Short.class.cast(orginalValue);
		}
		if (typeName.equals(LONG) || typeName.equals(VALUE_LONG)) {
			value = Long.class.cast(orginalValue);
		}
		if (typeName.equals(BOOLEAN) || typeName.equals(VALUE_BOOLEAN)) {
			value = Boolean.class.cast(orginalValue);
		}
		if (typeName.equals(CHAR) || typeName.equals(VALUE_CHAR)) {
			value = Character.class.cast(orginalValue);
		}
		if (typeName.equals(FLOAT) || typeName.equals(VALUE_FLOAT)) {
			value = Float.class.cast(orginalValue);
		}
		if (typeName.equals(DOUBLE) || typeName.equals(VALUE_DOUBLE)) {
			value = Double.class.cast(orginalValue);
		}
		return value;
	}
}
