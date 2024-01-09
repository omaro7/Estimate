package kr.co.goms.app.estimate.fragment;

import static kr.co.goms.app.estimate.command.ItemFormBottomDialogCommand.EXT_OBJECT;

import android.content.Intent;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import kr.co.goms.app.estimate.AppConstant;
import kr.co.goms.app.estimate.MainActivity;
import kr.co.goms.app.estimate.MyApplication;
import kr.co.goms.app.estimate.R;
import kr.co.goms.app.estimate.activity.SettingActivity;
import kr.co.goms.app.estimate.adapter.ClientAdapter;
import kr.co.goms.app.estimate.command.ItemFormBottomDialogCommand;
import kr.co.goms.app.estimate.common.EstimatePrefs;
import kr.co.goms.app.estimate.manager.SendManager;
import kr.co.goms.app.estimate.model.ClientBeanTB;
import kr.co.goms.app.estimate.send_data.SendDataFactory;
import kr.co.goms.module.common.activity.CustomActivity;
import kr.co.goms.module.common.base.BaseBean;
import kr.co.goms.module.common.base.WaterCallBack;
import kr.co.goms.module.common.command.BaseBottomDialogCommand;
import kr.co.goms.module.common.manager.DialogCommandFactory;
import kr.co.goms.module.common.manager.DialogManager;
import kr.co.goms.module.common.manager.FragmentMoveManager;
import kr.co.goms.module.common.observer.ObserverInterface;
import kr.co.goms.module.common.util.GomsLog;

public class ClientListFragment extends Fragment  implements View.OnClickListener {

    private final String TAG = ClientListFragment.class.getSimpleName();

    private Toolbar mToolbar;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Filter mFilter;

    private ProgressBar mPbLoading;

    private ArrayList<ClientBeanTB> mClientList = new ArrayList<ClientBeanTB>();

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
        return inflater.inflate(R.layout.fragment_client_list, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        mToolbar = view.findViewById(R.id.toolbar);
        ((CustomActivity) getActivity()).setSupportActionBar(mToolbar);
        Objects.requireNonNull(((CustomActivity) getActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(false);

        ImageView ivSetting = view.findViewById(R.id.iv_setting);
        ivSetting.setVisibility(View.GONE);
        ivSetting.setOnClickListener(this);

        mPbLoading = view.findViewById(R.id.pb_loader);

        mRecyclerView = view.findViewById(R.id.rv_client_list);

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

        getClientData();

        this.setHasOptionsMenu(true);
    }

    private void getClientData(){

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

                    mClientList = (ArrayList<ClientBeanTB>) baseBean.getObject();
                    GomsLog.d(TAG, "mClientList 갯수 : " + mClientList.size());

                    //왜 화면로딩이 되지 않을까?? -> Thread 처리
                    if (Looper.myLooper() == Looper.getMainLooper()) {
                        setClientList(mClientList);
                    } else {
                        // WorkThread이면, MainThread에서 실행 되도록 변경.
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setClientList(mClientList);
                            }
                        });
                    }

                } else {
                    GomsLog.d(TAG, "CallBack() : 인트로 Category Data 실패!!!!");
                }
            }
        };

        HashMap<String, Object> params = new HashMap<>();
        //params.put("mbIdx", mbIdx);
        SendManager.I().sendData(SendDataFactory.DATA_TYPE.CLI_LIST, params, mDataObserver);

    }

    @MainThread
    private void setClientList(ArrayList<ClientBeanTB> placeList){
        Log.d(TAG, "setItemList()");

        mAdapter = new ClientAdapter(getActivity(), placeList, new ClientAdapter.ClientClickListener() {
            @Override
            public void onClientClick(int position, ClientBeanTB clientBeanTB) {
                Log.d(TAG, "clientBeanTB 클릭 >>>> " + clientBeanTB.getCli_name());
                //((MainActivity)getActivity()).changeFragment(FieldBasicListFragment.getFragment(groupBeanS.getRes_mh_group_idx(), groupBeanS.getRes_mh_group_name()), "fieldBasicList");
                FragmentMoveManager.I().setManager(getActivity(), R.id.nav_host_fragment).changeFragment(ClientFormFragment.getFragment(clientBeanTB.getCli_idx()), AppConstant.FRAGMENT_TAG.CLIENT_FORM.name(), false);
            }

            @Override
            public void onClientLongClick(int position, ClientBeanTB clientBeanTB) {
                Log.d(TAG, "Group 롱클릭 >>>> " + clientBeanTB.getCli_name());
                goCliDeleteDialog(clientBeanTB);
            }
        });

        ((ClientAdapter)mAdapter).setSearchType(ClientAdapter.SEARCH_TYPE.TITLE);    //장소명 검색조건
        mFilter = ((ClientAdapter)mAdapter).getFilter();

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
                ((MainActivity)getActivity()).changeFragment(new ClientFormFragment(),"CliForm", false);
                break;
            case R.id.iv_setting:
                Intent intent = new Intent(getActivity(), SettingActivity.class);
                startActivity(intent);
                break;
        }
    }

    /**
     * 거래차 삭제하기
     * @param cliIdx
     */
    private void sendCliDelete(String cliIdx){
        HashMap<String, Object> params = new HashMap<>();
        params.put("cliIdx", cliIdx);
        SendManager.I().sendData(SendDataFactory.DATA_TYPE.CLI_DELETE, params, mDataObserver);
    }

    /**
     * 아이템 삭제하기 하단팝업
     * @param clientBeanTB
     */
    private void goCliDeleteDialog(ClientBeanTB clientBeanTB){
        DialogManager.I().setTag("cliDelete")
                .setTitle("'"+ clientBeanTB.getCli_name()+"' 삭제")
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
                            Log.d(TAG, " 클릭 >>>> 삭제하기 mbIdx:" + MyApplication.getInstance().prefs().get(EstimatePrefs.AD_ID) + ",groupIdx:" + clientBeanTB.getCli_idx());
                            sendCliDelete(clientBeanTB.getCli_idx());
                        }
                    }
                }))
                .showDialog(getActivity());
    }


}
