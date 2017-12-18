package com.tingwen.fragment;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.tingwen.R;
import com.bumptech.glide.Glide;
import com.tingwen.adapter.DividerGridItemDecoration;
import com.tingwen.bean.PhotoAlbumImage;
import com.tingwen.bean.PhotoUpImageBucket;
import com.tingwen.utils.SizeUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 相册选择
 * Created by Administrator on 2017/9/8 0008.
 */
public class PhotoAlbumFragment extends Fragment {

    private View view;
    private RecyclerView recyclerView;
    private RecyclerView albumListRecyclerView;
    private List<PhotoAlbumImage> photoAlbumImageList = new ArrayList<>();
    private List<PhotoAlbumImage> tempPhotoAlbumImageList = new ArrayList<>();
    private MyAdapter myAdapter;
    private PhotoBucketAdapter photoBucketAdapter;
    private RelativeLayout albumListRl;
    public boolean isShowAlbumList = false;
    private RelativeLayout rlBack;
    private View shadow;

    private HashMap<String, PhotoUpImageBucket> photoUpImageBucketHashMap = new HashMap<>();
    private List<PhotoUpImageBucket> photoUpImageBucketList = new ArrayList<>();

    private List<String> selectedImageList = new ArrayList<>();
    private TextView done;
    public int limit = 0;
    public boolean isSingle = false;
    private int currentImageCount = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_photo_album, container, false);
        initActionbar();
        initView();
        initData();
        new getAlbumPhotosTask().execute();
        buildPhotoUpBucketHashMap();
        return view;
    }

    private void initActionbar() {
        rlBack = (RelativeLayout) view.findViewById(R.id.back);
        rlBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        done = (TextView) view.findViewById(R.id.done);
        if (isSingle) {
            done.setVisibility(View.GONE);
        } else {
            done.setVisibility(View.VISIBLE);
            done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    done();
                }
            });
        }

    }

    private void done() {
        if (selectedImageList.size() > 0) {
            String[] images = new String[selectedImageList.size()];
            for (int i = 0; i < selectedImageList.size(); i++) {
                images[i] = selectedImageList.get(i);
            }
            Intent intent = new Intent();
            intent.putExtra("images", images);
            getActivity().setResult(getActivity().RESULT_OK, intent);
            getActivity().finish();
        } else {
            Toast.makeText(getActivity(), "请选择图片", Toast.LENGTH_SHORT).show();
        }
    }

    private void initView() {
        shadow = view.findViewById(R.id.shadow);
        shadow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideAlbumListRecycleView();
            }
        });
        recyclerView = (RecyclerView) view.findViewById(R.id.rlv);
        albumListRecyclerView = (RecyclerView) view.findViewById(R.id.rlv2);
        albumListRl = (RelativeLayout) view.findViewById(R.id.rl2);
        albumListRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isShowAlbumList) {
                    hideAlbumListRecycleView();
                } else {
                    showAlbumListRecyclerView();
                }
            }
        });
    }

    private void initData() {
        myAdapter = new MyAdapter();
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        recyclerView.addItemDecoration(new DividerGridItemDecoration(getActivity()));
        recyclerView.setAdapter(myAdapter);

        photoBucketAdapter = new PhotoBucketAdapter();
        albumListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        albumListRecyclerView.setAdapter(photoBucketAdapter);
    }

    public void onBackPressed() {
        if (isShowAlbumList) {
            hideAlbumListRecycleView();
        }
    }

    private void showAlbumListRecyclerView() {
        shadow.setVisibility(View.VISIBLE);
        TranslateAnimation show = (TranslateAnimation) AnimationUtils.loadAnimation(getActivity(), R.anim.anim_top_in);
        albumListRecyclerView.setVisibility(View.VISIBLE);
        albumListRecyclerView.startAnimation(show);
        isShowAlbumList = true;
    }

    private void hideAlbumListRecycleView() {
        shadow.setVisibility(View.GONE);
        TranslateAnimation hide = (TranslateAnimation) AnimationUtils.loadAnimation(getActivity(), R.anim.anim_bottom_out);
        albumListRecyclerView.setVisibility(View.GONE);
        albumListRecyclerView.startAnimation(hide);
        isShowAlbumList = false;
    }

    private class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
        private int width;
        private int height;

        public MyAdapter() {
            width = SizeUtil.getScreenWidth() / 3 - 7;
            height = width;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_view_photo_album, parent, false);
            MyViewHolder myViewHolder = new MyViewHolder(view);
            myViewHolder.imageView = (ImageView) view.findViewById(R.id.iv_photo_album);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(width, height);
            myViewHolder.imageView.setLayoutParams(layoutParams);

            myViewHolder.choose = (ImageView) view.findViewById(R.id.iv_choose);
            myViewHolder.rlChoose = (RelativeLayout) view.findViewById(R.id.rl_choose);
            return myViewHolder;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {

            final PhotoAlbumImage photoAlbumImage = tempPhotoAlbumImageList.get(position);
            Glide.with(getActivity()).load(new File(photoAlbumImage.getImagePath())).error(R.drawable.bg_black)
                    .placeholder(R.drawable.bg_black).into(holder.imageView);
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isSingle) {
                        selectedImageList.add(photoAlbumImage.getImagePath());
                        done();
                    } else {
                        String[] images = new String[tempPhotoAlbumImageList.size()];
                        for (int i = 0; i < tempPhotoAlbumImageList.size(); i++) {
                            images[i] = tempPhotoAlbumImageList.get(i).getImagePath();
                        }
//                        ImagesActivity.getImagesActivityInstance(getActivity(), images, position);
                        // TODO: 2017/9/9 0009 跳转至查看图片界面
                    }

                }
            });

            if (isSingle) {
                holder.rlChoose.setVisibility(View.GONE);
            } else {
                holder.rlChoose.setVisibility(View.VISIBLE);
            }

            holder.rlChoose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (currentImageCount >= limit) {
                        return;
                    }
                    if (photoAlbumImage.isSelected) {
                        holder.choose.setImageResource(R.drawable.icon_pic_unselected);
                        photoAlbumImage.isSelected = false;
                        selectedImageList.remove(photoAlbumImage.getImagePath());
                        currentImageCount--;
                        done.setText("确定 (" + currentImageCount + "/" + limit + ")");
                    } else {
                        holder.choose.setImageResource(R.drawable.iconfont_pic_selected);
                        photoAlbumImage.isSelected = true;
                        selectedImageList.add(photoAlbumImage.getImagePath());
                        currentImageCount++;
                        done.setText("确定 (" + currentImageCount + "/" + limit + ")");
                    }
                }
            });

            if (photoAlbumImage.isSelected) {
                holder.choose.setImageResource(R.drawable.iconfont_pic_selected);
            } else {
                holder.choose.setImageResource(R.drawable.icon_pic_unselected);
            }
        }

        @Override
        public int getItemCount() {
            return tempPhotoAlbumImageList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            private ImageView imageView;
            private ImageView choose;
            private RelativeLayout rlChoose;

            public MyViewHolder(View itemView) {
                super(itemView);
            }
        }
    }

    private class PhotoBucketAdapter extends RecyclerView.Adapter<PhotoBucketAdapter.BucketViewHolder> {

        @Override
        public BucketViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_view_photo_bucket, parent, false);
            BucketViewHolder viewHolder = new BucketViewHolder(view);
            viewHolder.choose = (ImageView) view.findViewById(R.id.choose);
            viewHolder.iv = (ImageView) view.findViewById(R.id.iv);
            viewHolder.name = (TextView) view.findViewById(R.id.name);
            viewHolder.count = (TextView) view.findViewById(R.id.count);
            viewHolder.rl = (RelativeLayout) view.findViewById(R.id.rl);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final BucketViewHolder holder, final int position) {
            final PhotoUpImageBucket photoUpImageBucket = photoUpImageBucketList.get(position);

            String name = photoUpImageBucket.getName();
            int count = photoUpImageBucket.getCount();
            String image = photoUpImageBucket.getList().get(0).getImagePath();

            holder.name.setText(name);

            Glide.with(getActivity()).load(new File(image)).error(R.drawable.bg_black)
                    .placeholder(R.drawable.bg_black).into(holder.iv);

            holder.count.setText(count + "");

            holder.rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (PhotoUpImageBucket photoUpImageBucket1 : photoUpImageBucketList) {
                        photoUpImageBucket1.isSelected = false;
                    }
                    photoUpImageBucket.isSelected = true;
                    notifyDataSetChanged();

                    tempPhotoAlbumImageList.clear();

                    if (photoUpImageBucket.isAllImage) {
                        tempPhotoAlbumImageList.addAll(photoAlbumImageList);
                    } else {
                        List<PhotoAlbumImage> list = photoUpImageBucket.getList();
                        tempPhotoAlbumImageList.addAll(list);
                    }
                    myAdapter.notifyDataSetChanged();

                    hideAlbumListRecycleView();
                    selectedImageList.clear();
                }
            });

            if (photoUpImageBucket.isSelected) {
                holder.choose.setVisibility(View.VISIBLE);
            } else {
                holder.choose.setVisibility(View.GONE);
            }

        }

        @Override
        public int getItemCount() {
            return photoUpImageBucketList.size();
        }

        public class BucketViewHolder extends RecyclerView.ViewHolder {
            private ImageView iv;
            private ImageView choose;
            private TextView name;
            private TextView count;
            private RelativeLayout rl;

            public BucketViewHolder(View itemView) {
                super(itemView);
            }
        }
    }

    public class getAlbumPhotosTask extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] params) {
            Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            ContentResolver contentResolver = getActivity().getContentResolver();
            String[] projection = new String[]{MediaStore.Images.Media.DATA};
            Cursor cursor = MediaStore.Images.Media.query(contentResolver, uri, projection, null, null, MediaStore.Images.Media.DATE_ADDED + " desc");
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            while (cursor.moveToNext()) {
                String path = cursor.getString(columnIndex);
                PhotoAlbumImage image = new PhotoAlbumImage();
                image.setImagePath(path);
                photoAlbumImageList.add(image);
            }
            tempPhotoAlbumImageList.clear();
            tempPhotoAlbumImageList.addAll(photoAlbumImageList);
            cursor.close();
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            myAdapter.notifyDataSetChanged();
        }
    }

    private void buildPhotoUpBucketHashMap() {
        new buildPhotoUpBucketHashMapAsy().execute();
    }

    private class buildPhotoUpBucketHashMapAsy extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] params) {
            String[] columns = new String[]{MediaStore.Images.Media._ID, MediaStore.Images.Media.BUCKET_ID, MediaStore.Images.Media.PICASA_ID
                    , MediaStore.Images.Media.DATA, MediaStore.Images.Media.DISPLAY_NAME, MediaStore.Images.Media.TITLE, MediaStore.Images.Media.SIZE
                    , MediaStore.Images.Media.BUCKET_DISPLAY_NAME};
            ContentResolver contentResolver = getActivity().getContentResolver();
            Cursor cursor = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null, null, MediaStore.Images.Media.DATE_MODIFIED + " desc");
            while (cursor.moveToNext()) {
                int photoIDIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
                int photoPath = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                int bucketDisplayName = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
                int bucketId = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_ID);

                if (cursor.getString(photoPath).substring(
                        cursor.getString(photoPath).lastIndexOf("/") + 1,
                        cursor.getString(photoPath).lastIndexOf("."))
                        .replaceAll(" ", "").length() <= 0) {
                    Log.d("photo", "出现了异常图片的地址：cur.getString(photoPathIndex)=" + cursor.getString(photoPath));
                    Log.d("photo", "出现了异常图片的地址：cur.getString(photoPathIndex).substring=" + cursor.getString(photoPath)
                            .substring(cursor.getString(photoPath).lastIndexOf("/") + 1, cursor.getString(photoPath).lastIndexOf(".")));
                } else {
                    String id = cursor.getString(photoIDIndex);
                    String path = cursor.getString(photoPath);
                    String name = cursor.getString(bucketDisplayName);
                    String bucketID = cursor.getString(bucketId);
                    PhotoUpImageBucket photoUpImageBucket = photoUpImageBucketHashMap.get(bucketID);
                    if (photoUpImageBucket == null) {
                        photoUpImageBucket = new PhotoUpImageBucket();
                        photoUpImageBucket.setName(name);
                        photoUpImageBucket.setList(new ArrayList<PhotoAlbumImage>());
                        photoUpImageBucketHashMap.put(bucketID, photoUpImageBucket);
                    }
                    photoUpImageBucket.setCount(photoUpImageBucket.getCount() + 1);
                    PhotoAlbumImage photoAlbumImage = new PhotoAlbumImage();
                    photoAlbumImage.setId(id);
                    photoAlbumImage.setImagePath(path);
                    photoUpImageBucket.getList().add(photoAlbumImage);
                }
            }
            cursor.close();

            for (Map.Entry<String, PhotoUpImageBucket> map : photoUpImageBucketHashMap.entrySet()) {
                photoUpImageBucketList.add(map.getValue());
            }

            PhotoUpImageBucket photoUpImageBucket = new PhotoUpImageBucket();
            photoUpImageBucket.setName("所有图片");
            photoUpImageBucket.setCount(photoAlbumImageList.size());

            ArrayList<PhotoAlbumImage> list = new ArrayList<>();
            PhotoAlbumImage photoAlbumImage = new PhotoAlbumImage();

            if(photoAlbumImageList.size() > 0) {
                photoAlbumImage.setImagePath(photoAlbumImageList.get(0).getImagePath());
                photoAlbumImage.setId(photoAlbumImageList.get(0).getId());
                list.add(photoAlbumImage);
                photoUpImageBucket.setList(list);
                photoUpImageBucket.isSelected = true;
                photoUpImageBucket.isAllImage = true;

                photoUpImageBucketList.add(0, photoUpImageBucket);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            photoBucketAdapter.notifyDataSetChanged();
            super.onPostExecute(o);
        }
    }


}
