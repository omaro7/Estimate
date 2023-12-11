package kr.co.goms.module.common.parser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import kr.co.goms.module.common.model.CommonBean;

public  class CommonJsonParser implements IJsonParser<Object> {

	CommonBean mCommonData = null;

	public CommonJsonParser() {
	}
	
	@Override
	public void parserJson(String jsonString) {
		try {
			JSONObject jObject = new JSONObject(jsonString.replace("\"", "'"));
			IJsonParserData<?> jsonData = new CommonJsonParserData();
			jsonData.setData(jObject);
			mCommonData = (CommonBean) jsonData.getData();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Object getData() {
		return mCommonData;
	}

	@Override
	public void parserJson(JSONObject jObject) {
		IJsonParserData<?> jsonData = new CommonJsonParserData();
		jsonData.setData(jObject);
		mCommonData = (CommonBean) jsonData.getData();
	}

	@Override
	public void parserJson(JSONArray jArray) {

	}

	@Override
	public void setParserData(IJsonParserData parserData) {

	}
}
