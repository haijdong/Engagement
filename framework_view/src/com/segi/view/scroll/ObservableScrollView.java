package com.segi.view.scroll;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * 自定义sc添加滑动事件监听
 * 
 * @author liangzx
 * 
 */
public class ObservableScrollView extends ScrollView{

	private ScrollViewListener scrollViewListener = null;
	

	public ObservableScrollView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public ObservableScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	
	public ObservableScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs,defStyle);
		// TODO Auto-generated constructor stub
	}

	public void setOnScrollListener(ScrollViewListener scrollViewListener) {
		this.scrollViewListener = scrollViewListener;
	}
	
	public void removeScrollListener()
	{
		this.scrollViewListener = null;
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);
		if (scrollViewListener != null) {
			scrollViewListener.onScrollChanged(getScrollY(), l, t, oldl, oldt);
		}
	}

	/**
	 * 滑动监听
	 * 
	 * @author liangzx
	 */
	public interface ScrollViewListener {
		void onScrollChanged(int scrollY, int l, int t, int oldl, int oldt);
	}
	
	/**
	 * 滑动监听
	 * 
	 * @author liangzx
	 */
	public interface ScrollViewInterceptListener {
		void intercept(boolean isIntercept);
	}
}
