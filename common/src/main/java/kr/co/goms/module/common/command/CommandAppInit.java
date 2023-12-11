package kr.co.goms.module.common.command;

import android.app.Activity;

import kr.co.goms.module.common.base.BaseBean;
import kr.co.goms.module.common.manager.BottomDialogCommandManager;
import kr.co.goms.module.common.manager.DialogManager;
import kr.co.goms.module.common.manager.HttpClientManager;


/**
 * 앱 초기화
 */
public class CommandAppInit extends Command {
    @Override
    protected BaseBean onCommand(Activity activity, Object o, BaseBean baseData) throws Exception {

        BottomDialogCommandManager.D();
        DialogManager.D();
        HttpClientManager.D();

        return null;
    }
}
