package com.segi.view.tree;

/**
 * 节点点击事件
 *
 * @author liangzx
 * @version 1.0
 * @time 2017-12-14 16:10
 **/
public interface OnTreeNodeClickListener {

    /**
     * 点击回调方法
     * @param node
     * @param position
     */
    void onTreeNodeClick(Node node, int position);
}
