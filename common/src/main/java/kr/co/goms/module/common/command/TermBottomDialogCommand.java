package kr.co.goms.module.common.command;

import android.app.Activity;
import android.os.Bundle;

import kr.co.goms.module.common.base.BaseBean;
import kr.co.goms.module.common.base.WaterCallBack;
import kr.co.goms.module.common.dialog.BottomDialogTerm;

/**
 * PermissionInfoBottomDialogCommand
 * 권한설정 취소 시, 알림 BottomPopup 입니다.
 * 사용예
 *         //전달 data
 *         Bundle bundle = new Bundle();
 *         bundle.putString("title","앱사용 권한 안내");
 *         bundle.putString("message", "하나원큐페이 앱을 사용하기 위해서는 [전화,저장] 권한을 허용해 주셔야 정상적인 서비스 이용이 가능합니다.\n\n앱 권한 설정화면으로 이동하시겠습니까?");
 *
 *         //매니져생성, setCommand, startCommand 처리
 *         BottomDialogCommandManager.getInstance().setCommand(new PermissionInfoBottomDialogCommand(PaymentSettingActivity.this, "0001",R.layout.base_bottom_popup, new Callback() {
 *             @Override
 *             public void update(BaseData baseData) {
 *                 String btnType = ((Bundle)baseData.getObj()).getString(BaseBottomDialogCommand.EXT_BTN_TYPE);
 *                 if(BaseBottomDialogCommand.BTN_TYPE.LEFT.name().equalsIgnoreCase(btnType)){
 *                     Toast.makeText(PaymentSettingActivity.this, "앱 종료", Toast.LENGTH_SHORT);
 *                     //앱 종료 처리
 *                     new CommandExit();
 *                 }else if(BaseBottomDialogCommand.BTN_TYPE.RIGHT.name().equalsIgnoreCase(btnType)){
 *                     //앱 설정이동
 *                     Toast.makeText(PaymentSettingActivity.this, "설정이동", Toast.LENGTH_SHORT);
 *                     Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
 *                     intent.setData(Uri.parse("package:" + getPackageName()));
 *                     startActivity(intent);
 *                     finish();
 *                 }
 *             }
 *         })).startCommand(PaymentSettingActivity.this, bundle);
 */
public class TermBottomDialogCommand extends Command {

    private final String TAG = TermBottomDialogCommand.class.getSimpleName();
    private Activity mActivity;
    private WaterCallBack mCommandCallBack;
    private static String mDialogCode = "0000";
    private static int mDialogResourceId;

    private static TermBottomDialogCommand mBaseDialogCommand;

    public static final String EXT_BTN_TYPE = "btn_type";
    public enum BTN_TYPE{
        LEFT,
        RIGHT,
    }

    //생성자
    public TermBottomDialogCommand(){

    }

    public TermBottomDialogCommand(Activity activity, WaterCallBack commandCallback){
        mActivity = activity;
        mCommandCallBack = commandCallback;
    }

    //싱클톤
    public static TermBottomDialogCommand getInstance(){
        if(mBaseDialogCommand == null){
            mBaseDialogCommand = new TermBottomDialogCommand();
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
    public TermBottomDialogCommand(Activity activity, String dialogCode, int resourceId, WaterCallBack commandCallback){
        mActivity = activity;
        mDialogCode = dialogCode;
        mDialogResourceId = resourceId;
        mCommandCallBack = commandCallback;
    }


    public TermBottomDialogCommand setBaseDialogCommand(Activity activity, String dialogCode, int resourceId, WaterCallBack commandCallback){
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

        final BottomDialogTerm baseBottomDialog = new BottomDialogTerm(activity, object);

        int negativeName = ((Bundle)object).getInt("negativeName");   //왼쪽 버튼 이름
        int positiveName = ((Bundle)object).getInt("positiveName");   //오른쪽 버튼 이름

        if(negativeName != -1) {
            baseBottomDialog.setOnLeftButtonListener(negativeName, new BaseCommand(BaseBean.STATUS.FAIL, "", new WaterCallBack() {
                @Override
                public void callback(BaseBean baseData) {
                    //왼쪽 버튼에 대한 콜백
                    Bundle bundle = new Bundle();
                    bundle.putString(EXT_BTN_TYPE, BTN_TYPE.LEFT.name());
                    baseData.setObject(bundle);
                    mCommandCallBack.callback(baseData);
                    baseBottomDialog.onDismiss();
                }
            }));
        }

        baseBottomDialog.setOnRightButtonListener(positiveName, new BaseCommand(BaseBean.STATUS.SUCCESS, "", new WaterCallBack() {
            @Override
            public void callback(BaseBean baseData) {
                //오른쪽 버튼에 대한 콜백
                Bundle bundle = new Bundle();
                bundle.putString(EXT_BTN_TYPE, BTN_TYPE.RIGHT.name());

                baseData.setObject(bundle);

                mCommandCallBack.callback(baseData);
                baseBottomDialog.onDismiss();
            }
        }));
        baseBottomDialog.show();

    }

}
