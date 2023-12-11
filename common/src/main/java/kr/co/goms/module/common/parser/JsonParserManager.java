package kr.co.goms.module.common.parser;

import android.util.ArrayMap;

import org.json.JSONObject;

import java.util.Map;

import kr.co.goms.module.common.model.CommonBean;

/**
* JsonParserManager 매니저
 */
public class JsonParserManager {

    static final String LOG_TAG = JsonParserManager.class.getSimpleName();

    static JsonParserManager instance;
    static ArrayMap<String, Object> mParserList;    //parser 등록
    static ArrayMap<String, Object> mParserDataList;//parser data 등록

    public enum JSON_PARSER_NAME{
        quiz_list,
        comment,
        news,
        calendar_comment,
    }

    protected JsonParserManager() {
    }

    protected void onDestroy() {

    }

    public static JsonParserManager I() {
        if(null == instance) {
            instance = new JsonParserManager();
        }

        return instance;
    }

    public static void D() {
        if(null != instance) {
            instance.onDestroy();
            instance = null;
        }
    }

    public void init(){
        mParserList = new ArrayMap<>();
        mParserDataList = new ArrayMap<>();
        mParserList.put("common", new CommonJsonParser());  // common 기본 처리
    }

    public Object getJsonParserData(String parserType){
        for (Map.Entry<String, Object> entry : mParserDataList.entrySet()) {
            String key = entry.getKey();
            if(parserType.equals(key)){
                Object object = entry.getValue();
                return object;
            }
        }
        return "";
    }

    /**
     *
     * @param jsonString
     * @param parserName -> "category"
     * @return
     */

    public void parserJsonData(String jsonString, String parserName, OnParserResult onParserResult){

        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JsonParserFactory jpFactory = new JsonParserFactory();
            IJsonParser<?> jsonParser = (IJsonParser<?>)jpFactory.create("common");
            jsonParser.parserJson(jsonObject);
            CommonBean commonData = (CommonBean) jsonParser.getData();

            //if (StringUtil.intToString(AppConstant.REQUEST_SUCCESS).equalsIgnoreCase(commonData.getRcode())){    //정상 전문

            IJsonParser<?> listJsonParser = (IJsonParser<?>)jpFactory.create(parserName);
            listJsonParser.setParserData((IJsonParserData)getJsonParserData(parserName));
            listJsonParser.parserJson(commonData.getRdata());
            Object object = listJsonParser.getData();

            //성공
            onParserResult.onParserResultSuccess(1, object);
        } catch (Exception e){
            //setMainUIHandler("error");
            //실패
            e.printStackTrace();
            onParserResult.onParserResultFail(0, "error");
        }
    }

    public interface OnParserResult {
        void onParserResultSuccess(int resultCode, Object object);
        void onParserResultFail(int resultCode, String errorMessage);
    }

    public ArrayMap<String, Object> getParserList() {
        if(mParserList == null){
            mParserList = new ArrayMap<String, Object>();
        }
        return mParserList;
    }

    public static void setParserList(ArrayMap<String, Object> mParserList) {
        JsonParserManager.mParserList = mParserList;
    }
    public ArrayMap<String, Object> getParserDataList() {
        if(mParserDataList == null){
            mParserDataList = new ArrayMap<String, Object>();
        }
        return mParserDataList;
    }

    public static void setParserDataList(ArrayMap<String, Object> mParserDataList) {
        JsonParserManager.mParserDataList = mParserDataList;
    }
}
