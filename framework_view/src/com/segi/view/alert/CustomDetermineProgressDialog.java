package com.segi.view.alert;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.FrameLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import cn.segi.framework.util.WindowDispaly;

import com.segi.view.R;


public class CustomDetermineProgressDialog extends Dialog {

	/**
	 * 当前进度
	 */
	private int mProgress = 0;
	/**
	 * 进度提示
	 */
	private TextView mTips;
	/**
	 * 进度条
	 */
	private ProgressBar mProgressBar;

	public CustomDetermineProgressDialog(Context context) {
		super(context, R.style.CustomDialog);
		initView();
	}

	protected CustomDetermineProgressDialog(Context context,
			boolean cancelable, OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		initView();
	}

	public CustomDetermineProgressDialog(Context context, int theme) {
		super(context, theme);
		initView();
	}

	private void initView() {
		setContentView(R.layout.view_progress_dialog);
		mTips = (TextView) findViewById(R.id.tip);
		mProgressBar = (ProgressBar) findViewById(R.id.progress);
		mProgressBar.setMax(100);

		View bg = findViewById(R.id.bg);
		LayoutParams bgParams = new LayoutParams((int) (WindowDispaly.getWidth() * 0.72), LayoutParams.WRAP_CONTENT);
		bg.setLayoutParams(bgParams);

		LinearLayout.LayoutParams barParams = (android.widget.LinearLayout.LayoutParams) mProgressBar.getLayoutParams();
		barParams.width = (int) (WindowDispaly.getWidth() * 0.45);
		mProgressBar.setLayoutParams(barParams);
		setCancelable(false);
	}
	

	/**
	 * 设置进度
	 * 
	 * @param progress
	 */
	public void setProgress(int progress) {
		mProgress = progress;
		if (null != mProgressBar) {
			mProgressBar.setProgress(progress);
		}
		if (null != mTips) {
			mTips.setText(String.format(getContext().getString(R.string.progress_tip), progress + "%"));
		}
	}

	/**
	 * 设置提示内容
	 * @param tip
	 */
	public void setContent(String tip)
	{
		if (null != mTips) {
			mTips.setText(tip);
		}
	}

	/**
	 * 返回进度
	 * 
	 * @return
	 */
	public int getProgress() {
		return mProgress;
	}

}
