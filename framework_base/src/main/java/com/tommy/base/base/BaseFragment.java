package com.tommy.base.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.segi.view.refresh.PullToRefreshBase;
import com.tommy.base.common.view.LanTianProgressDialog;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.segi.framework.fragment.BaseFrameworkFragment;
import cn.segi.framework.net.Request;
import cn.segi.framework.net.Response;
import cn.segi.framework.util.DoubleClickUtil;

public abstract class BaseFragment extends BaseFrameworkFragment {

	private LanTianProgressDialog mDialog;

	/**
	 * 是否重复提交
	 * @return
	 */
	public boolean isDoubleRequest(){
		setLoadingDialogCanelable(false);
		return DoubleClickUtil.isMuitClick();
	}

	/**
	 * 设置当前刷新时间
	 * 
	 * @param refreshView
	 */
	@SuppressLint("SimpleDateFormat")
	public void setLastUpdateTime(PullToRefreshBase<ListView> refreshView) {
		String text = new SimpleDateFormat("MM-dd HH:mm").format(new Date());
		refreshView.setLastUpdatedLabel(text);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		dismissLoadingDialog();
		mDialog = null;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (null != mView) {
			ViewGroup parent = (ViewGroup) mView.getParent();
			if (null != parent) {
				mView.destroyDrawingCache();
				parent.removeView(mView);
				mView = null;
			}
		} else {
			initViews();
			initEvents();
			initData();
		}
		return mView;
	}




	@Override
	protected void onProcessErrorUiResult(Request request, Response response) {
		super.onProcessErrorUiResult(request, response);
		dismissLoadingDialog();
	}

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
		if (null != mDialog && !mDialog.isShowing() && null != getActivity() && !getActivity().isFinishing()) {
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
}
