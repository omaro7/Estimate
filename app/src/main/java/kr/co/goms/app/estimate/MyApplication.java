package kr.co.goms.app.estimate;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import kr.co.goms.app.estimate.common.EstimatePrefs;
import kr.co.goms.app.estimate.jni.GomsJNI;
import kr.co.goms.app.estimate.manager.AdIdHelper;
import kr.co.goms.module.common.WaterFramework;
import kr.co.goms.module.common.application.ApplicationBackground;
import kr.co.goms.module.common.application.ApplicationInterface;
import kr.co.goms.module.common.curvlet.CurvletExit;
import kr.co.goms.module.common.curvlet.CurvletManager;
import kr.co.goms.module.common.curvlet.CurvletToast;

public class MyApplication extends ApplicationBackground implements ApplicationInterface, Application.ActivityLifecycleCallbacks, DefaultLifecycleObserver {

    private static final String LOG_TAG = MyApplication.class.getSimpleName();

    public static MyApplication mMyApplication;
    public static GomsJNI mGomsJNI;
    //public static ExamDBHelper mExamDBHelper;
    private EstimatePrefs mPreference;

    private ExecutorService mExecutorService;

    public MyApplication() {
        if(null != mMyApplication) {
            Log.d(LOG_TAG, "MyApplication() - 애플리케이션 생성자가 또 실행 되었다?");
        }
        Log.d(LOG_TAG, "################################## MyApplication() start! #################################");
        mMyApplication = this;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(LOG_TAG, "################################## MyApplication::onCreate() start! ##########################");
        this.registerActivityLifecycleCallbacks(this);

        mMyApplication = this;
        mPreference = EstimatePrefs.getInstance(getApplicationContext());

        mGomsJNI = new GomsJNI(this);
        //mExamDBHelper = new ExamDBHelper(this);

        curvletApiSettings();

        onSetupLifecycleObserver();
        beginFramework();
        checkDarkMode();

        initExecutors();
        initAdvertisingId();

    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        mMyApplication = null;
    }

    public void destroy() {
        Log.d(LOG_TAG, "destroy()");
        Log.e(LOG_TAG, "애플리케이션 종료");
    }

    private void initExecutors(){
        //백그라운드 작업을 실행하는 데 사용할 수 있는 4개의 스레드로 구성된 스레드 풀 생성
        mExecutorService = Executors.newFixedThreadPool(4);
        //Handler mainThreadHandler = HandlerCompat.createAsync(Looper.getMainLooper());
    }

    private void initAdvertisingId() {
        AdIdHelper.I().getAdvertisingId(this, new AdIdHelper.OnAdIDListener() {
            @Override
            public void onComplete(String adid) {
                Log.d(AppConstant.LOG_TAG, "ADID : " + adid);
                AdIdHelper.I().setADId(adid);
            }
        });
    }
    public ExecutorService getExecutorService() {
        return mExecutorService;
    }

    @Override
    public void curvletApiSettings() {
        Log.d(LOG_TAG, "curvletApiSettings()");
        CurvletManager.I().addCurvlet("water", "appExit", CurvletExit.class);
        CurvletManager.I().addCurvlet("water", "toast", CurvletToast.class);
    }

    public void init() {
        Log.d(LOG_TAG, "init()");
    }

    public static synchronized MyApplication getInstance() {
        return mMyApplication;
    }

    public GomsJNI getGomsJNI(){
        if(mGomsJNI == null){
            mGomsJNI = new GomsJNI(this);
        }
        return mGomsJNI;
    }

    public EstimatePrefs prefs() {
        return mPreference;
    }

    public String encryptKey() {
        if (prefs().getKey().equals(EstimatePrefs.EMPTY)) {
            return mGomsJNI.getEncryptKey();
        } else {
            return prefs().getKey();
        }
    }

    public void beginFramework() {
        ApplicationInterface applicationInterface = WaterFramework.I().getApplication();

        if(null == applicationInterface) {
            WaterFramework.Listener listener = new WaterFramework.Listener() {
                @Override
                public void init() {
                    mMyApplication.init();
                }

                @Override
                public void destroy() {
                    mMyApplication.destroy();
                }

                @Override
                public void shutdown(Bundle bundle) {

                }

                @Override
                public void restore(Bundle bundle) {

                }
            };

            WaterFramework.I().create(this, listener, 720.0f, 1280.0f);
        }
    }

    /**
     * 다크모드 미지원 처리
     */
    private void checkDarkMode(){
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }

    /**
     * DefaultLifecycleObserver method that shows the app open ad when the app moves to foreground.
     */
    @Override
    public void onStart(@NonNull LifecycleOwner owner) {
        DefaultLifecycleObserver.super.onStart(owner);
    }

    /** ActivityLifecycleCallback methods. */
    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {}

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        // An ad activity is started when an ad is showing, which could be AdActivity class from Google
        // SDK or another activity class implemented by a third party mediation partner. Updating the
        // currentActivity only when an ad is not showing will ensure it is not an ad activity, but the
        // one that shows the ad.
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {}

    @Override
    public void onActivityPaused(@NonNull Activity activity) {}

    @Override
    public void onActivityStopped(@NonNull Activity activity) {}

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {}

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {}


}
