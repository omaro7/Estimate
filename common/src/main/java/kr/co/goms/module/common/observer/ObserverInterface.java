package kr.co.goms.module.common.observer;

import kr.co.goms.module.common.base.BaseBean;
import kr.co.goms.module.common.base.WaterCallBack;

public interface ObserverInterface  extends WaterCallBack {
    void callback(BaseBean var1);
}
