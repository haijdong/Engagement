package com.segi.view.advert;

import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

/**
 * 广告适配器
 * @author tangst
 *
 */
public class AdvertPagerAdapter extends PagerAdapter {

	private List<ImageView> mImageViews;
	
	public void setImageViews(List<ImageView> imageViews) {
		mImageViews=imageViews;
	}
	
	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}
	@Override
	public int getCount() {
		if(mImageViews!=null && mImageViews.size()>0) {
			return mImageViews.size();
		}
		return 0;
	}
	
	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}

	@Override
	public void destroyItem(View container, int position, Object object) {
		if(mImageViews!=null && mImageViews.size()>0) {
			((ViewPager) container).removeView(mImageViews.get(position));
		}
	}
	
	@Override
	public Object instantiateItem(View container, int position) {
		((ViewPager) container).addView(mImageViews.get(position));
		return mImageViews.get(position);
	}

}
