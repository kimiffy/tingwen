package com.tingwen.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Administrator on 2015/10/20 0020.
 */
public class BitmapUtil {

    public static Bitmap getBitmapFromInternet_Compress(String url) {
        Bitmap bitmap = null;

        try {
            URL imageUrl = new URL(url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) imageUrl.openConnection();
            if (httpURLConnection.getResponseCode() == 200) {
                InputStream inputStream = httpURLConnection.getInputStream();
                byte[] data = readStream(inputStream);

                BitmapFactory.Options opts = new BitmapFactory.Options();
                opts.inJustDecodeBounds = true;
                BitmapFactory.decodeByteArray(data,0,data.length,opts);

                opts.inSampleSize = calculateInSampleSize(opts, 50, 50);
                opts.inJustDecodeBounds = false;

                bitmap = BitmapFactory.decodeByteArray(data,0,data.length,opts);

                inputStream.close();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  bitmap;
    }

    /*
     * 得到图片字节流 数组大小
     * */
    public static byte[] readStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while( (len=inStream.read(buffer)) != -1){
            outStream.write(buffer, 0, len);
        }
        outStream.close();
        inStream.close();
        return outStream.toByteArray();
    }

    public static Bitmap getBitmapFromInternet(String url) {
        Bitmap bitmap = null;

        try {
            URL imageUrl = new URL(url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) imageUrl.openConnection();
            if (httpURLConnection.getResponseCode() == 200) {
                InputStream inputStream = httpURLConnection.getInputStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
                inputStream.close();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  bitmap;
    }

    public static Bitmap getBitmapFromLocalImage(String path, int width, int height) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path,options);
        options.inSampleSize = calculateInSampleSize(options,width,height);
        options.inJustDecodeBounds = false;

        return  BitmapFactory.decodeFile(path,options);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth,
                                            int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;

            final float totalPixels = width * height;
            final float totalReqPixelsCap = reqWidth * reqHeight * 2;
            while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
                inSampleSize++;
            }
        }
        return inSampleSize;
    }

    public static File compressBitmap(File file, String path, String copressPath){
        //将所选图片转化为流的形式，path所得到的图片路径
        try {
            FileInputStream is = new FileInputStream(path);
            //定义一个file，压缩后的图片
            file = new File(copressPath);
            if(!file.getParentFile().exists()){
                file.getParentFile().mkdir();
            }
            if(!file.exists()){
                file.createNewFile();
            }
            int size = 1;
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = size;
            //将图片缩小为原来的1/size
            Bitmap image = BitmapFactory.decodeStream(is,null,options);
            is.close();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG,100,baos);//100表示不压缩，将不压缩的数据存放到baos中
            int per = 90;
            while (baos.toByteArray().length / 1024 > 500){//压缩图片是否大于500K，如果大于继续压缩
                baos.reset();//充值baos
                image.compress(Bitmap.CompressFormat.JPEG, per, baos);//压缩到原来的百分比
                per -= 10;
            }
            //回收图片，清理内存
            if(image != null && !image.isRecycled()){
                image.recycle();
                image = null;
                System.gc();
            }
            ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
            baos.close();
            FileOutputStream os;
            if(file.exists()) {
                os = new FileOutputStream(file);
                int bytesRead = 0;
                byte[] buffer = new byte[8192];
                while ((bytesRead = isBm.read(buffer, 0, 8192)) != -1) {
                    os.write(buffer, 0, bytesRead);
                }
                os.close();
            }
            isBm.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * 微博图片压缩到32k
     * @param bitmap
     * @return
     */
    public static Bitmap compressBitmap(Bitmap bitmap){
        //将所选图片转化为流的形式，path所得到的图片路径
            int size = 1;
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = size;

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);//100表示不压缩，将不压缩的数据存放到baos中
            int per = 90;
            while (baos.toByteArray().length / 1024 > 32){//压缩图片是否大于500K，如果大于继续压缩
                baos.reset();//充值baos
                bitmap.compress(Bitmap.CompressFormat.JPEG, per, baos);//压缩到原来的百分比
                per -= 10;
            }
        return bitmap;
    }
}
