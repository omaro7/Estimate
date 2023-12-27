package kr.co.goms.app.estimate.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import kr.co.goms.app.estimate.R;
import kr.co.goms.app.estimate.fragment.SettingFragment;
import kr.co.goms.module.common.activity.CustomActivity;

public class SettingActivity extends CustomActivity {

    FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        mFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();

        Fragment fragment = SettingFragment.getFragment(0);
        fragmentTransaction.replace(R.id.setting_nav_host_fragment, fragment).commit();
    }

    public void changeFragment(Fragment fragment, String name,@Nullable boolean isPop){

        //isPos > 현재 fragment 없애고, 이동 처리함. back했을 때, 확인 가능.
        if(isPop){
            mFragmentManager.popBackStack();
        }

        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.addToBackStack(name);
        fragmentTransaction.replace(R.id.setting_nav_host_fragment, fragment).commit();
    }

    @Override
    public void onBackPressed() {
        int count = mFragmentManager.getBackStackEntryCount();
        if(count == 0){
            super.onBackPressed();
        }else{
            getSupportFragmentManager().popBackStack();
        }
    }

}