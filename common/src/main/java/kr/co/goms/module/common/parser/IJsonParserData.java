package kr.co.goms.module.common.parser;

import org.json.JSONArray;
import org.json.JSONObject;

public interface IJsonParserData<GenericType>{
	void setData(JSONObject jObject);
	void setData(JSONArray jArray);
	GenericType getData();
	GenericType getArrayData();
}