package kr.co.goms.app.estimate.manager;

import android.content.Context;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;

import java.io.IOException;
import java.util.concurrent.Callable;

import kr.co.goms.app.estimate.MyApplication;

public class AdIdHelper {

    public static AdIdHelper instance;
    public String mAdid;
    public AdIdHelper() {
    }

    public static AdIdHelper I(){
        if(instance == null){
            instance = new AdIdHelper();
        }
        return instance;
    }

    public void getAdvertisingId(Context context, OnAdIDListener onAdIDListener) {
        //AD ID인데, 일단 값이 없음.
        MyApplication.getInstance().getExecutorService().execute(new AdIDTask(context, onAdIDListener));
    }

    public void setADId(String adid) {
        this.mAdid = adid;
    }

    class AdIDTask implements Runnable {
        private Context context;
        private OnAdIDListener onAdIDListener;
        public AdIDTask(Context context, OnAdIDListener onAdIDListener) {
            this.context = context;
            this.onAdIDListener = onAdIDListener;
        }

        @Override
        public void run() {
            // Task logic
            AdvertisingIdClient.Info adInfo = null;
            try {
                adInfo = AdvertisingIdClient.getAdvertisingIdInfo(context);
            } catch (GooglePlayServicesNotAvailableException |
                     GooglePlayServicesRepairableException | IOException e) {
                e.printStackTrace();
            }
            String strAdId = "";
            try{
                strAdId = adInfo.getId();
            }catch (NullPointerException e){
                e.printStackTrace();
            }
            onAdIDListener.onComplete(strAdId);
        }
    }

    class AdIDTaskCall implements Callable<String> {

        Context context;
        public AdIDTaskCall(Context context) {
            this.context = context;
        }

        @Override
        public String call() {
            // Task logic
            AdvertisingIdClient.Info adInfo = null;
            try {
                adInfo = AdvertisingIdClient.getAdvertisingIdInfo(context);
            } catch (GooglePlayServicesNotAvailableException |
                     GooglePlayServicesRepairableException | IOException e) {
                e.printStackTrace();
            }
            String strAdId = "";
            try{
                strAdId = adInfo.getId();
            }catch (NullPointerException e){
                e.printStackTrace();
            }
            return strAdId;
        }
    }

    public String getAdID() {
        return mAdid;
    }

    OnAdIDListener mOnAdIDListener;
    public interface OnAdIDListener{
        void onComplete(String adid);
    }
}