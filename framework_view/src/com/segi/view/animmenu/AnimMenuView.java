package com.segi.view.animmenu;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.segi.view.R;

import java.util.ArrayList;

import cn.segi.framework.log.Logger;

/**
 * 自定义动画菜单
 * Created by Kinwee on 2016/9/24.
 */
public abstract class AnimMenuView extends RelativeLayout {

    protected Context mContext;

    private ArrayList<TextView> textViews;

    private ArrayList<ImageView> imageViews;

    /**
     * 标签布局底部间距
     */
    private int labellayout_bottommargin = 300;

    /**
     * label间距
     */
    private int label_margin = 30;

    /**
     * 存放标签文字
     */
    private LinearLayout labelLayout;

    /**
     * 菜单关闭按钮
     */
    private ImageView menuCloseBtn;

    /**
     * 存放图标x坐标
     */
    private float imageX = -1;

    /**
     * 存放图标的y坐标
     */
    private float imageY = -1;

    /**
     * 开始动画的下标
     */
    private int startIndex = 0;

    private boolean isClose = false;

    /**
     * 图标大小
     */
    private int icon_size = 80;

    /**
     * 动画时间-毫秒
     */
    private int duration = 500;

    /**
     * 标签与图标动画结束位置的间距
     */
    private int label_margin_from_icon_end_of_anim = 30;

    /**
     * 关闭图标与父容器的间距
     */
    private int closeicon_margin_toparent = 40;
    /**
     * imageView和lable的间距
     */
    private int imageicon_margin_totextview =40;


    /**
     * 关闭图标缩放比例
     */
    private float close_icon_zoom_scale=0.73f;

    /**
     * 列表图标缩放比例
     */
    private float listicon_zoom_scale=0.75f;

    private View bottomView;

    public AnimMenuView(Context context) {
        super(context);
        mContext = context;
    }

