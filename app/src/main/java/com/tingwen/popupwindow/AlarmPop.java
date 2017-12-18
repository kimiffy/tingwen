package com.tingwen.popupwindow;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ListView;

import com.tingwen.R;
import com.tingwen.adapter.AlarmAdapter;
import com.tingwen.service.AlarmService;
import com.tingwen.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import razerdp.basepopup.BasePopupWindow;

/**
 * Created by Administrator on 2017/11/6 0006.
 */
public class AlarmPop extends BasePopupWindow implements View.OnClickListener {

    private View popupView;
    private List<Integer> list = new ArrayList<>();
    private Context context;
    private AlarmAdapter adapter;
    private int mPosition=-1;
    public AlarmPop(Activity context) {
        super(context);
        this.context=context;
        bindEvent();
    }

    @Override
    protected Animation initShowAnimation() {
        return getTranslateAnimation(250 * 2, 0, 300);
    }

    @Override
    public View getClickToDismissView() {
        return popupView.findViewById(R.id.click_to_dismiss);
    }

    @Override
    public View onCreatePopupView() {
        popupView = LayoutInflater.from(getContext()).inflate(R.layout.popup_alarm, null);
        return popupView;
    }

    @Override
    public View initAnimaView() {
        return popupView.findViewById(R.id.popup_anima);
    }

    private void bindEvent() {
        if (popupView != null) {
            ListView lvAlarm = (ListView) popupView.findViewById(R.id.lv_alarm);
            final List<Integer> list = initList();
            adapter = new AlarmAdapter(context, list);
            lvAlarm.setAdapter(adapter);
            lvAlarm.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    adapter.setSlectPosition(position);
                    mPosition=position;
                    Integer i = list.get(position);
                    ToastUtils.showBottomToast("程序将在"+i+"分钟后停止播放");
                    AlarmService.timelong = i*60*1000;
                    Intent time = new Intent(context, AlarmService.class);
                    context.startService(time);

                }
            });
            popupView.findViewById(R.id.tv_cancel).setOnClickListener(this);
            popupView.findViewById(R.id.tv_dismiss).setOnClickListener(this);
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_cancel:
                mPosition=-1;
                adapter.setSlectPosition(-1);
                AlarmService.timelong = 0;
                ToastUtils.showBottomToast("结束定时任务");
                Intent time = new Intent(context, AlarmService.class);
                context.stopService(time);
                alarmListener.alarmCancel();
                break;
            case R.id.tv_dismiss:
                dismiss();
                break;

            default:
                break;
        }
    }

    private List<Integer> initList() {
        list.add(10);
        list.add(20);
        list.add(30);
        list.add(40);
        list.add(50);
        list.add(60);
        return list;

    }

    public void setListener(AlarmListener alarmListener) {
        this.alarmListener = alarmListener;
    }

    private AlarmListener alarmListener;

    public interface AlarmListener {

        void alarmCancel();

    }
}
