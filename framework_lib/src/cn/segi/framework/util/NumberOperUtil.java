package cn.segi.framework.util;

import java.math.BigDecimal;

/**
 * 数字操作工具类
 *
 * @author liangzx
 * @version 1.0
 * @time 2017-10-11 13:10
 **/
public class NumberOperUtil {

    /**
     * 当浮点型数据位数超过10位之后，数据变成科学计数法显示。用此方法可以使其正常显示。
     * @param value
     * @return
     */
    public static String formatFloatNumber(float value) {
        if(value != 0.0f){
            java.text.DecimalFormat df = new java.text.DecimalFormat("###.##");
            return df.format(value);
        }else{
            return "0";
        }

    }

    /**
     * 当浮点型数据位数超过10位之后，数据变成科学计数法显示。用此方法可以使其正常显示。
     * @param value
     * @return
     */
    public static String formatFloatNumber(Float value) {
        if(value != null){
            if(value.floatValue() != 0.0f){
                java.text.DecimalFormat df = new java.text.DecimalFormat("###.##");
                return df.format(value.doubleValue());
            }else{
                return "0";
            }
        }
        return "0";
    }

    /**
     * 当浮点型数据位数超过10位之后，数据变成科学计数法显示。用此方法可以使其正常显示。
     * @param value
     * @return
     */
    public static String formatDoubleNumber(double value) {
        if(value != 0.00){
            java.text.DecimalFormat df = new java.text.DecimalFormat("###.##");
            return df.format(value);
        }else{
            return "0";
        }

    }

    /**
     * 当浮点型数据位数超过10位之后，数据变成科学计数法显示。用此方法可以使其正常显示。
     * @param value
     * @return
     */
    public static String formatDoubleNumber(Double value) {
        if(value != null){
            if(value.doubleValue() != 0.00){
                java.text.DecimalFormat df = new java.text.DecimalFormat("###.##");
                return df.format(value.doubleValue());
            }else{
                return "0";
            }
        }
        return "0";
    }

    /**
     * 相加
     * @param d1
     * @param d2
     * @return
     */
    public static double add(double d1, double d2){
        BigDecimal b1 = new BigDecimal(Double.toString(d1));
        BigDecimal b2 = new BigDecimal(Double.toString(d2));
        return b1.add(b2).doubleValue();
    }

    /**
     * 相加
     * @param f1
     * @param f2
     * @return
     */
    public static float add(float f1, float f2){
        BigDecimal b1 = new BigDecimal(Float.toString(f1));
        BigDecimal b2 = new BigDecimal(Float.toString(f2));
        return b1.add(b2).floatValue();
    }

    /**
     * 相减
     * @param d1
     * @param d2
     * @return
     */
    public static double sub(double d1, double d2){
        BigDecimal b1 = new BigDecimal(Double.toString(d1));
        BigDecimal b2 = new BigDecimal(Double.toString(d2));
        return b1.subtract(b2).doubleValue();
    }

    /**
     * 相减
     * @param f1
     * @param f2
     * @return
     */
    public static float sub(float f1, float f2){
        BigDecimal b1 = new BigDecimal(Float.toString(f1));
        BigDecimal b2 = new BigDecimal(Float.toString(f2));
        return b1.subtract(b2).floatValue();
    }

    /**
     * 相乘
     * @param d1
     * @param d2
     * @return
     */
    public static double mul(double d1, double d2){
        BigDecimal b1 = new BigDecimal(Double.toString(d1));
        BigDecimal b2 = new BigDecimal(Double.toString(d2));
        return b1.multiply(b2).doubleValue();
    }

    /**
     * 相乘
     * @param f1
     * @param f2
     * @return
     */
    public static float mul(float f1, float f2){
        BigDecimal b1 = new BigDecimal(Float.toString(f1));
        BigDecimal b2 = new BigDecimal(Float.toString(f2));
        return b1.multiply(b2).floatValue();
    }

    /**
     * 相除
     * @param d1
     * @param d2
     * @param scale
     * @return
     */
    public static double div(double d1, double d2, int scale){
        if(scale < 0){
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(Double.toString(d1));
        BigDecimal b2 = new BigDecimal(Double.toString(d2));
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();

    }

    /**
     * 相除
     * @param f1
     * @param f2
     * @param scale
     * @return
     */
    public static float div(float f1, float f2, int scale){
        if(scale < 0){
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(Float.toString(f1));
        BigDecimal b2 = new BigDecimal(Float.toString(f2));
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).floatValue();

    }
}
