package kr.co.goms.app.estimate.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import kr.co.goms.app.estimate.AppConstant;
import kr.co.goms.app.estimate.BuildConfig;
import kr.co.goms.app.estimate.MainActivity;
import kr.co.goms.app.estimate.MyApplication;
import kr.co.goms.app.estimate.R;
import kr.co.goms.app.estimate.activity.SettingActivity;
import kr.co.goms.app.estimate.adapter.EstimateAdapter;
import kr.co.goms.app.estimate.adapter.SpecificationAdapter;
import kr.co.goms.app.estimate.common.EstimatePrefs;
import kr.co.goms.app.estimate.manager.SendManager;
import kr.co.goms.app.estimate.model.EstimateBeanTB;
import kr.co.goms.app.estimate.model.SpecificationBeanTB;
import kr.co.goms.app.estimate.send_data.SendDataFactory;
import kr.co.goms.module.common.activity.CustomActivity;
import kr.co.goms.module.common.base.BaseBean;
import kr.co.goms.module.common.base.WaterCallBack;
import kr.co.goms.module.common.command.BaseBottomDialogCommand;
import kr.co.goms.module.common.curvlet.CurvletManager;
import kr.co.goms.module.common.manager.DialogCommandFactory;
import kr.co.goms.module.common.manager.DialogManager;
import kr.co.goms.module.common.manager.FragmentMoveManager;
import kr.co.goms.module.common.observer.ObserverInterface;
import kr.co.goms.module.common.util.FileUtil;
import kr.co.goms.module.common.util.GomsLog;
import kr.co.goms.module.common.util.ImageUtil;

public class SpecificationListFragment extends Fragment  implements View.OnClickListener {

    private final String TAG = SpecificationListFragment.class.getSimpleName();

    private Toolbar mToolbar;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Filter mFilter;

    private ProgressBar mPbLoading;

    private ArrayList<SpecificationBeanTB> mSpecificationList = new ArrayList<SpecificationBeanTB>();

    private static final int SPAN_COUNT = 1;

    private EditText mEtSearchPlace;
    private ImageButton mInputClear;

    private Button mBtnCreate;

