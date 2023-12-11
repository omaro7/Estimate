package kr.co.goms.module.common.command;

import android.app.Activity;
import android.os.Bundle;

import kr.co.goms.module.common.R;
import kr.co.goms.module.common.base.BaseBean;
import kr.co.goms.module.common.base.WaterCallBack;
import kr.co.goms.module.common.dialog.BottomDialogCommon;
import kr.co.goms.module.common.util.StringUtil;

/**
 * BaseBottomDialogCommand
 * 각 각의 BottomDialog도 interface 하고 싶지만, 유지보수가 해당 부분까지 하는게 도움이 될 것 같습니다.
 */
//공통 팝업 실행
public class BaseBottomDialogCommand extends Command {

    private final String TAG = BaseBottomDialogCommand.class.getSimpleName();
    private Activity mActivity;
    private WaterCallBack mCommandCallBack;
    private static String mDialogCode = "0000";             //BottomDialog code : 추후 코드로 분기 처리
    private static int mDialogResourceId;                   //BottomDialog XML Layout : 추후 layout 별도 처리

    private static BaseBottomDialogCommand mBaseDialogCommand;

    public static final String EXT_BTN_TYPE = "btn_type";   //버튼 타입 정의
    public enum BTN_TYPE{
        LEFT,
        RIGHT,
        NO_OPEN,
        SELECT,
    }

    //생성자
    public BaseBottomDialogCommand(){

    }

    //싱클톤
    public static BaseBottomDialogCommand getInstance(){
        if(mBaseDialogCommand == null){
            mBaseDialogCommand = new BaseBottomDialogCommand();
        }
        return mBaseDialogCommand;
    }


    public BaseBottomDialogCommand(Activity activity, WaterCallBack commandCallback){
        mActivity = activity;
        mCommandCallBack = commandCallback;
    }

    /**
     * 기본 BottomDialog
     * @param activity
     * @param dialogCode    BottomDialog code : 추후 코드로 분기 처리
     * @param resourceId    BottomDialog XML Layout : 추후 layout 별도 처리
     * @param commandCallback
     */
    public BaseBottomDialogCommand(Activity activity, String dialogCode, int resourceId, WaterCallBack commandCallback){
        mActivity = activity;
        mDialogCode = dialogCode;
        mDialogResourceId = resourceId;
        mCommandCallBack = commandCallback;
    }

    @Override
    protected BaseBean onCommand(Activity activity, Object object, BaseBean baseBean) {
        goBaseDialog(activity, object , baseBean);
        return baseBean;
    }

    /**
     * 기본 BottomDialog
     * @param activity
     * @param dialogCode    BottomDialog code : 추후 코드로 분기 처리
     * @param resourceId    BottomDialog XML Layout : 추후 layout 별도 처리
     * @param commandCallback
     */
    public BaseBottomDialogCommand setBaseDialogCommand(Activity activity, String dialogCode, int resourceId, WaterCallBack commandCallback){
        mActivity = activity;
        mDialogCode = dialogCode;
        mDialogResourceId = resourceId;
        mCommandCallBack = commandCallback;
        return mBaseDialogCommand;
    }


    /**
     * 공통 팝업
     */
    private void goBaseDialog(Activity activity, Object object, BaseBean baseBean){

        final BottomDialogCommon baseBottomDialog = new BottomDialogCommon(activity, object);

        String negativeName = ((Bundle)object).getString("negativeName");   //왼쪽 버튼 이름
        String positiveName = ((Bundle)object).getString("positiveName");   //오른쪽 버튼 이름

        if(!StringUtil.isEmpty(negativeName)) {
            baseBottomDialog.setOnLeftButtonListener(negativeName, new BaseCommand(BaseBean.STATUS.FAIL, "", new WaterCallBack() {
                @Override
                public void callback(BaseBean baseData) {
                    //왼쪽 버튼에 대한 콜백
                    Bundle bundle = new Bundle();
                    bundle.putString(EXT_BTN_TYPE, SearchListBottomDialogCommand.BTN_TYPE.LEFT.name());
                    baseData.setObject(bundle);
                    mCommandCallBack.callback(baseData);
                    baseBottomDialog.onDismiss();
                }
            }));
        }

        if(!StringUtil.isEmpty(positiveName)) {
            baseBottomDialog.setOnRightButtonListener(positiveName, new BaseCommand(BaseBean.STATUS.SUCCESS, "", new WaterCallBack() {
                @Override
                public void callback(BaseBean baseData) {
                    //오른쪽 버튼에 대한 콜백
                    Bundle bundle = new Bundle();
                    bundle.putString(EXT_BTN_TYPE, SearchListBottomDialogCommand.BTN_TYPE.RIGHT.name());

                    if(baseData != null) {
                        baseData.setObject(bundle);
                    }else{
                        baseData = new BaseBean();
                        baseData.setObject(bundle);
                    }

                    mCommandCallBack.callback(baseData);
                    baseBottomDialog.onDismiss();
                }
            }));
        }
        baseBottomDialog.show();

    }

}
