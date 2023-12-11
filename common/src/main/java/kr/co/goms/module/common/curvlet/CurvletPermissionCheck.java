package kr.co.goms.module.common.curvlet;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import java.util.ArrayList;

import kr.co.goms.module.common.base.BaseBean;
import kr.co.goms.module.common.base.WaterCallBack;
import kr.co.goms.module.common.command.Command;
import kr.co.goms.module.common.request.RequestCodeManager;

import static kr.co.goms.module.common.base.BaseBean.STATUS;

public class CurvletPermissionCheck extends Curvlet {
    static final String LOG_TAG = CurvletPermissionCheck.class.getSimpleName();
    static ArrayList<String> g_permissionList = null;
    static Command commandPermissonDenial = null;
    ArrayList<String> permissionCheck = new ArrayList();
    BaseBean data = null;
    String returnJS = null;
    String message = null;
    String[] permissionList = null;
    WaterCallBack callback = null;
    Activity activity = null;

    public CurvletPermissionCheck() {
    }

    protected void doGet(Activity activity, WaterCallBack response, Uri request) {
        this.data = new BaseBean();
        this.callback = response;
        this.returnJS = request.getQueryParameter(eQueryParameter.returnJS.toString());
        this.message = request.getQueryParameter(eQueryParameter.message.toString());
        String permissions = getPermission();
        this.activity = activity;
        this.permissionList = permissions.split(",");
        RequestCodeManager.I().putRequestObject(this);
        if (this.checkPermissionsEx(activity)) {
            this.response(STATUS.SUCCESS, eResponse.COMPLETE);
            Log.d(LOG_TAG, "권한 확인 결과 : 처리할 권한 없음");
        } else {
            this.response(STATUS.SUCCESS, eResponse.NEED_PERMISSION);
            Log.d(LOG_TAG, "권한 확인 결과 : 처리할 권한 있음");
        }

    }

    public void onRequestPermissionsResult(Activity activity, int requestCode, String[] permissions, int[] grantResults) {
        RequestCodeManager.I().popRequestObject(this);
        Log.d(LOG_TAG, "onRequestPermissionsResult()");
        Log.d(LOG_TAG, " >> requestCode : " + requestCode);

        for(int n = 0; n < permissions.length; ++n) {
            Log.d(LOG_TAG, " >> permissions[" + n + "] : " + permissions[n]);
        }

        boolean result = true;
        if (grantResults != null && grantResults.length > 0) {
            for(int i = 0; i < grantResults.length; ++i) {
                Log.d(LOG_TAG, " >> GrantResults[" + i + "] : " + grantResults[i]);
                if (grantResults[i] != 0) {
                    result = false;
                    break;
                }
            }
        }

        if (result) {
            this.response(STATUS.SUCCESS, eResponse.COMPLETE);
        } else {
            STATUS status = STATUS.PROGRESS;
            eResponse response = eResponse.DENIAL;
            if (null != commandPermissonDenial) {
                this.data.setStatus(status, "javascript:" + this.returnJS + "('" + response.toString() + "')");
                Log.d(LOG_TAG, "status : " + this.data.getStatus());
                Log.d(LOG_TAG, "data : " + this.data.getData());
                commandPermissonDenial.setBaseBean(this.data);
                commandPermissonDenial.command(activity, this.message);
                this.response(status, response);
            } else {
                this.response(status, response);
            }
        }

    }

    private void response(STATUS status, eResponse response) {
        this.data.setStatus(status, "javascript:" + this.returnJS + "('" + response.toString() + "')");
        this.data.setObject(this);
        this.callback.callback(this.data);
    }

    @SuppressLint("WrongConstant")
    private boolean checkPermissionsEx(Activity activity) {
        boolean result = true;
        if (Build.VERSION.SDK_INT >= 23) {
            int max = this.permissionList.length;

            for(int n = 0; n < max; ++n) {
                if (activity.checkSelfPermission(this.permissionList[n]) != 0) {
                    this.permissionCheck.add(this.permissionList[n]);
                    result = false;
                }
            }
        }

        return result;
    }

    public void callPermissionPop() {
        if (Build.VERSION.SDK_INT >= 23 && this.permissionCheck != null && this.permissionCheck.size() > 0) {
            this.activity.requestPermissions((String[])this.permissionCheck.toArray(new String[this.permissionCheck.size()]), this.getRequestCode());
        }

    }

    public static void seCommandPermissionDenial(Command command) {
        commandPermissonDenial = command;
    }

    public static void addPermission(String permission) {
        if (g_permissionList == null) {
            g_permissionList = new ArrayList();
        }

        int max = g_permissionList.size();

        for(int n = 0; n < max; ++n) {
            if (((String)g_permissionList.get(n)).equals(permission)) {
                return;
            }
        }

        g_permissionList.add(permission);
    }

    private static String getPermission() {
        String permissions = "";
        int max = g_permissionList.size();

        for(int n = 0; n < max; ++n) {
            if (0 >= permissions.length()) {
                permissions = (String)g_permissionList.get(n);
            } else {
                permissions = permissions + ",";
                permissions = permissions + (String)g_permissionList.get(n);
            }
        }

        g_permissionList.clear();
        return permissions;
    }

    public static enum eResponse {
        COMPLETE,
        DENIAL,
        NEED_PERMISSION;

        private eResponse() {
        }
    }

    public static enum eQueryParameter {
        returnJS,
        message,
        permissions;

        private eQueryParameter() {
        }
    }
}
