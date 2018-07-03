package cn.segi.framework.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * 通用适配器
 * @author liangzx
 * @version 1.0
 */
public abstract class CommonAdapter<T> extends BaseAdapter {

	protected int mItemLayoutId;
	protected Context mContext;
	protected List<T> mDatas;
	
	public CommonAdapter(Context context, List<T> mDatas, int itemLayoutId)
	{
		this.mContext = context;  
        this.mDatas = mDatas;  
        this.mItemLayoutId = itemLayoutId;  
	}

	/**
	 * 更新数据
	 * @param mDatas
	 */
	public void updateData(List<T> mDatas)
	{
		this.mDatas = mDatas;
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (null == mDatas) {
			return 0;
		}
		return mDatas.size();
	}

	@Override
	public T getItem(int position) {
		// TODO Auto-generated method stub
		if (null == mDatas) {
			return null;
		}
		return mDatas.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ViewHolder holder = getViewHolder(position, convertView, parent);
		convert(holder, getItem(position), position);
		return holder.getConvertView();
	}

	public abstract void convert(ViewHolder helper, T item, int position);
	
	private ViewHolder getViewHolder(int position, View convertView, ViewGroup parent)
	{
		return ViewHolder.get(mContext, convertView, parent, mItemLayoutId, position);
	}
	
}
