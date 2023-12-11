package kr.co.goms.module.common.parser;

import org.json.JSONArray;
import org.json.JSONObject;

/** A class to parse json data */
public class DataJsonParser implements IJsonParser<Object> {

	Object object;
	IJsonParserData mParserData;

	@Override
	public void parserJson(String jsonString) {

	}

	@Override
	public void parserJson(JSONObject jObject) {

	}

	@Override
	public void parserJson(JSONArray jsonArray) {
		mParserData.setData(jsonArray);
		object = mParserData.getArrayData();
	}

	@Override
	public void setParserData(IJsonParserData parserData) {
		this.mParserData = parserData;
	}

	@Override
	public Object getData() {
		return object;
	}
}