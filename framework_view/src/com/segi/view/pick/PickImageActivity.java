/*
 * 文件名：PickImageActivity.java
 * 创建人：王玉丰
 * 创建时间：2013-8-16
 * 版     权：Copyright Easier Digital Tech. Co. Ltd. All Rights Reserved.
 */
package com.segi.view.pick;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.text.TextUtils;
import cn.segi.framework.activity.BaseFrameworkActivity;
import cn.segi.framework.executor.ExecutorSupport;
import cn.segi.framework.log.Logger;
import cn.segi.framework.util.FrameworkUtil;
import cn.segi.framework.util.ImageUtil;

/**
 * 选择图片（从相册或拍照）功能
 * 
 * @author 王玉丰
 * @version [Transfer, 2013-8-17]
 */
public class PickImageActivity extends BaseFrameworkActivity {
    /**
     * PickImageActivity TAG
     */
    private static final String TAG = "PickImageActivity";
    
    /**
     * 跳转到拍照界面的action
     */
    private static final String IMAGE_CAPTURE_ACTION = "android.media.action.IMAGE_CAPTURE";

    /**
     * 存放到bundle中的key
     */
    private static final String KEY_IMAGE_FILE = "imageFile";

    /**
     * 存放到bundle中的key
     */
    private static final String KEY_PHOTO_BYTES = "photoBytes";

    /**
     * 图片来源
     */
    private int mType;
    
    /**
     * 是否需要裁剪图片
     */
    private int outputX;
    private int outputY;

    /**
     * 用于保存获得的图片
     */
    private File mImageFile;
    private Uri mUri;

