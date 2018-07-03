package cn.segi.framework.grayredirect;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 重定向URL和cookie存储工具类
 * 为了避免被内存回收，改造成shareperference方式存储
 * @author leip
 */
public final class GrayRedirectUtil {
    /**
     * 重定向URL存储map
     */
    private static final Map<String, String> redirectUrlMap = new HashMap<String, String>();
    @Deprecated
    /**
     * 获取重定向的url
     *
     * @param key
     * @return
     */
    public static String getRedirectUrl(String key) {
        return redirectUrlMap.get(key);
    }
    @Deprecated
    /**
     * 设置重定向的url
     *
     * @param key
     * @param redirectUrl
     * @return
     */
    public static void putRedirectUrl(String key, String redirectUrl) {
        redirectUrlMap.put(key, redirectUrl);
    }

    /**
     * 设置重定向cookie
     *
     * @param key
     * @param cookie
     * @return
     */
    public static void putCookie(String key, String cookie) {
        GrayRedirectSharedPreferences.getInstance().putCookie(key,cookie);
    }

    /**
     * 获取cookie
     *
     * @return
     */
    public static String getCookies() {
        String cookies = "";
        Map<String,String> cookieMap = GrayRedirectSharedPreferences.getInstance().getAllCookies();
        if(null!=cookieMap){
            Set<Map.Entry<String, String>> entrySet = cookieMap.entrySet();
            for (Map.Entry<String, String> entry : entrySet) {
                cookies += (entry.getKey() + "=" + entry.getValue() + ";");
            }
        }
        return cookies;
    }

    /**
     * 获取cookie
     *
     * @return
     */
    public static Map<String,String> getCookiesForMap() {
        Map<String,String> cookieMap = GrayRedirectSharedPreferences.getInstance().getAllCookies();
        return cookieMap;
    }

    /**
     * 清空灰度以及cookie静态map
     * 建议放到登录接口前先清空之前的缓存数据比较保险
     */
    public static void cleanGrayRedirect() {
        redirectUrlMap.clear();
        GrayRedirectSharedPreferences.getInstance().clearAllCookies();
    }

}
