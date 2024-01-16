package kr.co.goms.app.estimate.fragment;

import static kr.co.goms.app.estimate.command.ItemFormBottomDialogCommand.EXT_OBJECT;

import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import kr.co.goms.app.estimate.AppConstant;
import kr.co.goms.app.estimate.MainActivity;
import kr.co.goms.app.estimate.MyApplication;
import kr.co.goms.app.estimate.R;
import kr.co.goms.app.estimate.adapter.EstimateAdapter;
import kr.co.goms.app.estimate.adapter.EstimateItemAdapter;
import kr.co.goms.app.estimate.command.ItemFormBottomDialogCommand;
import kr.co.goms.app.estimate.common.EstimatePrefs;
import kr.co.goms.app.estimate.db.EstimateDB;
import kr.co.goms.app.estimate.manager.ExcelManager;
import kr.co.goms.app.estimate.manager.GlideHelper;
import kr.co.goms.app.estimate.manager.SendManager;
import kr.co.goms.app.estimate.model.ClientBeanTB;
import kr.co.goms.app.estimate.model.CompanyBeanTB;
import kr.co.goms.app.estimate.model.EstimateBeanTB;
import kr.co.goms.app.estimate.model.ItemBeanTB;
import kr.co.goms.app.estimate.send_data.SendDataFactory;
import kr.co.goms.module.admob.AdmobPrefs;
import kr.co.goms.module.admob.fragment.ChargingStationFragment;
import kr.co.goms.module.common.activity.CustomActivity;
import kr.co.goms.module.common.base.BaseBean;
import kr.co.goms.module.common.base.WaterCallBack;
import kr.co.goms.module.common.command.BaseBottomDialogCommand;
import kr.co.goms.module.common.command.SearchListBottomDialogCommand;
import kr.co.goms.module.common.curvlet.CurvletManager;
import kr.co.goms.module.common.manager.DialogCommandFactory;
import kr.co.goms.module.common.manager.DialogManager;
import kr.co.goms.module.common.manager.FragmentMoveManager;
import kr.co.goms.module.common.model.SearchListBean;
import kr.co.goms.module.common.observer.ObserverInterface;
import kr.co.goms.module.common.util.DateUtil;
import kr.co.goms.module.common.util.FormatUtil;
import kr.co.goms.module.common.util.GomsLog;
import kr.co.goms.module.common.util.MathUtil;
import kr.co.goms.module.common.util.StringUtil;

