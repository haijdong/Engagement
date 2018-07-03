package com.tommy.base.common.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.tommy.base.R;


/**
 * 相册目录列表弹窗
 */
public class CustomSelectPhotoPopWindow extends PopupWindow {

	public CustomSelectPhotoPopWindow(Context context, OnItemClickListener mOnItemClickListener, BaseAdapter mAdapter) {
		this.setBackgroundDrawable(new ColorDrawable(0xb0000000));
		this.setFocusable(true);
		this.setOutsideTouchable(true);
		ListView listView = new ListView(context);
		listView.setLayoutParams(new ListView.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		listView.setFocusable(true);
		listView.setFocusableInTouchMode(true);
		listView.setDivider(new ColorDrawable(context.getResources().getColor(R.color.common_line)));
		listView.setCacheColorHint(Color.TRANSPARENT);
		listView.setDividerHeight(1);
		if (null != mAdapter) {
			listView.setAdapter(mAdapter);
		}
		if (null != mOnItemClickListener) {
			listView.setOnItemClickListener(mOnItemClickListener);
		}
		this.setContentView(listView);
		this.setWidth(LayoutParams.MATCH_PARENT);
		this.setHeight(LayoutParams.WRAP_CONTENT);
	}
}
