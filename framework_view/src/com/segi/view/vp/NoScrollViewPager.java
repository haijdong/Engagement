package com.segi.view.vp;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 自适应子View高度的viewPager
 * 
 * @author hellsam
 * 
 */
public class NoScrollViewPager extends ViewPager {

    /**
     * 默认支持左右滑动
     */
	private boolean mScrollEnable = true;

	public NoScrollViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 触摸事件不触发
        if (this.mScrollEnable) {
            return super.onTouchEvent(event);
        }
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        // 不处理触摸拦截事件
        if (this.mScrollEnable) {
            return super.onInterceptTouchEvent(event);
        }
        return false;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        // 分发事件，这个是必须要的，如果把这个方法覆盖了，那么ViewPager的子View就接收不到事件了
        if (this.mScrollEnable) {
            return super.dispatchTouchEvent(event);
        }
        return super.dispatchTouchEvent(event);
    }

	/**
	 * 设置是否可以左右滑动
	 * @param enabled
	 */
	public void setScrollEnable(boolean enabled) {
		this.mScrollEnable = enabled;
	}

}
