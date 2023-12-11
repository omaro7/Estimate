package kr.co.goms.module.common.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ToastUtil {

	public static void CreateToast(Context context){
		CharSequence cs = "Customtoast";
		TextView tv = new TextView(context);
		tv.setText(cs);
		tv.setTextColor(Color.BLACK);
		LinearLayout ll = new LinearLayout(context);
		ll.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
		//ll.setBackgroundResource(R.drawable.background);
		ll.setGravity(Gravity.CENTER);
		ll.addView(tv);
		Toast t = Toast.makeText(context, "CustomToast", Toast.LENGTH_SHORT);
		t.setGravity(Gravity.CENTER, 0, 0);
		t.setView(ll);
		t.show();
	}

	public static void makeText(Activity Activity, String msg){
		Activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(Activity, msg, Toast.LENGTH_SHORT).show();
			}
		});
	}

	public static void CreateDefaultToast(Context ct, String msg){
		Toast.makeText(ct, msg, Toast.LENGTH_SHORT).show();
	}

	public static void CreateDefaultToastLong(Context ct, String msg){
		Toast.makeText(ct, msg, Toast.LENGTH_LONG).show();
	}
}
