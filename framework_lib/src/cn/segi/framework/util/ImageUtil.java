/*
 * 文件名：ImageUtil.java
 * 创建人：Administrator
 * 创建时间：2012-12-12
 * 版     权：Copyright Easier Digital Tech. Co. Ltd. All Rights Reserved.
 */
package cn.segi.framework.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.text.TextUtils;
import cn.segi.framework.application.BaseApplication;
import cn.segi.framework.consts.FrameworkConst;
import cn.segi.framework.log.Logger;

/**
 * Image通用操作封装工具类
 * 
 * @author liangzx
 * @version [segi, 2012-12-11]
 */
public class ImageUtil {
	/**
	 * ImageUtil TAG
	 */
	private static final String TAG = "ImageUtil";

	/**
	 * 将图片转化给byte[]操作
	 * 
	 * @param bm
	 *            图片对象
	 * @return 图片byte[]
	 */
	public static byte[] bitmap2Bytes(Bitmap bm) {
		byte[] bytes = null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		bytes = baos.toByteArray();
		try {
			baos.close();
		} catch (IOException e) {
			Logger.d(TAG, e.getMessage());
		}

		return bytes;
	}

	/**
	 * 将byte[]转化成图片<BR>
	 * 此方法不提供对图片的压缩
	 * 
	 * @param bytes
	 *            图片的byte[]
	 * @return 图片对象
	 */
	public static Bitmap bytes2Bimap(byte[] bytes) {
		if (bytes != null && bytes.length != 0) {
			return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
		} else {
			return null;
		}
	}

	/**
	 * 获取缩略后的Bitmap<BR>
	 * 此方法以目标高宽与原高宽的最大缩放比作缩略
	 * 
	 * @param bytes
	 *            图片byte数据
	 * @param newWidth
	 *            缩放后的Bitmap期待宽度
	 * @param newHeight
	 *            缩放后的Bitmap期待高度
	 * @return 返回缩略图
	 */
	public static Bitmap decodeThumbnailBitmap(byte[] bytes, int newWidth,
			int newHeight) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inJustDecodeBounds = true;
		BitmapFactory.decodeByteArray(bytes, 0, bytes.length, opt);
		opt.inJustDecodeBounds = false;
		opt.inSampleSize = computeSampleSize(opt.outWidth, opt.outHeight,
				newWidth, newHeight);

