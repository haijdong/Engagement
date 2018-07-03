/*
 * 文件名：Request.java
 * 创建人：liangzx
 * 创建时间：2015-10-16
 */
package cn.segi.framework.net;


/**
 * 业务请求<BR>
 * TODO 后续可考虑封装成工厂方式创建
 * 
 * @author liangzx
 * @version [2015-10-16] 
 */
public class Request {

    /**
     * 请求tag
     */
    private String requestTag;
    
    /**
     * 执行的具体业务ID
     */
    private int actionId;
    
    /**
     * 是否取消该请求
     */
    private volatile boolean cancel;
    
    /**
     * 请求的附属数据（参数）
     */
    private Object data;
    /**
     * 请求超时时间
     */
    private int timeoutMs = 10000;
    /**
     * 请求最大尝试次数
     */
    private int maxRetries = 0;
    
    /**
     * 处理结果回调监听器<BR>
     * 每个Request都基本需要一个ResponseListener, 所以增加此属性<BR>
     * 而CancelListener不是所有Request都需要, 所以不纳入属性
     */
    private ResponseListener responseListener;
    
    private String cookie;
    
    public String getCookie()
	{
		return cookie;
	}

	public void setCookie(String cookie)
	{
		this.cookie = cookie;
	}

	/**
     * 构造函数
     */
    public Request(String tag) {
    	requestTag = tag;
    }

    /**
     * 获取请求tag
     * @return requestId 请求tag
     */
    public String getRequestTag() {
        return requestTag;
    }

    /**
     * 获取执行的具体业务ID
     * @return actionId 执行的具体业务ID
     */
    public int getActionId() {
        return actionId;
    }

    /**
     * 设置执行的具体业务ID
     * @param actionId 执行的具体业务ID
     */
    public void setActionId(int actionId) {
        this.actionId = actionId;
    }

    /**
     * 判断是否取消该请求
     * @return cancel 是否取消该请求
     */
    public boolean isCancel() {
        return cancel;
    }

    /**
     * 设置是否取消该请求
     * @param cancel 是否取消该请求
     */
    public void setCancel(boolean cancel) {
        this.cancel = cancel;
    }

    /**
     * 获取请求的附属数据（参数）
     * @return data 请求的附属数据（参数）
     */
    public Object getData() {
        return data;
    }

    /**
     * 设置请求的附属数据（参数）
     * @param data 请求的附属数据（参数）
     */
    public void setData(Object data) {
        this.data = data;
    }

    /**
     * 获取处理结果回调监听器
     * @return responseListener 处理结果回调监听器
     */
    public ResponseListener getResponseListener() {
        return responseListener;
    }

    /**
     * 设置处理结果回调监听器
     * @param responseListener 处理结果回调监听器
     */
    public void setResponseListener(ResponseListener responseListener) {
        this.responseListener = responseListener;
    }

    /**
     * 获取超时时间
     * @return
     */
	public int getTimeoutMs() {
		return timeoutMs;
	}

	/**
	 * 设置超时时间
	 * @param timeoutMs
	 */
	public void setTimeoutMs(int timeoutMs) {
		this.timeoutMs = timeoutMs;
	}

	/**
	 * 获取尝试连接最大次数
	 * @return
	 */
	public int getMaxRetries() {
		return maxRetries;
	}

	/**
	 * 设置尝试连接最大次数
	 * @param maxRetries
	 */
	public void setMaxRetries(int maxRetries) {
		this.maxRetries = maxRetries;
	}
    
}
