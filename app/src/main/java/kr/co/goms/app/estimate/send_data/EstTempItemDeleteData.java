package kr.co.goms.app.estimate.send_data;

import java.util.ArrayList;

import kr.co.goms.app.estimate.MyApplication;
import kr.co.goms.app.estimate.manager.GsonManager;
import kr.co.goms.app.estimate.model.ItemBeanTB;
import kr.co.goms.module.common.base.BaseBean;
import kr.co.goms.module.common.task.RequestItem;
import kr.co.goms.module.common.util.GomsLog;

/**
 * 견적서 > 임시 아이템 삭제하기
 */
public class EstTempItemDeleteData extends LocalData implements ISendData{
    private final String TAG = EstTempItemDeleteData.class.getSimpleName();
    @Override
    public void onSendData() {
        GomsLog.d(TAG, EstTempItemDeleteData.class.getSimpleName() + " >>>> ItemList Data()");

        mParserType = GsonManager.PARSER_TYPE.item;
        RequestItem requestItem = new RequestItem();
        requestItem.bodyMap = mParam;

        String itemIdx = (String)mParam.get("itemIdx");
        String itemToken = (String)mParam.get("itemToken");

        ArrayList<ItemBeanTB> itemList = MyApplication.getInstance().getDBHelper().deleteTempItemData(itemIdx, itemToken);
        successData(itemList);

    }

    private void successData(Object object){
        GomsLog.d(TAG, "successData() 성공");
        BaseBean baseBean = new BaseBean();
        if(object != null) {
            baseBean.setObject(object);
        }
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
