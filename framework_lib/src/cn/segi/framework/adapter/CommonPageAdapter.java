package cn.segi.framework.adapter;

import java.util.List;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 通用适配器
 * @author liangzx
 * @version 1.0
 */
public abstract class CommonPageAdapter<T> extends PagerAdapter {

	protected int mItemLayoutId;
	protected Context mContext;
	protected List<T> mDatas;
	private SparseArray<View> mViews;  
	
	public CommonPageAdapter(Context context, List<T> mDatas, int itemLayoutId)
	{
		this.mContext = context;  
        this.mDatas = mDatas;  
        this.mItemLayoutId = itemLayoutId;  
        if (null != mDatas) {
        	mViews = new SparseArray<View>(mDatas.size()); 
		}
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
	public int getItemPosition(Object object) {
		return super.getItemPosition(object);
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		// TODO Auto-generated method stub
		return view == object;
	}
	
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		// TODO Auto-generated method stub
		if (null == mViews) {
			return null;
		}
		View view = mViews.get(position);  
        if (view == null) {  
        	view = LayoutInflater.from(mContext).inflate(mItemLayoutId, container, false);
        	convert(view, mDatas.get(position), position);
            mViews.put(position, view);  
        }  
        container.addView(view);  
        return view;  
	}
	
	/**
	 * 根据资源id获取控件
	 * @param viewId
	 * @return 
	 */
	@SuppressWarnings({ "unchecked", "hiding" })
	public <T extends View> T getView(View mConvertView,int viewId)
	{
		View view = null;
		view = mConvertView.findViewById(viewId);
		return (T)view;
	}
	
	public abstract void convert(View view, T item, int position);

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		if (null != mViews) {
			container.removeView(mViews.get(position));  
		}
	}
	
	
	
	
}
