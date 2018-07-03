package cn.segi.framework.grayredirect;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;

import cn.segi.framework.application.BaseApplication;

/**
 * Created by Administrator on 2017/7/20.
 * 无状态化和灰度的存储
 */

public class GrayRedirectSharedPreferences {
    /**
     * UserInfoPreferences单例
     */
    private static volatile GrayRedirectSharedPreferences sInstance;
    private static final String GRAY_REDIRECT = "gray_redirect";
    private SharedPreferences mGrayRedirectSp;

    /**
     * 构造
     *
     * @param context
     */
    private GrayRedirectSharedPreferences(Context context) {
        mGrayRedirectSp = context.getSharedPreferences(GRAY_REDIRECT, Context.MODE_PRIVATE);
    }

    /**
     * 获取UserInfoPreferences单例
     *
     * @param
     * @return UserInfoPreferences单例
     */
    public static GrayRedirectSharedPreferences getInstance() {
        if (sInstance == null) {
            synchronized (GrayRedirectSharedPreferences.class){
                if (sInstance == null) {
                    sInstance = new GrayRedirectSharedPreferences(BaseApplication.getContext());
                }
            }
        }
        return sInstance;
    }

    /**
     * 保存对应的键值对
     *
     * @param key
     * @param cookie
     */
    public void putCookie(String key, String cookie) {
        if (null != mGrayRedirectSp) {
            mGrayRedirectSp.edit().putString(key, cookie).commit();
        }
    }

    /**
     * 获取所有的cookies
     *
     * @return
     */
    public Map<String, String> getAllCookies() {
        if (null != mGrayRedirectSp) {
            return (Map<String, String>) mGrayRedirectSp.getAll();
        }
        return null;
    }

    /**
     * 清空所有的cookie
     */
    public void clearAllCookies() {
        if (null != mGrayRedirectSp) {
            mGrayRedirectSp.edit().clear().commit();
        }
    }
}
