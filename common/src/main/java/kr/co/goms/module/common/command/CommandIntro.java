package kr.co.goms.module.common.command;

import android.app.Activity;
import android.net.Uri;

import kr.co.goms.module.common.base.BaseBean;

public class CommandIntro extends Command {

    static final String LOG_TAG = CommandIntro.class.getSimpleName();

    @Override
    protected BaseBean onCommand(Activity activity, Object o, BaseBean baseData) throws Exception {
        Uri uri = (Uri)o;
        onFirstNetworkRequest(activity);
        return null;
    }

    /**
     * 첫 앱 초기세팅 호출
     * @param activity
     */
    private void onFirstNetworkRequest(Activity activity) {

    }
}
