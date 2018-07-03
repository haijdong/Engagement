/*
 * 文件名：PickImageActivity.java
 * 创建人：liangzx
 * 创建时间：2015-12-15
 */
package com.tommy.base.common.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.text.TextUtils;

import com.tommy.base.R;
import com.tommy.base.base.BaseActivity;

import java.io.File;

import cn.segi.framework.executor.ExecutorSupport;
import cn.segi.framework.log.Logger;
import cn.segi.framework.util.FrameworkUtil;
import cn.segi.framework.util.ImageUtil;

/**
 * 拍照功能
 * 
 * @author liangzx
 * @version 1.0
 */
public class CameraActivity extends BaseActivity {
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
	 * 图片路径
	 */
	public static final String EXTRA_IMAGE_PATH = "IMAGE_PATH";
	/**
	 * 请求码
	 */
	public static final String KEY_REQUEST_CODE = "requestCode";
	/**
	 * 拍照code
	 */
	public static final int CAMERA_CODE = 0x9999;
	/**
	 * 添加水印
	 */
	public static final String KEY_ADD_WATER_MARK = "add_water_mark";
	/**
	 * 是否能编辑
	 */
	public static final String KEY_IS_EDITABLE = "is_editable";

	/**
	 * 用于保存获得的图片
	 */
	private File mImageFile;
	/**
	 * 是否添加水印
	 */
	private boolean isAddWaterMark = true;
	/**
	 * 是否能编辑
	 */
	private boolean isEditAble = true;

	/**
	 * 请求码
	 */
	private int mRequestCode;

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle bundle = getIntent().getExtras();
		if (null != bundle) {
			mRequestCode = bundle.getInt(KEY_REQUEST_CODE);
			if (bundle.containsKey(KEY_ADD_WATER_MARK)) {
				isAddWaterMark = bundle.getBoolean(KEY_ADD_WATER_MARK);
			}
			if (bundle.containsKey(KEY_IS_EDITABLE)) {
				isEditAble = bundle.getBoolean(KEY_IS_EDITABLE, true);
			}
		} else {
			mRequestCode = CAMERA_CODE;
		}
		if (null == savedInstanceState) {
            goIntoSystemCamera(getIntent().getExtras());
		} else {
			mImageFile = (File) savedInstanceState.getSerializable(KEY_IMAGE_FILE);
			isAddWaterMark = savedInstanceState.getBoolean(KEY_ADD_WATER_MARK);
			isEditAble = savedInstanceState.getBoolean(KEY_IS_EDITABLE, true);
		}
	}

	@Override
	protected void initViews() {
	}

	@Override
	protected void initEvents() {
	}

	@Override
	protected void initData() {
	}

	/**
	 * 跳转到相机
	 * 
	 * @param bundle 从前一页面传递过来的参数
	 */
	private void goIntoSystemCamera(Bundle bundle) {
		Intent intent = new Intent();
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			// 给要拍摄的照片命名
			String fileName = System.currentTimeMillis() + ".jpg";
			String path = FrameworkUtil.getCompressImagePath();
			if (TextUtils.isEmpty(path)) {
				show("SD卡剩余空间不足…");
				finishActivity();
			} else {
				File picDir = new File(path);
				mImageFile = new File(picDir, fileName);
				// 跳转到拍照界面
				intent.setAction(IMAGE_CAPTURE_ACTION);
				intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mImageFile));
				startActivityForResult(intent, mRequestCode);
			}
		} else {
			// 给出提示:请插入SD卡,再进行操作
			show("请插入SD卡并重试");
			finishActivity();
		}
	}

	Handler mHandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg) {
			final String imagePath;
			if (msg.what == 0) {
				imagePath = getPath((Uri) msg.obj);
			}else {
				imagePath = (String) msg.obj;
			}
			File newFile = new File(imagePath);
			if (!newFile.exists() || newFile.length() <= 0) {
                mHandler.removeMessages(msg.what);
				mHandler.sendMessageDelayed(msg, 50);
				return;
			}

			ExecutorSupport.getExecutor().execute(new Runnable() {
				@Override
				public void run() {
					String newPath = ImageUtil.compressImage(imagePath);
                    if (isEditAble) {
                        Intent in = new Intent(CameraActivity.this, EditImageActivity.class);
                        in.putExtra(EditImageActivity.IMAGE_PATH, newPath);
                        in.putExtra(EditImageActivity.IS_ADD_WATER, isAddWaterMark);
                        startActivityForResult(in, EditImageActivity.REQUEST_CODE);
                    } else {
                        if (isAddWaterMark && !TextUtils.isEmpty(newPath)) {
                            ImageUtil.addWaterRemark(
                                    newPath,
                                    "water_remark_icon.png",
                                    getResources().getDimensionPixelSize(R.dimen.x24),
                                    Color.YELLOW
                                    , Color.BLACK);
                        }
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra(EXTRA_IMAGE_PATH, newPath);
                        Logger.d(TAG, "newPath:" + newPath);
                        setResult(RESULT_OK, resultIntent);
                        finish();
                    }
				}
			});
		}
	};

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
            if (mRequestCode == requestCode) {
                // 机型适配(不同手机返回的地址不一样)
                if (null != data && null != data.getData()) {
                    Message msg = new Message();
                    msg.what = 0;
                    msg.obj = data.getData();
                    mHandler.sendMessageDelayed(msg, 50);
                } else {
                    if (null != mImageFile) {
                        Message msg = new Message();
                        msg.what = 1;
                        msg.obj = mImageFile.getAbsolutePath();
                        mHandler.sendMessageDelayed(msg, 50);
                    }
                }
            } else if (EditImageActivity.REQUEST_CODE == requestCode) {
                if (null == data || null == data.getExtras()) {
                    return;
                }
                String newPath = data.getExtras().getString(EditImageActivity.RESULT_PATH);
                if (TextUtils.isEmpty(newPath)) {
                    return;
                }
                Logger.d(TAG, "newPath:" + newPath);
                Intent in = getIntent();
                in.putExtra(EXTRA_IMAGE_PATH, newPath);
                setResult(Activity.RESULT_OK, in);
                finish();
            }
		} else {
			finishActivity();
		}
	}

	/**
	 * 通过指定文件的URI得到文件所在的路径
	 * 
	 * @param uri
	 *            指定文件的URI
	 * @return 文件所在的路径
	 */
	private String getPath(Uri uri) {
		String[] projection = { MediaColumns.DATA };
		Cursor cursor = managedQuery(uri, projection, null, null, null);
		String path = "";
		if (null != cursor) {
			int index = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
			cursor.moveToFirst();
			path = cursor.getString(index);
			if (Integer.parseInt(Build.VERSION.SDK) < 14) {
				cursor.close();
			}
		}else{
			path = uri.getPath();
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
	 * @param outState
	 *            outState
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putSerializable(KEY_IMAGE_FILE, mImageFile);
		outState.putBoolean(KEY_ADD_WATER_MARK, isAddWaterMark);
		outState.putBoolean(KEY_IS_EDITABLE, isEditAble);
		super.onSaveInstanceState(outState);
	}

}
