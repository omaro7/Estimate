package kr.co.goms.app.estimate.send_data;

import java.util.ArrayList;

import kr.co.goms.app.estimate.MyApplication;
import kr.co.goms.app.estimate.manager.GsonManager;
import kr.co.goms.app.estimate.model.ClientBeanTB;
import kr.co.goms.app.estimate.model.CompanyBeanTB;
import kr.co.goms.module.common.base.BaseBean;
import kr.co.goms.module.common.task.RequestItem;
import kr.co.goms.module.common.util.GomsLog;

/**
 * 거래처 정보 업데이트하기
 */
public class CliUpdateData extends LocalData implements ISendData{
    private final String TAG = CliUpdateData.class.getSimpleName();
    @Override
    public void onSendData() {
        GomsLog.d(TAG, CliUpdateData.class.getSimpleName() + " >>>> CliUpdateData Data()");

        mParserType = GsonManager.PARSER_TYPE.item;
        RequestItem requestItem = new RequestItem();
        requestItem.bodyMap = mParam;

        ClientBeanTB clientBeanTB = new ClientBeanTB();
        clientBeanTB.setCli_idx((String)mParam.get("cliIdx"));
        clientBeanTB.setCli_name((String)mParam.get("cliName"));
        clientBeanTB.setCli_biz_num((String)mParam.get("cliBizNum"));
        clientBeanTB.setCli_ceo_name((String)mParam.get("cliCeoName"));
        clientBeanTB.setCli_uptae((String)mParam.get("cliUpTae"));
        clientBeanTB.setCli_upjong((String)mParam.get("cliUpJong"));
        clientBeanTB.setCli_tel_num((String)mParam.get("cliTelNum"));
        clientBeanTB.setCli_fax_num((String)mParam.get("cliFaxNum"));
        clientBeanTB.setCli_hp_num((String)mParam.get("cliHpNum"));
        clientBeanTB.setCli_manager_name((String)mParam.get("cliManagerName"));
        clientBeanTB.setCli_email((String)mParam.get("cliEmail"));
        clientBeanTB.setCli_zipcode((String)mParam.get("cliZipCode"));
        clientBeanTB.setCli_address_01((String)mParam.get("cliAddress01"));
        clientBeanTB.setCli_address_02((String)mParam.get("cliAddress02"));
        clientBeanTB.setCli_main_yn((String)mParam.get("cliMainYN"));

        ArrayList<ClientBeanTB> clientList = MyApplication.getInstance().getDBHelper().updateClient(clientBeanTB);
        successData(clientList);

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
