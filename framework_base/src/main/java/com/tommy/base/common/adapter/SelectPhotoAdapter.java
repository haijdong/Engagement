package com.tommy.base.common.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.tommy.base.R;
import com.tommy.base.common.model.SelectImgVo;

import java.util.List;

import cn.segi.framework.adapter.CommonAdapter;
import cn.segi.framework.adapter.ViewHolder;
import cn.segi.framework.imagecache.ImageLoaderUtil;



/**
 * 图片列表适配器
 */
public class SelectPhotoAdapter extends CommonAdapter<SelectImgVo> {

    private RelativeLayout.LayoutParams mLayoutParams = null;

    public SelectPhotoAdapter(Context context, List<SelectImgVo> mDatas, int itemLayoutId) {
        super(context, mDatas, itemLayoutId);
        int paddingPx = mContext.getResources().getDimensionPixelSize(R.dimen.x10);
        int width = mContext.getResources().getDisplayMetrics().widthPixels;
        int wid = (width - 4 * paddingPx) / 3;
        mLayoutParams = new RelativeLayout.LayoutParams(wid, wid);
    }


    @Override
    public void convert(ViewHolder helper, SelectImgVo item, int position) {
        ImageView imageview = helper.getView(R.id.select_img);
        imageview.setLayoutParams(mLayoutParams);
        ImageView statusView = helper.getView(R.id.status);
        statusView.setLayoutParams(mLayoutParams);
        ImageView selectIv = helper.getView(R.id.select_btn_iv);

        ImageLoaderUtil.load(mContext, imageview, item.path, R.drawable.ic_launcher);
        if (item.isSelect) {
            statusView.setVisibility(View.VISIBLE);
            selectIv.setImageResource(R.drawable.btn_selectimg_pre);
        } else {
            statusView.setVisibility(View.GONE);
            selectIv.setImageResource(R.drawable.btn_selectimg_nor);
        }

    }
}
