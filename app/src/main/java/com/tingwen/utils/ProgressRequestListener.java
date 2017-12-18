package com.tingwen.utils;

/**
 * Created by Administrator on 2016/7/10.
 */
public interface ProgressRequestListener {
    void onRequestProgress(long bytesWritten, long contentLength, boolean done, boolean isFailed);
}
