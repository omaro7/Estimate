package kr.co.goms.module.common.model;

import android.os.Parcel;
import android.os.Parcelable;

public class FontBeanS implements Parcelable {

	private String res_font_idx;
	private String res_font_name;
	private String res_font_file_name;
	private String res_font_file_path;
	private boolean isDownload;

	public FontBeanS() {
	}


	protected FontBeanS(Parcel in) {
		res_font_idx = in.readString();
		res_font_name = in.readString();
		res_font_file_name = in.readString();
		res_font_file_path = in.readString();
		isDownload = in.readByte() != 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(res_font_idx);
		dest.writeString(res_font_name);
		dest.writeString(res_font_file_name);
		dest.writeString(res_font_file_path);
		dest.writeByte((byte) (isDownload ? 1 : 0));
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<FontBeanS> CREATOR = new Creator<FontBeanS>() {
		@Override
		public FontBeanS createFromParcel(Parcel in) {
			return new FontBeanS(in);
		}

		@Override
		public FontBeanS[] newArray(int size) {
			return new FontBeanS[size];
		}
	};

	public String getRes_font_idx() {
		return res_font_idx;
	}

	public void setRes_font_idx(String res_font_idx) {
		this.res_font_idx = res_font_idx;
	}

	public String getRes_font_name() {
		return res_font_name;
	}

	public void setRes_font_name(String res_font_name) {
		this.res_font_name = res_font_name;
	}

	public String getRes_font_file_name() {
		return res_font_file_name;
	}

	public void setRes_font_file_name(String res_font_file_name) {
		this.res_font_file_name = res_font_file_name;
	}

	public String getRes_font_file_path() {
		return res_font_file_path;
	}

	public void setRes_font_file_path(String res_font_file_path) {
		this.res_font_file_path = res_font_file_path;
	}

	public boolean isDownload() {
		return isDownload;
	}

	public void setDownload(boolean download) {
		isDownload = download;
	}
}
