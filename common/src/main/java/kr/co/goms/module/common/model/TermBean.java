package kr.co.goms.module.common.model;

import android.os.Parcel;
import android.os.Parcelable;

public class TermBean implements Parcelable {

	private String term_header_title;
	private boolean isHeader = false;
	private String term_seq;
	private String term_title;
	private String term_link;
	private boolean isEssential = false;
	private boolean isSelect = false;

	public TermBean(){

	}

	public TermBean(Parcel in) {
		term_header_title = in.readString();
		isHeader = in.readByte() != 0;
		term_seq = in.readString();
		term_title = in.readString();
		term_link = in.readString();
		isEssential = in.readByte() != 0;
		isSelect = in.readByte() != 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(term_header_title);
		dest.writeByte((byte) (isHeader ? 1 : 0));
		dest.writeString(term_seq);
		dest.writeString(term_title);
		dest.writeString(term_link);
		dest.writeByte((byte) (isEssential ? 1 : 0));
		dest.writeByte((byte) (isSelect ? 1 : 0));
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<TermBean> CREATOR = new Creator<TermBean>() {
		@Override
		public TermBean createFromParcel(Parcel in) {
			return new TermBean(in);
		}

		@Override
		public TermBean[] newArray(int size) {
			return new TermBean[size];
		}
	};

	public String getTerm_header_title() {
		return term_header_title;
	}

	public void setTerm_header_title(String term_header_title) {
		this.term_header_title = term_header_title;
	}

	public boolean isHeader() {
		return isHeader;
	}

	public void setHeader(boolean header) {
		isHeader = header;
	}

	public String getTerm_seq() {
		return term_seq;
	}

	public void setTerm_seq(String term_seq) {
		this.term_seq = term_seq;
	}

	public String getTerm_title() {
		return term_title;
	}

	public void setTerm_title(String term_title) {
		this.term_title = term_title;
	}

	public String getTerm_link() {
		return term_link;
	}

	public void setTerm_link(String term_link) {
		this.term_link = term_link;
	}

	public boolean isEssential() {
		return isEssential;
	}

	public void setEssential(boolean essential) {
		isEssential = essential;
	}

	public boolean isSelect() {
		return isSelect;
	}

	public void setSelect(boolean select) {
		isSelect = select;
	}
}
