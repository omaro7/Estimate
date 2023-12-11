package kr.co.goms.module.admob;

import android.app.Application;
import android.util.Log;

public class AdmobModule {
    static final String LOG_TAG = AdmobModule.class.getSimpleName();

    public static Application mAppliation;
    public static boolean isInitModule = false;
    public static boolean isInitSurveyServer = false;
    public static String mIvKey, mEncryptKey;

    public static void initialize(Application application, String ivKey, String encryptKey) {
        Log.d(LOG_TAG, "QuizModule initialize()");
        mAppliation = application;
        onInitCurvlet();
        isInitModule = true;
        mIvKey = ivKey;
        mEncryptKey = encryptKey;
    }

    protected static void onInitCurvlet() {
        Log.d(LOG_TAG, "onInitCurvlet()");
        isInitSurveyServer = true;
    }

    public static boolean isIsInitModule() {
        return isInitModule;
    }
}
