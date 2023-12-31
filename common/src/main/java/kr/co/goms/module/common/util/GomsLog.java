package kr.co.goms.module.common.util;

import android.util.Log;

public class GomsLog {

	public static boolean mDebugLog = true;
    static String mDebugTag = GomsLog.class.getSimpleName();
    
    /**
     * Enables or disable debug logging through LogCat.
     */
    public void staticenableDebugLogging(boolean enable, String tag) {
    	mDebugLog = enable;
    	mDebugTag = tag;
    }
    
    public void enableDebugLogging(boolean enable) {
    	mDebugLog = enable;
    }
    
    public static void d(String msg) {
        if (mDebugLog) Log.d(mDebugTag, msg);
    }
    
    public static void d(String tag, String msg) {
        if (mDebugLog) Log.d(mDebugTag, tag + "," + msg);
    }    
    
    public static void e(String msg) {
        Log.e(mDebugTag, "Error: " + msg);
    }
    
    public static void w(String msg) {
        Log.w(mDebugTag, "Warning: " + msg);
    }  
    
}
