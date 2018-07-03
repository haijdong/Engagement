/*
 * 文件名：public class CustomAlterMultipleListDialog.java
 * 创建人：liangzx
 * 创建时间：2014-4-2
 * 版     权：Copyright Easier Digital Tech. Co. Ltd. All Rights Reserved.
 */
package com.segi.view.alert;


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

import com.segi.view.R;

/**
 * 多选列表
 * @author liangzx
 * @version [uHome, 2014-4-1]
 */
public class CustomAlterMultipleListDialog extends Dialog{
	
	private String mTitleName;
	private String mRBtnName;
	private OnChooseListener mListener;
	private boolean isCancelable;
	private CommonAdapter mAdapter;
	
	public CustomAlterMultipleListDialog(Context context, OnChooseListener listener, String titleName, String rBtnName, boolean isCancelable, CommonAdapter adapter) {
		super(context, R.style.CustomDialog);
		mTitleName = titleName;
		mRBtnName = rBtnName;
		mListener = listener;
		this.isCancelable = isCancelable;
		mAdapter = adapter;
	}
	
	public CustomAlterMultipleListDialog(Context context, int theme) {
		super(context, theme);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alert_list_dialog);
		if (TextUtils.isEmpty(mTitleName) && TextUtils.isEmpty(mRBtnName)) {
			findViewById(R.id.title_layout).setVisibility(View.GONE);
		}else{
			TextView tv_title = (TextView) findViewById(R.id.alert_title);
			TextView tv_rbtn = (TextView) findViewById(R.id.alert_right);
			if (!TextUtils.isEmpty(mTitleName)) {
				tv_title.setText(mTitleName);
			}
			if (!TextUtils.isEmpty(mRBtnName)) {
				tv_rbtn.setText(mRBtnName);
				tv_rbtn.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						dismiss();
						if (null != mListener) {
							mListener.onSubmit();
						}
					}
				});
			}
		}
		
    	ListView list = (ListView) findViewById(R.id.alert_list);
    	if (null != mAdapter) {
	    	list.setAdapter(mAdapter);
	    	list.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					if (null != mListener) {
						mListener.onChoose(arg2);
					}
				}
			});
    	}
    	setCancelable(isCancelable);
	}
	
	public void notifyData()
	{
		mAdapter.notifyDataSetChanged();
	}
	
	public interface OnChooseListener
	{
		void onChoose(int position);
		void onSubmit();
	}
	
	
}
