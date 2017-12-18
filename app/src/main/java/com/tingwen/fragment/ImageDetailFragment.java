package com.tingwen.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.tingwen.R;
import com.tingwen.activity.ImagesActivity;
import com.tingwen.widget.photoview.PhotoView;
import com.tingwen.widget.photoview.PhotoViewAttacher;


/**
 * 单张图片显示Fragment
 */
public class ImageDetailFragment extends Fragment {
    private String mImageUrl;
    private PhotoView mImageView;
    private ProgressBar progressBar;
    private PhotoViewAttacher mAttacher;
    private PopupWindow popupWindow;
    private View shadow;

    private ImagesActivity.DownLoadImageAsy downLoadImageAsy;
    private TextView save;

    public static ImageDetailFragment newInstance(String imageUrl) {
        final ImageDetailFragment f = new ImageDetailFragment();
        final Bundle args = new Bundle();
        args.putString("url", imageUrl);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mImageUrl = getArguments() != null ? getArguments().getString("url") : null;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.image_detail_fragment, container, false);
        shadow = v.findViewById(R.id.shadow);
        mImageView = (PhotoView) v.findViewById(R.id.image);
        mAttacher = new PhotoViewAttacher(mImageView);

        mAttacher.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {

            @Override
            public void onPhotoTap(View arg0, float arg1, float arg2) {
                getActivity().finish();
                getActivity().overridePendingTransition(0, R.anim.image_out);
            }

        });

        mAttacher.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                popupWindow.showAtLocation(mImageView.getRootView(), Gravity.CENTER, 0, 0);
                shadow.setVisibility(View.VISIBLE);
                return true;
            }
        });

        progressBar = (ProgressBar) v.findViewById(R.id.loading);

        View view = getActivity().getLayoutInflater().inflate(R.layout.save_photo_view, null);

        popupWindow = new PopupWindow(view, RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setTouchable(true);
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.transparency));
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                shadow.setVisibility(View.GONE);
            }
        });

        save = (TextView) view.findViewById(R.id.tv_save);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (downLoadImageAsy != null) {
                    downLoadImageAsy.cancel(true);
                    downLoadImageAsy = null;
                }
                downLoadImageAsy = new ImagesActivity.DownLoadImageAsy(getActivity());
                if (mImageUrl != null) {
                    downLoadImageAsy.execute(mImageUrl);
                }
                popupWindow.dismiss();
            }
        });
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (!TextUtils.isEmpty(mImageUrl)&&!mImageUrl.contains("http:")) {
            progressBar.setVisibility(View.VISIBLE);
            Glide.with(getActivity()).load("file://" + mImageUrl).listener(new RequestListener<String, GlideDrawable>() {
                @Override
                public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {

                    Toast.makeText(getActivity(), "加载失败!", Toast.LENGTH_SHORT).show();
                    return false;
                }

                @Override
                public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                    progressBar.setVisibility(View.GONE);
                    mAttacher.update();
                    return false;
                }
            }).into(mImageView);


        } else {
            if(TextUtils.isEmpty(mImageUrl)){//todo 验证
                mImageUrl="";
            }
            progressBar.setVisibility(View.VISIBLE);
            Glide.with(getActivity()).load(mImageUrl).listener(new RequestListener<String, GlideDrawable>() {
                @Override
                public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {

                    Toast.makeText(getActivity(), "加载失败!", Toast.LENGTH_SHORT).show();
                    return false;
                }

                @Override
                public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                    progressBar.setVisibility(View.GONE);
                    mAttacher.update();
                    return false;
                }
            }).into(mImageView);

        }
    }
}
