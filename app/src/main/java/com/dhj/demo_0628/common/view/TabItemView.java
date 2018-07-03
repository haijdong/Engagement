package com.dhj.demo_0628.common.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.ColorInt;
import android.support.annotation.Dimension;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dhj.demo_0628.R;

import static com.dhj.demo_0628.utils.DensityUtil.sp2px;


/**
 * 底部TabItemView(图片，文本)
 *
 * @author shenghaiyang
 */
public class TabItemView extends LinearLayout {

    /**
     * 默认文字大小，单位px
     */
    @Dimension
    private static final int DEFAULT_TEXT_SIZE = sp2px(14);
    /**
     * 图标
     */
    private final ImageView mIconView;
    /**
     * 文字
     */
    private final TextView mTextView;

    public TabItemView(Context context) {
        this(context, null);
    }

    public TabItemView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TabItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setGravity(Gravity.CENTER);
        setOrientation(VERTICAL);
        //不直接new是因为LayoutInflater可能会有其他factory
        LayoutInflater inflater = LayoutInflater.from(context);
        mIconView = (ImageView) inflater.inflate(R.layout.xp_default_tab_icon, this, false);
        mTextView = (TextView) inflater.inflate(R.layout.xp_default_tab_text, this, false);
        addView(mIconView);
        addView(mTextView);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TabItemView,
                defStyleAttr, R.style.TabItemView);
        Drawable icon = a.getDrawable(R.styleable.TabItemView_xp_icon);
        String text = a.getString(R.styleable.TabItemView_xp_text);
        ColorStateList textColor = a.getColorStateList(R.styleable.TabItemView_xp_textColor);
        int textSize = a.getDimensionPixelSize(R.styleable.TabItemView_xp_textSize, DEFAULT_TEXT_SIZE);
        a.recycle();
        if (text != null) {
            mTextView.setText(text);
        }
        if (icon != null) {
            mIconView.setImageDrawable(icon);
        }
        if (textColor != null) {
            mTextView.setTextColor(textColor);
        }
        mTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
    }

    /**
     * 设置图标
     *
     * @param resId 图标资源id
     */
    public void setIconResource(@DrawableRes int resId) {
        mIconView.setImageResource(resId);
    }

    /**
     * 设置图标
     *
     * @param drawable 图标drawable
     */
    public void setIconDrawable(@Nullable Drawable drawable) {
        mIconView.setImageDrawable(drawable);
    }

    /**
     * 设置文本
     *
     * @param resId 文本资源id
     */
    public void setText(@StringRes int resId) {
        mTextView.setText(resId);
    }

    /**
     * 设置文本
     */
    public void setText(CharSequence text) {
        mTextView.setText(text);
    }

    /**
     * 设置文字颜色
     *
     * @param color 颜色
     */
    public void setTextColor(@ColorInt int color) {
        mTextView.setTextColor(color);
    }

    /**
     * 设置文字颜色
     *
     * @param colors 颜色
     */
    public void setTextColor(ColorStateList colors) {
        mTextView.setTextColor(colors);
    }

    /**
     * 设置文字大小，单位sp
     */
    public void setTextSize(float size) {
        mTextView.setTextSize(size);
    }

    /**
     * 设置文字大小
     */
    public void setTextSize(int unit, float size) {
        mTextView.setTextSize(unit, size);
    }

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        mIconView.setSelected(selected);
        mTextView.setSelected(selected);
    }

    static class SavedState extends BaseSavedState {
        boolean selected;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            selected = (Boolean) in.readValue(null);
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeValue(selected);
        }

        @Override
        public String toString() {
            return "CheckedTextView.SavedState{"
                    + Integer.toHexString(System.identityHashCode(this))
                    + " selected=" + selected + "}";
        }

        public static final Creator<SavedState> CREATOR
                = new Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);
        ss.selected = isSelected();
        return ss;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        setSelected(ss.selected);
        requestLayout();
    }
}
