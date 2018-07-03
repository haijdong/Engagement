package cn.segi.framework.consts;

import android.os.Environment;

import java.io.File;

import cn.segi.framework.application.BaseApplication;

/**
 * 框架初始化参数设置，通常只要设置PROJECT_NAME即可
 * @author leipeng
 * 
 */
public final class FrameworkConst {
	/**
	 * 日志文件大小
	 */
	public static final int LOG_MAX_SIZE = 1024 * 1024;// 1M
	/**
	 * 日志文件夹名称
	 */
	private static final String LOG__DIRECTORY = "logDirectory";
	/**
	 * 图片文件夹名称
	 * 
	 */
	public static final String IMAGE_DIRECTORY = "imageDirectory";
	/**
	 * 音频目录
	 */
	private static final String AUDIO_DIRECTORY = "audioDirectory";
	/**
	 * 升级APK保存目录
	 * 
	 */
	private static final String APK_DIRECTORY = "apkDirectory";
	/**
	 * 文档文件保存目录
	 */
	private static final String FILE_DIRECTORY = "fileDirectory";
	/**
	 * 压缩图片的保存目录 例如 相框选择图片压缩存放在本目录
 	 * 
	 */
	public static final String IMAGE_COMPRESS_DIRECTORY = "comPressDirectory";
	/**
	 * 项目名称 项目初始化的时候初始化
	 */
	public static final String PROJECT_NAME = BaseApplication.getContext().getPackageName();
	/**
	 * 日志名称
	 */
	public static final String LOG_NAME = "error_log.txt";
	/**
	 * nomedia文件（用于系统相册不检测项目下的图片目录）
	 */
	public static final String NOMEDIA = ".nomedia";
	/**
	 * 项目相关文件路径
	 */
	public static final String FILE_PATH = Environment
			.getExternalStorageDirectory().getAbsolutePath()
			+ File.separator
			+ PROJECT_NAME;
	/**
	 * 项目图片文件路径
	 */
	public static final String IMAGE_PATH = FILE_PATH + File.separator + IMAGE_DIRECTORY;
	/**
	 * 项目日志文件路径
	 */
	public static final String LOG_PATH = FILE_PATH + File.separator + LOG__DIRECTORY;
	/**
	 * 项目图片文件路径
	 */
	public static final String APK_PATH = FILE_PATH + File.separator + APK_DIRECTORY;
	/**
	 * 项目图片压缩文件路径
	 */
    public static final String LOCAL_COMPRESS_CAMERA_PATH = FILE_PATH + File.separator + IMAGE_COMPRESS_DIRECTORY;
    /**
	 * 项目音频文件路径
	 */
    public static final String AUDIO_PATH = FILE_PATH + File.separator + AUDIO_DIRECTORY;
    /**
	 * 项目其他文件路径
	 */
    public static final String OTHER_FILE_PATH = FILE_PATH + File.separator + FILE_DIRECTORY;

}
