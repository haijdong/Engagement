package com.segi.view.alert;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.widget.TextView;

import com.segi.view.R;

public class CustomProgressDialog extends Dialog{
	
	TextView tvMsg;
	
	public CustomProgressDialog(Context context) {
		super(context, R.style.CustomProgressDialog);
		init();
	}
	
	public CustomProgressDialog(Context context, boolean canCloseable, String msg) {
		super(context, R.style.CustomProgressDialog);
		init(canCloseable, msg);
	}
	
	public CustomProgressDialog(Context context, boolean canCloseable, int msg) {
		super(context, R.style.CustomProgressDialog);
		init(canCloseable, msg);
	}

	public CustomProgressDialog(Context context, int theme) {
		super(context, theme);
	 }

	/**
	 * 初始化页面
	 */
	private void init() {
		setContentView(R.layout.custom_progress_dialog);
		setCancelable(false);
		setCanceledOnTouchOutside(false);
		tvMsg = (TextView) findViewById(R.id.progress_txt);
	}
	
	/**
	 * 初始化页面
	 * @param canCloseable
	 * @param msg
	 */
	private void init(boolean canCloseable, String msg) {
		setContentView(R.layout.custom_progress_dialog);
		setCancelable(canCloseable);
		setCanceledOnTouchOutside(canCloseable);
		tvMsg = (TextView) findViewById(R.id.progress_txt);
		if (!TextUtils.isEmpty(msg)) {
			tvMsg.setText(msg);
		}
	}
	
	/**
	 * 初始化页面
	 * @param canCloseable
	 * @param msg
	 */
	private void init(boolean canCloseable, int msg) {
		setContentView(R.layout.custom_progress_dialog);
		setCancelable(canCloseable);
		setCanceledOnTouchOutside(canCloseable);
		tvMsg = (TextView) findViewById(R.id.progress_txt);
		if (0 != msg) {
			tvMsg.setText(msg);
		}
	}
	 
	 
	 /**
	  * 设置是否可关闭
	  * @param canClose
	  */
	 public void setCanClose(boolean canClose)
	 {
		 setCancelable(canClose);
		 setCanceledOnTouchOutside(canClose);
	 }
	 
	  /**
      *
      * liangzx
      * setMessage 提示内容
      * @param strMessage
      */
     public void setMessage(String strMessage){
         if (tvMsg != null){
             tvMsg.setText(strMessage);
         }
     }
     
     /**
      *
      * liangzx
      * setMessage 提示内容
      * @param strMessage
      */
     public void setMessage(int strMessage){
    	 if (tvMsg != null){
    		 tvMsg.setText(strMessage);
    	 }
     }
	
	@Override
	public void dismiss() {
		if (null != this && isShowing()) {
			super.dismiss();
		}
	}
	
	@Override
	public void show() {
		if (null != this) {
			super.show();
		}
	}
}
