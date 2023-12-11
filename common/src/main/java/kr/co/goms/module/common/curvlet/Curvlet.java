package kr.co.goms.module.common.curvlet;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import kr.co.goms.module.common.base.BaseBean;
import kr.co.goms.module.common.base.WaterCallBack;
import kr.co.goms.module.common.request.RequestCodeManager;
import kr.co.goms.module.common.request.RequestCodeObject;

public abstract class Curvlet implements RequestCodeObject {
    private static final String LOG_TAG = "Curvlet";
    private static String schemeString = null;
    private static String hostString = null;
    private static String parameterString = null;
    private int requestCode = -1;
    private static String returnNext = null;
    private static String returnNextParams = "";

    public Curvlet() {

    }

    protected void process(Activity activity, WaterCallBack callback, Uri request) {
        try {
            this.setRequestCode(RequestCodeManager.I().generateRequestCodeValue());
            this.doGet(activity, callback, request);
            this.returnNext(activity, callback, request);
        } catch (Exception var5) {
            var5.printStackTrace();
        }

    }

    protected void returnNext(Activity activity, WaterCallBack callback, Uri request) {
        if (this.isEmpty(returnNext)) {
            Log.d("Curvlet", "returnNext 메소드가, 앱에 정의 되어있지 않다.");
        } else {
            String nextJS = request.getQueryParameter(returnNext);
            if (this.isEmpty(nextJS)) {
                Log.d("Curvlet", "웹에서 returnNext 메소드가, 요청 되지 않았다.");
            } else {
                try {
                    BaseBean baseData = new BaseBean();
                    baseData.setStatus(BaseBean.STATUS.SUCCESS, "javascript:" + returnNext + "(" + returnNextParams + ")");
                    callback.callback(baseData);
                } catch (Exception var6) {
                    var6.printStackTrace();
                }

            }
        }
    }

    public boolean isEmpty(String src) {
        return src == null || src.length() == 0;
    }

    protected abstract void doGet(Activity activity, WaterCallBack waterCallBack, Uri uri) throws Exception;

    public static void setScheme(String scheme) {
        schemeString = scheme;
        if (null != schemeString) {
            schemeString = schemeString + "://";
        }

    }

    public static void setHost(String host) {
        hostString = host;
    }

    public static void addParameter(String key, String value) {
        if (null != value) {
            if (null != parameterString) {
                parameterString = parameterString + "&" + key + "=" + value;
            } else {
                parameterString = "?" + key + "=" + value;
            }

        }
    }

    public static boolean addParameterUrlEncode(String key, String value, String enc) {
        boolean result;
        try {
            addParameter(key, URLEncoder.encode(value, "UTF-8"));
            result = true;
        } catch (UnsupportedEncodingException var5) {
            var5.printStackTrace();
            result = false;
        }

        return result;
    }

    public static String sendUrl() {
        if (null == schemeString) {
            return "";
        } else if (null == hostString) {
            return "";
        } else {
            if (null == parameterString) {
                parameterString = "";
            }

            String sendUrl = schemeString + hostString + parameterString;
            schemeString = null;
            hostString = null;
            parameterString = null;
            return sendUrl;
        }
    }

    public static void setReturnNext(String returnNext) {
        Curvlet.returnNext = returnNext;
    }

    public static void setReturnNextParams(String returnNextParams) {
        Curvlet.returnNextParams = returnNextParams;
    }

    public void setRequestCode(int requestCode) {
        this.requestCode = requestCode;
    }

    public int getRequestCode() {
        return this.requestCode;
    }

    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        Log.d("Curvlet", "Curvlet.onActivityResult(,,) 호출");
    }

    public void onRequestPermissionsResult(Activity activity, int requestCode, String[] permissions, int[] grantResults) {
        Log.d("Curvlet", "Curvlet.onRequestPermissionsResult(,,) 호출");
    }
}
