package com.segi.view.calendar.views;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Scroller;

import com.segi.view.calendar.bizs.calendars.MPCManager;
import com.segi.view.calendar.bizs.decors.DPDecor;
import com.segi.view.calendar.bizs.languages.DPLManager;
import com.segi.view.calendar.bizs.themes.DPTManager;
import com.segi.view.calendar.cons.DPMode;
import com.segi.view.calendar.entities.DPInfo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * YearView
 *
 * @author AigeStudio 2016-07-25
 */
public class YearView extends View {
    private final Region[][] MONTH_REGIONS_2 = new Region[2][6];

    private final DPInfo[][] INFO_2 = new DPInfo[2][6];

    private final Map<String, List<Region>> REGION_SELECTED = new HashMap<String, List<Region>>();

    private MPCManager mCManager = MPCManager.getInstance();
    private DPTManager mTManager = DPTManager.getInstance();

    protected Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG |
            Paint.LINEAR_TEXT_FLAG);
    private Scroller mScroller;
    private DecelerateInterpolator decelerateInterpolator = new DecelerateInterpolator();
    private AccelerateInterpolator accelerateInterpolator = new AccelerateInterpolator();
    private OnDateChangeListener onDateChangeListener;
    private MonthPicker.OnDatePickedListener onDatePickedListener;
    private ScaleAnimationListener scaleAnimationListener;

    private DPMode mDPMode = DPMode.MULTIPLE;
    private SlideMode mSlideMode;
    private DPDecor mDPDecor;

    private int circleRadius;
    private int indexYear;
    private int centerYear;
    private int leftYear;
    private int rightYear;
    private int width, height;
    private int lastPointX;
    private int lastMoveX;
    private int criticalWidth;
    private int animZoomOut1, animZoomIn1, animZoomOut2;

    private float sizeTextGregorian;

    private boolean isNewEvent, isTodayDisplay = false;
    
    private boolean isFirst = true;

    private boolean animStarting = false;

    private Map<String, BGCircle> cirApr = new HashMap<String, BGCircle>();
    private Map<String, BGCircle> cirDpr = new HashMap<String, BGCircle>();

    private List<String> dateSelected = new ArrayList<String>();

    public YearView(Context context) {
        super(context);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            scaleAnimationListener = new ScaleAnimationListener();
        }
        mScroller = new Scroller(context);
        mPaint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        return super.onSaveInstanceState();
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            invalidate();
        } else {
            requestLayout();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mScroller.forceFinished(true);
                mSlideMode = null;
                isNewEvent = true;
                lastPointX = (int) event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                if (isNewEvent) {
                    if (Math.abs(lastPointX - event.getX()) > 100) {
                        mSlideMode = SlideMode.HOR;
                        isNewEvent = false;
                    }
                }
                if (mSlideMode == SlideMode.HOR) {
                    int totalMoveX = (int) (lastPointX - event.getX()) + lastMoveX;
                    smoothScrollTo(totalMoveX, height);
                }
                break;
            case MotionEvent.ACTION_UP:
            	if (mSlideMode == SlideMode.HOR) {
                    if (Math.abs(lastPointX - event.getX()) > 25) {
                        if (lastPointX > event.getX() &&
                                Math.abs(lastPointX - event.getX()) >= criticalWidth) {
                            indexYear++;
                            centerYear++;
                        } else if (lastPointX < event.getX() &&
                                Math.abs(lastPointX - event.getX()) >= criticalWidth) {
                        	indexYear--;
                            centerYear--;
                        }
                        buildRegion();
                        computeDate();
                        smoothScrollTo(width * indexYear, height);
                        lastMoveX = width * indexYear;
                    } else {
                        defineRegion((int) event.getX(), (int) event.getY());
                    }
                } else {
                    defineRegion((int) event.getX(), (int) event.getY());
                }
                break;
        }
        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int measureWidth = MeasureSpec.getSize(widthMeasureSpec);
        setMeasuredDimension(measureWidth, (int) (measureWidth * 3F / 5F));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        width = w;
        height = h;

        criticalWidth = (int) (1F / 5F * width);

        int cellW = (int) (w / 6F);
        int cellH4 = (int) (h / 4F);

        circleRadius = cellW;

        animZoomOut1 = (int) (cellW * 1.2F);
        animZoomIn1 = (int) (cellW * 0.8F);
        animZoomOut2 = (int) (cellW * 1.1F);

        sizeTextGregorian = width / 20F;
        mPaint.setTextSize(sizeTextGregorian);

        for (int i = 0; i < MONTH_REGIONS_2.length; i++) {
            for (int j = 0; j < MONTH_REGIONS_2[i].length; j++) {
                Region region = new Region();
                region.set((j * cellW), (i * cellH4) + cellW, cellW + (j * cellW),
                       2 * cellW + (i * cellH4));
                MONTH_REGIONS_2[i][j] = region;
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(mTManager.colorBG());

        draw(canvas, width * (indexYear - 1), height, leftYear);
        draw(canvas, width * indexYear, height, centerYear);
        draw(canvas, width * (indexYear + 1), height, rightYear);
        if (isFirst) {
        	isFirst = false;
        	smoothScrollTo(width * indexYear, height);
		}
        drawBGCircle(canvas);
        
    }

    private void drawBGCircle(Canvas canvas) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            for (String s : cirDpr.keySet()) {
                BGCircle circle = cirDpr.get(s);
                drawBGCircle(canvas, circle);
            }
        }
        for (String s : cirApr.keySet()) {
            BGCircle circle = cirApr.get(s);
            drawBGCircle(canvas, circle);
        }
    }

    private void drawBGCircle(Canvas canvas, BGCircle circle) {
        canvas.save();
        canvas.translate(circle.getX() - circle.getRadius() / 2,
                circle.getY() - circle.getRadius() / 2);
        circle.getShape().getShape().resize(circle.getRadius(), circle.getRadius());
        circle.getShape().draw(canvas);
        canvas.restore();
    }

    private void draw(Canvas canvas, int x, int y, int year) {
        canvas.save();
        canvas.translate(x, y);
        DPInfo[][] info = mCManager.obtainDPInfo(year);
        DPInfo[][] result;
        Region[][] tmp;
        tmp = MONTH_REGIONS_2;
        arrayClear(INFO_2);
        result = arrayCopy(info, INFO_2);
        for (int i = 0; i < result.length; i++) {
            for (int j = 0; j < result[i].length; j++) {
                draw(canvas, tmp[i][j].getBounds(), info[i][j]);
            }
        }
        canvas.restore();
    }

    private void draw(Canvas canvas, Rect rect, DPInfo info) {
        drawBG(canvas, rect, info);
        drawGregorian(canvas, rect, info.strF, info);
    }

    private void drawBG(Canvas canvas, Rect rect, DPInfo info) {
        if (null != mDPDecor && info.isDecorBG) {
            mDPDecor.drawDecorBG(canvas, rect, mPaint, centerYear + "-" + info.strG);
        }
    }


    private void drawGregorian(Canvas canvas, Rect rect, String str, DPInfo info) {
        mPaint.setTextSize(sizeTextGregorian);
        mPaint.setStyle(Style.FILL);
    	if (info.isToday) {
        	mPaint.setColor(mTManager.colorToday());
		} else {
            mPaint.setColor(mTManager.colorG());
        }
        float y = rect.centerY();
        canvas.drawText(str, rect.centerX(), y, mPaint);
        if (info.isToday && isTodayDisplay) {
        	mPaint.setStyle(Style.FILL);
        	mPaint.setTextSize(sizeTextGregorian * 2 / 3);
        	canvas.drawText(DPLManager.getInstance().thisMonth(), rect.centerX(),
                    y + (float)(circleRadius / 3.2F), mPaint);
        } 
    }


    List<String> getDateSelected() {
        return dateSelected;
    }

    void setOnDateChangeListener(OnDateChangeListener onDateChangeListener) {
        this.onDateChangeListener = onDateChangeListener;
    }

    public void setOnDatePickedListener(MonthPicker.OnDatePickedListener onDatePickedListener) {
        this.onDatePickedListener = onDatePickedListener;
    }

    void setDPMode(DPMode mode) {
        this.mDPMode = mode;
    }

    void setDPDecor(DPDecor decor) {
        this.mDPDecor = decor;
    }

    DPMode getDPMode() {
        return mDPMode;
    }

    void setDate(int year, int month) {
        centerYear = year;
        indexYear = 0;
        buildRegion();
        computeDate();
        requestLayout();
        invalidate();
    }


    void setTodayDisplay(boolean isTodayDisplay) {
        this.isTodayDisplay = isTodayDisplay;
    }


    private void smoothScrollTo(int fx, int fy) {
        int dx = fx - mScroller.getFinalX();
        int dy = fy - mScroller.getFinalY();
        smoothScrollBy(dx, dy);
    }

    private void smoothScrollBy(int dx, int dy) {
        mScroller.startScroll(mScroller.getFinalX(), mScroller.getFinalY(), dx, dy, 500);
        invalidate();
    }

    private BGCircle createCircle(float x, float y) {
        OvalShape circle = new OvalShape();
        circle.resize(0, 0);
        ShapeDrawable drawable = new ShapeDrawable(circle);
        BGCircle circle1 = new BGCircle(drawable);
        circle1.setX(x);
        circle1.setY(y);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            circle1.setRadius((int) (circleRadius / 2F));
        }
        drawable.getPaint().setColor(mTManager.colorBGCircle());
        return circle1;
    }

    private void buildRegion() {
        String key = Integer.toString(indexYear);
        if (!REGION_SELECTED.containsKey(key)) {
            REGION_SELECTED.put(key, new ArrayList<Region>());
        }
    }

    private void arrayClear(DPInfo[][] info) {
        for (DPInfo[] anInfo : info) {
            Arrays.fill(anInfo, null);
        }
    }

    private DPInfo[][] arrayCopy(DPInfo[][] src, DPInfo[][] dst) {
        for (int i = 0; i < dst.length; i++) {
            System.arraycopy(src[i], 0, dst[i], 0, dst[i].length);
        }
        return dst;
    }

    private void defineRegion(int x, int y) {
        if (animStarting) {
            return;
        }
        Region[][] tmp = MONTH_REGIONS_2;
        for (int i = 0; i < tmp.length; i++) {
            for (int j = 0; j < tmp[i].length; j++) {
                Region region = tmp[i][j];
                if (TextUtils.isEmpty(mCManager.obtainDPInfo(centerYear)[i][j].strG)) {
                    continue;
                }
                if (region.contains(x, y)) {
                    List<Region> regions = REGION_SELECTED.get(Integer.toString(indexYear));
                    if (mDPMode == DPMode.SINGLE) {
                        cirApr.clear();
                        regions.add(region);
                        final String date = centerYear + "-" + mCManager.obtainDPInfo(centerYear)[i][j].strG;
                        BGCircle circle = createCircle(
                                region.getBounds().centerX() + indexYear * width,
                                region.getBounds().centerY() + height);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                            ValueAnimator animScale4 =
                                    ObjectAnimator.ofInt(circle, "radius", animZoomOut2, (int)(circleRadius / 2F));
                            animScale4.setDuration(400);
                            animScale4.setInterpolator(accelerateInterpolator);
                            animScale4.addUpdateListener(scaleAnimationListener);

                            AnimatorSet animSet = new AnimatorSet();
                            animSet.play(animScale4);
                            animSet.addListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationStart(Animator animation) {
                                    animStarting = true;
                                }
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    animStarting = false;
                                    if (null != onDatePickedListener) {
                                        onDatePickedListener.onDatePicked(date);
                                    }
                                }
                            });
                            animSet.start();
                        }
                        cirApr.put(date, circle);
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
                            invalidate();
                            if (null != onDatePickedListener) {
                                onDatePickedListener.onDatePicked(date);
                            }
                        }
                    } else if (mDPMode == DPMode.MULTIPLE) {
                        if (regions.contains(region)) {
                            regions.remove(region);
                        } else {
                            regions.add(region);
                        }
                        final String date = centerYear + "-" + mCManager.obtainDPInfo(centerYear)[i][j].strG;
                        if (dateSelected.contains(date)) {
                            dateSelected.remove(date);
                            BGCircle circle = cirApr.get(date);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                                ValueAnimator animScale = ObjectAnimator.ofInt(circle, "radius", circleRadius, 0);
                                animScale.setDuration(250);
                                animScale.setInterpolator(accelerateInterpolator);
                                animScale.addUpdateListener(scaleAnimationListener);
                                animScale.addListener(new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationStart(Animator animation) {
                                        animStarting = true;
                                    }
                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        animStarting = false;
                                        cirDpr.remove(date);
                                    }
                                });
                                animScale.start();
                                cirDpr.put(date, circle);
                            }
                            cirApr.remove(date);
                            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
                                invalidate();
                            }
                        } else {
                            dateSelected.add(date);
                            BGCircle circle = createCircle(
                                    region.getBounds().centerX() + indexYear * width,
                                    region.getBounds().centerY() + height);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                                ValueAnimator animScale1 =
                                        ObjectAnimator.ofInt(circle, "radius", 0, animZoomOut1);
                                animScale1.setDuration(250);
                                animScale1.setInterpolator(decelerateInterpolator);
                                animScale1.addUpdateListener(scaleAnimationListener);

                                ValueAnimator animScale2 =
                                        ObjectAnimator.ofInt(circle, "radius", animZoomOut1, animZoomIn1);
                                animScale2.setDuration(100);
                                animScale2.setInterpolator(accelerateInterpolator);
                                animScale2.addUpdateListener(scaleAnimationListener);

                                ValueAnimator animScale3 =
                                        ObjectAnimator.ofInt(circle, "radius", animZoomIn1, animZoomOut2);
                                animScale3.setDuration(150);
                                animScale3.setInterpolator(decelerateInterpolator);
                                animScale3.addUpdateListener(scaleAnimationListener);

                                ValueAnimator animScale4 =
                                        ObjectAnimator.ofInt(circle, "radius", animZoomOut2, circleRadius);
                                animScale4.setDuration(50);
                                animScale4.setInterpolator(accelerateInterpolator);
                                animScale4.addUpdateListener(scaleAnimationListener);

                                AnimatorSet animSet = new AnimatorSet();
                                animSet.playSequentially(animScale1, animScale2, animScale3, animScale4);
                                animSet.start();
                            }
                            cirApr.put(date, circle);
                            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
                                invalidate();
                            }
                        }
                    } else if (mDPMode == DPMode.BETWEEN) {
                    	if (regions.contains(region)) {
                            regions.remove(region);
                        } else {
                            regions.add(region);
                        }
                        final String date = centerYear + "-" + mCManager.obtainDPInfo(centerYear)[i][j].strG;
                        if (dateSelected.contains(date)) {
                            removeCircle(date);
                        } else {
                        	if (0 < dateSelected.size()) {
                            	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                            	try {
                            		Date nowChooseDate = sdf.parse(date);
                            		Date beforeDate =sdf.parse(dateSelected.get(0));
                            		if (nowChooseDate.before(beforeDate)) {
                            			if (dateSelected.size() > 1) {
                            				removeCircle(dateSelected.remove(0));
                            			}
                            			dateSelected.add(0, date);
                            		}else{
                            			if (dateSelected.size() == 1) {
                            				dateSelected.add(date);
                            			}else {
                            				removeCircle(dateSelected.remove(1));
                            				dateSelected.add(date);
                                		}
                            		}
                            	} catch (ParseException e) {
                            		e.printStackTrace();
                            	}
    						}else{
    							dateSelected.add(date);
    						}
                            BGCircle circle = createCircle(
                                    region.getBounds().centerX() + indexYear * width,
                                    region.getBounds().centerY() + height);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                                ValueAnimator animScale1 =
                                        ObjectAnimator.ofInt(circle, "radius", 0, animZoomOut1);
                                animScale1.setDuration(250);
                                animScale1.setInterpolator(decelerateInterpolator);
                                animScale1.addUpdateListener(scaleAnimationListener);

                                ValueAnimator animScale2 =
                                        ObjectAnimator.ofInt(circle, "radius", animZoomOut1, animZoomIn1);
                                animScale2.setDuration(100);
                                animScale2.setInterpolator(accelerateInterpolator);
                                animScale2.addUpdateListener(scaleAnimationListener);

                                ValueAnimator animScale3 =
                                        ObjectAnimator.ofInt(circle, "radius", animZoomIn1, animZoomOut2);
                                animScale3.setDuration(150);
                                animScale3.setInterpolator(decelerateInterpolator);
                                animScale3.addUpdateListener(scaleAnimationListener);

                                ValueAnimator animScale4 =
                                        ObjectAnimator.ofInt(circle, "radius", animZoomOut2, circleRadius);
                                animScale4.setDuration(50);
                                animScale4.setInterpolator(accelerateInterpolator);
                                animScale4.addUpdateListener(scaleAnimationListener);

                                AnimatorSet animSet = new AnimatorSet();
                                animSet.playSequentially(animScale1, animScale2, animScale3, animScale4);
                                animSet.start();
                            }
                            cirApr.put(date, circle);
                            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
                                invalidate();
                            }
                        }
					} else if (mDPMode == DPMode.NONE) {
                        if (regions.contains(region)) {
                            regions.remove(region);
                        } else {
                            regions.add(region);
                        }
                        final String date = centerYear + "-" + mCManager.obtainDPInfo(centerYear)[i][j].strG;
                        if (dateSelected.contains(date)) {
                            dateSelected.remove(date);
                        } else {
                            dateSelected.add(date);
                        }
                    }
                }
            }
        }
    }
    
    /**
     * 去掉多余的选中日期
     */
    private void removeCircle(final String date)
    {
    	dateSelected.remove(date);
    	BGCircle circle = cirApr.get(date);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            ValueAnimator animScale = ObjectAnimator.ofInt(circle, "radius", circleRadius, 0);
            animScale.setDuration(250);
            animScale.setInterpolator(accelerateInterpolator);
            animScale.addUpdateListener(scaleAnimationListener);
            animScale.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    animStarting = true;
                }
                @Override
                public void onAnimationEnd(Animator animation) {
                    animStarting = false;
                    cirDpr.remove(date);
                }
            });
            animScale.start();
            cirDpr.put(date, circle);
        }
        cirApr.remove(date);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            invalidate();
        }
    }

    private void computeDate() {
        leftYear = centerYear - 1;
        rightYear = centerYear + 1;
        if (null != onDateChangeListener) {
            onDateChangeListener.onYearChange(centerYear);
        }
    }

    public interface OnDateChangeListener {
        void onYearChange(int year);
    }

    private enum SlideMode {
        VER,
        HOR
    }

    private class BGCircle {
        private float x, y;
        private int radius;

        private ShapeDrawable shape;

        public BGCircle(ShapeDrawable shape) {
            this.shape = shape;
        }

        public float getX() {
            return x;
        }

        public void setX(float x) {
            this.x = x;
        }

        public float getY() {
            return y;
        }

        public void setY(float y) {
            this.y = y;
        }

        public int getRadius() {
            return radius;
        }

        public void setRadius(int radius) {
            this.radius = radius;
        }

        public ShapeDrawable getShape() {
            return shape;
        }

        public void setShape(ShapeDrawable shape) {
            this.shape = shape;
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private class ScaleAnimationListener implements ValueAnimator.AnimatorUpdateListener {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            YearView.this.invalidate();
        }
    }
}
