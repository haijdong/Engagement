/*
 * 文件名：FileUtil.java
 * 创建人：Administrator
 * 创建时间：2012-12-5
 * 版     权：Copyright Easier Digital Tech. Co. Ltd. All Rights Reserved.
 */
package cn.segi.framework.util;

import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileLock;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.segi.framework.executor.ExecutorSupport;


/**
 * File通用操作工具类
 * 
 * @author liangzx
 * @version [segi, 2012-12-11] 
 */
public class FileUtil {
    /**
     * FileUtil TAG
     */
    private static final String TAG = "FileUtil";

    
    /**
     * 文件转化byte[]操作
     * @param fileName 文件路径
     * @return 文件的byte[]格式
     */
    public static byte[] fileToByte(String fileName) {
        try {
            return fileToByte(new File(fileName));
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "fileToByte error");
        }
        return null;
    }
    
    /**
     * 文件转化byte[]操作
     * 
     * @param file 需要转化为byte[]的文件
     * @return 文件的byte[]格式
     * @throws IOException IO流异常
     */
    public static byte[] fileToByte(File file) throws IOException {
        InputStream in = new FileInputStream(file);
        try {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            byte[] barr = new byte[1024];
            while (true) {
                int r = in.read(barr);
                if (r <= 0) {
                    break;
                }
                buffer.write(barr, 0, r);
            }
            return buffer.toByteArray();
        } finally {
            closeStream(in);
        }
    }
    
    /**
     * 将文件的byte[]格式转化成一个文件
     * 
     * @param b 文件的byte[]格式
     * @param fileName 文件名称
     * @return 转化后的文件
     */
    public static File byteToFile(byte[] b, String fileName) {
        BufferedOutputStream bos = null;
        File file = null;
        // 增加文件锁处理
        FileLock fileLock = null;
        try {
            file = new File(fileName);
            if (!file.exists()) {
                File parent = file.getParentFile();
                // 此处增加判断parent != null && !parent.exists()
                if (parent != null && !parent.exists() && !parent.mkdirs()) {
                    // 创建不成功的话，直接返回null
                    return null;
                }
            }
            FileOutputStream fos = new FileOutputStream(file);
            // 获取文件锁
            fileLock = fos.getChannel().tryLock();
            if (fileLock != null) {
                bos = new BufferedOutputStream(fos);
                bos.write(b);
            }
        } catch (IOException e) {
            Log.e(TAG, "byteToFile error", e);
        } finally {
            if (fileLock != null) {
                try {
                    fileLock.release();
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e(TAG, "fileLock.release() error");
                }
            }
            // ***END***  [ST2-图片读取管理] 王玉丰 2012-8-6 modify
            closeStream(bos);
        }
        return file;
    }

    /**
     * 专门用来关闭可关闭的流
     * 
     * @param beCloseStream 需要关闭的流
     * @return 已经为空或者关闭成功返回true，否则返回false
     */
    public static boolean closeStream(java.io.Closeable beCloseStream) {
        if (beCloseStream != null) {
            try {
                beCloseStream.close();
                return true;
            } catch (IOException e) {
                Log.e(TAG, "close stream error", e);
                return false;
            }
        }
        return true;
    }
    
    /**
     * 附件类型 PIC
     */
    public static final String ATTACH_TYPE_PIC = "picture";

    /**
     * 附件类型 PDF
     */
    public static final String ATTACH_TYPE_PDF = "pdf";

    /**
     * 附件类型 VIDEO
     */
    public static final String ATTACH_TYPE_VIDEO = "video";

    /**
     * 附件类型 DOC
     */
    public static final String ATTACH_TYPE_DOC = "doc";

    /**
     * 附件类型 PPT
     */
    public static final String ATTACH_TYPE_PPT = "ppt";

    /**
     * 附件类型 XLS
     */
    public static final String ATTACH_TYPE_XLS = "xls";

    /**
     * 附件类型 TXT
     */
    public static final String ATTACH_TYPE_TXT = "txt";

    /**
     * 附件类型 XLS
     */
    public static final String ATTACH_TYPE_RAR = "rar";

    /**
     * 附件类型 HTML
     */
    public static final String ATTACH_TYPE_HTML = "html";

    /**
     * 附件类型 AUDIO
     */
    public static final String ATTACH_TYPE_AUDIO = "audio";

    /**
     * 附件类型 文件
     */
    public static final String ATTACH_TYPE_FILE = "file";

    /**
     * 缓冲区大小
     */
    private static final int BUFFER_SIZE = 100 * 1024;
    
    private static final String[][] MIME_MAPTABLE = {
        //{后缀名，MIME类型} 
        { ".3gp", "video/3gpp" },
        { ".amr", "audio/amr" },
        { ".apk", "application/vnd.android.package-archive" },
        { ".asf", "video/x-ms-asf" },
        { ".avi", "video/x-msvideo" },
        { ".bin", "application/octet-stream" },
        { ".bmp", "image/bmp" },
        { ".c", "text/plain" },
        { ".class", "application/octet-stream" },
        { ".conf", "text/plain" },
        { ".cpp", "text/plain" },
        { ".doc", "application/msword" },
        { ".docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document" },
        { ".xls", "application/vnd.ms-excel" },
        { ".xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" },
        { ".exe", "application/octet-stream" },
        { ".gif", "image/gif" },
        { ".gtar", "application/x-gtar" },
        { ".gz", "application/x-gzip" },
        { ".h", "text/plain" },
        { ".htm", "text/html" },
        { ".html", "text/html" },
        { ".jar", "application/java-archive" },
        { ".java", "text/plain" },
        { ".jpeg", "image/jpeg" },
        { ".jpg", "image/jpeg" },
        { ".js", "application/x-javascript" },
        { ".log", "text/plain" },
        { ".m3u", "audio/x-mpegurl" },
        { ".m4a", "audio/mp4a-latm" },
        { ".m4b", "audio/mp4a-latm" },
        { ".m4p", "audio/mp4a-latm" },
        { ".m4u", "video/vnd.mpegurl" },
        { ".m4v", "video/x-m4v" },
        { ".mov", "video/quicktime" },
        { ".mp2", "audio/x-mpeg" },
        { ".mp3", "audio/x-mpeg" },
        { ".mp4", "video/mp4" },
        { ".mpc", "application/vnd.mpohun.certificate" },
        { ".mpe", "video/mpeg" },
        { ".mpeg", "video/mpeg" },
        { ".mpg", "video/mpeg" },
        { ".mpg4", "video/mp4" },
        { ".mpga", "audio/mpeg" },
        { ".msg", "application/vnd.ms-outlook" },
        { ".ogg", "audio/ogg" },
        { ".pdf", "application/pdf" },
        { ".png", "image/png" },
        { ".pps", "application/vnd.ms-powerpoint" },
        { ".ppt", "application/vnd.ms-powerpoint" },
        { ".pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation" },
        { ".prop", "text/plain" }, 
        { ".rc", "text/plain" },
        { ".rmvb", "audio/x-pn-realaudio" }, 
        { ".rtf", "application/rtf" },
        { ".sh", "text/plain" }, 
        { ".tar", "application/x-tar" },
        { ".tgz", "application/x-compressed" }, 
        { ".txt", "text/plain" },
        { ".wav", "audio/x-wav" }, 
        { ".wma", "audio/x-ms-wma" },
        { ".wmv", "audio/x-ms-wmv" },
        { ".wps", "application/vnd.ms-works" }, 
        { ".xml", "text/plain" },
        { ".z", "application/x-compress" },
        { ".zip", "application/x-zip-compressed" }, 
        { "", "*/*" } 
    };

    /**
     * 获得附件文件类型
     * 
     * @param pathString
     *              文件路径或名称
     * @return
     *              文件类型
     */
    public static String getAttachType(String pathString) {
        String typeString = FileUtil.getFileTypeString(pathString);
        if (typeString.toLowerCase().equals("png")
                || typeString.toLowerCase().equals("jpg")
                || typeString.toLowerCase().equals("jpeg")
                || typeString.toLowerCase().equals("bmp")
                || typeString.toLowerCase().equals("gif")) {
            return ATTACH_TYPE_PIC;
        } else if (typeString.toLowerCase().equals("pdf")) {
            return ATTACH_TYPE_PDF;
        } else if (typeString.toLowerCase().equals("vob")
                || typeString.toLowerCase().equals("avi")
                || typeString.toLowerCase().equals("rm")
                || typeString.toLowerCase().equals("rmvb")
                || typeString.toLowerCase().equals("mp4")
                || typeString.toLowerCase().equals("3gp")) {
            return ATTACH_TYPE_VIDEO;
        } else if (typeString.toLowerCase().equals("doc")
                || typeString.toLowerCase().equals("docx")) {
            return ATTACH_TYPE_DOC;
        } else if (typeString.toLowerCase().equals("ppt")
                || typeString.toLowerCase().equals("pptx")) {
            return ATTACH_TYPE_PPT;
        } else if (typeString.toLowerCase().equals("xls")
                || typeString.toLowerCase().equals("xlsx")) {
            return ATTACH_TYPE_XLS;
        } else if (typeString.toLowerCase().equals("txt")) {
            return ATTACH_TYPE_TXT;
        } else if (typeString.toLowerCase().equals("mkv") 
                || typeString.toLowerCase().equals("avi")
                || typeString.toLowerCase().equals("rm") 
                || typeString.toLowerCase().equals("rmvb")
                || typeString.toLowerCase().equals("mp4") 
                || typeString.toLowerCase().equals("flv")) {
            return ATTACH_TYPE_VIDEO;
        } else if (typeString.toLowerCase().equals("rar") 
                || typeString.toLowerCase().equals("zip")) {
            return ATTACH_TYPE_RAR;
        } else if (typeString.toLowerCase().equals("html")
                || typeString.toLowerCase().equals("mht")) {
            return ATTACH_TYPE_HTML;
        } else if (typeString.toLowerCase().equals("mp3") 
                || typeString.toLowerCase().equals("wma")
                || typeString.toLowerCase().equals("wav")
                || typeString.toLowerCase().equals("amr")) {
            return ATTACH_TYPE_AUDIO;
        } else {
            return ATTACH_TYPE_FILE;
        }
    }    

    /**
     * 获得附件文件类型
     * @param type 文件类型
     * @return 文件类型
     */
    public static String getAttachTypeImmediate(String type) {
        if (TextUtils.isEmpty(type)) {
            return ATTACH_TYPE_FILE;
        }
        if (type.toLowerCase().equals("png")
                || type.toLowerCase().equals("jpg")
                || type.toLowerCase().equals("jpeg")
                || type.toLowerCase().equals("bmp")
                || type.toLowerCase().equals("gif")) {
            return ATTACH_TYPE_PIC;
        } else if (type.toLowerCase().equals("pdf")) {
            return ATTACH_TYPE_PDF;
        } else if (type.toLowerCase().equals("vob")
                || type.toLowerCase().equals("avi")
                || type.toLowerCase().equals("rm")
                || type.toLowerCase().equals("rmvb")
                || type.toLowerCase().equals("mp4")
                || type.toLowerCase().equals("3gp")) {
            return ATTACH_TYPE_VIDEO;
        } else if (type.toLowerCase().equals("doc")
                || type.toLowerCase().equals("docx")) {
            return ATTACH_TYPE_DOC;
        } else if (type.toLowerCase().equals("ppt")
                || type.toLowerCase().equals("pptx")) {
            return ATTACH_TYPE_PPT;
        } else if (type.toLowerCase().equals("xls")
                || type.toLowerCase().equals("xlsx")) {
            return ATTACH_TYPE_XLS;
        } else if (type.toLowerCase().equals("txt")) {
            return ATTACH_TYPE_TXT;
        } else if (type.toLowerCase().equals("mkv")
                || type.toLowerCase().equals("avi")
                || type.toLowerCase().equals("rm")
                || type.toLowerCase().equals("rmvb")
                || type.toLowerCase().equals("mp4")
                || type.toLowerCase().equals("flv")) {
            return ATTACH_TYPE_VIDEO;
        } else if (type.toLowerCase().equals("rar")
                || type.toLowerCase().equals("zip")) {
            return ATTACH_TYPE_RAR;
        } else if (type.toLowerCase().equals("html")
                || type.toLowerCase().equals("mht")) {
            return ATTACH_TYPE_HTML;
        } else if (type.toLowerCase().equals("mp3")
                || type.toLowerCase().equals("wma")
                || type.toLowerCase().equals("wav")
                || type.toLowerCase().equals("amr")) {
            return ATTACH_TYPE_AUDIO;
        } else {
            return ATTACH_TYPE_FILE;
        }
    }

    /**
     * 获得文件的后缀名
     * 
     * @param fileName
     *            文件名称
     * @return
     *         文件不带点的后缀名,没有后缀名返回""
     */
    public static String getFileTypeString(String fileName) {
        if (TextUtils.isEmpty(fileName)) {
            return "";
        }
        int index = fileName.lastIndexOf(".");
        if (index != -1) {
            String type = fileName.substring(index + 1).toLowerCase();
            return type;
        } else {
            return "";
        }
    }

    /**
     * 获取文件名
     * @param path
     * @return
     */
    public static String getFileName(String path) {
        if (TextUtils.isEmpty(path)) {
            return "";
        }
        int index = path.lastIndexOf("/");
        if (index != -1) {
            return path.substring(index + 1);
        }
        return "";
    }

    /**
     * 获取文件类型<BR>
     * 用于获取文件类型
     * 
     * @param fileName 文件名
     * @return 返回文件类型
     */
	public static String getMIMEType(String fileName) {

        String type = "*/*";
        //获取后缀名前的分隔符"."在fName中的位置。
        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex < 0) {
            return type;
        }
        /* 获取文件的后缀名*/
        String end = fileName.substring(dotIndex, fileName.length()).toLowerCase();
        if (end == "") {
            return type;
        }
        //在MIME和文件类型的匹配表中找到对应的MIME类型。 
        for (int i = 0; i < FileUtil.MIME_MAPTABLE.length; i++) { //MIME_MapTable??在这里你一定有疑问，这个MIME_MapTable是什么？ 
            if (end.equals(FileUtil.MIME_MAPTABLE[i][0])) {
                type = MIME_MAPTABLE[i][1];
                break;
            }
        }
        return type;
    }

    /**
     * 获取文件类型<BR>
     * 用于获取文件类型
     *
     * @param fileType 文件类型
     * @return 返回文件类型
     */
	public static String getMIMEType2(String fileType) {

        String type = "*/*";
        if (TextUtils.isEmpty(fileType)) {
            return type;
        } else if (!fileType.contains(".")) {
            type = "." + fileType;
        }
        for (int i = 0; i < FileUtil.MIME_MAPTABLE.length; i++) {
            if (type.equals(FileUtil.MIME_MAPTABLE[i][0])) {
                type = MIME_MAPTABLE[i][1];
                break;
            }
        }
        return type;
    }

    /**
     * 根据文件MimeType获取对应后缀字符串
     * @param mimeType
     * @return
     */
    public static String getSuffixByMimeType(String mimeType) {
        for (int i = 0; i < FileUtil.MIME_MAPTABLE.length; i++) {  
            if (mimeType.equals(FileUtil.MIME_MAPTABLE[i][1])) {
                return MIME_MAPTABLE[i][0];
            }
        }
        return "";
    }
    
    /**
     * MimeType对应的中文名
     * @param mimeType
     * @return
     */
    public static String getMimeTypeInChinese(String mimeType) {
    	String suffix = getSuffixByMimeType(mimeType);
    	
    	return getAttachType(suffix);
    }
    
    /** 
     * 复制文件
     * @param fileIn 要被copy的文件 
     * @param fileOutPut 将文件copy到那个目录下面 
     * @throws IOException 
     * @throws Exception 
     */  
    public static void copyFile(File fileIn,File fileOutPut) throws IOException  
    {  
        FileInputStream fileInputStream=new FileInputStream(fileIn);  
        FileOutputStream fileOutputStream=new FileOutputStream(fileOutPut);  
        byte[] by=new byte[1024];  
        int len;  
        while((len=fileInputStream.read(by))!=-1)  
        {  
            fileOutputStream.write(by, 0, len);  
        }  
        fileInputStream.close();  
        fileOutputStream.close();  
    }  
    
	public static void writeInfoToFile(final String message,final String name){
		final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		ExecutorSupport.getExecutor().execute(new Runnable() {
			@Override
			public void run() {
				String path = EnvUtil.getSDCardPath()+File.separator+"push";
				File mFile = new File(path);
				try {
					if(!mFile.exists()){
						mFile.mkdirs();
					}
					mFile = new File(path+File.separator+name+".txt");
					if(!mFile.exists()){
						mFile.createNewFile();
					}
					BufferedWriter writeBff = new BufferedWriter(new FileWriter(mFile,true));
					writeBff.append(message+"===="+format.format(new Date())+"\n");
					writeBff.flush();
					writeBff.close();
				} catch (Exception e) {
					// TODO: handle exception
				}
				
			}
		});
	} 
}
