package kr.co.goms.module.common.ui.toast;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import kr.co.goms.module.common.R;

/**
 *     public void showToast(String message){
 *         CustomToast customToast = new CustomToast(getActivity(), message);
 *         customToast.show();
 *     }
 */
public class CustomToast {
    private Toast toast;

    public CustomToast(Context context, String message) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.custom_toast, null);
        TextView text = layout.findViewById(R.id.tv_toast);
        text.setText(message);

        toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP, 0, 0); // 화면 상단에 표시
        toast.setView(layout);
    }

    public void show() {
        toast.show();
    }
}
