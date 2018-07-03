package com.segi.view.scroll;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * 一个可设置最大高度的listview
 *
 * @author liangzx
 * @version 1.0
 * @time 2017-07-07 16:04
 **/
public class DynamicHeightListView extends ListView {

    private int mMaxHeight = -1;

    public DynamicHeightListView(Context context) {
        super(context);
    }


    public DynamicHeightListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public DynamicHeightListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mMaxHeight > -1) {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(mMaxHeight, MeasureSpec.AT_MOST);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    /**
     * 获取最大高度
     * @return
     */
    public int getMaxHeight() {
        return mMaxHeight;
    }


    /**
     * 设置最大高度
     * @param maxHeight
     */
    public void setMaxHeight(int maxHeight) {
        this.mMaxHeight = maxHeight;
    }
}
