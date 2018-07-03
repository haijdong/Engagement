/*
 * 文件名：CustomAlterRadioDialog.java
 * 创建人：liangzx
 * 创建时间：2014-12-15
 * 版     权：Copyright Easier Digital Tech. Co. Ltd. All Rights Reserved.
 */
package com.segi.view.alert;

import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.segi.view.R;

/**
 * 弹窗式单选按钮列表
 * @author liangzx
 * @version [uHome, 2014-12-15]
 */
public class CustomAlterRadioDialog extends Dialog{
	
	public static AlertDialog customAlterDialog = null;
	
	public CustomAlterRadioDialog(Context context) {
		super(context);
	}
	
	public CustomAlterRadioDialog(Context context, int theme) {
		super(context, theme);
	}

	private static int mChooseId;
    
    /**
     * 创建弹出框
     * @param versionInfo
     * @author liangzx
     */
    public static void createDailog(Context context,
    		final OnWithDataDailogListener listener,
    		int title,
    		List<IdStringInfo> data,
    		boolean isCancelable) {
    	
    	customAlterDialog = new Builder(context).create();
    	customAlterDialog.show();
    	Window window = customAlterDialog.getWindow();
    	window.setContentView(R.layout.alert_radio_alert);
    	TextView tv_title = (TextView) window.findViewById(R.id.alert_title);
    	Button rBtn = (Button) window.findViewById(R.id.alert_rbtn);
    	Button lBtn = (Button) window.findViewById(R.id.alert_lbtn);
    	ListView listView = (ListView) window.findViewById(R.id.alert_list);
    	
    	rBtn.setText(R.string.cancel);
    	lBtn.setText(R.string.ok_btn);
    	lBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				listener.onPositiveButton(mChooseId);
				customAlterDialog.dismiss();
			}
		});
    	rBtn.setOnClickListener(new View.OnClickListener() {
    		@Override
    		public void onClick(View v) {
    			customAlterDialog.dismiss();
    		}
    	});
    	tv_title.setText(title);
    	if (null != data && data.size() > 0) {
    		CustomAdapter adapter = new CustomAdapter(context, data);
    		listView.setAdapter(adapter);
    	}
    	
    	customAlterDialog.setCancelable(isCancelable);
    	
    }
    
    /**
     * 列表适配器
     * @author liangzx
     */
    static class CustomAdapter extends BaseAdapter
    {
    	private List<IdStringInfo> mListData;
    	private LayoutInflater mInflater;
    	
    	public CustomAdapter(Context context, List<IdStringInfo> listData)
    	{
    		mListData = listData;
    		mInflater = LayoutInflater.from(context);
    	}
    		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			if (null != mListData && mListData.size() > 0) {
				return mListData.size();
			}
			return 0;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			if (null != mListData && mListData.size() > 0) {
				return mListData.get(position);
			}
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			if (null != mListData && mListData.size() > 0) {
				return mListData.get(position).id;
			}
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			convertView = mInflater.inflate(R.layout.alert_radio_item, parent, false);
			
			TextView name = (TextView) convertView.findViewById(R.id.name);
			ImageView icon = (ImageView) convertView.findViewById(R.id.icon);
			
			if (null != mListData && mListData.size() > position) {
				name.setText(mListData.get(position).name);
				if (mChooseId == mListData.get(position).id) {
					icon.setImageResource(R.drawable.radiobox_pre);
				} else if (position == 0 && mChooseId == 0) {
					icon.setImageResource(R.drawable.radiobox_pre);
					mChooseId = mListData.get(position).id;
				} else{
					icon.setImageResource(R.drawable.radiobox_nor);
				}
				convertView.setTag(mListData.get(position).id);
				convertView.setOnClickListener(chooseListener);
			}
			return convertView;
		}
    	
		android.view.View.OnClickListener chooseListener = new android.view.View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mChooseId = (Integer) v.getTag();
				notifyDataSetChanged();
			}
		};
    }
    
    
    
}
