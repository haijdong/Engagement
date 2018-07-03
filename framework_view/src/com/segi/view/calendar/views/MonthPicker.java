package com.segi.view.calendar.views;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import java.util.Calendar;
import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.segi.view.calendar.bizs.decors.DPDecor;
import com.segi.view.calendar.bizs.languages.DPLManager;
import com.segi.view.calendar.bizs.themes.DPTManager;
import com.segi.view.calendar.cons.DPMode;
import com.segi.view.calendar.utils.MeasureUtil;

/**
 * MonthPicker
 * 月份选择器
 * @author liangzx 2016-7-25
 */
public class MonthPicker extends LinearLayout {
    private DPTManager mTManager;// 主题管理器
    private DPLManager mLManager;// 语言管理器

    private YearView yearView;// 年视图
    private TextView tvYear;// 年份显示
    private TextView tvEnsure;// 确定按钮显示


    private OnDateSelectedListener onDateSelectedListener;// 月份多选后监听
    private OnDateScrollListener onDateScrollListener;// 月份变化监听

    /**
     * 月份单选监听器
     */
    public interface OnDatePickedListener {
        void onDatePicked(String date);
    }

    /**
     * 月份多选监听器
     */
    public interface OnDateSelectedListener {
        void onDateSelected(List<String> date);
    }
    
    /**
     * 月份滑动监听器
     */
    public interface OnDateScrollListener {
    	void onDateChange(String date);
    }

    public MonthPicker(Context context) {
        this(context, null);
    }

    public MonthPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        mTManager = DPTManager.getInstance();
        mLManager = DPLManager.getInstance();

        // 设置排列方向为竖向
        setOrientation(VERTICAL);

        LayoutParams llParams =
                new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

        // 标题栏根布局
        RelativeLayout rlTitle = new RelativeLayout(context);
        rlTitle.setBackgroundColor(mTManager.colorTitleBG());
        int rlTitlePadding = MeasureUtil.dp2px(context, 10);
        rlTitle.setPadding(rlTitlePadding, rlTitlePadding, rlTitlePadding, rlTitlePadding);


        // 标题栏子元素布局参数
        RelativeLayout.LayoutParams lpYear =
                new RelativeLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        lpYear.addRule(RelativeLayout.CENTER_HORIZONTAL);
        
        RelativeLayout.LayoutParams lpEnsure =
                new RelativeLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        lpEnsure.addRule(RelativeLayout.CENTER_VERTICAL);
        lpEnsure.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        // --------------------------------------------------------------------------------标题栏
        // 年份显示
        tvYear = new TextView(context);
        tvYear.setGravity(Gravity.CENTER_VERTICAL);
        tvYear.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        tvYear.setTextColor(mTManager.colorTitle());

        // 确定显示
        tvEnsure = new TextView(context);
        tvEnsure.setText(mLManager.titleEnsure());
        tvEnsure.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        tvEnsure.setTextColor(mTManager.colorTitle());
        tvEnsure.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != onDateSelectedListener) {
                    onDateSelectedListener.onDateSelected(yearView.getDateSelected());
                }
            }
        });

        rlTitle.addView(tvYear, lpYear);
        rlTitle.addView(tvEnsure, lpEnsure);

        addView(rlTitle, llParams);
        // ------------------------------------------------------------------------------------年视图
        yearView = new YearView(context);
        yearView.setOnDateChangeListener(new YearView.OnDateChangeListener() {
            @Override
            public void onYearChange(int year) {
                String tmp = String.valueOf(year);
                if (tmp.startsWith("-")) {
                    tmp = tmp.replace("-", mLManager.titleBC());
                }
                tvYear.setText(tmp + "年");
                MonthPicker.this.year = tmp;
            }
        });
        addView(yearView, llParams);
        Calendar today = Calendar.getInstance();
        setDate(today.get(Calendar.YEAR), today.get(Calendar.MONTH) + 1);
    }
    
    private String year;
    private String month;
    
    public Handler mHandler = new Handler()
	{
		public void handleMessage(Message msg) {
			if (msg.what == 0) {
				if (null != onDateScrollListener) {
					onDateScrollListener.onDateChange(year + "-" + month);
				}
			}else{
				yearView.invalidate();
			}
		};
	};

    /**
     * 设置初始化年月月份
     *
     * @param year  ...
     * @param month ...
     */
    public void setDate(int year, int month) {
        if (month < 1) {
            month = 1;
        }
        if (month > 12) {
            month = 12;
        }
        yearView.setDate(year, month);
    }

    public void setDPDecor(DPDecor decor) {
        yearView.setDPDecor(decor);
    }

    /**
     * 设置月份选择模式
     *
     * @param mode ...
     */
    public void setMode(DPMode mode) {
        if (mode != DPMode.MULTIPLE && mode != DPMode.BETWEEN) {
            tvEnsure.setVisibility(GONE);
        }
        yearView.setDPMode(mode);
    }


    public void setTodayDisplay(boolean isTodayDisplay) {
        yearView.setTodayDisplay(isTodayDisplay);
    }


    /**
     * 设置单选监听器
     *
     * @param onDatePickedListener ...
     */
    public void setOnDatePickedListener(OnDatePickedListener onDatePickedListener) {
        if (yearView.getDPMode() != DPMode.SINGLE) {
            throw new RuntimeException(
                    "Current DPMode does not SINGLE! Please call setMode set DPMode to SINGLE!");
        }
        yearView.setOnDatePickedListener(onDatePickedListener);
    }

    /**
     * 设置多选监听器
     *
     * @param onDateSelectedListener ...
     */
    public void setOnDateSelectedListener(OnDateSelectedListener onDateSelectedListener) {
        if (yearView.getDPMode() != DPMode.MULTIPLE && yearView.getDPMode() != DPMode.BETWEEN) {
            throw new RuntimeException(
                    "Current DPMode does not MULTIPLE! Please call setMode set DPMode to MULTIPLE!");
        }
        this.onDateSelectedListener = onDateSelectedListener;
    }
    
    /**
     * 设置年份变更监听器
     * @param onDateChangeListener
     */
    public void setOnDateScrollListener(OnDateScrollListener onDateScrollListener)
    {
    	if (null != onDateScrollListener) {
			this.onDateScrollListener = onDateScrollListener;
		}
    }
}
