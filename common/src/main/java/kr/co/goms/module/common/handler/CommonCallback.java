package kr.co.goms.module.common.handler;

import android.os.RemoteException;

public abstract class CommonCallback implements CommonHandlerConnection {
    private CommonCallback ccb = this;
    private CommonCallback.WaterCommonCallback waterCommonCb = new CommonCallback.WaterCommonCallback();

    public CommonCallback() {
    }

    ICommonCallback getICallback() {
        return this.waterCommonCb;
    }

    public abstract void onSuccess(String var1);

    public abstract void onFail(String var1, int var2);

    private class WaterCommonCallback extends ICommonCallback.Stub {
        private WaterCommonCallback() {
        }

        public void onSuccess(String id) throws RemoteException {
            CommonHandlerFramework.mTrackHash.remove(CommonCallback.this.ccb);
            CommonCallback.this.ccb.onSuccess(id);
        }

        public void onFail(String id, int errorCode) throws RemoteException {
            CommonHandlerFramework.mTrackHash.remove(CommonCallback.this.ccb);
            CommonCallback.this.ccb.onFail(id, errorCode);
        }
    }
}