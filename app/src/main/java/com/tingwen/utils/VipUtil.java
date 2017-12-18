package com.tingwen.utils;

import com.tingwen.app.AppSpUtil;

/**
 * Vip 相关工具类
 * Created by Administrator on 2017/10/30 0030.
 */
public class VipUtil {

    private volatile static VipUtil instance;
    private VipUtil (){}
    public static VipUtil getInstance() {
        if (instance == null) {
            synchronized (VipUtil.class) {
                if (instance == null) {
                    instance = new VipUtil();
                }
            }
        }
        return instance;
    }


    /**
     * @return int
     * 描述：获取用户会员状态 0 不是会员 1 会员 2 超级会员
     *
     */
    public int getVipState() {
        if (LoginUtil.isUserLogin()) {
            return AppSpUtil.getInstance().getUserInfo().getResults().getMember_type();
        } else {
            return 0;
        }
    }




}
