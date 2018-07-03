package com.tommy.base.base;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.segi.view.alert.T;
import com.tommy.base.R;
import com.tommy.base.common.view.LanTianProgressDialog;

import cn.segi.framework.application.BaseApplication;
import cn.segi.framework.net.Processor;
import cn.segi.framework.net.Request;
import cn.segi.framework.net.Response;
import cn.segi.framework.net.ResponseListener;
import cn.segi.framework.util.DoubleClickUtil;


/**
 * 可以网络请求的popupwindow
 * 
 * @author liangzx
 * 
 */
public abstract class BaseNetRequestPopupWindow extends PopupWindow implements
		ResponseListener {

	private LanTianProgressDialog mDialog;

	public BaseNetRequestPopupWindow(Context context) {
		super(context);
		setAnimationStyle(R.style.AnimBottom2);
		setWidth(LayoutParams.MATCH_PARENT);
		setHeight(LayoutParams.MATCH_PARENT);
		setFocusable(true);
		ColorDrawable dw = new ColorDrawable(context.getResources().getColor(R.color.transparent_half));
		setBackgroundDrawable(dw);
		setClippingEnabled(false);
	}

	/**
	 * 是否重复提交
	 * @return
	 */
	public boolean isDoubleRequest(){
		setLoadingDialogCanelable(false);
		return DoubleClickUtil.isMuitClick();
	}

	@Override
	public void dismiss() {
		// TODO Auto-generated method stub
		super.dismiss();
		dismissLoadingDialog();
		mDialog = null;
	}

	protected Request processAction(Processor processor, int actionId,
			Object data) {
		Request request = new Request(this.getClass().getName());
		request.setActionId(actionId);
		request.setData(data);
		request.setResponseListener(this);
		processor.processNet(request);
		return request;
	}

	protected Request processLocalAction(Processor processor, int actionId,
			Object data) {
		Request request = new Request(this.getClass().getName());
		request.setActionId(actionId);
		request.setData(data);
		request.setResponseListener(this);
		processor.processLocal(request);
		return request;
	}

	@Override
	public void onProcessResult(final Request request, final Response response) {
		// TODO Auto-generated method stub
		new Handler(Looper.getMainLooper()).post(new Runnable() {
			@Override
			public void run() {
				onProcessUiResult(request, response);
			}
		});
	}

	@Override
	public void onProcessErrorResult(final Request request,
			final Response response) {
		new Handler(Looper.getMainLooper()).post(new Runnable() {
			@Override
			public void run() {
				onProcessErrorUiResult(request, response);
			}
		});
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
		dismissLoadingDialog();
		VolleyError error = response.getVolleyError();
		if (error instanceof NetworkError) {
			T.show(BaseApplication.getContext(), "网络请求失败，请稍后重试");
		} else if (error instanceof ServerError) {
			T.show(BaseApplication.getContext(), "服务忙，请稍后再试");
		} else if (error instanceof AuthFailureError) {
			T.show(BaseApplication.getContext(), "授权失败");
		} else if (error instanceof ParseError) {
			T.show(BaseApplication.getContext(), "数据解析失败");
		} else if (error instanceof NoConnectionError) {
			T.show(BaseApplication.getContext(), "客户端无有效网络连接");
		} else if (error instanceof TimeoutError) {
			T.show(BaseApplication.getContext(), "网络请求超时");
		} else {
			T.show(BaseApplication.getContext(), "网络请求失败，请稍后重试");
		}
	}

	/**
	 * 处理数据
	 * 
	 * @param request
	 * @param response
	 */
	protected abstract void onProcessUiResult(Request request, Response response);

	/***
	 * 创建loading页
	 * @param context
	 * @param canCloseable
	 * @param msgId
	 */
	protected void createLoadingDialog(Context context, boolean canCloseable, int msgId) {
		if (isLoadingDialogShowing()) {
			mDialog.dismiss();
		}
		mDialog = new LanTianProgressDialog(context, canCloseable, msgId);
	}
	
	/***
	 * 创建loading页
	 * @param context
	 * @param canCloseable
	 * @param msg
	 */
	protected void createLoadingDialog(Context context, boolean canCloseable, String msg) {
		if (isLoadingDialogShowing()) {
			mDialog.dismiss();
		}
		mDialog = new LanTianProgressDialog(context, canCloseable, msg);
	}
	
	/**
	 * 显示loading页
	 */
	protected void showLoadingDialog() {
		if (null != mDialog && !mDialog.isShowing()) {
			mDialog.show();
		}
	}
	
	/**
	 * 关闭弹窗
	 */
	protected void dismissLoadingDialog() {
		if (isLoadingDialogShowing()) {
			mDialog.dismiss();
		}
	}
	
	/**
	 * 设置弹窗提示信息
	 * @param msg
	 */
	protected void setLoadingDialogMessage(String msg) {
		if (null != mDialog) {
			mDialog.setMessage(msg);
		}
	}
	
	/**
	 * 设置弹窗提示信息
	 * @param msg
	 */
	protected void setLoadingDialogMessage(int msg) {
		if (null != mDialog) {
			mDialog.setMessage(msg);
		}
	}
	
	/**
	 * 设置弹窗是否能关闭
	 * @param cancelable
	 */
	protected void setLoadingDialogCanelable(boolean cancelable) {
		if (null != mDialog) {
			mDialog.setCancelable(cancelable);
		}
	}
	
	/**
	 * 获取弹窗是否正在显示
	 * @return
	 */
	protected boolean isLoadingDialogShowing() {
		if (null != mDialog) {
			return mDialog.isShowing();
		}
		return false;
	}

	/**
	 * 适配7.0系统显示弹窗问题
	 * @param window
	 * @param parent
	 * @param offsetY
	 */
	public static void adapterApiV24ForShowAsDropDown(PopupWindow window, View parent, int offsetY) {
		if (null == window) {
			return;
		}
		if (null == parent) {
			return;
		}
		if(Build.VERSION.SDK_INT >= 24){
			Rect visibleFrame = new Rect();
			parent.getGlobalVisibleRect(visibleFrame);
			int height = parent.getResources().getDisplayMetrics().heightPixels - visibleFrame.bottom + Math.abs(offsetY);
			window.setHeight(height);
		}
		window.showAsDropDown(parent, 0, offsetY);
	}

	@Override
	public void showAsDropDown(View anchor) {
		if(Build.VERSION.SDK_INT >= 24){
			Rect visibleFrame = new Rect();
			anchor.getGlobalVisibleRect(visibleFrame);
			int height = anchor.getResources().getDisplayMetrics().heightPixels - visibleFrame.bottom;
			setHeight(height);
		}
		super.showAsDropDown(anchor);
	}

	@Override
	public void showAsDropDown(View anchor, int xoff, int yoff) {
		if(Build.VERSION.SDK_INT >= 24){
			Rect visibleFrame = new Rect();
			anchor.getGlobalVisibleRect(visibleFrame);
			int height = anchor.getResources().getDisplayMetrics().heightPixels - visibleFrame.bottom + Math.abs(yoff);
			setHeight(height);
		}
		super.showAsDropDown(anchor, xoff, yoff);
	}

	/**
	 * 获取上下文
	 * @return
	 */
	public Context getContext() {
		if (null != getContentView()) {
			return getContentView().getContext();
		}
		return null;
	}

	/**
	 * 获取状态栏高度
	 * @return
	 */
	public static int getStatusBarHeight()
	{
		try
		{
			Resources resource = BaseApplication.getContext().getResources();
			int resourceId = resource.getIdentifier("status_bar_height", "dimen", "Android");
			if (resourceId != 0)
			{
				return resource.getDimensionPixelSize(resourceId);
			}
		} catch (Exception e)
		{
		}
		return 0;
	}

	public static int getViewY(View v) {
		int[] location = new int[2];
		v.getLocationOnScreen(location);
		return location[1];
	}
}
