package com.segi.view.drag;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import java.util.ArrayList;

/**
 * Created by Kinwee on 2016/9/22.
 */
public abstract class DragListAdapter<T> extends BaseAdapter{

    private ArrayList<T> data;

    public void remove(T item){
        data.remove(item);
        notifyDataSetChanged();
    }

    public void insert(T item,int to){
        data.add(to,item);
    }

    public void setData(ArrayList<T> data){
        this.data=data;
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return null==data?0:data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }

    protected abstract View getDragListItemView(int position, View convertView, ViewGroup parent);
}
