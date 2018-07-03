package com.tommy.base.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSStsTokenCredentialProvider;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.segi.view.refresh.PullToRefreshBase;
import com.tommy.base.R;
import com.tommy.base.common.model.AliPolicyInfo;
import com.tommy.base.common.view.LanTianProgressDialog;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import cn.segi.framework.activity.BaseFrameworkActivity;
import cn.segi.framework.net.Processor;
import cn.segi.framework.net.Request;
import cn.segi.framework.net.Response;
import cn.segi.framework.util.DoubleClickUtil;

/**
 * 项目每个activity继承此activity
 * 
 * @author liangzx
 * 
 */
public abstract class BaseActivity extends BaseFrameworkActivity {
	private PopupWindow popupWindow;
	protected LanTianProgressDialog mDialog;

	@Override
	public void handleFragmentMessage(String tag, int what, Object obj) {
		// TODO Auto-generated method stub

	}

	@Override
	protected int getSystemBarColor() {
		// TODO Auto-generated method stub
		return R.color.transparent;
	}

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
	public void setLastUpdateTime(PullToRefreshBase refreshView) {
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
	protected void onProcessErrorUiResult(Request request, Response response) {
		// TODO Auto-generated method stub
		super.onProcessErrorUiResult(request, response);
		dismissLoadingDialog();
	}

	public Request processLocalAction(Processor processor, int actionId, Object data) {
		return super.processLocalAction(processor, actionId, data);
	}

	public Request processNetAction(Processor processor, int actionId, Object data) {
		return super.processNetAction(processor, actionId, data);
	}

	/**
	 * 生成请求实体
	 * 
	 * @param actionId
	 * @param data
	 * @return
	 */
	public Request createRequestInfo(int actionId, Object data) {
		Request request = new Request(this.getClass().getName());
		request.setActionId(actionId);
		request.setData(data);
		request.setResponseListener(this);
		return request;
	}

	/**
	 * 用请求实体请求网络接口
	 * 
	 * @param processor
	 * @param request
	 * @return
	 */
	public Request processNetActionByRequest(Processor processor,
                                             Request request) {
		if (null != processor) {
			processor.processNet(request);
		}
		return request;
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



	public void showPopWindow(String title, String describe, @NonNull final BaseAppCallback callback) {
		// 加载popupWindow的布局文件

		String infServie = LAYOUT_INFLATER_SERVICE;
		LayoutInflater layoutInflater = (LayoutInflater)getSystemService(infServie);
		View view = LayoutInflater.from(this).inflate(R.layout.pop_notice, null, false);
		TextView cannal = (TextView) view.findViewById(R.id.cannal);
		TextView iv_commit = (TextView) view.findViewById(R.id.iv_commit);
		TextView mtitle = (TextView) view.findViewById(R.id.title);
		TextView mdescribe = (TextView) view.findViewById(R.id.describe);
		mtitle.setText(title);
		mdescribe.setText(describe);
		cannal.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				popupWindow.dismiss();
				callback.onSuccess();
			}
		});
		iv_commit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				popupWindow.dismiss();
				popupWindow = null;
				callback.onError(0, "");

			}
		});
		popupWindow = new PopupWindow(view, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT, true);
		popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
		popupWindow.setBackgroundDrawable(getResources().getDrawable(R.color.transparent_half));
		popupWindow.setOutsideTouchable(true);
	}

	private void closePopWindow() {
		popupWindow.dismiss();
		popupWindow = null;
	}


	// OSS info
	protected AliPolicyInfo info;
	protected OSSCredentialProvider credentialProvider;
	protected OSS oss;
	/**
	 * 阿里云OSS上传（默认是异步多文件上传）
	 * @param urls
	 * @author LJH
	 */
	protected boolean ossUpload(final List<String> urls, final List<String> ossUrls) {
		if (info == null) {
			show("OSS令牌没有初始化");
			return false;
		}
		if (oss == null) {
			// 明文设置AccessKeyId/AccessKeySecret的方式建议只在测试时使用
			credentialProvider = new OSSStsTokenCredentialProvider(info.getAccessKeyId(), info.AccessKeySecret, info.getSecurityToken());
			oss = new OSSClient(getApplicationContext(), info.host, credentialProvider);
		}

		if (urls.size() <= 0) {
			// 文件全部上传完毕，这里编写上传结束的逻辑，如果要在主线程操作，最好用Handler或runOnUiThead做对应逻辑
			return true;// 这个return必须有，否则下面报越界异常，原因自己思考下哈
		}
		final String url = urls.get(0);
		if (TextUtils.isEmpty(url)) {
			urls.remove(0);
			// url为空就没必要上传了，这里做的是跳过它继续上传的逻辑。
			return ossUpload(urls, ossUrls);
		}

		File file = new File(url);
		if (null == file || !file.exists()) {
			urls.remove(0);
			// 文件为空或不存在就没必要上传了，这里做的是跳过它继续上传的逻辑。
			return ossUpload(urls, ossUrls);
		}
		// 文件后缀
		String fileSuffix = "";
		if (file.isFile()) {
			// 获取文件后缀名
			fileSuffix = file.getName().substring(file.getName().lastIndexOf("."));
		}
		// 文件标识符objectKey， 生成UUID
		UUID uuid = UUID.randomUUID();
		String uniqueId = uuid.toString();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String format1 = format.format(new Date());
		final String objectKey =  info.dir + format1 + "/" + uniqueId + fileSuffix;
		// 下面3个参数依次为bucket名，ObjectKey名，上传文件路径
		PutObjectRequest put = new PutObjectRequest(info.bucket, objectKey, url);

		// 设置进度回调
		put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
			@Override
			public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
				// 进度逻辑
			}
		});
		//SendMessageRequest request = new SendMessageRequest();
		// 异步上传
		// 异步上传
		OSSAsyncTask task = oss.asyncPutObject(put,
				new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
					@Override
					public void onSuccess(PutObjectRequest request, PutObjectResult result) { // 上传成功
						urls.remove(0);
						ossUrls.add("http://" + info.bucket + '.' + info.host + "/" + objectKey);
						if (ossUpload(urls, ossUrls)) {
							ossUploadSuccess(ossUrls);
						}
					}

					@Override
					public void onFailure(PutObjectRequest request, ClientException clientExcepion,
										  ServiceException serviceException) { // 上传失败
						dismissLoadingDialog();
						// 请求异常
						if (clientExcepion != null) {
							// 本地异常如网络异常等
//                            clientExcepion.printStackTrace();
							show("图片上传网络异常");
						}
						if (serviceException != null) {
							// 服务异常
//                            Log.e("ErrorCode", serviceException.getErrorCode());
//                            Log.e("RequestId", serviceException.getRequestId());
//                            Log.e("HostId", serviceException.getHostId());
//                            Log.e("RawMessage", serviceException.getRawMessage());
							show("图片上传服务异常");
						}
					}
				});
		// task.cancel(); // 可以取消任务
		// task.waitUntilFinished(); // 可以等待直到任务完成
		return false;
	}

	/**
	 * 阿里云OSS上传回调
	 * @param ossUrls
	 * @author LJH
	 */
	protected void ossUploadSuccess(final List<String> ossUrls) {
	}
}
