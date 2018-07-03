package cn.segi.framework.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

/**
 * 获取版本信息
 * @author liangzx
 * @version 1.0
 */
public class AppVersionUtil {
	
	/**
	 * 获取版本名称
	 * @param context
	 * @return
	 */
	public static String getVersionName(Context context){
        // 获取packagemanager的实例
        PackageManager packageManager = context.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = null;
		try {
			packInfo = packageManager.getPackageInfo(context.getPackageName(),0);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		String version ="";
		if(packInfo!=null) {
			version= packInfo.versionName;
		}
        return version;
	}
	
	/**
	 * 获取版本号
	 * @param context
	 * @return
	 */
	public static int getVersionCode(Context context){
		// 获取packagemanager的实例
		PackageManager packageManager = context.getPackageManager();
		// getPackageName()是你当前类的包名，0代表是获取版本信息
		PackageInfo packInfo = null;
		try {
			packInfo = packageManager.getPackageInfo(context.getPackageName(),0);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		int version = 0;
		if(packInfo!=null) {
			version= packInfo.versionCode;
		}
		return version;
	}
}
