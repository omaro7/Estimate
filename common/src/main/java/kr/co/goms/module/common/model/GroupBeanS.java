package kr.co.goms.module.common.model;

import android.os.Parcel;
import android.os.Parcelable;

public class GroupBeanS implements Parcelable {
    private String res_mh_group_idx;    //순번
    private String res_mh_group_name;   //그룹명

    public GroupBeanS(){

    }

    protected GroupBeanS(Parcel in) {
        res_mh_group_idx = in.readString();
        res_mh_group_name = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(res_mh_group_idx);
        dest.writeString(res_mh_group_name);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<GroupBeanS> CREATOR = new Creator<GroupBeanS>() {
        @Override
        public GroupBeanS createFromParcel(Parcel in) {
            return new GroupBeanS(in);
        }

        @Override
        public GroupBeanS[] newArray(int size) {
            return new GroupBeanS[size];
        }
    };

    public String getRes_mh_group_idx() {
        return res_mh_group_idx;
    }

    public void setRes_mh_group_idx(String res_mh_group_idx) {
        this.res_mh_group_idx = res_mh_group_idx;
    }

    public String getRes_mh_group_name() {
        return res_mh_group_name;
    }

    public void setRes_mh_group_name(String res_mh_group_name) {
        this.res_mh_group_name = res_mh_group_name;
    }
}
