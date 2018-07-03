package com.segi.view.advert;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.view.ViewPager.PageTransformer;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.segi.framework.imagecache.ImageLoaderUtil;

import com.segi.view.R;
import com.segi.view.indicator.CirclePageIndicator;

/**
 * 广告控件
 * @author liangzx
 * @version [liangzx, 2016-11-4]
 */
@SuppressLint("HandlerLeak")
public abstract class BaseAdvertLayout extends LinearLayout implements OnPageChangeListener, OnClickListener{

    private Context mContext = null;
    /**
     * ViewPager
     */
    protected ViewPager mPager;
    /**
     * ViewPager指示器
     */
    protected CirclePageIndicator mIndicator;
    /**
     * ViewPager适配器
     */
    protected AdvertPagerAdapter mAdapter;
    /**
     * 默认图
     */
    protected ImageView mDefaultImg;
    private int mDefaultImgResourceId;
    /**
     * 广告标题
     */
    protected TextView mAdvertTitle;
    /**
     * 当前页码
     */
    private int mCurrentIndex = 0;
    /**
     * 循环切换ViewPager的时间间隔
     */
    private static final long CHANGE_INTERVAL = 5 * 1000;
    /**
     * 切换图片的消息事件
     */
    private static final int CHANGE_ITEM = 0;
    /**
     * 加载图片的消息事件
     */
    private static final int INIT_ITEM = 1;
    /**
     * 广告数据
     */
    protected List<?> mAdvertDatas;
    /**
     * 广告点击触发事件
     */
    protected OnAdvertClickListener mListener;
    
