package com.segi.view.tree;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import java.util.List;

import cn.segi.framework.adapter.ViewHolder;
import cn.segi.framework.util.WindowDispaly;

/**
 * 树形结构列表适配器
 *
 * @author liangzx
 * @version 1.0
 * @time 2017-12-14 16:03
 **/
public abstract class TreeListViewAdapter<B> extends BaseAdapter implements AdapterView.OnItemClickListener {

    /**
     * 列表
     */
    protected ListView mTreeListView;
    /**
     * 存储所有节点数据
     */
    protected List<Node> mAllNodes;
    /**
     * 存储所有可见的节点数据
     */
    protected List<Node> mShowNodes;
    /**
     * 点击的回调事件
     */
    private OnTreeNodeClickListener mOnTreeNodeClickListener;
    /**
     *
     */
    protected Context mContext;
    /**
     * 布局文件id
     */
    protected int mItemLayoutId;
    /**
     * 每一层增加的间距
     */
    protected int mLevelPadding = 30;

    /**
     * 设置回调事件
     * @param listener
     */
    public void setOnTreeNodeClickListener(OnTreeNodeClickListener listener) {
        this.mOnTreeNodeClickListener = listener;
    }

    /**
     *
     * @param context
     * @param treeListView
     * @param datas
     * @param defaultExpandLevel
     */
    public TreeListViewAdapter(Context context, ListView treeListView, int layoutId, List<B> datas, int defaultExpandLevel, OnTreeNodeClickListener listener) {
        this.mContext = context;
        this.mItemLayoutId = layoutId;
        this.mOnTreeNodeClickListener = listener;
        this.mTreeListView = treeListView;
        try {
            mAllNodes = TreeHelper.getSortedNodes(datas, defaultExpandLevel);
            mShowNodes = TreeHelper.filterVisibleNode(mAllNodes);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        mLevelPadding = getLevelPaddingSize();
        mTreeListView.setOnItemClickListener(this);
    }

    /**
     *
     * @param context
     * @param treeListView
     * @param layoutId
     * @param datas
     * @param defaultExpandLevel
     */
    public TreeListViewAdapter(Context context, ListView treeListView, int layoutId, List<B> datas, int defaultExpandLevel) {
        this(context, treeListView, layoutId, datas, defaultExpandLevel, null);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (null != mShowNodes && mShowNodes.size() > position) {
            Node item = mShowNodes.get(position);
            if (null != mOnTreeNodeClickListener) {
                mOnTreeNodeClickListener.onTreeNodeClick(item, position);
            }
        }
    }

    /**
     * 关闭或打开
     * @param n
     */
    public void expandOrCollapse(Node n) {
        if (!n.isLeaf()) {//非叶子节点
            n.setExpand(!n.isExpand());
            mShowNodes = TreeHelper.filterVisibleNode(mAllNodes);
            notifyDataSetChanged();
        }
    }

    /**
     * 获取当前展开的最大层数
     */
    public int getCurrentExpendMaxLevel() {
        return TreeHelper.getCurrentExpendMaxLevel(mShowNodes);
    }

    /**
     * 批量更新全部数据
     * @param datas
     */
    public void updateData(List<B> datas) {
        try {
            mAllNodes = TreeHelper.getSortedNodes(datas, 0);
            mShowNodes = TreeHelper.filterVisibleNode(mAllNodes);
            notifyDataSetChanged();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 新增数据
     * @param datas
     */
    public void addData(List<B> datas) {
        try {
            mAllNodes = TreeHelper.getSortedNodes(datas, mAllNodes, 0);
            mShowNodes = TreeHelper.filterVisibleNode(mAllNodes);
            notifyDataSetChanged();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新节点是否是子节点
     * @param node
     * @param isLeaf
     */
    public void updateNodeLeaf(Node node, boolean isLeaf) {
        if (null != node) {
            node.setLeaf(isLeaf ? 2 : 1);
            node.setIcon();
        }
    }

    /**
     * 依层次叠加列表宽度
     */
    public void fillTreeLvForFullScreen() {
        int maxLevel = getCurrentExpendMaxLevel();
        ViewGroup.LayoutParams params = mTreeListView.getLayoutParams();
        params.width = WindowDispaly.getWidth() + (maxLevel - 1) * mLevelPadding;
        mTreeListView.setLayoutParams(params);
    }


    @Override
    public int getCount() {
        if (null == mShowNodes) {
            return 0;
        }
        return mShowNodes.size();
    }

    @Override
    public Node getItem(int position) {
        if (null == mShowNodes) {
            return null;
        }
        return mShowNodes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder = getViewHolder(position, convertView, parent);
        Node item = getItem(position);
        convert(holder, item, position, item.getLevel(), mLevelPadding);
        return holder.getConvertView();
    }

    /**
     * 由子类实现Item布局填充内容<br>
     * 返回当前层次和每一层次间距大小
     * @param helper
     * @param item
     * @param position
     * @param level
     * @param padding
     */
    public abstract void convert(ViewHolder helper, Node item, int position, int level, int padding);


    private ViewHolder getViewHolder(int position, View convertView, ViewGroup parent) {
        return ViewHolder.get(mContext, convertView, parent, mItemLayoutId, position);
    }

    /**
     * 获取每一层增加的间距
     * @return
     */
    public abstract int getLevelPaddingSize();
}
