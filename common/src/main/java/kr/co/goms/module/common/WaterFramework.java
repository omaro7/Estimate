package kr.co.goms.module.common;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import kr.co.goms.module.common.application.ApplicationInterface;
import kr.co.goms.module.common.base.BaseBean;
import kr.co.goms.module.common.curvlet.CurvletManager;
import kr.co.goms.module.common.observer.BackgroundStateNotify;
import kr.co.goms.module.common.observer.ObserverInterface;

public class WaterFramework {
    private static final String LOG_TAG = "Framework";
    private static WaterFramework instance;
    private static String token;
    private static String tokenUnusual;
    private static WaterFramework.STATE state;
    private static float displayStandardWidth;
    private static float displayStandardHeight;
    private static String commandPush;
    private static ObserverInterface observerPush;
    private static ArrayList<Activity> activityUnusualList;
    private ApplicationInterface app;
    private WaterFramework.Listener listener;
    private ArrayList<Activity> activityList;
    private Activity currentActivity;
    private HashMap<String, WaterFramework.Event> eventHashMap;
    private HashMap<String, WaterFramework.Event> eventMashMapInOut;
    private Handler handler;

    public WaterFramework() {
    }

    public static WaterFramework I() {
        if (null == instance) {
            Log.e("WaterFramework", "WaterFramework 생성");
            instance = new WaterFramework();
        }

        return instance;
    }

    protected static void D() {
        if (null != instance) {
            Log.e("WaterFramework", "WaterFramework 종료");
            instance.onDestroy();
            instance = null;
        }

    }

    public void appTaskRemove(Context context) {
        @SuppressLint("WrongConstant") ActivityManager am = (ActivityManager)context.getSystemService("activity");
        if (am != null && Build.VERSION.SDK_INT >= 21) {
            List<ActivityManager.AppTask> taskList = am.getAppTasks();
            if (taskList != null && taskList.size() > 0) {
                ((ActivityManager.AppTask)taskList.get(0)).setExcludeFromRecents(true);
            }
        }

    }

    public void setStateApplicationOnly(WaterFramework.STATE s) {
        Log.d("WaterFramework", "setStateApplicationOnly() >> state : " + s);
        setState(s);
    }

    private static void setState(WaterFramework.STATE s) {
        Log.d("WaterFramework", "setState() >> state : " + s);
        if (WaterFramework.STATE.SHUTTING_DOWN != state || state.ordinal() <= s.ordinal()) {
            if (state == s) {
                Log.d("WaterFramework", "요청된 엔진 업데이트 상태 값이 같다. : " + s);
            } else if (null == instance) {
                Log.d("WaterFramework", "현재 프래임워크 인스턴스가 null 이다.");
            } else {
                BaseBean notifyData = BackgroundStateNotify.I().getData();
                if (null != notifyData) {
                    Log.d("WaterFramework", "엔진 상태 통지 : " + s);
                    BackgroundStateNotify.I().updateState(s);
                    BackgroundStateNotify.I().notifyObservers();
                }

                Log.d("WaterFramework", "엔진 상태 변경 : " + state + " >> " + s);
                state = s;
                String key;
                WaterFramework.Event event;
                Iterator var4;
                Map.Entry elem;
                if (null != instance.eventMashMapInOut) {
                    instance.eventHashMap.clear();
                    var4 = instance.eventMashMapInOut.entrySet().iterator();

                    while(var4.hasNext()) {
                        elem = (Map.Entry)var4.next();
                        key = (String)elem.getKey();
                        event = (WaterFramework.Event)elem.getValue();
                        instance.eventHashMap.put(key, event);
                    }
                }

                if (null != instance.eventHashMap) {
                    var4 = instance.eventHashMap.entrySet().iterator();

                    while(var4.hasNext()) {
                        elem = (Map.Entry)var4.next();
                        key = (String)elem.getKey();
                        event = (WaterFramework.Event)elem.getValue();
                        if (null != event) {
                            event.state(state);
                        }
                    }
                }

            }
        }
    }

    protected void commandPush(boolean handlerCall) {
        if (null != commandPush && null != instance) {
            if (handlerCall) {
                this.handler.sendEmptyMessageDelayed(1, 0L);
            } else {
                CurvletManager.I();
                CurvletManager.process(this.getActivity(), observerPush, commandPush);
                commandPush = null;
            }
        }

    }

