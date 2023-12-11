package kr.co.goms.module.common.model;

import java.io.File;

public class FontBean {
	
	private String font_path;
	private int font_type;		// 0 system 1 asset
	private File file;

	public String getFont_path() {
		return font_path;
	}

	public void setFont_path(String font_path) {
		this.font_path = font_path;
	}

	public int getFont_type() {
		return font_type;
	}

	public void setFont_type(int font_type) {
		this.font_type = font_type;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}
}
