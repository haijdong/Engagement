package com.segi.view.pick;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.segi.view.R;
import com.segi.view.imageview.graffiti.GraffitiParams;
import com.segi.view.imageview.graffiti.GraffitiView;

import java.io.File;

import cn.segi.framework.activity.BaseFrameworkActivity;
import cn.segi.framework.executor.ExecutorSupport;
import cn.segi.framework.util.FrameworkUtil;
import cn.segi.framework.util.ImageUtil;

/**
 * 编辑图片页面基类
 *
 * @author liangzx
 * @version 1.0
 * @time 2017-11-07 11:06
 **/
public abstract class BaseEditImageActivity extends BaseFrameworkActivity implements View.OnClickListener, GraffitiView.GraffitiListener {

    /**
     * 撤销按钮
     */
    private ImageView mUndoImg;
    /**
     * 编辑图片控件
     */
    private GraffitiView mGraffitiView;
    /**
     * 配置参数
     */
    private GraffitiParams mGraffitiParams;
    /**
     * 是否需要水印
     */
    private boolean mIsAddWaterMark = true;
    /**
     * 图片路径
     */
    public static final String IMAGE_PATH = "image_path";
    /**
     * 配置参数
     */
    public static final String OPER_SETTING = "oper_setting";
    /**
     * 是否需要水印
     */
    public static final String IS_ADD_WATER = "is_add_water";
    /**
     * 返回图片的地址
     */
    public static final String RESULT_PATH = "result_path";
    /**
     * 请求页面编码
     */
    public static final int REQUEST_CODE = 0x7777;
    /**
     * 不触摸屏幕超过一定时间显示导航栏
     */
    private Runnable mShowDelayRunnable;
    /**
     * 触摸屏幕超过一定时间隐藏导航栏
     */
    private Runnable mHideDelayRunnable;
    /**
     * 头部及底视图
     */
    private View mHeaderLayout;
    private View mBottomLayout;
    /**
     * 头部动画
     */
    private TranslateAnimation mHeaderShowAnimation;
    private TranslateAnimation mHeaderHideAnimation;
    /**
     * 底部动画
     */
    private TranslateAnimation mBottomShowAnimation;
    private TranslateAnimation mBottomHideAnimation;

    @Override
    protected void initViews() {
        setContentView(R.layout.edit_image);
        mUndoImg = (ImageView) findViewById(R.id.undo);
        mUndoImg.setTag(false);
        mHeaderLayout = findViewById(R.id.headerLayout);
        mBottomLayout = findViewById(R.id.bottomLayout);
     }