    public static boolean setPush(ObserverInterface observer, String command) {
        observerPush = observer;
        commandPush = command;
        boolean result = false;
        switch(getState()) {
            case FOREGROUND:
                if (null != instance) {
                    instance.commandPush(true);
                    result = false;
                }
                break;
            case INITIALIZATION:
            case BACKGROUND:
            case SHUTTING_DOWN:
            case DESTROY:
                result = true;
        }

        return result;
    }

    public static void beginExit() {
        setState(WaterFramework.STATE.SHUTTING_DOWN);
        if (null != instance) {
            instance.onCloseToken();
            onCloseUnusualToken();
        }

    }

    public static WaterFramework.STATE getState() {
        return state;
    }

    public static String getToken() {
        return token;
    }

    public static String getTokenUnusual() {
        return tokenUnusual;
    }

    public static float getDisplayStandardWidth() {
        return displayStandardWidth;
    }

    public static float getDisplayStandardHeight() {
        return displayStandardHeight;
    }

    public void create(ApplicationInterface application, WaterFramework.Listener listener, float standardWidth, float standardHeight) {
        displayStandardWidth = standardWidth;
        displayStandardHeight = standardHeight;
        if (null != instance) {
            Log.d("WaterFramework", "WaterFramework create");
            this.app = application;
            this.listener = listener;
            this.handler = new WaterFramework.WaterFrameworkHandler();
            this.onCreateToken();
            this.onInit();
        }

    }

    public String registerEvent(WaterFramework.Event event) {
        Log.d("WaterFramework", "registerEvent()");
        if (null == this.eventHashMap) {
            this.eventHashMap = new HashMap();
            this.eventMashMapInOut = new HashMap();
        }

        UUID uuid = UUID.randomUUID();
        uuid.toString();
        event.eventKey = uuid.toString();
        Log.d("WaterFramework", "registerEvent() >> synchronized");
        this.eventMashMapInOut.put(uuid.toString(), event);
        return uuid.toString();
    }

    public void unRegisterEvent(String eventKey) {
        Log.d("WaterFramework", "unRegisterEvent()");
        if (null != this.eventHashMap) {
            Log.d("WaterFramework", "unRegisterEvent() >> synchronized");
            this.eventMashMapInOut.remove(eventKey);
        }
    }

    public void unregisterEventAllClear() {
        Log.d("WaterFramework", "unregisterEventAllClear()");
        if (null != this.eventHashMap) {
            Log.d("WaterFramework", "unregisterEventAllClear() >> synchronized");
            this.eventMashMapInOut.clear();
        }
    }

    private void onInit() {
        Log.d("WaterFramework", "WaterFramework onInit");
        setState(WaterFramework.STATE.INITIALIZATION);
        if (null != this.listener) {
            this.listener.init();
        }

        BackgroundStateNotify.I();
        BackgroundStateNotify.I().setData(new BaseBean());
    }

    private void onCreateToken() {
        UUID uuid = UUID.randomUUID();
        token = uuid.toString();
    }

    private void onCloseToken() {
        token = null;
    }

    public static void onCreateUnusualToken() {
        if (null == tokenUnusual) {
            UUID uuid = UUID.randomUUID();
            tokenUnusual = uuid.toString();
        }

    }

    public static void onCloseUnusualToken() {
        tokenUnusual = null;
    }

    private void onDestroy() {
        Log.d("WaterFramework", "WaterFramework onDestroy");
        token = null;
        tokenUnusual = null;
        if (null != this.listener) {
            this.listener.destroy();
        }

        setState(WaterFramework.STATE.DESTROY);
        BackgroundStateNotify.D();
        this.app = null;
    }

    public Activity findActivity(String activityName) {
        if (null != this.activityList) {
            int nSize = this.activityList.size();

            for(int n = nSize - 1; n >= 0; --n) {
                Activity activity = (Activity)this.activityList.get(n);
                if (activity.getClass().getSimpleName().equalsIgnoreCase(activityName)) {
                    return activity;
                }
            }
        }

        return null;
    }

    public void clearActivity() {
        if (null != this.activityList) {
            int nSize = this.activityList.size();
            Log.d("WaterFramework", "WaterFramework Activity Count : " + nSize);

            for(int n = nSize - 1; n >= 0; --n) {
                ((Activity)this.activityList.get(n)).finish();
            }
        }

        this.currentActivity = null;
    }

