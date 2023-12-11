package kr.co.goms.module.common.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import kr.co.goms.module.common.R;
import kr.co.goms.module.common.WaterFramework;
import kr.co.goms.module.common.adapter.SearchListAdapter;
import kr.co.goms.module.common.base.BaseBean;
import kr.co.goms.module.common.base.WaterCallBack;
import kr.co.goms.module.common.command.Command;
import kr.co.goms.module.common.curvlet.CurvletManager;
import kr.co.goms.module.common.model.SearchListBean;
import kr.co.goms.module.common.observer.ObserverInterface;
import kr.co.goms.module.common.util.GomsLog;
import kr.co.goms.module.common.util.StringUtil;

/**
 * Created by Administrator on 2019-07-23.
 */

public class BottomDialogSearchList {

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
    protected WaterCallBack selectedCallback = null;

    protected EditText mEtSearch = null;
    protected TextView titleTV = null;
    protected TextView messageTV = null;

    protected String titleString = null;
    protected String messageString = null;

    //message 중간에 텍스트 색상 변경이 있을경우
    protected String spannableString = "";
    protected int spannableColor = -1;

    protected boolean isShowTitle = false;          //제목 노출 여부
    protected boolean isShowMsg = false;            //내용 노출 여부
    protected boolean isShowSearch = false;            //검색 노출 여부
    protected boolean isCancelable = false;         //BackKey 시, 닫기 여부
    protected boolean isCancelTouchOutSide = false; //바깥 영역 클릭 시, 닫기 여부
    protected boolean isShowCloseBtn = false;       //닫기 버튼 노출 여부

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private SearchListBean mCurrSearchListBean;
    private ObserverInterface mDataObserver;

    private RelativeLayout mRltSearch;

    private ArrayList mArrayList;

    public BottomDialogSearchList(Activity activity, Object object){
        this.activity = activity;
        this.titleString = ((Bundle)object).getString("title");
        this.messageString = ((Bundle)object).getString("message");
        this.isShowTitle = ((Bundle)object).getBoolean("isShowTitle", false); //하단 팝업에 대한 제목 노출 여부
        this.isShowMsg = ((Bundle)object).getBoolean("isShowMsg", false);   //하단 팝업에 대한 내용 노출 여부
        this.isShowSearch = ((Bundle)object).getBoolean("isShowSearch", false);   //하단 팝업에 대한 검색 노출 여부

        this.negativeName = ((Bundle)object).getString("negativeName");
        this.positiveName = ((Bundle)object).getString("positiveName");

        this.isShowCloseBtn = ((Bundle)object).getBoolean("isShowCloseBtn", true);   //하단 팝업에 대한 Close(닫기) 버튼 노출 여부
        this.isCancelable = ((Bundle)object).getBoolean("isCancelable", true);                 //BottomDialog는 전체영역이기에 작동하지 않음
        this.isCancelTouchOutSide = ((Bundle)object).getBoolean("isCancelTouchOutSide", true); //BottomDialog는 전체영역이기에 작동하지 않음

        this.mArrayList = ((Bundle)object).getParcelableArrayList("arrayList");
    }

    protected void onCreate(Activity activity, String title, String message) {
        this.activity = activity;

        GomsLog.d("Survey", "isCancelable : " + isCancelable);
        GomsLog.d("Survey", "isCancelTouchOutSide : " + isCancelTouchOutSide);

        int total = 0;
        try{
            total = mArrayList.size();
        }catch(NullPointerException e){

        }

        GomsLog.d("Survey", "mArrayList total : " + total);


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
        dialog.setContentView(R.layout.bottom_popup_search_list);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if(null != dialog) {
                    dialog.dismiss();
                }
            }
        });

        mRltSearch = dialog.findViewById(R.id.rlt_search);

        mEtSearch = dialog.findViewById(R.id.et_search);
        mEtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(((SearchListAdapter)mAdapter).getFilter() == null){
                    return;
                }else {
                    ((SearchListAdapter) mAdapter).getFilter().filter(charSequence);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        if(isShowSearch){
            mRltSearch.setVisibility(View.VISIBLE);
        }else{
            mRltSearch.setVisibility(View.GONE);
        }

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

        mDataObserver = new ObserverInterface() {
            @Override
            public void callback(BaseBean baseBean) {
                GomsLog.d("Survey", "observer  CallBack()");
                setAdapter((ArrayList<SearchListBean>)baseBean.getObject());
            }
        };

        mRecyclerView = dialog.findViewById(R.id.rv_search_list);
        setData();
    }

    private void setData(){
        //옵저버 호출하여 setAdapter() 처리
        BaseBean baseBean = new BaseBean();
        baseBean.setObject(mArrayList);
        mDataObserver.callback(baseBean);
    }

    private void setAdapter(ArrayList<SearchListBean> list){

        mAdapter = new SearchListAdapter(activity, list, new SearchListAdapter.SearchListClickListener() {
            @Override
            public void onItemClick(int position, SearchListBean bean) {
                
                //항목 체크에 대한 toggle 처리
                bean.setSelect(!bean.isSelect());
                mCurrSearchListBean = bean;
                mEtSearch.setText(bean.getList_title());

                BaseBean baseBean = new BaseBean();
                baseBean.setObject(bean);
                selectedCallback.callback(baseBean);

                //.
                // mRecyclerView.getAdapter().notifyDataSetChanged();
            }

        });

        mLayoutManager = new LinearLayoutManager(activity);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
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

    public void setOnSelectListener(WaterCallBack callBack){
        selectedCallback = callBack;
    }

    private void setClickListener(View v, final Command command, final WaterCallBack waterCallBack) {
        v.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                int id = v.getId();
                if(id == R.id.ok_bt2 || id == R.id.ok_bt1){

                    if(mCurrSearchListBean == null) {
                        CurvletManager.process(activity, null, "water://toast?text=회원 전화번호를 선택해주세요");
                        return;
                    }

                    BaseBean baseBean = new BaseBean();
                    baseBean.setObject(mCurrSearchListBean);
                    selectedCallback.callback(baseBean);

                }

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
}
