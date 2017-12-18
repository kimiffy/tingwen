package com.tingwen.net.callback;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.lzy.okgo.callback.AbsCallback;

import java.lang.reflect.Type;

import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * 普通的jsoncallback  需要额外的写构造传入类型
 * 不对状态码统一管理,直接返回解析后的bean
 * Created by Administrator on 2017/7/4 0004.
 */
public abstract class SimpleJsonCallback<T> extends AbsCallback<T> {

    private Type type;
    private Class<T> clazz;


    public SimpleJsonCallback(Type type) {
        this.type = type;
    }

    public SimpleJsonCallback(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public T convertResponse(Response response) throws Throwable {

        //详细自定义的原理和文档，看这里： https://github.com/jeasonlzy/okhttp-OkGo/wiki/JsonCallback

        ResponseBody body = response.body();
        if(body==null) return null;
        T data=null;
        Gson gson = new Gson();
        JsonReader jsonReader = new JsonReader(body.charStream());
        if(type!=null) data= gson.fromJson(jsonReader,type);
        if(clazz!=null) data= gson.fromJson(jsonReader,clazz);
        return data;
    }


}
