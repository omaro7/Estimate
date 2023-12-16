package kr.co.goms.app.estimate.command;

import android.app.Activity;
import android.os.Bundle;
import android.os.Parcelable;

import kr.co.goms.app.estimate.dialog.BottomDialogItemForm;
import kr.co.goms.module.common.base.BaseBean;
import kr.co.goms.module.common.base.WaterCallBack;
import kr.co.goms.module.common.command.BaseCommand;
import kr.co.goms.module.common.command.Command;
import kr.co.goms.module.common.dialog.BottomDialogInputForm;
import kr.co.goms.module.common.manager.DialogManager;
import kr.co.goms.module.common.util.StringUtil;

public class ItemFormBottomDialogCommand extends Command {

    private final String TAG = ItemFormBottomDialogCommand.class.getSimpleName();
    private Activity mActivity;
    private WaterCallBack mCommandCallBack;
    private static String mDialogCode = "0000";
    private static int mDialogResourceId;

    private static ItemFormBottomDialogCommand mBaseDialogCommand;

    public static final String EXT_BTN_TYPE = "btn_type";
    public enum BTN_TYPE{
        LEFT,
        RIGHT,
        SELECT,
    }

    public static final String EXT_INPUT = "ext_input";
    public static final String EXT_OBJECT = "ext_object";

    //생성자
    public ItemFormBottomDialogCommand(){

    }

    public ItemFormBottomDialogCommand(Activity activity, WaterCallBack commandCallback){
        mActivity = activity;
        mCommandCallBack = commandCallback;
    }

    //싱클톤
    public static ItemFormBottomDialogCommand getInstance(){
        if(mBaseDialogCommand == null){
            mBaseDialogCommand = new ItemFormBottomDialogCommand();
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
    public ItemFormBottomDialogCommand(Activity activity, String dialogCode, int resourceId, WaterCallBack commandCallback){
        mActivity = activity;
        mDialogCode = dialogCode;
        mDialogResourceId = resourceId;
        mCommandCallBack = commandCallback;
    }

    public ItemFormBottomDialogCommand setBaseDialogCommand(Activity activity, String dialogCode, int resourceId, WaterCallBack commandCallback){
        mActivity = activity;
        mDialogCode = dialogCode;
        mDialogResourceId = resourceId;
        mCommandCallBack = commandCallback;
        return mBaseDialogCommand;
    }

    public ItemFormBottomDialogCommand setBaseDialogCommand(Activity activity, WaterCallBack commandCallback){
        mActivity = activity;
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

        final BottomDialogItemForm baseBottomDialog = new BottomDialogItemForm(activity, object);

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
                bundle.putParcelable(EXT_OBJECT, (Parcelable) baseBean.getObject());
                baseBean.setObject(bundle);

                mCommandCallBack.callback(baseBean);
                baseBottomDialog.onDismiss();
            }
        });

        baseBottomDialog.show();

    }

}
