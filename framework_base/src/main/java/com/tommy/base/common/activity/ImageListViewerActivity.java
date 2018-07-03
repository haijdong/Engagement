/*
 * 文件名：ImageListViewerActivity.java
 * 创建人：汤亚杰
 * 创建时间：2013-12-30
 * 版     权：Copyright Easier Digital Tech. Co. Ltd. All Rights Reserved.
 */
package com.tommy.base.common.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.segi.view.imageview.touch.BaseImageViewPagerActivity;
import com.tommy.base.R;
import com.tommy.base.config.FusionConfig;

import java.util.ArrayList;
import java.util.List;

import cn.segi.framework.consts.FrameworkConst;
import cn.segi.framework.util.StringUtils;


/**
 * 图片加载大图
 */
public class ImageListViewerActivity extends BaseImageViewPagerActivity implements View.OnClickListener {

	public static final String EXTRA_IMAGE_ARRAY_PATH = "image_array_path";
	public static final String EXTRA_IMAGE_LIST_PATH = "image_list_path";
	public static final String EXTRA_IMAGE_STRING_PATH = "image_string_path";
	public static final String EXTRA_IMAGE_CURRENT_INDEX = "image_current_index";
    /**
     * 是否能删除
     */
    public static final String OPER_DELETE = "oper_delete";
    /**
     * 是否能删除图片
     */
    private boolean mCanDelete = false;
    /**
     * 删除的图片列表
     */
    private ArrayList<String> mDeletePaths = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.push_zoom_in, 0);
        super.onCreate(savedInstanceState);
    }

    @Override
	public void initDatas() {
		mCurrentIndex = getIntent().getIntExtra(EXTRA_IMAGE_CURRENT_INDEX, 0);
		String imageString = getIntent().getStringExtra(EXTRA_IMAGE_STRING_PATH);
		List<String> imageList = getIntent().getStringArrayListExtra(EXTRA_IMAGE_LIST_PATH);
		if (!TextUtils.isEmpty(imageString)) {
			mImageSrcArray = imageString.split(",");
		} else if (imageList != null && imageList.size() > 0) {
			mImageSrcArray = new String[imageList.size()];
			imageList.toArray(mImageSrcArray);
		} else {
			mImageSrcArray = getIntent().getStringArrayExtra(EXTRA_IMAGE_ARRAY_PATH);
		}
	}

    @Override
    public void initViews() {
        super.initViews();
        mCanDelete = getIntent().getBooleanExtra(OPER_DELETE, false);
        findViewById(R.id.delete).setVisibility(mCanDelete ? View.VISIBLE : View.GONE);
        findViewById(R.id.LButton).setOnClickListener(this);
        findViewById(R.id.delete).setOnClickListener(this);
    }


    @Override
	public void loadImageMethod(ImageView img, String imageUrl) {
		if (imageUrl.contains(FrameworkConst.IMAGE_COMPRESS_DIRECTORY) || StringUtils.isHttpUrl(imageUrl)) {
            Glide.with(this).load(imageUrl).fitCenter()
                    .placeholder(R.drawable.ic_launcher).crossFade()
                    .into(img);
        } else {
            Glide.with(this).load(FusionConfig.IMAGE_URL + imageUrl).fitCenter()
                    .placeholder(R.drawable.ic_launcher).crossFade()
                    .into(img);
        }
	}

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.LButton) {
            setImageClickCallBackMethod(v);
        } else if (v.getId() == R.id.delete) {
            if (null == mImageSrcArray
                    || 0 == mImageSrcArray.length
                    || mImageSrcArray.length <= mCurrentIndex) {
                return;
            }
            if (null == mDeletePaths) {
                mDeletePaths = new ArrayList<>();
            }
            mDeletePaths.add(mImageSrcArray[mCurrentIndex]);
            ArrayList<String> tmp = new ArrayList<>();
            for (int i = 0; i < mImageSrcArray.length; i++) {
                if (i != mCurrentIndex) {
                    tmp.add(mImageSrcArray[i]);
                }
            }
            mImageSrcArray = tmp.toArray(new String[tmp.size()]);
            if (mImageSrcArray.length <= mCurrentIndex) {
                mCurrentIndex -= 1;
            }
            if (mCurrentIndex == -1) {
                setImageClickCallBackMethod(null);
            } else {
                notifyDataSetChanged();
                setCurrentPage(mCurrentIndex);
                setCurrentIndex(mCurrentIndex);
            }
        }
    }

    @Override
    public void setImageClickCallBackMethod(View v) {
        if (null != mDeletePaths && mDeletePaths.size() > 0) {
            Intent in = getIntent();
            in.putExtra(EXTRA_IMAGE_LIST_PATH, mDeletePaths);
            setResult(Activity.RESULT_OK, in);
        }
        finish();
    }

    @Override
    public void onBackPressed() {
        setImageClickCallBackMethod(null);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.push_zoom_out);
    }
}
