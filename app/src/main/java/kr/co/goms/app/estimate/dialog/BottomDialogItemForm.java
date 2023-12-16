package kr.co.goms.app.estimate.dialog;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

import kr.co.goms.app.estimate.R;
import kr.co.goms.app.estimate.model.ItemBeanTB;
import kr.co.goms.module.common.base.BaseBean;
import kr.co.goms.module.common.base.WaterCallBack;
import kr.co.goms.module.common.command.Command;
import kr.co.goms.module.common.curvlet.CurvletManager;
import kr.co.goms.module.common.manager.DialogManager;
import kr.co.goms.module.common.model.GroupBeanS;
import kr.co.goms.module.common.util.StringUtil;

public class BottomDialogItemForm {

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
    protected EditText mEtItemName = null;
    protected EditText mEtItemStd = null;
    protected EditText mEtItemUnit = null;
    protected EditText mEtItemPrice = null;
    protected EditText mEtItemRemark = null;
    protected TextView titleTV = null;
    protected TextView messageTV = null;
    protected String tagString = null;
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

    private ArrayList mArrayList;

    public BottomDialogItemForm(Activity activity, Object object){
        this.activity = activity;
        this.tagString = ((Bundle)object).getString(DialogManager.DIALOG_TEXT.tag.name());
        this.titleString = ((Bundle)object).getString(DialogManager.DIALOG_TEXT.title.name());
        this.messageString = ((Bundle)object).getString(DialogManager.DIALOG_TEXT.message.name());
        this.isShowTitle = ((Bundle)object).getBoolean(DialogManager.DIALOG_TEXT.isShowTitle.name(), false); //하단 팝업에 대한 제목 노출 여부
        this.isShowMsg = ((Bundle)object).getBoolean(DialogManager.DIALOG_TEXT.isShowMsg.name(), false);   //하단 팝업에 대한 내용 노출 여부

        this.negativeName = ((Bundle)object).getString(DialogManager.DIALOG_TEXT.negativeName.name());
        this.positiveName = ((Bundle)object).getString(DialogManager.DIALOG_TEXT.positiveName.name());
        this.isShowCloseBtn = ((Bundle)object).getBoolean(DialogManager.DIALOG_TEXT.isShowCloseBtn.name(), true);   //하단 팝업에 대한 Close(닫기) 버튼 노출 여부
        this.isCancelable = ((Bundle)object).getBoolean(DialogManager.DIALOG_TEXT.isCancelable.name(), true);                 //BottomDialog는 전체영역이기에 작동하지 않음
        this.isCancelTouchOutSide = ((Bundle)object).getBoolean(DialogManager.DIALOG_TEXT.isCancelTouchOutSide.name(), true); //BottomDialog는 전체영역이기에 작동하지 않음

        this.mArrayList = ((Bundle)object).getParcelableArrayList("arrayList");
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
        params.windowAnimations = kr.co.goms.module.common.R.style.DialogAnimationPopupStyle;
        dialog.getWindow().setAttributes(params);
        dialog.setContentView(R.layout.bottom_popup_item_form);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if(null != dialog) {
                    dialog.dismiss();
                }
            }
        });

        mEtItemName = dialog.findViewById(R.id.et_item_name);
        mEtItemStd = dialog.findViewById(R.id.et_item_std);
        mEtItemUnit = dialog.findViewById(R.id.et_item_unit);
        mEtItemPrice = dialog.findViewById(R.id.et_item_price);
        mEtItemRemark = dialog.findViewById(R.id.et_item_remark);

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
                int id = v.getId();
                if(id == R.id.ok_bt2 || id == R.id.ok_bt1){

                    String itemName = mEtItemName.getText().toString();
                    String itemStd = mEtItemStd.getText().toString();
                    String itemUnit = mEtItemUnit.getText().toString();
                    String itemPrice = mEtItemPrice.getText().toString();
                    String itemRemark = mEtItemRemark.getText().toString();

                    if(StringUtil.isEmpty(itemName)){
                        CurvletManager.process(activity, null, "water://toast?text=상품명을 입력해주세요");
                        return;
                    }

                    ItemBeanTB itemBeanTB = new ItemBeanTB();
                    itemBeanTB.setItem_name(itemName);
                    itemBeanTB.setItem_std(itemStd);
                    itemBeanTB.setItem_unit(itemUnit);
                    itemBeanTB.setItem_price(itemPrice);
                    itemBeanTB.setItem_remark(itemRemark);

                    dialog.dismiss();
                    dialog = null;

                    BaseBean baseBean = new BaseBean();
                    baseBean.setObject(itemBeanTB);
                    positiveCallback.callback(baseBean);

                }
            }
        });
    }

    private boolean isExistGroupName(String groupName){
        boolean isExist = false;

        for(Object groupBeanS : mArrayList){
            if(groupName.equalsIgnoreCase(((GroupBeanS)groupBeanS).getRes_mh_group_name())){
                isExist = true;
            }
        }

        return isExist;
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


        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(params);
        dialog.show();
    }

}
