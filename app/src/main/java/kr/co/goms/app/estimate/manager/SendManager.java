package kr.co.goms.app.estimate.manager;

import android.util.ArrayMap;

import java.io.File;
import java.util.HashMap;

import kr.co.goms.app.estimate.send_data.LocalData;
import kr.co.goms.app.estimate.send_data.SendData;
import kr.co.goms.app.estimate.send_data.SendDataFactory;
import kr.co.goms.module.common.curvlet.CurvletManager;
import kr.co.goms.module.common.observer.ObserverInterface;
import kr.co.goms.module.common.util.GomsLog;

/**
 * 전송 매니져
 */
public class SendManager {

    private final static String TAG = SendManager.class.getSimpleName();

    static SendManager mSendManager;    //instance

    public static REPOSITORY_TYPE mRepositoryType = REPOSITORY_TYPE.LOCAL;
    static enum REPOSITORY_TYPE{
        SERVER,
        LOCAL,
    }

    public SendManager() {

    }

    public static SendManager I(){
        if(mSendManager == null){
            mSendManager = new SendManager();
        }
        return mSendManager;
    }

    public void destroy() {

    }

    /**
     * 서버 전송 처리
     * @param dataType DATA_TYPE
     * @param param 전달 param
     * @param observer  결과 observer
     */
    public void sendData(SendDataFactory.DATA_TYPE dataType, HashMap<String, Object> param, ObserverInterface observer){
        GomsLog.d(TAG, "sendData()");
        SendDataFactory sendDataFactory = new SendDataFactory();
        Object sendData = sendDataFactory.createSendData(dataType);

        if (sendData instanceof LocalData) {
            LocalData localData = (LocalData) sendData;
            localData.setParam(param);
            localData.setObserver(observer);
            localData.onSendData();
        } else {
            SendData localData = (SendData) sendData;
            localData.setParam(param);
            localData.setObserver(observer);
            localData.onSendData();
        }

    }

    public void sendData(SendDataFactory.DATA_TYPE dataType, HashMap<String, Object> param, ArrayMap<Integer, File> fileParam, ObserverInterface observer){
        GomsLog.d(TAG, "sendData()");
        SendDataFactory sendDataFactory = new SendDataFactory();
        SendData sendData = (SendData)sendDataFactory.createSendData(dataType);

        sendData.setParam(param);
        sendData.setFileParam(fileParam);
        sendData.setObserver(observer);
        sendData.onSendData();
    }

}