    private Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case INIT_ITEM:
				mPager.removeAllViews();
				mPager.setAdapter(mAdapter);
				mIndicator.setViewPager(mPager);
				mAdapter.notifyDataSetChanged();
				mDefaultImg.setVisibility(View.GONE);
				setAdvertTitleName(0);
				sendMsgToChangeItem(CHANGE_ITEM);
				break;
			case CHANGE_ITEM:
				changeViewPager();
				break;
			}
		}
    };
    
	public BaseAdvertLayout(Context context) {
		super(context);
		init(context);
	}
	public BaseAdvertLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	@SuppressLint("InflateParams") 
	private void init(Context context) {
		mContext = context;
		LayoutInflater inflater = (LayoutInflater) getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View advertLayout = initSelfRootView();
		if (null == advertLayout) {
			advertLayout = inflater.inflate(R.layout.advert, null);
			mPager = (ViewPager) advertLayout.findViewById(R.id.pager);
			mIndicator = (CirclePageIndicator) advertLayout.findViewById(R.id.indicator);
			mDefaultImg = (ImageView) advertLayout.findViewById(R.id.def_img);
			mAdvertTitle = (TextView) advertLayout.findViewById(R.id.advertTitle);
		}
		addView(advertLayout);
		
		
		mPager.setOnTouchListener(mVPouchListener);
		mIndicator.setOnPageChangeListener(this);
		mIndicator.requestLayout();
	}
	
	/**
	 * 初始化根布局
	 */
	protected abstract View initSelfRootView();
	
	
	/**
	 * 设置默认图
	 * @param defaultImageId 
	 * @param imgHeight 如果大于0,设置图片高度,也当做viewpager的固定高度
	 */
	public void setDefaultImgToViewPager(int defaultImageId, int imgHeight)
	{
		mDefaultImgResourceId = defaultImageId;
		Bitmap defaultBm = BitmapFactory.decodeResource(getResources(), defaultImageId);
		mDefaultImg.setImageBitmap(defaultBm);
		resetLayoutWidthHeight(LayoutParams.MATCH_PARENT, imgHeight > 0 ? imgHeight : defaultBm.getHeight());
	}
	
	/**
	 * 重设高宽度
	 * @param w
	 * @param h
	 */
	public void resetLayoutWidthHeight(int w, int h)
	{
		if (null != mPager && w != 0 && h != 0) {
			mPager.getLayoutParams().width = w;
			mPager.getLayoutParams().height = h;
			mPager.requestLayout();
		}
	}
	
	/**
	 * 设置圆点是否显示
	 * @param visibility
	 */
	public void setIndicatorVisibility(int visibility)
	{
		if (null != mIndicator) {
			mIndicator.setVisibility(visibility);
		}
	}
	
	/**
	 * 设置标题是否显示
	 * @param visibility
	 */
	public void setTitleVisibility(int visibility)
	{
		if (null != mAdvertTitle) {
			mAdvertTitle.setVisibility(visibility);
		}
	}

	
	@Override
	public void onPageScrollStateChanged(int arg0) {
		
	}
	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		
	}
	
	@Override
	public void onPageSelected(int position) {
		mCurrentIndex = position;
		setAdvertTitleName(position);
		sendMsgToChangeItem(CHANGE_ITEM);
	}
	
	/**
	 * 获取当前滑动到第几页
	 * @return
	 */
	public int getCurrentIndex()
	{
		return mCurrentIndex;
	}
	
	/**
	 * 返回广告数据
	 */
	public List<?> getAdvertDatas()
	{
		return mAdvertDatas;
	}
	
	/**
	 * 拼接完整图片地址
	 * @param url
	 * @return
	 */
	protected abstract String getImageUrl(String url);
	
	/**
	 * 加载广告
	 * @param adverts
	 */
	public void loaderAdverts(List<AdvertInfo> adverts, OnAdvertClickListener listener) {
		if(adverts == null || adverts.size() == 0) {
			return ;
		}
		List<ImageView> views = new ArrayList<ImageView>();
		mAdvertDatas = adverts;
		mListener = listener;
		if (adverts.size() == 1) {
			mIndicator.setVisibility(View.GONE);
		}
		for(int i = 0 ; i < adverts.size(); i++) {
			ImageView item = new ImageView(mContext);
			views.add(item);
			item.setOnClickListener(this);
			item.setScaleType(ScaleType.CENTER_CROP);
			ImageLoaderUtil.load(mContext, item, getImageUrl(adverts.get(i).imageUrl), mDefaultImgResourceId);
		}
		// 填充ViewPager的数据适配器
		mAdapter = new AdvertPagerAdapter();
		mAdapter.setImageViews(views);
		mHandler.sendEmptyMessage(INIT_ITEM);
	}
	
	/**
	 * 加载纯图片
	 * @param adverts
	 */
	public void loaderImages(List<String> urlList, OnAdvertClickListener listener) {
		if(urlList==null || urlList.size()==0) {
			return ;
		}
		List<ImageView> views = new ArrayList<ImageView>();
		mAdvertDatas = urlList;
		mListener = listener;
		if (urlList.size() == 1) {
			mIndicator.setVisibility(View.GONE);
		}
		for(int i = 0; i < urlList.size(); i++) {
			if (null != urlList.get(i)) {
				ImageView item = new ImageView(mContext);
				item.setScaleType(ScaleType.CENTER_CROP);
				item.setOnClickListener(this);
				views.add(item);
				ImageLoaderUtil.load(mContext, item, getImageUrl(urlList.get(i)), mDefaultImgResourceId);
			}
		}
		mAdapter = new AdvertPagerAdapter();
		mAdapter.setImageViews(views);
		mHandler.sendEmptyMessage(INIT_ITEM);
	}
	
	/**
	 * 设置广告标题
	 * @param position
	 */
	private void setAdvertTitleName(int position)
	{
		if (mAdvertTitle.getVisibility() == View.VISIBLE 
				&& null != mAdvertDatas 
				&& mAdvertDatas.size() > 0
				&& mAdvertDatas.get(0) instanceof AdvertInfo) {
			mAdvertTitle.setText(((AdvertInfo)mAdvertDatas.get(position)).tittle);
		}
	}
	
    /**
     * 发送计划改变ViewPager当前Item的事件
     * @param what 改变事件
     */
    private void sendMsgToChangeItem(int what) {
    	mHandler.removeMessages(what);
    	mHandler.sendEmptyMessageDelayed(what, CHANGE_INTERVAL);
    }
    
    /**
     * 改变ViewPager当前Item
     */
    private void changeViewPager() {
        int pagerCount = mAdapter.getCount();
        if (mCurrentIndex < pagerCount) {
            int changeIndex = (mCurrentIndex + 1) % pagerCount;
            mPager.setCurrentItem(changeIndex, changeIndex == 0 ? false : true);                
        }
    }
	
    /**
     * 设置监听器
     * @param listener
     */
    public void setOnAdvertClickListener(OnAdvertClickListener listener)
    {
    	mListener = listener;
    }
    
    @Override
    public void onClick(View v) {
    	if (null != mListener) {
			mListener.onAdvertChooseItem(mAdvertDatas.get(mCurrentIndex));
		}
    }
    
    /**
     * 设置广告滑动动画
     * @param former
     */
    public void setPageTransformer(PageTransformer former)
    {
    	if (Build.VERSION.SDK_INT >= 11)
    	{
    		if (null != mPager) {
    			mPager.setPageTransformer(true, former);
    		}
    	}
    }
    
    /**
     * viewpageer的点击事件，防止与scrollview冲突
     * @author liangzx
     */
    OnTouchListener mVPouchListener = new OnTouchListener() {
		
		@SuppressLint("ClickableViewAccessibility") 
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// TODO Auto-generated method stub
			v.getParent().requestDisallowInterceptTouchEvent(true);
            return false;
		}
	};
    
}
