package kr.co.goms.module.common.handler;

import android.os.Parcel;
import android.os.Parcelable;

public class SampleResult implements Parcelable {
    public static final Creator<SampleResult> CREATOR = new Creator<SampleResult>() {
        public SampleResult createFromParcel(Parcel source) {
            return new SampleResult(source);
        }

        public SampleResult[] newArray(int size) {
            return new SampleResult[size];
        }
    };
    private String sampleResultId;

    public SampleResult() {
    }

    private SampleResult(Parcel source) {
        this.readFromParcel(source);
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.sampleResultId);
    }

    public int describeContents() {
        return 0;
    }

    public String getSampleResultId() {
        return this.sampleResultId;
    }

    public void readFromParcel(Parcel source) {
        this.sampleResultId = source.readString();
    }

    public void setSampleResultId(String enrollmentId) {
        this.sampleResultId = enrollmentId;
    }

    public String toString() {
        String str = "sampleResultId : " + this.sampleResultId;
        return str;
    }

}
