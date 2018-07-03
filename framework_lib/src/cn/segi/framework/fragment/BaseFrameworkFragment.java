package cn.segi.framework.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

import cn.segi.framework.net.Processor;
import cn.segi.framework.net.Request;
import cn.segi.framework.net.Response;
import cn.segi.framework.net.ResponseListener;

/**
 * fragment框架类
 * @author liangzx
 * @version 1.0
 */
public abstract class BaseFrameworkFragment extends Fragment implements ResponseListener{
	/**
     * 与Activity共享事件的回调接口
     */
    private FragmentMessageListener mFragmentMessageListener;
    /**
     * 总视图
     */
    protected View mView;
    
    /**
     * 转换成与Activity交互的回调接口<BR>
     * {@inheritDoc}
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mFragmentMessageListener = (FragmentMessageListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement FragmentMessageListener");
        }
    }
    
    /**
     * 发送消息至与Fragment绑定的Activity<BR>
     * 
     * @param what 消息类型
     * @param obj 传递数据
     */
    protected void sendMessageToActivity(int what, Object obj) {
        if (mFragmentMessageListener != null) {
            mFragmentMessageListener.handleFragmentMessage(getTag(), what, obj);
        }
    }
    
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
    		Bundle savedInstanceState) {
    	initViews();
    	initEvents();
    	initData();
    	return mView;
    }
    
    /** 初始化视图 **/
	protected abstract void initViews();

	/** 初始化事件 **/
	protected abstract void initEvents();
    
	/** 初始化数据 **/
	protected abstract void initData();


	/**
	 * 设置布局
	 * @param layoutId
	 */
	public void setContentView(int layoutId, ViewGroup container) {
		mView = LayoutInflater.from(getActivity()).inflate(layoutId, container);
	}

	/**
	 * 设置布局
	 * @param layoutId
	 */
	public void setContentView(int layoutId) {
		setContentView(layoutId, null);
	}

	/**
	 * 查找控件
	 * @param viewId
	 */
	public View findViewById(int viewId) {
		if (null != mView) {
			return mView.findViewById(viewId);
		}
		return null;
	}
	
    /**
     * 网络请求
     * 执行处理特定的动作请求<BR>
     * 不自行创建Request及执行方法, 且回调在本Activity中处理
     * @param processor 执行请求的处理器
     * @param actionId 请求的动作ID
     * @param data 请求的参数
     * @return 返回请求对象
     */
    protected Request processNetAction(Processor processor, int actionId, Object data) {
        return processAction(processor, actionId, data, this, 1);
    }
    
    /**
     * 本地请求
     * 执行处理特定的动作请求<BR>
     * 不自行创建Request及执行方法, 且回调在本Activity中处理
     * @param processor 执行请求的处理器
     * @param actionId 请求的动作ID
     * @param data 请求的参数
     * @return 返回请求对象
     */
    protected Request processLocalAction(Processor processor, int actionId, Object data) {
    	return processAction(processor, actionId, data, this, 2);
    }
    
    /**
	 * 执行处理特定的动作请求
	 * 
	 * @param processor
	 *            执行请求的处理器
	 * @param actionId
	 *            请求的动作ID
	 * @param data
	 *            请求的参数
	 * @param responseListener
	 * 
	 * @param type 请求类型（1网络，2本地）
	 *            回调接口
	 * @return 返回请求对象
	 */
	protected Request processAction(Processor processor, int actionId,
			Object data, ResponseListener responseListener, int type) {
		Request request = new Request(this.getClass().getName());
		request.setActionId(actionId);
		request.setData(data);
		request.setResponseListener(responseListener);
		if (type == 1) {
			processor.processNet(request);
		}else{
			processor.processLocal(request);
		}
		return request;
	}
    
    /**
     * 取消执行特定的请求
     * 
     * @param processor 执行该请求的处理器
     * @param tag 已执行的请求
     */
    protected void cancelRequest(Processor processor, String tag) {
        processor.cancel(tag);
    }

    
    /**
     * 界面发出的请求(processAction方法)执行结果回调, 此方法一般由处理器的执行线程回调<BR>
     * 此方法默认回调主线程中处理的方法onProcessUiResult, 可根据具体实现重写此方法<BR>
     * {@inheritDoc}
     */
    @Override
    public void onProcessResult(final Request request, final Response response) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    onProcessUiResult(request, response);
                }
            });            
        }
    }
    
    /**
	 * 界面发出的请求(processAction方法)执行结果回调, 此方法一般由处理器的执行线程回调<BR>
	 * 此方法默认回调主线程中处理的方法onProcessUiResult, 若需要回调到onProcessUiResult, 务必调用super<BR>
	 * 可根据具体实现重写此方法
	 * <p>
	 * {@inheritDoc}
	 */
	@Override
	public void onProcessErrorResult(final Request request, final Response response) {
		if (getActivity() != null) {
			getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					onProcessErrorUiResult(request, response);
				}
			});
		}
	}
    
    
    /**
	 * 处理结果回调到UI中处理
	 * 
	 * @param request
	 *            请求
	 * @param response
	 *            相应的处理结果
	 */
	protected void onProcessUiResult(final Request request,
			final Response response) {

	}
	
	/**
	 * 处理结果回调到UI中处理
	 * 
	 * @param request
	 *            请求
	 * @param response
	 *            相应的处理结果
	 */
	protected void onProcessErrorUiResult(Request request, Response response) {
		VolleyError error = response.getVolleyError();
		if(error instanceof NetworkError) {
			show("网络请求失败，请稍后重试");
		} else if( error instanceof ServerError) {
			show("服务忙，请稍后再试");
		} else if( error instanceof AuthFailureError) {
			show("授权失败");
		} else if( error instanceof ParseError) {
			show("数据解析失败");
		} else if( error instanceof NoConnectionError) {
			show("客户端无有效网络连接");
		} else if( error instanceof TimeoutError) {
			show("网络请求超时");
		} else{
			show("网络请求失败，请稍后重试");
		}
	}
    
    /**
     * 取消结果回调到UI中处理
     * @param request 请求
     * @param isCancel 是否取消成功
     */
    protected void onCancelUiResult(final Request request, final boolean isCancel) {
        
    }

    /**
     * 与Activity共享事件的回调接口<BR>
     * 
     * @author liangzx
     * @version [segi, 2013-6-26] 
     */
    public interface FragmentMessageListener {
        /**
         * 处理Fragment发送的消息
         * @param tag Fragment的tag
         * @param what 消息类型
         * @param obj 传递数据
         */
        public void handleFragmentMessage(String tag, int what, Object obj);
    }
    
    
    /**
     * 显示toast
     * @param desc
     */
    protected void show(String desc)
    {
    	Toast.makeText(getActivity(), desc, Toast.LENGTH_SHORT).show();
    }

    /**
     * 显示toast
     * @param desc
     */
    protected void show(CharSequence desc)
    {
    	Toast.makeText(getActivity(), desc, Toast.LENGTH_SHORT).show();
    }

    /**
     * 显示toast
     * @param id
     */
    protected void show(int id)
    {
    	Toast.makeText(getActivity(), id, Toast.LENGTH_SHORT).show();
    }
    
   
    @Override
    public void onResume() {
    	// TODO Auto-generated method stub
    	super.onResume();
    }
    @Override
    public void onPause() {
    	// TODO Auto-generated method stub
    	super.onPause();
    }
    
    @Override
    public void onDestroy() {
    	// TODO Auto-generated method stub
    	super.onDestroy();
    }
    
    /** 
     * 通过Class跳转界面 
     **/
	protected void startActivity(Class<?> cls) {
		startActivity(cls, null);
	}

	/** 含有Bundle通过Class跳转界面 **/
	protected void startActivity(Class<?> cls, Bundle bundle) {
		Intent intent = new Intent();
		intent.setClass(getActivity(), cls);
		if (bundle != null) {
			intent.putExtras(bundle);
		}
		startActivity(intent);
	}

	/** 通过Action跳转界面 **/
	protected void startActivity(String action) {
		startActivity(action, null);
	}

	/** 含有Bundle通过Action跳转界面 **/
	protected void startActivity(String action, Bundle bundle) {
		Intent intent = new Intent();
		intent.setAction(action);
		if (bundle != null) {
			intent.putExtras(bundle);
		}
		startActivity(intent);
	}
}
