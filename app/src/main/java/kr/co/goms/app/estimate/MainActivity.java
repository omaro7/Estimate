package kr.co.goms.app.estimate;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import kr.co.goms.app.estimate.activity.SettingActivity;
import kr.co.goms.app.estimate.common.EstimatePrefs;
import kr.co.goms.app.estimate.fragment.BizFormFragment;
import kr.co.goms.app.estimate.fragment.EstimateListFragment;
import kr.co.goms.module.common.activity.CustomActivity;
import kr.co.goms.module.common.base.BaseBean;
import kr.co.goms.module.common.base.WaterCallBack;
import kr.co.goms.module.common.command.BaseBottomDialogCommand;
import kr.co.goms.module.common.manager.DialogManager;
import kr.co.goms.module.common.util.StringUtil;

public class MainActivity extends CustomActivity implements View.OnClickListener{

    private final String LOG_TAG = MainActivity.class.getSimpleName();

    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;

    private ActivityResultLauncher<Intent> mCameraLauncher;
    private ActivityResultLauncher<String> mAlbumLauncher;
    public static ActivityResultLauncher<String> mCadLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();

        String adId = MyApplication.getInstance().prefs().get(EstimatePrefs.AD_ID);

        mFragmentTransaction.replace(R.id.flt_root, new EstimateListFragment()).commitAllowingStateLoss();
        setInitUI();

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Toast.makeText(MainActivity.this, "Back button pressed!", Toast.LENGTH_SHORT).show();

            }
        };

        // Register the callback
        this.getOnBackPressedDispatcher().addCallback(this, callback);
    }

    private void setInitUI(){

    }


    private void setInitLauncher(){

        mAlbumLauncher = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri selectedImageUri) {
                    if (selectedImageUri != null) {
                        BizFormFragment bizFormFragment = (BizFormFragment) getSupportFragmentManager().findFragmentByTag("bizForm");
                        bizFormFragment.setAlbumPhoto(selectedImageUri);
                    }
                }
            });

    }

    private void goSetting(){
        Intent intent = new Intent(this, SettingActivity.class);
        startActivity(intent);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        /*
        switch (id) {
            case R.id.lottie_toad:
                goSetting();
                break;
        }
         */
    }

    public void changeFragment(Fragment fragment,@Nullable String tag){
        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.flt_root, fragment, tag);
        mFragmentTransaction.addToBackStack(null);
        mFragmentTransaction.commit();
    }

    public ActivityResultLauncher<Intent> getCameraLauncher(){
        return this.mCameraLauncher;
    }

    public ActivityResultLauncher<String> getAlbumLauncher(){
        return this.mAlbumLauncher;
    }

    public ActivityResultLauncher<String> getCadLauncher(){
        return this.mAlbumLauncher;
    }

    @Override
    public void onBackPressed() {
        Log.d(LOG_TAG, "onBackPressed()");

        int count = mFragmentManager.getBackStackEntryCount();
        if(count == 0){
            showExitDialog();
        }else{
            getSupportFragmentManager().popBackStack();
        }

    }

    private void showExitDialog(){
        DialogManager.I().showBottomDialogAppExitType(this, getString(kr.co.goms.module.common.R.string.app_exit), getString(R.string.app_full_name_2line) + " 앱을 종료합니다.", new WaterCallBack() {
            @Override
            public void callback(BaseBean baseBean) {
                String btnType = ((Bundle)baseBean.getObject()).getString(BaseBottomDialogCommand.EXT_BTN_TYPE);
                if(BaseBottomDialogCommand.BTN_TYPE.LEFT.name().equalsIgnoreCase(btnType)){

                }else if(BaseBottomDialogCommand.BTN_TYPE.RIGHT.name().equalsIgnoreCase(btnType)){
                    exitApp();
                }
            }
        });
    }

    private void exitApp(){
        Log.d(LOG_TAG, "exitApp()");
        moveTaskToBack(true);
        finishAffinity();
        System.exit(0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }
}