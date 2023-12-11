package kr.co.goms.module.common.parser;

import org.json.JSONArray;
import org.json.JSONObject;

public interface IJsonParser  <GenericType>{
    void parserJson(String jsonString);
    void parserJson(JSONObject jObject);
    void parserJson(JSONArray jArray);
    void setParserData(IJsonParserData parserData);
    GenericType getData();
}

