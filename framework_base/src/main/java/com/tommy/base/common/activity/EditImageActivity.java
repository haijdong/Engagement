package com.tommy.base.common.activity;

import android.content.Context;

import com.segi.view.alert.CustomProgressDialog;
import com.segi.view.pick.BaseEditImageActivity;
import com.tommy.base.R;


/**
 * 编辑图片
 *
 * @author liangzx
 * @version 1.0
 * @time 2017-11-07 19:06
 **/
public class EditImageActivity extends BaseEditImageActivity {

    protected CustomProgressDialog mDialog;


    @Override
    protected int getSystemBarColor() {
        return R.color.transparent_half_80;
    }

    @Override
    public void showDialog(String tip) {
        createLoadingDialog(this, false, tip);
        showLoadingDialog();
    }

    @Override
    public void dimissDialog() {
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
        mDialog = new CustomProgressDialog(context, canCloseable, msgId);
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
        mDialog = new CustomProgressDialog(context, canCloseable, msg);
    }

    /**
     * 显示loading页
     */
    protected void showLoadingDialog() {
        if (null != mDialog && !mDialog.isShowing() && !isFinishing()) {
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
