package kr.co.goms.app.estimate.jni;


import kr.co.goms.app.estimate.AppConstant;
import kr.co.goms.app.estimate.MyApplication;

public class GomsJNI {

    public native String ivKey();
    public native String encryptKey();

    private static MyApplication mApp;

    static {
        System.loadLibrary(AppConstant.APP_JNI);
    }

    public GomsJNI(MyApplication app)
    {
        mApp = app;
    }

    // 암호키
    public String getEncryptKey()
    {
        return encryptKey();
    }


}