    public void clearActivityThisActivityException(Activity thisActivityException) {
        if (null != this.activityList) {
            int nSize = this.activityList.size();
            Log.d("WaterFramework", "WaterFramework Activity Count : " + nSize);

            for(int n = nSize - 1; n >= 0; --n) {
                Activity activity = (Activity)this.activityList.get(n);
                if (activity == thisActivityException) {
                    this.currentActivity = activity;
                } else {
                    activity.finish();
                }
            }
        }

    }

    public void clearActivityThisActivityException(Class cls) {
        if (null != this.activityList) {
            int nSize = this.activityList.size();
            Log.d("WaterFramework", "WaterFramework Activity Count : " + nSize);

            for(int n = nSize - 1; n >= 0; --n) {
                Activity activity = (Activity)this.activityList.get(n);
                if (activity.getClass() == cls) {
                    this.currentActivity = activity;
                } else {
                    activity.finish();
                }
            }
        }

    }

    public void clearActivityClassList(ArrayList<Class> activityClassList) {
        if (null != this.activityList && null != activityClassList) {
            Iterator var3 = activityClassList.iterator();

            while(var3.hasNext()) {
                Class finishActivity = (Class)var3.next();
                int nSize = this.activityList.size();
                Log.d("WaterFramework", "WaterFramework Activity Count : " + nSize);

                for(int n = nSize - 1; n >= 0; --n) {
                    Activity activity = (Activity)this.activityList.get(n);
                    if (activity.getClass() == finishActivity) {
                        activity.finish();
                    }
                }
            }

            int nSize = this.activityList.size();
            if (0 < nSize) {
                this.currentActivity = (Activity)this.activityList.get(nSize - 1);
            } else {
                this.currentActivity = null;
            }
        }

    }

    public static void clearActivityUnusual() {
        try {
            if (null != activityUnusualList) {
                int size = activityUnusualList.size();
                Log.d("WaterFramework", "WaterFramework Activity Unusual 현재 수량 : " + size);

                for(int n = size - 1; n >= 0; --n) {
                    Log.d("WaterFramework", "WaterFramework Activity Unusual 현재 번호(역순) : " + n);
                    ((Activity)activityUnusualList.get(n)).finish();
                }
            }
        } catch (Exception var2) {
            Log.e("WaterFramework", "clearActivityUnusual() -> activityUnusualList finish() 호출 시 Exception 발생");
        }

    }

    public ApplicationInterface getApplication() {
        return this.app;
    }

    protected void onSave(Bundle savedInstanceState) {
        Log.d("WaterFramework", "WaterFramework onSave");
        if (null != savedInstanceState && null != this.listener) {
            this.listener.shutdown(savedInstanceState);
        }

    }

    protected void onRestore(Bundle savedInstanceState) {
        Log.d("WaterFramework", "WaterFramework onRestore");
        if (null != savedInstanceState && null != this.listener) {
            this.listener.restore(savedInstanceState);
        }

    }

    private WaterFramework.STATE isApplicationBackgroundState(Context context) {
        @SuppressLint("WrongConstant") ActivityManager am = (ActivityManager)context.getSystemService("activity");
        List<ActivityManager.RunningTaskInfo> taskInfoList = am.getRunningTasks(1);
        if (!taskInfoList.isEmpty()) {
            ComponentName topActivity = ((ActivityManager.RunningTaskInfo)taskInfoList.get(0)).topActivity;
            String tempTopActivityPackageName = topActivity.getPackageName();
            String tempContextPackageName = context.getPackageName();
            Log.d("WaterFramework", "Top Activity Package Name : " + tempTopActivityPackageName);
            Log.d("WaterFramework", "Context Package Name : " + tempContextPackageName);
            if (!topActivity.getPackageName().equals(context.getPackageName())) {
                Log.d("WaterFramework", " isApplicationBackgroundState : " + WaterFramework.STATE.BACKGROUND);
                return WaterFramework.STATE.BACKGROUND;
            }

            Log.d("WaterFramework", " isApplicationBackgroundState : .... ");
        }

        Log.d("WaterFramework", " isApplicationBackgroundState : " + WaterFramework.STATE.FOREGROUND);
        return WaterFramework.STATE.FOREGROUND;
    }

    private void updateApplicationBackgroundState(Context context, WaterFramework.STATE stateWant) {
        if (Build.VERSION.SDK_INT >= 11) {
            try {
                WaterFramework.STATE stateCheck = this.isApplicationBackgroundState(context);
                if (stateWant == stateCheck) {
                    setState(stateCheck);
                }
            } catch (Exception var4) {
                var4.printStackTrace();
                setState(stateWant);
            }
        } else {
            setState(stateWant);
        }

    }

