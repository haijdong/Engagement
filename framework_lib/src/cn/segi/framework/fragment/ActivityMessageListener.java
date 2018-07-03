package cn.segi.framework.fragment;

/**
 * activity发送通知到fragment
 */
public interface ActivityMessageListener {
    /**
     * 处理Fragment发送的消息
     * @param tag Fragment的tag
     * @param what 消息类型
     * @param obj 传递数据
     */
    void handleActivityMessage(String tag, int what, Object obj);
}