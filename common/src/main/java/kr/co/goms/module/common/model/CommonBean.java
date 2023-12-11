package kr.co.goms.module.common.model;

import org.json.JSONArray;

public class CommonBean {
	
	private String rcode;
	private String rmsg;
	private String rdate;
	private String rPage;
	private JSONArray rdata;

	public String getRcode() {
		return rcode;
	}

	public void setRcode(String rcode) {
		this.rcode = rcode;
	}

	public String getRmsg() {
		return rmsg;
	}

	public void setRmsg(String rmsg) {
		this.rmsg = rmsg;
	}

	public String getRdate() {
		return rdate;
	}

	public void setRdate(String rdate) {
		this.rdate = rdate;
	}

	public String getRTotalPage() {
		return rPage;
	}

	public void setRTotalPage(String rPage) {
		this.rPage = rPage;
	}

	public JSONArray getRdata() {
		return rdata;
	}

	public void setRdata(JSONArray rdata) {
		this.rdata = rdata;
	}
}
