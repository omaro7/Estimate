package kr.co.goms.module.common.curvlet;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import kr.co.goms.module.common.base.WaterCallBack;

public class CurvletActionView  extends Curvlet {
    @Override
    protected void doGet(Activity activity, WaterCallBack waterCallBack, Uri request) throws Exception {
        Intent intent = new Intent("android.intent.action.VIEW", request);
        activity.startActivity(intent);
    }
}
