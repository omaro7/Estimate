package kr.co.goms.module.common.manager;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.DatePicker;

import java.util.ArrayList;
import java.util.Calendar;

import kr.co.goms.module.common.base.BaseBean;
import kr.co.goms.module.common.base.WaterCallBack;
import kr.co.goms.module.common.command.BaseBottomDialogCommand;
import kr.co.goms.module.common.command.BaseDialogCommand;
import kr.co.goms.module.common.command.Command;
import kr.co.goms.module.common.util.DateUtil;
import kr.co.goms.module.common.util.GomsLog;
import kr.co.goms.module.common.util.StringUtil;

/**
 * DialogManager 매니저
 * 1.DialogManager 매니저 사용법
 DialogManager.I().setTitle("앱사용 권한 안내")
 .setMessage("해당 앱을 사용하기 위해서는 [전화,저장] 권한을 허용해 주셔야 정상적인 서비스 이용이 가능합니다.\n\n앱 권한 설정화면으로 이동하시겠습니까?")
 .setShowTitle(false)
 .setShowMessage(true)
 .setNagativeBtnName(R.string.no)
 .setPositiveBtnName(R.string.yes)
 .setCancelable(true)
 .setCancelTouchOutSide(true)
 .setCommand(DialogCommandFactory.I().createDialogCommand(StartActivity.this, DialogCommandFactory.DIALOG_TYPE.permission.name(), new WaterCallBack() {
@Override
public void callback(BaseBean baseData) {
String btnType = ((Bundle)baseData.getObject()).getString(BaseBottomDialogCommand.EXT_BTN_TYPE);
if(BaseBottomDialogCommand.BTN_TYPE.LEFT.name().equalsIgnoreCase(btnType)){
//앱 종료 처리
Command exit = new CommandExit();
exit.command(StartActivity.this, "");
}else if(BaseBottomDialogCommand.BTN_TYPE.RIGHT.name().equalsIgnoreCase(btnType)){
//설정이동
Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
intent.setData(Uri.parse("package:" + StartActivity.this.getPackageName()));
StartActivity.this.startActivity(intent);
StartActivity.this.finish();
}
}
}))
 .showDialog(StartActivity.this);
 *
 */
public class DialogManager {

    static final String LOG_TAG = DialogManager.class.getSimpleName();

    static DialogManager instance;
    Bundle mDialogBundle = new Bundle();
    private Command mCommand;
    private OnSelectClickListener mOnSelectClickListener;

    public static final String EXT_DATE = "ext_date";

    public enum DIALOG_TEXT {
        tag("tag"),
        title("title"),
        message("message"),
        isShowTitle("isShowTitle"),
        iNoOpenDay("iNoOpenDay"),
        isShowMsg("isShowMsg"),
        isShowSearch("isShowSearch"),
        isShowCloseBtn("isShowCloseBtn"),
        negativeName("negativeName"),
        positiveName("positiveName"),
        isCancelable("isCancelable"),
        isCancelTouchOutSide("isCancelTouchOutSide"),
        arrayList("arrayList"),
        ;

        DIALOG_TEXT(String title) {
        }
    }

    public static final String EXT_BTN_TYPE = "btn_type";
    public enum BTN_TYPE{
        LEFT,
        RIGHT,
        SELECT,
    }

    protected DialogManager() {
    }

    protected void onDestroy() {

    }

