package kr.co.goms.app.estimate.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

import kr.co.goms.app.estimate.AppConstant;
import kr.co.goms.app.estimate.MainActivity;
import kr.co.goms.app.estimate.MyApplication;
import kr.co.goms.app.estimate.R;
import kr.co.goms.app.estimate.manager.AdIdHelper;
import kr.co.goms.app.estimate.manager.GlideHelper;
import kr.co.goms.app.estimate.manager.SendManager;
import kr.co.goms.app.estimate.model.ClientBeanTB;
import kr.co.goms.app.estimate.model.CompanyBeanTB;
import kr.co.goms.app.estimate.send_data.SendDataFactory;
import kr.co.goms.module.common.activity.CustomActivity;
import kr.co.goms.module.common.base.BaseBean;
import kr.co.goms.module.common.curvlet.CurvletManager;
import kr.co.goms.module.common.manager.FragmentMoveManager;
import kr.co.goms.module.common.observer.ObserverInterface;
import kr.co.goms.module.common.util.FileUtil;
import kr.co.goms.module.common.util.GomsLog;
import kr.co.goms.module.common.util.StringUtil;

public class ClientFormFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = ClientFormFragment.class.getSimpleName();

    public ClientFormFragment(){}

    private Toolbar mToolbar;

    private EditText mEtComName, mEtBizNum, mEtCeoName, mEtUpTae, mEtUpJong, mEtManagerName, mEtTel, mEtFax, mEtHp, mEtEmail, mEtZipCode, mEtAddress01, mEtAddress02;
    private SwitchCompat mSwitchMainYn;
    private String mCliIdx = "";
    private FORM_TYPE mFormType = FORM_TYPE.CREATE;
    private enum FORM_TYPE{
        CREATE, //생성
        MODIFY,   //수정
    }

    private ObserverInterface mDataObserver;
    public static ClientFormFragment getFragment(String cliIdx){
        ClientFormFragment fragment = new ClientFormFragment();
        Bundle bundle = new Bundle();
        bundle.putString("cliIdx", cliIdx);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        ConstraintLayout layout = (ConstraintLayout) inflater.inflate(R.layout.fragment_client_form, container, false);
        return layout;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        try {
            mCliIdx = getArguments().getString("cliIdx");
        }catch(NullPointerException e){

        }

        mToolbar = view.findViewById(R.id.toolbar);
        ((CustomActivity) getActivity()).setSupportActionBar(mToolbar);
        Objects.requireNonNull(((CustomActivity) getActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        TextView tvToolBarTitle = view.findViewById(R.id.tv_toolbar_title);
        tvToolBarTitle.setText("거래처 정보");

        initData(view, mCliIdx);
        setObserverData();
        this.setHasOptionsMenu(true);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            getActivity().onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.btn_save){
            if(checkValue()){
                goSave();
            }
        }
    }

    private void setObserverData(){
        mDataObserver = new ObserverInterface() {
            @Override
            public void callback(BaseBean baseBean) {
                GomsLog.d(TAG, "mDataObserver  CallBack()");

                if (baseBean.getStatus() == BaseBean.STATUS.SUCCESS) {
                    FragmentMoveManager.I().setManager(getActivity(), R.id.setting_nav_host_fragment).changeFragment(new ClientListFragment(), "CliList", true);
                } else {
                    GomsLog.d(TAG, "CallBack() : mDataObserver 실패!!!!");
                }
            }
        };
    }
    private boolean checkValue(){
        String comName = mEtComName.getText().toString();
        String bizNum = mEtBizNum.getText().toString();
        if(StringUtil.isEmpty(comName)){
            CurvletManager.process(getActivity(), null, "water://toast?text=제목을 넣어주세요");
            return false;
        }
        if(comName.length() <= 1){
            CurvletManager.process(getActivity(), null, "water://toast?text=회사명을 2자리 이상 넣어주세요");
            return false;
        }

        if(StringUtil.isEmpty(bizNum)){
            CurvletManager.process(getActivity(), null, "water://toast?text=사업자번호를 넣어주세요");
            return false;
        }
        if(bizNum.length() != 10){
            CurvletManager.process(getActivity(), null, "water://toast?text=사업자번호 10자리를 넣어주세요");
            return false;
        }
        /*
        if(!StringUtil.isBizNumValid(bizNum)){
            CurvletManager.process(getActivity(), null, "water://toast?text=사업자번호를 정확히 넣어주세요");
            return false;
        }
        */
        return true;
    }

    private void initData(View view, String cliIdx){

        if(StringUtil.isEmpty(cliIdx)){
            mFormType = FORM_TYPE.CREATE;
        }else{
            mFormType = FORM_TYPE.MODIFY;
        }

        mEtComName = view.findViewById(R.id.et_com_name);
        mEtBizNum= view.findViewById(R.id.et_biz_num);
        mEtCeoName = view.findViewById(R.id.et_ceo_name);
        mEtUpTae = view.findViewById(R.id.et_uptae);
        mEtUpJong = view.findViewById(R.id.et_upjong);
        mEtManagerName = view.findViewById(R.id.et_manager_name);
        mEtTel = view.findViewById(R.id.et_tel);
        mEtFax = view.findViewById(R.id.et_fax);
        mEtHp = view.findViewById(R.id.et_hp);
        mEtEmail = view.findViewById(R.id.et_email);
        mEtZipCode = view.findViewById(R.id.et_zipcode);
        mEtAddress01 = view.findViewById(R.id.et_address_01);
        mEtAddress02 = view.findViewById(R.id.et_address_02);

        mSwitchMainYn = view.findViewById(R.id.sc_main_yn);

        Button btnSave = view.findViewById(R.id.btn_save);
        btnSave.setOnClickListener(this);

        if(FORM_TYPE.MODIFY.name().equalsIgnoreCase(mFormType.name()) && StringUtil.isNotNull(cliIdx)) {
            ClientBeanTB clientBeanTB = MyApplication.getInstance().getDBHelper().getClient(cliIdx);

            mEtComName.setText(clientBeanTB.getCli_name());
            mEtBizNum.setText(clientBeanTB.getCli_biz_num());
            mEtCeoName.setText(clientBeanTB.getCli_ceo_name());
            mEtUpTae.setText(clientBeanTB.getCli_uptae());
            mEtUpJong.setText(clientBeanTB.getCli_upjong());
            mEtManagerName.setText(clientBeanTB.getCli_manager_name());
            mEtTel.setText(clientBeanTB.getCli_tel_num());
            mEtFax.setText(clientBeanTB.getCli_fax_num());
            mEtHp.setText(clientBeanTB.getCli_hp_num());
            mEtEmail.setText(clientBeanTB.getCli_email());
            mEtZipCode.setText(clientBeanTB.getCli_zipcode());
            mEtAddress01.setText(clientBeanTB.getCli_address_01());
            mEtAddress02.setText(clientBeanTB.getCli_address_02());
            mSwitchMainYn.setChecked("Y".equalsIgnoreCase(clientBeanTB.getCli_main_yn()));
            btnSave.setText("수정");
        }
    }

    private void goSave(){
        String comName = mEtComName.getText().toString();
        String bizNum = mEtBizNum.getText().toString();
        String ceoName = mEtCeoName.getText().toString();
        String upTae = mEtUpTae.getText().toString();
        String upJong  = mEtUpJong.getText().toString();
        String managerName  = mEtManagerName.getText().toString();
        String tel = mEtTel.getText().toString();
        String fax  = mEtFax.getText().toString();
        String hp  = mEtHp.getText().toString();
        String email = mEtEmail.getText().toString();
        String zipCode = mEtZipCode.getText().toString();
        String address01 = mEtAddress01.getText().toString();
        String address02 = mEtAddress02.getText().toString();
        String mainYn = mSwitchMainYn.isChecked()?"Y":"N";

        ClientBeanTB clientBeanTB = new ClientBeanTB();
        clientBeanTB.setCli_idx(mCliIdx);
        clientBeanTB.setCli_name(comName);
        clientBeanTB.setCli_biz_num(bizNum);
        clientBeanTB.setCli_ceo_name(ceoName);
        clientBeanTB.setCli_uptae(upTae);
        clientBeanTB.setCli_upjong(upJong);
        clientBeanTB.setCli_manager_name(managerName);
        clientBeanTB.setCli_tel_num(tel);
        clientBeanTB.setCli_fax_num(fax);
        clientBeanTB.setCli_hp_num(hp);
        clientBeanTB.setCli_email(email);
        clientBeanTB.setCli_zipcode(zipCode);
        clientBeanTB.setCli_address_01(address01);
        clientBeanTB.setCli_address_02(address02);
        clientBeanTB.setCli_main_yn(mainYn);

        if(FORM_TYPE.MODIFY.name().equalsIgnoreCase(mFormType.name())) {
            HashMap<String, Object> params = new HashMap<>();
            params.put("cliIdx", clientBeanTB.getCli_idx());
            params.put("cliName", clientBeanTB.getCli_name());
            params.put("cliBizNum", clientBeanTB.getCli_biz_num());
            params.put("cliCeoName", clientBeanTB.getCli_ceo_name());
            params.put("cliUpTae", clientBeanTB.getCli_uptae());
            params.put("cliUpJong", clientBeanTB.getCli_upjong());
            params.put("cliManagerName", clientBeanTB.getCli_manager_name());
            params.put("cliTelNum", clientBeanTB.getCli_tel_num());
            params.put("cliFaxNum", clientBeanTB.getCli_fax_num());
            params.put("cliHpNum", clientBeanTB.getCli_hp_num());
            params.put("cliEmail", clientBeanTB.getCli_email());
            params.put("cliZipCode", clientBeanTB.getCli_zipcode());
            params.put("cliAddress01", clientBeanTB.getCli_address_01());
            params.put("cliAddress02", clientBeanTB.getCli_address_02());
            params.put("cliMainYN", clientBeanTB.getCli_main_yn());

            SendManager.I().sendData(SendDataFactory.DATA_TYPE.CLI_UPDATE, params, mDataObserver);
        }else {
            MyApplication.getInstance().getDBHelper().insertClient(clientBeanTB);
        }
    }
}
