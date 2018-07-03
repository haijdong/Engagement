package com.segi.view.alert;

import java.util.Calendar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.segi.view.R;

@SuppressLint("ViewConstructor")
public class CustomNumberPicker extends LinearLayout implements OnClickListener{
	
	private TextView content;
	/**
	 * 1年2月3日4时5分
	 * 年2014-2098
	 * 月1-12
	 * 日1-31
	 * 时0-24
	 * 分0-60
	 */
	private int type = 0;
	/**
	 * 某年某月最大天数
	 */
	private int mMaxDayofMonth = 0;
	/**
	 * 显示的值
	 */
	private int mShowValue = 0;

	/**
	 * 时间变化通知
	 */
	private OnTimeChangeListener mListener;
	/**
	 * 
	 * @param context
	 * @param attrs
	 * @param type(1年2月3日4时5分)
	 * @param defaultValue
	 */
	public CustomNumberPicker(Context context, int type, int defaultValue, OnTimeChangeListener listener) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.number_picker, this);
		
		Button add = (Button) findViewById(R.id.add);
		content = (TextView) findViewById(R.id.content);
		Button minus = (Button) findViewById(R.id.minus);
		this.type = type ;
		this.mShowValue = defaultValue;
		this.mListener = listener;
		
		add.setId(1);
		minus.setId(2);
		add.setOnClickListener(this);
		minus.setOnClickListener(this);
	
		Calendar cal = Calendar.getInstance();
		
		switch (type) {
		case 1:
			if (defaultValue != 0) {
				content.setText(String.format(getResources().getString(R.string.year_value), defaultValue));
			}else{
				mShowValue = cal.get(Calendar.YEAR);
				content.setText(String.format(getResources().getString(R.string.year_value), mShowValue));
			}
			cal = null;
			break;
		case 2:
			if (defaultValue != 0) {
				content.setText(String.format(getResources().getString(R.string.month_value), defaultValue));
			}else{
				mShowValue = cal.get(Calendar.MONTH);
				content.setText(String.format(getResources().getString(R.string.month_value), mShowValue));
			}
			cal = null;
			break;
		case 3:
			if (defaultValue != 0) {
				content.setText(String.format(getResources().getString(R.string.day_value), defaultValue));
			}else{
				mShowValue = cal.get(Calendar.DAY_OF_MONTH);
				content.setText(String.format(getResources().getString(R.string.day_value), mShowValue));
			}
			break;
		case 4:
			if (defaultValue != 0) {
				content.setText(String.format(getResources().getString(R.string.hour_value), defaultValue));
			}else{
				mShowValue = cal.get(Calendar.HOUR_OF_DAY);
				content.setText(String.format(getResources().getString(R.string.hour_value), mShowValue));
			}
			cal = null;
			break;
		case 5:
			if (defaultValue != 0) {
				content.setText(String.format(getResources().getString(R.string.minute_value), defaultValue));
			}else{
				mShowValue = cal.get(Calendar.MINUTE);
				content.setText(String.format(getResources().getString(R.string.minute_value), mShowValue));
			}
			cal = null;
			break;
		}
	
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case 1:
			mShowValue++;
			if (type == 1) {
				if (mShowValue <= 1990 || mShowValue >= 2099) {
					mShowValue = 1991;
				}
				mListener.onChange();
				content.setText(mShowValue+"年");
			}else if (type == 2) {
				if (0 >= mShowValue || mShowValue >= 13) {
					mShowValue = 1;
				}
				mListener.onChange();
				content.setText(mShowValue+"月");
			}else if (type == 3) {
				if (0 >= mShowValue || mShowValue > mMaxDayofMonth) {
					mShowValue = 1;
				}
				mListener.onChange();
				content.setText(mShowValue+"日");
			}else if (type == 4) {
				if (0 > mShowValue || mShowValue >= 24) {
					mShowValue = 0;
				}
				mListener.onChange();
				content.setText(mShowValue+"时");
			}else if (type == 5) {
				if (0 > mShowValue || mShowValue >= 60) {
					mShowValue = 0;
				}
				mListener.onChange();
				content.setText(mShowValue+"分");
			}
			break;
		case 2:
			mShowValue--;
			if (type == 1) {
				if (1990 >= mShowValue || mShowValue >= 2099) {
					mShowValue = 2099;
				}
				mListener.onChange();
				content.setText(mShowValue+"年");
			}else if (type == 2) {
				if (0 >= mShowValue || mShowValue >= 13) {
					mShowValue = 12;
				}
				mListener.onChange();
				content.setText(mShowValue+"月");
			}else if (type == 3) {
				if (0 >= mShowValue || mShowValue > mMaxDayofMonth) {
					mShowValue = mMaxDayofMonth;
				}
				mListener.onChange();
				content.setText(mShowValue+"日");
			}else if (type == 4) {
				if (0 > mShowValue || mShowValue >= 25) {
					mShowValue = 23;
				}
				mListener.onChange();
				content.setText(mShowValue+"时");
			}else if (type == 5) {
				if (0 > mShowValue || mShowValue >= 60) {
					mShowValue = 59;
				}
				mListener.onChange();
				content.setText(mShowValue+"分");
			}
			break;
		}
		
	}
	
	/**
	 * 获取显示的值
	 */
	public int getValue()
	{
		return mShowValue;
	}
	

	/**
	 * 设置或重置最大天数
	 * @param year
	 * @param month
	 */
	public void setMonthYear(int year, int month)
	{
		if (year == 0) {
			year = 2014;
		}
		if (month == 0) {
			month = 1;
		}
		if (month != 2) {  
	        switch (month) {  
	        case 1:  
	        case 3:  
	        case 5:  
	        case 7:  
	        case 8:  
	        case 10:  
	        case 12:  
        	mMaxDayofMonth = 31;  
	        break;  
	        case 4:  
	        case 6:  
	        case 9:  
	        case 11:  
        	mMaxDayofMonth = 30;  
	  
	        }  
	    } else {  
	        //闰年  
	        if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0)  
	        mMaxDayofMonth = 29;  
	        else  
	        mMaxDayofMonth = 28;  
	  
	    }  
	}


	
}
