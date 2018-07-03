package com.tommy.base.common.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.tommy.base.R;
import com.tommy.base.common.model.SelectPhotoPopVo;

import java.util.List;

import cn.segi.framework.adapter.CommonAdapter;
import cn.segi.framework.adapter.ViewHolder;
import cn.segi.framework.imagecache.ImageLoaderUtil;


public class SelectPhotoPopAdapter extends CommonAdapter<SelectPhotoPopVo> {

	private Context context;

	public SelectPhotoPopAdapter(Context context, List<SelectPhotoPopVo> mDatas, int itemLayoutId) {
		super(context, mDatas, itemLayoutId);
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	@Override
	public void convert(ViewHolder helper, SelectPhotoPopVo item, int position) {
		// TODO Auto-generated method stub
		ImageView iv = helper.getView(R.id.photo_select_ic);
		TextView title = helper.getView(R.id.photo_select_title);
		TextView count = helper.getView(R.id.photo_select_total);
		title.setText(item.dirName);
		count.setText("(" + item.count + ")");
		title.setTag(item);
		ImageLoaderUtil.load(mContext, iv, item.path,
				R.drawable.ic_launcher);
		// ImageLoaderUtil.get(context, item.path);
	}

}
