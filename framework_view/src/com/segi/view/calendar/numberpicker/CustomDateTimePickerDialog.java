package com.segi.view.calendar.numberpicker;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.segi.view.R;
import com.segi.view.calendar.numberpicker.CustomNumberPickerView.OnValueChangeListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import cn.segi.framework.util.WindowDispaly;

/**
 * 自定义日历控件，目前支持 年月 2016-12 年 2016 两种格式
 * 年月日格式暂时不支持
 *
 * @author Administrator
 *
 */
public class CustomDateTimePickerDialog extends Dialog implements View.OnClickListener{

	/**
	 * 选择后的事件监听
	 */
	private NumPickerListener mNumPickerListener;
    /**
     *
     */
    private CustomNumberPickerView mYearPickerView, mMonthPickerView, mDayPickerView, mHourPickerView, mMinutePickerView, mDatePickerView;
    /**
     * 默认年月日时分的时间格式
     */
    private PickerEnum mPickType = PickerEnum.YYYYMMDDHHMM;
    /**
     * 容器
     */
    private LinearLayout mContentLayout;
    /**
     * 记录时间值
     */
    private int mYear = 0;
    private int mMonth = 0;
    private int mDay = 0;
    private int mHour = 0;
    private int mMinute = 0;
    private String mInitTime;
    private String mFormatStr = "yyyy-MM-dd HH:mm:ss";

    public CustomDateTimePickerDialog(Context context, int theme) {
    	super(context, theme);
    	// TODO Auto-generated constructor stub
    }

	/**
	 * 默认返回年月日时分格式YYYYMMDDHHMM
	 * @param context
	 * @param theme
	 * @see PickerEnum
	 */
	public CustomDateTimePickerDialog(Context context, int theme, NumPickerListener numPickerListener) {
		this(context, theme);
		mNumPickerListener = numPickerListener;
	}

	/**
	 * @param context
	 * @param theme
	 * @param pickType
	 * @see PickerEnum
	 */
	public CustomDateTimePickerDialog(Context context, int theme, PickerEnum pickType, NumPickerListener numPickerListener) {
		this(context, theme, numPickerListener);
		this.mPickType = pickType;
	}

