package com.tingwen.utils;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.tingwen.net.UrlProvider;
import com.tingwen.net.callback.SimpleJsonCallback;

import java.util.HashMap;
import java.util.Map;

/**
 * 课程工具类
 * Created by Administrator on 2017/11/29 0029.
 */
public class ClassUtil {

    private volatile static ClassUtil instance;
    private ClassUtil (){}
    public static ClassUtil getInstance() {
        if (instance == null) {
            synchronized (ClassUtil.class) {
                if (instance == null) {
                    instance = new ClassUtil();
                }
            }
        }
        return instance;
    }


    /**
     * 上传上一次听的课程时间
     * @param id
     * @param time
     * @param position
     */
    public void upLoadLastPlayTime(String id, String time, String position){
        Map<String, String> map = new HashMap<>();
        map.put("user_id", LoginUtil.getUserId());
        map.put("act_id",id );
        map.put("number", position);
        map.put("time", time);
        OkGo.<String>post(UrlProvider.UPLOAD_CLASS_LAST_PLAY).params(map).execute(new SimpleJsonCallback<String>(String.class) {
            @Override
            public void onSuccess(Response<String> response) {

            }
        });




    }


}