    @Override
    protected void initEvents() {
        findViewById(R.id.cancel).setOnClickListener(this);
        findViewById(R.id.finish).setOnClickListener(this);
        mUndoImg.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        if (null != getIntent().getExtras()) {
            Bundle bundle = getIntent().getExtras();
            String imagePath = bundle.getString(IMAGE_PATH);
            mGraffitiParams = bundle.getParcelable(OPER_SETTING);
            mIsAddWaterMark = bundle.getBoolean(IS_ADD_WATER, true);
            if (null != mGraffitiParams) {
                imagePath = mGraffitiParams.imagePath;
            }
            if (TextUtils.isEmpty(imagePath)) {
                finish();
                return;
            }
            File file = new File(imagePath);
            if (!file.exists()) {
                finish();
                return;
            }

            if (null == mGraffitiParams) {
                mGraffitiParams = new GraffitiParams();
                mGraffitiParams.imagePath = imagePath;
                mGraffitiParams.isDrawableOutside = false;
                mGraffitiParams.isFullScreen = false;
                mGraffitiParams.penType = GraffitiView.Pen.HAND;
                mGraffitiParams.penColor = Color.RED;
                mGraffitiParams.changePanelVisibilityDelay = 200;
                mGraffitiParams.penSize = getResources().getDimensionPixelSize(R.dimen.x3);
                mGraffitiParams.savePath = FrameworkUtil.getCompressImagePath() + File.separator + "edit_" + file.getName();
            }
            showDialog("正在加载中...");
            //加载图片
            Glide.with(this).load(mGraffitiParams.imagePath).asBitmap().into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    dimissDialog();
                    Message msg = new Message();
                    msg.what = 2;
                    msg.obj = resource;
                    if (null != msg.obj) {
                        mHandler.sendMessage(msg);
                    }
                }
            });

            mShowDelayRunnable = new Runnable() {
                @Override
                public void run() {
                    showView();
                }
            };
            mHideDelayRunnable = new Runnable() {
                @Override
                public void run() {
                    hideView();
                }
            };
            return;
        }
        finish();
    }


    /**
     * 初始化绘图控件
     * @param bitmap
     */
    private void initGraffitiView(Bitmap bitmap) {
        if (null == bitmap) {
            finish();
            return;
        }
        mGraffitiView = new GraffitiView(this
                , bitmap
                , null
                , false
                , this);
        setSetting();
        FrameLayout contentLayout = (FrameLayout) findViewById(R.id.graffitiContent);
        contentLayout.addView(mGraffitiView, FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);

        mGraffitiView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initAnimation();
                        mHeaderLayout.removeCallbacks(mHideDelayRunnable);
                        mHeaderLayout.removeCallbacks(mShowDelayRunnable);
                        mBottomLayout.removeCallbacks(mHideDelayRunnable);
                        mBottomLayout.removeCallbacks(mShowDelayRunnable);
                        mHeaderLayout.postDelayed(mHideDelayRunnable, mGraffitiParams.changePanelVisibilityDelay);
                        mBottomLayout.postDelayed(mHideDelayRunnable, mGraffitiParams.changePanelVisibilityDelay);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP:
                        initAnimation();
                        mHandler.sendEmptyMessage(1);
                        mHeaderLayout.removeCallbacks(mHideDelayRunnable);
                        mHeaderLayout.removeCallbacks(mShowDelayRunnable);
                        mBottomLayout.removeCallbacks(mHideDelayRunnable);
                        mBottomLayout.removeCallbacks(mShowDelayRunnable);
                        mHeaderLayout.postDelayed(mShowDelayRunnable, mGraffitiParams.changePanelVisibilityDelay);
                        mBottomLayout.postDelayed(mShowDelayRunnable, mGraffitiParams.changePanelVisibilityDelay);
                        break;
                }
                return false;
            }
        });
    }

    /**
     * 设置配置
     */
    private void setSetting() {
        if (null == mGraffitiView || null == mGraffitiParams) {
            return;
        }
        mGraffitiView.setIsDrawableOutside(mGraffitiParams.isDrawableOutside);
        mGraffitiView.setPen(mGraffitiParams.penType);
        mGraffitiView.setColor(mGraffitiParams.penColor);
        mGraffitiView.setPaintSize(mGraffitiParams.penSize);
    }

    /**
     * 更新撤销按钮状态
     */
    private void updateUndoStatus() {
        boolean oldStatus = (boolean) mUndoImg.getTag();
        if (oldStatus == mGraffitiView.isModified()) {
            return;
        }
        mUndoImg.setTag(mGraffitiView.isModified());
        mUndoImg.setImageResource(mGraffitiView.isModified() ? R.drawable.img_editimg_cancel_pre : R.drawable.img_editimg_cancel_nor);
    }


    /**
     * 显示视图
     */
    private void showView() {
        if (null != mHeaderLayout) {
            if (mHeaderLayout.getVisibility() != View.VISIBLE) {
                mHeaderLayout.clearAnimation();
                mHeaderLayout.startAnimation(mHeaderShowAnimation);
                mHeaderLayout.setVisibility(View.VISIBLE);
            }
        }
        if (null != mBottomLayout) {
            if (mBottomLayout.getVisibility() != View.VISIBLE) {
                mBottomLayout.clearAnimation();
                mBottomLayout.startAnimation(mBottomShowAnimation);
                mBottomLayout.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * 隐藏视图
     */
    private void hideView() {
        if (null != mHeaderLayout) {
            if (mHeaderLayout.getVisibility() == View.VISIBLE) {
                mHeaderLayout.clearAnimation();
                mHeaderLayout.startAnimation(mHeaderHideAnimation);
                mHeaderLayout.setVisibility(View.GONE);
            }
        }
        if (null != mBottomLayout) {
            if (mBottomLayout.getVisibility() == View.VISIBLE) {
                mBottomLayout.clearAnimation();
                mBottomLayout.startAnimation(mBottomHideAnimation);
                mBottomLayout.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 初始化动画
     */
    private void initAnimation() {
        if (null == mHeaderShowAnimation) {
            mHeaderShowAnimation = new TranslateAnimation(0, 0, -mHeaderLayout.getHeight(), 0);
            mHeaderShowAnimation.setDuration(500);
            mHeaderShowAnimation.setFillAfter(true);
        }
        if (null == mHeaderHideAnimation) {
            mHeaderHideAnimation = new TranslateAnimation(0, 0, 0, -mHeaderLayout.getHeight());
            mHeaderHideAnimation.setDuration(500);
            mHeaderHideAnimation.setFillAfter(true);
        }
        if (null == mBottomShowAnimation) {
            mBottomShowAnimation = new TranslateAnimation(0, 0, mBottomLayout.getHeight(), 0);
            mBottomShowAnimation.setDuration(500);
            mBottomShowAnimation.setFillAfter(true);
        }
        if (null == mBottomHideAnimation) {
            mBottomHideAnimation = new TranslateAnimation(0, 0, 0, mBottomLayout.getHeight());
            mBottomHideAnimation.setDuration(500);
            mBottomHideAnimation.setFillAfter(true);
        }
    }

    /**
     *
     */
    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 2) {
                initGraffitiView((Bitmap) msg.obj);
            } else if (msg.what == 1) {
                updateUndoStatus();
            }
        }
    };

    @Override
    public void handleFragmentMessage(String tag, int what, Object obj) {
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.cancel) {
            finish();
        } else if (v.getId() == R.id.undo) {
            if (null != mGraffitiView) {
                mGraffitiView.undo();
                mHandler.sendEmptyMessage(1);
            }
        } else if (v.getId() == R.id.finish) {
            if (null != mGraffitiView) {
                showDialog("请稍候...");
                ExecutorSupport.getExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
                        mGraffitiView.save();
                    }
                });
            }
        }
    }

    @Override
    public void onSaved(final Bitmap bitmap, Bitmap bitmapEraser) {
        if (null != bitmapEraser) {
            bitmapEraser.recycle();
        }
        String newPath = ImageUtil.createFileByBitmap(mGraffitiParams.imagePath, bitmap);
        if (mIsAddWaterMark) {
            ImageUtil.addWaterRemark(
                    newPath,
                    "water_remark_icon.png",
                    getResources().getDimensionPixelSize(R.dimen.x24),
                    Color.YELLOW, Color.BLACK);
        }
        dimissDialog();
        Intent in = getIntent();
        in.putExtra(RESULT_PATH, newPath);
        setResult(Activity.RESULT_OK, in);
        finish();
        if (null != bitmap) {
            bitmap.recycle();
        }
    }

    @Override
    public void onError(int i, String msg) {
        show(msg);
    }

    @Override
    public void onReady() {

    }

    /**
     * 显示弹窗
     */
    public abstract void showDialog(String tip);

    /**
     * 取消弹窗
     */
    public abstract void dimissDialog();

}
