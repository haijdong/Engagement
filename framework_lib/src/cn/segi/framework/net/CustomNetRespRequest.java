package cn.segi.framework.net;
/*
 * 文件名：CustomStringRequest.java
 * 创建人：liangzx
 * 创建时间：2015-10-16
 */
import java.util.Map;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;

/**
 * 自定义StringRequest<BR>
 * 与AbstractRequestProcessor进行绑定
 * @author liangzx
 *
 */
public class CustomNetRespRequest extends com.android.volley.Request<NetworkResponse> {

	private final Listener<NetworkResponse> mListener;
	
	private HttpMessage mProcessor;
	
	private Request mRequest;

	/**
     * Creates a new mRequest with the given method.
     *
     * @param method the mRequest {@link Method} to use
     * @param url URL to fetch the string at
     * @param listener Listener to receive the String response
     * @param errorListener Error listener, or null to ignore errors
     */
    public CustomNetRespRequest(int method, String url, Listener<NetworkResponse> listener,
            ErrorListener errorListener, HttpMessage processor, Request request) {
        super(method, url, errorListener);
        mListener = listener;
        mProcessor = processor;
        mRequest = request;
    }
    /**
     * Creates a new GET mRequest.
     *
     * @param url URL to fetch the string at
     * @param listener Listener to receive the String response
     * @param errorListener Error listener, or null to ignore errors
     */
    public CustomNetRespRequest(String url, Listener<NetworkResponse> listener, 
    		ErrorListener errorListener, AbstractRequestProcessor processor, Request request) {
        this(Method.GET, url, listener, errorListener, processor, request);
    }

    @Override
    protected void deliverResponse(NetworkResponse response) {
        mListener.onResponse(response);
    }

    @Override
    protected Response<NetworkResponse> parseNetworkResponse(NetworkResponse response) {
        return Response.success(response, HttpHeaderParser.parseCacheHeaders(response));
    }
	
    @Override
	public byte[] getBody() throws AuthFailureError {
		// TODO Auto-generated method stub
		byte[] body = mProcessor.getBody(mRequest.getActionId(), mRequest.getData());
		if (body != null) {
			return body;
		}
		return super.getBody();
	}
	
	@Override
	public Map<String, String> getHeaders()
			throws AuthFailureError {
		// TODO Auto-generated method stub
		return mProcessor.getHeaders(mRequest.getActionId(), mRequest.getData());
	}
	
	@Override
	protected Map<String, String> getParams()
			throws AuthFailureError {
		// TODO Auto-generated method stub
		if (null != mRequest.getData()) {
			return mProcessor.getParams(mRequest.getActionId(), mRequest.getData());
		}else{
			return null;
		}
	}
	
	@Override
	public String getBodyContentType() {
		// TODO Auto-generated method stub
		return mProcessor.getBodyContentType(mRequest.getActionId());
	}
	
	
}
