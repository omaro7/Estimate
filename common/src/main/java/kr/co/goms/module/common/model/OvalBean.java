package kr.co.goms.module.common.model;

import android.os.Parcel;
import android.os.Parcelable;

public class OvalBean implements Parcelable {

	private String oval_seq;
	private String oval_title;
	private boolean isSelect = false;

	public OvalBean(){

	}

	public OvalBean(Parcel in) {
		oval_seq = in.readString();
		oval_title = in.readString();
		isSelect = in.readByte() != 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(oval_seq);
		dest.writeString(oval_title);
		dest.writeByte((byte) (isSelect ? 1 : 0));
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<OvalBean> CREATOR = new Creator<OvalBean>() {
		@Override
		public OvalBean createFromParcel(Parcel in) {
			return new OvalBean(in);
		}

		@Override
		public OvalBean[] newArray(int size) {
			return new OvalBean[size];
		}
	};

	public String getOval_seq() {
		return oval_seq;
	}

	public void setOval_seq(String oval_seq) {
		this.oval_seq = oval_seq;
	}

	public String getOval_title() {
		return oval_title;
	}

	public void setOval_title(String oval_title) {
		this.oval_title = oval_title;
	}

	public boolean isSelect() {
		return isSelect;
	}

	public void setSelect(boolean select) {
		isSelect = select;
	}
}
