package kr.co.goms.app.estimate.send_data;

import java.util.ArrayList;

import kr.co.goms.app.estimate.MyApplication;
import kr.co.goms.app.estimate.manager.GsonManager;
import kr.co.goms.app.estimate.model.EstimateBeanTB;
import kr.co.goms.app.estimate.model.ItemBeanTB;
import kr.co.goms.module.common.base.BaseBean;
import kr.co.goms.module.common.task.RequestItem;
import kr.co.goms.module.common.util.GomsLog;

/**
 * 견적서 저장하기
 */
public class EstInsertData extends LocalData implements ISendData{
    private final String TAG = EstInsertData.class.getSimpleName();
    @Override
    public void onSendData() {
        GomsLog.d(TAG, EstInsertData.class.getSimpleName() + " >>>> EstInsertData Data()");

        mParserType = GsonManager.PARSER_TYPE.item;
        RequestItem requestItem = new RequestItem();
        requestItem.bodyMap = mParam;

        EstimateBeanTB estimateBeanTB = (EstimateBeanTB)mParam.get("estimateBeanTB");
        ArrayList<ItemBeanTB> itemList = (ArrayList<ItemBeanTB>)mParam.get("estimateItemList");

        /*
        EstimateBeanTB estimateBeanTB = new EstimateBeanTB();
        estimateBeanTB.setCom_idx(mParam.get("comIdx"));                        //회사키
        estimateBeanTB.setCli_idx(mParam.get("cliIdx"));                        //거래처키
        estimateBeanTB.setEst_date(mParam.get("estDate"));                      //견적날짜
        estimateBeanTB.setEst_effective_date(mParam.get("estEffectiveDate"));   //유효일자
        estimateBeanTB.setEst_delivery_date(mParam.get("estDeliveryDate"));     //납기일자
        estimateBeanTB.setEst_payment_condition(mParam.get("estPaymentCondition")); //결제조건
        estimateBeanTB.setEst_delivery_location(mParam.get("estDeliveryLocation")); //배송장소
        estimateBeanTB.setEst_num(mParam.get("estNum"));                        //견적번호
        estimateBeanTB.setEst_cli_name(mParam.get("estCliName"));               //거래처이름
        estimateBeanTB.setEst_cli_tel(mParam.get("estCliTel"));                 //거래처 전화번호
        estimateBeanTB.setEst_cli_fax(mParam.get("estCliFax"));                 //거래처 팩스번호
        estimateBeanTB.setEst_cli_zipcode(mParam.get("estCliZipCode"));         //거래처 우편번호
        estimateBeanTB.setEst_cli_address_01(mParam.get("estCliAddress01"));    //거래처 주소
        estimateBeanTB.setEst_cli_address_02(mParam.get("estCliAddress02"));    //거래처 주소
        estimateBeanTB.setEst_cli_manager_name(mParam.get("estCliManagerName"));//거래처 담당자명
        estimateBeanTB.setEst_cli_remark(mParam.get("estCliRemark"));           //거래처 비고
        estimateBeanTB.setEst_com_name(mParam.get("estComName"));               //회사명
        estimateBeanTB.setEst_com_ceo_name(mParam.get("estComCeoName"));        //회사대표자명
        estimateBeanTB.setEst_com_biz_num(mParam.get("estComBizNum"));          //회사사업자번호
        estimateBeanTB.setEst_com_email(mParam.get("estComEmail"));             //회사이메일
        estimateBeanTB.setEst_com_zipcode(mParam.get("estComZipCode"));         //회사우편번호
        estimateBeanTB.setEst_com_address_01(mParam.get("estComAddress01"));    //회사주소
        estimateBeanTB.setEst_com_address_02(mParam.get("estComAddress02"));    //회사주소
        estimateBeanTB.setEst_tax_type(mParam.get("estTaxType"));               //세금타입
        estimateBeanTB.setEst_total_price(mParam.get("estTotalPrice"));         //전체금액
        estimateBeanTB.setEst_remark(mParam.get("estRemark"));                  //견적비고
        estimateBeanTB.setEst_regdate(mParam.get("estRegDate"));                //견적생성일자
        */

        ArrayList<EstimateBeanTB> clientList = MyApplication.getInstance().getDBHelper().insertEstimate(estimateBeanTB,itemList);
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
