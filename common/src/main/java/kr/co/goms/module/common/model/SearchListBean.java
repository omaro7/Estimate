package kr.co.goms.module.common.model;

import android.os.Parcel;
import android.os.Parcelable;

public class SearchListBean implements Parcelable {

	private String list_idx;
	private String list_seq;
	private String list_title;
	private boolean isSelect = false;

	public SearchListBean(){

	}

	public SearchListBean(Parcel in) {
		list_idx = in.readString();
		list_seq = in.readString();
		list_title = in.readString();
		isSelect = in.readByte() != 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(list_idx);
		dest.writeString(list_seq);
		dest.writeString(list_title);
		dest.writeByte((byte) (isSelect ? 1 : 0));
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<SearchListBean> CREATOR = new Creator<SearchListBean>() {
		@Override
		public SearchListBean createFromParcel(Parcel in) {
			return new SearchListBean(in);
		}

		@Override
		public SearchListBean[] newArray(int size) {
			return new SearchListBean[size];
		}
	};

	public String getList_idx() {
		return list_idx;
	}

	public void setList_idx(String list_idx) {
		this.list_idx = list_idx;
	}

	public String getList_seq() {
		return list_seq;
	}

	public void setList_seq(String list_seq) {
		this.list_seq = list_seq;
	}

	public String getList_title() {
		return list_title;
	}

	public void setList_title(String list_title) {
		this.list_title = list_title;
	}

	public boolean isSelect() {
		return isSelect;
	}

	public void setSelect(boolean select) {
		isSelect = select;
	}
}
