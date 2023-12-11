package kr.co.goms.module.common.curvlet;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import kr.co.goms.module.common.base.WaterCallBack;

public class CurvletActionSendTo  extends Curvlet {
    public CurvletActionSendTo() {
    }

    protected void doGet(Activity activity, WaterCallBack response, Uri request) throws Exception {
        Intent intent = new Intent("android.intent.action.SENDTO", request);
        String msg_body = Uri.parse("sms://sms?" + request.getQuery()).getQueryParameter("body");
        intent.putExtra("sms_body", msg_body);
        activity.startActivity(intent);
    }
}