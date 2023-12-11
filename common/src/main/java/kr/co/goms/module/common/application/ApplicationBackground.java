package kr.co.goms.module.common.application;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

import kr.co.goms.module.common.WaterFramework;

public class ApplicationBackground extends Application {

    final static String LOG_TAG = ApplicationBackground.class.getSimpleName();

    LifecycleListener lifecycleListener = new LifecycleListener();

    protected void onSetupLifecycleObserver(){
        ProcessLifecycleOwner.get().getLifecycle().addObserver(lifecycleListener);
    }

    class LifecycleListener implements LifecycleObserver {

        @OnLifecycleEvent(Lifecycle.Event.ON_START)
        public void onAppForegrounded() {
            // App in foreground
            Log.d(LOG_TAG, "onAppForegrounded()");
            WaterFramework.I().setStateApplicationOnly(WaterFramework.STATE.FOREGROUND);
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
        public void onAppBackgrounded() {
            //App in background
            Log.d(LOG_TAG, "onAppBackgrounded()");
            WaterFramework.I().setStateApplicationOnly(WaterFramework.STATE.BACKGROUND);
        }
    }
}