package kr.co.goms.app.estimate.manager.gson;

import com.google.gson.Gson;

public class GsonJsonImpl implements GsonImpl {

    Gson mGson;

    @Override
    public void builder() {
        mGson = new Gson();
    }

    @Override
    public Object from(String data, Class gClass) {
        Object object = mGson.fromJson(data, gClass);
        return object;
    }


}
