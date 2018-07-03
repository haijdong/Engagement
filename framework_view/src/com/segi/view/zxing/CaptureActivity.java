/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.segi.view.zxing;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.DecodeHintType;
import com.google.zxing.Result;
import com.segi.view.R;
import com.segi.view.zxing.camera.CameraManager;
import com.segi.view.zxing.view.ViewfinderView;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import cn.segi.framework.activity.BaseFrameworkActivity;
import cn.segi.framework.log.Logger;

/**
 * This activity opens the camera and does the actual scanning on a background thread. It draws a
 * viewfinder to help the user place the barcode correctly, shows feedback as the image processing
 * is happening, and then overlays the results when a scan is successful.
 *
 * @author dswitkin@google.com (Daniel Switkin)
 * @author Sean Owen
 */
public class CaptureActivity extends BaseFrameworkActivity implements SurfaceHolder.Callback {

  private static final String TAG = CaptureActivity.class.getSimpleName();

  private CameraManager cameraManager;
  private CaptureActivityHandler handler;
  private ViewfinderView viewfinderView;
  private boolean hasSurface;
  private Collection<BarcodeFormat> decodeFormats;
  private Map<DecodeHintType,?> decodeHints;
  private String characterSet;
  public InactivityTimer inactivityTimer;
  public BeepManager beepManager;
  private AmbientLightManager ambientLightManager;
  
  /**
  * ui配置相关参数
  */
  public static final String SCAN_TITLE = "sacn_title";
  /**
   * 扫码结果
   */
  public static final String RESULT_CODE = "result_code";

  public ViewfinderView getViewfinderView() {
    return viewfinderView;
  }

  public Handler getHandler() {
    return handler;
  }

  public CameraManager getCameraManager() {
    return cameraManager;
  }

  @Override
  protected void initViews() {
	Window window = getWindow();
	window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	setContentView(R.layout.camera);
	
	hasSurface = false;
	inactivityTimer = new InactivityTimer(this);
	beepManager = new BeepManager(this);
	ambientLightManager = new AmbientLightManager(this);
	
	TextView titleTv = (TextView) findViewById(R.id.scan_title);
	String title = this.getIntent().getStringExtra(SCAN_TITLE);
	if(TextUtils.isEmpty(title)){
		title = "二维码";
	}
	titleTv.setText(title);
  }
  
