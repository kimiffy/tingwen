package com.tingwen.popupwindow;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.tingwen.R;


/**
 * Created by Administrator on 2017/5/8 0008.
 */
public class ClassBuyerInfoPop extends PopupWindow implements View.OnClickListener {


    private final Activity context;
    private View view;
    private TextView cancel,commit;
    private EditText name;
    private EditText phone;
    private EditText weixin;
    private EditText job;
    private EditText city;

    public ClassBuyerInfoPop(Activity context) {
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.class_detail_buyer_info_pop, null);
        initView(view);
        DisplayMetrics metrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(metrics);
         int widthPixels = metrics.widthPixels;
        initClick();

        // 设置SelectPicPopupWindow的View
        this.setContentView(view);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(2*widthPixels/3);
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



    }

    private void initView(View view) {

        name = (EditText) view.findViewById(R.id.et_name);
        phone = (EditText) view.findViewById(R.id.et_phone);
        weixin = (EditText) view.findViewById(R.id.et_weixin);
        city = (EditText) view.findViewById(R.id.et_city);
        job = (EditText) view.findViewById(R.id.et_job);
        cancel = (TextView) view.findViewById(R.id.tv_cancel);
        commit = (TextView) view.findViewById(R.id.tv_commit);
    }

    private void initClick() {
        cancel.setOnClickListener(this);
        commit.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_cancel:
                buyerInfoListener.cancelCommit();
                break;
            case R.id.tv_commit:
                String name = this.name.getText().toString().trim();
                if(name.equals("")){
                    Toast.makeText(context,"请填写姓名", Toast.LENGTH_SHORT).show();
                    return;
                }
                String phone = this.phone.getText().toString().trim();

                if(phone.equals("")){
                    Toast.makeText(context,"请填写手机号码", Toast.LENGTH_SHORT).show();
                    return;
                }
                String weixin = this.weixin.getText().toString().trim();
                if(weixin.equals("")){
                    Toast.makeText(context,"请填写微信号", Toast.LENGTH_SHORT).show();
                    return;
                }
                String job = this.job.getText().toString().trim();
                if(job.equals("")){
                    Toast.makeText(context,"请填写工作描述", Toast.LENGTH_SHORT).show();
                    return;
                }
                String city = this.city.getText().toString().trim();
                if(city.equals("")){
                    Toast.makeText(context,"请填写所在城市", Toast.LENGTH_SHORT).show();
                    return;
                }
                buyerInfoListener.commit( name, phone,weixin,job,city);
                break;
            default:
                break;
        }

    }
    public void showPopupWindow(View parent) {
        if (!this.isShowing()) {
            this.showAtLocation(parent, Gravity.CENTER, 0, 0);
        } else {
            this.dismiss();
        }
    }

    private BuyerInfoListener buyerInfoListener;

    public interface BuyerInfoListener {

        void cancelCommit();

        void commit(String name, String phone, String weixin, String job, String city);
    }

    public void setOnListener(BuyerInfoListener buyerInfoListener) {
        this.buyerInfoListener = buyerInfoListener;
    }

}
