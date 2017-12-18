package com.tingwen.popupwindow;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tingwen.R;
import com.tingwen.utils.ColorPhrase;

import java.text.NumberFormat;

/**
 * Created by Administrator on 2017/5/17 0017.
 */
public class SuperVipPop extends PopupWindow {


    private View view;
    private CheckBox cbVip;
    private CheckBox cbNor;
    private TextView tvPrice, tvSvipPrice;
    private String price = "";
    private String svipPrice = "";
    private TextView buyPrice;
    private RelativeLayout commit;
    private TextView content;
    private NumberFormat nf;
    private String title;
    private TextView name;
    private TextView tv_1, tv_2, tv_3, tv_4, tv_6, tv_7;
    private  RelativeLayout  rl_5;
    public SuperVipPop(Activity context, String price, String Title, String svipPrice) {

        this.price = price;
        this.title = Title;
        this.svipPrice = svipPrice;

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.class_super_vip_pop, null);
        initView(view);
        DisplayMetrics metrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int widthPixels = metrics.widthPixels;
        // 设置SelectPicPopupWindow的View
        this.setContentView(view);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(97 * widthPixels / 100);
//        this.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        // 刷新状态
        this.update();
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0000000000);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        this.setBackgroundDrawable(dw);
        initClick();
        initData();

    }

    private void initView(View view) {
        cbVip = (CheckBox) view.findViewById(R.id.cb_1);
        cbNor = (CheckBox) view.findViewById(R.id.cb_2);
        tvPrice = (TextView) view.findViewById(R.id.tv_single_class_price);
        tvSvipPrice = (TextView) view.findViewById(R.id.tv_super_vip_price);
        buyPrice = (TextView) view.findViewById(R.id.tv_buy_price);
        commit = (RelativeLayout) view.findViewById(R.id.rl_commit);
        content = (TextView) view.findViewById(R.id.tv_class_content_1);
        name = (TextView) view.findViewById(R.id.tv_class_name);
        nf = NumberFormat.getInstance();

        tv_1 = (TextView) view.findViewById(R.id.tv_1);
        tv_2 = (TextView) view.findViewById(R.id.tv_2);
        tv_3 = (TextView) view.findViewById(R.id.tv_3);
        tv_4 = (TextView) view.findViewById(R.id.tv_4);
        rl_5 = (RelativeLayout) view.findViewById(R.id.rl_5);

        tv_6 = (TextView) view.findViewById(R.id.tv_6);
        tv_7 = (TextView) view.findViewById(R.id.tv_7);


    }

    private void initClick() {
        cbVip.setChecked(true);
        cbNor.setChecked(false);


        cbVip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cbVip.setChecked(true);
                cbNor.setChecked(false);
                buyPrice.setText("¥ " + svipPrice);
            }
        });
        cbNor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cbNor.setChecked(true);
                cbVip.setChecked(false);
                buyPrice.setText("¥ " + String.valueOf(nf.format(Double.parseDouble(price))));
            }
        });


        tvSvipPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Vipchecked();
            }
        });


        tvPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Norchecked();
            }
        });


        tv_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Vipchecked();
            }
        });

        tv_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Vipchecked();
            }
        });
        rl_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Vipchecked();
            }
        });


        tv_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Vipchecked();
            }
        });







        tv_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Norchecked();
            }
        });

        tv_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Norchecked();
            }
        });


        tv_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Norchecked();
            }
        });

        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Norchecked();
            }
        });


        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cbVip.isChecked()) {
                    commitListener.VipCommit();
                } else if (cbNor.isChecked()) {
                    commitListener.NorCommit();
                }
            }
        });


    }

    private void initData() {
        tvPrice.setText("¥ " + String.valueOf(nf.format(Double.parseDouble(price))));
        name.setText(title);
        tvSvipPrice.setText("¥ " + svipPrice);
        buyPrice.setText("¥ " + svipPrice);
        CharSequence format = ColorPhrase.from("{所有课程免费听}").withSeparator("{}").innerColor(Color.parseColor("#EA8B5B")).format();
        content.setText(format);
    }


    private CommitListener commitListener;

    public interface CommitListener {

        void VipCommit();

        void NorCommit();
    }

    public void setCommitListener(CommitListener commitListener) {
        this.commitListener = commitListener;
    }


    private void Norchecked() {
        cbNor.setChecked(true);
        cbVip.setChecked(false);
        buyPrice.setText("¥ " + String.valueOf(nf.format(Double.parseDouble(price))));
    }

    private void Vipchecked() {
        cbVip.setChecked(true);
        cbNor.setChecked(false);
        buyPrice.setText("¥ " + svipPrice);
    }

}
