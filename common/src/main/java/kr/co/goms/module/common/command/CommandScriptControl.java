package kr.co.goms.module.common.command;

import android.app.Activity;

import kr.co.goms.module.common.base.BaseBean;
import kr.co.goms.module.common.curvlet.CurvletManager;

public class CommandScriptControl extends Command {
    protected Activity activity;
    String curvletCommand;

    public CommandScriptControl() {
    }

    public CommandScriptControl(String curvletCommand) {
        this.curvletCommand = curvletCommand;
    }

    protected BaseBean onCommand(Activity activity, Object obj, BaseBean data) {
        if (!this.isEmpty((String)obj)) {
            CurvletManager.I();
            CurvletManager.process(activity, this.mWaterCallBack, (String)obj);
        } else if (!this.isEmpty(this.curvletCommand)) {
            CurvletManager.I();
            CurvletManager.process(activity, this.mWaterCallBack, this.curvletCommand);
        }

        return data;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    private boolean isEmpty(String src) {
        return src == null || src.length() == 0;
    }
}