  @Override
  protected void initEvents() {
	  findViewById(R.id.cancel_scan).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				CaptureActivity.this.finish();
			}
	  });
  }

  @Override
  protected void onResume() {
    super.onResume();
    
    // CameraManager must be initialized here, not in onCreate(). This is necessary because we don't
    // want to open the camera driver and measure the screen size if we're going to show the help on
    // first launch. That led to bugs where the scanning rectangle was the wrong size and partially
    // off screen.
    cameraManager = new CameraManager(getApplication());

    viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
    viewfinderView.setCameraManager(cameraManager);

    handler = null;

//    if (ScanSetting.KEY_DISABLE_AUTO_ORIENTATION) {
//      setRequestedOrientation(getCurrentOrientation());
//    } else {
      setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//默认竖屏
//    }

    beepManager.updatePrefs();
    ambientLightManager.start(cameraManager);

    inactivityTimer.onResume();

    Intent intent = getIntent();

    decodeFormats = null;
    characterSet = null;

    if (intent != null) {
      characterSet = intent.getStringExtra(Intents.Scan.CHARACTER_SET);
    }

    SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
    SurfaceHolder surfaceHolder = surfaceView.getHolder();
    if (hasSurface) {
      // The activity was paused but not stopped, so the surface still exists. Therefore
      // surfaceCreated() won't be called, so init the camera here.
      initCamera(surfaceHolder);
    } else {
      // Install the callback and wait for surfaceCreated() to init the camera.
      surfaceHolder.addCallback(this);
    }
  }
  
  @Override
  protected void onPause() {
    if (handler != null) {
      handler.quitSynchronously();
      handler = null;
    }
    inactivityTimer.onPause();
    ambientLightManager.stop();
    beepManager.close();
    cameraManager.closeDriver();
    //historyManager = null; // Keep for onActivityResult
    if (!hasSurface) {
      SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
      SurfaceHolder surfaceHolder = surfaceView.getHolder();
      surfaceHolder.removeCallback(this);
    }
    super.onPause();
  }

  @Override
  protected void onDestroy() {
    inactivityTimer.shutdown();
    super.onDestroy();
  }
  
  /**
   * 初始化摄像头
   * @param surfaceHolder
   */
  private void initCamera(SurfaceHolder surfaceHolder) {
    if (surfaceHolder == null) {
      throw new IllegalStateException("No SurfaceHolder provided");
    }
    if (cameraManager.isOpen()) {
      Logger.w(TAG, "initCamera() while already open -- late SurfaceView callback?");
      return;
    }
    try {
      cameraManager.openDriver(surfaceHolder);
      // Creating the handler starts the preview, which can also throw a RuntimeException.
      if (handler == null) {
        handler = new CaptureActivityHandler(this, decodeFormats, decodeHints, characterSet, cameraManager);
      }
    } catch (IOException ioe) {
      Logger.w(TAG, ioe.getMessage());
//      Toast.makeText(this, R.string.msg_camera_framework_bug, Toast.LENGTH_SHORT).show();
    } catch (RuntimeException e) {
      // Barcode Scanner has seen crashes in the wild of this variety:
      // java.?lang.?RuntimeException: Fail to connect to camera service
      Logger.w(TAG, "Unexpected error initializing camera", e);
//      Toast.makeText(this, R.string.msg_camera_framework_bug, Toast.LENGTH_SHORT).show();
    }
  }

  /**
   * 获取当前屏幕方向
   * @return
   */
  private int getCurrentOrientation() {
    int rotation = getWindowManager().getDefaultDisplay().getRotation();
    if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
      switch (rotation) {
        case Surface.ROTATION_0:
        case Surface.ROTATION_90:
          return ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
        default:
          return ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
      }
    } else {
      switch (rotation) {
        case Surface.ROTATION_0:
        case Surface.ROTATION_270:
          return ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
        default:
          return ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
      }
    }
  }

  @Override
  public void surfaceCreated(SurfaceHolder holder) {
    if (holder == null) {
      Logger.e(TAG, "*** WARNING *** surfaceCreated() gave us a null surface!");
    }
    if (!hasSurface) {
      hasSurface = true;
      initCamera(holder);
    }
  }

  @Override
  public void surfaceDestroyed(SurfaceHolder holder) {
    hasSurface = false;
  }

  @Override
  public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

  }

  /**
   * A valid barcode has been found, so give an indication of success and show the results.
   *
   * @param rawResult The contents of the barcode.
   * @param scaleFactor amount by which thumbnail was scaled
   * @param barcode   A greyscale bitmap of the camera data which was decoded.
   */
  public void handleDecode(Result rawResult, Bitmap barcode, float scaleFactor) {
	inactivityTimer.onActivity();
	if (barcode != null) {
		// beep/vibrate and we have an image to draw on
		beepManager.playBeepSoundAndVibrate();
	}
	    
	final String resultString = rawResult.getText();
	if (resultString.equals("")) {
		show("扫描失败");
	}else {
		Intent resultIntent = new Intent();
		Bundle bundle = new Bundle();
		bundle.putString(RESULT_CODE, resultString);
		resultIntent.putExtras(bundle);
		setResult(RESULT_OK, resultIntent);
	}
	finish();
  }


  /**
   * 延时重启扫描页面
   * @param delayMS
   */
  public void restartPreviewAfterDelay(long delayMS) {
    if (handler != null) {
      handler.sendEmptyMessageDelayed(R.id.restart_preview, delayMS);
    }
  }

  /**
   * 重绘ViewFinderView
   */
  public void drawViewfinder() {
    viewfinderView.drawViewfinder();
  }
  
  @Override
  public boolean onKeyDown(int keyCode, KeyEvent event) {
    switch (keyCode) {
      case KeyEvent.KEYCODE_FOCUS:
      case KeyEvent.KEYCODE_CAMERA:
        // Handle these events so they don't launch the Camera app
        return true;
      // Use volume up/down to turn on light
      case KeyEvent.KEYCODE_VOLUME_DOWN:
        cameraManager.setTorch(false);
        return true;
      case KeyEvent.KEYCODE_VOLUME_UP:
        cameraManager.setTorch(true);
        return true;
    }
    return super.onKeyDown(keyCode, event);
  }

	@Override
	public void handleFragmentMessage(String tag, int what, Object obj) {
	}
	
	
	@Override
	protected void initData() {
	}
  
//  生成二维码样例代码
//  Intent intent = new Intent();
//  intent.setAction(Intents.Encode.ACTION);
//  intent.putExtra(Intents.Encode.FORMAT, BarcodeFormat.QR_CODE);
//  intent.putExtra(Intents.Encode.TYPE, Contents.Type.TEXT);
//  intent.putExtra(Intents.Encode.DATA, "Hello World!");
//  try {
//		new QRCodeEncoder(this, intent, smallerDimension, false).encodeAsBitmap();
//	} catch (WriterException e1) {
//		// TODO Auto-generated catch block
//		e1.printStackTrace();
//	}
  
}
