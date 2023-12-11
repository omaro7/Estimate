package kr.co.goms.module.common.handler;

import android.content.Context;
import android.os.HandlerThread;
import android.os.Message;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class CommonHandlerFramework {

    public static HashMap<Object, Integer> mTrackHash = new HashMap();

    private static Context mContext;
    private static CommonHandler mHandler;

    private void notifyFramework() {
        if (mTrackHash != null && !mTrackHash.isEmpty()) {
            Iterator var1 = mTrackHash.entrySet().iterator();

            while(var1.hasNext()) {
                Map.Entry<Object, Integer> entry = (Map.Entry)var1.next();
                Object cb = entry.getKey();
                Integer key = (Integer)entry.getValue();
                switch((Integer)entry.getValue()) {
                    case 1:
                        ((SampleCallback)cb).onFail(-2, (SampleResult)null);
                    case 2:
                        ((CommonCallback)cb).onFail((String)null, -2);
                    default:
                        break;
                }
            }

            mTrackHash.clear();
        }
    }

    /**
        handleMsg = CommonMessage.obtainMessage(CommonHandlerConstants.WATER_START, callbackObj);
        sendMessage(handleMsg);
     * @param m
     */
    public static synchronized void sendMessage(Message m) {

        if (mHandler == null) {
            HandlerThread handlerThread = new HandlerThread(CommonHandlerFramework.class.getSimpleName());
            handlerThread.start();
            mHandler = new CommonHandler(mContext, handlerThread.getLooper());
        }
        else {
            mHandler.sendMessage(m);
        }
    }

}
