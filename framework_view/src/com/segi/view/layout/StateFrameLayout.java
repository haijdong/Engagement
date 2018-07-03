package com.segi.view.layout;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * 带属性状态的FrameLayout，配合
 * {@link EditableImageLayout}使用
 * Created by Shaohua Song on 2017/8/12.
 */

public class StateFrameLayout extends FrameLayout {

    private EditableImageLayout.ViewType state;//视图的类型

    private EditableImageLayout.UrlType urlType;//图片的地址类型

    public StateFrameLayout(@NonNull Context context) {
        super(context);
    }

    public StateFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public StateFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public EditableImageLayout.ViewType getState() {
        return state;
    }

    public void setState(EditableImageLayout.ViewType state) {
        this.state = state;
    }

    public EditableImageLayout.UrlType getUrlType() {
        return urlType;
    }

    public void setUrlType(EditableImageLayout.UrlType urlType) {
        this.urlType = urlType;
    }
}