    public AnimMenuView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs,
                    R.styleable.AnimMenuView, 0, 0);

            labellayout_bottommargin = Math.max(1, a.getDimensionPixelSize(
                    R.styleable.AnimMenuView_labellayout_bottommargin, labellayout_bottommargin));
            label_margin = Math.max(1, a.getDimensionPixelSize(
                    R.styleable.AnimMenuView_label_margin, label_margin));
            icon_size = Math.max(1, a.getDimensionPixelSize(
                    R.styleable.AnimMenuView_icon_size, icon_size));
            label_margin_from_icon_end_of_anim = Math.max(1, a.getDimensionPixelSize(
                    R.styleable.AnimMenuView_label_margin_from_icon_end_of_anim, label_margin_from_icon_end_of_anim));
            closeicon_margin_toparent = Math.max(1, a.getDimensionPixelSize(
                    R.styleable.AnimMenuView_closeicon_margin_toparent, closeicon_margin_toparent));
            imageicon_margin_totextview = Math.max(1, a.getDimensionPixelSize(
                    R.styleable.AnimMenuView_imageicon_margin_totextview, imageicon_margin_totextview));
            duration = a.getInt(
                    R.styleable.AnimMenuView_duration,
                    duration);
            close_icon_zoom_scale=a.getFloat(R.styleable.AnimMenuView_close_icon_zoom_scale,close_icon_zoom_scale);
            listicon_zoom_scale=a.getFloat(R.styleable.AnimMenuView_listicon_zoom_scale,listicon_zoom_scale);
            a.recycle();
        }
    }

    public AnimMenuView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
    }

    protected void init() {
        imageViews = creatImageViews();
        textViews = creatTextViews();
        bottomView = new View(mContext);
        bottomView.setId(0x1001);
        RelativeLayout.LayoutParams viewParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1);
        viewParams.addRule(ALIGN_PARENT_BOTTOM);
        bottomView.setLayoutParams(viewParams);
        addView(bottomView);
        addLabels();
        addView(labelLayout);
        addImageviews();
        addCloseBtn();
        myHandler.sendEmptyMessageDelayed(0, 100);
    }


    Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            closeBtnAnim();
        }
    };

    /**
     * 加入标签
     */
    private void addLabels() {
        labelLayout = new LinearLayout(mContext);
        labelLayout.setOrientation(LinearLayout.HORIZONTAL);
        labelLayout.setWeightSum(textViews.size());
        for (int i = 0; i < textViews.size(); i++) {
            LinearLayout.LayoutParams labelParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
            labelParams.setMargins(label_margin, imageicon_margin_totextview, label_margin, label_margin);
            textViews.get(i).setLayoutParams(labelParams);
            textViews.get(i).setGravity(Gravity.CENTER);
            labelLayout.addView(textViews.get(i));
            textViews.get(i).setAlpha(0);
        }
        LayoutParams labelParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        labelParams.addRule(ALIGN_PARENT_BOTTOM);
        labelLayout.setLayoutParams(labelParams);
        labelLayout.setPadding(0, 0, 0, labellayout_bottommargin);
    }

    /**
     * 加入图标
     */
    private void addImageviews() {
        for (int i = 0; i < imageViews.size(); i++) {
            LayoutParams imageParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            imageParams.addRule(ABOVE, bottomView.getId());
            imageParams.addRule(CENTER_HORIZONTAL);
            imageViews.get(i).setLayoutParams(imageParams);
            imageViews.get(i).setAlpha(0);
            ObjectAnimator anim1 = ObjectAnimator.ofFloat(imageViews.get(i), "scaleX",
                    1f,0.2f);
            ObjectAnimator anim2 = ObjectAnimator.ofFloat(imageViews.get(i), "scaleY",
                    1f,0.2f );
            AnimatorSet animSet = new AnimatorSet();
            animSet.setDuration(1);
            animSet.playTogether(anim1, anim2);
            animSet.start();
            imageViews.get(i).setVisibility(View.INVISIBLE);
            addView(imageViews.get(i));
        }
    }

    /**
     * 开始图标、标签动画
     */
    private void startImageAndLabelAnim() {
        if (isClose) {
            /**
             * 关闭动画
             */
            ObjectAnimator anim4 = ObjectAnimator.ofFloat(imageViews.get(startIndex), "y",
                    imageViews.get(startIndex).getY(), imageViews.get(startIndex).getY() + 5, imageViews.get(startIndex).getY() - 15);
            AnimatorSet animSet = new AnimatorSet();
            animSet.setDuration(duration / 3);
            animSet.setInterpolator(new LinearInterpolator());
            animSet.play(anim4);
            animSet.start();
            animSet.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    ObjectAnimator anim1 = ObjectAnimator.ofFloat(imageViews.get(startIndex), "scaleX",
                            1f,listicon_zoom_scale);
                    ObjectAnimator anim2 = ObjectAnimator.ofFloat(imageViews.get(startIndex), "scaleY",
                            1f,listicon_zoom_scale );

                    ObjectAnimator anim3 = ObjectAnimator.ofFloat(imageViews.get(startIndex), "x",
                            imageViews.get(startIndex).getX(), imageX);

                    ObjectAnimator anim4 = ObjectAnimator.ofFloat(imageViews.get(startIndex), "y",
                            imageViews.get(startIndex).getY(), imageY);

                    ObjectAnimator anim5 = ObjectAnimator.ofFloat(imageViews.get(startIndex), "alpha",
                            1.0f, 0f);

                    ObjectAnimator anim6 = ObjectAnimator.ofFloat(textViews.get(startIndex), "y",
                            textViews.get(startIndex).getY(), textViews.get(startIndex).getY() + 20);
                    ObjectAnimator anim7 = ObjectAnimator.ofFloat(textViews.get(startIndex), "alpha",
                            1.0f, 0f);
                    AnimatorSet animSet = new AnimatorSet();
                    animSet.setDuration(duration);
                    animSet.setInterpolator(new LinearInterpolator());
                    //两个动画同时执行
                    animSet.playTogether(anim1, anim2, anim3, anim4, anim5, anim6, anim7);
                    animSet.start();
                    if(startIndex==textViews.size()-1){
                        labelAnimStart(isClose);
                    }
                    anim2.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            Logger.e("onAnimationStart","onAnimationStart");
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            startIndex--;
                            if (startIndex < 0) {
                                startIndex = 0;
                                closeBtnAnim();
                                return;
                            }
                            startImageAndLabelAnim();
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    });
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });


        } else {
            /**
             * 初始动画
             */
            if (imageViews.size() != 0) {
                for (int i = 0; i < imageViews.size(); i++) {
                    imageViews.get(i).setVisibility(View.VISIBLE);
                }
                if (imageX == -1 && imageY == -1) {
                    imageX = imageViews.get(0).getX();
                    imageY = imageViews.get(0).getY();
                }
            }
            ObjectAnimator anim1 = ObjectAnimator.ofFloat(imageViews.get(startIndex), "scaleX",
                    listicon_zoom_scale  , 1f);
            ObjectAnimator anim2 = ObjectAnimator.ofFloat(imageViews.get(startIndex), "scaleY",
                    listicon_zoom_scale  , 1f);

            int screenWidth = getWidth();
            int x = screenWidth / imageViews.size() / 2 - imageViews.get(startIndex).getWidth() / 2 +
                    screenWidth / imageViews.size() * startIndex;
            ObjectAnimator anim3 = ObjectAnimator.ofFloat(imageViews.get(startIndex), "x",
                    imageViews.get(startIndex).getX(), x);
            Logger.e("AnimMenuView","labelLayout.getHeight()="+labelLayout.getHeight());
            Logger.e("AnimMenuView","imageViews.get(startIndex).getY()="+imageViews.get(startIndex).getY());
            Logger.e("AnimMenuView","imageViews.get(startIndex).getHeight()="+imageViews.get(startIndex).getHeight());
            ObjectAnimator anim4 = ObjectAnimator.ofFloat(imageViews.get(startIndex), "y",
                    imageViews.get(startIndex).getY(), imageViews.get(startIndex).getY() - labelLayout.getHeight()+imageViews.get(startIndex).getHeight()*(1-listicon_zoom_scale)-label_margin_from_icon_end_of_anim+20);
            ObjectAnimator anim5 = ObjectAnimator.ofFloat(textViews.get(startIndex), "y",
                    textViews.get(startIndex).getY(), textViews.get(startIndex).getY() - 20);

            ObjectAnimator anim6 = ObjectAnimator.ofFloat(textViews.get(startIndex), "alpha",
                    0f, 1.0f);
            AnimatorSet animSet = new AnimatorSet();
            animSet.setDuration(duration);
            animSet.setInterpolator(new LinearInterpolator());
            //两个动画同时执行
            animSet.playTogether(anim1, anim2, anim3, anim4, anim5, anim6);
            animSet.start();
            if(startIndex==0){
                labelAnimStart(isClose);
            }
            anim4.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    Logger.e("onAnimationStart","onAnimationStart");
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    ObjectAnimator anim4 = ObjectAnimator.ofFloat(imageViews.get(startIndex), "y",
                            imageViews.get(startIndex).getY(), imageViews.get(startIndex).getY() + 15, imageViews.get(startIndex).getY() - 5);
                    AnimatorSet animSet = new AnimatorSet();
                    animSet.setDuration(duration / 3);
                    animSet.setInterpolator(new LinearInterpolator());
                    animSet.play(anim4);
                    animSet.start();
                    animSet.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            startIndex++;
                            if (startIndex == imageViews.size()) {
                                startIndex = imageViews.size() - 1;
                                setButtonEnable(true);
                                closeAnimEnd(isClose);
                                Logger.e("初始动画","onAnimationEnd");
                                return;
                            }
                            startImageAndLabelAnim();
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    });
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            anim4.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {

                }
            });
        }
    }

    public void closeAnim(){
        setButtonEnable(false);
        isClose = true;
        startImageAndLabelAnim();
    }

    private void setButtonEnable(boolean enable){
        menuCloseBtn.setEnabled(enable);
        for (int i = 0; i < imageViews.size(); i++) {
            imageViews.get(i).setEnabled(enable);
        }
    }


    /**
     * 加入关闭按钮
     */
    private void addCloseBtn() {
        menuCloseBtn = creatCloseBtn();
        LayoutParams imageParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        imageParams.addRule(ABOVE, bottomView.getId());
        imageParams.addRule(CENTER_HORIZONTAL);
        imageParams.setMargins(0, 0, 0, closeicon_margin_toparent);
        menuCloseBtn.setLayoutParams(imageParams);
        addView(menuCloseBtn);
        setButtonEnable(false);
        menuCloseBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                closeAnim();
            }
        });
        ObjectAnimator anim1 = ObjectAnimator.ofFloat(menuCloseBtn, "scaleX",
                1f ,close_icon_zoom_scale );
        ObjectAnimator anim2 = ObjectAnimator.ofFloat(menuCloseBtn, "scaleY",
                1f   , close_icon_zoom_scale);
        AnimatorSet animSet = new AnimatorSet();
        animSet.setDuration(1);
        animSet.playTogether(anim1, anim2);
        menuCloseBtn.setVisibility(View.INVISIBLE);
        animSet.start();
    }

    /**
     * 关闭按钮动画
     */
    private void closeBtnAnim() {
        if (isClose) {
            /**
             * 关闭动画
             */
            ObjectAnimator anim1 = ObjectAnimator.ofFloat(menuCloseBtn, "scaleX",
                    1f ,close_icon_zoom_scale );
            ObjectAnimator anim2 = ObjectAnimator.ofFloat(menuCloseBtn, "scaleY",
                    1f   , close_icon_zoom_scale);
            ObjectAnimator anim3 = ObjectAnimator.ofFloat(menuCloseBtn, "rotation", 90F, 0F);//360度旋转

            ObjectAnimator anim4 = ObjectAnimator.ofFloat(menuCloseBtn, "alpha",
                    1.0f, 0f);

            AnimatorSet animSet = new AnimatorSet();
            animSet.setDuration(duration);
            animSet.setInterpolator(new LinearInterpolator());
            //两个动画同时执行
            animSet.playTogether(anim1, anim2, anim3, anim4);
            animSet.start();
            animSet.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    Logger.e("关闭动画", "onAnimationEnd");
                    closeAnimEnd(isClose);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        } else {
            menuCloseBtn.setVisibility(View.VISIBLE);
            /**
             * 初始动画
             */
            ObjectAnimator anim1 = ObjectAnimator.ofFloat(menuCloseBtn, "scaleX",
                    close_icon_zoom_scale ,1f );
            ObjectAnimator anim2 = ObjectAnimator.ofFloat(menuCloseBtn, "scaleY",
                    close_icon_zoom_scale ,1f );
            ObjectAnimator anim3 = ObjectAnimator.ofFloat(menuCloseBtn, "rotation", 0F, 90F);//360度旋转

            AnimatorSet animSet = new AnimatorSet();
            animSet.setDuration(duration);
            animSet.setInterpolator(new LinearInterpolator());
            //两个动画同时执行
            animSet.playTogether(anim1, anim2, anim3);
            animSet.start();
            animSet.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    startImageAndLabelAnim();
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        }
    }


    /**
     * 创建关闭按钮
     *
     * @return
     */
    protected abstract ImageView creatCloseBtn();

    protected abstract void closeAnimEnd(boolean isClose);

    /**
     * 创建图标
     *
     * @return
     */
    protected abstract ArrayList<ImageView> creatImageViews();

    /**
     * 创建label
     *
     * @return
     */
    protected abstract ArrayList<TextView> creatTextViews();

    /**
     * 标签动画开始
     * @param isClose 是否关闭
     */
    protected abstract void labelAnimStart(boolean isClose);
}
