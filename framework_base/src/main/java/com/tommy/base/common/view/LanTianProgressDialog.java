package com.tommy.base.common.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;

import com.segi.view.R;
import com.segi.view.loading.WaitingView;

public class LanTianProgressDialog extends Dialog{

	private WaitingView mWaitingView;

	public LanTianProgressDialog(Context context) {
        this(context, R.style.CustomProgressDialog);
		init();
	}

	public LanTianProgressDialog(Context context, boolean canCloseable, String msg) {
        this(context, R.style.CustomProgressDialog);
		init(canCloseable, msg);
	}

	public LanTianProgressDialog(Context context, boolean canCloseable, int msg) {
		this(context, R.style.CustomProgressDialog);
		init(canCloseable, msg);
	}

	public LanTianProgressDialog(Context context, int theme) {
		super(context, theme);
        WaitingView.color = Color.parseColor("#0196FF");
        WaitingView.circleRadius  = 20f;
	 }

	/**
	 * 初始化页面
	 */
	private void init() {
		init(false, null);
	}
	
	/**
	 * 初始化页面
	 * @param canCloseable
	 * @param msg
	 */
	private void init(boolean canCloseable, String msg) {
		setContentView(R.layout.waiting_pupop);
        mWaitingView = (WaitingView) findViewById(R.id.id_wv);
		setCancelable(canCloseable);
		setCanceledOnTouchOutside(canCloseable);
	}
	
	/**
	 * 初始化页面
	 * @param canCloseable
	 * @param msg
	 */
	private void init(boolean canCloseable, int msg) {
		setContentView(R.layout.waiting_pupop);
        mWaitingView = (WaitingView) findViewById(R.id.id_wv);
		setCancelable(canCloseable);
		setCanceledOnTouchOutside(canCloseable);
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
     }
     
     /**
      *
      * liangzx
      * setMessage 提示内容
      * @param strMessage
      */
     public void setMessage(int strMessage){
     }
	
	@Override
	public void dismiss() {
		if (null != this && isShowing()) {
			super.dismiss();
            mWaitingView.stop();
		}
	}
	
	@Override
	public void show() {
		if (null != this) {
			super.show();
            mWaitingView.start();
		}
	}


}
