package kr.co.goms.module.common.handler;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

/**
 * CommonHandler
     msg = CommonHandlerMessage.setMessage(CommonHandlerConstants.WATER_START, callbackObj);
     sendMessage(msg);
 */
public class CommonHandler extends Handler {

    private static final String TAG = CommonHandler.class.getSimpleName();
    private Context mContext;

    CommonHandler(Context context, Looper looper) {
        super(looper);
        mContext = context;
    }

    public void handleMessage(Message msg) {
        CommonHandlerMessage msgObj = (CommonHandlerMessage) msg.obj;
        try {
            switch (msg.what) {
                case CommonHandlerConstants.WATER_START: {
                    Object object = msgObj.obj;
                    Object callbackObj = msgObj.callbackObj;
                    //process 진행 할 것
                }
                break;

                case CommonHandlerConstants.WATER_INIT: {
                    ICommonCallback callback = (ICommonCallback) msgObj.callbackObj;
                    //process 진행 할 것
                }
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e.getCause());
        }
    }
}
