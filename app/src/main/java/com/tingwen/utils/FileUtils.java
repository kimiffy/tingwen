package com.tingwen.utils;


import com.tingwen.app.GlobalContext;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 描述: 文件处理工具类
 * 名称: FileUtils
 */
public class FileUtils {

    public static final String TAG = FileUtils.class.getSimpleName();

    /**
     * 读取assert文件
     *
     * @param file
     * @return
     */
    public static String readAssetsFile(String file) {
        try {
            InputStream is = GlobalContext.getInstance().getAssets().open(file);
            int size = is.available();
            // Read the entire asset into a local byte buffer.
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            return new String(buffer, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 写入文件
     *
     * @param in
     * @param file
     */
    public static void writeFile(InputStream in, File file) {
        if (!file.getParentFile().exists())
            file.getParentFile().mkdirs();
        try {
            FileOutputStream out = new FileOutputStream(file);
            byte[] buffer = new byte[1024 * 128];
            int len = -1;
            while ((len = in.read(buffer)) != -1)
                out.write(buffer, 0, len);
            out.flush();
            out.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取文件
     *
     * @param file
     * @return
     */
    public static String readFileToString(File file) {
        StringBuffer sb = new StringBuffer();
        try {
            InputStream is = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String readLine = null;
            while ((readLine = reader.readLine()) != null) {
                sb.append(readLine);
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return sb.toString();
    }


    /**
     * 判断文件是否存在
     * @param path
     * @return
     */
    public static Boolean isFileExists(String path) {
        File file = new File(path);
        if (file.exists()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 删除文件
     * @param uri
     */
    public static void deleteFile(String uri) {
        File file = new File(uri);
        if (file.exists()) {
            file.delete();
        }
    }
}
