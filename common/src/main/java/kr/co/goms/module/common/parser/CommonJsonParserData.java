package kr.co.goms.module.common.parser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import kr.co.goms.module.common.model.CommonBean;

public  class CommonJsonParserData implements IJsonParserData {

	private CommonBean mCommonBean;
	
	@Override
	public void setData(JSONObject jObject) {
		mCommonBean = new CommonBean();
		
		String resResult 	= "";
		String resMessage 	= "";
		String resDatetime 	= "";
		String resTotalPage	= "";

		JSONArray resArray 	= null;
		
		try { resResult = jObject.getString("res_result"); } catch (JSONException e) { }
		try { resMessage = jObject.getString("res_message"); } catch (JSONException e) { }
		try { resDatetime = jObject.getString("res_datetime"); } catch (JSONException e) { }
		try { resTotalPage = jObject.getString("res_total_page"); } catch (JSONException e) { }
		try { resArray = jObject.getJSONArray("res_data"); } catch (JSONException e) { }

		mCommonBean.setRcode(resResult);
		mCommonBean.setRmsg(resMessage);
		mCommonBean.setRdate(resDatetime);
		mCommonBean.setRTotalPage(resTotalPage);
		mCommonBean.setRdata(resArray);
	}

	@Override
	public Object getData() {
		return mCommonBean;
	}

	@Override
	public void setData(JSONArray jArray) {

	}

	@Override
	public Object getArrayData() {
		return null;
	}
}
