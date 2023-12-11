package kr.co.goms.module.admob.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

import kr.co.goms.module.admob.AdmobPrefs;
import kr.co.goms.module.admob.R;

public class ChargingStationFragment extends BaseFragment {

    private ConstraintLayout mRootView;
    private ConstraintLayout mLayoutView;
    private ConstraintSet mConstraintSet = new ConstraintSet();

    private TextView mTvDiaCnt;
    private RewardedAd mRewardedAd;

    private LinearLayout mLltChargingStation;

    /*-----------------------------------------------Member-------------------------------------------------------*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initAd();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_charging_station, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        mRootView = view.findViewById(R.id.rootView);
        mLayoutView = view.findViewById(R.id.clt_layout);

        mLltChargingStation = view.findViewById(R.id.llt_charging);
        mTvDiaCnt = view.findViewById(R.id.tv_dia_cnt);

        mTvDiaCnt.setText(String.valueOf(AdmobPrefs.getInstance(getContext()).get(AdmobPrefs.DIA_CNT, 0)));

        mLltChargingStation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRewardedVideo();
            }
        });

    }

    //리워드 광고 보기
    private void showRewardedVideo(){
        if (mRewardedAd != null) {

            mRewardedAd.show(getActivity(), new OnUserEarnedRewardListener() {
                @Override
                public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                    int rewardAmount = rewardItem.getAmount();
                    //String rewardType = rewardItem.getType();
                    addDia(rewardAmount);
                    loadRewardedVideoAd();
                }
            });

        }else{
            Toast.makeText(getContext(), "Wait a second\nRewarded Video Ad Failed To Load", Toast.LENGTH_SHORT).show();
            loadRewardedVideoAd();
        }
    }

    private void loadRewardedVideoAd() {

        AdRequest adRequest = new AdRequest.Builder().build();
        RewardedAd.load(getActivity(), getString(R.string.admob_widget_banner_reward),
                adRequest, new RewardedAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        mRewardedAd = null;
                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd ad) {
                        mRewardedAd = ad;
                    }
                });

        //mRewardedVideoAd.loadAd(getString(R.string.admob_widget_banner_reward), new AdRequest.Builder().build());
    }

    private void initAd(){
        /*
        mRewardedAd = MobileAds.getRewardedVideoAdInstance(getContext());
        mRewardedVideoAd.setRewardedVideoAdListener(new RewardedVideoAdListener() {
            @Override
            public void onRewardedVideoAdLoaded() {

            }

            @Override
            public void onRewardedVideoAdOpened() {

            }

            @Override
            public void onRewardedVideoStarted() {

            }

            @Override
            public void onRewardedVideoAdClosed() {
                loadRewardedVideoAd();
            }

            @Override
            public void onRewarded(RewardItem rewardItem) {
                addDia(rewardItem.getAmount());
            }

            @Override
            public void onRewardedVideoAdLeftApplication() {

            }

            @Override
            public void onRewardedVideoAdFailedToLoad(int i) {

            }

            @Override
            public void onRewardedVideoCompleted() {

            }
        });
         */
        loadRewardedVideoAd();
    }

    private void addDia(int amount){

        int currDiaCnt = AdmobPrefs.getInstance().get(AdmobPrefs.DIA_CNT, 0);
        int addDiaCnt = currDiaCnt + amount;
        AdmobPrefs.getInstance().put(AdmobPrefs.DIA_CNT, addDiaCnt);

        Toast.makeText(getContext(), "Add Dia", Toast.LENGTH_SHORT).show();
        mTvDiaCnt.setText(String.valueOf(addDiaCnt));

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
