/*
 * 文件名：HttpMessage.java
 * 创建人：liangzx
 * 创建时间：2015-10-16
 */
package cn.segi.framework.net;

import java.util.Map;

/**
 * Http消息<BR>
 * @author liangzx
 * @version [2015-10-16] 
 */
public interface HttpMessage {
    /**
     * 获取该Http请求的Url<BR>
     * 
     * @param httpRequestId 该Http请求ID
     * @param requestParams 该Http请求附带参数
     * @return 返回该Http请求的Url
     */
	public String getUrl(int httpRequestId, Object requestParams);

    /**
     * 获取该Http请求的请求方式<BR>
     * 
     * @param httpRequestId 该Http请求ID
     * @param requestParams 该Http请求附带参数
     * @return 返回该Http请求的请求方式
     */
	public int getMethod(int httpRequestId, Object requestParams);

    /**
     * 获取该Http请求的请求头<BR>
     * 
     * @param request 该Http请求ID
     * @return 返回该Http请求的请求头
     */
	public Map<String, String> getHeaders(Request request);
	 /**
     * 获取该Http请求的请求头<BR>
     * 
     * @param httpRequestId 该Http请求ID
     * @param requestParams 该Http请求附带参数
     * @return 返回该Http请求的请求头
     */
	 public Map<String, String> getHeaders(int httpRequestId, Object requestParams);
    
    /**
     * 获取该Http请求的请求参数<BR>
     * 
     * @param httpRequestId 该Http请求ID
     * @param requestParams 该Http请求附带参数
     * @return 返回该Http请求的请求参数
     */
	public Map<String, String> getParams(int httpRequestId, Object requestParams);

    /**
     * 获取该Http请求的内容实体<BR>
     * POST或PUT请求才需要用到此对象
     * 
     * @param httpRequestId 该Http请求ID
     * @param requestParams 该Http请求附带参数
     * @return 返回该Http请求的所带内容体
     */
	public byte[] getBody(int httpRequestId, Object requestParams);
	
	/**
     * Returns the content type of the POST or PUT body.
     * @param httpRequestId
     * @return
     */
    public String getBodyContentType(int httpRequestId);
}