	/**
	 * @param context
	 * @param theme
	 * @param pickType
	 * @param initTime 初始时间
	 * @see PickerEnum
	 */
	public CustomDateTimePickerDialog(Context context, int theme, PickerEnum pickType, NumPickerListener numPickerListener, String initTime) {
		this(context, theme, pickType, numPickerListener);
		mInitTime = initTime;
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.view_datetime_picker);
		findViewById(R.id.cancel_btn).setOnClickListener(this);
		findViewById(R.id.ok_btn).setOnClickListener(this);
		mContentLayout = (LinearLayout) findViewById(R.id.content);
		initPickerView();
	}

	/**
	 * 初始化选择器
	 */
	private void initPickerView(){
		LinearLayout.LayoutParams contentParam = (LinearLayout.LayoutParams) mContentLayout.getLayoutParams();
		if (PickerEnum.YYYYMMDDHHMM == mPickType) {
			mYearPickerView = new CustomNumberPickerView(getContext(), R.style.YearNumPickerTheme);
			mMonthPickerView = new CustomNumberPickerView(getContext(), R.style.MonthNumPickerTheme);
			mDayPickerView = new CustomNumberPickerView(getContext(), R.style.DayNumPickerTheme);
			mHourPickerView = new CustomNumberPickerView(getContext(), R.style.HourNumPickerTheme);
			mMinutePickerView = new CustomNumberPickerView(getContext(), R.style.MinuteNumPickerTheme);
			contentParam.width = WindowDispaly.getWidth() * 5 / 6;
			setYearValueChangeListener();
			setMonthValueChangeListener();
            mFormatStr = "yyyy-MM-dd HH:mm";
		}else if (PickerEnum.YYYYMMDDHH == mPickType) {
			mYearPickerView = new CustomNumberPickerView(getContext(), R.style.YearNumPickerTheme);
			mMonthPickerView = new CustomNumberPickerView(getContext(), R.style.MonthNumPickerTheme);
			mDayPickerView = new CustomNumberPickerView(getContext(), R.style.DayNumPickerTheme);
			mHourPickerView = new CustomNumberPickerView(getContext(), R.style.HourNumPickerTheme);
			contentParam.width = WindowDispaly.getWidth() * 3 / 4;
			setYearValueChangeListener();
			setMonthValueChangeListener();
            mFormatStr = "yyyy-MM-dd HH";
		}else if (PickerEnum.YYYYMMDD == mPickType) {
			mYearPickerView = new CustomNumberPickerView(getContext(), R.style.YearNumPickerTheme);
			mMonthPickerView = new CustomNumberPickerView(getContext(), R.style.MonthNumPickerTheme);
			mDayPickerView = new CustomNumberPickerView(getContext(), R.style.DayNumPickerTheme);
			contentParam.width = WindowDispaly.getWidth() * 3 / 4;
			setYearValueChangeListener();
			setMonthValueChangeListener();
            mFormatStr = "yyyy-MM-dd";
		}else if (PickerEnum.YYYYMM == mPickType) {
			mYearPickerView = new CustomNumberPickerView(getContext(), R.style.YearNumPickerTheme);
			mMonthPickerView = new CustomNumberPickerView(getContext(), R.style.MonthNumPickerTheme);
			contentParam.width = WindowDispaly.getWidth() * 3 / 4;
            mFormatStr = "yyyy-MM";
		}else if (PickerEnum.MMDDHHMM == mPickType) {
			mMonthPickerView = new CustomNumberPickerView(getContext(), R.style.MonthNumPickerTheme);
			mDayPickerView = new CustomNumberPickerView(getContext(), R.style.DayNumPickerTheme);
			mHourPickerView = new CustomNumberPickerView(getContext(), R.style.HourNumPickerTheme);
			mMinutePickerView = new CustomNumberPickerView(getContext(), R.style.MinuteNumPickerTheme);
			contentParam.width = WindowDispaly.getWidth() * 3 / 4;
			setMonthValueChangeListener();
            mFormatStr = "MM-dd HH:mm";
		}else if (PickerEnum.HHMM == mPickType) {
			mHourPickerView = new CustomNumberPickerView(getContext(), R.style.HourNumPickerTheme);
			mMinutePickerView = new CustomNumberPickerView(getContext(), R.style.MinuteNumPickerTheme);
			contentParam.width = WindowDispaly.getWidth() * 3 / 4;
            mFormatStr = "HH:mm";
		}else if (PickerEnum.MMDD == mPickType) {
			mMonthPickerView = new CustomNumberPickerView(getContext(), R.style.MonthNumPickerTheme);
			mDayPickerView = new CustomNumberPickerView(getContext(), R.style.DayNumPickerTheme);
			contentParam.width = WindowDispaly.getWidth() * 3 / 4;
			setMonthValueChangeListener();
            mFormatStr = "MM-dd";
		}else if (PickerEnum.MMDDHH == mPickType) {
			mMonthPickerView = new CustomNumberPickerView(getContext(), R.style.MonthNumPickerTheme);
			mDayPickerView = new CustomNumberPickerView(getContext(), R.style.DayNumPickerTheme);
			mHourPickerView = new CustomNumberPickerView(getContext(), R.style.HourNumPickerTheme);
			contentParam.width = WindowDispaly.getWidth() * 3 / 4;
			setMonthValueChangeListener();
            mFormatStr = "MM-dd HH";
		}else if (PickerEnum.YYYY == mPickType) {
			mYearPickerView = new CustomNumberPickerView(getContext(), R.style.YearNumPickerTheme);
			contentParam.width = WindowDispaly.getWidth() * 3 / 4;
            mFormatStr = "yyyy";
		}else if (PickerEnum.MM == mPickType) {
			mMonthPickerView = new CustomNumberPickerView(getContext(), R.style.MonthNumPickerTheme);
			contentParam.width = WindowDispaly.getWidth() * 3 / 4;
            mFormatStr = "MM";
		}else if (PickerEnum.DD == mPickType) {
			mDayPickerView = new CustomNumberPickerView(getContext(), R.style.DayNumPickerTheme);
			contentParam.width = WindowDispaly.getWidth() * 3 / 4;
			mFormatStr = "dd";
		} else if (PickerEnum.DDHH == mPickType) {
			mDatePickerView = new CustomNumberPickerView(getContext(), R.style.DateNumPickerTheme);
			mHourPickerView = new CustomNumberPickerView(getContext(), R.style.HourNumPickerTheme);
			contentParam.width = WindowDispaly.getWidth() * 3 / 4;
			mFormatStr = "dd HH";
		}
		mContentLayout.setLayoutParams(contentParam);
		setPickerViewParamsAndValue();
	}

	/**
	 * 设置选择器布局
	 */
	private void setPickerViewParamsAndValue()
	{
		Calendar calendar = null;
		if (TextUtils.isEmpty(mInitTime) || TextUtils.isEmpty(mFormatStr)) {
			calendar = Calendar.getInstance(Locale.CHINA);
		}else{
			calendar = Calendar.getInstance(Locale.CHINA);
			final SimpleDateFormat format = new SimpleDateFormat(mFormatStr);
			try {
				calendar.setTime(format.parse(mInitTime));
			} catch (ParseException e) {
				e.printStackTrace();
				calendar = Calendar.getInstance(Locale.CHINA);
			}
		}
		if (null != calendar) {
			mYear = calendar.get(Calendar.YEAR);
			if (mYear > 2050 || mYear < 1950) {
				mYear = 2017;
			}
			mMonth = calendar.get(Calendar.MONTH) + 1;
			mDay = calendar.get(Calendar.DAY_OF_MONTH);
			mHour = calendar.get(Calendar.HOUR_OF_DAY);
			mMinute = calendar.get(Calendar.MINUTE);
			LinearLayout.LayoutParams itemParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			itemParams.weight = 1;
			if (null != mYearPickerView) {
				mYearPickerView.setLayoutParams(itemParams);
				mYearPickerView.setMinValue(1950);
				mYearPickerView.setMaxValue(2050);
				mYearPickerView.setValue(mYear);
				mContentLayout.addView(mYearPickerView);
				findViewById(R.id.yearTitle).setVisibility(View.VISIBLE);
			}
			if (null != mMonthPickerView) {
				mMonthPickerView.setLayoutParams(itemParams);
				mMonthPickerView.setMinValue(1);
				mMonthPickerView.setMaxValue(12);
				mMonthPickerView.setValue(mMonth);
				mContentLayout.addView(mMonthPickerView);
				findViewById(R.id.monthTitle).setVisibility(View.VISIBLE);
			}
			if (null != mDayPickerView) {
				int maxDayNum = calendar.getActualMaximum(Calendar.DATE);
				refreshDayPickerView(maxDayNum);
				mDayPickerView.setLayoutParams(itemParams);
				mDayPickerView.setValue(mDay);
				mContentLayout.addView(mDayPickerView);
				findViewById(R.id.dayTitle).setVisibility(View.VISIBLE);
			}
			if (null != mDatePickerView) {
				mDatePickerView.setLayoutParams(itemParams);
				mDatePickerView.setMinValue(0);
				mDatePickerView.setMaxValue(30);
				if (mDay > 30 || mDay < 0) {
					mDay = 0;
				}
                mDatePickerView.setValue(mDay);
				mContentLayout.addView(mDatePickerView);
				TextView title = (TextView) findViewById(R.id.dayTitle);
				title.setText("天");
				title.setVisibility(View.VISIBLE);
			}
			if (null != mHourPickerView) {
				mHourPickerView.setLayoutParams(itemParams);
				mHourPickerView.setMinValue(0);
				mHourPickerView.setMaxValue(23);
				mHourPickerView.setValue(mHour);
				mContentLayout.addView(mHourPickerView);
				findViewById(R.id.hourTitle).setVisibility(View.VISIBLE);
			}
			if (null != mMinutePickerView) {
				mMinutePickerView.setLayoutParams(itemParams);
				mMinutePickerView.setMinValue(0);
				mMinutePickerView.setMaxValue(59);
				mContentLayout.addView(mMinutePickerView);
				findViewById(R.id.minuteTitle).setVisibility(View.VISIBLE);
			}
		}else{
			dismiss();
		}
	}


	/**
	 * 确定按钮的回调
	 * @author Administrator
	 *
	 */
	public interface NumPickerListener{
		/**
		 * 确定按钮的回调
		 * @param year
		 * @param month
		 * @param day
		 * @param hour
		 * @param minute
		 */
		public void timeSelectedCallBack(String year, String month, String day, String hour, String minute);
	}

	/**
	 * 设置年份监听事件
	 */
	private void setYearValueChangeListener()
	{
		if (null != mYearPickerView) {
			mYearPickerView.setOnValueChangedListener(new OnValueChangeListener() {

				@Override
				public void onValueChange(CustomNumberPickerView picker, int oldVal,
						int newVal) {
					mYear = Integer.parseInt(mYearPickerView.getContentByCurrValue());
					Calendar c = Calendar.getInstance(Locale.CHINA);
					c.set(Calendar.YEAR, mYear);
					c.set(Calendar.MONTH, mMonth - 1);
					c.set(Calendar.DAY_OF_MONTH, 1);
					int maxDayNum = c.getActualMaximum(Calendar.DATE);

					if (mDayPickerView.getMaxValue() != maxDayNum) {
						int curValue = Integer.parseInt(mDayPickerView.getContentByCurrValue());
						refreshDayPickerView(maxDayNum);
						mDayPickerView.setValue(curValue > maxDayNum ? maxDayNum : curValue);
					}

					Log.d("onValueChange", mYearPickerView.getContentByCurrValue() + " " + maxDayNum);
				}
			});
		}
	}

	/**
	 * 刷新日选择器
	 * @param maxDayNum
	 */
	private void refreshDayPickerView(int maxDayNum)
	{
		String[] values = new String[maxDayNum];
        for (int i = 1; i <= maxDayNum; i++) {
            if (i <= 9) {
                values[i - 1] = "0" + Integer.toString(i);
            } else {
                values[i - 1] = Integer.toString(i);
            }
        }
        mDayPickerView.setDisplayedValues(values, true);
		mDayPickerView.setMinValue(1);
		mDayPickerView.setMaxValue(maxDayNum);
	}

	/**
	 * 设置月份监听事件
	 */
	private void setMonthValueChangeListener()
	{
		if (null != mMonthPickerView) {
			mMonthPickerView.setOnValueChangedListener(new OnValueChangeListener() {

				@Override
				public void onValueChange(CustomNumberPickerView picker, int oldVal,
						int newVal) {
					mMonth = Integer.parseInt(mMonthPickerView.getContentByCurrValue());
					Calendar c = Calendar.getInstance(Locale.CHINA);
					c.set(Calendar.YEAR, mYear);
					c.set(Calendar.MONTH, mMonth - 1);
					c.set(Calendar.DAY_OF_MONTH, 1);
					int maxDayNum = c.getActualMaximum(Calendar.DATE);

					if (mDayPickerView.getMaxValue() != maxDayNum) {
						String[] values = new String[maxDayNum];
						for (int i = 1; i <= maxDayNum; i++) {
							if (i <= 9) {
								values[i - 1] = "0" + Integer.toString(i);
							} else {
								values[i - 1] = Integer.toString(i);
							}
						}
						int curValue = Integer.parseInt(mDayPickerView.getContentByCurrValue());
						mDayPickerView.setDisplayedValues(values, true);
						mDayPickerView.setMaxValue(maxDayNum);
						mDayPickerView.setValue(curValue > maxDayNum ? maxDayNum : curValue);
					}
					Log.d("onValueChange", mMonthPickerView.getContentByCurrValue() + " " + maxDayNum);
				}
			});
		}
	}


	@Override
	public void onClick(View v) {
		dismiss();
		if(R.id.ok_btn == v.getId()){
			if(null != mNumPickerListener){
				String year = "";
				String month = "";
				String day = "";
				String hour = "";
				String minute = "";

				if (null != mYearPickerView) {
					year = mYearPickerView.getContentByCurrValue();
				}else{
					year = Integer.toString(mYear);
				}
				if (null != mMonthPickerView) {
					month = mMonthPickerView.getContentByCurrValue();
				}else{
					month = Integer.toString(mMonth);
				}
				if (null != mDayPickerView) {
					day = mDayPickerView.getContentByCurrValue();
				} else if (null != mDatePickerView) {
					day = mDatePickerView.getContentByCurrValue();
				} else{
					day = Integer.toString(mDay);
				}
				if (null != mHourPickerView) {
					hour = mHourPickerView.getContentByCurrValue();
				}else{
					hour = Integer.toString(mHour);
				}
				if (null != mMinutePickerView) {
					minute = mMinutePickerView.getContentByCurrValue();
				}else{
					minute = Integer.toString(mMinute);
				}
				mNumPickerListener.timeSelectedCallBack(year, month, day, hour, minute);
			}
		}
	}

	/**
	 * 设置年份
	 * @param year
	 */
	public void setYear(int year){
		if(null != mYearPickerView && mYearPickerView.getMaxValue() >= year){
			mYearPickerView.setValue(year);
		}
	}

	/**
	 * 设置月份
	 * @param month
	 */
	public void setMonth(int month){
		if(null != mMonthPickerView && mMonthPickerView.getMaxValue() >= month){
			mMonthPickerView.setValue(month);
		}
	}

	/**
	 * 设置天数
	 * @param day
	 */
	public void setDay(int day){
		if(null != mDayPickerView && mDayPickerView.getMaxValue() >= day){
			mDayPickerView.setValue(day);
		}
	}

	/**
	 * 设置小时
	 * @param hour
	 */
	public void setHour(int hour){
		if(null != mHourPickerView && mHourPickerView.getMaxValue() >= hour){
			mHourPickerView.setValue(hour);
		}
	}
	
	/**
	 * 设置分钟
	 * @param minute
	 */
	public void setMinute(int minute){
		if(null != mMinutePickerView && mMinutePickerView.getMaxValue() >= minute){
			mMinutePickerView.setValue(minute);
		}
	}
	
}
