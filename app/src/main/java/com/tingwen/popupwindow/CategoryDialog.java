package com.tingwen.popupwindow;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.tingwen.R;
import com.tingwen.bean.CategoryBean;
import com.tingwen.utils.SizeUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 分类新闻选择
 * Created by Administrator on 2017/10/19 0019.
 */
public class CategoryDialog extends DialogFragment implements View.OnClickListener{


    private View view;
    private ListView listView;
    private LinearLayout llConfirm;
    private TextView tvCancel,tvSure;
    private List<CategoryBean.ResultsBean> list = new ArrayList<>();
    private LayoutInflater inflater;
    private OnChooseNewsCategoryListener onChooseNewsCategoryListener;

    private String categry = "";
    private String categryId = "";
    private int[] check;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        view = inflater.inflate(R.layout.category_dialog,container,false);
        this.inflater = inflater;
        initView();
        initData();
        return view;
    }

    public static final CategoryDialog newInstance()
    {
        CategoryDialog fragment = new CategoryDialog();

        return fragment ;
    }
    public CategoryDialog(){

    }

    private void initView() {
        listView = (ListView) view.findViewById(R.id.lv);
        llConfirm = (LinearLayout) view.findViewById(R.id.ll_comfirm);
        tvCancel = (TextView) view.findViewById(R.id.tv_cancel);
        tvSure = (TextView) view.findViewById(R.id.tv_sure);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                check[position] = check[position] == 0 ? 1 : 0;
                if(check[position] == 1){
                    view.findViewById(R.id.tv).setSelected(true);
                }else{
                    view.findViewById(R.id.tv).setSelected(false);
                }
            }
        });
        tvCancel.setOnClickListener(this);
        tvSure.setOnClickListener(this);
        listView.setDividerHeight(0);
        int width = SizeUtil.getScreenWidth() / 3 * 2;
        int height = SizeUtil.getScreenHeight() / 2;
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) listView.getLayoutParams();
        layoutParams.width = width;
        layoutParams.height = height;
        listView.setLayoutParams(layoutParams);
        LinearLayout.LayoutParams layoutParams1 = (LinearLayout.LayoutParams) llConfirm.getLayoutParams();
        layoutParams1.width = width;
        llConfirm.setLayoutParams(layoutParams1);

    }

    private void initData() {
        check = new int[list.size()];
        listView.setAdapter(new MyAdapter());
    }
    public void setList(List<CategoryBean.ResultsBean> list){
        this.list=list;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_cancel:
                dismiss();
                break;
            case R.id.tv_sure:
                categryId = "";
                categry = "";
                for (int i = 0; i <check.length ; i++) {
                    if(check[i] == 1){
                        categry += list.get(i).getType()+" ";
                        categryId += list.get(i).getNumberkey()+",";
                    }
                }
                onChooseNewsCategoryListener.onChooseNewsCategoryFinished(categry,categryId);
                dismiss();
                break;
            default:
                break;
        }
    }

    public interface OnChooseNewsCategoryListener {
        void onChooseNewsCategoryFinished(String category,String categoryId);
    }

    public void setOnChooseNewsCategoryListener(OnChooseNewsCategoryListener onChooseNewsCategoryListener) {
        this.onChooseNewsCategoryListener = onChooseNewsCategoryListener;
    }
    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if(convertView == null) {
                viewHolder = new ViewHolder();
                convertView = inflater.inflate(R.layout.category_dialoag_item,parent,false);
                viewHolder.textView = (TextView) convertView.findViewById(R.id.tv);
                convertView.setTag(viewHolder);
            }else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.textView.setText(list.get(position).getType());
            if(check[position] == 1){
                viewHolder.textView.setSelected(true);
            }else{
                viewHolder.textView.setSelected(false);
            }
            return convertView;
        }

        private class ViewHolder {
            TextView textView;
        }
    }
}
