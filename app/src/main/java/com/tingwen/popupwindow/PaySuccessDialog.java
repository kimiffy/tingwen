package com.tingwen.popupwindow;

import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.tingwen.R;
import com.tingwen.bean.SimpleMsgBean;
import com.tingwen.net.UrlProvider;
import com.tingwen.net.callback.SimpleJsonCallback;
import com.tingwen.utils.EmojiUtil;
import com.tingwen.utils.LoginUtil;
import com.tingwen.utils.SizeUtil;
import com.tingwen.utils.ToastUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 支付成功后的Dialog
 * Created by Administrator on 2016/11/9 0009.
 */
public class PaySuccessDialog extends Dialog implements View.OnClickListener {

    private View view;
    private LinearLayout llDelete;
    private EditText etMessage;
    private TextView tvTextNumber;
    private TextView tvCheck;
    private Button btnMessage;
    private TextView tvJingyan;

    private boolean isCheck;//是否勾选
    private PaySuccessShareListener mPaySuccessShareListener;
    private String mAct_id;

    private String mPost_id;
    private String money;

    public PaySuccessDialog(Context context, String act_id, String post_id, String money) {
        super(context);

        mAct_id = act_id;
        mPost_id = post_id;
        this.money = money;
        inite();
    }

    private void inite() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        view = getLayoutInflater().inflate(R.layout.dialog_pay_success, null);

        llDelete = (LinearLayout) view.findViewById(R.id.ll_delete);
        etMessage = (EditText) view.findViewById(R.id.et_message);
        tvTextNumber = (TextView) view.findViewById(R.id.tv_text_number);
        tvCheck = (TextView) view.findViewById(R.id.tv_check);
        btnMessage = (Button) view.findViewById(R.id.btn_message);
        tvJingyan = (TextView) view.findViewById(R.id.tv_jingyan);

        tvCheck.setSelected(true);
        isCheck = true;

        if (LoginUtil.isUserLogin()) {
            successAnimation();
        }

        llDelete.setOnClickListener(this);
        btnMessage.setOnClickListener(this);
        tvCheck.setOnClickListener(this);
        etMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int live = 25 - (s.length());
                if (live >= 0) {
                    tvTextNumber.setText("还能输入" + live + "个字符");
                } else {
                    tvTextNumber.setText("还能输入0个字符");
                    String text = s.subSequence(0, 25).toString();
                    etMessage.setText(text);
                    etMessage.setSelection(text.length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        setContentView(view);

    }

    private void successAnimation() {
        int i = 0;
        try {
            i = Integer.parseInt(money);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        String text = "经验\n+" + i * 5;
        int index = text.indexOf("+");
        SpannableStringBuilder builder = new SpannableStringBuilder(text);
        builder.setSpan(new ForegroundColorSpan(getContext().getResources().getColor(R.color.main_text_color_grey))
                , 0, index, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        builder.setSpan(new ForegroundColorSpan(getContext().getResources().getColor(R.color.btn_gold))
                , index, text.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        tvJingyan.setText(builder);
        final AnimationSet animationSet = new AnimationSet(true);
        final TranslateAnimation translateAnimation = new TranslateAnimation(0, SizeUtil.dip2px(getContext(), 70), 0, SizeUtil.dip2px(getContext(), 70));
        final TranslateAnimation translateAnimation1 = new TranslateAnimation(-SizeUtil.dip2px(getContext(), 35),
                -SizeUtil.dip2px(getContext(), 40), -SizeUtil.dip2px(getContext(), 35),
                -SizeUtil.dip2px(getContext(), 40));
        final AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        animationSet.addAnimation(translateAnimation);
        animationSet.addAnimation(alphaAnimation);
        animationSet.setDuration(200);
        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                translateAnimation1.setDuration(300);
                translateAnimation1.setRepeatMode(Animation.REVERSE);
                translateAnimation1.setRepeatCount(1);
                tvJingyan.setAnimation(translateAnimation1);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        translateAnimation1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                translateAnimation1.cancel();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        tvJingyan.setVisibility(View.VISIBLE);
        tvJingyan.startAnimation(animationSet);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_delete:
                mPaySuccessShareListener.paySuccessfinish();
                dismiss();
                break;
            case R.id.tv_check:
                if (isCheck) {
                    tvCheck.setSelected(false);
                    isCheck=false;
                } else {
                    tvCheck.setSelected(true);
                    isCheck=true;
                }
                break;
            case R.id.btn_message:
                senMessage();
                if (isCheck && mPaySuccessShareListener != null) {
                    mPaySuccessShareListener.paySuccessShare();
                } else {
                    if (mPaySuccessShareListener != null) {
                        mPaySuccessShareListener.paySuccessThanks();
                    }
                }
                dismiss();
                break;
            default:
                break;
        }
    }

    private void senMessage() {
        Map<String, String> map = new HashMap<>();
        String content = EmojiUtil.codeMsg(etMessage.getText().toString());
        if (TextUtils.isEmpty(content)) {
            return;
        }
        if (LoginUtil.isUserLogin()) {
            map.put("accessToken", LoginUtil.getAccessToken());
            map.put("act_id", mAct_id);
            map.put("post_id", mPost_id);
            map.put("message", content);
            OkGo.<SimpleMsgBean>post(UrlProvider.LEAVE_MESSAGE).params(map).execute(new SimpleJsonCallback<SimpleMsgBean>(SimpleMsgBean.class) {
                @Override
                public void onSuccess(Response<SimpleMsgBean> response) {
                    int status = response.body().getStatus();
                    if(status==1){
                        String msg = response.body().getMsg();
                        ToastUtils.showBottomToast(msg);
                    }
                }
            });
        }
    }

    public void setListener(PaySuccessShareListener paySuccessShareListener) {
        this.mPaySuccessShareListener = paySuccessShareListener;
    }

    public interface PaySuccessShareListener {
        void paySuccessShare();//选择了分享

        void paySuccessThanks();//没选择分享

        void paySuccessfinish();//直接关闭
    }
}
