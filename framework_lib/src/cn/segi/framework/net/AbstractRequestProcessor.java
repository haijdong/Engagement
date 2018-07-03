/*
 * 文件名：AbstractRequestProcessor.java
 * 创建人：liangzx
 * 创建时间：2015-10-16
 */
package cn.segi.framework.net;

import android.text.TextUtils;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;

import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Executor;

import cn.segi.framework.application.BaseApplication;
import cn.segi.framework.executor.ExecutorSupport;
import cn.segi.framework.grayredirect.GrayRedirectUtil;
import cn.segi.framework.log.Logger;
import cn.segi.framework.util.PrintObjectData;


/**
 * 请求处理器<BR>
 * 将请求都提交到线程池中去执行
 * 
 * @author liangzx
 * @version [2015-10-16] 
 */
public abstract class AbstractRequestProcessor implements Processor, HttpMessage
{    
    
    /**
     * 线程池
     */
    private final Executor mExecutor;
    
    /**
     * 构造函数
     */
    protected AbstractRequestProcessor() {
        if (ExecutorSupport.getExecutor() == null) {
            throw new IllegalArgumentException("Executor is null");
        }
        mExecutor = ExecutorSupport.getExecutor();
    }
    

    /**
     * 直接调用volley构建网络连接运行的Runnable, 默认认为Request中的actionId就是Http请求ID
     * @param request 请求
     */
    @Override
    public void processNet(final Request request) {
    	getProcessRunnable(request);
    }
    
    /**
     * 获取用于线程执行的Runable<BR>
     * @param request 请求
     * @return 返回执行的Runable
     */
    @Override
    public void processLocal(final Request request) {
    	// TODO Auto-generated method stub
    	Runnable runnable = new Runnable() {
			@Override
			public void run() {
				getProcessRunnable(request);
			}
		};
        mExecutor.execute(runnable);
    }
    
    /**
     * 将相应请求的取消结果监听器放置Map中<BR>
     * {@inheritDoc}
     */
    @Override
    public void cancel(String tag) {
    	RequestQueue requestQueue = BaseApplication.getRequestQueue();
    	if (null != requestQueue && !TextUtils.isEmpty(tag)) {
    		requestQueue.cancelAll(tag);
		}
    }
    
