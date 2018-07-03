package cn.segi.framework.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Environment;
import android.view.inputmethod.InputMethodManager;

/**
 * 环境工具类
 * @author tangst
 * @version [uHome, 2013-11-27]
 */
public class EnvUtil {
	/**
	 * 判断sdcard是否存在
	 * @return
	 * @author tangst
	 */
	
	public static boolean isExitSDCard() {
		if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
			return true;
		} else{
			return false;
		}
	}
	
	/**
	 * 获取SDCard路径
	 * @return
	 * @author tangst
	 */
	public static String getSDCardPath() {
		return Environment.getExternalStorageDirectory().toString();
	}
	
	/**
	 * 获取SDCard中保存外部文件的包名
	 * @return
	 * @author tangst
	 */
	public static String getSDCardPakageName() {
		return "segi/uhome/";
	}
	
	/**
	 * 关闭输入法
	 * @param activity
	 */
	public static void closeInputMethod(Activity activity) {
		InputMethodManager inputMethodManager = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
		if(activity.getCurrentFocus()!=null) {
			inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}
	
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
