package cn.segi.framework.util;

import java.lang.reflect.Method;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;

import cn.segi.framework.application.BaseApplication;

public class WindowDispaly {

    private static DisplayMetrics dm;

//	private static int widthPixels;

//	private static int heightPixels;

    /**
     * 初始化屏幕参数
     */
    private static void initWindowDisplay() {
        dm = BaseApplication.getContext().getResources().getDisplayMetrics();
    }

    /**
     * 获取屏幕宽度
     *
     * @return
     */
    public static int getWidth() {
        if (null == dm) {
            initWindowDisplay();
        }
        return dm.widthPixels;
    }

    /**
     * 获取屏幕高度
     *
     * @return
     */
    public static int getHeight() {
        if (null == dm) {
            initWindowDisplay();
        }
        return dm.heightPixels;
    }

    /**
     * 获取屏幕密度
     *
     * @return
     */
    public static float getDensity() {
        if (null == dm) {
            initWindowDisplay();
        }
        return dm.density;
    }

    /**
     * 获取屏幕密度dpi
     *
     * @return
     */
    public static float getDensityDpi() {
        if (null == dm) {
            initWindowDisplay();
        }
        return dm.densityDpi;
    }

    /**
     * dp转化px
     *
     * @param context
     * @param dp
     * @return
     */
    public static int dip2px(Context context, float dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (dp * density + 0.5f);
    }

    /**
     * px转化dp
     *
     * @param context
     * @param px
     * @return
     */
    public static int px2dip(Context context, float px) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (px / density + 0.5f);
    }

    /**
     * 获取
     *
     * @param res
     * @param key
     * @return
     */
    public static int getStatusBarHeight() {
        int result = 0;
        int resourceId = BaseApplication.getContext().getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = BaseApplication.getContext().getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static int getNavigationBarHeight(Context context) {
        int fullScreenHeight = 0;
        int visiableHeight = context.getResources().getDisplayMetrics().heightPixels;
        Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        Class c;
        try {
            c = Class.forName("android.view.Display");
            Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
            method.invoke(display, dm);
            fullScreenHeight = dm.heightPixels;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fullScreenHeight - visiableHeight;
    }

    /**
     * 获取
     *
     * @param res
     * @param key
     * @return
     */
    public static int getDimenSize(Context context, int deminId) {
        int result = 0;
        result = context.getResources().getDimensionPixelSize(deminId);
        return result;
    }
}
