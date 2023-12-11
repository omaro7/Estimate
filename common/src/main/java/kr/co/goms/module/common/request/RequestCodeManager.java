package kr.co.goms.module.common.request;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import java.util.HashMap;

public class RequestCodeManager {
    private static final String LOG_TAG = "RequestCodeManager";
    private static RequestCodeManager instance;
    HashMap<Integer, Object> requestObjectList = null;
    private static int requestCodeValue = 0;

    private RequestCodeManager() {
    }

    public static synchronized RequestCodeManager I() {
        if (null == instance) {
            instance = new RequestCodeManager();
        }

        return instance;
    }

    public static synchronized void D() {
        if (null != instance) {
            instance = null;
        }

    }

    public boolean putRequestObject(RequestCodeObject requestCodeObject) {
        if (null == requestCodeObject) {
            Log.d("RequestCodeManager", "입력된 객체가 null 이다.");
            return false;
        } else {
            if (null == this.requestObjectList) {
                this.requestObjectList = new HashMap();
            }

            Object tempObj = this.requestObjectList.get(requestCodeObject.getRequestCode());
            if (null != tempObj) {
                Log.d("RequestCodeManager", "같은 Request Code 값을 가진 오브젝트가 이미 있다. Request Code : " + requestCodeObject.getRequestCode());
                return false;
            } else {
                this.requestObjectList.put(requestCodeObject.getRequestCode(), requestCodeObject);
                Log.d("RequestCodeManager", "RequestCodeObject를 정상적으로 등록 함.");
                return true;
            }
        }
    }

    public boolean popRequestObject(RequestCodeObject requestCodeObject) {
        if (null == requestCodeObject) {
            Log.d("RequestCodeManager", "입력된 객체가 null 이다.");
            return false;
        } else if (null == this.requestObjectList) {
            Log.d("RequestCodeManager", "목록 객체가 null 이다.");
            return false;
        } else {
            Object tempObj = this.requestObjectList.get(requestCodeObject.getRequestCode());
            if (null == tempObj) {
                Log.d("RequestCodeManager", "같은 Request Code 값의 오브젝트가 없다. Request Code : " + requestCodeObject.getRequestCode());
                return false;
            } else {
                this.requestObjectList.remove(requestCodeObject.getRequestCode());
                Log.d("RequestCodeManager", "RequestCodeObject를 정상적으로 제거 함.");
                return true;
            }
        }
    }

    public int generateRequestCodeValue() {
        return ++requestCodeValue;
    }

    public boolean onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        if (null == this.requestObjectList) {
            Log.d("RequestCodeManager", "목록 객체가 null 이다.");
            return false;
        } else {
            RequestCodeObject requestCodeObject = (RequestCodeObject)this.requestObjectList.get(requestCode);
            if (null == requestCodeObject) {
                Log.d("RequestCodeManager", "찾은 객체가 null 이다.");
                Log.d("RequestCodeManager", "목록에는 " + this.requestObjectList.size() + " 개의 객체가 있다.");
                return false;
            } else {
                Log.d("RequestCodeManager", "찾은 객체의 onActivityResult 를 호출 한다.");
                requestCodeObject.onActivityResult(activity, requestCode, resultCode, data);
                return true;
            }
        }
    }

    public boolean onRequestPermissionsResult(Activity activity, int requestCode, String[] permissions, int[] grantResults) {
        if (null == this.requestObjectList) {
            Log.d("RequestCodeManager", "목록 객체가 null 이다.");
            return false;
        } else {
            RequestCodeObject requestCodeObject = (RequestCodeObject)this.requestObjectList.get(requestCode);
            if (null == requestCodeObject) {
                Log.d("RequestCodeManager", "찾은 객체가 null 이다.");
                Log.d("RequestCodeManager", "목록에는 " + this.requestObjectList.size() + " 개의 객체가 있다.");
                return false;
            } else {
                Log.d("RequestCodeManager", "찾은 객체의 onActivityResult 를 호출 한다.");
                requestCodeObject.onRequestPermissionsResult(activity, requestCode, permissions, grantResults);
                return true;
            }
        }
    }
}

