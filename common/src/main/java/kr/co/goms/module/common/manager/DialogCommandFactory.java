package kr.co.goms.module.common.manager;

import android.app.Activity;

import kr.co.goms.module.common.base.WaterCallBack;
import kr.co.goms.module.common.command.AppExitBottomDialogCommand;
import kr.co.goms.module.common.command.BaseBottomDialogCommand;
import kr.co.goms.module.common.command.BaseDialogCommand;
import kr.co.goms.module.common.command.Command;
import kr.co.goms.module.common.command.InputFormBottomDialogCommand;
import kr.co.goms.module.common.command.NoOpenDayBottomDialogCommand;
import kr.co.goms.module.common.command.PermissionInfoBottomDialogCommand;
import kr.co.goms.module.common.command.SearchListBottomDialogCommand;
import kr.co.goms.module.common.command.TermBottomDialogCommand;
import kr.co.goms.module.common.command.TodayBottomDialogCommand;

/**
* DialogManager 매니저
*/

public class DialogCommandFactory {

    static final String LOG_TAG = DialogCommandFactory.class.getSimpleName();

    static DialogCommandFactory instance;
    private Command mCommand;
    public enum DIALOG_TYPE{
        basic,
        permission,
        bottom_basic,
		bottom_app_exit,
		term,
        today,
        no_open_day,
		search_list,
        input_form,
    }

    protected DialogCommandFactory() {
    }

    protected void onDestroy() {

    }

    public static DialogCommandFactory I() {
        if(null == instance) {
            instance = new DialogCommandFactory();
        }

        return instance;
    }

    public static void D() {
        if(null != instance) {
            instance.onDestroy();
            instance = null;
        }
    }

    public void init(){

    }

    public Command createDialogCommand(Activity activity, String type, WaterCallBack callback){
        if(DIALOG_TYPE.basic.name().equals(type)) {
            return BaseDialogCommand.getInstance().setBaseDialogCommand(activity, "", -1, callback);
        }else if(DIALOG_TYPE.permission.name().equals(type)) {
            return PermissionInfoBottomDialogCommand.getInstance().setBaseDialogCommand(activity, "", -1, callback);
        }else if(DIALOG_TYPE.bottom_basic.name().equals(type)){
            return BaseBottomDialogCommand.getInstance().setBaseDialogCommand(activity, "", -1, callback);
        }else if(DIALOG_TYPE.term.name().equals(type)) {
            return TermBottomDialogCommand.getInstance().setBaseDialogCommand(activity, "", -1, callback);
        }else if(DIALOG_TYPE.today.name().equals(type)) {
            return TodayBottomDialogCommand.getInstance().setBaseDialogCommand(activity, "", -1, callback);
        }else if(DIALOG_TYPE.no_open_day.name().equals(type)) {
            return NoOpenDayBottomDialogCommand.getInstance().setBaseDialogCommand(activity, "", -1, callback);
        }else if(DIALOG_TYPE.search_list.name().equals(type)) {
            return SearchListBottomDialogCommand.getInstance().setBaseDialogCommand(activity, "", -1, callback);
        }else if(DIALOG_TYPE.bottom_app_exit.name().equals(type)) {
            return AppExitBottomDialogCommand.getInstance().setBaseDialogCommand(activity, "", -1, callback);
        }else if(DIALOG_TYPE.input_form.name().equals(type)) {
            return InputFormBottomDialogCommand.getInstance().setBaseDialogCommand(activity, "", -1, callback);
        }else{
            return BaseDialogCommand.getInstance().setBaseDialogCommand(activity, "", -1, callback);
        }
    }

}
