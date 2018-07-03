package cn.segi.framework.util;

import java.lang.reflect.Field;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.util.DisplayMetrics;
import android.view.inputmethod.InputMethodManager;

/**
 * 
 * @author tang
 *
 */

public class Machine {
	private static boolean sCheckTablet = false;
	private static boolean sIsTablet = false;

	// 判断当前设备是否为平板
	private static boolean isPad(Context context) {
	    DisplayMetrics dm = context.getResources().getDisplayMetrics();
	    double x = Math.pow(dm.widthPixels, 2);  
	    double y = Math.pow(dm.heightPixels, 2);  
	    // 屏幕尺寸  
	    double screenInches = Math.sqrt(x + y)/dm.densityDpi;  
	    if ((int)screenInches == 7) {
	    	return (context.getResources().getConfiguration().screenLayout
	                & Configuration.SCREENLAYOUT_SIZE_MASK)
	                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
		}else if ((int)screenInches > 7){
			return (context.getResources().getConfiguration().screenLayout
	                & Configuration.SCREENLAYOUT_SIZE_MASK)
	                >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
		}else{
			return false;
		}
	}

	public static boolean isTablet(Context context) {
		if (sCheckTablet == true) {
			return sIsTablet;
		}
		sCheckTablet = true;
		sIsTablet = isPad(context);
		return sIsTablet;
	}
	
	/**
	 * 关闭输入法
	 * @return
	 */
	public static void closeInputMethod(Activity activity,Context context) {
		InputMethodManager inputMethodManager = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(),
		InputMethodManager.HIDE_NOT_ALWAYS);
	}
	
	/**
	 * 获取通知栏的高度
	 * @param context
	 * @return
	 * @author tangst
	 */
	public static int getStatusBarHeight(Context context){
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        } 
        return statusBarHeight;
    }
}
