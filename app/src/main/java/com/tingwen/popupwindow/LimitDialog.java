package com.tingwen.popupwindow;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.tingwen.R;
import com.tingwen.constants.AppConfig;

/**
 * Created by Administrator on 2017/10/31 0031.
 */
public class LimitDialog extends Dialog {
    private View view;
    private TextView cancel,vip,content;

    public LimitDialog(Context context) {
        super(context);
        init();
    }

    private void init() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        view = getLayoutInflater().inflate(R.layout.dialog_play_times_limit, null);
        content = (TextView) view.findViewById(R.id.tv_content);
        String s= AppConfig.PRE_PLAY_TIME_LIMIT_VALUE+"";
        content.setText("您还不是会员,每日可收听"+s+"条,今日已听完,是否前往开通会员,收听更多资讯");
        content.setTextSize(15);
        cancel = (TextView) view.findViewById(R.id.tv_cancel);
        vip = (TextView) view.findViewById(R.id.tv_vip);
        setContentView(view);
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity= Gravity.CENTER;
        dialogWindow.setAttributes(lp);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                limitListener.dismiss();
            }
        });
        vip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                limitListener.buyVip();
            }
        });

    }


    private LimitListener limitListener;

    public interface LimitListener {

        void dismiss();

        void buyVip();
    }

    public void setLimitListener(LimitListener limitListener) {
        this.limitListener = limitListener;
    }

}
