/*
 * 文件名：CustomAlterDialog.java
 * 创建人：liangzx
 * 创建时间：2013-11-14
 * 版     权：Copyright Easier Digital Tech. Co. Ltd. All Rights Reserved.
 */
package com.segi.view.alert;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.FrameLayout.LayoutParams;
import android.widget.TextView;

import com.segi.view.R;

import cn.segi.framework.util.WindowDispaly;


/**
 * 弹出窗
 * @author liangzx
 * @version [uHome, 2015-8-1]
 */
public class CustomAlterDialog extends Dialog{
	
	private String mTitleName;
	private String mContent;
	private String mLBtnName;
	private String mRBtnName;
	private OnDailogListener mListener;
	private boolean isCancelable;
	private int mGravity;
	private TextView lBtn,rBtn;

	/**
	 * 是否自动关闭
	 */
	private boolean autoClose=true;
	
	public CustomAlterDialog(Context context, OnDailogListener listener, String titleName, String content, String lbtnName, String rbtnName, boolean isCancelable) {
		super(context, R.style.CustomDialog);
		mTitleName = titleName;
		mContent = content;
		mLBtnName = lbtnName;
		mRBtnName = rbtnName;
		mListener = listener;
		this.isCancelable = isCancelable;
	}

	public CustomAlterDialog(Context context, OnDailogListener listener, String titleName, String content, String lbtnName, String rbtnName, boolean isCancelable,boolean autoClose) {
		super(context, R.style.CustomDialog);
		mTitleName = titleName;
		mContent = content;
		mLBtnName = lbtnName;
		mRBtnName = rbtnName;
		mListener = listener;
		this.isCancelable = isCancelable;
		this.autoClose = autoClose;
	}

	public CustomAlterDialog(Context context, OnDailogListener listener, String titleName, String content, String lbtnName, String rbtnName, boolean isCancelable, int gravity) {
		super(context, R.style.CustomDialog);
		mTitleName = titleName;
		mContent = content;
		mLBtnName = lbtnName;
		mRBtnName = rbtnName;
		mListener = listener;
		this.isCancelable = isCancelable;
		this.mGravity = gravity;
	}
	
	public CustomAlterDialog(Context context, OnDailogListener listener, String titleName, String content, String lbtnName, String rbtnName, boolean isCancelable, int gravity,boolean autoClose) {
		super(context, R.style.CustomDialog);
		mTitleName = titleName;
		mContent = content;
		mLBtnName = lbtnName;
		mRBtnName = rbtnName;
		mListener = listener;
		this.isCancelable = isCancelable;
		this.mGravity = gravity;
		this.autoClose = autoClose;
	}

	public CustomAlterDialog(Context context, OnDailogListener listener, int titleName, String content, int lbtnName, int rbtnName, boolean isCancelable) {
		super(context, R.style.CustomDialog);
		mTitleName = context.getResources().getString(titleName);
		mContent = content;
		if (0 != lbtnName) {
			mLBtnName = context.getResources().getString(lbtnName);
		}
		mRBtnName = context.getResources().getString(rbtnName);
		mListener = listener;
		this.isCancelable = isCancelable;
	}
	
	public CustomAlterDialog(Context context, int theme) {
		super(context, theme);
	}
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alert_dialog);
		View bg = findViewById(R.id.alert_layout);
		int dialogW = WindowDispaly.getWidth() * 3 / 4;
		bg.setLayoutParams(new LayoutParams(dialogW, LayoutParams.WRAP_CONTENT));
		
		TextView tv_title = (TextView) findViewById(R.id.alert_title);
		TextView tvContent = (TextView) findViewById(R.id.alert_content);
        lBtn = (TextView) findViewById(R.id.alert_lbtn);
    	rBtn = (TextView) findViewById(R.id.alert_rbtn);
    	
    	
    	if (TextUtils.isEmpty(mTitleName)) {
			tv_title.setVisibility(View.GONE);
		}else{
			tv_title.setText(mTitleName);
		}
    	if (TextUtils.isEmpty(mContent)) {
    		tvContent.setVisibility(View.GONE);
		}else{
			if (mGravity != 0) {
				tvContent.setGravity(mGravity);
			}
			tvContent.setText(mContent);
			tvContent.setMovementMethod(new ScrollingMovementMethod());
		}    	
		if(!TextUtils.isEmpty(mLBtnName)) {
			lBtn.setText(mLBtnName);
			lBtn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (mListener != null) {
						mListener.onNegativeButton();
					}
					if(autoClose){
						CustomAlterDialog.this.dismiss();
					}
				}
			});
			
		}else{
			lBtn.setVisibility(View.GONE);
		}
		if(!TextUtils.isEmpty(mRBtnName)) {
			rBtn.setText(mRBtnName);
			rBtn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (mListener != null) {
						mListener.onPositiveButton();
					}
					if(autoClose){
						CustomAlterDialog.this.dismiss();
					}
				}
			});
		}else{
			rBtn.setVisibility(View.GONE);
		}
		setCancelable(isCancelable);
	}
	
	
	/**
	 * 设置右边按钮字体颜色
	 * @param color
	 */
	public void setRbtnColor(int color)
	{
		if(null!=rBtn){
			rBtn.setTextColor(color);
		}
	}
	
	/**
	 * 设置右边按钮字体粗体
	 */
	public void setRbtnBoldText()
	{
		if (null != rBtn)
		{
			rBtn.getPaint().setFakeBoldText(true);
		}
	}
}
