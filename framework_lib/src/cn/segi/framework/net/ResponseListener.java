/*
 * 文件名：ResponseListener.java
 * 创建人：liangzx
 * 创建时间：2015-10-16
 */
package cn.segi.framework.net;

/**
 * 处理结果的回调接口<BR>
 * 为统一界面与执行结果的返回报告, 使用Request与Response方式组织逻辑处理及回调
 * 
 * @author liangzx
 * @version [2012-12-24] 
 */
public interface ResponseListener {
    /**
     * 请求成功处理结果
     * @param request 请求
     * @param response 对应的响应结果
     */
    void onProcessResult(Request request, Response response);
    
    /**
     * 请求失败处理结果
     * @param request 请求
     */
    void onProcessErrorResult(Request request, Response response);
    
}
