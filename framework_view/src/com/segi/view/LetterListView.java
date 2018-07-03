package com.segi.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * 字母列表选择器
 * fix by kinwee
 * @author liangzx
 *
 */
public class LetterListView extends View {
    private static final String TAG = "MyLetterListView";
    private static final String[] letters = {"A","B","C","D","E","F","G","H","I","J","K","L"
            ,"M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z","#"};
    private OnLetterTouchListener letterTouchListener;

    public LetterListView(Context context) {
        super(context);
        init(context);
    }

    public LetterListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LetterListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context){
    }

    /**
     * 每一项的高度
     */
    private float itemHeight = -1;
    private Paint paint;
    private Bitmap letterBitmap;
    private int mTxtColor = Color.BLACK;
    @Override
    protected void onDraw(Canvas canvas) {
        if(letters == null){
            return ;
        }
        itemHeight = getHeight() /letters.length;
        //初始化画笔
        paint = new Paint();
        paint.setTextSize(itemHeight / 2);
        paint.setFakeBoldText(true);
        //字体颜色
        paint.setColor(mTxtColor);
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);

        //创建一张包含所有列表的图
        Canvas mCanvas = new Canvas();
        letterBitmap = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        //Logger.v(TAG, "width,height" + getMeasuredWidth() + ":" + getMeasuredHeight());
        mCanvas.setBitmap(letterBitmap);
        float widthCenter = getMeasuredWidth() / 2.0F;
        //画字符上图片中
        for(int i = 0 ; i < letters.length; i ++){
            mCanvas.drawText(letters[i], widthCenter-paint.measureText(letters[i])/2,
                    itemHeight * i +itemHeight,paint);
        }
        if(letterBitmap != null){//图片不为空就画图
            canvas.drawBitmap(letterBitmap,0,0,paint);
        }
        super.onDraw(canvas);
    }

    /**
     * 设置文字颜色
     * @param color
     */
    public void setTextColor(int color)
    {
        paint = null;
        mTxtColor = color;
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        if(letterTouchListener == null || letters == null){
            return false;
        }
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                int position = (int) (event.getY() / itemHeight);
                if(position >= 0 && position < letters.length){
                    letterTouchListener.onLetterTouch(letters[position],position);
                }
                return true;
            case MotionEvent.ACTION_OUTSIDE:
            case MotionEvent.ACTION_UP:
                letterTouchListener.onActionUp();
                return true;
        }
        return false;
    }

    /**
     * 设置点击某个字母的时候
     * @param listener
     */
    public void setOnLetterTouchListener (OnLetterTouchListener listener){
        this.letterTouchListener = listener;
    }

    public interface OnLetterTouchListener{

        /**
         * 某个字母被按下的时候
         * @param letter
         * @param position
         */
        public void onLetterTouch(String letter, int position);

        /**
         * 触控手指离开的时候
         */
        public void onActionUp();
    }
}