    /**
     * 头像数据
     */
    private byte[] mPhotoBytes;
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (null == savedInstanceState) {
            dispatch(getIntent().getExtras());
        } else {
            mImageFile = (File) savedInstanceState.getSerializable(KEY_IMAGE_FILE);
            mPhotoBytes = savedInstanceState.getByteArray(KEY_PHOTO_BYTES);
        }
    }

    /**
     * 根据不同的模式跳转到不同的页面
     * 
     * @param bundle 从前一页面传递过来的参数
     */
    private void dispatch(Bundle bundle) {
        // 图片来源
        mType = bundle.getInt(PickImageAction.EXTRA_MODE);
        outputX = bundle.getInt(PickImageAction.EXTRA_OUTPUT_X, 300);
        outputY = bundle.getInt(PickImageAction.EXTRA_OUTPUT_Y, 300);

        Intent intent = new Intent();
        switch (mType) {
            case PickImageAction.MODE_ALBUM:
                intent.setAction(Intent.ACTION_PICK);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, mType);
                break;
            case PickImageAction.MODE_CAMERA:
					if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
						// 给要拍摄的照片命名
						String path = FrameworkUtil.getCompressImagePath() + File.separator + System.currentTimeMillis() + ".jpg";
						if (TextUtils.isEmpty(path)) {
							show("SD卡剩余空间不足…");
							finishActivity();
						} else {
							mImageFile = new File(path);
							//跳转到拍照界面
							intent.setAction(IMAGE_CAPTURE_ACTION);
							intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
							intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mImageFile));
							startActivityForResult(intent, mType);
						}
					} else {
						//给出提示:请插入SD卡,再进行操作
						show("请插入SD卡并重试");
						finishActivity();
					}
                break;
            case PickImageAction.CROP_IMG:
            	if (bundle.containsKey(PickImageAction.EXTRA_IMAGE_PATH) && null != bundle.getString(PickImageAction.EXTRA_IMAGE_PATH)) {
            		File file = new File(bundle.getString(PickImageAction.EXTRA_IMAGE_PATH));
            		if (file.exists()) {
            			mUri = Uri.fromFile(new File(FrameworkUtil.getCompressImagePath() + File.separator + System.currentTimeMillis() + ".jpg"));
            			Uri openUri = Uri.fromFile(file);
            			Intent cropIntent = new Intent();  
            			cropIntent.setAction("com.android.camera.action.CROP");  
            			cropIntent.setDataAndType(openUri, "image/*");// mUri是已经选择的图片Uri  
            			cropIntent.putExtra("crop", "true");  
            			cropIntent.putExtra("aspectX", 1);// 裁剪框比例  
            			cropIntent.putExtra("aspectY", 1);  
            			cropIntent.putExtra("outputX", outputX);// 输出图片大小  
            			cropIntent.putExtra("outputY", outputY);  
            			cropIntent.putExtra("scale", true);
            			cropIntent.putExtra("return-data", false); 
            			cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, mUri);
            			cropIntent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
            			cropIntent.putExtra("noFaceDetection", true);
            			startActivityForResult(cropIntent, mType);
            		}else{
            			finishActivity();
            		}
				}else{
        			finishActivity();
        		}
            	break;
            default:
                finishActivity();
                break;
        }
    }

    /**
     * Activity回调处理
     * 
     * @param requestCode 请求Code
     * @param resultCode 响应Code
     * @param data 返回数据
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {        
        Logger.d(TAG, "onActivityResult requestCode: " + requestCode + " resultCode:" + resultCode);
        if (RESULT_OK == resultCode) {
            switch (requestCode) {
                case PickImageAction.MODE_ALBUM: {
                    Uri selectedContentUri = data.getData();
                    // 解码前的文件路径
                    String selectedContentPath = null;
                    if (selectedContentUri.toString().startsWith("content")) {
                        selectedContentPath = getPath(selectedContentUri);
                    } else {
                        selectedContentPath = selectedContentUri.toString()
                                .substring(selectedContentUri.toString().indexOf(":") + 3);
                    }
                    // 解码后的文件路径
                    String decodedPath = null;
                    try {
                        // 暂时使用UTF-8解码方式, 可能会有机型适配问题
                        decodedPath = URLDecoder.decode(selectedContentPath,
                            "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        // 不推荐的解码方式
                        decodedPath = URLDecoder.decode(selectedContentPath);
                    }
                    // 恢复解码之前的"+"号
                    if (decodedPath.contains("%2B")) {
                        decodedPath = decodedPath.replace("%2B", "+");
                    }
                    if (!TextUtils.isEmpty(decodedPath)) {
                    	compressImage(decodedPath);
					}else{
						finish();
					}
                    break;
                }
                case PickImageAction.MODE_CAMERA: {
                    // 机型适配(不同手机返回的地址不一样)
                	String imagePath;
                    if (null != data && null != data.getData()) {
                    	imagePath = getPath(data.getData());
                    	compressImage(imagePath);
                    } else if (null != mImageFile){
                    	imagePath = mImageFile.getAbsolutePath();
                    	compressImage(imagePath);
                    } else{
						finish();
					}
                    break;
                }
                case PickImageAction.CROP_IMG:
                    //从裁剪页面返回不为空，直接把数据往回传
                	Intent resultIntent = new Intent();
					resultIntent.putExtra(PickImageAction.EXTRA_IMAGE_PATH, mUri.getPath());
					setResult(RESULT_OK, resultIntent);
					finish();
                    break;
                default:
                    break;
            }
        } else {
            finishActivity();
        }
        
        super.onActivityResult(requestCode, resultCode, data);
    }
    
 
    
    /**
     * 压缩图片
     * @param imagePath
     */
    private void compressImage(final String imagePath)
    {
    	ExecutorSupport.getExecutor().execute(new Runnable() {
    		@Override
    		public void run() {
    			if (!TextUtils.isEmpty(imagePath)) {
    				String newPath = ImageUtil.compressImage(imagePath);
					Intent resultIntent = new Intent();
					resultIntent.putExtra(PickImageAction.EXTRA_IMAGE_PATH, newPath);
					setResult(RESULT_OK, resultIntent);
					finish();
				}
    		}
    	});
    }

    /**
     * 通过指定文件的URI得到文件所在的路径
     * 
     * @param uri 指定文件的URI
     * @return 文件所在的路径
     */
    private String getPath(Uri uri) {
        String[] projection = {MediaColumns.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        String path = "";
        if (null != cursor) {
            int index = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
            cursor.moveToFirst();
            path = cursor.getString(index);
            if (Integer.parseInt(Build.VERSION.SDK) < 14) {
                cursor.close();
            }
        }
        
        return path;
    }

    /**
     * 操作Back键
     */
    @Override
    public void onBackPressed() {
        finishActivity();
    }

    /**
     * 操作不成功时的回调
     */
    private void finishActivity() {
        Intent intent = new Intent();
        setResult(RESULT_CANCELED, intent);
        finish();
    }

    /**
     * 某些型号的手机【如：MOTOME863】拍照完成返回的时候会重新启动Activity
     * 
     * @param outState outState
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(KEY_IMAGE_FILE, mImageFile);
        outState.putByteArray(KEY_PHOTO_BYTES, mPhotoBytes);
        super.onSaveInstanceState(outState);
    }

    /**
     * 设置图片相关参数定义<BR>
     * 
     * @author 王玉丰
     * @version [Transfer, 2013-8-17]
     */
    public interface PickImageAction {
    	/**
         * 跳转到设置图片页面的Action
         */
        String ACTION = "com.segi.view.SETTING_IMG";

        /**
         * 选择图片的方式
         */
        String EXTRA_MODE = "PICK_IMAGE_MODE";

        /**
         * 返回图片的路径
         */
        String EXTRA_IMAGE_PATH = "PICK_IMAGE_PATH";
        
        /**
         * 裁剪输出X坐标
         */
        String EXTRA_OUTPUT_X = "OUTPUT_X";
        /**
         * 裁剪输出Y坐标
         */
        String EXTRA_OUTPUT_Y = "OUTPUT_Y";
        
        /**
         * 本地相册
         */
        int MODE_ALBUM = 1001;

        /**
         * 拍照
         */
        int MODE_CAMERA = 1002;
        
        /**
         * 截图
         */
        int CROP_IMG = 1003;

    }

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void initEvents() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void initViews() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleFragmentMessage(String arg0, int arg1, Object arg2) {
		// TODO Auto-generated method stub
		
	}
    
    /**
     * 判断是否有拍照权限
     * @return
     */
//    private boolean isCameraCanUse() {  
//    	boolean canUse = true;  
//    	Camera mCamera = null;  
//    	try {  
//    		// TODO camera驱动挂掉,处理??  
//    		mCamera = Camera.open();  
//    	} catch (Exception e) {  
//    		canUse = false;  
//    	}  
//    	if (canUse) {  
//    		mCamera.release();  
//    		mCamera = null;  
//    	}
//    	return canUse;
//    }  
}
