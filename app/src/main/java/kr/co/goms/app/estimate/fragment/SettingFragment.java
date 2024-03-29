package kr.co.goms.app.estimate.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.Objects;

import kr.co.goms.app.estimate.AppConstant;
import kr.co.goms.app.estimate.BuildConfig;
import kr.co.goms.app.estimate.MainActivity;
import kr.co.goms.app.estimate.MyApplication;
import kr.co.goms.app.estimate.R;
import kr.co.goms.app.estimate.activity.SettingActivity;
import kr.co.goms.app.estimate.common.EstimatePrefs;
import kr.co.goms.module.admob.AdmobPrefs;
import kr.co.goms.module.admob.fragment.ChargingStationFragment;
import kr.co.goms.module.common.activity.CustomActivity;
import kr.co.goms.module.common.util.StringUtil;

public class SettingFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = SettingFragment.class.getSimpleName();

    public SettingFragment(){}

    private Toolbar mToolbar;
    private SwitchCompat mSwitchBlcok;
    private EditText mEtEstimatePrefix, mEtEstimateFolder;
    private Button mBtnCharge;
    private String isShowBlockYN = "N";


    public static SettingFragment getFragment(int page){
        SettingFragment fragment = new SettingFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("page", page);
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
        ConstraintLayout layout = (ConstraintLayout) inflater.inflate(R.layout.fragment_setting, container, false);
        return layout;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mToolbar = view.findViewById(R.id.toolbar);
        ((CustomActivity) getActivity()).setSupportActionBar(mToolbar);
        Objects.requireNonNull(((CustomActivity) getActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        TextView tvToolBarTitle = view.findViewById(R.id.tv_toolbar_title);
        tvToolBarTitle.setText("설정");

        isShowBlockYN = MyApplication.getInstance().prefs().get(EstimatePrefs.MH_BLOCK_IMPORT_YN);

        mSwitchBlcok = view.findViewById(R.id.sc_block_yn);
        mSwitchBlcok.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isShowBlockYN = isChecked?"Y":"N";
                MyApplication.getInstance().prefs().put(EstimatePrefs.MH_BLOCK_IMPORT_YN, isShowBlockYN);
            }
        });

        mSwitchBlcok.setChecked("Y".equalsIgnoreCase(isShowBlockYN));

        mEtEstimatePrefix = view.findViewById(R.id.et_estimate_prefix);
        mEtEstimateFolder = view.findViewById(R.id.et_estimate_folder);

        String estimatePrefix = MyApplication.getInstance().prefs().get(AppConstant.EST_PREFIX);
        if(StringUtil.isEmpty(estimatePrefix)){
            estimatePrefix = "AB";
        }
        mEtEstimatePrefix.setText(estimatePrefix);


        String estimateFolder= MyApplication.getInstance().prefs().get(AppConstant.EST_FOLDER);
        if(StringUtil.isEmpty(estimateFolder)){
            estimateFolder = AppConstant.EST_FOLDER_DEFAULT;
        }
        mEtEstimateFolder.setText(estimateFolder);

        RelativeLayout rltCom = view.findViewById(R.id.rl_com);
        RelativeLayout rltItem = view.findViewById(R.id.rl_item);
        RelativeLayout rltCli = view.findViewById(R.id.rl_cli);
        RelativeLayout rltEst = view.findViewById(R.id.rl_est);

        view.findViewById(R.id.btn_est_prefix).setOnClickListener(this);
        view.findViewById(R.id.btn_est_folder).setOnClickListener(this);
        mBtnCharge = view.findViewById(R.id.btn_charge);
        mBtnCharge.setOnClickListener(this);

        int dia = AdmobPrefs.getInstance(getContext()).get(AdmobPrefs.DIA_CNT, 0);
        mBtnCharge.setText(String.valueOf(dia));

        rltCom.setOnClickListener(this);
        rltItem.setOnClickListener(this);
        rltCli.setOnClickListener(this);
        rltEst.setOnClickListener(this);

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
        if(id == R.id.rl_com){
            ((MainActivity)getActivity()).changeFragment(new CompanyListFragment(), "ComList", false);
        }else if(id == R.id.rl_item){
            ((MainActivity)getActivity()).changeFragment(new ItemListFragment(), "ItemList", false);
        }else if(id == R.id.rl_cli){
            ((MainActivity)getActivity()).changeFragment(new ClientListFragment(), "CliList", false);
        }else if(id == R.id.rl_est){
            ((MainActivity)getActivity()).changeFragment(new EstimateListFragment(), "EstList", false);
        }else if(id == R.id.btn_est_prefix){
            String prefix = mEtEstimatePrefix.getText().toString();
            MyApplication.getInstance().prefs().put(AppConstant.EST_PREFIX, prefix);
        }else if(id == R.id.btn_est_folder){
            String folder = mEtEstimateFolder.getText().toString();
            MyApplication.getInstance().prefs().put(AppConstant.EST_FOLDER, folder);
        }else if(id == R.id.btn_charge){
            ((MainActivity)requireActivity()).changeFragment(new ChargingStationFragment(), "충전소", false);
        }

    }
}
