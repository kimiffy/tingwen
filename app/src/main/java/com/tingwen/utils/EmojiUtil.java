package com.tingwen.utils;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.util.Log;

import com.tingwen.R;

import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmojiUtil {
    private static final String REGEX_STR = "\\[e\\](.*?)\\[/e\\]";
    private static final String REGEX_STR2 = "\\[e1\\](.*?)\\[/e1\\]";

    /**
     * 对spanableString进行正则判断，如果符合要求，则以表情图片代替
     */
    public static void dealExpression(Context context, SpannableString spannableString, Pattern patten, int start)
            throws Exception {
        Matcher matcher = patten.matcher(spannableString);
        while (matcher.find()) {
            String key = matcher.group();
            Log.d("Key", key);
            if (matcher.start() < start) {
                continue;
            }
            Field field = R.drawable.class.getDeclaredField("emoji_"
                    + key.substring(key.indexOf("]") + 1, key.lastIndexOf("[")));
            int resId = Integer.parseInt(field.get(null).toString());
            if (resId != 0) {
                Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resId);

                int rawHeight = bitmap.getHeight();
                int rawWidth = bitmap.getWidth();
                int newHeight = 40;
                int newWidth = 40;
                float heightScale = ((float) newHeight) / rawHeight;
                float widthScale = ((float) newWidth) / rawWidth;

                Matrix matrix = new Matrix();
                matrix.postScale(heightScale, widthScale);

                Bitmap bitmap2 = Bitmap.createBitmap(bitmap, 0, 0, rawHeight, rawWidth, matrix, true);

                BitmapDrawable bitmapDrawable = new BitmapDrawable(context.getResources(), bitmap2);
                bitmapDrawable.setBounds(0, 0, bitmapDrawable.getIntrinsicWidth(), bitmapDrawable.getIntrinsicHeight());

                ImageSpan imageSpan = new ImageSpan(bitmapDrawable);

                int end = matcher.start() + key.length();
                spannableString.setSpan(imageSpan, matcher.start(), end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                if (end < spannableString.length()) {
                    dealExpression(context, spannableString, patten, end);
                }
                break;
            }
        }
    }

    /**
     * @param context
     * @param str
     * @return
     * @desc <pre>
     * 解析字符串中的表情字符串替换成表情图片
     * </pre>
     * @author Weiliang Hu
     * @date 2013-12-17
     */
    public static SpannableString getExpressionString(Context context, String str) {
        SpannableString spannableString = new SpannableString(str);
        Pattern sinaPatten = Pattern.compile(REGEX_STR, Pattern.CASE_INSENSITIVE);
        try {
            dealExpression(context, spannableString, sinaPatten, 0);
        } catch (Exception e) {
            Log.e("Emoji", e.getMessage());
        }
        return spannableString;
    }


    /**
     * 表情解析，转换成unicode字符
     *
     * @param cs
     * @param context
     * @return
     */
    public static String convertToMsg(CharSequence cs, Context context) {
        SpannableStringBuilder sb = new SpannableStringBuilder(cs);
        ImageSpan[] imageSpans = sb.getSpans(0, cs.length(), ImageSpan.class);
        for (int i = 0; i < imageSpans.length; i++) {
            ImageSpan imageSpan = imageSpans[i];
            String source = imageSpan.getSource();
            int start = sb.getSpanStart(imageSpan);
            int end = sb.getSpanEnd(imageSpan);
            if (source.contains("[")) {
                sb.replace(start, end, convertUnicode(source));
            }
        }
        sb.clearSpans();
        return sb.toString();
    }

    private static String convertUnicode(String emo) {
        emo = emo.substring(1, emo.length() - 1);
        if (emo.length() < 6) {
            return new String(Character.toChars(Integer.parseInt(emo, 16)));
        }
        String[] emos = emo.split("_");
        char[] char0 = Character.toChars(Integer.parseInt(emos[0], 16));
        char[] char1 = Character.toChars(Integer.parseInt(emos[1], 16));
        char[] emoji = new char[char0.length + char1.length];
        for (int i = 0; i < char0.length; i++) {
            emoji[i] = char0[i];
        }
        for (int i = char0.length; i < emoji.length; i++) {
            emoji[i] = char1[i - char0.length];
        }
        return new String(emoji);
    }


    /**
     * 处理评论中的表情
     *
     * @param str
     * @return
     */
    public static String codeMsg(String str) {
        StringBuilder stringBuilder1 = new StringBuilder();
        char[] ach = str.toCharArray();
        int len = ach.length;
        int[] acp = new int[Character.codePointCount(ach, 0, len)];//获取字符集中的Unicode数量
        int j = 0;
        for (int i = 0, cp; i < len; i += Character.charCount(cp)) {
            cp = Character.codePointAt(ach, i);
            if (Character.charCount(cp) > 1 && i + 2 <= str.length()) {
                String string = str.substring(i, i + 2);
                String codeStr = "";
                try {
                    codeStr = LoginUtil.encode(LoginUtil.AESCODE, string.getBytes());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("[e1]" + codeStr + "[/e1]");
                stringBuilder1.append(stringBuilder.toString());
            } else {
                String string = str.substring(i, i + 1);
                stringBuilder1.append(string);
            }
            acp[j++] = cp;
            int cpCount = Character.charCount(cp);
            Log.i("cpCount", cpCount + "");
        }
        return stringBuilder1.toString();
    }

    public static void decodeMsg(Pattern pattern, StringBuilder stringBuilder) {
        Matcher matcher = pattern.matcher(stringBuilder.toString());

        while (matcher.find()) {
            String key = matcher.group();
            int start = matcher.start();
            int end = start + key.length();
            String content = key.substring(key.indexOf("]") + 1, key.lastIndexOf("["));
            String newContent = "";
            try {
                 newContent =LoginUtil.decryptDES(content,LoginUtil.AESCODE);
            } catch (Exception e) {
                e.printStackTrace();
            }
            stringBuilder.replace(start, end, newContent);
            if (end < stringBuilder.length()) {
                decodeMsg(pattern, stringBuilder);
            }
            break;
        }
    }

    public static StringBuilder getDecodeMsg(String str) {
        StringBuilder stringBuilder = new StringBuilder(str);
        Pattern pattern = Pattern.compile(REGEX_STR2, Pattern.CASE_INSENSITIVE);
        decodeMsg(pattern, stringBuilder);
        return stringBuilder;
    }

}
