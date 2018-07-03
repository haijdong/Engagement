package cn.segi.framework.util;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;

/**
 * Created by leip on 2017/7/1.
 */

public final class MetaDataUtil {
    //在application应用<meta-data>元素。
    public static String readActivityMetaData(Activity activity, String metaKey) {
        try {
            ActivityInfo info = activity.getPackageManager().getActivityInfo(activity.getComponentName(), PackageManager.GET_META_DATA);
            return info.metaData.getString(metaKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    //在application应用<meta-data>元素。
    public static String readApplicationMetaData(Context mContext, String metaKey) {
        try {
            ApplicationInfo appInfo = mContext.getPackageManager().getApplicationInfo(mContext.getPackageName(), PackageManager.GET_META_DATA);
            return appInfo.metaData.getString(metaKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";

    }


    //在service应用<meta-data>元素。
    public static String readServiceMetaData(Context mContext, String metaKey, Class serviceClass) {
        try {
            ComponentName cn = new ComponentName(mContext, serviceClass);
            ServiceInfo info = mContext.getPackageManager().getServiceInfo(cn, PackageManager.GET_META_DATA);
            return info.metaData.getString(metaKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";

    }

    //在receiver应用<meta-data>元素。
    public static String readReceiverMetaData(Context mContext, String metaKey, Class receiverClass) {
        try {
            ComponentName cn = new ComponentName(mContext, receiverClass);
            ActivityInfo info = mContext.getPackageManager().getReceiverInfo(cn, PackageManager.GET_META_DATA);
            return info.metaData.getString(metaKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}