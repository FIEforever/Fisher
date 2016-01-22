package com.doyoteam.fisher.db.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 展示图（商家图片和团购图片）
 * Created by F on 2015/12/21.
 */
public class Picture implements Parcelable {
    public String url;
    public String describe;

    public Picture() {
    }

    public Picture(String url, String describe) {
        this.url = url;
        this.describe = describe;
    }

    private Picture(Parcel in) {
        this.url = in.readString();
        this.describe = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(url);
        out.writeString(describe);
    }

    public static final Creator<Picture> CREATOR = new Creator<Picture>() {
        @Override
        public Picture createFromParcel(Parcel in) {
            return new Picture(in);
        }

        @Override
        public Picture[] newArray(int size) {
            return new Picture[size];
        }
    };
}
