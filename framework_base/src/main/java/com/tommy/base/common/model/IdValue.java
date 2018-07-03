package com.tommy.base.common.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 简单的键值对实体
 *
 * @author liangzx
 * @version 1.0
 * @time 2018-01-31 16:35
 **/
public class IdValue implements Parcelable {

    /**
     * id
     */
    public String id;
    /**
     * 名称
     */
    public String name;
    /**
     * 是否选中
     */
    public boolean isChecked;

    public IdValue() {

    }

    public IdValue(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public IdValue(String id, String name, boolean isChecked) {
        this.id = id;
        this.name = name;
        this.isChecked = isChecked;
    }

    protected IdValue(Parcel in) {
        id = in.readString();
        name = in.readString();
        isChecked = in.readByte() != 0;
    }

    public static final Creator<IdValue> CREATOR = new Creator<IdValue>() {
        @Override
        public IdValue createFromParcel(Parcel in) {
            return new IdValue(in);
        }

        @Override
        public IdValue[] newArray(int size) {
            return new IdValue[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeByte((byte) (isChecked ? 1 : 0));
    }
}
