package kr.co.goms.module.common.manager;

import android.util.Log;

import java.util.HashMap;
import java.util.Iterator;

import kr.co.goms.module.common.base.BaseBean;
import kr.co.goms.module.common.observer.ObserverInterface;

public class ObserverManager {

    static final String LOG_TAG = kr.co.goms.module.common.manager.ObserverManager.class.getSimpleName();

    static kr.co.goms.module.common.manager.ObserverManager instance;
    private HashMap<Class, ObserverInterface> mObserverInterfaceHashMap = new HashMap<Class, ObserverInterface>();

    protected ObserverManager() {

    }

    protected void onDestroy() {

    }

    public static kr.co.goms.module.common.manager.ObserverManager I() {
        if(null == instance) {
            instance = new kr.co.goms.module.common.manager.ObserverManager();
        }

        return instance;
    }

    public static void D() {
        if(null != instance) {
            instance.onDestroy();
            instance = null;
        }
    }

    public boolean register(Class modelClass, ObserverInterface observer){

        if(null == observer) {
            Log.d(LOG_TAG, "register() >> 옵저버가 null 임");
            return false;
        }

        if(null == mObserverInterfaceHashMap) {
            mObserverInterfaceHashMap =  new HashMap<Class, ObserverInterface>();
        }

        for(int n = 0 ; n < mObserverInterfaceHashMap.size() ; ++n) {
            if(observer == mObserverInterfaceHashMap.get(n)) {
                Log.d(LOG_TAG, "register() >> 이미 등록 된 옵저버");
                return false;
            }
        }

        mObserverInterfaceHashMap.put(modelClass, observer);
        return true;
    }

    public boolean unRegister(ObserverInterface observer) {
        Log.d(LOG_TAG, "unRegister()");

        if(null == observer) {
            Log.d(LOG_TAG, "unRegister() >> 옵저버가 null 임");
            return false;
        }

        if(null == mObserverInterfaceHashMap) {
            Log.d(LOG_TAG, "unRegister() >> 목록이 null 임");
            return false;
        }

        for(int n = 0 ; n < mObserverInterfaceHashMap.size() ; ++n) {
            if(observer == mObserverInterfaceHashMap.get(n)) {
                Log.d(LOG_TAG, "unRegister() >> 삭제할 옵저버를 찾음");
                mObserverInterfaceHashMap.remove(observer);
                return true;
            }
        }

        Log.d(LOG_TAG, "unRegister() >> 삭제할 옵저버를 못찾음");

        return false;
    }

    public void command(Class modelClass, BaseBean baseBean){

        Iterator<?> keys = mObserverInterfaceHashMap.keySet().iterator();
        while (keys.hasNext()) {
            Class key = (Class) keys.next();
            if (key.equals(modelClass)) {
                mObserverInterfaceHashMap.get(key).callback(baseBean);
            }
        }

        Log.d(LOG_TAG, "onResponse() >> 콜백 처리 완료");
    }
}
