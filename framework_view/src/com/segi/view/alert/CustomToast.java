package com.segi.view.alert;


import cn.segi.framework.util.WindowDispaly;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
 
/**
 * 自定义toast<BR>
 * 后面的toast内容会覆盖前面的toast
 * @author liangzx
 * @version 1.0
 *
 */
public class CustomToast {
    
	public static CustomToast mCustomToast;
	
	/**
	 * 获取toast的单例<BR>
	 * 实现后面toast覆盖前面的toast<BR>
	 * @param context
	 * @param text
	 * @param duration
	 * @return
	 */
	public static CustomToast getSingleInstance(Context context, CharSequence text, int duration)
	{
		if (null == mCustomToast) {
			mCustomToast = CustomToast.makeText(context, text, duration);
		}else{
			mCustomToast.setView(CustomToast.makeText(context, text, duration).getView());
		}
		return mCustomToast;
	}
	
	
    public static CustomToast makeText(Context context, CharSequence text, int duration) 
    {
        CustomToast result = new CustomToast(context);
         
        LinearLayout mLayout = new LinearLayout(context);
        mLayout.setGravity(Gravity.CENTER);
        TextView tv = new TextView(context);
        tv.setMaxWidth(WindowDispaly.getWidth() / 2);
        tv.setText(text);
        tv.setTextColor(Color.WHITE);
        tv.setGravity(Gravity.CENTER);
        tv.setPadding(20, 20, 20, 20);
        
        GradientDrawable shape = new GradientDrawable();
	    shape.setColor(Color.parseColor("#000000"));
	    shape.setCornerRadius(10);
	    shape.setAlpha(179);
        mLayout.setBackground(shape);
        
        mLayout.addView(tv, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        result.mNextView = mLayout;
        result.mDuration = duration;
 
        return result;
    }
     
    public static final int LENGTH_SHORT = 2000;
    public static final int LENGTH_LONG = 3500;
     
    private final Handler mHandler = new Handler();
    private int mDuration = LENGTH_SHORT;
    private int mGravity = Gravity.CENTER;
    private int mX, mY;
    private float mHorizontalMargin;
    private float mVerticalMargin;
    private View mView;
    private View mNextView;
     
    private WindowManager mWM;
    private final WindowManager.LayoutParams mParams = new WindowManager.LayoutParams();
     
     
     public CustomToast(Context context) {
            init(context);
        }
      
    /**
     * Set the view to show.
     * @see #getView
     */
    public void setView(View view) {
        mNextView = view;
    }
 
    /**
     * Return the view.
     * @see #setView
     */
    public View getView() {
        return mNextView;
    }
 
    /**
     * Set how long to show the view for.
     * @see #LENGTH_SHORT
     * @see #LENGTH_LONG
     */
    public void setDuration(int duration) {
        mDuration = duration;
    }
 
    /**
     * Return the duration.
     * @see #setDuration
     */
    public int getDuration() {
        return mDuration;
    }
     
    /**
     * Set the margins of the view.
     *
     * @param horizontalMargin The horizontal margin, in percentage of the
     *        container width, between the container's edges and the
     *        notification
     * @param verticalMargin The vertical margin, in percentage of the
     *        container height, between the container's edges and the
     *        notification
     */
    public void setMargin(float horizontalMargin, float verticalMargin) {
        mHorizontalMargin = horizontalMargin;
        mVerticalMargin = verticalMargin;
    }
 
    /**
     * Return the horizontal margin.
     */
    public float getHorizontalMargin() {
        return mHorizontalMargin;
    }
 
    /**
     * Return the vertical margin.
     */
    public float getVerticalMargin() {
        return mVerticalMargin;
    }
 
    /**
     * Set the location at which the notification should appear on the screen.
     * @see android.view.Gravity
     * @see #getGravity
     */
    public void setGravity(int gravity, int xOffset, int yOffset) {
        mGravity = gravity;
        mX = xOffset;
        mY = yOffset;
    }
 
     /**
     * Get the location at which the notification should appear on the screen.
     * @see android.view.Gravity
     * @see #getGravity
     */
    public int getGravity() {
        return mGravity;
    }
 
    /**
     * Return the X offset in pixels to apply to the gravity's location.
     */
    public int getXOffset() {
        return mX;
    }
     
    /**
     * Return the Y offset in pixels to apply to the gravity's location.
     */
    public int getYOffset() {
        return mY;
    }
     
    /**
     * schedule handleShow into the right thread
     */
    public void show() {
    	mHandler.removeMessages(0);
        mHandler.post(mShow);
         
        if(mDuration > 0)
        {
            mHandler.postDelayed(mHide, mDuration);
        }
    }
 
    /**
     * schedule handleHide into the right thread
     */
    public void hide() {
        mHandler.post(mHide);
    }
     
    private final Runnable mShow = new Runnable() {
        public void run() {
            handleShow();
        }
    };
 
    private final Runnable mHide = new Runnable() {
        public void run() {
            handleHide();
        }
    };
 
    private void init(Context context)
    {   
         final WindowManager.LayoutParams params = mParams;
         params.height = WindowManager.LayoutParams.WRAP_CONTENT;
         params.width = WindowManager.LayoutParams.WRAP_CONTENT;
         params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                 | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                 | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
         params.format = PixelFormat.TRANSLUCENT;
         params.windowAnimations = android.R.style.Animation_Toast;
         params.type = WindowManager.LayoutParams.TYPE_TOAST;
         params.setTitle("Toast");
          
         mWM = (WindowManager) context.getApplicationContext()
                 .getSystemService(Context.WINDOW_SERVICE);
    }
     
     
    private void handleShow() {
 
        if (mView != mNextView) {
            // remove the old view if necessary
            handleHide();
            mView = mNextView;
//            mWM = WindowManagerImpl.getDefault();
            final int gravity = mGravity;
            mParams.gravity = gravity;
            if ((gravity & Gravity.HORIZONTAL_GRAVITY_MASK) == Gravity.FILL_HORIZONTAL) 
            {
                mParams.horizontalWeight = 1.0f;
            }
            if ((gravity & Gravity.VERTICAL_GRAVITY_MASK) == Gravity.FILL_VERTICAL) 
            {
                mParams.verticalWeight = 1.0f;
            }
            mParams.x = mX;
            mParams.y = mY;
            mParams.verticalMargin = mVerticalMargin;
            mParams.horizontalMargin = mHorizontalMargin;
            if (mView.getParent() != null) 
            {
                mWM.removeView(mView);
            }
            mWM.addView(mView, mParams);
        }
    }
 
    private void handleHide() 
    {
        if (mView != null) 
        {
            if (mView.getParent() != null) 
            {
                mWM.removeView(mView);
            }
            mView = null;
        }
    }
}