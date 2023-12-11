package kr.co.goms.module.common.handler;

import android.os.Parcel;
import android.os.Parcelable;

public class CommonResult implements Parcelable {
    public static final Creator<CommonResult> CREATOR = new Creator<CommonResult>() {
        public CommonResult createFromParcel(Parcel source) {
            return new CommonResult(source);
        }

        public CommonResult[] newArray(int size) {
            return new CommonResult[size];
        }
    };
    private String commonResultId;

    public CommonResult() {
    }

    private CommonResult(Parcel source) {
        this.readFromParcel(source);
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.commonResultId);
    }

    public int describeContents() {
        return 0;
    }

    public String getCommonResultId() {
        return this.commonResultId;
    }

    public void readFromParcel(Parcel source) {
        this.commonResultId = source.readString();
    }

    public void setCommonResultId(String enrollmentId) {
        this.commonResultId = enrollmentId;
    }

    public String toString() {
        String str = "commonResultId : " + this.commonResultId;
        return str;
    }

}
