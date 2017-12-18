package com.tingwen.utils;

import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import com.tingwen.net.UrlProvider;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * 图片工具类
 */
public class ImageUtil {
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] { split[1] };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String getDocumentId(Uri documentUri) {
        final List<String> paths = documentUri.getPathSegments();
        if (paths.size() >= 2 && "document".equals(paths.get(0))) {
            return paths.get(1);
        }
        if (paths.size() >= 4 && "tree".equals(paths.get(0)) && "document".equals(paths.get(2))) {
            return paths.get(3);
        }
        throw new IllegalArgumentException("Invalid URI: " + documentUri);
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = { column };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * 95. * @param uri The Uri to check. 96. * @return Whether the Uri
     * authority is ExternalStorageProvider. 97.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * 103. * @param uri The Uri to check. 104. * @return Whether the Uri
     * authority is DownloadsProvider. 105.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * 111. * @param uri The Uri to check. 112. * @return Whether the Uri
     * authority is MediaProvider. 113.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * 119. * @param uri The Uri to check. 120. * @return Whether the Uri
     * authority is Google Photos. 121.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    public static String changeSuggestImageAddress(String imageUrl){
        if (imageUrl != null && !imageUrl.contains("ttp:")) {
            if(!imageUrl.contains("data")) {
                imageUrl = UrlProvider.URL_IMAGE + "/data/upload/avatar/" + imageUrl;
            }else {
                imageUrl = UrlProvider.URL_IMAGE + "/"+imageUrl;

            }
        }
        return imageUrl;
    }

    public static String changeSuggestImageAddress_(String imageUrl){
        if (imageUrl != null && !imageUrl.contains("ttp:")) {
            if(!imageUrl.contains("data")) {
                imageUrl = UrlProvider.URL_IMAGE + "/data/upload/" + imageUrl;
            }else{
                imageUrl = UrlProvider.URL_IMAGE + "/"+imageUrl;
            }
        }
        return imageUrl;
    }

    /**
     * 光给新闻详情里面的大图做适配的
     * @param url
     * @return
     */
    public static String changeNewsImageAddress(String url){
        if(url != null){
            if(!url.contains("ttp:")){
                url = UrlProvider.URL_IMAGE + url;
            }
        }
        return url;
    }

    /**
     * 复杂的图片地址转换
     * @return
     */
    public static String changeComplexAddress(String url){
        String newUrl = "";
        try {
            JSONObject jsonObject = new JSONObject(url);
            newUrl = jsonObject.getString("thumb");
            if(!newUrl.contains("ttp:")){
                newUrl = UrlProvider.URL_IMAGE + newUrl;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  newUrl;
    }
}
