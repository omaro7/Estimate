package kr.co.goms.app.estimate.process.command;

import android.app.Activity;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

import kr.co.goms.app.estimate.MyApplication;
import kr.co.goms.app.estimate.process.ProcessGather;
import kr.co.goms.module.common.base.BaseBean;
import kr.co.goms.module.common.command.Command;
import kr.co.goms.module.common.observer.ObserverInterface;
import kr.co.goms.module.common.util.GomsLog;

public class IntroCommand  extends Command {
    private final static String TAG = IntroCommand.class.getSimpleName();

    ProcessGather processGather = null;
    int pidIntroData = 0;
    Activity activity;

    @Override
    protected BaseBean onCommand(Activity activity, Object object, BaseBean data) throws Exception {

        Log.d(TAG, ">>>>>>>>>>>>>>>>>>>>>>>>> IntroCommand 시작");

        this.activity = activity;

        onCreateProcessGather();

        /**
         * 실제 처리할 부분
         */
        initDataSet();

        return null;
    }

    /**
     * 병렬 처리 후 프로세스 통합
     */
    private void onCreateProcessGather() {
        Log.d(TAG, "onCreateProcessGather()");
        processGather = new ProcessGather();
        processGather.create();
        processGather.setOnCompleteListener(new ProcessGather.Complete() {
            @Override
            public void complete(boolean flag, int pid, Object object, int remainProcess) {
                Log.d(TAG, "onCreateProcessGather() >> complete()");
                Log.d(TAG, "onCreateProcessGather() >> complete() : flag : " + flag);
                Log.d(TAG, "onCreateProcessGather() >> complete() : remainProcess : " + remainProcess);
                if(flag) {
                    response(null);
                }
            }
        });

        //프로세스 등록
        pidIntroData = processGather.createProcess("IntroData");
    }

    private void initDataSet() {
        GomsLog.d(TAG, "initDataSet()");
        ObserverInterface dataObserver = new ObserverInterface() {
            @Override
            public void callback(BaseBean baseBean) {
                GomsLog.d(TAG, "mDataObserver  CallBack()");

                if (baseBean.getStatus() == BaseBean.STATUS.SUCCESS) {
                    finishCommand();
                } else {
                    GomsLog.d(TAG, "CallBack() : 인트로 Category Data 실패!!!!");
                    finishCommand();
                }
            }
        };

        GomsLog.d(TAG, "sendIntroData()");
        HashMap<String, Object> params = new HashMap<>();
        //SendManager.I().sendData(SendDataFactory.URL_DATA_TYPE.INTRO, params, dataObserver);

        //서버 송신없이 그냥 끝냄.
        finishCommand();
    }

    private void finishCommand(){
        //완료 처리 - > complete 호출
        processGather.finishProcess(pidIntroData);
    }

    /**
     * 다음 스텝 진행
     * @param outData
     */
    private void response(BaseBean outData) {
        BaseBean callBackData = new BaseBean();
        callBackData.setStatus(BaseBean.STATUS.SUCCESS, "");
        callback(callBackData);
    }

}
