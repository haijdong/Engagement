package cn.segi.framework.net;
/*
 * 文件名：CustomStringRequest.java
 * 创建人：liangzx
 * 创建时间：2015-10-16
 */
import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.apache.http.protocol.HTTP;

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
public class CustomStringRequest extends com.android.volley.Request<String> {

	private final Listener<String> mListener;
	
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
    public CustomStringRequest(int method, String url, Listener<String> listener,
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
    public CustomStringRequest(String url, Listener<String> listener, 
    		ErrorListener errorListener, AbstractRequestProcessor processor, Request request) {
        this(Method.GET, url, listener, errorListener, processor, request);
    }

    @Override
    protected void deliverResponse(String response) {
        mListener.onResponse(response);
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        String parsed;
        try {
            parsed = new String(response.data, HTTP.UTF_8);
        } catch (UnsupportedEncodingException e) {
            parsed = new String(response.data);
        }
        return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));
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
