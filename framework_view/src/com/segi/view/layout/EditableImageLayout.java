package com.segi.view.layout;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.segi.view.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 批量添加图片显示，支持本地图片和网络图片
 * 编辑模式和浏览模式
 *
 */
public class EditableImageLayout extends FlowLayout {

    private int deleteImageSrc = R.drawable.alert_update_top_close;//删除按钮
    private int addImageSrc = R.drawable.addpicture_btn;//图片
    private final static int IMAGE_DELETE = R.id.pic_option_delete;
    private final static int IMAGE_ID = R.id.pic_option_image;

    private int maxViewNumber = Integer.MAX_VALUE;//最大的图片数量
    private int rowColNumber = 1;//每行控件的数量

    public static final int MODE_BROWS = 1;//浏览模式
    public static final int MODE_EDIT = 2;//编辑模式

    /**
     * 操作模式
     * 浏览模式 = 1
     * 编辑模式 = 2
     */
    private int optionMode = MODE_BROWS;


    private Context context;
    private List<ImageInfo> imageInfos = new ArrayList<>();//图片列表

    /**
     * 两张图片之间的间距
     */
    private int paddingPx = getResources().getDimensionPixelSize(R.dimen.x30);
    private int childWidth;
    private boolean hasMeasured = false;//是否测量完毕
    OnImageViewOptionListener mListener;

    public EditableImageLayout(Context context) {
        this(context, null);
    }

