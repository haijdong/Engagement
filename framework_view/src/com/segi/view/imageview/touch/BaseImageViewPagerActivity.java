package com.segi.view.imageview.touch;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.segi.view.R;
import com.segi.view.imageview.TouchImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * 可缩放图片viewpager
 * 
 * @author liangzx
 * 
 */
public abstract class BaseImageViewPagerActivity extends Activity implements
		OnPageChangeListener {

	/**
	 * Step 1: Download and set up v4 support library:
	 * http://developer.android.com/tools/support-library/setup.html Step 2:
	 * Create ExtendedViewPager wrapper which calls
	 * TouchImageView.canScrollHorizontallyFroyo Step 3: ExtendedViewPager is a
	 * custom view and must be referred to by its full package name in XML Step
	 * 4: Write TouchImageAdapter, located below Step 5. The ViewPager in the
	 * XML should be ExtendedViewPager 提示：如果默认页面不变化，只需要实现initDatas方法
	 */

	protected ExtendedViewPager mViewPager;
	/**
	 * 当前第几张图片标识
	 */
	protected TextView mCurrentPageSite;
	/**
	 * 图片数据
	 */
	protected String[] mImageSrcArray;
	/**
	 * 当前位置
	 */
	protected int mCurrentIndex;
    /**
     * 适配器
     */
    protected TouchImageAdapter mTouchImageAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentViewLayoutId();
		initDatas();
		initViews();
	}

	/**
	 * 初始化数据
	 */
	public abstract void initDatas();

	/**
	 * 加载图片的方法
	 */
	public abstract void loadImageMethod(ImageView img, String imageUrl);

	/**
	 * 初始化页面
	 */
	public void initViews() {
		mViewPager = (ExtendedViewPager) findViewById(R.id.view_pager);
		mCurrentPageSite = (TextView) findViewById(R.id.current_page_site);
		mViewPager.setAdapter(mTouchImageAdapter = new TouchImageAdapter());
		mViewPager.setOnPageChangeListener(this);
		setCurrentPage(mCurrentIndex);
		setCurrentIndex(mCurrentIndex);
	}

	/**
	 * 默认使用view包布局文件<BR>
	 * 重写该方法可以
	 */
	protected void setContentViewLayoutId() {
		setContentView(R.layout.image_viewpager_demo);
	}

	/**
	 * 设置跳转到第几页
	 * 
	 * @param index
	 */
	public void setCurrentPage(int index) {
		mViewPager.setCurrentItem(index);
	}

	/**
	 * 设置底部当前第几页的标识
	 */
	public void setCurrentIndex(int index) {
		mCurrentIndex = index;
		mCurrentPageSite.setText((mCurrentIndex + 1) + "/"
				+ mImageSrcArray.length);
	}

    /**
     * 刷新页面
     */
	public void notifyDataSetChanged() {
        if (null != mTouchImageAdapter) {
            mTouchImageAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 删除页面
     * @param position
     */
    public void removeViewAt(int position) {
        if (null != mViewPager) {
			View view = mViewPager.getChildAt(position);
			if(null!=view){
				mViewPager.removeView(view);
				//).removeViewAt(position);
			}
        }
    }


	/**
	 * 设置图片单击回调事件
	 * 
	 * @param v
	 */
	public void setImageClickCallBackMethod(View v) {
		BaseImageViewPagerActivity.this.finish();
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}

	@Override
	public void onPageSelected(int index) {
		setCurrentIndex(index);
	}

	class TouchImageAdapter extends PagerAdapter {

		@Override
		public int getCount() {
            if (null == mImageSrcArray) {
                return 0;
            }
			return mImageSrcArray.length;
		}

		@Override
		public View instantiateItem(ViewGroup container, int position) {
			TouchImageView img = new TouchImageView(container.getContext());
			loadImageMethod(img, mImageSrcArray[position]);
			container.addView(img, LinearLayout.LayoutParams.MATCH_PARENT,
					LinearLayout.LayoutParams.MATCH_PARENT);
			img.setOnClickListener(imageListener);
			return img;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        OnClickListener imageListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				setImageClickCallBackMethod(v);
			}
		};

	}

	/**
	 * 保存图片
	 * 
	 * @param bitmap
	 * @param file
	 */
	protected void saveImage(Bitmap bitmap, File file) {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			FileOutputStream fos = null;
			try {
				String imgName = mImageSrcArray[mCurrentIndex]
						.substring(mImageSrcArray[mCurrentIndex]
								.lastIndexOf("/"));
				File img = new File(file, imgName);
				if (!file.exists()) {
					file.mkdir();
				}
				if (!img.exists()) {
					fos = new FileOutputStream(img);
					if (imgName.endsWith("png") || imgName.endsWith("PNG")) {
						bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
					} else {
						bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
					}
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} finally {
				try {
					if (null != fos) {
						fos.close();
					}
					// 通知系统相册及时更新
					Intent intent = new Intent(
							Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
					Uri uri = Uri.fromFile(file);
					intent.setData(uri);
					getBaseContext().sendBroadcast(intent);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}
	}
}
