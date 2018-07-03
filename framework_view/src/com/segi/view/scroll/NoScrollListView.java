/*
 * 文件名：NoScrollListView.java
 * 创建人：王玉丰
 * 创建时间：2013-9-9
 * 版     权：Copyright Easier Digital Tech. Co. Ltd. All Rights Reserved.
 */
package com.segi.view.scroll;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * 没有滚动条的ListView<BR>
 * 解决ListView与ScrollView共用问题
 * 
 * @author liangzx
 * @version [segi, 2013-9-9] 
 */
public class NoScrollListView extends ListView {
	
	/**
	 * 构造
	 */
	public NoScrollListView(Context context) {
		super(context);
	}
	
	/**
	 * 构造
	 */
	public NoScrollListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	/**
	 * 构造
	 */
	public NoScrollListView(Context context, AttributeSet attrs, int defStyle) {
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
