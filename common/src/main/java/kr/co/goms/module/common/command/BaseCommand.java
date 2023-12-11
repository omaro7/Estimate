package kr.co.goms.module.common.command;

import android.app.Activity;

import kr.co.goms.module.common.base.BaseBean;
import kr.co.goms.module.common.base.WaterCallBack;


/**
 * Base Bottom Popup에 대한 Command 재정의
 */
public class BaseCommand extends Command {

    public BaseCommand(BaseBean.STATUS status, String message, WaterCallBack callback) {
        super();
        BaseBean baseData = new BaseBean();
        baseData.setStatus(status, message);
        setBaseBean(baseData);
        setCallBack(callback);
    }

    @Override
    protected BaseBean onCommand(Activity activity, Object o, BaseBean baseData) throws Exception {
        return getBaseBean();
    }
}