    /**
     * 用于分发任务，无论是本地请求还是网络请求<BR>
     * 用request.getActionId() 进行任务分类分发 
     * @param request
     */
    protected abstract void getProcessRunnable(Request request);
    
    
    /**
     * 调用volley的StringRequest请求网络<BR>
     * 
     * @param request 请求参数
     */
	protected void connect(final Request request)
    {
    	String url = getUrl(request.getActionId(), request.getData());
    	int method = getMethod(request.getActionId(), request.getData());
    	if ((method != Method.POST && method != Method.PUT && method != Method.PATCH && method != Method.UPLOAD)) {
    		String params = PrintObjectData.printRequestData(request.getData());
    		if (!TextUtils.isEmpty(params)) {
    			url += params;
			}
    	}
    	if (BaseApplication.IS_DEBUGGER) {
    		Logger.d(AbstractRequestProcessor.class.getName(), "Request's ActionId:" + request.getActionId() + ",Url:" + url);
    		if (Method.GET != method) 
    			Logger.d(AbstractRequestProcessor.class.getName(), "Request's Params:"+ PrintObjectData.printRequestDataForLog(request.getData())) ;
		}
    	final RequestQueue requestQueue = BaseApplication.getRequestQueue();
    	
    	com.android.volley.Request<String> volleyRequest = null;
    	if (method == Method.UPLOAD) {
    		volleyRequest = new CustomMultipartRequest(url
    				, new ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError error) {
							Response response = new Response();
							response.setVolleyError(error);
							fireErrorResult(request, response);
						}
					}
    				, new Listener<String>() {
						@Override
						public void onResponse(final String responseStr) {
							Runnable runnable = new Runnable() {
								@Override
								public void run() {
									Response response = new Response();
									processHttpOk(request, responseStr, response);
									fireResult(request, response);
								}
							};
					        mExecutor.execute(runnable);
						}
					}, this, request);
			volleyRequest.setRetryPolicy(new DefaultRetryPolicy(request.getTimeoutMs()
					, request.getMaxRetries()
					, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
			volleyRequest.setTag(request.getRequestTag());
			volleyRequest.setShouldCache(false);
			requestQueue.add(volleyRequest);
		}else{

			final CustomNetRespRequest respRequest = new CustomNetRespRequest(method
					, url
					, new com.android.volley.Response.Listener<NetworkResponse>() {
				@Override
				public void onResponse(final NetworkResponse newResponse) {
					String respResult;
					try {
						respResult = new String(newResponse.data, HTTP.UTF_8);
					} catch (UnsupportedEncodingException e) {
						respResult = new String(newResponse.data);
					}
					Response response = new Response();
					String cookie = newResponse.headers.get("Set-Cookie");
					try {
						if(null != cookie && cookie != ""){
							JSONObject jsonObject = new JSONObject(cookie);
							Iterator<String> iterator = jsonObject.keys();
							while (iterator.hasNext()) {
								String name = iterator.next();
								GrayRedirectUtil.putCookie(name, jsonObject.optString(name));
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					processHttpOk(request, respResult, response);
					fireResult(request, response);
				}
			}, new com.android.volley.Response.ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError error) {
						Response response = new Response();
						response.setVolleyError(error);
						String responseStr;
						try {
							responseStr = new String(error.networkResponse.data, HTTP.UTF_8);
						} catch (UnsupportedEncodingException e) {
							responseStr = new String(error.networkResponse.data);
						} catch (Exception e) {
							responseStr = null;
						}
						processHttpError(request, responseStr, response);
						fireErrorResult(request, response);
                }
			}, this, request);
			respRequest.setRetryPolicy(new DefaultRetryPolicy(request.getTimeoutMs()
					, request.getMaxRetries()
					, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
			respRequest.setTag(request.getRequestTag());
			respRequest.setShouldCache(false);
			requestQueue.add(respRequest);
		}
    }
    
    
    
    
    /**
     * 获取该Http请求的Url<BR>
     * 
     * @param httpRequestId 该Http请求ID
     * @param requestParams 该Http请求附带参数
     * @return 返回该Http请求的Url
     */
    public String getUrl(int httpRequestId, Object requestParams){
    	return null;
    }

    /**
     * 获取该Http请求的请求方式<BR>
     * 
     * @param httpRequestId 该Http请求ID
     * @param requestParams 该Http请求附带参数
     * @return 返回该Http请求的请求方式
     */
    public int getMethod(int httpRequestId, Object requestParams){
    	return Method.GET;
    }

    /**
     * 获取该Http请求的请求头<BR>
     * 
     * @param request 该Http请求ID
     * @return 返回该Http请求的请求头
     */
    public Map<String, String> getHeaders(Request request){
    	return Collections.emptyMap();
    }
    
    /**
     * 获取该Http请求的请求头<BR>
     * 
     * @param httpRequestId 该Http请求ID
     * @param requestParams 该Http请求附带参数
     * @return 返回该Http请求的请求头
     */
    public Map<String, String> getHeaders(int httpRequestId, Object requestParams){
    	return Collections.emptyMap();
    }
    
    /**
     * 获取该Http请求的请求参数<BR>
     * 
     * @param httpRequestId 该Http请求ID
     * @param requestParams 该Http请求附带参数
     * @return 返回该Http请求的请求参数
     */
    public Map<String, String> getParams(int httpRequestId, Object requestParams){
    	return null;
    }

    /**
     * 获取该Http请求的内容实体<BR>
     * POST或PUT请求才需要用到此对象
     * 
     * @param httpRequestId 该Http请求ID
     * @param requestParams 该Http请求附带参数
     * @return 返回该Http请求的所带内容体
     */
    public byte[] getBody(int httpRequestId, Object requestParams){
    	return null;
    }
    
    /**
     * Returns the content type of the POST or PUT body.
     * @param httpRequestId
     * @return
     */
    public String getBodyContentType(int httpRequestId)
    {
    	return "application/x-www-form-urlencoded; charset=utf-8";
    }
    
    /**
     * 先对网络返回数据进行预处理，再抛向processRespContent进行业务处理
     * @param request 请求
     * @param content Http返回的内容
     * @param response 返回给界面层的Response
     */
    protected abstract void processHttpOk(Request request, String content, Response response);
    
    /**
     * 先对网络返回数据进行预处理，再抛向processRespContent进行业务处理
     * @param request 请求
     * @param content Http返回的内容
     * @param response 返回给界面层的Response
     */
    protected abstract void processHttpError(Request request, String content, Response response);
    
    /**
     * 处理Http返回的内容
     * @param request 请求
     * @param content Http返回的内容
     * @param response 返回给界面层的Response
     */
    protected abstract void processRespContent(Request request, JSONObject content, Response response);
    
    /**
     * 发出Result回调
     * @param request 请求
     * @param response 相应的响应结果
     */
    protected void fireResult(Request request, Response response) {
        ResponseListener respListener = request.getResponseListener();
        if (respListener != null) {
        	respListener.onProcessResult(request, response);
        }
    }
    
    /**
     * 发出Result回调
     * @param request 请求
     * @param response 相应的响应结果
     */
    protected void fireErrorResult(Request request, Response response) {
    	ResponseListener respListener = request.getResponseListener();
    	if (respListener != null) {
    		respListener.onProcessErrorResult(request, response);
    	}
    }

    /**
     * 将httpResp的内容转换成字符串
     * @param httpResp Http请求返回的数据
     * @return 转换成字符串的响应数据
     * @throws IOException 抛出IOException异常
     */
    public String handleResponse(String httpResp) throws IOException {
        if (!TextUtils.isEmpty(httpResp)) { 
            byte[] content = httpResp.getBytes();
            return new String(content, HTTP.UTF_8); 
        } else { 
            return null; 
        } 
    }
    
}
