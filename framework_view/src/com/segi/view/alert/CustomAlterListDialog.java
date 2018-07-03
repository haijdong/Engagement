/*
 * 文件名：CustomAlterListDialog.java
 * 创建人：liangzx
 * 创建时间：2014-4-2
 * 版     权：Copyright Easier Digital Tech. Co. Ltd. All Rights Reserved.
 */
package com.segi.view.alert;


import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import cn.segi.framework.adapter.CommonAdapter;
import cn.segi.framework.adapter.ViewHolder;

import com.segi.view.R;

/**
 * 单选列表
 * @author liangzx
 * @version [uHome, 2014-4-1]
 */
public class CustomAlterListDialog extends Dialog{
	
	private String mTitleName;
	private OnChooseListener mListener;
	private boolean isCancelable;
	private ArrayList<IdStringInfo> mDatas;
	
	public CustomAlterListDialog(Context context, OnChooseListener listener, String titleName, boolean isCancelable, ArrayList<IdStringInfo> datas) {
		super(context, R.style.CustomDialog);
		mTitleName = titleName;
		mListener = listener;
		this.isCancelable = isCancelable;
		mDatas = datas;
	}
	
	public CustomAlterListDialog(Context context, int theme) {
		super(context, theme);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alert_list_dialog);
		if (TextUtils.isEmpty(mTitleName)) {
			findViewById(R.id.title_layout).setVisibility(View.GONE);
		}else{
			TextView tv_title = (TextView) findViewById(R.id.alert_title);
			tv_title.setText(mTitleName);
		}
		
    	ListView list = (ListView) findViewById(R.id.alert_list);
    	if (null != mDatas && mDatas.size() > 0) {
	    	list.setAdapter(new CustomAdapter(getContext(), mDatas, R.layout.text_black_item));
    		list.setOnItemClickListener(new OnItemClickListener() {
    			@Override
    			public void onItemClick(AdapterView<?> parent, View view,
    					int position, long id) {
    				dismiss();
    				if (null != mListener) {
						mListener.onChoose(position);	
					}
    			}
    		});
    	}
    	setCancelable(isCancelable);
	}
	
	public interface OnChooseListener
	{
		void onChoose(int position);
	}
	
	
	class CustomAdapter extends CommonAdapter<IdStringInfo>
	{

		public CustomAdapter(Context context, List<IdStringInfo> mDatas,
				int itemLayoutId) {
			super(context, mDatas, itemLayoutId);
		}

		@Override
		public void convert(ViewHolder h, IdStringInfo item, int position) {
			h.setText(R.id.text, item.name);
		}
		
	}
	
}