    private ObserverInterface mDataObserver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_specification_list, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        mToolbar = view.findViewById(R.id.toolbar);
        ((CustomActivity) getActivity()).setSupportActionBar(mToolbar);
        Objects.requireNonNull(((CustomActivity) getActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(false);

        ImageView ivSetting = view.findViewById(R.id.iv_setting);
        //ivSetting.setVisibility(View.GONE);
        ivSetting.setOnClickListener(this);

        mPbLoading = view.findViewById(R.id.pb_loader);

        mRecyclerView = view.findViewById(R.id.rv_specification_list);

        //mLayoutManager = new GridLayoutManager(view.getContext(), SPAN_COUNT);
        mLayoutManager = new StaggeredGridLayoutManager(SPAN_COUNT, StaggeredGridLayoutManager.VERTICAL);
        ((StaggeredGridLayoutManager)mLayoutManager).setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        mRecyclerView.setLayoutManager(mLayoutManager);

        mEtSearchPlace = view.findViewById(R.id.et_search_place);

        mEtSearchPlace.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    if (!TextUtils.isEmpty(charSequence.toString()) || !"".equals(charSequence.toString())) {
                        String searchTxt = mEtSearchPlace.getText().toString();
                        mFilter.filter(searchTxt);
                        showInputClear(true);
                    } else if (TextUtils.isEmpty(charSequence.toString())) {
                        mFilter.filter("");
                        showInputClear(false);
                    }
                    mRecyclerView.getAdapter().notifyDataSetChanged();
                }catch(NullPointerException e){

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        mInputClear = view.findViewById(R.id.btn_input_clear);
        mInputClear.setOnClickListener(this);

        mBtnCreate = view.findViewById(R.id.btn_ok);
        mBtnCreate.setOnClickListener(this);

        getSpecificationData();

        if(BuildConfig.IS_FREE) {
            //띠배너 호출
            initBanner(view);
        }

        this.setHasOptionsMenu(true);
    }

    private void initBanner(View view){
        //광고
        AdView adViewMiddle = view.findViewById(R.id.adview_bottom);
        AdRequest adRequestMiddle = new AdRequest.Builder().build();
        adViewMiddle.loadAd(adRequestMiddle);
    }

    private void getSpecificationData(){

        mDataObserver = new ObserverInterface() {
            @Override
            public void callback(BaseBean baseBean) {
                GomsLog.d(TAG, "mDataObserver  CallBack()");

                if (Looper.myLooper() == Looper.getMainLooper()) {
                    mPbLoading.setVisibility(View.GONE);
                } else {
                    // WorkThread이면, MainThread에서 실행 되도록 변경.
                    try {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mPbLoading.setVisibility(View.GONE);
                            }
                        });
                    }catch(NullPointerException e){

                    }
                }

                if (baseBean.getStatus() == BaseBean.STATUS.SUCCESS) {

                    mSpecificationList = (ArrayList<SpecificationBeanTB>) baseBean.getObject();
                    GomsLog.d(TAG, "mSpecificationList 갯수 : " + mSpecificationList.size());

                    //왜 화면로딩이 되지 않을까?? -> Thread 처리
                    if (Looper.myLooper() == Looper.getMainLooper()) {
                        setSpecificationList(mSpecificationList);
                    } else {
                        // WorkThread이면, MainThread에서 실행 되도록 변경.
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setSpecificationList(mSpecificationList);
                            }
                        });
                    }

                } else {
                    GomsLog.d(TAG, "CallBack() : 인트로 Category Data 실패!!!!");
                }
            }
        };

        HashMap<String, Object> params = new HashMap<>();
        SendManager.I().sendData(SendDataFactory.DATA_TYPE.SPEC_LIST, params, mDataObserver);

    }

    @MainThread
    private void setSpecificationList(ArrayList<SpecificationBeanTB> estmateList){
        Log.d(TAG, "setSpecificationList()");

        mAdapter = new SpecificationAdapter(getActivity(), estmateList, new SpecificationAdapter.SpecificationClickListener() {
            @Override
            public void onSpecificationClick(int position, SpecificationBeanTB specificationBeanTB) {
                Log.d(TAG, "specificationBeanTB 클릭");
                FragmentMoveManager.I().setManager(getActivity(), R.id.nav_host_fragment).changeFragment(SpecificationViewFragment.getFragment(specificationBeanTB.getSpec_idx()), AppConstant.FRAGMENT_TAG.SPECIFICATION_VIEW.name(), false);
            }

            @Override
            public void onSpecificationLongClick(int position, SpecificationBeanTB specificationBeanTB) {
                Log.d(TAG, "specificationBeanTB 롱클릭");
                goSpecDeleteDialog(specificationBeanTB);
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onSpecificationExcelDownloadClick(int position, SpecificationBeanTB specificationBeanTB) {
                Log.d(TAG, "specificationBeanTB 견적서 다운로드 >>>> " + specificationBeanTB.getSpec_excel_path()); //content://media/external_primary/file/26950
                Uri excelFileUri = Uri.parse(specificationBeanTB.getSpec_excel_path());

                Log.d(TAG, "excelFileUri.toString() : " + excelFileUri.toString()); //content://media/external_primary/file/26950

                try {
                    if (FileUtil.isFileExist(ImageUtil.getRealPathFromURI(getActivity(), excelFileUri))) {
                        Log.d(TAG, "excel File 존재합니다");
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setDataAndType(excelFileUri, "application/vnd.ms-excel");
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        startActivity(intent);
                    } else {
                        Log.d(TAG, "excel File 존재하지 않습니다");
                        CurvletManager.process(getActivity(), null, "water://toast?text=excel file이 존재하지 않습니다.");
                        //엑셀파일 위치 초기화
                        MyApplication.getInstance().getDBHelper().updateSpecificationExcelPath(specificationBeanTB.getSpec_idx(), "");
                        mAdapter.notifyDataSetChanged();
                    }
                }catch(Exception e){
                    Log.d(TAG, "excel File 존재하지 않습니다");
                    CurvletManager.process(getActivity(), null, "water://toast?text=excel file이 존재하지 않습니다.");
                    //엑셀파일 위치 초기화
                    MyApplication.getInstance().getDBHelper().updateSpecificationExcelPath(specificationBeanTB.getSpec_idx(), "");
                    mAdapter.notifyDataSetChanged();
                }
            }
        });

        ((SpecificationAdapter)mAdapter).setSearchType(SpecificationAdapter.SEARCH_TYPE.TITLE);    //장소명 검색조건
        mFilter = ((SpecificationAdapter)mAdapter).getFilter();

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setVisibility(View.VISIBLE);

        //LayoutManager를 재세팅을 해야지만, 상단으로 다시 정렬됨
        mLayoutManager = new StaggeredGridLayoutManager(SPAN_COUNT, StaggeredGridLayoutManager.VERTICAL);
        ((StaggeredGridLayoutManager)mLayoutManager).setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        mRecyclerView.setLayoutManager(mLayoutManager);

    }


    @Override
    public void onPause() {
        super.onPause();
    }

    /**
     * 글자 클리어 처리
     */
    private void applyClear(){
        mEtSearchPlace.setText("");
        mFilter.filter("");
        showInputClear(false);
    }

    private void showInputClear(boolean isShow){
        if(isShow) {
            mInputClear.setVisibility(View.VISIBLE);
        }else{
            mInputClear.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        switch (id) {
            case R.id.btn_input_clear:
                applyClear();
                break;
            case R.id.btn_ok:
                //goDialog();
                ((MainActivity)getActivity()).changeFragment(new EstimateFormFragment(),"EstForm", false);
                break;
            case R.id.iv_setting:
                Intent intent = new Intent(getActivity(), SettingActivity.class);
                startActivity(intent);
                break;
        }
    }

    /**
     * 명세서 삭제하기
     * @param specIdx
     */
    private void sendSpecificationDelete(String specIdx){
        HashMap<String, Object> params = new HashMap<>();
        params.put("specIdx", specIdx);
        SendManager.I().sendData(SendDataFactory.DATA_TYPE.SPEC_DELETE, params, mDataObserver);
    }

    /**
     * 명세서 삭제하기 하단팝업
     * @param specificationBeanTB
     */
    private void goSpecDeleteDialog(SpecificationBeanTB specificationBeanTB){
        DialogManager.I().setTag("specDelete")
                .setTitle("'"+ specificationBeanTB.getEstimateBeanTB().getEst_cli_name()+"' 삭제")
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
                            Log.d(TAG, " 클릭 >>>> 삭제하기" + specificationBeanTB.getSpec_idx());
                            sendSpecificationDelete(specificationBeanTB.getSpec_idx());
                        }
                    }
                }))
                .showDialog(getActivity());
    }


}