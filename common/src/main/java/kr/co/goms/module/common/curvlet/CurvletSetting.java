package kr.co.goms.module.common.curvlet;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;

import kr.co.goms.module.common.base.WaterCallBack;

public class CurvletSetting extends Curvlet {

    public CurvletSetting() {
    }

    protected void doGet(Activity activity, WaterCallBack callback, Uri request) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + activity.getPackageName()));
        activity.startActivity(intent);
        activity.finish();
    }
}
