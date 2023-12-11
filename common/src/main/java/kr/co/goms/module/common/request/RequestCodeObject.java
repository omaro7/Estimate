package kr.co.goms.module.common.request;

import android.app.Activity;
import android.content.Intent;

public interface RequestCodeObject {
    void setRequestCode(int var1);
    int getRequestCode();
    void onActivityResult(Activity var1, int var2, int var3, Intent var4);
    void onRequestPermissionsResult(Activity var1, int var2, String[] var3, int[] var4);
}