    protected void onPause(Activity activity) {
        Log.d("WaterFramework", "WaterFramework onPause");
        this.updateApplicationBackgroundState(activity.getApplicationContext(), WaterFramework.STATE.BACKGROUND);
    }

    protected void onResume(Activity activity) {
        Log.d("WaterFramework", "WaterFramework onResume");
        this.updateApplicationBackgroundState(activity.getApplicationContext(), WaterFramework.STATE.FOREGROUND);
    }

    public void onPutActivity(Activity activity, Bundle savedInstanceState) {
        Log.d("WaterFramework", "WaterFramework onPutActivity");
        if (null == this.activityList) {
            this.activityList = new ArrayList();
            this.onRestore(savedInstanceState);
        }

        if (this.currentActivity != activity) {
            this.currentActivity = activity;
            Log.d("WaterFramework", "WaterFramework onPutActivity >> (PUSH) Activity Name : " + activity.getClass().getSimpleName());
            this.activityList.add(this.currentActivity);
            Log.d("WaterFramework", "WaterFramework onPutActivity >> (PUSH) Activity Count : " + this.activityList.size());
        }
    }

    public void onPopActivity(Activity activity) {
        Log.d("WaterFramework", "WaterFramework onPopActivity");
        if (null != this.activityList) {
            int nSize = this.activityList.size();

            for(int n = nSize - 1; n >= 0; --n) {
                Activity _activity = (Activity)this.activityList.get(n);
                if (_activity == activity) {
                    Log.d("WaterFramework", "WaterFramework onPutActivity >> (POP) Activity Name : " + activity.getClass().getSimpleName());
                    this.activityList.remove(n);
                    Log.d("WaterFramework", "WaterFramework onPopActivity >> (POP) Activity Count : " + this.activityList.size());
                    if (n > 0) {
                        this.currentActivity = (Activity)this.activityList.get(n - 1);
                    } else if (this.activityList.size() > 0) {
                        this.currentActivity = (Activity)this.activityList.get(this.activityList.size() - 1);
                    } else {
                        this.currentActivity = null;
                    }

                    return;
                }
            }
        }

    }

    public static void onPutActivityUnusual(Activity activity, Bundle savedInstanceState) {
        if (null == activityUnusualList) {
            activityUnusualList = new ArrayList();
        }

        activityUnusualList.add(activity);
    }

    public static void onPopActivityUnusual(Activity activity) {
        if (null != activityUnusualList) {
            int nSize = activityUnusualList.size();

            for(int n = nSize - 1; n >= 0; --n) {
                Activity _activity = (Activity)activityUnusualList.get(n);
                if (_activity == activity) {
                    activityUnusualList.remove(n);
                    if (0 >= activityUnusualList.size()) {
                        D();
                    }

                    return;
                }
            }
        }

    }

    /** @deprecated */
    @Deprecated
    public Activity getActivity() {
        return this.currentActivity;
    }

    static {
        state = WaterFramework.STATE.DESTROY;
        displayStandardWidth = 720.0F;
        displayStandardHeight = 1280.0F;
    }

    public interface Listener {
        void init();

        void destroy();

        void shutdown(Bundle var1);

        void restore(Bundle var1);
    }

    public abstract static class Event {
        String eventKey;
        boolean enable = false;

        public Event() {
        }

        public String getEventKey() {
            return this.eventKey;
        }

        public boolean isEnable() {
            return this.enable;
        }

        public void setEnable(boolean enable) {
            this.enable = enable;
        }

        void state(WaterFramework.STATE state) {
            Log.d("WaterFramework", "state(" + state + ")");
            Log.d("WaterFramework", "state() >> isEnable : " + this.isEnable());
            if (this.isEnable()) {
                this.onState(state);
            }
        }

        protected abstract void onState(WaterFramework.STATE var1);
    }

    @SuppressLint({"HandlerLeak"})
    protected class WaterFrameworkHandler extends Handler {
        protected WaterFrameworkHandler() {
        }

        public void handleMessage(Message msg) {
            WaterFramework.this.commandPush(false);
        }
    }

    public static enum STATE {
        INITIALIZATION,
        FOREGROUND,
        BACKGROUND,
        SHUTTING_DOWN,
        DESTROY;

        private STATE() {
        }
    }
}
