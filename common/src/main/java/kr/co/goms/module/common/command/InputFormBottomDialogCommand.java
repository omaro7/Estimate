package kr.co.goms.module.common.command;

import android.app.Activity;
import android.os.Bundle;

import kr.co.goms.module.common.base.BaseBean;
import kr.co.goms.module.common.base.WaterCallBack;
import kr.co.goms.module.common.dialog.BottomDialogInputForm;
import kr.co.goms.module.common.dialog.BottomDialogSearchList;
import kr.co.goms.module.common.manager.DialogManager;
import kr.co.goms.module.common.model.SearchListBean;
import kr.co.goms.module.common.util.StringUtil;

public class InputFormBottomDialogCommand extends Command {

    private final String TAG = InputFormBottomDialogCommand.class.getSimpleName();
    private Activity mActivity;
    private WaterCallBack mCommandCallBack;
    private static String mDialogCode = "0000";
    private static int mDialogResourceId;

    private static InputFormBottomDialogCommand mBaseDialogCommand;

    public static final String EXT_BTN_TYPE = "btn_type";
    public enum BTN_TYPE{
        LEFT,
        RIGHT,
        SELECT,
    }

    public static final String EXT_INPUT = "ext_input";

    //생성자
    public InputFormBottomDialogCommand(){

    }

    public InputFormBottomDialogCommand(Activity activity, WaterCallBack commandCallback){
        mActivity = activity;
        mCommandCallBack = commandCallback;
    }

    //싱클톤
    public static InputFormBottomDialogCommand getInstance(){
        if(mBaseDialogCommand == null){
            mBaseDialogCommand = new InputFormBottomDialogCommand();
        }
        return mBaseDialogCommand;
    }


    /**
     * 기본 BottomDialog
     * @param activity
     * @param dialogCode    BottomDialog code : 추후 코드로 분기 처리
     * @param resourceId    BottomDialog XML Layout : 추후 layout 별도 처리
     * @param commandCallback
     */
    public InputFormBottomDialogCommand(Activity activity, String dialogCode, int resourceId, WaterCallBack commandCallback){
        mActivity = activity;
        mDialogCode = dialogCode;
        mDialogResourceId = resourceId;
        mCommandCallBack = commandCallback;
    }


    public InputFormBottomDialogCommand setBaseDialogCommand(Activity activity, String dialogCode, int resourceId, WaterCallBack commandCallback){
        mActivity = activity;
        mDialogCode = dialogCode;
        mDialogResourceId = resourceId;
        mCommandCallBack = commandCallback;
        return mBaseDialogCommand;
    }


    @Override
    protected BaseBean onCommand(Activity activity, Object object, BaseBean baseData) throws Exception {
        goBaseDialog(activity, object , baseData);
        return baseData;
    }

    /**
     * 공통 팝업
     */
    private void goBaseDialog(Activity activity, Object object, BaseBean baseData){

        final BottomDialogInputForm baseBottomDialog = new BottomDialogInputForm(activity, object);

        String negativeName = ((Bundle)object).getString(DialogManager.DIALOG_TEXT.negativeName.name());   //왼쪽 버튼 이름
        String positiveName = ((Bundle)object).getString(DialogManager.DIALOG_TEXT.positiveName.name());   //오른쪽 버튼 이름

        if(!StringUtil.isEmpty(negativeName)) {
            baseBottomDialog.setOnLeftButtonListener(negativeName, new BaseCommand(BaseBean.STATUS.FAIL, "", new WaterCallBack() {
                @Override
                public void callback(BaseBean baseBean) {
                    //왼쪽 버튼에 대한 콜백
                    Bundle bundle = new Bundle();
                    bundle.putString(EXT_BTN_TYPE, BTN_TYPE.LEFT.name());
                    baseBean.setObject(bundle);
                    mCommandCallBack.callback(baseBean);
                    baseBottomDialog.onDismiss();
                }
            }));
        }

        baseBottomDialog.setOnRightButtonListener(positiveName, new BaseCommand(BaseBean.STATUS.SUCCESS, "", null), new WaterCallBack() {
            @Override
            public void callback(BaseBean baseBean) {
                //오른쪽 버튼에 대한 콜백
                Bundle bundle = new Bundle();
                bundle.putString(EXT_BTN_TYPE, BTN_TYPE.RIGHT.name());
                bundle.putString(EXT_INPUT, baseBean.getData());
                baseBean.setObject(bundle);

                mCommandCallBack.callback(baseBean);
                baseBottomDialog.onDismiss();
            }
        });

        baseBottomDialog.show();

    }

}
