package com.tingwen.activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.Toast;

import com.tingwen.R;
import com.tingwen.constants.AppConfig;
import com.tingwen.fragment.ImageDetailFragment;
import com.tingwen.widget.HackyViewPager;
import com.tingwen.widget.logger.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class ImagesActivity extends AppCompatActivity {
    private HackyViewPager viewPager;
    private ImagePagerAdapter adapter;
    private ArrayList<String> imageUrls;
    private String[] imageStrings;
    private ArrayList<ImageDetailFragment> imageDetailFragmentArrayList;
    private int position;


    public static void getImagesActivityInstance(Context context, ArrayList<String> imageUrls, int position) {
        Intent intent = new Intent(context, ImagesActivity.class);
        intent.putExtra("position", position);
        intent.putStringArrayListExtra("imageUrls", imageUrls);
        context.startActivity(intent);
    }
    public static void getImagesActivityInstance(Context context, String[] imageStrings, int position) {
        Intent intent = new Intent(context, ImagesActivity.class);
        intent.putExtra("position", position);
        intent.putExtra("imageStrings", imageStrings);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images);

        initView();
        initData();

        if (savedInstanceState != null) {
            imageStrings = savedInstanceState.getStringArray("imageStrings");
            imageUrls = savedInstanceState.getStringArrayList("imageUrls");
            if(imageStrings != null && imageStrings.length > 0) {
                imageUrls = new ArrayList<>();
                for(String image : imageStrings) {
                    imageUrls.add(image);
                }
            }
            position = savedInstanceState.getInt("position");
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(0, R.anim.image_out);
    }

    private void initView() {
        viewPager = (HackyViewPager) findViewById(R.id.vp);
    }

    private void initData() {

        imageUrls = new ArrayList<>();
        imageDetailFragmentArrayList = new ArrayList<>();

        imageUrls = getIntent().getStringArrayListExtra("imageUrls");
        imageStrings = getIntent().getStringArrayExtra("imageStrings");
        if(imageStrings != null && imageStrings.length > 0) {
            imageUrls = new ArrayList<>();
            for(String image : imageStrings) {
                imageUrls.add(image);
            }
        }

        position = getIntent().getIntExtra("position", 0);

        for(int i=0;i<imageUrls.size();i++) {
            imageDetailFragmentArrayList.add(ImageDetailFragment.newInstance(imageUrls.get(i)));
        }

        adapter = new ImagePagerAdapter(getSupportFragmentManager(),imageUrls);

        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(position);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putInt("position", position);
        outState.putStringArrayList("imageUrls", imageUrls);
        outState.putStringArray("imageStrings", imageStrings);
    }

    private class ImagePagerAdapter extends FragmentStatePagerAdapter {

        public ArrayList<String> fileList;

        public ImagePagerAdapter(FragmentManager fm, ArrayList<String> fileList) {
            super(fm);
            this.fileList = fileList;
        }

        @Override
        public int getCount() {
            return fileList == null ? 0 : fileList.size();
        }

        @Override
        public Fragment getItem(int position) {
            ImageDetailFragment imageDetailFragment = imageDetailFragmentArrayList.get(position);
            return imageDetailFragment;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageDetailFragment imageDetailFragment = (ImageDetailFragment) super.instantiateItem(container, position);
            if(imageDetailFragment != null) {
                Bundle bundle = new Bundle();
                bundle.putString("url",imageUrls.get(position));
                imageDetailFragment.setArguments(bundle);
            }
            return imageDetailFragment;
        }
    }


    public static class DownLoadImageAsy extends AsyncTask {
        private String message = "";
        private Context context;

        public DownLoadImageAsy (Context context) {
            this.context = context.getApplicationContext();
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object[] params) {
            File dir = new File(AppConfig.EXTRASTROGEDOWNLOADPATH);
            if (!dir.exists()) {
                dir.mkdir();
            }

            File file = new File(dir, params + ".jpg");
            if (file.exists()) {
                message = "已保存到手机中";
                Logger.e("图片已经存在!");
                return null;
            }

            try {
                file.createNewFile();
                URL url = new URL((String) params[0]);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setReadTimeout(5000);

                if (httpURLConnection.getResponseCode() == 200) {
                    InputStream inputStream = httpURLConnection.getInputStream();
                    FileOutputStream fileOutputStream = new FileOutputStream(file);

                    byte[] buffer = new byte[4 * 1028];
                    int len = -1;
                    while ((len = inputStream.read(buffer)) != -1) {
                        fileOutputStream.write(buffer, 0, len);
                    }
                    fileOutputStream.flush();

                    inputStream.close();
                    fileOutputStream.close();

                    message = "已保存到手机中";
                } else {
                    message = "保存失败,请检查网络";
                }
            } catch (IOException e) {
                e.printStackTrace();
                message = "保存失败";
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    }
}
