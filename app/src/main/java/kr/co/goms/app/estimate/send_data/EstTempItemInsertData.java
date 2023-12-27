package kr.co.goms.app.estimate.send_data;

import java.util.ArrayList;

import kr.co.goms.app.estimate.MyApplication;
import kr.co.goms.app.estimate.manager.GsonManager;
import kr.co.goms.app.estimate.model.ItemBeanTB;
import kr.co.goms.module.common.base.BaseBean;
import kr.co.goms.module.common.task.RequestItem;
import kr.co.goms.module.common.util.FormatUtil;
import kr.co.goms.module.common.util.GomsLog;
import kr.co.goms.module.common.util.StringUtil;

/**
 * 견적서 > 임시 아이템 저장하기
 */
public class EstTempItemInsertData extends LocalData implements ISendData{
    private final String TAG = EstTempItemInsertData.class.getSimpleName();
    @Override
    public void onSendData() {
        GomsLog.d(TAG, EstTempItemInsertData.class.getSimpleName() + " >>>> ItemList Data()");

        mParserType = GsonManager.PARSER_TYPE.item;
        RequestItem requestItem = new RequestItem();
        requestItem.bodyMap = mParam;

        ItemBeanTB itemBeanTB = new ItemBeanTB();
        itemBeanTB.setItem_token((String)mParam.get("itemToken"));
        itemBeanTB.setItem_name((String)mParam.get("itemName"));
        itemBeanTB.setItem_std((String)mParam.get("itemStd"));
        itemBeanTB.setItem_unit((String)mParam.get("itemUnit"));
        itemBeanTB.setItem_quantity((String)mParam.get("itemQuantity"));
        itemBeanTB.setItem_unit_price((String)mParam.get("itemUnitPrice"));
        itemBeanTB.setItem_price((String)mParam.get("itemPrice"));
        itemBeanTB.setItem_tax_price((String)mParam.get("itemTaxPrice"));
        itemBeanTB.setItem_total_price((String)mParam.get("itemTotalPrice"));
        itemBeanTB.setItem_remark((String)mParam.get("itemRemark"));

        ArrayList<ItemBeanTB> itemList = MyApplication.getInstance().getDBHelper().insertTempEstimateItem(itemBeanTB);
        successData(itemList);

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
