/*
 * 文件名：CustomAlterInputDialog.java
 * 创建人：liangzx
 * 创建时间：2014-4-1
 * 版     权：Copyright Easier Digital Tech. Co. Ltd. All Rights Reserved.
 */
package com.segi.view.alert;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Paint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.segi.view.R;

/**
 * 弹出窗
 * @author liangzx
 * @version [uHome, 2014-4-1]
 */
public class CustomAlterInputDialog extends Dialog{
	
	public static AlertDialog customAlterDialog = null;
	private static EditText tv_input = null;
	
	public CustomAlterInputDialog(Context context) {
		super(context);
	}
	
	public CustomAlterInputDialog(Context context, int theme) {
		super(context, theme);
	}
	

	/**
	 * 创建弹出框
	 * @param versionInfo
	 * @author tangst
	 */
    public static void createDailog(Context context,
    		final OnWithDataDailogListener listener,
    		String title,
    		String positionButtonName,
    		String negativeButtonName,
    		boolean isCancelable) {

    	LayoutInflater inflater = LayoutInflater.from(context);
    	View textEntryView = inflater.inflate(R.layout.alert_input_dialog, null);
    	customAlterDialog = new Builder(context).create();
    	customAlterDialog.setView(((Activity)context).getLayoutInflater().inflate(R.layout.alert_input_dialog, null));
    	customAlterDialog.show();
    	Window window = customAlterDialog.getWindow();
//    	int height = window.getWindowManager().getDefaultDisplay().getHeight();
//    	Logger.d("ALTER", "高度："+height);
//    	window.setLayout(LayoutParams.WRAP_CONTENT, height/4);
    	window.setContentView(textEntryView);
    	
    	TextView tv_title = (TextView) window.findViewById(R.id.alert_title);
    	tv_input = (EditText) window.findViewById(R.id.alert_input);
    	Button lBtn = (Button) window.findViewById(R.id.alert_lbtn);
    	Button rBtn = (Button) window.findViewById(R.id.alert_rbtn);
    	tv_title.setText(title);
    	tv_input.requestFocusFromTouch();
		if(!TextUtils.isEmpty(negativeButtonName)) {
			lBtn.setText(negativeButtonName);
			lBtn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (listener != null) {
						listener.onNegativeButton();
					}
					customAlterDialog.dismiss();
				}
			});
			
		}else{
			lBtn.setVisibility(View.GONE);
		}
		if(!TextUtils.isEmpty(positionButtonName)) {
			rBtn.setText(positionButtonName);
			rBtn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (listener != null) {
						listener.onPositiveButton(tv_input.getText().toString());
					}
				}
			});
		}else{
			rBtn.setVisibility(View.GONE);
		}
		customAlterDialog.setCancelable(isCancelable);
		
    }
    
    /**
     * 创建弹出框，显示邀请码描述
     * @param versionInfo
     * @author tangst
     */
    public static void createInviteCodeDailog(final Context context,
    		final OnWithDataDailogListener listener,
    		String title,
    		String positionButtonName,
    		String negativeButtonName,
    		boolean isCancelable) {
    	
    	LayoutInflater inflater = LayoutInflater.from(context);
    	View textEntryView = inflater.inflate(R.layout.alert_input_dialog, null);
    	customAlterDialog = new Builder(context).create();
    	customAlterDialog.setView(((Activity)context).getLayoutInflater().inflate(R.layout.alert_input_dialog, null));
    	customAlterDialog.show();
    	Window window = customAlterDialog.getWindow();
//    	int height = window.getWindowManager().getDefaultDisplay().getHeight();
//    	Logger.d("ALTER", "高度："+height);
//    	window.setLayout(LayoutParams.WRAP_CONTENT, height/4);
    	window.setContentView(textEntryView);
    	
    	((LinearLayout) window.findViewById(R.id.about_linear)).setVisibility(View.VISIBLE);
    	TextView tv_title = (TextView) window.findViewById(R.id.alert_title);
    	tv_input = (EditText) window.findViewById(R.id.alert_input);
    	Button lBtn = (Button) window.findViewById(R.id.alert_lbtn);
    	Button rBtn = (Button) window.findViewById(R.id.alert_rbtn);
    	TextView about = (TextView) window.findViewById(R.id.about);
		
    	about.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		about.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//点击下放文字触发的事件
			}
		});
    	tv_title.setText(title);
    	tv_input.requestFocusFromTouch();
    	if(!TextUtils.isEmpty(negativeButtonName)) {
    		lBtn.setText(negativeButtonName);
    		lBtn.setOnClickListener(new View.OnClickListener() {
    			@Override
    			public void onClick(View v) {
    				// TODO Auto-generated method stub
    				if (listener != null) {
    					listener.onNegativeButton();
    				}
    				customAlterDialog.dismiss();
    			}
    		});
    		
    	}else{
    		lBtn.setVisibility(View.GONE);
    	}
    	if(!TextUtils.isEmpty(positionButtonName)) {
    		rBtn.setText(positionButtonName);
    		rBtn.setOnClickListener(new View.OnClickListener() {
    			@Override
    			public void onClick(View v) {
    				// TODO Auto-generated method stub
    				if (listener != null) {
    					listener.onPositiveButton(tv_input.getText().toString());
    				}
    			}
    		});
    	}else{
    		rBtn.setVisibility(View.GONE);
    	}
    	customAlterDialog.setCancelable(isCancelable);
    	
    }
}
