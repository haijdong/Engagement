package cn.segi.framework.util;

import android.annotation.SuppressLint;
import android.os.Environment;
import android.os.StatFs;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.segi.framework.consts.FrameworkConst;

/**
 * 项目无关的工具类
 * 
 * @author leipeng
 * 
 */
@SuppressLint("SimpleDateFormat")
public final class FrameworkUtil {
	/**
	 * 判断是否存在SD卡
	 */
	public static boolean isExistSDCard() {
		return android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);

	}

	/**
	 * 获取SD卡剩余空间
	 * 
	 * @return
	 */
	public static long getSDFreeSize() {
		// 取得SD卡文件路径
		File path = Environment.getExternalStorageDirectory();
		StatFs sf = new StatFs(path.getPath());
		// 获取单个数据块的大小(Byte)
		long blockSize = sf.getBlockSize();
		// 空闲的数据块
		long freeBlocks = sf.getAvailableBlocks();
		return (freeBlocks * blockSize); // 单位MB
	}

	/**
	 * 获取SD卡路径
	 * 
	 * @return
	 */
	public static String getSDPath() {
		String path = "";
		if (isExistSDCard()) {
			path = android.os.Environment.getExternalStorageDirectory().toString();
		}
		return path;
	}

	/**
	 * 将异常信息保存到SD卡文件
	 * 
	 * @param ex
	 * @return
	 */
	public static void saveCrashInfoToFile(Throwable ex) {
		if (!isExistSDCard()) {// 如果没有sdcard，则不存
			return;
		}
		if (getSDFreeSize() < FrameworkConst.LOG_MAX_SIZE) {
			return;
		}
		Writer writer = null;
		PrintWriter printWriter = null;
		String stackTrace = "";
		try {
			writer = new StringWriter();
			printWriter = new PrintWriter(writer);
			ex.printStackTrace(printWriter);
			Throwable cause = ex.getCause();
			while (cause != null) {
				cause.printStackTrace(printWriter);
				cause = cause.getCause();
			}
			stackTrace = writer.toString();
		} catch (Exception e) {
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (printWriter != null) {
				printWriter.close();
			}
		}
		StringBuilder sb = new StringBuilder();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		String timestamp = sdf.format(date);
		sb.append("=============error log start=================");
		sb.append(timestamp);
		sb.append(System.getProperty("line.separator"));
		sb.append(stackTrace);
		sb.append("=============error log end=================");
		BufferedWriter mBufferedWriter = null;
		try {
			String path = FrameworkConst.LOG_PATH
					+ File.separator +FrameworkConst.LOG_NAME;
			File mFile = new File(path);
			File pFile = mFile.getParentFile();
			if (!pFile.exists()) {
				pFile.mkdirs();
			}
			if (mFile.length() > FrameworkConst.LOG_MAX_SIZE) {
				mFile.delete();
				mFile.createNewFile();
			}
			mBufferedWriter = new BufferedWriter(new FileWriter(mFile, true));
			mBufferedWriter.append(sb.toString());
			mBufferedWriter.flush();
			mBufferedWriter.close();
		} catch (IOException e) {
		} finally {
			if (mBufferedWriter != null) {
				try {
					mBufferedWriter.close();
				} catch (IOException e) {
				}
			}
		}
	}

	/**
	 * 获取图片路径
	 * 
	 * @return
	 */
	public static String getImagePath() {
		return FrameworkConst.IMAGE_PATH;
	}

	/**
	 * 获取日志路径
	 * 
	 * @return
	 */
	public static String getLogPath() {
		return FrameworkConst.LOG_PATH;
	}

	/**
	 * 获取APK路径
	 * 
	 * @return
	 */
	public static String getApkPath() {
		return FrameworkConst.APK_PATH;
	}
	
	/**
	 * 获取音频路径
	 * 
	 * @return
	 */
	public static String getAudioPath() {
		return FrameworkConst.AUDIO_PATH;
	}

	/**
	 * 获取其他文件目录路径
	 * @return
	 */
	public static String getOtherFilePath() {
		return FrameworkConst.OTHER_FILE_PATH;
	}
	
	/**
	 * 获取压缩图片路径
	 * 
	 * @return
	 */
	public static String getCompressImagePath() {
		return FrameworkConst.LOCAL_COMPRESS_CAMERA_PATH;
	}
	
	/**
	 * 获取项目目录路径
	 * 
	 * @return
	 */
	public static String getApplicationPath() {
		return FrameworkConst.FILE_PATH;
	}


}