    public static DialogManager I() {
        if(null == instance) {
            instance = new DialogManager();
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

    public DialogManager setTag(String tag){
        mDialogBundle.putString(DIALOG_TEXT.tag.name(), tag);
        return instance;
    }

    public DialogManager setTitle(String title){
        mDialogBundle.putString(DIALOG_TEXT.title.name(), title);
        return instance;
    }

    public DialogManager setMessage(String message){
        mDialogBundle.putString(DIALOG_TEXT.message.name(), message);
        return instance;
    }

    public DialogManager setShowTitle(boolean isShow){
        mDialogBundle.putBoolean(DIALOG_TEXT.isShowTitle.name(), isShow);
        return instance;
    }

    /**
    //-1 : 노출하지 않음, 0 : 노출하되, 한번 보여주고 안보여주기, 1,2,3,5,7(일주일),15,30(한달)
    */
    public DialogManager setNoOpenDay(int noOpenDay){
        mDialogBundle.putInt(DIALOG_TEXT.iNoOpenDay.name(), noOpenDay);  //
        return instance;
    }

    public DialogManager setShowMessage(boolean isShow){
        mDialogBundle.putBoolean(DIALOG_TEXT.isShowMsg.name(), isShow);
        return instance;
    }
    public DialogManager setShowSearch(boolean isShow){
        mDialogBundle.putBoolean(DIALOG_TEXT.isShowSearch.name(), isShow);
        return instance;
    }

    public DialogManager setShowCloseBtn(boolean isShow){
        mDialogBundle.putBoolean(DIALOG_TEXT.isShowCloseBtn.name(), isShow);
        return instance;
    }
	
    public DialogManager setNegativeBtnName(String name){
        mDialogBundle.putString(DIALOG_TEXT.negativeName.name(), name);
        return instance;
    }

    public DialogManager setPositiveBtnName(String name){
        mDialogBundle.putString(DIALOG_TEXT.positiveName.name(), name);
        return instance;
    }

    public DialogManager setCancelable(boolean cancelable){
        mDialogBundle.putBoolean(DIALOG_TEXT.isCancelable.name(), cancelable);
        return instance;
    }

    public DialogManager setCancelTouchOutSide(boolean cancelableOutSide){
        mDialogBundle.putBoolean(DIALOG_TEXT.isCancelTouchOutSide.name(), cancelableOutSide);
        return instance;
    }

    public DialogManager setDataList(ArrayList object){
        mDialogBundle.putParcelableArrayList(DIALOG_TEXT.arrayList.name(), object);
        return instance;
    }

    public DialogManager setCommand(Command command){
        mCommand = command;
        return instance;
    }

    public DialogManager setCallback(OnSelectClickListener onSelectClickListener){
        mOnSelectClickListener = onSelectClickListener;
        return instance;
    }

    public DialogManager showDialog(){
        return instance;
    }

    public Bundle getDialogBundle(){
        return mDialogBundle;
    }

    public DialogManager showDialog(Activity activity){
        BottomDialogCommandManager.I().setCommand(mCommand).startCommand(activity, mDialogBundle);
        return instance;
    }

    public void showBottomDialogBasicType(Activity activity, String title, String message){
        Bundle bundle = new Bundle();
        bundle.putString(DIALOG_TEXT.title.name(), title);
        bundle.putString(DIALOG_TEXT.message.name(), message);
        bundle.putBoolean(DIALOG_TEXT.isShowTitle.name(), true);
        bundle.putBoolean(DIALOG_TEXT.isShowMsg.name(), true);
        bundle.putString(DIALOG_TEXT.negativeName.name(), "");
        bundle.putString(DIALOG_TEXT.positiveName.name(), activity.getString(kr.co.goms.module.common.R.string.confirm));

        Command command = DialogCommandFactory.I().createDialogCommand(activity, DialogCommandFactory.DIALOG_TYPE.bottom_basic.name(), null);
        BottomDialogCommandManager.I().setCommand(command).startCommand(activity, bundle);
    }

    public void showBottomDialogAppExitType(Activity activity, String title, String message, WaterCallBack waterCallBack){
        Bundle bundle = new Bundle();
        bundle.putString(DIALOG_TEXT.title.name(), title);
        bundle.putString(DIALOG_TEXT.message.name(), message);
        bundle.putBoolean(DIALOG_TEXT.isShowTitle.name(), true);
        bundle.putBoolean(DIALOG_TEXT.isShowMsg.name(), true);
        bundle.putString(DIALOG_TEXT.negativeName.name(), "");
        bundle.putString(DIALOG_TEXT.positiveName.name(), activity.getString(kr.co.goms.module.common.R.string.finish));

        Command command = DialogCommandFactory.I().createDialogCommand(activity, DialogCommandFactory.DIALOG_TYPE.bottom_app_exit.name(), waterCallBack);
        BottomDialogCommandManager.I().setCommand(command).startCommand(activity, bundle);
    }

    public void showDialogDatePicker(Activity activity, WaterCallBack waterCallBack){
        Calendar cal = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                String sMonth = StringUtil.intToString((month+1));
                if(Integer.parseInt(sMonth)<10)
                {
                    sMonth="0"+sMonth;
                }

                String date = StringUtil.intToString(year) + sMonth + dayOfMonth;

                Bundle bundle = new Bundle();
                bundle.putString(EXT_DATE, date);

                BaseBean baseBean = new BaseBean();
                baseBean.setObject(bundle);
                waterCallBack.callback(baseBean);
            }
        };

        new DatePickerDialog(activity, onDateSetListener, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE)).show();
    }

    public interface OnSelectClickListener {
        void onLeftBtn(BaseBean baseBean);
        void onRightBtn(BaseBean baseBean);
    }
}