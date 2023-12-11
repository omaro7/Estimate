package kr.co.goms.module.common.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Looper;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import kr.co.goms.module.common.R;
import kr.co.goms.module.common.base.BaseBean;
import kr.co.goms.module.common.base.WaterCallBack;
import kr.co.goms.module.common.command.Command;
import kr.co.goms.module.common.util.StringUtil;

/**
 * Created by Administrator on 2019-07-23.
 */

public class BaseDialog {

    protected Activity activity = null;
    protected Dialog dialog;
    protected boolean positiveButtonFlag = false;
    protected boolean negativeButtonFlag = false;
    protected Button positiveButton = null;
    protected Button negativeButton = null;

    protected String positiveName = null;
    protected String negativeName = null;
    protected Command positiveCommand = null;
    protected Command negativeCommand = null;
    protected WaterCallBack positiveCallback = null;
    protected WaterCallBack negativeCallback = null;

    protected TextView titleTV = null;
    protected TextView messageTV = null;

    protected String titleString = null;
    protected String messageString = null;

    //message 중간에 텍스트 색상 변경이 있을경우
    protected String spannableString = "";
    protected int spannableColor = -1;

    protected boolean isShowTitle = false;          //제목 노출 여부
    protected boolean isShowMsg = false;            //내용 노출 여부
    protected boolean isCancelable = false;         //BackKey 시, 닫기 여부
    protected boolean isCancelTouchOutSide = false; //바깥 영역 클릭 시, 닫기 여부
    protected boolean isShowCloseBtn = false;       //닫기 버튼 노출 여부

    protected String mLeftBtnText = "";
    protected String mRightBtnText = "";

    public BaseDialog(Activity activity, int title, int messageId) {
        this.activity = activity;
        titleString = activity.getString(title);
        messageString = activity.getString(messageId);
    }

    public BaseDialog(Activity activity, String title, String message) {
        this.activity = activity;
        titleString = title;
        messageString = message;
    }

    /**
     *
     * @param activity
     * @param title
     * @param message
     * @param _isShowTitle : 제목 노출 여부
     */
    public BaseDialog(Activity activity, String title, String message, boolean _isShowTitle) {
        this.activity = activity;
        titleString = title;
        messageString = message;
        isShowTitle = _isShowTitle;
    }

    /**
     *
     * @param activity
     * @param title
     * @param message
     * @param _isShowTitle          : 제목 노출 여부
     * @param _isCancelable         : 빽키 클릭 시, 닫기 여부
     * @param _isCancelTouchOutSide   : 바깥 영역 클릭 시, 닫기 여부. true이면, isCancelable도 true가 되어버림 ^^
     */
    public BaseDialog(Activity activity, String title, String message, boolean _isShowTitle, boolean _isCancelable, boolean _isCancelTouchOutSide) {
        this.activity = activity;
        titleString = title;
        messageString = message;
        isShowTitle = _isShowTitle;
        isCancelable = _isCancelable;
        isCancelTouchOutSide = _isCancelTouchOutSide;
    }

    /**
     *
     * @param activity
     * @param title
     * @param message
     * @param _isShowTitle          : 제목 노출 여부
     * @param _isCancelable         : 빽키 클릭 시, 닫기 여부
     * @param _isCancelTouchOutSide   : 바깥 영역 클릭 시, 닫기 여부. true이면, isCancelable도 true가 되어버림 ^^
     */
    public BaseDialog(Activity activity, String title, String message, boolean _isShowTitle, boolean _isMsgTitle, boolean _isCancelable, boolean _isCancelTouchOutSide) {
        this.activity = activity;
        titleString = title;
        messageString = message;
        isShowTitle = _isShowTitle;
        isShowMsg = _isMsgTitle;
        isCancelable = _isCancelable;
        isCancelTouchOutSide = _isCancelTouchOutSide;
    }

    public BaseDialog(Activity activity, String title, String message, boolean _isShowTitle, boolean _isShowMsg, boolean _isCancelable, boolean _isCancelTouchOutSide, boolean _isShowCloseBtn) {
        this.activity = activity;
        titleString = title;
        messageString = message;
        isShowTitle = _isShowTitle;
        isShowMsg = _isShowMsg;
        isCancelable = _isCancelable;
        isCancelTouchOutSide = _isCancelTouchOutSide;
        isShowCloseBtn = _isShowCloseBtn;
    }

    public BaseDialog(Activity activity, Object object){
        this.activity = activity;
        this.titleString = ((Bundle)object).getString("title");
        this.messageString = ((Bundle)object).getString("message");
        this.isShowTitle = ((Bundle)object).getBoolean("isShowTitle", false); //하단 팝업에 대한 제목 노출 여부
        this.isShowMsg = ((Bundle)object).getBoolean("isShowMsg", false);   //하단 팝업에 대한 내용 노출 여부
        this.isCancelable = ((Bundle)object).getBoolean("isCancelable", false);
        this.isCancelTouchOutSide = ((Bundle)object).getBoolean("isCancelTouchOutSide", true);
    }

