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
 * 견적서 수정
 */
public class EstUpdateData extends LocalData implements ISendData{
    private final String TAG = EstUpdateData.class.getSimpleName();
    @Override
    public void onSendData() {
        GomsLog.d(TAG, EstUpdateData.class.getSimpleName() + " >>>> EstUpdateData Data()");

        mParserType = GsonManager.PARSER_TYPE.item;
        RequestItem requestItem = new RequestItem();
        requestItem.bodyMap = mParam;

        EstimateBeanTB estimateBeanTB = (EstimateBeanTB)mParam.get("estimateBeanTB");
        ArrayList<ItemBeanTB> itemList = (ArrayList<ItemBeanTB>)mParam.get("estimateItemList");

        ArrayList<EstimateBeanTB> clientList = MyApplication.getInstance().getDBHelper().updateEstimate(estimateBeanTB, itemList);
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
