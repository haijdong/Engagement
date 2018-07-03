/*
 * 文件名：Processor.java
 * 创建人：liangzx
 * 创建时间：2015-10-16
 */
package cn.segi.framework.net;


/**
 * 请求处理器<BR>
 * 
 * @author liangzx
 * @version [2015-10-16] 
 */
public interface Processor {
    /**
     * 处理网络请求
     * @param request 请求
     */
    void processNet(Request request);
    
    /**
     * 处理本地请求
     * @param request 请求
     */
    void processLocal(Request request);
    
    /**
     * 取消请求
     * @param tag 请求标识
     */
    void cancel(String tag);
}
