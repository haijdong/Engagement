/*
 * 文件名: StringUtils.java
 * 创建人: Transfer
 * 创建时间: 2012-11-22
 * 版     权：Copyright Easier Digital Tech. Co. Ltd. All Rights Reserved.
 */
package cn.segi.framework.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Properties;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Environment;
import android.view.Window;
import android.view.WindowManager;

public class SystemBarUtil {

	public static void initSystemBarStyle(Activity mContext, int colorId) {

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//			if (Build.MODEL.contains("MI") && isMiUi6()) {
//				initSystemBarForXm(mContext);
//			} else {
				setTranslucentStatus(mContext);
//			}
			SystemBarTintManager tintManager = new SystemBarTintManager(mContext);
			tintManager.setStatusBarTintEnabled(true);
			tintManager.setStatusBarAlpha(1.0f);
			if (colorId != 0) {
				tintManager.setStatusBarTintColor(mContext.getResources().getColor(colorId));
			}
//			tintManager.setNavigationBarTintEnabled(true);
//			tintManager.setNavigationBarAlpha(1.0f);
//			tintManager.setNavigationBarTintColor(mContext.getResources()
//					.getColor(R.color.green));
		}
	}

	@TargetApi(19)
	private static void setTranslucentStatus(Activity mContext) {
		Window win = mContext.getWindow();
		// 透明状态栏
		win.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		// 透明导航栏
//		win.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		WindowManager.LayoutParams winParams = win.getAttributes();
		final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
		winParams.flags |= bits;
		win.setAttributes(winParams);
	}

	private static void initSystemBarForXm(Activity mContext) {
		Window window = mContext.getWindow();
		Class clazz = window.getClass();
		try {
			int tranceFlag = 0;
			int darkModeFlag = 0;
			Class layoutParams = Class
					.forName("android.view.MiuiWindowManager$LayoutParams");
			Field field = layoutParams
					.getField("EXTRA_FLAG_STATUS_BAR_TRANSPARENT");
			tranceFlag = field.getInt(layoutParams);
			field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
			darkModeFlag = field.getInt(layoutParams);
			Method extraFlagField = clazz.getMethod("setExtraFlags", int.class,
					int.class);
			// 只需要状态栏透明
			extraFlagField.invoke(window, tranceFlag, tranceFlag); // 状态栏透明且黑色字体
																	// extraFlagField.invoke(window,
																	// tranceFlag
																	// |
																	// darkModeFlag,
																	// tranceFlag
																	// |
																	// darkModeFlag);
																	// //清除黑色字体
																	// extraFlagField.invoke(window,
																	// 0,
																	// darkModeFlag);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	public static boolean isMiUi6(){
		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream(new File(Environment.getRootDirectory(), "build.prop")));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    return "V6".equalsIgnoreCase(properties.getProperty("ro.miui.ui.version.name"));
	}
}
