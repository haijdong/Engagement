package cn.segi.framework.util;

/**
 * 重复点击工具类
 * Created by Kinwee on 2017/2/27.
 */
public class DoubleClickUtil {

    private static long lastClickTime = 0L;
    private static final long INTERVAL_TIME = 1000;
    /**
     * 是否重复点击
     */
    public static boolean isMuitClick(){
        long currClickTime = System.currentTimeMillis();
        if(currClickTime - lastClickTime <INTERVAL_TIME){
            lastClickTime = System.currentTimeMillis();
            return true;
        }
        return false;
    }
    
    public static boolean isMuitClick(long intervalTime){
        long currClickTime = System.currentTimeMillis();
        if(currClickTime - lastClickTime <intervalTime){
            lastClickTime = System.currentTimeMillis();
            return true;
        }
        return false;
    }

}
