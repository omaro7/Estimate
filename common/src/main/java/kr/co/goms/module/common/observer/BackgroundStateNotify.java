package kr.co.goms.module.common.observer;

import kr.co.goms.module.common.WaterFramework;
import kr.co.goms.module.common.base.BaseBean;

public class BackgroundStateNotify extends Subject {
    private static BackgroundStateNotify instance = null;
    private String background = "onBackground";
    private String foreground = "onForeground";

    private BackgroundStateNotify() {
    }

    public static synchronized BackgroundStateNotify I() {
        if (null == instance) {
            instance = new BackgroundStateNotify();
            instance.create();
        }

        return instance;
    }

    public static synchronized void D() {
        if (null != instance) {
            instance.destroy();
            instance = null;
        }

    }

    private void create() {
    }

    private void destroy() {
    }

    public void updateState(WaterFramework.STATE state) {
        switch(state) {
            case BACKGROUND:
                this.data.setStatus(BaseBean.STATUS.SUCCESS, "javascript:" + this.background + "()");
                break;
            case FOREGROUND:
                this.data.setStatus(BaseBean.STATUS.SUCCESS, "javascript:" + this.foreground + "()");
        }

    }

    public void setBackground(String background) {
        this.background = background;
    }

    public void setForeground(String foreground) {
        this.foreground = foreground;
    }
}
