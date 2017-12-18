package com.tingwen.popupwindow;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tingwen.R;
import com.tingwen.activity.PayActivity;
import com.tingwen.utils.LauncherHelper;


/**
 * Created by Administrator on 2017/3/6 0006.
 */
public class ZanShangPopupWindow extends PopupWindow implements View.OnClickListener {
    private View conentView;
    private RelativeLayout rlSecond;
    private RelativeLayout rlThird;
    private TextView tvOne;//一听币
    private TextView tvFive;//五听币
    private TextView tvTen;//十听币
    private TextView tvFifty;//五十听币
    private TextView tvHandured;//一百听币
    private TextView tvCustom;//自定义
    private Button btnShang1;//选择页面的赞赏按钮
    private Button btnShang3;//自定义页面的赞赏按钮
    private TextView tvBack;
    private TextView tvBack3;
    private EditText etShangNumber;//自定义输入赞赏金额
    private boolean isShang = false;//是否是赞赏后回来详情页
    private LinearLayout llDelete;//清除输入

    private float shangNumber = 0;
    private Context context;
    private String act_id;//主播的ID

    public ZanShangPopupWindow(Activity context, String id) {
        this.context = context ;
        this.act_id = id;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        conentView = inflater.inflate(R.layout.pop_zanshang, null);

        initView(conentView);

        initClick();


        // 设置SelectPicPopupWindow的View
        this.setContentView(conentView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
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

    /**
     * 初始化view
     *
     * @param headerView
     */
    private void initView(View headerView) {

        rlSecond = (RelativeLayout) headerView.findViewById(R.id.rl_second);
        rlThird = (RelativeLayout) headerView.findViewById(R.id.rl_third);
        tvOne = (TextView) headerView.findViewById(R.id.tv_one);
        tvFive = (TextView) headerView.findViewById(R.id.tv_five);
        tvTen = (TextView) headerView.findViewById(R.id.tv_ten);
        tvFifty = (TextView) headerView.findViewById(R.id.tv_fifty);
        tvHandured = (TextView) headerView.findViewById(R.id.tv_handured);
        tvCustom = (TextView) headerView.findViewById(R.id.tv_custom);
        tvBack = (TextView) headerView.findViewById(R.id.tv_back);
        tvBack3 = (TextView) headerView.findViewById(R.id.tv_back3);
        btnShang1 = (Button) headerView.findViewById(R.id.btn_shang_1);
        btnShang3 = (Button) headerView.findViewById(R.id.btn_shang_3);
        etShangNumber = (EditText) headerView.findViewById(R.id.et_custom_money);
        llDelete = (LinearLayout) headerView.findViewById(R.id.ll_delete);
    }

    /**
     * 设置点击监听
     */
    private void initClick() {

        tvOne.setOnClickListener(this);
        tvFive.setOnClickListener(this);
        tvTen.setOnClickListener(this);
        tvFifty.setOnClickListener(this);
        tvHandured.setOnClickListener(this);
        tvCustom.setOnClickListener(this);
        btnShang1.setOnClickListener(this);
        tvBack.setOnClickListener(this);
        btnShang3.setOnClickListener(this);
        tvBack3.setOnClickListener(this);
        llDelete.setOnClickListener(this);
        tvFive.setSelected(true);//默认选中5元
        shangNumber = 5;

    }

    public void showPopupWindow(View parent) {
        if (!this.isShowing()) {
            this.showAtLocation(parent, Gravity.CENTER, 0, 0);
        } else {
            this.dismiss();
        }
    }


    /**
     * 设置听币为未选择
     */
    private void setAllUnselect() {
        tvOne.setSelected(false);
        tvFive.setSelected(false);
        tvTen.setSelected(false);
        tvFifty.setSelected(false);
        tvHandured.setSelected(false);
    }

    @Override
    public void onClick(View v) {


        switch (v.getId()) {
            case R.id.tv_one:
                shangNumber = 1;
                setAllUnselect();
                tvOne.setSelected(true);
                break;
            case R.id.tv_five:
                shangNumber = 5;
                setAllUnselect();
                tvFive.setSelected(true);
                break;
            case R.id.tv_ten:
                shangNumber = 10;
                setAllUnselect();
                tvTen.setSelected(true);
                break;
            case R.id.tv_fifty:
                shangNumber = 50;
                setAllUnselect();
                tvFifty.setSelected(true);
                break;
            case R.id.tv_handured:
                shangNumber = 100;
                setAllUnselect();
                tvHandured.setSelected(true);
                break;
            case R.id.btn_shang_1:
                Bundle bundle = new Bundle();
                bundle.putFloat("money", shangNumber);
                bundle.putString("act_id", act_id);
                bundle.putInt("type", 2);
                LauncherHelper.getInstance().launcherActivity(context, PayActivity.class, bundle);
                break;

            case R.id.tv_custom:
                rlSecond.setVisibility(View.GONE);
                rlThird.setVisibility(View.VISIBLE);
                break;

            case R.id.btn_shang_3:
                String money = String.valueOf(etShangNumber.getText());
                if (!TextUtils.isEmpty(money)) {
                    shangNumber = Float.parseFloat(money);
                    if (shangNumber != 0) {
                        if (shangNumber < 0.1) {
                            Toast.makeText(context, "亲，打赏至少要一毛钱哦~", Toast.LENGTH_SHORT).show();
                            etShangNumber.setText("");
                        } else {
                            Bundle b = new Bundle();
                            b.putFloat("money", shangNumber);
                            b.putString("act_id", act_id);
                            b.putInt("type", 2);
                            LauncherHelper.getInstance().launcherActivity(context, PayActivity.class, b);
                        }
                    } else {
                        Toast.makeText(context, "金额不允许是0", Toast.LENGTH_SHORT).show();
                        etShangNumber.setText("");
                    }
                } else {
                    Toast.makeText(context, "请填写金额~", Toast.LENGTH_SHORT).show();
                }
                break;
            //返回2
            case R.id.tv_back:
                this.dismiss();
                break;
            //返回3
            case R.id.tv_back3:
                rlThird.setVisibility(View.GONE);
                rlSecond.setVisibility(View.VISIBLE);
                break;
            case R.id.ll_delete:
                etShangNumber.setText("");
                break;
        }
    }



    private ZanShangListener zanShangListener ;

    public interface ZanShangListener {

        void paySuccessCallBack(String tingbi, Float shangNumber, String id);

        void payNoLogin(Float shangNumber, String id);
    }

    //回调方法
    public void setOnListener(ZanShangListener zanShangListener) {
        this.zanShangListener = zanShangListener;
    }
}
