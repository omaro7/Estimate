package kr.co.goms.module.common;

import android.app.Application;
import android.util.Log;

public class CommonModule {
    static final String LOG_TAG = CommonModule.class.getSimpleName();

    public static Application mAppliation;
    public static boolean isFree = true;

    public static void initialize(Application application, boolean _isFree) {
        Log.d(LOG_TAG, "QuizModule initialize()");
        mAppliation = application;
        isFree = _isFree;
    }

}
