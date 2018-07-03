package cn.segi.framework.log;

import android.util.Log;
import cn.segi.framework.application.BaseApplication;

public final class Logger
{
	public static final String TAG = "framework_lib";
	

	public static void d(String msg)
	{
		if (BaseApplication.IS_DEBUGGER)
		{
			Log.d(TAG, msg);
		}
	}

	public static void w(String msg)
	{
		if (BaseApplication.IS_DEBUGGER)
		{
			Log.w(TAG, msg);
		}
	}

	public static void e(String msg)
	{
		if (BaseApplication.IS_DEBUGGER)
		{
			Log.e(TAG, msg);
		}
	}

	public static void i(String msg)
	{
		if (BaseApplication.IS_DEBUGGER)
		{
			Log.i(TAG, msg);
		}
	}

	public static void d(String TAG, String msg)
	{
		if (BaseApplication.IS_DEBUGGER)
		{
			Log.d(TAG, msg);
		}
	}

	public static void w(String TAG, String msg)
	{
		if (BaseApplication.IS_DEBUGGER)
		{
			Log.w(TAG, msg);
		}
	}

	public static void e(String TAG, String msg)
	{
		if (BaseApplication.IS_DEBUGGER)
		{
			Log.e(TAG, msg);
		}
	}

	public static void i(String TAG, String msg)
	{
		if (BaseApplication.IS_DEBUGGER)
		{
			Log.i(TAG, msg);
		}
	}
	
	public static void d(String TAG, String msg, Throwable e)
	{
		if (BaseApplication.IS_DEBUGGER)
		{
			Log.d(TAG, msg, e);
		}
	}
	
	public static void w(String TAG, String msg, Throwable e)
	{
		if (BaseApplication.IS_DEBUGGER)
		{
			Log.w(TAG, msg, e);
		}
	}
	
	public static void e(String TAG, String msg, Throwable e)
	{
		if (BaseApplication.IS_DEBUGGER)
		{
			Log.e(TAG, msg, e);
		}
	}
	
	public static void i(String TAG, String msg, Throwable e)
	{
		if (BaseApplication.IS_DEBUGGER)
		{
			Log.i(TAG, msg, e);
		}
	}
}
