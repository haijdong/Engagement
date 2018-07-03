package com.segi.view.imageview.graffiti;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * 涂鸦参数
 */
public class GraffitiParams implements Parcelable {

    /**
     * 图片路径
     */
    public String imagePath;
    /**
     * 　保存路径，如果为null，则图片保存在根目录下/DCIM/Graffiti/
     */
    public String savePath;
    /**
     * 　保存路径是否为目录，如果为目录，则在该目录生成由时间戳组成的图片名称
     */
    public boolean savePathIsDir;
    /**
     * 　橡皮擦底图，如果为null，则底图为当前图片路径
     * {@link GraffitiView( Context , Bitmap , String , boolean, GraffitiView.GraffitiListener)}
     */
    public String eraserPath;

    /**
     * 橡皮擦底图是否调整大小，如果为true则调整到跟当前涂鸦图片一样的大小．
     * 默认为true
     */
    public boolean eraserImageIsResizeable = true;

    /**
     * 触摸时，图片区域外是否绘制涂鸦轨迹
     */
    public boolean isDrawableOutside;

    /**
     * 涂鸦时（手指按下）隐藏设置面板的延长时间(ms)，当小于等于0时则为不尝试隐藏面板（即保持面板当前状态不变）;当大于0时表示需要触摸屏幕超过一定时间后才隐藏
     * 或者手指抬起时展示面板的延长时间(ms)，或者表示需要离开屏幕超过一定时间后才展示
     * 默认为800ms
     */
    public long changePanelVisibilityDelay = 800; //ms

    /**
     * 是否全屏显示，即是否隐藏状态栏
     * 默认为false，表示状态栏继承应用样式
     */
    public boolean isFullScreen = false;

    /**
     * 画笔类型
     */
    public GraffitiView.Pen penType = GraffitiView.Pen.HAND;

    /**
     * 画笔颜色
     */
    public int penColor = Color.RED;

    /**
     * 画笔大小
     */
    public int penSize = 30;

    public GraffitiParams(){};

    protected GraffitiParams(Parcel in) {
        imagePath = in.readString();
        savePath = in.readString();
        savePathIsDir = in.readByte() != 0;
        eraserPath = in.readString();
        eraserImageIsResizeable = in.readByte() != 0;
        isDrawableOutside = in.readByte() != 0;
        changePanelVisibilityDelay = in.readLong();
        isFullScreen = in.readByte() != 0;
        penColor = in.readInt();
        penSize = in.readInt();
    }

    public static final Creator<GraffitiParams> CREATOR = new Creator<GraffitiParams>() {
        @Override
        public GraffitiParams createFromParcel(Parcel in) {
            return new GraffitiParams(in);
        }

        @Override
        public GraffitiParams[] newArray(int size) {
            return new GraffitiParams[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(imagePath);
        dest.writeString(savePath);
        dest.writeByte((byte) (savePathIsDir ? 1 : 0));
        dest.writeString(eraserPath);
        dest.writeByte((byte) (eraserImageIsResizeable ? 1 : 0));
        dest.writeByte((byte) (isDrawableOutside ? 1 : 0));
        dest.writeLong(changePanelVisibilityDelay);
        dest.writeByte((byte) (isFullScreen ? 1 : 0));
        dest.writeInt(penColor);
        dest.writeInt(penSize);
    }
}