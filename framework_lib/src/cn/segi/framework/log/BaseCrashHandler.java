package cn.segi.framework.log;

import java.lang.Thread.UncaughtExceptionHandler;

import android.content.Context;
import cn.segi.framework.executor.ExecutorSupport;
import cn.segi.framework.util.FrameworkUtil;

/**
 * 程序异常处理
 * 
 * @author
 * 
 */
public class BaseCrashHandler implements UncaughtExceptionHandler {
	private static BaseCrashHandler mCrashHandler = null;
	private Context mContext;
	private Thread.UncaughtExceptionHandler defaultHandler = Thread
			.getDefaultUncaughtExceptionHandler();
	/**
	 * 是否按照默认方式处理异常 false 表示按照自己的方式处试true 表示按照系统的处理方试
	 */
	private static final boolean IS_DEFAULT_ERROR = true;

	private BaseCrashHandler(Context context) {
		this.mContext = context;
		Thread.setDefaultUncaughtExceptionHandler(this);
	}

	public static BaseCrashHandler getInstance(Context context) {
		if (null == mCrashHandler) {
			mCrashHandler = new BaseCrashHandler(context);
		}
		return mCrashHandler;
	}

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		// TODO Auto-generated method stub
		/**
		 * 代码发布前，将所有出现异常的情况抛出 减少正式发布的时候异常出现的次数
		 */
		handleUserDefinedException(ex);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 否则按照系统默认的处理进行处理
		defaultHandler.uncaughtException(thread, ex);

	}

	/**
	 * 自定义错误处理收集错误信息
	 * 
	 * @param ex
	 * @return true:如果处理了该异常信息;否则返回false.
	 */
	private void handleUserDefinedException(final Throwable ex) {
		if (ex == null) {
			return;
		}
		Runnable mRunnable = new Runnable() {

			@Override
			public void run() {
				FrameworkUtil.saveCrashInfoToFile(ex);
			}
		};
		ExecutorSupport.getExecutor().execute(mRunnable);
	}

}
