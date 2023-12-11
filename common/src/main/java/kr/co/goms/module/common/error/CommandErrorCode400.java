package kr.co.goms.module.common.error;

import android.app.Activity;
import android.os.Bundle;

import kr.co.goms.module.common.R;
import kr.co.goms.module.common.base.BaseBean;
import kr.co.goms.module.common.base.WaterCallBack;
import kr.co.goms.module.common.command.BaseBottomDialogCommand;
import kr.co.goms.module.common.command.Command;
import kr.co.goms.module.common.manager.DialogCommandFactory;
import kr.co.goms.module.common.manager.DialogManager;
import kr.co.goms.module.common.model.CommonBean;

public class CommandErrorCode400 extends Command {

    @Override
    protected BaseBean onCommand(Activity activity, Object object, BaseBean baseData) throws Exception {
        DialogManager.I().setTitle("에러")
                .setMessage(((CommonBean)object).getRmsg())
                .setShowTitle(true)
                .setShowMessage(true)
                .setNegativeBtnName("")
                .setPositiveBtnName(activity.getString(R.string.confirm))
                .setCancelable(true)
                .setCancelTouchOutSide(true)
                .setCommand(DialogCommandFactory.I().createDialogCommand(activity, DialogCommandFactory.DIALOG_TYPE.basic.name(), new WaterCallBack() {
                    @Override
                    public void callback(BaseBean baseData) {
                        String btnType = ((Bundle)baseData.getObject()).getString(BaseBottomDialogCommand.EXT_BTN_TYPE);
                        if(BaseBottomDialogCommand.BTN_TYPE.LEFT.name().equalsIgnoreCase(btnType)){

                        }else if(BaseBottomDialogCommand.BTN_TYPE.RIGHT.name().equalsIgnoreCase(btnType)){

                        }
                    }
                }))
                .showDialog(activity);

        return null;
    }
}
