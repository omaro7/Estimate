package kr.co.goms.module.common.curvlet;

import android.app.Activity;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import kr.co.goms.module.common.base.WaterCallBack;

public class CurvletToast extends CurvletCommon {
    String showText;
    int showType = 0;

    public CurvletToast() {
    }

    @Override
    protected void doGet(Activity activity, WaterCallBack waterCallBack, Uri uri) {
        this.activity = activity;
        this.showText = uri.getQueryParameter("text");
        if (this.isEmpty(this.showText)) {
            this.showText = "";
        }

        String tempShowType = uri.getQueryParameter("showType");
        if (this.isEmpty(tempShowType)) {
            tempShowType = "0";
        }

        try {
            this.showType = Integer.valueOf(tempShowType);
        } catch (Exception var6) {
            this.showType = 0;
        }

        this.onToast();
    }


    private void onToast() {

        if(activity == null) {
            Log.d("CurvletToast", "activity null");
            return;
        }

        this.activity.runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(CurvletToast.this.activity, CurvletToast.this.showText, CurvletToast.this.showType).show();
            }
        });
    }
}
