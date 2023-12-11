package kr.co.goms.module.common.manager;

import android.app.Activity;
import android.os.Bundle;

import kr.co.goms.module.common.base.BaseBean;
import kr.co.goms.module.common.base.WaterCallBack;
import kr.co.goms.module.common.command.BaseDialogCommand;
import kr.co.goms.module.common.command.Command;
import kr.co.goms.module.common.command.PermissionInfoBottomDialogCommand;

/**
 * Common BottomDialog Manager
 * 2021.11.05 hjh
 */
public class BottomDialogCommandManager {
    public static BottomDialogCommandManager instance;
    public Command mCommand;
    private BottomDialogCommandManager.OnSelectClickListener mOnSelectClickListener;
    Bundle mBundle = new Bundle();
    
    //생성자
    public BottomDialogCommandManager(){

    }

    //싱클톤
    public static BottomDialogCommandManager I() {
        if(null == instance) {
            instance = new BottomDialogCommandManager();
        }

        return instance;
    }

    protected void onDestroy() {

    }

    public static void D() {
        if(null != instance) {
            instance.onDestroy();
            instance = null;
        }
    }

    //명령어 세팅
    public BottomDialogCommandManager setCommand(Command command){
        mCommand = command;
        return instance;
    }

    //명령어 실행
    public BottomDialogCommandManager startCommand(Activity activity, Object object){
        mCommand.command(activity, object);
        return instance;
    }

    public BottomDialogCommandManager setTitle(String title){
        mBundle.putString("title", title);
        return instance;
    }

    public BottomDialogCommandManager setMessage(String message){
        mBundle.putString("message", message);
        return instance;
    }

    public BottomDialogCommandManager setShowTitle(boolean isShow){
        mBundle.putBoolean("isShowTitle", isShow);
        return instance;
    }

    public BottomDialogCommandManager setShowMessage(boolean isShow){
        mBundle.putBoolean("isShowMsg", isShow);
        return instance;
    }

    public BottomDialogCommandManager setShowCloseBtn(boolean isShow){
        mBundle.putBoolean("isShowCloseBtn", isShow);
        return instance;
    }

    public BottomDialogCommandManager setNagativeBtnName(int resId){
        mBundle.putInt("nagativeName", resId);
        return instance;
    }

    public BottomDialogCommandManager setPositiveBtnName(int resId){
        mBundle.putInt("positiveName", resId);
        return instance;
    }

    public BottomDialogCommandManager setCancelable(boolean cancelable){
        mBundle.putBoolean("isCancelable", cancelable);
        return instance;
    }

    public BottomDialogCommandManager setCancelTouchOutSide(boolean cancelableOutSide){
        mBundle.putBoolean("isCancelTouchOutSide", cancelableOutSide);
        return instance;
    }

    public BottomDialogCommandManager setCallback(OnSelectClickListener onSelectClickListener){
        mOnSelectClickListener = onSelectClickListener;
        return instance;
    }

    public BottomDialogCommandManager showDialog(Activity activity){
        instance.setCommand(mCommand).startCommand(activity, mBundle);
        return instance;
    }

    public interface OnSelectClickListener {
        void onLeftBtn(BaseBean baseBean);
        void onRightBtn(BaseBean baseBean);
    }

}
