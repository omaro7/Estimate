package kr.co.goms.module.common.curvlet;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import kr.co.goms.module.common.base.BaseBean;
import kr.co.goms.module.common.base.WaterCallBack;
import kr.co.goms.module.common.request.RequestCodeManager;

public class CurvletPermissionOverlayWindow extends Curvlet {
    private static final String LOG_TAG = "CurvletPermissionOverlayWindow";
    BaseBean data = null;
    WaterCallBack callback = null;

    public CurvletPermissionOverlayWindow() {
    }

    protected void doGet(Activity activity, WaterCallBack callback, Uri uri) throws Exception {
        RequestCodeManager.I().putRequestObject(this);
        this.data = new BaseBean();
        this.callback = callback;
        if (Build.VERSION.SDK_INT >= 23) {
            if (!Settings.canDrawOverlays(activity)) {
                Intent intent = new Intent("android.settings.action.MANAGE_OVERLAY_PERMISSION", Uri.parse("package:" + activity.getPackageName()));
                activity.startActivityForResult(intent, this.getRequestCode());
            } else {
                this.data.setStatus(BaseBean.STATUS.SUCCESS, PERMISSION.AGREE.toString());
                this.callback.callback(this.data);
            }
        } else {
            this.data.setStatus(BaseBean.STATUS.SUCCESS, PERMISSION.AGREE.toString());
            this.callback.callback(this.data);
        }

    }

    @SuppressLint("LongLogTag")
    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        Log.d(LOG_TAG, "onActivityResult(" + requestCode + "," + resultCode + "," + data + ")");
        RequestCodeManager.I().popRequestObject(this);
        PERMISSION permission = PERMISSION.AGREE;
        if (Build.VERSION.SDK_INT >= 23) {
            if (!Settings.canDrawOverlays(activity)) {
                permission = PERMISSION.DISAGREE;
            } else {
                permission = PERMISSION.AGREE;
            }
        } else {
            permission = PERMISSION.AGREE;
        }

        this.data.setStatus(BaseBean.STATUS.SUCCESS, permission.toString());
        this.callback.callback(this.data);
    }

    public static enum PERMISSION {
        AGREE,
        DISAGREE;

        private PERMISSION() {
        }
    }
}
