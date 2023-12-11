package kr.co.goms.module.common.error;

import android.app.Activity;

import kr.co.goms.module.common.base.BaseBean;
import kr.co.goms.module.common.command.Command;
import kr.co.goms.module.common.curvlet.CurvletManager;

public class CommandExitApp extends Command {

    @Override
    protected BaseBean onCommand(Activity activity, Object object, BaseBean baseData) throws Exception {
        CurvletManager.process(activity, null, "water://exit");
        return null;
    }
}