    protected void onCreate(Activity activity, String title, String message) {
        this.activity = activity;

        dialog = new Dialog(activity);

        dialog.setCancelable(isCancelable);
        dialog.setCanceledOnTouchOutside(isCancelTouchOutSide);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.CENTER);
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height= WindowManager.LayoutParams.MATCH_PARENT;
        //params.windowAnimations = R.style.DialogAnimationPopupStyle;
        dialog.getWindow().setAttributes(params);
        dialog.setContentView(R.layout.ui_base_popup_round);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if(null != dialog) {
                    dialog.dismiss();
                }
            }
        });

        titleTV = dialog.findViewById(R.id.title_tv);
        messageTV = dialog.findViewById(R.id.context_tv1);

        if(isShowTitle){
            titleTV.setVisibility(View.VISIBLE);
            dialog.findViewById(R.id.llt_title).setVisibility(View.VISIBLE);
        }else{
            titleTV.setVisibility(View.GONE);
            dialog.findViewById(R.id.llt_title).setVisibility(View.GONE);
        }

        if(isShowMsg){
            messageTV.setVisibility(View.VISIBLE);
            dialog.findViewById(R.id.llt_context).setVisibility(View.VISIBLE);
        }else{
            messageTV.setVisibility(View.GONE);
            dialog.findViewById(R.id.llt_context).setVisibility(View.GONE);
        }

        titleTV.setText(title);
        messageTV.setText(message);

        titleTV.setContentDescription(title);
        messageTV.setContentDescription(message);

        //메시지 중간 텍스트 색상변경.
        if(!spannableString.equals("") && spannableColor != -1) {
            String messageText = messageTV.getText().toString();
            SpannableStringBuilder ssb = new SpannableStringBuilder(messageText);
            ssb.setSpan(new ForegroundColorSpan(spannableColor), messageText.indexOf(spannableString), messageText.indexOf(spannableString)+spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            messageTV.setText(ssb);
        }

    }

    public void setOnRightButtonListener(String name, final Command command) {
        if (activity == null || activity.isFinishing()) {
            return;
        }

        positiveButtonFlag = true;
        positiveName = name;
        positiveCommand = command;
    }

    public void setOnRightButtonListener(String name, final Command command, WaterCallBack callback) {
        setOnRightButtonListener(name, command);
        positiveCallback = callback;
    }

    public void setOnLeftButtonListener(String name, final Command command) {
        if (activity == null || activity.isFinishing()) {
            return;
        }

        negativeButtonFlag = true;
        negativeName = name;
        negativeCommand = command;
    }

    public void setOnLeftButtonListener(String name, final Command command, WaterCallBack callback) {
        setOnLeftButtonListener(name, command);
        negativeCallback = callback;
    }

    private void setClickListener(View v, final Command command, final WaterCallBack callback) {
        v.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
                dialog = null;
                if (null != command) {
                    command.command(activity, "");
                }

                if (null != callback) {
                    BaseBean baseData = new BaseBean();
                    baseData.setStatus(BaseBean.STATUS.SUCCESS, "");
                    callback.callback(baseData);
                }
            }
        });
    }

    public void show() {

        if (Looper.myLooper() == Looper.getMainLooper()) {
            if(null == dialog) {
                onCreate(activity, titleString, messageString);
            }

            if(false == dialog.isShowing()) {
                onShow();
            }
        } else {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(null == dialog) {
                        onCreate(activity, titleString, messageString);
                    }

                    if(false == dialog.isShowing()) {
                        onShow();
                    }
                }
            });
        }
    }

    /**
     * DialogFragment에서 BasePopup > dismiss() 호출
     */
    public void onDismiss(){
        if(dialog != null) {
            dialog.dismiss();
        }
    }

    private void onShow() {

        //접근성 초점 이동 시, 앞 뒤에 공간 생성
        titleTV.setContentDescription(" " +titleTV.getText().toString()+ " ");
        messageTV.setContentDescription(" " +messageTV.getText().toString()+" ");

        if(true == positiveButtonFlag && true == negativeButtonFlag) {
            // 버튼 2개
            dialog.findViewById(R.id.bt_rl).setVisibility(View.VISIBLE);
            dialog.findViewById(R.id.ok_bt1).setVisibility(View.GONE);

            positiveButton = dialog.findViewById(R.id.ok_bt2);
            negativeButton = dialog.findViewById(R.id.cancel_bt);
        } else {
            // 버튼 1개
            dialog.findViewById(R.id.bt_rl).setVisibility(View.GONE);
            dialog.findViewById(R.id.ok_bt1).setVisibility(View.VISIBLE);

            positiveButton = dialog.findViewById(R.id.ok_bt1);
            negativeButton = dialog.findViewById(R.id.ok_bt1);
            if(false == StringUtil.isEmpty(positiveName)) {
                negativeName = positiveName;
                negativeCommand = positiveCommand;
                negativeCallback = positiveCallback;
            } else {
                positiveName = negativeName;
                positiveCommand = negativeCommand;
                positiveCallback = negativeCallback;
            }
        }

        if(false == StringUtil.isEmpty(positiveName)) {
            positiveButton.setText(positiveName);
        }
        setClickListener(positiveButton, positiveCommand, positiveCallback);

        if(false == StringUtil.isEmpty(negativeName)) {
            negativeButton.setText(negativeName);
        }
        setClickListener(negativeButton, negativeCommand, negativeCallback);

        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(params);
        dialog.show();
    }

    //메시지 중간 텍스트 색상변경.
    public void setSpannableMessage(String spannableString, int spannableColor) {
        this.spannableString = spannableString;
        this.spannableColor = spannableColor;
    }

}
