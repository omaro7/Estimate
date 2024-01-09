package kr.co.goms.module.common.manager;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class FragmentMoveManager {
    static FragmentMoveManager instance;  //인스턴스

    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;
    private int mLayoutId;

    public FragmentMoveManager() {
    }

    public static FragmentMoveManager I(){
        if(instance == null){
            instance = new FragmentMoveManager();
        }
        return instance;
    }

    /**
     *
     * @param activity
     * @param layoutId R.id.setting_nav_host_fragment
     * @return
     */
    public FragmentMoveManager setManager(FragmentActivity activity, int layoutId){
        mFragmentManager = activity.getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mLayoutId = layoutId;
        return instance;
    }

    public FragmentMoveManager changeFragment(Fragment fragment, String name, boolean isPop){

        //isPos > 현재 fragment 없애고, 이동 처리함. back했을 때, 확인 가능.
        if(isPop){
            mFragmentManager.popBackStack();
        }

        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.addToBackStack(name);
        fragmentTransaction.replace(mLayoutId, fragment, name).commit();
        return instance;
    }

    public Fragment getFragment(String tagName){
        Fragment fragment = mFragmentManager.findFragmentByTag(tagName);
        return fragment;
    }
}
