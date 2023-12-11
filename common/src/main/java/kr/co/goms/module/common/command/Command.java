package kr.co.goms.module.common.command;

import android.app.Activity;
import android.util.Log;

import kr.co.goms.module.common.base.BaseBean;
import kr.co.goms.module.common.base.WaterCallBack;

public abstract class Command {
    static final String LOG_TAG = Command.class.getSimpleName();
    private boolean threadFlag = false;
    protected WaterCallBack mWaterCallBack = null;
    private BaseBean data = null;

    public Command() {
    }

    public void setThreadFlag(boolean threadFlag) {
        this.threadFlag = threadFlag;
    }

    public void command(final Activity activity, final Object obj) {
        if (this.threadFlag) {
            Thread thread = new Thread(new Runnable() {
                public void run() {
                    Command.this.onProcess(activity, obj);
                }
            });
            thread.setDaemon(true);
            thread.start();
        } else {
            this.onProcess(activity, obj);
        }

    }

    private void onProcess(Activity activity, Object obj) {
        try {
            this.data = this.onCommand(activity, obj, this.data);
        } catch (Exception var4) {
            var4.printStackTrace();
        }

        this.callback(this.data);
    }

    protected abstract BaseBean onCommand(Activity var1, Object var2, BaseBean var3) throws Exception;

    protected void callback(BaseBean data) {
        Log.d(LOG_TAG, "update()");
        if (null != this.mWaterCallBack) {
            try {
                if (null != data) {
                    this.mWaterCallBack.callback(data);
                } else {
                    Log.d(LOG_TAG, "update() >> data null");
                }
            } catch (Exception var3) {
                var3.printStackTrace();
            }
        }

    }

    public void setCallBack(WaterCallBack waterCallBack) {
        this.mWaterCallBack = waterCallBack;
    }

    public void setBaseBean(BaseBean data) {
        this.data = data;
    }

    public BaseBean getBaseBean() {
        return this.data;
    }
}