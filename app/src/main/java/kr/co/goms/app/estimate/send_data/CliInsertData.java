package kr.co.goms.app.estimate.send_data;

import java.util.ArrayList;

import kr.co.goms.app.estimate.MyApplication;
import kr.co.goms.app.estimate.manager.GsonManager;
import kr.co.goms.app.estimate.model.ClientBeanTB;
import kr.co.goms.module.common.base.BaseBean;
import kr.co.goms.module.common.task.RequestItem;
import kr.co.goms.module.common.util.GomsLog;

/**
 * 거래처 저장하기
 */
public class CliInsertData extends LocalData implements ISendData{
    private final String TAG = CliInsertData.class.getSimpleName();
    @Override
    public void onSendData() {
        GomsLog.d(TAG, CliInsertData.class.getSimpleName() + " >>>> ItemList Data()");

        mParserType = GsonManager.PARSER_TYPE.item;
        RequestItem requestItem = new RequestItem();
        requestItem.bodyMap = mParam;

        ClientBeanTB clientBeanTB = new ClientBeanTB();
        clientBeanTB.setCli_name((String)mParam.get("cliName"));
        clientBeanTB.setCli_biz_num((String)mParam.get("cliBizNum"));
        clientBeanTB.setCli_ceo_name((String)mParam.get("cliCeoName"));
        clientBeanTB.setCli_uptae((String)mParam.get("cliUpTae"));
        clientBeanTB.setCli_upjong((String)mParam.get("cliUpJung"));
        clientBeanTB.setCli_tel_num((String)mParam.get("cliTelNum"));
        clientBeanTB.setCli_fax_num((String)mParam.get("cliFaxNum"));
        clientBeanTB.setCli_hp_num((String)mParam.get("cliHpNum"));
        clientBeanTB.setCli_manager_name((String)mParam.get("cliManagerName"));
        clientBeanTB.setCli_email((String)mParam.get("cliEmail"));
        clientBeanTB.setCli_zipcode((String)mParam.get("cliZipCode"));
        clientBeanTB.setCli_address_01((String)mParam.get("cliAddress01"));
        clientBeanTB.setCli_address_02((String)mParam.get("cliAddress02"));
        clientBeanTB.setCli_remark((String)mParam.get("cliRemark"));

        ArrayList<ClientBeanTB> clientList = MyApplication.getInstance().getDBHelper().insertClient(clientBeanTB);
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
