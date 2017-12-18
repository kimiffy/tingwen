package com.tingwen.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import com.tingwen.R;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.tingwen.adapter.ProgramPicAdapter;
import com.tingwen.base.BaseLazyFragment;
import com.tingwen.bean.PictureBean;

import java.util.ArrayList;

import butterknife.BindView;


/**
 * 图片
 * Created by Administrator on 2017/8/14 0014.
 */
public class ProgramPictureFragment extends BaseLazyFragment {


    @BindView(R.id.rlv_pic)
    LRecyclerView rlvPic;
    private String images;
    public ArrayList<PictureBean>  imageUrls;
    private LRecyclerViewAdapter lRecyclerViewAdapter;
    private ProgramPicAdapter picAdapter;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_picture;
    }

    public static ProgramPictureFragment newInstance(String images) {
        ProgramPictureFragment fragment = new ProgramPictureFragment();
        Bundle args = new Bundle();
        args.putString("images", images);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected void initData() {
        super.initData();
        if (getArguments() != null) {
            images = getArguments().getString("images");
        }

        imageUrls = new ArrayList<>();
        rlvPic.setLayoutManager(new LinearLayoutManager(getActivity()));
        picAdapter = new ProgramPicAdapter(getActivity(), imageUrls);
        lRecyclerViewAdapter = new LRecyclerViewAdapter(picAdapter);
        rlvPic.setAdapter(lRecyclerViewAdapter);
        //禁止下拉刷新上拉加载更多
        rlvPic.setPullRefreshEnabled(false);
        rlvPic.setLoadMoreEnabled(false);

    }

    @Override
    protected void onFragmentVisibleChange(boolean isVisible) {
        super.onFragmentVisibleChange(isVisible);
        if (isVisible) {

            if (images != null && imageUrls.size() == 0) {

                if (images.contains("<p>")) {
                    // TODO: 2017/8/21 0021 原方法很多获取不到图片 注释掉下面的代码 ,暂不采用
//                    compile 'org.jsoup:jsoup:1.8.3'
//                    Document document = Jsoup.parse(images);
//                    Elements elements = document.getElementsByTag("p");
//                    for (Element element : elements) {
//                        String image = element.select("img").attr("src").trim();
//                        if (!image.isEmpty()) {
//                            PictureBean bean = new PictureBean();
//                            bean.setImg(image);
//                            imageUrls.add(bean);
//                        }
//                    }
                } else {

                    PictureBean bean = new PictureBean();
                    bean.setImg(images);
                    imageUrls.add(bean);
                }

                if (imageUrls.size() == 0) {

                } else {
                    picAdapter.setList(imageUrls);

                }

            }
        }
    }



}
