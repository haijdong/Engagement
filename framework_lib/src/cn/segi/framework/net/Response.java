/*
 * 文件名：Response.java
 * 创建人：liangzx
 * 创建时间：2015-10-16
 */
package cn.segi.framework.net;

import com.android.volley.VolleyError;

/**
 * 请求对应的响应结果<BR>
 * 
 * @author liangzx
 * @version [2015-10-16] 
 */
public class Response {
	/**
	 * volley错误信息
	 */
	private VolleyError vollyError;
	
    /**
     * 处理请求返回的结果码
     */
    private int resultCode;
    
    /**
     * 结果描述(此字段主要对应于服务器接口的resultDesc字段)
     */
    private String resultDesc;
    
    /**
     * 返回数据
     */
    private Object resultData;

	/**
     * 获取处理请求返回的HTTP错误信息
     * @return vollyError 处理请求返回的HTTP错误信息
     */
    public VolleyError getVolleyError() {
    	return vollyError;
    }
    
    /**
     * 设置处理请求返回的HTTP错误信息
     * @param vollyError 处理请求返回的HTTP错误信息
     */
    public void setVolleyError(VolleyError vollyError) {
    	this.vollyError = vollyError;
    }

    /**
     * 获取处理请求返回的结果码
     * @return resultCode 处理请求返回的结果码
     */
    public int getResultCode() {
        return resultCode;
    }

    /**
     * 设置处理请求返回的结果码
     * @param resultCode 处理请求返回的结果码
     */
    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    /**
     * 获取结果描述(此字段主要对应于服务器接口的resultDesc字段)
     * @return resultDesc 结果描述(此字段主要对应于服务器接口的resultDesc字段)
     */
    public String getResultDesc() {
        return resultDesc;
    }

    /**
     * 设置结果描述(此字段主要对应于服务器接口的resultDesc字段)
     * @param resultDesc 结果描述(此字段主要对应于服务器接口的resultDesc字段)
     */
    public void setResultDesc(String resultDesc) {
        this.resultDesc = resultDesc;
    }

    /**
     * 获取返回数据
     * @return resultData 返回数据
     */
    public Object getResultData() {
        return resultData;
    }

    /**
     * 设置返回数据
     * @param resultData 返回数据
     */
    public void setResultData(Object resultData) {
        this.resultData = resultData;
    }
}
