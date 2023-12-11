package kr.co.goms.module.common.handler;

import android.os.RemoteException;

public abstract class SampleCallback implements CommonHandlerConnection {
    private SampleCallback ccb = this;
    private SampleCallback.WaterSampleCallback waterSampleCb = new SampleCallback.WaterSampleCallback();

    public SampleCallback() {
    }

    ISampleCallback getICallback() {
        return this.waterSampleCb;
    }

    public abstract void onSuccess(SampleResult var1);

    public abstract void onFail(int var1, SampleResult sampleResult);

    private class WaterSampleCallback extends ISampleCallback.Stub {
        private WaterSampleCallback() {
        }

        public void onSuccess(SampleResult sampleResult) throws RemoteException {
            CommonHandlerFramework.mTrackHash.remove(SampleCallback.this.ccb);
            SampleCallback.this.ccb.onSuccess(sampleResult);
        }

        public void onFail(int errorCode, SampleResult sampleResult) throws RemoteException {
            CommonHandlerFramework.mTrackHash.remove(SampleCallback.this.ccb);
            SampleCallback.this.ccb.onFail(errorCode, sampleResult);
        }
    }
}