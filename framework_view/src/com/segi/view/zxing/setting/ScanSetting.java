package com.segi.view.zxing.setting;

import com.segi.view.zxing.camera.FrontLightMode;

/**
 * 扫描设置
 * @author liangzx
 *
 */
public interface ScanSetting {

	//一维码：商品
	public static final boolean KEY_DECODE_1D_PRODUCT = true;
	//一维码：工业
	public static final boolean KEY_DECODE_1D_INDUSTRIAL = true;
	//二维码
	public static final boolean KEY_DECODE_QR = true;
	//Data Matrix
	public static final boolean KEY_DECODE_DATA_MATRIX = true;
	//Aztec
	public static final boolean KEY_DECODE_AZTEC = true;
	//PDF417(测试)
	public static final boolean KEY_DECODE_PDF417 = true;
	
	//扫描成功后声音提示
	public static final boolean KEY_PLAY_BEEP = true;
	//振动
	public static final boolean KEY_VIBRATE = true;
	//闪光灯
	public static final String KEY_FRONT_LIGHT_MODE = FrontLightMode.OFF.toString();
	//批量扫描模式
	public static final boolean KEY_BULK_MODE = false;
	//自动对焦
	public static final boolean KEY_AUTO_FOCUS = true;
	//反色
	public static final boolean KEY_INVERT_SCAN = false;
	//不自动旋转
	public static final boolean KEY_DISABLE_AUTO_ORIENTATION = true;
	//不持续对焦
	public static final boolean KEY_DISABLE_CONTINUOUS_FOCUS = true;
	//不曝光
	public static final boolean KEY_DISABLE_EXPOSURE = true;
	//不使用距离测量
	public static final boolean KEY_DISABLE_METERING = true;
	//不进行条形码场景匹配
	public static final boolean KEY_DISABLE_BARCODE_SCENE_MODE = true;
	//自动打开网页
	public static final boolean KEY_AUTO_OPEN_WEB = false;

}
