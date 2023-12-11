package kr.co.goms.module.common.curvlet;

import android.app.Activity;
import android.net.Uri;
import android.util.Log;

import kr.co.goms.module.common.base.WaterCallBack;

public class CurvletCommon extends Curvlet {
    private static final String LOG_TAG = "CurvletCommon";
    protected Activity activity;
    protected WaterCallBack callback = null;

    public CurvletCommon() {
    }

    protected void doGet(Activity activity, WaterCallBack callback, Uri uri) {
        Log.i("CurvletCommon", "url : " + uri.toString());
        this.activity = activity;
        this.callback = callback;
        String value = uri.getEncodedQuery();
        this.onDoGet(callback, uri, value);
    }

    protected void onDoGet(WaterCallBack view, Uri uri, String value) {
    }
}