package kr.co.goms.module.common.command;

import android.app.Activity;

import kr.co.goms.module.common.WaterFramework;
import kr.co.goms.module.common.application.ApplicationInterface;
import kr.co.goms.module.common.base.BaseBean;

public class CommandExit  extends Command {
    public CommandExit() {
    }

    protected BaseBean onCommand(Activity activity, Object obj, BaseBean data) {
        WaterFramework.beginExit();
        ApplicationInterface application = WaterFramework.I().getApplication();
        if (null != application) {
            application.destroy();
        }
        WaterFramework.I().clearActivity();
        return data;
    }
}
