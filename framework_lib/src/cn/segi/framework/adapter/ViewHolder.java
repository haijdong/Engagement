package cn.segi.framework.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * 通用封装的viewholder
 * @author liangzx
 * @version 1.0
 */
public class ViewHolder {
	/**
	 * 存储布局里每个控件
	 */
	private final SparseArray<View> mViews;
	/**
	 * list里面的item视图
	 */
	private View mConvertView;
	
	/**
	 * 统一初始化布局
	 * @param context
	 * @param parent
	 * @param layoutId
	 * @param position
	 */
	private ViewHolder(Context context, ViewGroup parent, int layoutId, int position)
	{
		this.mViews = new SparseArray<View>();
		mConvertView = LayoutInflater.from(context).inflate(layoutId, parent, false);
//		mConvertView = LayoutInflater.from(context).inflate(layoutId, null);
		mConvertView.setTag(this);
	}
	
	/**
	 * 静态方法调用，获取ViewHolder
	 * @param context
	 * @param convertView
	 * @param parent
	 * @param layoutId
	 * @param position
	 * @return
	 */
	public static ViewHolder get(Context context, View convertView, ViewGroup parent, int layoutId, int position)
	{
		if (convertView  == null) {
			return new ViewHolder(context, parent, layoutId, position);
		}
		return (ViewHolder) convertView.getTag();
	}
	
	/**
	 * 根据资源id获取控件
	 * @param viewId
	 * @return 
	 */
	@SuppressWarnings("unchecked")
	public <T extends View> T getView(int viewId)
	{
		View view = mViews.get(viewId);
		if (view == null) {
			view = mConvertView.findViewById(viewId);
			mViews.put(viewId, view);
		}
		return (T)view;
	}
	
	/**
	 * 获取视图
	 * @return
	 */
	public View getConvertView()
	{
		return mConvertView;
	}
	
	/** 
     * 为TextView设置字符串 
     * @param viewId 
     * @param text 
     * @return 
     */  
    public ViewHolder setText(int viewId, CharSequence text)
    {  
        TextView view = getView(viewId);  
        view.setText(text);  
        return this;  
    }
    
    
    /**
     * 设置字符串样式
     * @param viewId
     * @param text
     * @param style
     * @return
     */
    public ViewHolder setText(int viewId, CharSequence text, int style)
    {  
    	TextView view = getView(viewId);  
    	view.setText(text);  
    	view.setTextAppearance(mConvertView.getContext(), style);
    	return this;  
    }
    
    /**
     * 设置字符串以及内容位置
     * @param viewId
     * @param text
     * @param style
     * @param grayvity
     * @return
     */
    public ViewHolder setText(int viewId, CharSequence text, int style, int grayvity)
    {  
    	TextView view = getView(viewId);  
    	view.setText(text);  
    	view.setGravity(grayvity);
    	view.setTextAppearance(mConvertView.getContext(), style);
    	return this;  
    }
}
