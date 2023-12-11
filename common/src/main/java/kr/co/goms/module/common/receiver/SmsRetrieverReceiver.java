package kr.co.goms.module.common.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Status;

import kr.co.goms.module.common.util.StringUtil;

/**
 *
 * 1. 문자내용이 140byte를 초과하면안된다.
 * 2. SMS 맨앞에 <#>가 반드시 포함되어야 한다.
 * 3. SMS 맨마지막에 앱을 식별하는11글자 해시문자열을 포함해야한다.
 * <#> 인증번호[1234] Evp3gExcg1e
 *
 * SMS 권한없이 SMS Retriever API로 인증번호 자동 입력 처리
 * 특정 형식일 경우에만 동작
 * 	public void setSmsNum(String num) {
 * 		if (checkNumText != null) {
 * 			getCheckNum = num;
 * 			checkNumText.setText(getCheckNum);
 *      }
 *  }
 */
public class SmsRetrieverReceiver extends BroadcastReceiver {

	private static String tempNum;
	@Override
	public void onReceive(Context context, Intent intent) {

		if (SmsRetriever.SMS_RETRIEVED_ACTION.equals(intent.getAction())) {
			Bundle extras = intent.getExtras();
			Status status = (Status) extras.get(SmsRetriever.EXTRA_STATUS);
			switch (status.getStatusCode()) {
				case CommonStatusCodes.SUCCESS:
					String message = (String) extras.get(SmsRetriever.EXTRA_SMS_MESSAGE);
					if(StringUtil.getIndexOf(message, "[곰스]") != -1){
						String num = "";
						if (StringUtil.getIndexOf(message, "인증번호:") != -1)
							num = StringUtil.substr(message, StringUtil.getIndexOf(message, "인증번호:") + 5, StringUtil.getIndexOf(message, "인증번호:") + 11);
						else
							num = StringUtil.substr(message, StringUtil.getIndexOf(message, "인증번호[") + 5, StringUtil.getIndexOf(message, "인증번호[") + 11);

						if(StringUtil.isNull(tempNum) || !tempNum.equals(num)) {

							//todo hjh 요청한 EditText에 넣기
							//if (NewPaySignUpSMSFragment.me != null){
							//	NewPaySignUpSMSFragment.me.setSmsNum(num);
							//}

							tempNum = num;
						}else{
							tempNum = "";
						}
					}
					break;
				case CommonStatusCodes.TIMEOUT:
                    // SMSRetriver 의 타임아웃은 15분 .. 타임아웃이 어떻게 처리될 것인가?
//					if(PaySignBase.me != null) PaySignBase.me.setSmsNumTO("");
					break;
			}
		}

/*		SmsRetrieverClient client = SmsRetriever.getClient(this *//* context *//*);
		Task<Void> task = client.startSmsRetriever();

		task.addOnSuccessListener(new OnSuccessListener<Void>() {
			@Override
			public void onSuccess(Void aVoid) {
				IntentFilter intentFilter = new IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION);
				registerReceiver(smsReceiver, intentFilter);
				Log.e("testest", "onSuccess");
			}
		});
		task.addOnFailureListener(new OnFailureListener() {
			@Override
			public void onFailure(@NonNull Exception e) {
				Log.e("testest", "onFailure" + e.toString());
			}
		});*/
	}
}
