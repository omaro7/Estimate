package kr.co.goms.module.common.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Looper;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import kr.co.goms.module.common.CommonModule;
import kr.co.goms.module.common.R;
import kr.co.goms.module.common.base.BaseBean;
import kr.co.goms.module.common.base.WaterCallBack;
import kr.co.goms.module.common.command.Command;
import kr.co.goms.module.common.util.StringUtil;

/**
 * Created by Administrator on 2019-07-23.
 */

public class BottomDialogAppExit extends BottomDialogCommon{

    public BottomDialogAppExit(Activity activity, Object object){
        this.activity = activity;
        this.titleString = ((Bundle)object).getString("title");
        this.messageString = ((Bundle)object).getString("message");
        this.isShowTitle = ((Bundle)object).getBoolean("isShowTitle", false); //하단 팝업에 대한 제목 노출 여부
        this.isShowMsg = ((Bundle)object).getBoolean("isShowMsg", false);   //하단 팝업에 대한 내용 노출 여부

        this.negativeName = ((Bundle)object).getString("negativeName");
        this.negativeName = ((Bundle)object).getString("positiveName");

        this.isShowCloseBtn = ((Bundle)object).getBoolean("isShowCloseBtn", true);   //하단 팝업에 대한 Close(닫기) 버튼 노출 여부
        this.isCancelable = ((Bundle)object).getBoolean("isCancelable", true);                 //BottomDialog는 전체영역이기에 작동하지 않음
        this.isCancelTouchOutSide = ((Bundle)object).getBoolean("isCancelTouchOutSide", true); //BottomDialog는 전체영역이기에 작동하지 않음

    }

    protected void onCreate(Activity activity, String title, String message) {
        this.activity = activity;

        dialog = new Dialog(activity);
        dialog.setCancelable(isCancelable);
        dialog.setCanceledOnTouchOutside(isCancelTouchOutSide);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height= WindowManager.LayoutParams.MATCH_PARENT;
        params.windowAnimations = R.style.DialogAnimationPopupStyle;
        dialog.getWindow().setAttributes(params);
        dialog.setContentView(R.layout.bottom_popup_appexit);
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
        }else{
            titleTV.setVisibility(View.GONE);
        }

        if(isShowMsg || StringUtil.isNotNull(message)){
            messageTV.setVisibility(View.VISIBLE);
        }else{
            messageTV.setVisibility(View.GONE);
        }

        titleTV.setText(title);
        titleTV.setContentDescription(title);

        messageTV.setText(message);
        messageTV.setContentDescription(message);

        if(isShowCloseBtn){
            dialog.findViewById(R.id.iv_close).setVisibility(View.VISIBLE);
        }else{
            dialog.findViewById(R.id.iv_close).setVisibility(View.GONE);
        }

        dialog.findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDismiss();
            }
        });

        AdView adViewMiddle = dialog.findViewById(R.id.adview_middle);
        if(CommonModule.isFree) {
            //띠배너 호출
            //광고
            AdRequest adRequestMiddle = new AdRequest.Builder().build();
            adViewMiddle.loadAd(adRequestMiddle);
        }else{
            adViewMiddle.setVisibility(View.GONE);
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

    public void setOnLeftButtonListener(String name, final Command command, WaterCallBack waterCallBack) {
        setOnLeftButtonListener(name, command);
        negativeCallback = waterCallBack;
    }

    private void setClickListener(View v, final Command command, final WaterCallBack waterCallBack) {
        v.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                dialog.dismiss();
                dialog = null;
                if (null != command) {
                    command.command(activity, "");
                }

                if (null != waterCallBack) {
                    BaseBean baseData = new BaseBean();
                    baseData.setStatus(BaseBean.STATUS.SUCCESS, "");
                    waterCallBack.callback(baseData);
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

    protected boolean naverOpenCheck() {
        return false;
    }

    private void onShow() {

        if(true == positiveButtonFlag && true == negativeButtonFlag) {
            // 버튼 2개
            dialog.findViewById(R.id.bt_rl).setVisibility(View.VISIBLE);
            dialog.findViewById(R.id.ok_bt1).setVisibility(View.GONE);

            positiveButton = dialog.findViewById(R.id.ok_bt2);
            negativeButton = dialog.findViewById(R.id.cancel_bt);
        }else if(!positiveButtonFlag && !negativeButtonFlag){
            // 버튼 0개
            dialog.findViewById(R.id.bt_rl).setVisibility(View.GONE);
            dialog.findViewById(R.id.ok_bt1).setVisibility(View.GONE);
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
            setClickListener(positiveButton, positiveCommand, positiveCallback);
        }

        if(false == StringUtil.isEmpty(negativeName)) {
            negativeButton.setText(negativeName);
            setClickListener(negativeButton, negativeCommand, negativeCallback);
        }

        if(true == naverOpenCheck()) {
            // 처리할 내용 없다.
        } else {
            WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setAttributes(params);
            dialog.show();
        }
    }

    //메시지 중간 텍스트 색상변경.
    public void setSpannableMessage(String spannableString, int spannableColor) {
        this.spannableString = spannableString;
        this.spannableColor = spannableColor;
    }

    private void changeButtonState(String bgColor, String tvColor, boolean isEnabled) {
        if(isEnabled){
            positiveButton.setBackgroundResource(R.drawable.bg_round_ractangle_00a49d);
            positiveButton.setTextColor(Color.parseColor("#ffffff"));
            positiveButton.setEnabled(true);
        }else{
            positiveButton.setBackgroundResource(R.drawable.bg_round_ractangle_e5e5e5);
            positiveButton.setTextColor(Color.parseColor("#a2a2a2"));
            positiveButton.setEnabled(false);
        }
    }
}
