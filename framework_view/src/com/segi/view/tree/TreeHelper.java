package com.segi.view.tree;


import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 树形列表工具类
 *
 * @author liangzx
 * @version 1.0
 * @time 2017-12-14 16:32
 **/
public class TreeHelper {

    /**
     * 传入用户数据，转化为排序后的节点列表数据
     * @param datas
     * @param defaultExpendLevel
     * @return
     */
    public static <B> List<Node> getSortedNodes(List<B> datas, List<Node> oldNodeList, int defaultExpendLevel) throws IllegalAccessException {
        List<Node> result = new ArrayList<Node>();
        //转化数据
        List<Node> nodes = convetData2Node(datas);
        if (null != oldNodeList && oldNodeList.size() > 0) {
            if (null == nodes) {
                nodes = new ArrayList<>(oldNodeList);
                updateNodesRelation(nodes);
            } else {
                updateNodesRelation(nodes, oldNodeList);
                nodes.addAll(oldNodeList);
            }
        } else {
            updateNodesRelation(nodes);
        }
        //拿到根节点
        List<Node> rootNodes = getRootNodes(nodes);
        //排序
        if (null != rootNodes) {
            for (Node n : rootNodes) {
                addNode(result, n, defaultExpendLevel, 1);
            }
        }
        return result;
    }

    /**
     * 传入用户数据，转化为排序后的节点列表数据(不包含新增数据)
     * @param datas
     * @param defaultExpendLevel
     * @param <B>
     * @return
     * @throws IllegalAccessException
     */
    public static <B> List<Node> getSortedNodes(List<B> datas, int defaultExpendLevel) throws IllegalAccessException {
        return getSortedNodes(datas, null, defaultExpendLevel);
    }

    /**
     * 将用户数据转化为节点数据
     * @param datas
     * @return
     */
    private static <T, B> List<Node> convetData2Node(List<B> datas) throws IllegalAccessException {
        if (null == datas || datas.size() <= 0) {
            return null;
        }
        //创建节点
        List<Node> nodes = new ArrayList<>();
        Node<T, B> node = null;
        for (B b : datas) {
            node = new Node<T, B>();
            Class<? extends Object> clazz = b.getClass();
            Field[] declaredFields = clazz.getDeclaredFields();
            for (Field f : declaredFields) {
                if (null != f.getAnnotation(TreeNodeId.class)) {
                    node.id = (T) f.get(b);
                }
                if (null != f.getAnnotation(TreeNodePid.class)) {
                    node.pId = (T) f.get(b);
                }
                if (null != f.getAnnotation(TreeNodeName.class)) {
                    node.name = (String) f.get(b);
                }
                if (null != f.getAnnotation(TreeNodeLeaf.class)) {
                    node.setLeaf(f.getBoolean(b) ? 2 : 1);
                }
            }
            node.bean = b;
            nodes.add(node);
        }
        return nodes;
    }

    /**
     * 更新节点间关系
     * @param nodes
     */
    private static void updateNodesRelation(List<Node> nodes) {
        if (null == nodes) {
            return;
        }
        for (int i = 0; i < nodes.size(); i++) {
            Node iNode = nodes.get(i);
            for (int j = i + 1; j < nodes.size(); j++) {
                Node jNode = nodes.get(j);
                if (jNode.pId.equals(iNode.id)) {
                    if (null == iNode.children) {
                        iNode.children = new ArrayList<>();
                    }
                    iNode.children.add(jNode);
                    jNode.parent = iNode;
                } else if (iNode.pId.equals(jNode.id)) {
                    if (null == jNode.children) {
                        jNode.children = new ArrayList<>();
                    }
                    jNode.children.add(iNode);
                    iNode.parent = jNode;
                }
            }
        }
    }

    /**
     * 更新节点间关系(新增数据与老数据进行绑定)
     * @param newNodes
     * @param oldNodes
     */
    private static void updateNodesRelation(List<Node> newNodes, List<Node> oldNodes) {
        if (null == oldNodes) {
            return;
        }
        if (null == newNodes) {
            return;
        }
        for (int i = 0; i < newNodes.size(); i++) {
            Node newItem = newNodes.get(i);
            for (int j = 0; j < oldNodes.size(); j++) {
                Node oldItem = oldNodes.get(j);
                if (newItem.pId.equals(oldItem.id)) {
                    if (null == oldItem.children) {
                        oldItem.children = new ArrayList();
                    }
                    oldItem.children.add(newItem);
                    oldItem.setExpand(true);
                    newItem.parent = oldItem;
                    break;
                }
            }
        }
    }

    /**
     * 获取根节点列表
     * @param nodes
     * @return
     */
    private static List<Node> getRootNodes(List<Node> nodes) {
        if (null == nodes) {
            return null;
        }
        List<Node> root = new ArrayList<Node>();
        for (Node n : nodes) {
            if (n.isRoot()) {
                root.add(n);
            }
        }
        return root;
    }

    /**
     * 把一个节点上的所有的内容都挂上去
     * @param nodes
     * @param item
     * @param defaultExpandLeval
     * @param currentLevel
     */
    private static void addNode(List<Node> nodes, Node item, int defaultExpandLeval, int currentLevel) {
        nodes.add(item);
        if (defaultExpandLeval >= currentLevel) {
            item.setExpand(true);
        }
        if (item.isLeaf()) {
            return;
        }
        if (null == item.children) {
            return;
        }
        for (int i = 0; i < item.children.size(); i++) {
            addNode(nodes, (Node) item.children.get(i), defaultExpandLeval, currentLevel + 1);
        }
    }

    /**
     * 筛选出可见的节点数据
     * @param nodes
     */
    public static List<Node> filterVisibleNode(List<Node> nodes) {
        if (null != nodes) {
            List<Node> result = new ArrayList<Node>();
            for (Node node : nodes) {
                // 如果为跟节点，或者上层目录为展开状态
                if (node.isRoot() || node.isParentExpand()) {
                    node.setIcon();
                    result.add(node);
                }
            }
            return result;
        }
        return null;
    }

    /**
     * 获取当前已展开最大的层次
     * @param nodes
     */
    public static int getCurrentExpendMaxLevel(List<Node> nodes) {
        if (null == nodes) {
            return 1;
        }
        int maxLevel = 1;
        for (Node n : nodes) {
            if (n.isExpand()) {
                int level = n.getLevel() + 1;
                maxLevel = level > maxLevel ? level : maxLevel;
            }
        }
        return maxLevel;
    }

}
