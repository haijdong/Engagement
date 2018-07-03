package com.segi.view.webview;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.util.AttributeSet;
import android.webkit.CookieManager;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.Map;
import java.util.Set;


/**
 * 自定义webview 兼容某些机型https协议不能用 之后项目中用webview全部用这个类，如果有特殊需要另行商量
 * 
 * @author leip
 * 
 */
public class CustomWebView extends WebView {
	/**
	 * 对外暴露接口监听接口
	 */
	private WebViewHandlerListener mWebViewHandlerListener;
	
	public final static int FILECHOOSER_RESULTCODE = 3;  
	public final static int FILECHOOSER_RESULTCODE_FOR_ANDROID_5 = 4;  
	private Context mContext;
	private ValueCallback<Uri> mUploadMessage;  
	private ValueCallback<Uri[]> mUploadMessageForSdk5;  

	public void setWebViewHandlerListener(
			WebViewHandlerListener mWebViewHandlerListener) {
		this.mWebViewHandlerListener = mWebViewHandlerListener;
	}

	public CustomWebView(Context context, AttributeSet attrs, int defStyle,
			boolean privateBrowsing) {
		super(context, attrs, defStyle, privateBrowsing);
		this.mContext = context;
		webViewSetting();
	}

	public CustomWebView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.mContext = context;
		webViewSetting();
	}

	public CustomWebView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		webViewSetting();
	}

	/**
	 * webview 设置
	 */
	private void webViewSetting() {
		clearCache(true);
		WebSettings settings = getSettings();
		settings.setCacheMode(WebSettings.LOAD_DEFAULT);
		settings.setJavaScriptEnabled(true);
		settings.setDomStorageEnabled(true);
		settings.setGeolocationEnabled(true);
		settings.setRenderPriority(RenderPriority.HIGH);
		settings.setUserAgentString(settings.getUserAgentString() + " " + getContext().getPackageName());
		setHorizontalScrollBarEnabled(false);
		settings.setAllowFileAccess(true);
		settings.setJavaScriptCanOpenWindowsAutomatically(true);
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
			settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
		}
		setWebViewClient(new WebViewClient() {

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				// TODO Auto-generated method stub
				super.onPageStarted(view, url, favicon);
				if(null!=mWebViewHandlerListener){
					mWebViewHandlerListener.onPageStarted(view, url,favicon);
				}
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				// TODO Auto-generated method stub
				super.onPageFinished(view, url);
				if(null!=mWebViewHandlerListener){
					mWebViewHandlerListener.onPageFinished(view, url);
				}
			}

			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				// TODO Auto-generated method stub
				super.onReceivedError(view, errorCode, description, failingUrl);
				if(null!=mWebViewHandlerListener){
					mWebViewHandlerListener.onReceivedError(view, errorCode,description,failingUrl);
				}
			}
            /**
             * 这个方法不对外暴露，默认统一处理
             */
			@Override
			public void onReceivedSslError(WebView view,
					SslErrorHandler handler, SslError error) {
				// TODO Auto-generated method stub
//				super.onReceivedSslError(view, handler, error);
				handler.proceed();//接受证书
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// TODO Auto-generated method stub
				if(null!=mWebViewHandlerListener){
					return mWebViewHandlerListener.shouldOverrideUrlLoading(view,url);
				}
				return super.shouldOverrideUrlLoading(view, url);
			}

		});
		setWebChromeClient(new CustomWebChromeClient());
	}
	
	
	/**
	 * 自定义WebChromeClient，兼容h5 <input type = "file">标签文件上传没反应
	 * @author Administrator
	 *
	 */
	public class CustomWebChromeClient extends WebChromeClient
	{
		CustomWebChromeClient()
		{

		}

		@Override
		public void onProgressChanged(WebView view, int newProgress)
		{
			// TODO Auto-generated method stub
			super.onProgressChanged(view, newProgress);
			if (null != mWebViewHandlerListener)
			{
				mWebViewHandlerListener.onProgressChanged(view, newProgress);
			}
		}

		@Override
		public boolean onJsAlert(WebView view, String url, String message,
				JsResult result)
		{
			// TODO Auto-generated method stub
			if (null != mWebViewHandlerListener)
			{
				return mWebViewHandlerListener.onJsAlert(view, url, message,
						result);
			}
			return super.onJsAlert(view, url, message, result);
		}

		@Override
		public void onReceivedTitle(WebView view, String title)
		{
			// TODO Auto-generated method stub
			super.onReceivedTitle(view, title);
			if (null != mWebViewHandlerListener)
			{
				mWebViewHandlerListener.onReceivedTitle(view, title);
			}
		}

		// For Android 3.0+
		public void openFileChooser(ValueCallback<Uri> uploadMsg,
				String acceptType)
		{
				fileChose(uploadMsg);
		}

		// For Android < 3.0
		public void openFileChooser(ValueCallback<Uri> uploadMsg)
		{
			openFileChooser(uploadMsg, "");
		}

		// For Android > 4.1.1
		public void openFileChooser(ValueCallback<Uri> uploadMsg,
				String acceptType, String capture)
		{
			openFileChooser(uploadMsg, acceptType);
		}

		// For Android > 5.0
		@TargetApi(Build.VERSION_CODES.LOLLIPOP)
		@Override
		public boolean onShowFileChooser(WebView webView,
				ValueCallback<Uri[]> filePathCallback,
				FileChooserParams fileChooserParams)
		{
			fileChoseForSdk5(filePathCallback);
			return true;
		}
		
		private void fileChose(ValueCallback<Uri> uploadMsg)
		{
			// TODO Auto-generated method stub
			    mUploadMessage = uploadMsg;  
			    Intent i = new Intent(Intent.ACTION_GET_CONTENT);  
			    i.addCategory(Intent.CATEGORY_OPENABLE);  
			    i.setType("image/*");  
			    Intent chooseIntent = Intent.createChooser(i, "File Chooser");
			    if(null!=mContext && mContext instanceof Activity){
			    	((Activity)mContext).startActivityForResult(chooseIntent,FILECHOOSER_RESULTCODE);  
			    }
		}
		private void fileChoseForSdk5(ValueCallback<Uri[]> uploadMsg)
		{
			// TODO Auto-generated method stub
			    mUploadMessageForSdk5 = uploadMsg;  
			    Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);  
			    contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);  
			    contentSelectionIntent.setType("image/*");  
			    Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);  
			    chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);  
			    chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser"); 
			    if(null!=mContext && mContext instanceof Activity){
			    	((Activity)mContext).startActivityForResult(chooserIntent,FILECHOOSER_RESULTCODE_FOR_ANDROID_5);  
			    }
		}
	}
	/**
	 * 5.0之前的回调
	 * @param data
	 * @param uploadMsg
	 */
	public void onActivityResultForFileChoose(Intent data,int resultCode)
	{
		if (null != mUploadMessage)
		{
			if (Activity.RESULT_OK == resultCode)
			{
				Uri result = (data == null) ? null : data.getData();
				mUploadMessage.onReceiveValue(result);
			} else
			{
				mUploadMessage.onReceiveValue(null);
			}
			mUploadMessage = null;
		}
	}
	/**
	 * 5.0系统之后的回调
	 * @param data
	 * @param uploadMsg
	 */
	public void onActivityResultForFileChooseFor5(Intent data,int resultCode)
	{
		if (null != mUploadMessageForSdk5)
		{
			if (Activity.RESULT_OK == resultCode)
			{
				Uri result = (data == null) ? null : data.getData();
				if (result != null)
				{
					mUploadMessageForSdk5.onReceiveValue(new Uri[] { result });
				} else
				{
					mUploadMessageForSdk5.onReceiveValue(new Uri[] {});
				}
			} else
			{
				mUploadMessageForSdk5.onReceiveValue(null);
			}
			mUploadMessageForSdk5 = null;
		}
	}
	

	/**
	 * 对外回调接口
	 * 
	 * @author Administrator
	 * 
	 */
	public interface WebViewHandlerListener {
		
		public void onProgressChanged(WebView view, int newProgress);

		public boolean shouldOverrideUrlLoading(WebView view, String url);

		public void onPageFinished(WebView view, String url);

		public void onPageStarted(WebView view, String url, Bitmap favicon);

		public void onReceivedError(WebView view, int errorCode, String description, String failingUrl);
		
		public boolean onJsAlert(WebView view, String url, String message,JsResult result);
		
		public void onReceivedTitle(WebView view, String title);
	}

	/**
	 * 设置cookie，针对需要cookie的webview可以调用该方法设置
	 * @param cookiesMap
	 * @param url 需要设置cookie的url
	 */
	public void setCookies(Map<String,String> cookiesMap,String url){
		CookieManager.getInstance().setAcceptCookie(true);
		CookieManager.getInstance().removeSessionCookie();
		if(null!=cookiesMap){
			Set<Map.Entry<String, String>> entrySet = cookiesMap.entrySet();
			for (Map.Entry<String, String> entry : entrySet) {
				CookieManager.getInstance().setCookie(url,entry.getKey()+"="+entry.getValue());
			}
		}
	}

}
