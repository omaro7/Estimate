package kr.co.goms.module.common.curvlet;

import android.app.Activity;
import android.net.Uri;

import kr.co.goms.module.common.base.WaterCallBack;
import kr.co.goms.module.common.command.CommandExit;

public class CurvletExit  extends Curvlet {
    public CurvletExit() {
    }

    protected void doGet(Activity activity, WaterCallBack callback, Uri request) throws Exception {
        CommandExit cmdExit = new CommandExit();
        cmdExit.command(activity, "");
    }
}