		return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, opt);
	}

	/**
	 * 裁剪图片<BR>
	 * 此方法return-data设置为true，返回的data是Bitmap，所以适合裁小图，大图需再构造其他方法
	 * 
	 * @param context
	 *            上下文页面
	 * @param uri
	 *            需要裁剪的图片Uri
	 * @param requestCode
	 *            请求码
	 * @param outputX
	 *            裁剪区的宽
	 * @param outputY
	 *            裁剪区的高
	 * @param returnData
	 *            是否返回Bitmap
	 * @return 是否跳转成功
	 * @throws ActivityNotFoundException
	 *             如果系统没有裁剪功能，会抛出异常
	 */
	public static void startPhotoCrop(Activity context, Uri uri,
			int requestCode, int outputX, int outputY)
			throws ActivityNotFoundException {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// 是否发送裁剪信号
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", outputX);
		intent.putExtra("outputY", outputY);
		intent.putExtra("return-data", true);
		context.startActivityForResult(intent, requestCode);
	}

	/**
	 * 计算缩放比例<BR>
	 * 返回目标高宽与原高宽的最大缩放比
	 * 
	 * @param oldWidth
	 *            Bitmap原宽度
	 * @param oldHeight
	 *            Bitmap原高度
	 * @param newWidth
	 *            缩放后的Bitmap期待宽度
	 * @param newHeight
	 *            缩放后的Bitmap期待高度
	 * @return 返回最大缩放比
	 */
	public static int computeSampleSize(int oldWidth, int oldHeight,
			int newWidth, int newHeight) {
		int len = 0;
		if (oldWidth > oldHeight) {
			len = oldWidth;
			oldWidth = oldHeight;
			oldHeight = len;
		}
		int hSampleSize = 1;
		int wSampleSize = 1;
		if (oldHeight > newHeight) {
			hSampleSize = (int) Math.ceil((float) oldHeight / newHeight);
		}

		if (oldWidth > newWidth) {
			wSampleSize = (int) Math.ceil((float) oldWidth / newWidth);
		}
		int sampleSize = hSampleSize > wSampleSize ? hSampleSize : wSampleSize;
		if (sampleSize % 2 == 1) {
			++sampleSize;
		}
		return sampleSize;
	}

	/**
	 * 从Resource中获取Bitmap
	 * 
	 * @param context
	 *            上下文
	 * @param width
	 *            Bitmap宽
	 * @param height
	 *            Bitmap高
	 * @param resId
	 *            Resource ID
	 * @return Bitmap资源
	 */
	public static Bitmap createBitmapFromRes(Context context, int width,
			int height, int resId) {
		Drawable drawable = context.getResources().getDrawable(resId);
		drawable.setBounds(0, 0, width, height);
		Bitmap bitmap = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		drawable.draw(canvas);
		return bitmap;
	}

	/**
	 * 将图片变成圆角
	 * 
	 * @param bitmap
	 *            要处理的图片
	 * @return 转换完毕的图片
	 */
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
		if (null != bitmap) {
			Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
					bitmap.getHeight(), Config.ARGB_8888);
			Canvas canvas = new Canvas(output);
			
			final int color = 0xff424242;
			final Paint paint = new Paint();
			final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
			final RectF rectF = new RectF(rect);
			final float roundPx = 8;
			
			paint.setAntiAlias(true);
			canvas.drawARGB(0, 0, 0, 0);
			paint.setColor(color);
			canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
			
			paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
			canvas.drawBitmap(bitmap, rect, rect, paint);
			return output;
		}
		return null;
	}

	/**
	 * 从assets目录中读取图片
	 * 
	 * @param context
	 *            上下文
	 * @param fileName
	 *            图片文件名
	 * @return 返回
	 */
	public static Bitmap getImageFromAssetFile(Context context, String fileName) {
		Bitmap image = null;
		try {
			AssetManager am = context.getAssets();
			InputStream is = am.open(fileName);
			image = BitmapFactory.decodeStream(is);
			is.close();
		} catch (Exception e) {
			Logger.d(TAG, e.getMessage());
		}
		return image;
	}

	/**
	 * 读取图片文件的旋转角度
	 * 
	 * @param path
	 *            文件路径
	 * @return 返回旋转角度
	 */
	public static int readPictureDegree(String path) {
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			default:
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return degree;
	}

	/**
	 * 矫正旋转角度
	 * 
	 * @param degree
	 *            需要矫正的角度
	 * @param bitmap
	 *            图片Bitmap
	 * @return 矫正后的Bitmap
	 */
	public static Bitmap rotaintBitmap(Bitmap bitmap, int degree) {
		if (null != bitmap) {
			Matrix matrix = new Matrix();
			matrix.postRotate(degree);
			Bitmap bit = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
					bitmap.getHeight(), matrix, true);
			return bit;
		}
		return null;
	}
	

	/**
	 * 图片压缩方法
	 * @param srcPath
	 * @return
	 */
	public static String compressImage(String srcPath) {
		String newPath = "";
		if (TextUtils.isEmpty(srcPath)) {
			return "";
		}
		File imageFile = new File(srcPath);
		if (imageFile.exists() && imageFile.isFile()) {
			String suffixName = ".png";
			int pointIndex = srcPath.lastIndexOf(".");
			if (-1 != pointIndex) {
				suffixName = srcPath.substring(pointIndex, srcPath.length());
			}
			String fileName = System.currentTimeMillis() + suffixName;
			newPath = FrameworkConst.LOCAL_COMPRESS_CAMERA_PATH+File.separator+fileName;
			try {
				if (imageFile.length() / 1024 > 150) {
					int degree = readPictureDegree(srcPath);
					BitmapFactory.Options newOpts = new BitmapFactory.Options();
					newOpts.inJustDecodeBounds = true;
					Bitmap bitmap = BitmapFactory.decodeFile(srcPath,newOpts);//此时返回bm为空
					newOpts.inSampleSize = calculateInSampleSize(newOpts, 720, 1280);
					newOpts.inJustDecodeBounds = false;
					newOpts.inPurgeable = true;
					newOpts.inInputShareable = true;
					bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
					if (newPath.equals(srcPath)) {
						imageFile.delete();
					}
					if (degree != 0) {
						Bitmap tmpBm = rotaintBitmap(bitmap, degree);
						if (null != tmpBm) {
							bitmap = tmpBm;
						}
					}
					compressImageTheQuality(newPath, bitmap);
				}else{
					if (!newPath.equals(srcPath)) {
						FileUtil.copyFile(imageFile, new File(newPath));
					}
					return newPath;
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return newPath;
    }

	/**
	 * 图片压缩方法
	 * @param newPath
	 * @param bitmap
	 * @return
	 */
	public static String createFileByBitmap(String newPath, Bitmap bitmap) {
        if (TextUtils.isEmpty(newPath)) {
            return "";
        }
        if (null == bitmap) {
            return "";
        }
        FileOutputStream outputStream = null;
        File file = new File(newPath);
        if (file.exists()) {
            file.delete();
        }
        try {
            outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != outputStream) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return newPath;
        }
	}
	
	/**
	 * 压缩质量
	 * @param newPath
	 * @param bitmap
	 * @throws IOException 
	 */
	private static void compressImageTheQuality(String newPath, Bitmap bitmap) throws IOException
	{
		FileOutputStream os = new FileOutputStream(newPath);
		if(null != bitmap){
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os); 
			os.flush();
			os.close();
			File imageFile = new File(newPath);
			long image_kb = imageFile.length() / 1024;
			int sampleSize = 100;
			if (image_kb > 200) {
				sampleSize = (int) (200 * 100 / image_kb);
			}else if (image_kb > 150) {
				sampleSize = 90;
			}
			if (sampleSize != 100) {
				os = new FileOutputStream(newPath);
				bitmap.compress(Bitmap.CompressFormat.JPEG, sampleSize, os); 
				bitmap.recycle();
				bitmap=null;
				os.flush();
				os.close();
			}
		}
	}
	
	
	
	/**
	* 计算图片压缩值
	* @param options
	* @param reqWidth
	* @param reqHeight
	* @return
	*/
	public static int calculateInSampleSize(BitmapFactory.Options options,
	            int reqWidth, int reqHeight) {
		int len = 0;
		int oldHeight = options.outHeight;
		int oldWidth = options.outWidth;
		if (oldWidth > oldHeight) {
			len = oldWidth;
			oldWidth = oldHeight;
			oldHeight = len;
		}
		int inSampleSize = 1;
		if (oldWidth > reqWidth || oldHeight > reqHeight) {
		    while ((oldHeight / inSampleSize) > reqHeight
		            && (oldWidth / inSampleSize) > reqWidth) {
		        inSampleSize *= 2;
		    }
		}
		return inSampleSize;
	}
	
	
	
	/**
	 * 添加水印图片
	 * @param waterRemarkPath
	 */
	@SuppressLint("SimpleDateFormat")
	public static void addWaterRemark(String oldPath, String waterRemarkPath, int textSize, int textColor, int bgColor)
	{
		if (TextUtils.isEmpty(oldPath)) {
			return;
		}
		if (TextUtils.isEmpty(waterRemarkPath)) {
			return;
		}
		Bitmap oldBitmap = BitmapFactory.decodeFile(oldPath);
		if (null == oldBitmap) {
			return;
		}
		Bitmap waterBitmap = getBitmapFormAssets(waterRemarkPath);
		if (null == waterBitmap) {
			return;
		}
		Bitmap newBitmap = null;
		
		Paint strPaint = new Paint();
		strPaint.setAntiAlias(true);
		strPaint.setColor(textColor);
		strPaint.setTextSize(textSize);
		
		Paint bgPaint = new Paint();
		bgPaint.setColor(bgColor);
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String dateTime = format.format(new Date());
		Rect textRect = new Rect();
		strPaint.getTextBounds(dateTime, 0, dateTime.length(), textRect);
		
		int old_w = oldBitmap.getWidth();
		int old_h = oldBitmap.getHeight();
		int water_w = waterBitmap.getWidth();
		int water_h = waterBitmap.getHeight();
		int txt_w = Math.abs(textRect.right - textRect.left);
		int txt_h = Math.abs(textRect.top - textRect.bottom);
		int spacing = 5;
		
		int all_w = water_w + txt_w + 2 * spacing;
		int all_h = water_h + spacing;
		
		float scaleTxt = 1;
		if (all_w > old_w) {
			scaleTxt = (float)old_w/ (water_w + txt_w + 2 * spacing) / 3;
			strPaint.setTextSize(textSize * scaleTxt);
			strPaint.getTextBounds(dateTime, 0, dateTime.length(), textRect);
			txt_w = Math.abs(textRect.right - textRect.left);
			txt_h = Math.abs(textRect.top - textRect.bottom);
            int tmp_w = (int) (water_w * scaleTxt);
            int tmp_h = (int) (water_h * scaleTxt);
			water_w = tmp_w > 0 ? tmp_w : water_w;
			water_h = tmp_h > 0 ? tmp_h : water_h;
			waterBitmap = Bitmap.createScaledBitmap(waterBitmap, water_w, water_h, true);
			spacing = (int) (spacing * scaleTxt);
			all_w = water_w + txt_w + 2 * spacing;
			all_h = water_h + spacing;
		}
		
		if (all_w <= old_w && all_h <= old_h && txt_h <= old_h) {
			newBitmap = Bitmap.createBitmap(oldBitmap.getWidth(), oldBitmap.getHeight(), Config.ARGB_8888);
			Canvas cv = new Canvas(newBitmap);
			cv.drawBitmap(oldBitmap, 0, 0, null);
			cv.drawBitmap(waterBitmap, old_w - all_w, old_h - all_h, null);
			cv.drawRect(old_w - txt_w - spacing, old_h - txt_h - spacing, old_w - spacing, old_h - spacing, bgPaint);   
			cv.drawText(dateTime, old_w - txt_w - spacing, old_h - spacing, strPaint);
			cv.save(Canvas.ALL_SAVE_FLAG);
			cv.restore();
			//保存到文件
			try {
				compressImageTheQuality(oldPath, newBitmap);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				if(null != newBitmap){
					newBitmap.recycle();
					newBitmap = null;
				}
			}
		}
		if (null != oldBitmap) {
			oldBitmap.recycle();
		}
		if (null != waterBitmap) {
			waterBitmap.recycle();
		}
	}
	
	
	/**
	 * 从assets中读取图片
	 * @param fileName
	 * @return
	 */
	public static Bitmap getBitmapFormAssets(String fileName)
	{
		Bitmap assetsBitmap = null;
		AssetManager am = BaseApplication.getContext().getAssets();
		try {
			InputStream is = am.open(fileName);
			assetsBitmap = BitmapFactory.decodeStream(is);
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return assetsBitmap;
	}
	

	/**
	 * 根据图片实际大小判断图片是否进行等比缩放 将图片压缩到一M内
	 * @param imageFile
	 * @param sourcePath
	 * @param 	 * @throws Exception
	 */
	@SuppressLint("NewApi")
	public static void compressImageBySize(String sourcePath) {

        try{
        	long fileSize = 1024 * 1024;
        	double rate = 0.0;
        	BitmapFactory.Options newOpts = new BitmapFactory.Options();
	        newOpts.inJustDecodeBounds = true;
	        Bitmap mBitmap = BitmapFactory.decodeFile(sourcePath);
	        int oldWidth =  newOpts.outWidth;
	        int oldHeight =  newOpts.outHeight;
            int newWidth = oldWidth;
            int newHeight = oldHeight;
            if (newWidth >= newHeight) {
				rate = (double) newHeight / newWidth;
			} else {
				rate = (double) newWidth / newHeight;
			}
            long newSize = mBitmap.getByteCount();
            mBitmap.recycle();
            mBitmap = null;
            while(newSize>fileSize){//如果图片大小大于或者等于1M 进行缩放
                newWidth = (int) (newWidth * rate);
				newHeight = (int) (newHeight * rate);
				mBitmap = Bitmap.createBitmap(newWidth, newHeight, Config.ARGB_8888);
				newSize = mBitmap.getByteCount();
				mBitmap.recycle();
				mBitmap = null;
            }
            newOpts.outWidth = newWidth;
            newOpts.outHeight = newHeight;
            newOpts.inJustDecodeBounds = false;
            newOpts.inSampleSize = ImageUtil.computeSampleSize(oldWidth, oldHeight,newWidth, newHeight);;//设置缩放比例
            mBitmap = BitmapFactory.decodeFile(sourcePath, newOpts);
            FileOutputStream os = new FileOutputStream(sourcePath);
			mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
			if(null!=mBitmap){
				mBitmap.recycle();
				mBitmap=null;
			}
        }catch(Exception ex){
            ex.printStackTrace();
        }
   
	}
	
	/**
	 * 根据图片路径压缩图片
	 * @param srcPath
	 * 通过图片大小压缩
	 */
	@SuppressLint("NewApi")
	public static String compressImage4yz(String srcPath) {
		int SAMP_WIDTH = 0;
		int SAMP_HEIGHT= 0;
		int COMPRESS_PIX = 1280;
		String newPath = "";
		float factor = 0.0f;
		try {
			BitmapFactory.Options newOpts = new BitmapFactory.Options();
			newOpts.inJustDecodeBounds = true;
	        Bitmap bitmap = BitmapFactory.decodeFile(srcPath,newOpts);//此时返回bm为空
	        if(newOpts.outWidth<=COMPRESS_PIX && newOpts.outHeight<=COMPRESS_PIX){//都小于1280 则不进行压缩
	        	
	        }else{
	        	if(newOpts.outWidth>=newOpts.outHeight){
	        		factor = (float)COMPRESS_PIX/newOpts.outWidth;
	        	}else{
	        	    factor = (float)COMPRESS_PIX/newOpts.outHeight;
	        	}
	        	SAMP_WIDTH = (int) (newOpts.outWidth * factor);
        		SAMP_HEIGHT = (int) (newOpts.outHeight * factor);
        		int sampleSize = computeSampleSize(newOpts.outWidth, newOpts.outHeight, SAMP_WIDTH, SAMP_HEIGHT);
		        if(sampleSize!=1){
		        	sampleSize = (sampleSize%2==0)? sampleSize : sampleSize+1;
		        }
		        newOpts.inSampleSize = sampleSize;
	        }
	        newOpts.inJustDecodeBounds = false;
	        bitmap = BitmapFactory.decodeFile(srcPath,newOpts);//此时返回bm为空
	        String suffixName = srcPath.substring(srcPath.lastIndexOf("."), srcPath.length());
			String fileName = System.currentTimeMillis() + suffixName;
	        newPath = FrameworkConst.LOCAL_COMPRESS_CAMERA_PATH+File.separator+fileName;
	        FileOutputStream os = new FileOutputStream(newPath);
			if(null!=bitmap){
				bitmap.compress(Bitmap.CompressFormat.JPEG, 60, os);  
				bitmap.recycle();
				bitmap=null;
			}
			os.flush();
			os.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
		}catch(OutOfMemoryError ooe){
			
		}catch (Exception e) {
			// TODO Auto-generated catch block
		}  
		return newPath;
    }
	
	public static void batchCompressImage(List<String> pathList) {
		if (null == pathList) {
			return;
		}
		for (String path : pathList) {
			if (!StringUtils.isNullOrEmpty(path)) {
				path = compressImage4yz(path);
			}
		}
	}

	/**
	 * 不加载图片前提下获取图片宽高
	 * @param resources
	 * @param resId
	 * @return
	 */
	public static int[] getImageWidthHeight(Resources resources, int resId){
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(resources, resId, options); // 此时返回的bitmap为null
		return new int[]{options.outWidth,options.outHeight};
	}

	/**
	 * 不加载图片前提下获取图片宽高
	 * @param path
	 * @return
	 */
	public static int[] getImageWidthHeight(String path){
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options); // 此时返回的bitmap为null
		return new int[]{options.outWidth,options.outHeight};
	}
}
