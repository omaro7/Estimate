package kr.co.goms.app.estimate.manager;

import androidx.annotation.Nullable;

import com.google.gson.Gson;

import java.util.HashMap;

import kr.co.goms.app.estimate.manager.gson.GsonImpl;
import kr.co.goms.app.estimate.manager.gson.GsonJsonImpl;
import kr.co.goms.app.estimate.model.ItemBeanTB;

/**
 * 사용법
 //createParserData를 통한  해당 Parser의 Object 생성
 Object object = GsonManager.I().createParserData(jsonData, GsonManager.PARSER_TYPE.survey_list);

 BaseBean baseBean = new BaseBean();
 baseBean.setObject(((SurveyListBeanG)object).getRes_data());   //object만 casting 하면 됩니다.
 mDataObserver.callback(baseBean);
 */
public class GsonManager {

    static GsonManager mGsonManager;    //instance
    static Gson mGson;                  //Gson

    public enum GSON_TYPE {
        json,
        xml,
    }

    //각 Parser 정의
    public enum PARSER_TYPE{
        item(ItemBeanTB.class),
        ;

        Class mObjectClass;
        PARSER_TYPE(Class objectClass) {
            mObjectClass = objectClass;
        }

        public Class getObjectClass() {
            return mObjectClass;
        }
    }

    public GsonManager() {
    }

    public static GsonManager I(){
        if(mGsonManager == null){
            mGsonManager = new GsonManager();
        }
        if(mGson == null){
            mGson = new Gson();
        }
        return mGsonManager;
    }

    /**
     * jsonString을 통한 해당 Parser 처리 후 Return
     * @param jsonData      //json data
     * @param parserType    //parser type
     * @return
     */
    public Object createParserData(String jsonData, PARSER_TYPE parserType){
        if(parserType.getObjectClass() == null){
            return null;
        }else {
            return fromGson(GSON_TYPE.json.name(), jsonData, parserType);
            //return mGson.fromJson(jsonData, parserType.getObjectClass());
        }
    }

    /**
     * gsonType json, xml에 따른 Gson 생성자
     * @param gsonType
     * @return
     */
    public GsonImpl createGsonBuilder(String gsonType){
        GsonImpl gsonImpl;
        gsonImpl = new GsonJsonImpl();
        gsonImpl.builder();
        return gsonImpl;
    }

    /**
     * data를 object로 변환처리
     * @param gonsType
     * @param data
     * @param gClass
     * @return
     */
    public Object fromGson(@Nullable String gonsType, String data, Class gClass){
        GsonImpl gsonImpl = createGsonBuilder(gonsType);
        return gsonImpl.from(data, gClass);
    }

    /**
     * data를 object로 변환처리
     * @param gonsType
     * @param data
     * @param parserType
     * @return
     */
    public Object fromGson(@Nullable String gonsType, String data, PARSER_TYPE parserType){
        GsonImpl gsonImpl = createGsonBuilder(gonsType);

        if(parserType.getObjectClass() == null){
            return null;
        }else {
            return gsonImpl.from(data, parserType.getObjectClass());
        }
    }

    /**
     * HashMap데이타를 json형태 string으로 변환
     * @param hashMap
     * @return
     */
    public String getHashMapData(HashMap hashMap){
        String data = mGson.toJson(hashMap);
        return data;
    }

}
