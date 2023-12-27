package kr.co.goms.app.estimate.send_data;

import java.util.ArrayList;

import kr.co.goms.app.estimate.MyApplication;
import kr.co.goms.app.estimate.manager.GsonManager;
import kr.co.goms.app.estimate.model.CompanyBeanTB;
import kr.co.goms.module.common.base.BaseBean;
import kr.co.goms.module.common.task.RequestItem;
import kr.co.goms.module.common.util.GomsLog;

/**
 * 나의 회사정보 삭제하기
 */
public class ComDeleteData extends LocalData implements ISendData{
    private final String TAG = ComDeleteData.class.getSimpleName();
    @Override
    public void onSendData() {
        GomsLog.d(TAG, ComDeleteData.class.getSimpleName() + " >>>> ComDeleteData Data()");

        mParserType = GsonManager.PARSER_TYPE.item;
        RequestItem requestItem = new RequestItem();
        requestItem.bodyMap = mParam;

        String comIdx = (String)mParam.get("comIdx");

        ArrayList<CompanyBeanTB> companyList = MyApplication.getInstance().getDBHelper().deleteCompanyData(comIdx);
        successData(companyList);

    }

    private void successData(Object object){
        GomsLog.d(TAG, "successData() 성공");
        BaseBean baseBean = new BaseBean();
        baseBean.setObject(object);
        baseBean.setStatus(BaseBean.STATUS.SUCCESS);
        mObserver.callback(baseBean);
    }

    private void failData(String jsonString){
        GomsLog.d(TAG, "failData() 실패");
        BaseBean baseBean = new BaseBean();
        baseBean.setStatus(BaseBean.STATUS.FAIL);
        baseBean.setObject(jsonString);
        mObserver.callback(baseBean);
    }
}
