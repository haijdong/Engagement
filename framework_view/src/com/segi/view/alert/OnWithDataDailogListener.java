package com.segi.view.alert;

/**
 * dailog对话框
 * @author liangzx
 * @version [uHome, 2014-4-1]
 */
public interface OnWithDataDailogListener {
	/**
	 * 确定按钮
	 * @author liangzx
	 */
	void onPositiveButton(Object data);
	
	/**
	 * 取消按钮
	 * @author liangzx
	 */
	void onNegativeButton();
}
