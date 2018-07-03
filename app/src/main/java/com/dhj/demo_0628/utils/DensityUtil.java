package com.dhj.demo_0628.utils;

import android.content.res.Resources;

/**
 * 尺寸工具类
 *
 * @author shenghaiyang
 */
public final class DensityUtil {

    private DensityUtil() {
    }

    /**
     * dp转为px
     *
     * @param value dp
     * @return px
     */
    public static int dp2px(float value) {
        return Math.round(value * Resources.getSystem().getDisplayMetrics().density);
    }

    /**
     * sp转为px
     *
     * @param value sp
     * @return px
     */
    public static int sp2px(float value) {
        return Math.round(value * Resources.getSystem().getDisplayMetrics().scaledDensity);
    }
}
