/*
 * 文件名：NoScrollGridView.java
 * 创建人：林铎
 * 创建时间：2013-7-27
 * 版     权：Copyright Easier Digital Tech. Co. Ltd. All Rights Reserved.
 */
package com.segi.view.scroll;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * 
 * 没有滚动条的GridView<BR>
 * 解决GridView与ScrollView共用问题
 * 
 * @author liangzx
 * @version [segi, 2013-7-27] 
 */
public class NoScrollGridView extends GridView {
	
	/**
	 * 构造
	 */
	public NoScrollGridView(Context context) {
		super(context);
	}
	
	/**
	 * 构造
	 */
	public NoScrollGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	/**
	 * 构造
	 */
	public NoScrollGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}

}
