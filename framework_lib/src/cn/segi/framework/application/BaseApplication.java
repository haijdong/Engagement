package cn.segi.framework.application;

import android.app.Application;
import android.content.Intent;
import android.support.multidex.MultiDexApplication;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.io.File;
import java.util.HashMap;

import cn.segi.framework.consts.FrameworkConst;
import cn.segi.framework.executor.ExecutorSupport;
import cn.segi.framework.log.BaseCrashHandler;
import cn.segi.framework.util.FrameworkUtil;


public abstract class BaseApplication extends Application {

	/**
	 * 是否打开日志
	 */
	public static boolean IS_DEBUGGER = true;
	/**
	 * 网络请求队列
	 */
	protected static RequestQueue mRequestQueue;
	/**
	 * 全局上下文
	 */
	protected static BaseApplication mContext;
	/**
	 * 自定义HTTPS证书参数
	 */
	protected static HashMap<String, Object> mHttpsCertificateParams;
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		mContext = this;
		startApplication();
	}
	
	/**
	 * 应用启动时调用的方法
	 */
	private void startApplication(){
//		createDir();
		BaseCrashHandler.getInstance(BaseApplication.this);
		initHttpsCertificateParams();
	}

	/**
	 * 初始化HTTPS证书参数
	 */
	protected void initHttpsCertificateParams()
	{
	}
	
	/**
	 * 
	 * 创建日志，apk，图片3个文件夹
	 */
	protected void createDir(){
		//创建项目相关根目
		String rootPath = FrameworkConst.FILE_PATH;
		File mRootFile = new File(rootPath);
		if (!mRootFile.exists()) {
			mRootFile.mkdir();
		}
		//升级apk的保存目
		File mApkFile = new File(FrameworkUtil.getApkPath());
		if (!mApkFile.exists()) {
			mApkFile.mkdir();
		}
		//日志的保存目
		File mLogFile = new File(FrameworkUtil.getLogPath());
		if (!mLogFile.exists()) {
			mLogFile.mkdir();
		}
		//图片的保存目
		File mImageFile = new File(FrameworkUtil.getImagePath());
		if (!mImageFile.exists()) {
			mImageFile.mkdir();
		}
		//音频的保存目
		File mAudioFile = new File(FrameworkUtil.getAudioPath());
		if (!mAudioFile.exists()) {
			mAudioFile.mkdir();
		}
		//图片压缩的保存目
		File mCompressFile = new File(FrameworkUtil.getCompressImagePath());
		if (!mCompressFile.exists()) {
			mCompressFile.mkdir();
		}
		//图片压缩的保存目
		File mOtherFile = new File(FrameworkUtil.getOtherFilePath());
		if (!mOtherFile.exists()) {
			mOtherFile.mkdir();
		}
	}
	
	/**
	 * 获取上下文
	 * @return
	 */
	public static BaseApplication getContext() {
		return mContext;
	}
	
	/**
	 * 获取网络请求队列
	 */
	public static RequestQueue getRequestQueue()
	{
		if (null == mRequestQueue) {
			if (null == mHttpsCertificateParams) {
				mRequestQueue = Volley.newRequestQueue(mContext);
			} else {
				mRequestQueue = Volley.newRequestQueue(mContext,
						(Integer) mHttpsCertificateParams.get("certRawResId")
						, (String) mHttpsCertificateParams.get("protocol")
						, (String) mHttpsCertificateParams.get("certType"));
			}
		}
		return mRequestQueue;
	}

	
	/**
	 * 启动service
	 */
	private void startBaseService(Intent serviceIntent) {
		if(null!=serviceIntent){
//			serviceIntent.setClass(this, BaseService.class);
			startService(serviceIntent);
		}
	}
	/**
	 * 停止service
	 * @param serviceIntent
	 */
	private void stopBaseService(Intent serviceIntent){
		if(null!=serviceIntent){
			this.stopService(serviceIntent);
		}
	}
	
	@Override
	public void onTerminate() {
		super.onTerminate();
		terminateApplication();
	};

	@Override
	public void onLowMemory() {
		// TODO Auto-generated method stub
		super.onLowMemory();
	}
	
	/**
	 * 退出应用调用
	 */
	public void terminateApplication() {
//		if(null!=dbUtils){
//			dbUtils.closeDB();
//			dbUtils = null;
//		}
		ExecutorSupport.shutdown();
	}
	
	
}
