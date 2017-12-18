
package com.tingwen.net;

import java.io.Serializable;

/**
 * 只有msg 和 code 的包装类
 */
public class SimpleResponse implements Serializable {

    private static final long serialVersionUID = -1477609349345966116L;

    public int status;
    public String msg;

    public BaseResponse toBaseResponse() {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.status = status;
        baseResponse.msg = msg;
        return baseResponse;
    }
}
