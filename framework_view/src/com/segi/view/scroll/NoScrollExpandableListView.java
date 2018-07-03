/*
 * 文件名：NoScrollListView.java
 * 创建人：王玉丰
 * 创建时间：2013-9-9
 * 版     权：Copyright Easier Digital Tech. Co. Ltd. All Rights Reserved.
 */
package com.segi.view.scroll;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ListAdapter;

/**
 * 没有滚动条的ExpandableListView<BR>
 * 解决ExpandableListView与ScrollView共用问题
 * 
 * @author liangzx
 * @version [segi, 2013-9-9] 
 */
public class NoScrollExpandableListView extends ExpandableListView {
	
	/**
	 * 构造
	 */
	public NoScrollExpandableListView(Context context) {
		super(context);
	}
	
	/**
	 * 构造
	 */
	public NoScrollExpandableListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	/**
	 * 构造
	 */
	public NoScrollExpandableListView(Context context, AttributeSet attrs, int defStyle) {
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
	
	/**
	 * 计算所有高度
	 * @param listView
	 */
	public void setListViewHeightBasedOnChildren() {
        //获取ListView对应的Adapter
	    ListAdapter listAdapter = this.getAdapter();
	    if (listAdapter == null) {
	        // pre-condition
	        return;
	    }
	
	    int totalHeight = 0;
	    for (int i = 0, len = listAdapter.getCount(); i < len; i++) {   //listAdapter.getCount()返回数据项的数目
	        View listItem = listAdapter.getView(i, null, this);
	        listItem.measure(0, 0);  //计算子项View 的宽高
	        totalHeight += listItem.getMeasuredHeight();  //统计所有子项的总高度
	    }
	
	    ViewGroup.LayoutParams params = this.getLayoutParams();
	    params.height = totalHeight + (this.getDividerHeight() * (listAdapter.getCount() - 1));
	    //listView.getDividerHeight()获取子项间分隔符占用的高度
	    //params.height最后得到整个ListView完整显示需要的高度
	    this.setLayoutParams(params);
	}

}
