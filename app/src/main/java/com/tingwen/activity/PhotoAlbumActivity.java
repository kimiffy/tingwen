package com.tingwen.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;

import com.tingwen.R;
import com.tingwen.fragment.PhotoAlbumFragment;


/**
 * 相册选择照片
 */
public class PhotoAlbumActivity extends AppCompatActivity {
    private PhotoAlbumFragment photoAlbumFragment;
    private int limit;
    private boolean isSingle = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_album);

        initData();
        if(savedInstanceState != null) {
            limit = savedInstanceState.getInt("limit");
            isSingle = savedInstanceState.getBoolean("single");
        }
        photoAlbumFragment.limit = limit;
        photoAlbumFragment.isSingle = isSingle;
        getSupportFragmentManager().beginTransaction().replace(R.id.container,photoAlbumFragment,"photo").commit();
    }

    private void initData() {
        photoAlbumFragment = new PhotoAlbumFragment();
        limit = getIntent().getIntExtra("limit",0);
        isSingle = getIntent().getBooleanExtra("single", false);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        outState.putInt("limit",limit);
        outState.putBoolean("single",isSingle);
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    public void onBackPressed() {
        if(photoAlbumFragment.isShowAlbumList) {
            photoAlbumFragment.onBackPressed();
            return;
        }
        super.onBackPressed();
    }
}
