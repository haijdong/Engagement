/*
 * 文件名：CustomCalendarDialog.java
 * 创建人：liangzx
 * 创建时间：2015-12-8
 */
package com.segi.view.alert;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.ViewGroup;

import com.segi.view.R;
import com.segi.view.calendar.bizs.calendars.DPCManager;
import com.segi.view.calendar.bizs.decors.DPDecor;
import com.segi.view.calendar.bizs.themes.DPTManager;
import com.segi.view.calendar.bizs.themes.DPTheme;
import com.segi.view.calendar.bizs.themes.OwnerTheme;
import com.segi.view.calendar.cons.DPMode;
import com.segi.view.calendar.views.MonthPicker;
import com.segi.view.calendar.views.MonthPicker.OnDatePickedListener;
import com.segi.view.calendar.views.MonthPicker.OnDateSelectedListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * 单选日历弹出窗
 * @author liangzx
 * @version [uHome, 2015-8-1]
 */
public class CustomYearDialog extends Dialog{
	
	private List<String> tmp = new ArrayList<String>();

	private boolean isCancelable;
	
	private OnChooseDateListener mOnChooseDateListener;
	
	private int initYear, initMonth;
	
	private DPMode mDPMode = DPMode.SINGLE;

	private DPTheme mDPTheme;
	
	public CustomYearDialog(Context context, int initYear, int initMonth, OnChooseDateListener mOnChooseDateListener, boolean isCancelable, DPMode dpMode, DPTheme theme) {
		super(context, R.style.CustomDialog);
		this.mOnChooseDateListener = mOnChooseDateListener;
		this.isCancelable = isCancelable;
		if (initYear == 0 || initMonth == 0) {
			Calendar today = Calendar.getInstance();
			this.initYear = today.get(Calendar.YEAR);
			this.initMonth = today.get(Calendar.MONTH) + 1;
		}else{
			this.initYear = initYear;
			this.initMonth = initMonth;
		}
		if (null != dpMode) {
			mDPMode = dpMode;
		}
		if (null != theme) {
			mDPTheme = theme;
		}
	}

	public CustomYearDialog(Context context, int initYear, int initMonth,OnChooseDateListener mOnChooseDateListener, boolean isCancelable, DPMode dpMode) {
		this(context, initYear, initMonth, mOnChooseDateListener, isCancelable, dpMode, null);
	}

	
	public CustomYearDialog(Context context, int theme) {
		super(context, theme);
	}
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		DPCManager.getInstance().setDecorBG(tmp);
		if (null == mDPTheme) {
			DPTManager.getInstance().initCalendar(new OwnerTheme());
		} else {
			DPTManager.getInstance().initCalendar(mDPTheme);
		}

        MonthPicker picker = new MonthPicker(getContext());
        picker.setDate(initYear, initMonth);
        picker.setDPDecor(new DPDecor() {
        	@Override
        	public void drawDecorBG(Canvas canvas, Rect rect, Paint paint) {
        		paint.setColor(Color.parseColor("#FAA419"));
        		canvas.drawCircle(rect.centerX(), rect.centerY(), rect.width() / 2F, paint);
        	}
        });
        picker.setMode(mDPMode);
        picker.setTodayDisplay(true);
        
        if (DPMode.MULTIPLE == mDPMode || DPMode.BETWEEN == mDPMode) {
        	picker.setOnDateSelectedListener(new OnDateSelectedListener() {
        		@Override
        		public void onDateSelected(List<String> date) {
        			tmp.clear();
        			tmp.addAll(date);
        			if (null != mOnChooseDateListener) {
        				mOnChooseDateListener.choosedMultipleDate(date);
        			}
        			dismiss();
        		}
        	});
		} else {
	        picker.setOnDatePickedListener(new OnDatePickedListener() {
	            @Override
	            public void onDatePicked(String date) {
	            	tmp.clear();
	            	tmp.add(date);
	            	if (null != mOnChooseDateListener) {
	            		mOnChooseDateListener.choosedDate(date);
					}
	                dismiss();
	            }
	        });
		}
		setCancelable(isCancelable);
		ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		setContentView(picker, params);
	}
	
	
	public interface OnChooseDateListener {
		/**
		 * 确定按钮
		 * @author liangzx
		 */
		void choosedDate(String data);
		/**
		 * 确定按钮
		 * @author liangzx
		 */
		void choosedMultipleDate(List<String> data);
	}

    
}
