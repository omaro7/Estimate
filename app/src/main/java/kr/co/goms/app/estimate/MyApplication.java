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
import androidx.lifecycle.ProcessLifecycleOwner;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.appopen.AppOpenAd;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import kr.co.goms.app.estimate.common.EstimatePrefs;
import kr.co.goms.app.estimate.db.DBHelper;
import kr.co.goms.app.estimate.jni.GomsJNI;
import kr.co.goms.app.estimate.manager.AdIdHelper;
import kr.co.goms.module.admob.AdmobModule;
import kr.co.goms.module.common.CommonModule;
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
    public DBHelper mDBHelper;
    private EstimatePrefs mPreference;

    private ExecutorService mExecutorService;

    private Activity currentActivity;
    private AppOpenAdManager appOpenAdManager;

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
        mDBHelper = new DBHelper(this);

        //mDBHelper.deleteTable(EstimateDB.EstimateTable.ESTIMATE_TABLE);
        //mDBHelper.createTable();
        //mDBHelper.removeTableData(EstimateDB.EstimateItemTable.ESTIMATE_ITEM_TABLE);
        //mDBHelper.removeTableData(EstimateDB.TempItemTable.TEMP_ITEM_TABLE);

        curvletApiSettings();

        onSetupLifecycleObserver();
        beginFramework();
        checkDarkMode();

        if(BuildConfig.IS_FREE) {
            initAds();
        }

        initExecutors();
        initAdvertisingId();

        CommonModule.initialize(this, BuildConfig.IS_FREE);

    }

    private void initAds(){

        AdmobModule.initialize(this, mGomsJNI.ivKey(), mGomsJNI.encryptKey());

        MobileAds.initialize(this);
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
        appOpenAdManager = new AppOpenAdManager();

        /*
        List<String> testDeviceIds = Arrays.asList("33BE2250B43518CCDA7DE426D04EE231");
        RequestConfiguration requestConfiguration = new RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build();
        MobileAds.setRequestConfiguration(requestConfiguration);
         */
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
        CurvletManager.I().create("water");
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

    private class AppOpenAdManager {

        private static final String LOG_TAG = "AppOpenAdManager";
        private static final String AD_UNIT_ID = "ca-app-pub-9725459038205351/5947995474";

        private AppOpenAd appOpenAd = null;
        private boolean isLoadingAd = false;
        private boolean isShowingAd = false;

        /** Keep track of the time an app open ad is loaded to ensure you don't show an expired ad. */
        private long loadTime = 0;

        /** Constructor. */
        public AppOpenAdManager() {}

        /**
         * Load an ad.
         *
         * @param context the context of the activity that loads the ad
         */
        private void loadAd(Context context) {
            // Do not load ad if there is an unused ad or one is already loading.
            if (isLoadingAd || isAdAvailable()) {
                return;
            }

            isLoadingAd = true;
            AdRequest request = new AdRequest.Builder().build();
            AppOpenAd.load(
                    context,
                    AD_UNIT_ID,
                    request,
                    new AppOpenAd.AppOpenAdLoadCallback() {
                        /**
                         * Called when an app open ad has loaded.
                         *
                         * @param ad the loaded app open ad.
                         */
                        @Override
                        public void onAdLoaded(AppOpenAd ad) {
                            appOpenAd = ad;
                            isLoadingAd = false;
                            loadTime = (new Date()).getTime();

                            Log.d(LOG_TAG, "onAdLoaded.");
                            //Toast.makeText(context, "onAdLoaded", Toast.LENGTH_SHORT).show();
                        }

                        /**
                         * Called when an app open ad has failed to load.
                         *
                         * @param loadAdError the error.
                         */
                        @Override
                        public void onAdFailedToLoad(LoadAdError loadAdError) {
                            isLoadingAd = false;
                            Log.d(LOG_TAG, "onAdFailedToLoad: " + loadAdError.getMessage());
                            //Toast.makeText(context, "onAdFailedToLoad", Toast.LENGTH_SHORT).show();
                        }
                    });
        }

        /** Check if ad was loaded more than n hours ago. */
        private boolean wasLoadTimeLessThanNHoursAgo(long numHours) {
            long dateDifference = (new Date()).getTime() - loadTime;
            long numMilliSecondsPerHour = 3600000;
            return (dateDifference < (numMilliSecondsPerHour * numHours));
        }

        /** Check if ad exists and can be shown. */
        private boolean isAdAvailable() {
            // Ad references in the app open beta will time out after four hours, but this time limit
            // may change in future beta versions. For details, see:
            // https://support.google.com/admob/answer/9341964?hl=en
            return appOpenAd != null && wasLoadTimeLessThanNHoursAgo(4);
        }

        /**
         * Show the ad if one isn't already showing.
         *
         * @param activity the activity that shows the app open ad
         */
        private void showAdIfAvailable(@NonNull final Activity activity) {
            showAdIfAvailable(
                    activity,
                    new OnShowAdCompleteListener() {
                        @Override
                        public void onShowAdComplete() {
                            // Empty because the user will go back to the activity that shows the ad.
                        }
                    });
        }

        /**
         * Show the ad if one isn't already showing.
         *
         * @param activity the activity that shows the app open ad
         * @param onShowAdCompleteListener the listener to be notified when an app open ad is complete
         */
        private void showAdIfAvailable(
                @NonNull final Activity activity,
                @NonNull OnShowAdCompleteListener onShowAdCompleteListener) {
            // If the app open ad is already showing, do not show the ad again.
            if (isShowingAd) {
                Log.d(LOG_TAG, "The app open ad is already showing.");
                return;
            }

            // If the app open ad is not available yet, invoke the callback then load the ad.
            if (!isAdAvailable()) {
                Log.d(LOG_TAG, "The app open ad is not ready yet.");
                onShowAdCompleteListener.onShowAdComplete();
                loadAd(activity);
                return;
            }

            Log.d(LOG_TAG, "Will show ad.");

            appOpenAd.setFullScreenContentCallback(
                    new FullScreenContentCallback() {
                        /** Called when full screen content is dismissed. */
                        @Override
                        public void onAdDismissedFullScreenContent() {
                            // Set the reference to null so isAdAvailable() returns false.
                            appOpenAd = null;
                            isShowingAd = false;

                            Log.d(LOG_TAG, "onAdDismissedFullScreenContent.");

                            onShowAdCompleteListener.onShowAdComplete();
                            loadAd(activity);

/*
                            Intent intent = new Intent(currentActivity, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
*/

                        }

                        /** Called when fullscreen content failed to show. */
                        @Override
                        public void onAdFailedToShowFullScreenContent(AdError adError) {
                            appOpenAd = null;
                            isShowingAd = false;

                            Log.d(LOG_TAG, "onAdFailedToShowFullScreenContent: " + adError.getMessage());

                            onShowAdCompleteListener.onShowAdComplete();
                            loadAd(activity);
                        }

                        /** Called when fullscreen content is shown. */
                        @Override
                        public void onAdShowedFullScreenContent() {
                            Log.d(LOG_TAG, "onAdShowedFullScreenContent.");
                        }
                    });

            isShowingAd = true;
            appOpenAd.show(activity);
        }
    }


    /**
     * DefaultLifecycleObserver method that shows the app open ad when the app moves to foreground.
     */
    @Override
    public void onStart(@NonNull LifecycleOwner owner) {
        DefaultLifecycleObserver.super.onStart(owner);
        if(BuildConfig.IS_FREE) {
            appOpenAdManager.showAdIfAvailable(currentActivity);
        }
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
        if(BuildConfig.IS_FREE) {
            if (!appOpenAdManager.isShowingAd) {
                currentActivity = activity;
            }
        }
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

    public DBHelper getDBHelper() {
        return mDBHelper;
    }

    public void setDBHelper(DBHelper mDBHelper) {
        this.mDBHelper = mDBHelper;
    }

    /**
     * Interface definition for a callback to be invoked when an app open ad is complete
     * (i.e. dismissed or fails to show).
     */
    public interface OnShowAdCompleteListener {
        void onShowAdComplete();
    }
}
