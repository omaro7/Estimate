package kr.co.goms.app.estimate.send_data;

import java.util.ArrayList;

import kr.co.goms.app.estimate.MyApplication;
import kr.co.goms.app.estimate.manager.GsonManager;
import kr.co.goms.app.estimate.model.CompanyBeanTB;
import kr.co.goms.module.common.base.BaseBean;
import kr.co.goms.module.common.task.RequestItem;
import kr.co.goms.module.common.util.GomsLog;

/**
 * 나의 회사정보 업데이트하기
 */
public class ComUpdateData extends LocalData implements ISendData{
    private final String TAG = ComUpdateData.class.getSimpleName();
    @Override
    public void onSendData() {
        GomsLog.d(TAG, ComUpdateData.class.getSimpleName() + " >>>> ComUpdateData Data()");

        mParserType = GsonManager.PARSER_TYPE.item;
        RequestItem requestItem = new RequestItem();
        requestItem.bodyMap = mParam;

        CompanyBeanTB companyBeanTB = new CompanyBeanTB();
        companyBeanTB.setCom_idx((String)mParam.get("comIdx"));
        companyBeanTB.setCom_name((String)mParam.get("comName"));
        companyBeanTB.setCom_biz_num((String)mParam.get("comBizNum"));
        companyBeanTB.setCom_ceo_name((String)mParam.get("comCeoName"));
        companyBeanTB.setCom_uptae((String)mParam.get("comUpTae"));
        companyBeanTB.setCom_upjong((String)mParam.get("comUpJong"));
        companyBeanTB.setCom_tel_num((String)mParam.get("comTelNum"));
        companyBeanTB.setCom_fax_num((String)mParam.get("comFaxNum"));
        companyBeanTB.setCom_hp_num((String)mParam.get("comHpNum"));
        companyBeanTB.setCom_manager_name((String)mParam.get("comManagerName"));
        companyBeanTB.setCom_email((String)mParam.get("comEmail"));
        companyBeanTB.setCom_zipcode((String)mParam.get("comZipCode"));
        companyBeanTB.setCom_address_01((String)mParam.get("comAddress01"));
        companyBeanTB.setCom_address_02((String)mParam.get("comAddress02"));
        companyBeanTB.setCom_account_num((String)mParam.get("comAccountNum"));
        companyBeanTB.setCom_stamp_path((String)mParam.get("comStampPath"));
        companyBeanTB.setCom_logo_path((String)mParam.get("comLogoPath"));
        companyBeanTB.setCom_main_yn((String)mParam.get("comMainYN"));

        ArrayList<CompanyBeanTB> companyList = MyApplication.getInstance().getDBHelper().updateCompany(companyBeanTB);
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
