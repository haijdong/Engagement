package cn.segi.framework.net;

import com.android.volley.AuthFailureError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.MultipartRequest;

import java.util.Map;

/**
 * 文件上传请求实体类
 * @author liangzx
 * @version 1.0
 */
public class CustomMultipartRequest extends MultipartRequest{

	private HttpMessage mProcessor;
	
	private Request mRequest;
	
	@SuppressWarnings("unchecked")
	public CustomMultipartRequest(String url, ErrorListener errorListener,
			Listener<String> listener, HttpMessage processor, Request request) {
		super(url, errorListener, listener, null);
		mProcessor = processor;
        mRequest = request;
        mParams = request.getData() != null ? (Map<String, String>)request.getData() : null;
        buildMultipartEntity();
	}
	public CustomMultipartRequest(String url, ErrorListener errorListener,
			Listener<String> listener, Map<String, String> params) {
		super(url, errorListener, listener, params);
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
   

}
