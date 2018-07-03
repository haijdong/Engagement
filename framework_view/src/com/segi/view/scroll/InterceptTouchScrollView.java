package com.segi.view.scroll;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 自定义sc添加滑动事件监听
 * 
 * @author liangzx
 * 
 */
public class InterceptTouchScrollView extends ObservableScrollView{

    /**
     * 是否拦截滑动事件
     */
    private boolean isIntercept = false;
	public boolean isIntercept() {
		return isIntercept;
	}

	public void setIntercept(boolean isIntercept) {
		this.isIntercept = isIntercept;
	}

	public InterceptTouchScrollView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public InterceptTouchScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	
	public InterceptTouchScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs,defStyle);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return isIntercept? false: super.onInterceptTouchEvent(ev);
	}
}