public class EstimateFormFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = EstimateFormFragment.class.getSimpleName();

    public EstimateFormFragment(){}

    private Toolbar mToolbar;
    private TextView mTvComName, mTvCliName, mTvEstDate, mTvEstEffectiveDate, mTvEstDeliveryDate, mTvItemTotalPrice;
    private ImageView mIvComSearch, mIvCliSearch;
    private EditText mEtManagerName, mEtTel, mEtFax, mEtHp, mEtEmail, mEtRemark;

    private SwitchCompat mSwitchVat;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<ItemBeanTB> mItemList = new ArrayList<ItemBeanTB>();
    private static final int SPAN_COUNT = 1;
    private ObserverInterface mDataObserver;
    private String mEstIdx = "";
    private String mComIdx = "";
    private String mCliIdx = "";
    private String mEstToken = "";
    private String isVatYN = "N";

    private ArrayList<CompanyBeanTB> mCompanyList;
    private ArrayList<ClientBeanTB> mClientList;

    private boolean isCliSelected = false;  //거래처 선택 여부
    private String mCliSelectedName = "";   //선택된 거래처명
    private String mCliSelectedIdx = "";   //선택된 거래처키
    private String mComSelectedName = "";   //선택된 나의회사명
    private String mComSelectedIdx = "";   //선택된 나의회사키

    private CompanyBeanTB mCurrCompanyBeanTB;
    private ClientBeanTB mCurrClientBeanTB;

    private FORM_TYPE mFormType = FORM_TYPE.CREATE;
    private enum FORM_TYPE{
        CREATE, //생성
        MODIFY,   //수정
    }

    private LinearLayout mLltStamp;
    private ImageView mIvStamp;

    public static EstimateFormFragment getFragment(String estIdx){
        EstimateFormFragment fragment = new EstimateFormFragment();
        Bundle bundle = new Bundle();
        bundle.putString("estIdx", estIdx);
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
        ConstraintLayout layout = (ConstraintLayout) inflater.inflate(R.layout.fragment_estimate_form, container, false);
        return layout;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        try {
            mEstIdx = getArguments().getString("estIdx");
        }catch(NullPointerException e){

        }

        mToolbar = view.findViewById(R.id.toolbar);
        ((CustomActivity) getActivity()).setSupportActionBar(mToolbar);
        Objects.requireNonNull(((CustomActivity) getActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        TextView tvToolBarTitle = view.findViewById(R.id.tv_toolbar_title);
        tvToolBarTitle.setText("견적서 정보");

        mSwitchVat = view.findViewById(R.id.sc_vat_yn);

        mRecyclerView = view.findViewById(R.id.rv_estimate_item);
        mLayoutManager = new StaggeredGridLayoutManager(SPAN_COUNT, StaggeredGridLayoutManager.VERTICAL);
        ((StaggeredGridLayoutManager)mLayoutManager).setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        mRecyclerView.setLayoutManager(mLayoutManager);

        String prefix = MyApplication.getInstance().prefs().get(AppConstant.EST_PREFIX);
        StringBuffer sb = new StringBuffer();
        sb.append(prefix);
        sb.append(MathUtil.randomToken(10));

        mEstToken = sb.toString();
        GomsLog.d(TAG, "mEstToken : " + mEstToken);

        initData(view, mEstIdx);

        setItemDataObserver();

        checkBasicCompany();
        checkBasicClient();

        mSwitchVat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isVatYN = isChecked?"Y":"N";
                ((EstimateItemAdapter)mAdapter).changeVatData(isVatYN);
                setEstTotalPrice(isChecked);
            }
        });

        mSwitchVat.setChecked("Y".equalsIgnoreCase(isVatYN));

        this.setHasOptionsMenu(true);

    }

    private void setItemDataObserver(){
        mDataObserver = new ObserverInterface() {
            @Override
            public void callback(BaseBean baseBean) {
                GomsLog.d(TAG, "mDataObserver  CallBack()");

                if (baseBean.getStatus() == BaseBean.STATUS.SUCCESS) {

                    mItemList = (ArrayList<ItemBeanTB>) baseBean.getObject();
                    GomsLog.d(TAG, "mItemList 갯수 : " + mItemList.size());

                    //왜 화면로딩이 되지 않을까?? -> Thread 처리
                    if (Looper.myLooper() == Looper.getMainLooper()) {
                        setEsimateItemList(mItemList);
                    } else {
                        // WorkThread이면, MainThread에서 실행 되도록 변경.
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setEsimateItemList(mItemList);
                            }
                        });
                    }

                } else {
                    GomsLog.d(TAG, "CallBack() : 인트로 Category Data 실패!!!!");
                }
            }
        };
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
        if(id == R.id.btn_save) {
            if (checkValue()) {
                goSave(mComIdx, mCliIdx);
            }
        }else if(id == R.id.btn_excel) {
            setDiaBottomDialog(mEstIdx);
        }else if(id == R.id.iv_com_search){
            goComSearch(mCompanyList);
        }else if(id == R.id.iv_cli_search){

            int iClientTotal = 0;
            try{
                iClientTotal = mClientList.size();
            }catch (Exception e){

            }
            if(iClientTotal == 0) {
                goClientInsertDialog();
            }else if(iClientTotal == 1){
                ClientBeanTB clientBeanTB = mClientList.get(0);
                mCliSelectedIdx = clientBeanTB.getCli_idx();
                mTvCliName.setText(clientBeanTB.getCli_name());
                isCliSelected = true;
                mCliSelectedName = clientBeanTB.getCli_name();
            }else {
                goCliSearch(mClientList);
            }
        }else if (id == R.id.tv_est_date) {
            DialogManager.I().showDialogDatePicker(getActivity(), new WaterCallBack() {
                @Override
                public void callback(BaseBean baseBean) {
                    String estDate = (String) ((Bundle) baseBean.getObject()).get(DialogManager.EXT_DATE);
                    estDate = DateUtil.displayDateYMD8(estDate);
                    mTvEstDate.setText(estDate);
                }
            });
        }else if (id == R.id.tv_est_effective_date) {
            DialogManager.I().showDialogDatePicker(getActivity(), new WaterCallBack() {
                @Override
                public void callback(BaseBean baseBean) {
                    String estDate = (String) ((Bundle) baseBean.getObject()).get(DialogManager.EXT_DATE);
                    estDate = DateUtil.displayDateYMD8(estDate);
                    mTvEstEffectiveDate.setText(estDate);
                }
            });
        }else if (id == R.id.tv_est_delivery_date) {
            DialogManager.I().showDialogDatePicker(getActivity(), new WaterCallBack() {
                @Override
                public void callback(BaseBean baseBean) {
                    String estDate = (String) ((Bundle) baseBean.getObject()).get(DialogManager.EXT_DATE);
                    estDate = DateUtil.displayDateYMD8(estDate);
                    mTvEstDeliveryDate.setText(estDate);
                }
            });
        }else if(id == R.id.btn_est_item_add){
            addEstItem(mEstToken);
        }
    }

    private void goComSearch(ArrayList<CompanyBeanTB> companyList){

        ArrayList<SearchListBean> searchList = new ArrayList<>();

        for(CompanyBeanTB clientBeanTB : companyList){
            SearchListBean searchListBean = new SearchListBean();
            searchListBean.setList_idx(clientBeanTB.getCom_idx());
            searchListBean.setList_title(clientBeanTB.getCom_name());
            searchListBean.setSelect(false);
            searchList.add(searchListBean);
        }

        DialogManager.I().setTitle("나의 회사 선택")
                .setMessage("나의 회사를 선택해 주세요")
                .setShowTitle(true)
                .setShowMessage(true)
                .setNegativeBtnName("")
                .setPositiveBtnName("")
                .setCancelable(true)
                .setCancelTouchOutSide(true)
                .setDataList(searchList)
                .setCommand(DialogCommandFactory.I().createDialogCommand(getActivity(), DialogCommandFactory.DIALOG_TYPE.search_list.name(), new WaterCallBack() {
                    @Override
                    public void callback(BaseBean baseBean) {
                        String btnType = ((Bundle)baseBean.getObject()).getString(BaseBottomDialogCommand.EXT_BTN_TYPE);
                        if(BaseBottomDialogCommand.BTN_TYPE.LEFT.name().equalsIgnoreCase(btnType)){
                            Log.d(TAG, " 클릭 >>>> 왼쪽");
                        }else if(BaseBottomDialogCommand.BTN_TYPE.RIGHT.name().equalsIgnoreCase(btnType)){
                            Log.d(TAG, " 클릭 >>>> 오른쪽");
                            /*
                            String inputData = ((Bundle)baseData.getObject()).getString(InputFormBottomDialogCommand.EXT_INPUT);
                            if(StringUtil.isNotNull(inputData)){
                                sendNewGroupNeme(inputData);
                            }
                             */
                        }else if(BaseBottomDialogCommand.BTN_TYPE.SELECT.name().equalsIgnoreCase(btnType)){
                            SearchListBean searchListBean = ((Bundle)baseBean.getObject()).getParcelable(SearchListBottomDialogCommand.EXT_SEARCH_BEAN);
                            mComSelectedIdx = searchListBean.getList_idx();
                            mTvComName.setText(searchListBean.getList_title());
                            mComSelectedName = searchListBean.getList_title();
                            Log.d(TAG, " 클릭 >>>> 선택 : " + mComSelectedName + ", " + searchListBean.getList_title());

                        }
                    }
                }))
                .showDialog(getActivity());
    }
    private void goCliSearch(ArrayList<ClientBeanTB> clientList){

        ArrayList<SearchListBean> searchList = new ArrayList<>();

        for(ClientBeanTB clientBeanTB : clientList){
            SearchListBean searchListBean = new SearchListBean();
            searchListBean.setList_idx(clientBeanTB.getCli_idx());
            searchListBean.setList_title(clientBeanTB.getCli_name());
            searchListBean.setSelect(false);
            searchList.add(searchListBean);
        }

        DialogManager.I().setTitle("거래처 선택")
                .setMessage("거래처를 선택해 주세요")
                .setShowTitle(true)
                .setShowMessage(true)
                .setNegativeBtnName("")
                .setPositiveBtnName("")
                .setCancelable(true)
                .setCancelTouchOutSide(true)
                .setDataList(searchList)
                .setCommand(DialogCommandFactory.I().createDialogCommand(getActivity(), DialogCommandFactory.DIALOG_TYPE.search_list.name(), new WaterCallBack() {
                    @Override
                    public void callback(BaseBean baseBean) {
                        String btnType = ((Bundle)baseBean.getObject()).getString(BaseBottomDialogCommand.EXT_BTN_TYPE);
                        if(BaseBottomDialogCommand.BTN_TYPE.LEFT.name().equalsIgnoreCase(btnType)){
                            Log.d(TAG, " 클릭 >>>> 왼쪽");
                        }else if(BaseBottomDialogCommand.BTN_TYPE.RIGHT.name().equalsIgnoreCase(btnType)){
                            Log.d(TAG, " 클릭 >>>> 오른쪽");
                            /*
                            String inputData = ((Bundle)baseData.getObject()).getString(InputFormBottomDialogCommand.EXT_INPUT);
                            if(StringUtil.isNotNull(inputData)){
                                sendNewGroupNeme(inputData);
                            }
                             */
                        }else if(BaseBottomDialogCommand.BTN_TYPE.SELECT.name().equalsIgnoreCase(btnType)){
                            SearchListBean searchListBean = ((Bundle)baseBean.getObject()).getParcelable(SearchListBottomDialogCommand.EXT_SEARCH_BEAN);
                            mCliSelectedIdx = searchListBean.getList_idx();
                            mTvCliName.setText(searchListBean.getList_title());
                            isCliSelected = true;
                            mCliSelectedName = searchListBean.getList_title();
                            Log.d(TAG, " 클릭 >>>> 선택 : " + mCliSelectedName + ", " + searchListBean.getList_title());

                        }
                    }
                }))
                .showDialog(getActivity());
    }

    private void addEstItem(String token){
        DialogManager.I().setTag("itemInsert")
                .setTitle("상품")
                .setMessage("견적 상품을 입력해 주세요")
                .setShowTitle(true)
                .setShowMessage(true)
                .setNegativeBtnName("")
                .setPositiveBtnName(getActivity().getString(kr.co.goms.module.common.R.string.confirm))
                .setCancelable(true)
                .setCancelTouchOutSide(true)
                .setShowItem(true)
                .setCommand(ItemFormBottomDialogCommand.getInstance().setBaseDialogCommand(getActivity(),  new WaterCallBack() {
                            @Override
                            public void callback(BaseBean baseData) {
                                String btnType = ((Bundle) baseData.getObject()).getString(BaseBottomDialogCommand.EXT_BTN_TYPE);
                                if (BaseBottomDialogCommand.BTN_TYPE.LEFT.name().equalsIgnoreCase(btnType)) {
                                    Log.d(TAG, " 클릭 >>>> 왼쪽");
                                } else if (BaseBottomDialogCommand.BTN_TYPE.RIGHT.name().equalsIgnoreCase(btnType)) {

                                    Bundle bundle = (Bundle)baseData.getObject();

                                    ItemBeanTB itemBeanTB = (ItemBeanTB) bundle.getParcelable(EXT_OBJECT);

                                    HashMap<String, Object> params = new HashMap<>();
                                    params.put("itemToken", token);
                                    params.put("itemName", itemBeanTB.getItem_name());
                                    params.put("itemStd", itemBeanTB.getItem_std());
                                    params.put("itemUnit", itemBeanTB.getItem_unit());
                                    params.put("itemQuantity", itemBeanTB.getItem_quantity());
                                    params.put("itemUnitPrice", itemBeanTB.getItem_unit_price());
                                    params.put("itemRemark", itemBeanTB.getItem_remark());

                                    //아이템 총합계
                                    String itemPrice = null;
                                    String taxPrice = null;
                                    String totalPrice = null;
                                    int iQuantity = 0;
                                    int iUnitPrice = 0;
                                    int iPrice = 0;
                                    int iTaxPrice = 0;
                                    int iTotalPrice = 0;
                                    try{
                                        iQuantity = StringUtil.stringToInt(itemBeanTB.getItem_quantity());
                                        iUnitPrice = StringUtil.stringToInt(itemBeanTB.getItem_unit_price());
                                        iPrice = iQuantity * iUnitPrice;
                                        itemPrice = StringUtil.intToString(iPrice);
                                        iTaxPrice = (int)(iPrice / 10);
                                        taxPrice = StringUtil.intToString(iTaxPrice);
                                        iTotalPrice = iPrice + iTaxPrice;
                                        totalPrice = StringUtil.intToString(iTotalPrice);
                                    }catch(Exception e){

                                    }

                                    params.put("itemPrice", itemPrice);
                                    params.put("itemTaxPrice", taxPrice);
                                    params.put("itemTotalPrice", totalPrice);

                                    SendManager.I().sendData(SendDataFactory.DATA_TYPE.EST_TEMP_ITEM_INSERT, params, new ObserverInterface() {
                                        @Override
                                        public void callback(BaseBean baseBean) {
                                            GomsLog.d(TAG, "mDataObserver  CallBack()");
                                            if (baseBean.getStatus() == BaseBean.STATUS.SUCCESS) {
                                                ArrayList<ItemBeanTB> tempItemList = (ArrayList<ItemBeanTB>) baseBean.getObject();
                                                GomsLog.d(TAG, "tempItemList 갯수 : " + tempItemList.size());

                                                mItemList.clear();
                                                mItemList.addAll(tempItemList);

                                                //왜 화면로딩이 되지 않을까?? -> Thread 처리
                                                if (Looper.myLooper() == Looper.getMainLooper()) {
                                                    setEsimateItemList(mItemList);
                                                } else {
                                                    // WorkThread이면, MainThread에서 실행 되도록 변경.
                                                    getActivity().runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            setEsimateItemList(mItemList);
                                                        }
                                                    });
                                                }
                                            } else {
                                                GomsLog.d(TAG, "CallBack()");
                                            }
                                        }
                                    });
                                }
                            }
                        })
                ).showDialog(getActivity());
    }

    private boolean checkValue(){
        String cliName = mTvCliName.getText().toString();
        if(StringUtil.isEmpty(cliName)){
            CurvletManager.process(getActivity(), null, "water://toast?text=거래처를 선택해 주세요");
            return false;
        }

        if(cliName.length() <= 1){
            CurvletManager.process(getActivity(), null, "water://toast?text=거래처명을 2자리 이상 넣어주세요");
            return false;
        }

        /*
        if(!StringUtil.isBizNumValid(bizNum)){
            CurvletManager.process(getActivity(), null, "water://toast?text=사업자번호를 정확히 넣어주세요");
            return false;
        }
        */

        int iItemTotal = 0;
        try {
            iItemTotal = mAdapter.getItemCount();
        }catch(Exception e){

        }
        if(iItemTotal <= 0){
            CurvletManager.process(getActivity(), null, "water://toast?text=상품을 넣어주세요");
            return false;
        }
        return true;
    }

    /**
     * 초기데이타
     * @param view
     * @param estIdx
     */
    private void initData(View view, String estIdx){
        Log.d(TAG, "initData()");

        if(StringUtil.isEmpty(estIdx)){
            mFormType = FORM_TYPE.CREATE;
        }else{
            mFormType = FORM_TYPE.MODIFY;
        }

        mIvComSearch = view.findViewById(R.id.iv_com_search);               //나의회사 서치
        mTvComName = view.findViewById(R.id.tv_com_name);
        mIvCliSearch = view.findViewById(R.id.iv_cli_search);               //거래처서치
        mTvCliName = view.findViewById(R.id.tv_cli_name);
        mTvEstDate = view.findViewById(R.id.tv_est_date);                   //견적일시
        mTvEstEffectiveDate = view.findViewById(R.id.tv_est_effective_date);//유효일시
        mTvEstDeliveryDate = view.findViewById(R.id.tv_est_delivery_date);  //납기일시
        mEtTel = view.findViewById(R.id.et_tel);
        mEtFax = view.findViewById(R.id.et_fax);
        mEtManagerName = view.findViewById(R.id.et_manager_name);
        mEtHp = view.findViewById(R.id.et_hp);
        mEtEmail = view.findViewById(R.id.et_email);
        mEtRemark = view.findViewById(R.id.et_remart);
        mTvItemTotalPrice = view.findViewById(R.id.tv_item_total_price);

        mLltStamp = view.findViewById(R.id.llt_stamp);
        mIvStamp = view.findViewById(R.id.iv_stamp);

        mIvComSearch.setOnClickListener(this);
        mIvCliSearch.setOnClickListener(this);
        mTvEstDate.setOnClickListener(this);
        mTvEstEffectiveDate.setOnClickListener(this);
        mTvEstDeliveryDate.setOnClickListener(this);

        Button btnSave = view.findViewById(R.id.btn_save);
        Button btnExcel = view.findViewById(R.id.btn_excel);
        btnSave.setOnClickListener(this);
        btnExcel.setOnClickListener(this);

        view.findViewById(R.id.btn_est_item_add).setOnClickListener(this);

        HashMap<String, Object> params = new HashMap<>();
        SendManager.I().sendData(SendDataFactory.DATA_TYPE.CLI_LIST, null, new ObserverInterface() {
            @Override
            public void callback(BaseBean baseBean) {
                if (baseBean.getStatus() == BaseBean.STATUS.SUCCESS) {
                    mClientList = (ArrayList<ClientBeanTB>) baseBean.getObject();
                    GomsLog.d(TAG, "clientList 갯수 : " + mClientList.size());
                } else {
                    GomsLog.d(TAG, "CallBack() : mClientList 실패!!!!");
                }
            }
        });

        SendManager.I().sendData(SendDataFactory.DATA_TYPE.COM_LIST, null, new ObserverInterface() {
            @Override
            public void callback(BaseBean baseBean) {
                if (baseBean.getStatus() == BaseBean.STATUS.SUCCESS) {
                    mCompanyList = (ArrayList<CompanyBeanTB>) baseBean.getObject();
                    GomsLog.d(TAG, "mCompanyList 갯수 : " + mCompanyList.size());
                } else {
                    GomsLog.d(TAG, "CallBack() : mCompanyList 실패!!!!");
                }
            }
        });

        if(FORM_TYPE.MODIFY.name().equalsIgnoreCase(mFormType.name())) {

            EstimateBeanTB estimateBeanTB = MyApplication.getInstance().getDBHelper().getEstimateData(mEstIdx);
            mTvCliName.setText(estimateBeanTB.getEst_cli_name());
            mTvComName.setText(estimateBeanTB.getEst_com_name());
            mTvEstDate.setText(estimateBeanTB.getEst_date());
            mTvEstEffectiveDate.setText(estimateBeanTB.getEst_effective_date());
            mTvEstDeliveryDate.setText(estimateBeanTB.getEst_delivery_date());

            mEtManagerName.setText(estimateBeanTB.getEst_com_manager_name());
            mEtTel.setText(estimateBeanTB.getEst_com_tel());
            mEtFax.setText(estimateBeanTB.getEst_com_fax());
            mEtHp.setText(estimateBeanTB.getEst_com_hp());
            mEtEmail.setText(estimateBeanTB.getEst_com_email());

            mEtRemark.setText(estimateBeanTB.getEst_remark());
            mTvItemTotalPrice.setText(estimateBeanTB.getEst_total_price());

            isVatYN = estimateBeanTB.getEst_tax_type();
            mSwitchVat.setChecked("Y".equalsIgnoreCase(estimateBeanTB.getEst_tax_type()));
            btnSave.setText("수정");
            getEstimateItemList(estIdx);

        }else{
            //오늘날짜로 기입
            String currDate = DateUtil.getCurrentDate();
            mTvEstDate.setText(currDate);

            btnExcel.setVisibility(View.GONE);
        }

    }

    /**
     * 견적서 아이템 리스트 보기
     * @param tempItemList
     */
    @MainThread
    private void setEsimateItemList(ArrayList<ItemBeanTB> tempItemList){
        Log.d(TAG, "setEsimateItemList()");

        mAdapter = new EstimateItemAdapter(getActivity(), tempItemList, new EstimateItemAdapter.ItemClickListener() {
            @Override
            public void onItemClick(int position, ItemBeanTB itemBeanTB) {
                Log.d(TAG, "itemBeanTB 클릭 >>>> " + itemBeanTB.getItem_name());
                //((MainActivity)getActivity()).changeFragment(FieldBasicListFragment.getFragment(groupBeanS.getRes_mh_group_idx(), groupBeanS.getRes_mh_group_name()), "fieldBasicList");
            }

            @Override
            public void onItemLongClick(int position, ItemBeanTB itemBeanTB) {
                Log.d(TAG, "Group 롱클릭 >>>> " + itemBeanTB.getItem_name());
                goItemDeleteDialog(itemBeanTB);
            }
        });

        ((EstimateItemAdapter)mAdapter).changeVatData(isVatYN);

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setVisibility(View.VISIBLE);

        //LayoutManager를 재세팅을 해야지만, 상단으로 다시 정렬됨
        mLayoutManager = new StaggeredGridLayoutManager(SPAN_COUNT, StaggeredGridLayoutManager.VERTICAL);
        ((StaggeredGridLayoutManager)mLayoutManager).setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        mRecyclerView.setLayoutManager(mLayoutManager);

        setEstTotalPrice("Y".equalsIgnoreCase(isVatYN));
    }


    /**
     * 아이템 삭제하기 하단팝업
     * @param itemBeanTB
     */
    private void goItemDeleteDialog(ItemBeanTB itemBeanTB){
        DialogManager.I().setTag("itemDelete")
                .setTitle("'"+ itemBeanTB.getItem_name()+"' 삭제")
                .setMessage("해당 데이타를 삭제하시겠습니까?")
                .setShowTitle(true)
                .setShowMessage(true)
                .setNegativeBtnName("취소")
                .setPositiveBtnName(getActivity().getString(kr.co.goms.module.common.R.string.confirm))
                .setCancelable(true)
                .setCancelTouchOutSide(true)
                .setCommand(DialogCommandFactory.I().createDialogCommand(getActivity(), DialogCommandFactory.DIALOG_TYPE.bottom_basic.name(), new WaterCallBack() {
                    @Override
                    public void callback(BaseBean baseData) {
                        String btnType = ((Bundle)baseData.getObject()).getString(BaseBottomDialogCommand.EXT_BTN_TYPE);
                        if(BaseBottomDialogCommand.BTN_TYPE.LEFT.name().equalsIgnoreCase(btnType)){
                            Log.d(TAG, " 클릭 >>>> 왼쪽");
                        }else if(BaseBottomDialogCommand.BTN_TYPE.RIGHT.name().equalsIgnoreCase(btnType)){
                            Log.d(TAG, " 클릭 >>>> 삭제하기 mbIdx:" + MyApplication.getInstance().prefs().get(EstimatePrefs.AD_ID) + ",itemIdx:" + itemBeanTB.getItem_idx());

                            if(FORM_TYPE.MODIFY.name().equalsIgnoreCase(mFormType.name())) {
                                sendItemDelete(mEstIdx, itemBeanTB.getItem_idx());
                            }else {
                                sendTempItemDelete(itemBeanTB.getItem_idx(), itemBeanTB.getItem_token());
                            }
                        }
                    }
                }))
                .showDialog(getActivity());
    }

    /**
     * 견적서 > 임시 상품 삭제하기
     * @param itemIdx
     */
    private void sendTempItemDelete(String itemIdx, String itemToken){
        HashMap<String, Object> params = new HashMap<>();
        params.put("itemIdx", itemIdx);
        params.put("itemToken", itemToken);
        SendManager.I().sendData(SendDataFactory.DATA_TYPE.EST_TEMP_ITEM_DELETE, params, mDataObserver);
    }

    /**
     * 견적서 > 상품 삭제하기
     * @param estIdx
     * @param estItemIdx
     */
    private void sendItemDelete(String estIdx, String estItemIdx){
        HashMap<String, Object> params = new HashMap<>();
        params.put("estIdx", estIdx);
        params.put("estItemIdx", estItemIdx);
        SendManager.I().sendData(SendDataFactory.DATA_TYPE.EST_ITEM_DELETE, params, mDataObserver);
    }

    /**
     * 나의 회사정보 여부 확인 > 없으면 회사정보 Insert로 보냄
     */
    private void checkBasicCompany(){
        ObserverInterface mCompanyObserver = new ObserverInterface() {
            @Override
            public void callback(BaseBean baseBean) {
                GomsLog.d(TAG, "mDataObserver  CallBack()");

                if (baseBean.getStatus() == BaseBean.STATUS.SUCCESS) {

                    ArrayList<CompanyBeanTB> companyList = (ArrayList<CompanyBeanTB>) baseBean.getObject();
                    GomsLog.d(TAG, "mCompanyList 갯수 : " + companyList.size());

                    int iCompanyTotal = 0;
                    try{
                        iCompanyTotal = companyList.size();
                    }catch (Exception e){

                    }

                    if(iCompanyTotal == 0){
                        goCompanyInsertDialog();
                    }else if(iCompanyTotal == 1){
                        mCurrCompanyBeanTB = companyList.get(0);
                    }else{
                        for(CompanyBeanTB companyBeanTB : companyList){
                            if("Y".equalsIgnoreCase(companyBeanTB.getCom_main_yn())){
                                mCurrCompanyBeanTB = companyBeanTB;
                            }
                        }
                        //메인회사가 없음 > 최근 등록된 나의회사를 선정함.
                        if(mCurrCompanyBeanTB == null){
                            mCurrCompanyBeanTB = companyList.get(0);
                        }
                        mIvComSearch.setVisibility(View.VISIBLE);  //1개 이상이면 검색아이콘 보이게 처리
                    }

                    if(mCurrCompanyBeanTB != null){
                        mTvComName.setText(mCurrCompanyBeanTB.getCom_name());
                        mEtManagerName.setText(mCurrCompanyBeanTB.getCom_manager_name());
                        mEtTel.setText(mCurrCompanyBeanTB.getCom_tel_num());
                        mEtFax.setText(mCurrCompanyBeanTB.getCom_fax_num());
                        mEtHp.setText(mCurrCompanyBeanTB.getCom_hp_num());
                        mEtEmail.setText(mCurrCompanyBeanTB.getCom_email());
                        mComIdx = mCurrCompanyBeanTB.getCom_idx();

                        if(!StringUtil.isEmpty(mCurrCompanyBeanTB.getCom_stamp_path())){
                            // 저장하면 /external/images/media/26563 >> 앞에 content://media 추가
                            Uri mPhotoStampUri = Uri.parse("content://media" + mCurrCompanyBeanTB.getCom_stamp_path());
                            GlideHelper.I().setImageView(requireActivity(), mPhotoStampUri, mIvStamp);
                            mLltStamp.setVisibility(View.VISIBLE);
                        }else{
                            mLltStamp.setVisibility(View.GONE);
                        }

                    }

                } else {
                    GomsLog.d(TAG, "CallBack() : Data 실패!!!!");
                    goCompanyInsertDialog();
                }
            }
        };
        HashMap<String, Object> params = new HashMap<>();
        SendManager.I().sendData(SendDataFactory.DATA_TYPE.COM_LIST, params, mCompanyObserver);
    }


    /**
     * 거래처 여부 확인 > 없으면 거래처 생성 아이콘을 변경
     */
    private void checkBasicClient(){
        ObserverInterface clientObserver = new ObserverInterface() {
            @Override
            public void callback(BaseBean baseBean) {
                GomsLog.d(TAG, "mDataObserver  CallBack()");

                if (baseBean.getStatus() == BaseBean.STATUS.SUCCESS) {

                    ArrayList<ClientBeanTB> clientList = (ArrayList<ClientBeanTB>) baseBean.getObject();
                    GomsLog.d(TAG, "clientList 갯수 : " + clientList.size());

                    int iClientTotal = 0;
                    try{
                        iClientTotal = clientList.size();
                    }catch (Exception e){

                    }

                    if(iClientTotal == 0){
                        mIvCliSearch.setBackground(getActivity().getResources().getDrawable(R.drawable.baseline_add_black_24, null));
                        mIvCliSearch.setVisibility(View.VISIBLE);  //추가아이콘 보이게 처리
                    }else if(iClientTotal == 1){
                        mCurrClientBeanTB = clientList.get(0);
                    }else{
                        for(ClientBeanTB clientBeanTB : clientList){
                            if("Y".equalsIgnoreCase(clientBeanTB.getCli_main_yn())){
                                mCurrClientBeanTB = clientBeanTB;
                            }
                        }
                        //메인회사가 없음 > 최근 등록된 나의회사를 선정함.
                        if(mCurrClientBeanTB == null){
                            mCurrClientBeanTB = clientList.get(0);
                        }
                        mIvCliSearch.setVisibility(View.VISIBLE);  //1개 이상이면 검색아이콘 보이게 처리
                    }

                    if(mCurrClientBeanTB != null){
                        mTvCliName.setText(mCurrClientBeanTB.getCli_name());
                        mCliIdx = mCurrClientBeanTB.getCli_idx();
                    }

                } else {
                    GomsLog.d(TAG, "CallBack() : Data 실패!!!!");
                }
            }
        };
        HashMap<String, Object> params = new HashMap<>();
        SendManager.I().sendData(SendDataFactory.DATA_TYPE.CLI_LIST, params, clientObserver);
    }

    /**
     * 나의 회사정보가 없음. 등록하러 가세요
     */
    private void goCompanyInsertDialog(){
        DialogManager.I().setTag("companyInsert")
                .setTitle("회사정보 없음")
                .setMessage("나의 회사정보 입력이 필요합니다.")
                .setShowTitle(true)
                .setShowMessage(true)
                .setNegativeBtnName("")
                .setPositiveBtnName(getActivity().getString(kr.co.goms.module.common.R.string.confirm))
                .setCancelable(false)
                .setCancelTouchOutSide(false)
                .setShowCloseBtn(false)
                .setCommand(DialogCommandFactory.I().createDialogCommand(getActivity(), DialogCommandFactory.DIALOG_TYPE.bottom_basic.name(), new WaterCallBack() {
                    @Override
                    public void callback(BaseBean baseData) {
                        String btnType = ((Bundle)baseData.getObject()).getString(BaseBottomDialogCommand.EXT_BTN_TYPE);
                        if(BaseBottomDialogCommand.BTN_TYPE.LEFT.name().equalsIgnoreCase(btnType)){
                            Log.d(TAG, " 클릭 >>>> 왼쪽");
                        }else if(BaseBottomDialogCommand.BTN_TYPE.RIGHT.name().equalsIgnoreCase(btnType)){
                            //((SettingActivity)getActivity()).changeFragment(new CompanyFormFragment(), "ComForm", true);
                            FragmentMoveManager.I().setManager(getActivity(), R.id.nav_host_fragment).changeFragment(new CompanyFormFragment(), AppConstant.FRAGMENT_TAG.COMPANY_FORM.name(), true);
                        }
                    }
                }))
                .showDialog(getActivity());
    }

    /**
     * 거래처 없음. 거래처 등록하러 가세요
     */
    private void goClientInsertDialog(){
        DialogManager.I().setTag("companyInsert")
                .setTitle("거래처 없음")
                .setMessage("거래처 등록이 필요합니다.")
                .setShowTitle(true)
                .setShowMessage(true)
                .setNegativeBtnName("")
                .setPositiveBtnName(getActivity().getString(kr.co.goms.module.common.R.string.confirm))
                .setCancelable(true)
                .setCancelTouchOutSide(true)
                .setShowCloseBtn(true)
                .setCommand(DialogCommandFactory.I().createDialogCommand(getActivity(), DialogCommandFactory.DIALOG_TYPE.bottom_basic.name(), new WaterCallBack() {
                    @Override
                    public void callback(BaseBean baseData) {
                        String btnType = ((Bundle)baseData.getObject()).getString(BaseBottomDialogCommand.EXT_BTN_TYPE);
                        if(BaseBottomDialogCommand.BTN_TYPE.LEFT.name().equalsIgnoreCase(btnType)){
                            Log.d(TAG, " 클릭 >>>> 왼쪽");
                        }else if(BaseBottomDialogCommand.BTN_TYPE.RIGHT.name().equalsIgnoreCase(btnType)){
                            //((SettingActivity)getActivity()).changeFragment(new CompanyFormFragment(), "ComForm", true);
                            FragmentMoveManager.I().setManager(getActivity(), R.id.nav_host_fragment).changeFragment(new ClientFormFragment(), AppConstant.FRAGMENT_TAG.CLIENT_FORM.name(), false);
                        }
                    }
                }))
                .showDialog(getActivity());
    }

    /**
     * 저장하기
     * @param comIdx
     * @param cliIdx
     */
    private void goSave(String comIdx, String cliIdx){
        String comName = mTvComName.getText().toString();                   //회사명
        String cliName = mTvCliName.getText().toString();                   //거래처명
        String estDate = mTvEstDate.getText().toString();                   //견적일시
        String estEffectiveDate = mTvEstEffectiveDate.getText().toString(); //유효일시
        String estDeliveryDate = mTvEstDeliveryDate.getText().toString();   //납기일시

        String managerName = mEtManagerName.getText().toString();           //담당자명
        String tel = mEtTel.getText().toString();                           //연락처
        String fax = mEtFax.getText().toString();                           //팩스번호
        String hp = mEtHp.getText().toString();                             //전화번호
        String email = mEtEmail.getText().toString();                       //이메일
        String remark = mEtRemark.getText().toString();                     //비고
        String totalPrice = mTvItemTotalPrice.getText().toString();         //총금액
        String taxType = mSwitchVat.isChecked() ? "Y" : "N";                //부가세여부

        mCliSelectedName = "";
        isCliSelected = false;

        StringBuffer sb = new StringBuffer();
        String prefix = MyApplication.getInstance().prefs().get(AppConstant.EST_PREFIX, "AB");
        sb.append(prefix);  //AB
        sb.append("_");     //AB_
        sb.append(DateUtil.getCurrentDate());   //AB_20231224
        sb.append("_");                         //AB_20231224_
        sb.append(MyApplication.getInstance().getDBHelper().getEstimateNum());  //AB_20231224_0001

        String estNUm = sb.toString();

        EstimateBeanTB estimateBeanTB = new EstimateBeanTB();

        if(FORM_TYPE.MODIFY.name().equalsIgnoreCase(mFormType.name())) {
            estimateBeanTB.setEst_idx(mEstIdx);
        }

        estimateBeanTB.setCom_idx(comIdx);
        estimateBeanTB.setCli_idx(cliIdx);
        estimateBeanTB.setEst_com_name(comName);
        estimateBeanTB.setEst_cli_name(cliName);
        estimateBeanTB.setEst_num(estNUm);
        estimateBeanTB.setEst_date(estDate);
        estimateBeanTB.setEst_effective_date(estEffectiveDate);
        estimateBeanTB.setEst_delivery_date(estDeliveryDate);

        estimateBeanTB.setEst_com_manager_name(managerName);
        estimateBeanTB.setEst_com_tel(tel);
        estimateBeanTB.setEst_com_fax(fax);
        estimateBeanTB.setEst_com_hp(hp);
        estimateBeanTB.setEst_com_email(email);
        estimateBeanTB.setEst_tax_type(taxType);
        estimateBeanTB.setEst_total_price(FormatUtil.removeComma(totalPrice));
        estimateBeanTB.setEst_remark(remark);

        //별도로 가져와서 공급자 회사 정보 넣기
        CompanyBeanTB companyBeanTB = MyApplication.getInstance().getDBHelper().getCompany(comIdx);

        estimateBeanTB.setEst_com_ceo_name(companyBeanTB.getCom_ceo_name());
        estimateBeanTB.setEst_com_biz_num(companyBeanTB.getCom_biz_num());
        estimateBeanTB.setEst_com_uptae(companyBeanTB.getCom_uptae());
        estimateBeanTB.setEst_com_upjong(companyBeanTB.getCom_upjong());
        estimateBeanTB.setEst_com_zipcode(companyBeanTB.getCom_zipcode());
        estimateBeanTB.setEst_com_address_01(companyBeanTB.getCom_address_01());
        estimateBeanTB.setEst_com_address_02(companyBeanTB.getCom_address_02());

        ArrayList<ItemBeanTB> itemList = ((EstimateItemAdapter)mAdapter).getData();

        HashMap<String, Object> params = new HashMap<>();
        params.put("estimateBeanTB", estimateBeanTB);
        params.put("estimateItemList", itemList);

        if(FORM_TYPE.MODIFY.name().equalsIgnoreCase(mFormType.name())) {

            SendManager.I().sendData(SendDataFactory.DATA_TYPE.EST_UPDATE, params, new ObserverInterface() {
                @Override
                public void callback(BaseBean baseBean) {
                    GomsLog.d(TAG, "mDataObserver  CallBack()");

                    if (baseBean.getStatus() == BaseBean.STATUS.SUCCESS) {
                        mItemList = (ArrayList<ItemBeanTB>) baseBean.getObject();
                        GomsLog.d(TAG, "mItemList 갯수 : " + mItemList.size());

                        //왜 화면로딩이 되지 않을까?? -> Thread 처리
                        if (Looper.myLooper() == Looper.getMainLooper()) {
                            //setItemList(mItemList);
                        } else {
                            // WorkThread이면, MainThread에서 실행 되도록 변경.
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //setItemList(mItemList);
                                }
                            });
                        }
                        //임시 아이템 테이블 데이타 모두 삭제처리
                        MyApplication.getInstance().mDBHelper.removeTableData(EstimateDB.TempItemTable.TEMP_ITEM_TABLE);
                    } else {
                        GomsLog.d(TAG, "CallBack()");
                    }
                }
            });
        }else {
            SendManager.I().sendData(SendDataFactory.DATA_TYPE.EST_INSERT, params, new ObserverInterface() {
                @Override
                public void callback(BaseBean baseBean) {
                    GomsLog.d(TAG, "mDataObserver  CallBack()");

                    if (baseBean.getStatus() == BaseBean.STATUS.SUCCESS) {
                        mItemList = (ArrayList<ItemBeanTB>) baseBean.getObject();
                        GomsLog.d(TAG, "mItemList 갯수 : " + mItemList.size());

                        //왜 화면로딩이 되지 않을까?? -> Thread 처리
                        if (Looper.myLooper() == Looper.getMainLooper()) {
                            //setItemList(mItemList);
                            FragmentMoveManager.I().setManager(Objects.requireNonNull(getActivity()), R.id.nav_host_fragment).changeFragment(new EstimateListFragment(), AppConstant.FRAGMENT_TAG.ESTIMATE_LIST.name(), true);
                        } else {
                            // WorkThread이면, MainThread에서 실행 되도록 변경.
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    FragmentMoveManager.I().setManager(Objects.requireNonNull(getActivity()), R.id.nav_host_fragment).changeFragment(new EstimateListFragment(), AppConstant.FRAGMENT_TAG.ESTIMATE_LIST.name(), true);
                                }
                            });
                        }
                        //임시 아이템 테이블 데이타 모두 삭제처리
                        MyApplication.getInstance().mDBHelper.removeTableData(EstimateDB.TempItemTable.TEMP_ITEM_TABLE);
                    } else {
                        GomsLog.d(TAG, "CallBack()");
                    }
                }
            });
        }
    }

    /**
     * 견적서 > 해당 견적서 아이템 리스트 가져오기
     * @param estIdx
     */
    private void getEstimateItemList(String estIdx){
        HashMap<String, Object> params = new HashMap<>();
        params.put("estIdx", estIdx);
        SendManager.I().sendData(SendDataFactory.DATA_TYPE.EST_ITEM_LIST, params, new ObserverInterface() {
            @Override
            public void callback(BaseBean baseBean) {
                if (baseBean.getStatus() == BaseBean.STATUS.SUCCESS) {
                    mItemList = (ArrayList<ItemBeanTB>) baseBean.getObject();
                    GomsLog.d(TAG, "mItemList 갯수 : " + mItemList.size());

                    //왜 화면로딩이 되지 않을까?? -> Thread 처리
                    if (Looper.myLooper() == Looper.getMainLooper()) {
                        setEsimateItemList(mItemList);
                    } else {
                        // WorkThread이면, MainThread에서 실행 되도록 변경.
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setEsimateItemList(mItemList);
                            }
                        });
                    }
                } else {
                    GomsLog.d(TAG, "CallBack()");
                }
            }
        });
    }

    /**
     * 부가세에 따른 총금액 산출하기
     * @param isVat
     */
    private void setEstTotalPrice(boolean isVat){
        String itemTotalPrice = "0";
        itemTotalPrice = ((EstimateItemAdapter)mAdapter).getTotalPrice();
        mTvItemTotalPrice.setText(itemTotalPrice);
    }

    private void goExcel(String mEstIdx){
        if(!StringUtil.isEmpty(mEstIdx)) {
            ExcelManager.I(getActivity()).setExcelInterface(new ExcelManager.ExcelInterface() {
                @Override
                public void onComplete(String path, Uri uri) {
                    //엑셀 경로 업데이트
                    GomsLog.d(TAG, "onComplete() >> mEstIdx : " + mEstIdx);
                    GomsLog.d(TAG, "onComplete() >> path : " + path);
                    GomsLog.d(TAG, "onComplete() >> uri.toString : " + uri.toString()); //content://media/external_primary/file/26950
                    GomsLog.d(TAG, "onComplete() >> uri.getPath : " + uri.getPath());   ///external_primary/file/26950 안불러짐..
                    MyApplication.getInstance().getDBHelper().updateEstimateExcelPath(mEstIdx, uri.toString());
                    //완료 하단팝업 띄우기
                    showDialogComplete(AppConstant.SAVE_EXCEL_TYPE.ESTIMATE.getName());
                }
            });

            EstimateBeanTB estimateBeanTB = MyApplication.getInstance().getDBHelper().getEstimateData(mEstIdx);
            //엑셀 첫 시작 시점입니다.
            ExcelManager.I(getActivity()).createWorkbook();
            //견적서 엑셀 작성하기
            ExcelManager.I(getActivity()).createExcel(estimateBeanTB, AppConstant.SAVE_EXCEL_TYPE.ESTIMATE);
        }
    }

    /**
     * 견적서 생성완료
     * @param excelType
     */
    public void showDialogComplete(String excelType){

        DialogManager.I().setTitle(excelType + " 엑셀 생성")
                .setMessage(excelType + " 엑셀 생성을 완료했습니다.")
                .setShowTitle(true)
                .setShowMessage(true)
                .setNegativeBtnName("")
                .setPositiveBtnName("확인")
                .setCancelable(false)
                .setCancelTouchOutSide(false)
                .setCommand(DialogCommandFactory.I().createDialogCommand(getActivity(), DialogCommandFactory.DIALOG_TYPE.basic.name(), new WaterCallBack() {
                    @Override
                    public void callback(BaseBean baseData) {
                        String btnType = ((Bundle)baseData.getObject()).getString(BaseBottomDialogCommand.EXT_BTN_TYPE);
                        if(BaseBottomDialogCommand.BTN_TYPE.LEFT.name().equalsIgnoreCase(btnType)){

                        }else if(BaseBottomDialogCommand.BTN_TYPE.RIGHT.name().equalsIgnoreCase(btnType)){
                            //requireActivity().onBackPressed();
                        }
                    }
                }))
                .showDialog(getActivity());
    }

    @MainThread
    private void setDiaBottomDialog(String mEstIdx){

        int diaCnt = AdmobPrefs.getInstance(getContext()).get(AdmobPrefs.DIA_CNT, 0);
        String bottomDialogTitle = "엑셀 생성 시작하기(보유 DIA " + diaCnt + "개)";

        if(diaCnt <= 0){
            DialogManager.I().setTitle(bottomDialogTitle)
                    .setMessage("견적서 엑셀 생성은 다이아 1개가 필요합니다.\n충전소로 이동하시겠습니까?")
                    .setShowTitle(true)
                    .setShowMessage(true)
                    .setNegativeBtnName("아니오")
                    .setPositiveBtnName("네")
                    .setCancelable(true)
                    .setCancelTouchOutSide(true)
                    .setCommand(DialogCommandFactory.I().createDialogCommand(getActivity(), DialogCommandFactory.DIALOG_TYPE.bottom_basic.name(), new WaterCallBack() {
                        @Override
                        public void callback(BaseBean baseData) {
                            String btnType = ((Bundle)baseData.getObject()).getString(BaseBottomDialogCommand.EXT_BTN_TYPE);
                            if(BaseBottomDialogCommand.BTN_TYPE.LEFT.name().equalsIgnoreCase(btnType)){

                            }else if(BaseBottomDialogCommand.BTN_TYPE.RIGHT.name().equalsIgnoreCase(btnType)){
                                ((MainActivity)getActivity()).changeFragment(new ChargingStationFragment(), "Charge", false);
                            }else if(BaseBottomDialogCommand.BTN_TYPE.SELECT.name().equalsIgnoreCase(btnType)){
                            }
                        }
                    }))
                    .showDialog(getActivity());
        }else{
            DialogManager.I().setTitle(bottomDialogTitle)
                    .setMessage("견적서 엑셀 생성은 다이아 1개가 차감됩니다.\n견적서 엑셀 생성을 시작하시겠습니까?")
                    .setShowTitle(true)
                    .setShowMessage(true)
                    .setNegativeBtnName("아니오")
                    .setPositiveBtnName("네")
                    .setCancelable(true)
                    .setCancelTouchOutSide(true)
                    .setCommand(DialogCommandFactory.I().createDialogCommand(getActivity(), DialogCommandFactory.DIALOG_TYPE.bottom_basic.name(), new WaterCallBack() {
                        @Override
                        public void callback(BaseBean baseData) {
                            String btnType = ((Bundle)baseData.getObject()).getString(BaseBottomDialogCommand.EXT_BTN_TYPE);
                            if(BaseBottomDialogCommand.BTN_TYPE.LEFT.name().equalsIgnoreCase(btnType)){

                            }else if(BaseBottomDialogCommand.BTN_TYPE.RIGHT.name().equalsIgnoreCase(btnType)){
                                int minusCnt = diaCnt - 1;
                                AdmobPrefs.getInstance(getContext()).put(AdmobPrefs.DIA_CNT, minusCnt);
                                goExcel(mEstIdx);
                            }else if(BaseBottomDialogCommand.BTN_TYPE.SELECT.name().equalsIgnoreCase(btnType)){
                            }
                        }
                    }))
                    .showDialog(getActivity());
        }
    }


}
