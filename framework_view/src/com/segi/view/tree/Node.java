package com.segi.view.tree;

import java.util.List;

/**
 * 节点实体
 *
 * @author liangzx
 * @version 1.0
 * @time 2017-12-14 15:44
 **/
public class Node<T, B> {

    /**
     * 所属节点的真实实体数据
     */
    public B bean;
    /**
     * 节点id
     */
    public T id;
    /**
     * 父节点id
     */
    public T pId;
    /**
     * 节点名称
     */
    public String name;
    /**
     * 是否展开
     */
    private boolean isExpand;
    /**
     * 图标的资源id（开启、关闭、其他，三种类型）
     */
    public static int ICON_EXPAND = 0, ICON_NO_EXPAND = 1, ICON_OTHER = -1;
    /**
     * 坐标箭头图标
     */
    public int icon = ICON_OTHER;
    /**
     * 父节点实体
     */
    public Node<T, B> parent;
    /**
     * 子节点列表
     */
    public List<Node> children;
    /**
     * 是否为叶子节点（为兼容可能不是一次性返回的数据）<br>
     * 1：不是<br>
     * 2：是<br>
     */
    private int isLeaf;


    /**
     * 是否为根节点
     * @return
     */
    public boolean isRoot() {
        return null == parent;
    }

    /**
     * 设置是否是叶子节点<br>
     * 1：不是<br>
     * 2：是<br>
     * @param isLeaf
     */
    public void setLeaf(int isLeaf) {
        this.isLeaf = isLeaf;
    }

    /**
     * 是否为叶子节点
     * @return
     */
    public boolean isLeaf() {
        return isLeaf == 0 ? (null == children || children.size() <= 0) : isLeaf == 2;
    }

    /**
     * 获取子节点数量
     * @return
     */
    public int getChildCount() {
        if (null != children) {
            return children.size();
        }
        return 0;
    }

    /**
     * 父节点是否为展开状态
     */
    public boolean isParentExpand() {
        if (isRoot()) {
            return false;
        }
        return parent.isExpand;
    }

    /**
     * 获取当前层级
     * @return
     */
    public int getLevel() {
        if (isRoot()) {
            return 1;
        }
        return parent.getLevel() + 1;
    }

    /**
     * 是否收起
     * @return
     */
    public boolean isExpand() {
        return isExpand;
    }

    /**
     * 设置展开收起状态
     * @param isExpand
     */
    public void setExpand(boolean isExpand) {
        this.isExpand = isExpand;
        if (!isExpand) {//收起时，下面所有节点都收起
            if (null != children && children.size() > 0) {
                for (Node item : children) {
                    item.setExpand(isExpand);
                }
            }
        }
    }

    /**
     * 设置图标
     */
    public void setIcon() {
        if (!isLeaf()) {//不是叶子节点
            if (isExpand) {
                icon = ICON_EXPAND;
            } else {
                icon = ICON_NO_EXPAND;
            }
        } else {
            icon = ICON_OTHER;
        }
    }

    @Override
    public String toString() {
        return "Node{" +
                "id=" + id +
                ", pId=" + pId +
                ", name='" + name + '\'' +
                ", level=" + getLevel() +
                '}';
    }

}
