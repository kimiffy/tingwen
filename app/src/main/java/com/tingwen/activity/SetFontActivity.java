package com.tingwen.activity;

import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tingwen.R;
import com.tingwen.app.AppSpUtil;
import com.tingwen.base.BaseActivity;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

/**
 * 设置字体
 * Created by Administrator on 2017/10/16 0016.
 */
public class SetFontActivity extends BaseActivity {

    @BindView(R.id.ivLeft)
    ImageView ivLeft;
    @BindView(R.id.rlv_font)
    RecyclerView rlvFont;

    private String[] str;
    private CommonAdapter<String> commonAdapter;

    private int currentPosition;
    @Override
    protected int getLayoutResId() {
        return R.layout.activity_setfont;
    }

    @Override
    protected void setListener() {
        super.setListener();
        commonAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                currentPosition=position;
                AppSpUtil.getInstance().saveFrontSize(position);
                commonAdapter.notifyDataSetChanged();
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

        ivLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    protected void initData() {
        super.initData();
        currentPosition = AppSpUtil.getInstance().getFrontSize();
        str = getResources().getStringArray(R.array.font_set);
        List<String> list = Arrays.asList(str);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        rlvFont.setLayoutManager(manager);
        rlvFont.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        commonAdapter = new CommonAdapter<String>(this, R.layout.item_front, list) {
            @Override
            protected void convert(ViewHolder holder, String s, int position) {

                TextView tvfont = holder.getView(R.id.tv_font);
                ImageView ivfont = holder.getView(R.id.iv_font);
                ivfont.setVisibility(View.GONE);
                tvfont.setText(s);
                if(currentPosition==position){
                    ivfont.setVisibility(View.VISIBLE);
                }
            }
        };
        rlvFont.setAdapter(commonAdapter);
    }




}
