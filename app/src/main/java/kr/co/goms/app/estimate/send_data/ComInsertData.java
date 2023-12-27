package kr.co.goms.app.estimate.send_data;

import java.util.ArrayList;

import kr.co.goms.app.estimate.MyApplication;
import kr.co.goms.app.estimate.manager.GsonManager;
import kr.co.goms.app.estimate.model.CompanyBeanTB;
import kr.co.goms.module.common.base.BaseBean;
import kr.co.goms.module.common.task.RequestItem;
import kr.co.goms.module.common.util.GomsLog;

/**
 * 나의 회사정보 저장하기
 */
public class ComInsertData extends LocalData implements ISendData{
    private final String TAG = ComInsertData.class.getSimpleName();
    @Override
    public void onSendData() {
        GomsLog.d(TAG, ComInsertData.class.getSimpleName() + " >>>> ComInsertData Data()");

        mParserType = GsonManager.PARSER_TYPE.item;
        RequestItem requestItem = new RequestItem();
        requestItem.bodyMap = mParam;

        CompanyBeanTB companyBeanTB = new CompanyBeanTB();
        companyBeanTB.setCom_name((String)mParam.get("cliName"));
        companyBeanTB.setCom_biz_num((String)mParam.get("cliBizNum"));
        companyBeanTB.setCom_ceo_name((String)mParam.get("cliCeoName"));
        companyBeanTB.setCom_uptae((String)mParam.get("cliUpTae"));
        companyBeanTB.setCom_upjong((String)mParam.get("cliUpJung"));
        companyBeanTB.setCom_tel_num((String)mParam.get("cliTelNum"));
        companyBeanTB.setCom_fax_num((String)mParam.get("cliFaxNum"));
        companyBeanTB.setCom_hp_num((String)mParam.get("cliHpNum"));
        companyBeanTB.setCom_manager_name((String)mParam.get("cliManagerName"));
        companyBeanTB.setCom_email((String)mParam.get("cliEmail"));
        companyBeanTB.setCom_zipcode((String)mParam.get("cliZipCode"));
        companyBeanTB.setCom_address_01((String)mParam.get("cliAddress01"));
        companyBeanTB.setCom_address_02((String)mParam.get("cliAddress02"));

        ArrayList<CompanyBeanTB> companyList = MyApplication.getInstance().getDBHelper().insertCompany(companyBeanTB);
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
