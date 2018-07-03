package com.segi.view.alert;
/*
 * 文件名：CustomDateTimeDialog2.java
 * 创建人：liangzx
 * 创建时间2014-5-3
 */

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.segi.view.R;

/**
 * 弹出窗
 * @author liangzx
 * @version [uHome, 2014-5-3]
 */
public class CustomDateTimeDialog extends Dialog{
	
	private static Dialog customAlterDialog = null;
	private static TextView mTimeTv;
	
	private static CustomNumberPicker mYearPicker,mMonthPicker,mDayPicker,mHourPicker,mMinutePicker;
	/**
	 * 类型：1为只有年月日，2为只有时分秒，3为年月日时分秒，4月日时,5年月
	 */
	private static int mType = 1;
	
	public CustomDateTimeDialog(Context context) {
		super(context);
	}
	
	public CustomDateTimeDialog(Context context, int theme) {
		super(context, theme);
	}

	/**
	 * 
	 * @param context 上下文
	 * @param listener 监听事件类
	 * @param initTime 初始时间
	 * @param type 类型1为只有年月日，2为只有时分秒，3为年月日时分秒，4月日时
	 * @param textView 
	 */
    public static void createDailog(Context context,
    		final OnDailogListener listener,
    		String initTime,
    		int type,
    		final TextView textView
    		) {
    	mType = type;
    	TimeModel timeInfo = new TimeModel();
    	getInitTime(initTime, timeInfo);//初始化时间
    	customAlterDialog = new Builder(context).create();
    	customAlterDialog.show();
    	Window window = customAlterDialog.getWindow();
    	window.setContentView(R.layout.custom_datetime);
    	mTimeTv = (TextView) window.findViewById(R.id.timeTv);
    	Button lBtn = (Button) window.findViewById(R.id.cancelBtn);
    	Button rBtn = (Button) window.findViewById(R.id.okBtn);
    	LinearLayout content = (LinearLayout) window.findViewById(R.id.time_content);
    	
    	switch (type) {
		case 1:
			mYearPicker = new CustomNumberPicker(context, 1, timeInfo.year, timeListener);
			mMonthPicker = new CustomNumberPicker(context, 2, timeInfo.month, timeListener);
			mDayPicker = new CustomNumberPicker(context, 3, timeInfo.day, timeListener);
			content.addView(mYearPicker);
			content.addView(mMonthPicker);
			content.addView(mDayPicker);
			mDayPicker.setMonthYear(timeInfo.year, timeInfo.month);
			String initDateTime = timeInfo.year + "年"
    				+ (timeInfo.month > 9? timeInfo.month:"0"+timeInfo.month) + "月"
    				+ (timeInfo.day > 9? timeInfo.day : "0"+timeInfo.day) + "日";
			mTimeTv.setText(initDateTime);
			break;
		case 2:
			mHourPicker = new CustomNumberPicker(context, 4, timeInfo.hour, timeListener);
			mMinutePicker = new CustomNumberPicker(context, 5, timeInfo.minute, timeListener);
			content.addView(mHourPicker);
			content.addView(mMinutePicker);
			String initDateTime2 = 
					(timeInfo.hour > 9 ? timeInfo.hour : "0"+timeInfo.hour) + "点"
					+ (timeInfo.minute > 9 ? timeInfo.minute : "0"+timeInfo.minute) + "分";
			mTimeTv.setText(initDateTime2);
			break;
		case 3:
			mYearPicker = new CustomNumberPicker(context, 1, timeInfo.year, timeListener);
			mMonthPicker = new CustomNumberPicker(context, 2, timeInfo.month, timeListener);
			mDayPicker = new CustomNumberPicker(context, 3, timeInfo.day, timeListener);
			mHourPicker = new CustomNumberPicker(context, 4, timeInfo.hour, timeListener);
			mMinutePicker = new CustomNumberPicker(context, 5, timeInfo.minute, timeListener);
			mDayPicker.setMonthYear(timeInfo.year, timeInfo.month);
			content.addView(mYearPicker);
			content.addView(mMonthPicker);
			content.addView(mDayPicker);
			content.addView(mHourPicker);
			content.addView(mMinutePicker);
			String initDateTime3 = timeInfo.year + "年"
					+ (timeInfo.month > 9? timeInfo.month:"0"+timeInfo.month) + "月"
					+ (timeInfo.day >9 ? timeInfo.day : "0"+ timeInfo.day) + "日"
					+ (timeInfo.hour > 9? timeInfo.hour : "0"+timeInfo.hour)+ "点"
					+ (timeInfo.minute > 9 ? timeInfo.minute : "0"+timeInfo.minute)+ "分";
			mTimeTv.setText(initDateTime3);
			break;
		case 4:
			mMonthPicker = new CustomNumberPicker(context, 2, timeInfo.month, timeListener);
			mDayPicker = new CustomNumberPicker(context, 3, timeInfo.day, timeListener);
			mHourPicker = new CustomNumberPicker(context, 4, timeInfo.hour, timeListener);
			content.addView(mMonthPicker);
			content.addView(mDayPicker);
			content.addView(mHourPicker);
			String initDateTime4 = (timeInfo.month > 9? timeInfo.month :"0"+timeInfo.month) + "月"
					+ (timeInfo.day >9 ? timeInfo.day : "0"+timeInfo.day) + "日"
					+ (timeInfo.hour > 9? timeInfo.hour : "0"+timeInfo.hour)+"点";
			mTimeTv.setText(initDateTime4);
			break;
		case 5:
			mYearPicker = new CustomNumberPicker(context, 1, timeInfo.year, timeListener);
			mMonthPicker = new CustomNumberPicker(context, 2, timeInfo.month, timeListener);
			content.addView(mYearPicker);
			content.addView(mMonthPicker);
			String initDateTime5 = timeInfo.year + "年" + timeInfo.month + "月";
			mTimeTv.setText(initDateTime5);
			break;
		}
    	
    	
		lBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (null != listener) {
					listener.onNegativeButton();
				}
				mType = 1;
				mYearPicker = null;
				mMonthPicker = null;
				mDayPicker = null;
				mHourPicker = null;
				mMinutePicker = null;
				customAlterDialog.dismiss();
			}
		});

		rBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				textView.setText(getValue());
				if (null != listener) {
					listener.onPositiveButton();
				}
				customAlterDialog.dismiss();
			}
		});
		customAlterDialog.setCancelable(true);
		
    }
    
    /**
     * 监听事件调用的方法，设置title的内容
     */
	public static void setTimeTitle() {
        String sb = "";
		if (1 == mType) {
			sb = String.format("%d年%02d月%02d日", mYearPicker.getValue(), mMonthPicker.getValue(), mDayPicker.getValue()); 
			mDayPicker.setMonthYear(mYearPicker.getValue(), mMonthPicker.getValue());
			mTimeTv.setText(sb.toString());
		}else if(2 == mType){
			sb = String.format("%02d点%02d分",
					mHourPicker.getValue(),
					mMinutePicker.getValue()
					);
			mTimeTv.setText(sb.toString());
		}else if (3 == mType) {
			sb = String.format("%d年%02d月%02d日", mYearPicker.getValue(), mMonthPicker.getValue(), mDayPicker.getValue()); 
			sb += " ";
			sb += String.format("%02d点%02d分", mHourPicker.getValue(), mMinutePicker.getValue());
			mDayPicker.setMonthYear(mYearPicker.getValue(), mMonthPicker.getValue());
			mTimeTv.setText(sb.toString());
		}else if (4 == mType) {
			sb = String.format("%02d月%02d日", mMonthPicker.getValue(), mDayPicker.getValue()); 
			sb +=String.format("%02d点",mHourPicker.getValue());
			mDayPicker.setMonthYear(0, mMonthPicker.getValue());
			mTimeTv.setText(sb.toString());
		}else if (5 == mType) {
			mTimeTv.setText(String.format("%d年%1d月", mYearPicker.getValue(), mMonthPicker.getValue())); 
		}
		sb = null;
	}
    
	/**
	 * 获取时间显示到其他view上
	 */
	private static String getValue()
	{
		String sb = "";
		if (1 == mType) {
			sb = String.format("%d-%02d-%02d", mYearPicker.getValue(), mMonthPicker.getValue(), mDayPicker.getValue()); 
			mDayPicker.setMonthYear(mYearPicker.getValue(), mMonthPicker.getValue());
		}else if(2 == mType){
			sb = String.format("%02d:%02d",
					mHourPicker.getValue(),
					mMinutePicker.getValue()
					);
		}else if (3 == mType) {
			sb = String.format("%d-%02d-%02d", mYearPicker.getValue(), mMonthPicker.getValue(), mDayPicker.getValue()); 
			sb += " ";
			sb += String.format("%02d:%02d", mHourPicker.getValue(), mMinutePicker.getValue());
			mDayPicker.setMonthYear(mYearPicker.getValue(), mMonthPicker.getValue());
		}else if (4 == mType) {
			sb = String.format("%02d月%02d日", mMonthPicker.getValue(), mDayPicker.getValue()); 
			sb +=String.format("%02d点",mHourPicker.getValue());
			mDayPicker.setMonthYear(0, mMonthPicker.getValue());
		}else if (5 == mType) {
			sb = String.format("%d年%1d月", mYearPicker.getValue(), mMonthPicker.getValue()); 
		}
		return sb;
	}
	
	/**
	 * 初始化时间
	 * @param initTime
	 */
	static void getInitTime(String initTime, TimeModel timeInfo)
	{
		int year,month,day,hourOfDay,minute;
		Calendar cal = Calendar.getInstance();
		if (!TextUtils.isEmpty(initTime)) {
			if (mType == 1) {
				String[] date = initTime.split("-");
				year = Integer.valueOf(date[0]);
				month = Integer.valueOf(date[1]);
				day = Integer.valueOf(date[2]);
				timeInfo.set(year, month, day);
			}else if(mType == 2){
				String[] time = initTime.split(":");
				hourOfDay = Integer.valueOf(time[0]);
				minute = Integer.valueOf(time[1]);
				timeInfo.set(hourOfDay, minute);
			}else if(mType == 3){
				String[] allTime = initTime.split(" ");
				if (allTime.length > 1) {
					String[] date = allTime[0].split("-");
					String[] time = allTime[1].split(":");
					year = Integer.valueOf(date[0]);
					month = Integer.valueOf(date[1]);
					day = Integer.valueOf(date[2]);
					hourOfDay = Integer.valueOf(time[0]);
					minute = Integer.valueOf(time[1]);
					timeInfo.set(year, month, day, hourOfDay, minute);
				}
			}else if(mType == 4){
				//5月4日10点
				Pattern p = Pattern.compile("(.+)月(.+)日(.+)点");
				Matcher m = p.matcher(initTime);
				if (m.find()) {
					if (m.groupCount() > 2) {
						timeInfo.set(0, Integer.valueOf(m.group(1)), Integer.valueOf(m.group(2)), Integer.valueOf(m.group(3)), 0);
					}
				}
			}else{
				Pattern p = Pattern.compile("(.+)年(.+)月");
				Matcher m = p.matcher(initTime);
				if (m.find()) {
					if (m.groupCount() > 1) {
						timeInfo.set(Integer.valueOf(m.group(1)), Integer.valueOf(m.group(2)), 0, 0, 0);
					}
				}
			}
		}else{
			timeInfo.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH)+1, cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE));
		}
	}

	/**
	 * 时间变化事件
	 */
	static OnTimeChangeListener timeListener = new OnTimeChangeListener() {
		@Override
		public void onChange() {
			// TODO Auto-generated method stub
			setTimeTitle();
		}
	};
	
	/**
	 * 时间实例
	 * @author Administrator
	 *
	 */
	static class TimeModel
	{
		int year;
		int month;
		int day;
		int hour;
		int minute;
		
		
		public void set(int year, int month , int day)
		{
			this.year = year;
			this.month = month;
			this.day = day;
		}
		
		public void set(int hour, int minute)
		{
			this.hour = hour;
			this.minute = minute;
		}
		
		public void set(int year, int month, int day, int hour, int minute)
		{
			this.year = year;
			this.month = month;
			this.day = day;
			this.hour = hour;
			this.minute = minute;
		}
	}
}
