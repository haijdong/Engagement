package com.segi.view.scroll;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * 不会跟着内部高度而滚动的布局
 * @author liangzx
 *
 */
public class NoChangeScrollView extends ScrollView {

	public NoChangeScrollView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	
	public NoChangeScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public NoChangeScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected int computeScrollDeltaToGetChildRectOnScreen(Rect rect) {
		// TODO Auto-generated method stub
		return 0;
	}

}
