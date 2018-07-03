package com.tommy.base.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ListView;

import com.tommy.base.R;

import java.util.ArrayList;

/**
 * 通用列表弹窗<br>
 * 可以请求网络
 * @author liangzx
 * @version 1.0
 * @time 2017-09-27 9:38
 **/
public abstract class BaseGridViewNetPopupWindow<T> extends BaseNetRequestPopupWindow implements View.OnClickListener, AdapterView.OnItemClickListener {

    /**
     *
     */
    public View mMenuView;
    /**
     * 列表数据
     */
    public ArrayList<T> mListData = null;
    /**
     * 列表
     */
    public GridView mGridView;

    /**
     *
     * @param context
     * @param data
     */
    public BaseGridViewNetPopupWindow(Context context, ArrayList<T> data) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(getLayoutId(), null);
        mMenuView.setBackgroundColor(mMenuView.getContext().getResources().getColor(R.color.common_bg));
        setContentView(mMenuView);
        mListData = data;
        initView();
        initData();
    }

    /**
     * 获取布局id
     * @return
     */
    public abstract int getLayoutId();

    /**
     * 初始化页面
     */
    public abstract void initView();

    /**
     * 初始化数据
     */
    public abstract void initData();

    /**
     *
     * @param id
     * @return
     */
    public View findViewById(int id) {
        return mMenuView.findViewById(id);
    }


    /**
     * 设置空布局
     */
    public abstract void setEmptyView();

    /**
     * 设置适配器
     * @param adapter
     * @return
     */
    public void setAdapter(BaseAdapter adapter)
    {
        if (null != mGridView && null != adapter) {
            mGridView.setAdapter(adapter);
        }
    }

    /**
     * 设置适配器
     * @param adapter
     * @return
     */
    public void setAdapter(ListView listView, BaseAdapter adapter)
    {
        if (null != listView && null != adapter) {
            listView.setAdapter(adapter);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.LButton) {
            dismiss();
        }
    }

}