    public EditableImageLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EditableImageLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.EditableImageLayout);
        if (array.hasValue(R.styleable.EditableImageLayout_maxViewNumber)) {
            maxViewNumber = array.getInteger(R.styleable.EditableImageLayout_maxViewNumber, 0);
        }
        if (array.hasValue(R.styleable.EditableImageLayout_rowColNumber)) {
            rowColNumber = array.getInteger(R.styleable.EditableImageLayout_rowColNumber, 0);
        }
        if (array.hasValue(R.styleable.EditableImageLayout_optionMode)) {
            optionMode = array.getInteger(R.styleable.EditableImageLayout_optionMode, MODE_BROWS);
        }
        if (array.hasValue(R.styleable.EditableImageLayout_addImageSrc)) {
            addImageSrc = array.getResourceId(R.styleable.EditableImageLayout_addImageSrc, addImageSrc);
        }
        if (array.hasValue(R.styleable.EditableImageLayout_addImageSrc)) {
            deleteImageSrc = array.getResourceId(R.styleable.EditableImageLayout_deleteImageSrc, deleteImageSrc);
        }
        if (array.hasValue(R.styleable.EditableImageLayout_imagePadding)) {
            paddingPx = array.getDimensionPixelSize(R.styleable.EditableImageLayout_imagePadding, 0);
        }
        if (array.hasValue(R.styleable.EditableImageLayout_deleteImageSize)) {
            deleteImageSize = array.getDimensionPixelSize(R.styleable.EditableImageLayout_deleteImageSize, 0);
        }
        if (array.hasValue(R.styleable.EditableImageLayout_deleteImageMarginTop)) {
            deleteImageMarginTop = array.getDimensionPixelSize(R.styleable.EditableImageLayout_deleteImageMarginTop, 0);
        }
        if (array.hasValue(R.styleable.EditableImageLayout_deleteImageMarginRight)) {
            deleteImageMarginRight = array.getDimensionPixelSize(R.styleable.EditableImageLayout_deleteImageMarginRight, 0);
        }
        array.recycle();
    }


    /**
     * 视图的大小参数设置都在onMeasure中，只有onMeasure执行了成功了，才能设置child视图的大小
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int contentWidth = sizeWidth - paddingLeft - paddingRight;
        childWidth = contentWidth / rowColNumber;
        setChildLayoutParama(new EditableImageLayout.MarginLayoutParams(childWidth, childWidth));
        if (!hasMeasured) {
            hasMeasured = true;
            updateView(imageInfos.size());
        }
    }

    /**
     * 设置item的layoutparama
     *
     * @param params
     */
    public void setChildLayoutParama(EditableImageLayout.MarginLayoutParams params) {
        childWidth = params.width;
    }

    /**
     * 设置最大数量，必须在添加视图之前调用
     *
     * @param maxViewNumber
     */
    public void setMaxViewNumber(int maxViewNumber) {
        this.maxViewNumber = maxViewNumber;
    }

    /**
     * 设置每行视图的数量，必须在添加视图之前调用
     *
     * @param rowColNumber
     */
    public void setRowColNumber(int rowColNumber) {
        this.rowColNumber = rowColNumber;
        removeAllViews();
        //需要更新
        if (hasMeasured) {
            updateView(this.imageInfos.size());
        }
        requestLayout();
    }

    /**
     * 切换编辑模式
     * @param mode 模式
     */
    public void setOptionMode(int mode) {
        if (mode != MODE_BROWS && mode != MODE_EDIT) {
            mode = MODE_BROWS;
        }

        if (!hasMeasured) {
            hasMeasured = true;
            if (mode == MODE_EDIT) {
                if (!hasEmptyView() && getChildCount() < maxViewNumber) {
                    if (childWidth > 0) {
                        addNewEmptyView(getChildCount(), null, UrlType.LOCAL_FILE);
                    } else {
                        hasMeasured = false;
                    }
                }
            } else {
                if (hasEmptyView()) {
                    removeEmptyView();
                }
            }
            updateViewMode(mode);
        } else if (mode != optionMode) {
            if (mode == MODE_EDIT) {
                if (!hasEmptyView() && getChildCount() < maxViewNumber) {
                    if (childWidth > 0) {
                        addNewEmptyView(getChildCount(), null, UrlType.LOCAL_FILE);
                    } else {
                        hasMeasured = false;
                    }
                }
            } else {
                if (hasEmptyView()) {
                    removeEmptyView();
                }
            }
            updateViewMode(mode);
        }
        optionMode = mode;
    }

    public int getOptionMode() {
        return optionMode;
    }

    public String getOptionModeName() {
        if (optionMode == MODE_EDIT) {
            return "MODE_EDIT";
        } else {
            return "MODE_BROWS";
        }
    }

    /**
     * 设置控件模式，
     * 查看模式，不需要+号，
     * 编辑模式，保留空
     *
     * @param mode
     */
    private void updateViewMode(int mode) {
        if (mode == MODE_BROWS) {
            for (int i = 0; i < getChildCount(); i++) {
                updateChildViewStateAt(i, ViewType.ONLY_BROWS);
            }
        } else {
            for (int i = 0; i < getChildCount(); i++) {
                ImageView imageView = (ImageView) getChildAt(i).findViewById(IMAGE_ID);
                if (isEmptyView(imageView)) {
                    updateChildViewStateAt(i, ViewType.EMPTY);
                } else {
                    updateChildViewStateAt(i, ViewType.DELETE_ABLE);
                }
            }
        }
    }

    public boolean isEmptyView(ImageView view) {
        if (view.getTag(IMAGE_ID) == null) {
            return true;
        }
        return false;
    }

    /**
     * 移除空的视图
     */
    private void removeEmptyView() {
        for (int i = 0; i < getChildCount(); i++) {
            if (getChildAt(i).findViewById(IMAGE_ID).getTag(IMAGE_ID) == null) {
                removeViewAt(i);
                i--;
            }
        }
    }

    private void updateChildViewStateAt(int position, ViewType state) {
        StateFrameLayout layout = (StateFrameLayout) getChildAt(position);
        layout.setLayoutParams(new MarginLayoutParams(childWidth, childWidth));
        layout.setState(state);
        if (layout.getState() == ViewType.ONLY_BROWS) {
            layout.findViewById(IMAGE_DELETE).setVisibility(INVISIBLE);
        } else if (layout.getState() == ViewType.DELETE_ABLE) {
            layout.findViewById(IMAGE_DELETE).setVisibility(VISIBLE);
        } else if (layout.getState() == ViewType.EMPTY) {
            layout.findViewById(IMAGE_DELETE).setVisibility(INVISIBLE);
        }
    }


    private int deleteImageMarginTop = 0;
    private int deleteImageMarginRight = 0;
    private int deleteImageSize = 30;


    /**
     * 设置删除按钮的大小和位置
     *
     * @param size
     * @param marginTop
     * @param marginRight
     */
    public void setDeltedImageParamas(int size, int marginTop, int marginRight) {
        deleteImageSize = size;
        deleteImageMarginTop = marginTop;
        deleteImageMarginRight = marginRight;
        if (getChildCount() > 0) {
            for (int i = 0; i < getChildCount(); i++) {
                FrameLayout.LayoutParams layp = new FrameLayout.LayoutParams(size, size);
                layp.setMargins(0, marginTop, marginRight, 0);
                layp.gravity = Gravity.CENTER;
                getChildAt(i).findViewById(IMAGE_DELETE).setLayoutParams(layp);
            }
            requestLayout();
        }
        invalidate();
    }

    public void setImageMargin(int leftMargin) {
        paddingPx = leftMargin;
        if (getChildCount() > 0) {
            for (int i = 0; i < getChildCount(); i++) {
                FrameLayout.LayoutParams layp = new FrameLayout.LayoutParams(childWidth - leftMargin, childWidth - leftMargin);
                layp.setMargins(leftMargin / 2, leftMargin / 2, leftMargin / 2, leftMargin / 2);
                layp.gravity = Gravity.CENTER;
                getChildAt(i).findViewById(IMAGE_ID).setLayoutParams(layp);
            }
            requestLayout();
        }
        invalidate();
    }

    public int getMaxSize() {
        return maxViewNumber;
    }

    public int getRowColNumber() {
        return rowColNumber;
    }


    public enum ViewType {
        /**
         * 空白的，可以添加
         */
        EMPTY,

        /**
         * 有图的，有删除按钮
         */
        DELETE_ABLE,

        /**
         * 有图的，无删除按钮，只是浏览的
         */
        ONLY_BROWS;

        @Override
        public String toString() {
            return super.toString();
        }
    }

    public enum UrlType {
        LOCAL_FILE, //本地
        URL;//网络

        @Override
        public String toString() {
            return super.toString();
        }
    }


    /**
     * 添加图片，添加之后，图片上就是删除按钮
     *
     * @param position
     * @param path
     */
    public void addNewEmptyView(int position, String path, UrlType sourceType) {
        createView(ViewType.EMPTY, position, path, sourceType);
    }


    /**
     * @param position
     * @param path
     * @param sourceType
     */
    private void addNewView(int position, String path, UrlType sourceType) {
        if (optionMode == MODE_BROWS) {
            createView(ViewType.ONLY_BROWS, position, path, sourceType);
        } else if (optionMode == MODE_EDIT) {
            createView(ViewType.DELETE_ABLE, position, path, sourceType);
        }
    }

    /**
     * 如果是编辑状态，数量小于max，插入倒数第二个位置
     * 如果数量等于max，移除最后一个，插入最后一个
     * <p>
     * 如果是浏览状态，数量小于max，插入最后一个
     *
     * @param path
     */
    public void addNewView(String path, UrlType sourceType) {
        if (optionMode == MODE_EDIT) {
            if (getChildCount() < maxViewNumber) {
                addNewView(getChildCount() - 1, path, sourceType);
            } else {
                removeViewAt(getChildCount() - 1);
                addNewView(getChildCount(), path, sourceType);
            }
        } else if (optionMode == MODE_BROWS) {
            addNewView(getChildCount() - 1, path, sourceType);
        }
        updateChildViewTags();
        updateUrls();
    }


    /**
     * @param position
     */
    private void deleteView(int position) {
        if (optionMode == MODE_BROWS) {
            imageInfos.remove(position);
            removeViewAt(position);
        } else if (optionMode == MODE_EDIT) {
            imageInfos.remove(position);
            removeViewAt(position);
            if (!hasEmptyView()) {
                addNewEmptyView(getChildCount(), null, UrlType.LOCAL_FILE);
            }
        }
        updateUrls();
        updateChildViewTags();
    }


    public boolean hasEmptyView() {
        if (getChildCount() == 0) {
            return false;
        }
        if (getChildAt(getChildCount() - 1).findViewById(IMAGE_ID).getTag(IMAGE_ID) == null) {
            return true;
        }
        return false;
    }

    /**
     * 更新tag
     */
    private void updateChildViewTags() {
        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).setTag(i);
        }
    }

    public static class ImageInfo implements Serializable {

        public ImageInfo(UrlType sourceType, String url) {
            this.sourceType = sourceType;
            this.url = url;
        }

        UrlType sourceType;
        String url;

        public UrlType getSourceType() {
            return sourceType;
        }

        public void setSourceType(UrlType sourceType) {
            this.sourceType = sourceType;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    /**
     * 添加图片链接，并设置图片
     *
     * @param urls
     */
    public void setImageListsAndShow(ArrayList<ImageInfo> urls) {
        this.imageInfos.clear();
        removeAllViews();
        int size = urls.size() < maxViewNumber ? urls.size() : maxViewNumber;//数据总长度
        this.imageInfos.addAll(urls.subList(0, size));
        //需要更新
        if (hasMeasured) {
            updateView(size);
        }
    }

    public List<ImageInfo> getImageInfos(){
        return imageInfos;
    }

    private void updateView(int size) {
        for (int i = 0; i < size; i++) {
//            this.imageInfos.add(imageInfos.get(i));
            addNewView(i, imageInfos.get(i).getUrl(), imageInfos.get(i).getSourceType());
        }
        if (optionMode == MODE_EDIT && size < maxViewNumber && !hasEmptyView()) {
            addNewEmptyView(size, null, UrlType.LOCAL_FILE);
        }
        updateChildViewTags();
        updateUrls();
    }

    /**
     * 添加 视图
     *
     * @return
     */
    private void createView(ViewType state, final int position, final String path, UrlType sourceType) {

        final StateFrameLayout layout = new StateFrameLayout(context);
        layout.setTag(position);
        layout.setUrlType(sourceType);
        layout.setState(state);
        childWidth = (getMeasuredWidth() - getPaddingLeft() - getPaddingRight()) / rowColNumber;
        MarginLayoutParams childParams = new MarginLayoutParams(childWidth, childWidth);
        layout.setLayoutParams(childParams);

        final ImageView mAddView = new ImageView(context);
        mAddView.setId(IMAGE_ID);
        mAddView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        FrameLayout.LayoutParams layp = new FrameLayout.LayoutParams(childWidth - paddingPx, childWidth - paddingPx);
        layp.setMargins(paddingPx / 2, paddingPx / 2, paddingPx / 2, paddingPx / 2);
        layp.gravity = Gravity.CENTER;
        mAddView.setLayoutParams(layp);
        layout.addView(mAddView);

        ImageView deleted = new ImageView(context);
        deleted.setId(IMAGE_DELETE);
        deleted.setImageResource(deleteImageSrc);
        FrameLayout.LayoutParams layp2 = new FrameLayout.LayoutParams(deleteImageSize, deleteImageSize);
        layp2.setMargins(0, deleteImageMarginTop, deleteImageMarginRight, 0);
        layp2.gravity = Gravity.TOP | Gravity.RIGHT;
        deleted.setLayoutParams(layp2);
        layout.addView(deleted);

        switch (state) {
            case EMPTY://无图片需要添加
                deleted.setVisibility(GONE);
                mAddView.setImageResource(addImageSrc);
                break;
            case DELETE_ABLE://有图片，需要删除
                deleted.setVisibility(VISIBLE);
                if (mListener != null) {
                    mListener.loadImageView(mAddView, path, sourceType);
//                    Glide.with(context).load(path).into(mAddView);
                }
                mAddView.setTag(IMAGE_ID, new ImageInfo(sourceType, path));
                break;
            case ONLY_BROWS://有图片不需要删除
                deleted.setVisibility(GONE);
                if (mListener != null) {
                    mListener.loadImageView(mAddView, path,sourceType);
//                    Glide.with(context).load(path).into(mAddView);
                }
                mAddView.setTag(IMAGE_ID, new ImageInfo(sourceType, path));
                break;
        }

        mAddView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    if (mAddView.getTag() == null) {
                        mListener.onViewAdd(((Integer) ((View) mAddView.getParent()).getTag()).byteValue());//添加图片
                    } else {
                        ViewType state1 = ((StateFrameLayout) mAddView.getParent()).getState();
                        mListener.onViewBrows(((Integer) ((View) mAddView.getParent()).getTag()).byteValue(), path, state1);//查看图片
                    }
                }
            }
        });
        deleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    deleteView(((Integer) layout.getTag()).intValue());
                    mListener.onViewDeleted((Integer) layout.getTag(), path);//删除图片
                }
            }
        });
        addView(layout, position);
    }

    private void updateUrls() {
        imageInfos.clear();
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            ImageInfo tag = (ImageInfo) getChildAt(i).findViewById(IMAGE_ID).getTag(IMAGE_ID);
            if (tag != null) {
                imageInfos.add((ImageInfo) getChildAt(i).findViewById(IMAGE_ID).getTag(IMAGE_ID));
            }
        }
    }

    /**
     * 获取url列表
     *
     * @return
     */
    public List<String> getViewPathList() {
        ArrayList<String> list = new ArrayList<>();
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            if (getChildAt(i).findViewById(IMAGE_ID).getTag(IMAGE_ID) != null) {
                list.add(((ImageInfo) getChildAt(i).findViewById(IMAGE_ID).getTag(IMAGE_ID)).getUrl());
            }
        }
        return list;
    }

    public int getUrlSize() {
        int size = 0;
        for (int i = 0; i < getChildCount(); i++) {
            if (getChildAt(i).findViewById(IMAGE_ID).getTag(IMAGE_ID) != null) {
                size++;
            }
        }
        return size;
    }


    /**
     * 获取所有网络图片的url
     *
     * @return
     */
    public List<String> getUrlList() {
        ArrayList<String> list = new ArrayList<>();
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            StateFrameLayout stateFrameLayout = (StateFrameLayout) getChildAt(i);
            if (stateFrameLayout.getUrlType() == UrlType.URL && getChildAt(i).findViewById(IMAGE_ID).getTag(IMAGE_ID) != null) {
                list.add(((ImageInfo) getChildAt(i).findViewById(IMAGE_ID).getTag(IMAGE_ID)).getUrl());
            }
        }
        return list;
    }

    /**
     * 获取需要上传的图片的路径列表
     *
     * @return
     */
    public List<String> getUploadViewPathList() {
        ArrayList<String> list = new ArrayList<>();
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            StateFrameLayout stateFrameLayout = (StateFrameLayout) getChildAt(i);
            if (stateFrameLayout.getUrlType() == UrlType.LOCAL_FILE && getChildAt(i).findViewById(IMAGE_ID).getTag(IMAGE_ID) != null) {
                list.add(((ImageInfo) getChildAt(i).findViewById(IMAGE_ID).getTag(IMAGE_ID)).getUrl());
            }
        }
        return list;
    }


    public interface OnImageViewOptionListener {

        void loadImageView(ImageView iv, String url,UrlType urlType);

        void onViewDeleted(int position, String path);//删除对外接口，这里不能操作视图本身的list数量，否则会出现视图显示错误

        void onViewBrows(int position, String path, ViewType state);//查看图片

        void onViewAdd(int position);
    }


    public void setOnImageViewOptionListener(OnImageViewOptionListener listener) {
        this.mListener = listener;
    }
}
