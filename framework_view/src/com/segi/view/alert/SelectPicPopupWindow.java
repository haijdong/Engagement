package com.segi.view.alert;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;

import com.segi.view.R;

/**
 * 图片及文件选择弹窗
 */
public class SelectPicPopupWindow extends PopupWindow implements OnClickListener{

	private View mMenuView;

	private OnClickListener mItemsOnClick;

	public SelectPicPopupWindow(Context context, OnClickListener itemsOnClick, boolean hasCamera, boolean hasAlbum, boolean hasFile) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.select_pic, null);
		mItemsOnClick = itemsOnClick;
        mMenuView.findViewById(R.id.btn_cancel).setOnClickListener(this);
		if (hasCamera) {
			mMenuView.findViewById(R.id.btn_take_photo).setOnClickListener(this);
		} else {
			mMenuView.findViewById(R.id.btn_take_photo).setVisibility(View.GONE);
		}
		if (hasAlbum) {
			mMenuView.findViewById(R.id.btn_album).setOnClickListener(this);
		} else {
			mMenuView.findViewById(R.id.btn_album).setVisibility(View.GONE);
		}
        if (hasFile) {
            mMenuView.findViewById(R.id.file_layout).setVisibility(View.VISIBLE);
            mMenuView.findViewById(R.id.btn_file).setOnClickListener(this);
        }
        // 设置SelectPicPopupWindow的View
		this.setContentView(mMenuView);
		// 设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(LayoutParams.FILL_PARENT);
		// 设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(LayoutParams.WRAP_CONTENT);
		// 设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
		// 设置SelectPicPopupWindow弹出窗体动画效果
		this.setAnimationStyle(R.style.AnimBottom);
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0x80000000);
		// 设置SelectPicPopupWindow弹出窗体的背景
		this.setBackgroundDrawable(dw);
		// mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
		mMenuView.setOnTouchListener(new OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {

				int height = mMenuView.findViewById(R.id.pop_layout).getTop();
				int y = (int) event.getY();
				if (event.getAction() == MotionEvent.ACTION_UP) {
					if (y < height) {
						dismiss();
					}
				}
				return true;
			}
		});

	}
    public SelectPicPopupWindow(Context context, OnClickListener itemsOnClick) {
        this(context, itemsOnClick, true, true, false);
    }

	@Override
	public void onClick(View v) {
		dismiss();
		if (null != mItemsOnClick) {
			mItemsOnClick.onClick(v);
		}
	}
}
