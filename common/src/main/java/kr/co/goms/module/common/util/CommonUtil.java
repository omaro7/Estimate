package kr.co.goms.module.common.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Handler;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Size;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.EditText;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import kr.co.goms.module.common.R;

public class CommonUtil {

    private final static String TAG = CommonUtil.class.getSimpleName();

    public static void delayApply(Integer intValue){
        try {
            Thread.sleep(intValue);
        } catch (InterruptedException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }

    public static float convertDpToPX(Activity activity, float dp){
        DisplayMetrics outMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        float density = outMetrics.density;
        float px = dp*density;
        return px;
    }

    public static float convertPxToDp(Activity activity, float px){
        DisplayMetrics outMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        float density = outMetrics.density;
        float dp = px / density;
        return dp;
    }

    /**
     * Converts the specified DP to PIXELS according to current screen density
     * @param context
     * @param dp
     * @return
     */
    public static float dpToPx(Context context, float dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return (int) ((dp * displayMetrics.density) + 0.5);
    }

    public static int dipToint(Context context, float f)
    {
        return (int)TypedValue.applyDimension(1, f, context.getResources().getDisplayMetrics());
    }

    public static int dipToint(Context context, int j)
    {
        return (int)TypedValue.applyDimension(1, j, context.getResources().getDisplayMetrics());
    }

    /* EditText Email Check */
    public static boolean isEditTextContainEmail(EditText argEditText) {
        try {
            Pattern pattern = Pattern.compile("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
            Matcher matcher = pattern.matcher(argEditText.getText());
            return matcher.matches();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /* TextView Email Check */
    public static boolean isTextContainEmail(String argText) {
        try {
            Pattern pattern = Pattern.compile("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
            Matcher matcher = pattern.matcher(argText);
            return matcher.matches();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Compare 2 version strings and tell if the first is higher, equal or lower
     * Source: http://stackoverflow.com/questions/6701948/efficient-way-to-compare-version-strings-in-java
     *
     * @param ver1 Reference version
     * @param ver2 Comparison version
     *
     * @return 1 if ver1 is higher, 0 if equal, -1 if ver1 is lower
     */
    public static final int compareVersions(String ver1, String ver2) {

        String[] vals1 = ver1.split("\\.");
        String[] vals2 = ver2.split("\\.");
        int i=0;
        while(i<vals1.length&&i<vals2.length&&vals1[i].equals(vals2[i])) {
            i++;
        }

        if (i<vals1.length&&i<vals2.length) {
            int diff = Integer.valueOf(vals1[i]).compareTo(Integer.valueOf(vals2[i]));
            return diff<0?-1:diff==0?0:1;
        }

        return vals1.length<vals2.length?-1:vals1.length==vals2.length?0:1;
    }

    /*
     * 배너 웹뷰 처리
     */
    @SuppressLint("NewApi")
    public static void setUpWebViewDefaults(WebView webView) {
        WebSettings settings = webView.getSettings();

        // Enable Javascript
        settings.setJavaScriptEnabled(false);

        // Use WideViewport and Zoom out if there is no viewport defined
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);

        // Enable pinch to zoom without the zoom buttons
        settings.setBuiltInZoomControls(false);

        settings.setDisplayZoomControls(false);

        WebView.setWebContentsDebuggingEnabled(false);

    }


    /*
     * 리소스 ID 가지고 오기
     * 사용방법 : getResId("icon", context, Drawable.class);
     */
    public static int getResId(String variableName, Class<?> c) {
        try {
            Field idField = c.getDeclaredField(variableName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    /*
     * 앱 설치 여부 확인
     * uri : kr.co.adphoto.m
     */
    public static boolean isAppInstalledOrNot(Context context, String uri) {
        PackageManager pm = context.getPackageManager();
        boolean app_installed = false;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        }
        catch (NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed ;
    }
    public static boolean isPackageInstalled(Context context, String packageName) {
        final PackageManager packageManager = context.getPackageManager();
        Intent intent = packageManager.getLaunchIntentForPackage(packageName);
        if (intent == null) {
            return false;
        }
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }
    public static void goSleep(long time){
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void confirmAdPhotoActivity(Activity activity){
        ActivityManager am = (ActivityManager) activity.getSystemService(activity.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> list = am.getRunningAppProcesses();

        for(int i = 0; i < list.size(); i++)
        {
            Log.d(TAG, list.get(i).processName);
        }
    }

    //로컬 버젼 확인
    public static String getVersionName(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (NameNotFoundException e) {
            Log.d(TAG, "err NameNotFoundException -> " + e.toString());
            return null;
        }
    }

    public static void goGooglePlayApp(Activity activity, String packageName){
        Uri uri = Uri.parse("market://details?id=" + packageName);
        Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            activity.startActivity(myAppLinkToMarket);
        } catch (ActivityNotFoundException e) {
            ToastUtil.makeText(activity, "You don't have Google Play installed");
        }
    }

    public static void  goOtherApp(Activity activity, String packageName){
        Intent LaunchIntent = activity.getPackageManager().getLaunchIntentForPackage(packageName);
        LaunchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(LaunchIntent);
    }

    public static String getDirPathOnly(String s)
    {
        List list;
        StringBuffer stringbuffer;
        try
        {
            list = Uri.parse(s).getPathSegments();
            stringbuffer = new StringBuffer();
        }
        catch(Exception exception)
        {
            return null;
        }

        for(int j = 0; j < list.size(); j++){
            stringbuffer.append((new StringBuilder("/")).append((String)list.get(j)).toString());
        }
        return stringbuffer.toString();

    }

    public static boolean isEmpty(Object obj)
    {
        if(obj == null)
            return true;
        if((obj instanceof ArrayList) && ((ArrayList)obj).size() == 0)
            return true;
        if((obj instanceof Map) && ((Map)obj).size() == 0)
            return true;
        if((obj instanceof Boolean) && !((Boolean)obj).booleanValue())
            return true;
        return (obj instanceof String) && ((String)obj).length() == 0;
    }

    public static float confirmDisplayRatio(Activity activity){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        float aspectRatio = (float) displayMetrics.widthPixels / (float) displayMetrics.heightPixels;
        return aspectRatio;
    }

    @SuppressLint("NewApi")
    public static Size confirmDisplayDeviceSize(Activity activity){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        Size size = new Size(displayMetrics.widthPixels, displayMetrics.heightPixels);
        return size;
    }

    public static void confirmDisplay(Activity activity){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int deviceWidth = displayMetrics.widthPixels;
        int deviceHeight =displayMetrics.heightPixels;

        float aspectRatio = (float) displayMetrics.widthPixels / (float) displayMetrics.heightPixels;

        Log.d(TAG, "deviceWidth : " + deviceWidth);
        Log.d(TAG, "deviceHeight : " + deviceHeight);
        Log.d(TAG, "aspectRatio : " + aspectRatio);

    }

    public static void confirmResizDisplay(Activity activity){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int deviceWidth = displayMetrics.widthPixels;
        int deviceHeight =displayMetrics.heightPixels;

        float aspectRatio = (float) displayMetrics.widthPixels / (float) displayMetrics.heightPixels;

        int targetWidth = 900;
        float scale = (float) targetWidth / deviceWidth;

        int newHeight = (int) Math.round(deviceHeight * scale);

        Log.d(TAG, "deviceWidth : " + deviceWidth);
        Log.d(TAG, "deviceHeight : " + deviceHeight);
        Log.d(TAG, "aspectRatio : " + aspectRatio);
        Log.d(TAG, "targetWidth : " + targetWidth);
        Log.d(TAG, "newHeight : " + newHeight);
    }

    public static int getResizHeight(Activity activity, int targetWidth){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int deviceWidth = displayMetrics.widthPixels;
        int deviceHeight =displayMetrics.heightPixels;
        float scale = (float) targetWidth / deviceWidth;
        int newHeight = (int) Math.round(deviceHeight * scale);
        Log.d(TAG, "newHeight : " + newHeight);
        return newHeight;
    }

    public static int greatestCommonFactor(int width, int height) {
        return (height == 0) ? width : greatestCommonFactor(height, width % height);
    }

    /**
     * 비율 산출
     * @param width
     * @param height
     * @return "4,3"
     */
    public static String getAspect(int width, int height){
        int factor = greatestCommonFactor(width, height);

        int widthRatio = width / factor;
        int heightRatio = height / factor;

        StringBuffer sb = new StringBuffer();
        sb.append(widthRatio);
        sb.append(",");
        sb.append(heightRatio);
        return sb.toString();
    }

    public static boolean isFloatingEqual(float v1, float v2) {
        if (v1 == v2)
            return true;
        float absoluteDifference = Math.abs(v1 - v2);
        float maxUlp = Math.max(Math.ulp(v1), Math.ulp(v2));
        return absoluteDifference < 5 * maxUlp;
    }

    public static boolean isWifiConnect(Activity activity) {

        ConnectivityManager manager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo mobile = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if(wifi.isConnected()){
            return true;
        }

        return false;
    }

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public static long calculateLength(CharSequence c) {
        double len = 0;
        for (int i = 0; i < c.length(); i++) {
            int tmp = (int) c.charAt(i);
            if (tmp > 0 && tmp < 127) {
                len += 0.5;
            } else {
                len++;
            }
        }
        return Math.round(len);
    }

    public static void hideSoftInput(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view == null) view = new View(activity);
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void showSoftInput(EditText edit, Context context) {
        edit.setFocusable(true);
        edit.setFocusableInTouchMode(true);
        edit.requestFocus();
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(edit, 0);
    }

    public static void toggleSoftInput(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    public static int getStatusBarHeight(Resources res) {
        return (int) (24 * res.getDisplayMetrics().density);
    }

//    public static int getToolBarHeight(Context context) {
//        int[] attrs = new int[] {R.attr.actionBarSize};
//        TypedArray ta = context.obtainStyledAttributes(attrs);
//        int toolBarHeight = ta.getDimensionPixelSize(0, -1);
//        ta.recycle();
//        return toolBarHeight;
//    }

    public static float range(final int percentage, final float start, final float end) {
        return (end - start) * percentage / 100.0f + start;
    }

    public static int range(final int percentage, final int start, final int end) {
        return (end - start) * percentage / 100 + start;
    }

    public static int rangePercentage(final int range, final int start, final int end) {
        return  (int)((float)((range - start) * 100) / (end - start) + 1);
    }

    public static String MD5(String md5) {
        if (TextUtils.isEmpty(md5)) return null;
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(md5.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (byte anArray : array) {
                sb.append(Integer.toHexString((anArray & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException ignored) {
        } catch(UnsupportedEncodingException ignored){
        }
        return null;
    }

    /**
     * 리스너 아규먼트 넘기기 위해서 ArrayList 객체를 생성한다.
     * @param vals 추가할 아규먼트들...
     * @return ArrayList 객체
     */
    public static ArrayList<Object> setArg(Object ... vals)
    {
        ArrayList<Object> arg = new ArrayList<Object>();
        for(Object val : vals) arg.add(val);
        return arg;
    }

    /**
     * 키보드를 올리고/내린다.
     * @param context 컨텍스트 객체
     * @param et EditText 객체
     * @param isShow 표시여부
     */
    public static void setSoftKeyControl(final Context context, final EditText et, final boolean isShow)
    {
        if(et != null)
        {
            new Handler().postDelayed(new Runnable()
            {
                @Override
                public void run()
                {
                    if(isShow) et.requestFocus();
//					else et.clearFocus();

                    InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);

                    if(isShow) imm.showSoftInput(et, 0);
                    else imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
                }
            }, 100);
        }
    }
    public static void setSoftKeyControl(final Context context, final View et, final boolean isShow)
    {
        if(et != null)
        {
            new Handler().postDelayed(new Runnable()
            {
                @Override
                public void run()
                {
                    if(isShow) et.requestFocus();
//					else et.clearFocus();

                    InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);

                    if(isShow) imm.showSoftInput(et, 0);
                    else imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
                }
            }, 100);
        }
    }

    public boolean isConnectingToInternet(final Context context){
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null)
        {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }

        }
        return false;
    }

    /**
     * NFC 설정으로 가기
     * @param activity
     */
    public static void goNfcSettings(Activity activity) {
        activity.startActivityForResult(new Intent(Settings.ACTION_NFC_SETTINGS),5001);
    }

    /**
     *         // 디스플레이 멀티뷰(작게 : 360dpi, 크게 : 420dpi)
     *         // 디스플레이 표준보기(작게 : 485dpi, 크게 : 520dpi)
     *         if(getRealDpWidth(this) >= 485){
     *             View decorView = getWindow().getDecorView();
     *             // Hide both the navigation bar and the status(task) bar.
     *             // SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as a general rule, you should design your app to hide the status bar whenever you hide the navigation bar.
     *             int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
     *             decorView.setSystemUiVisibility(uiOptions);
     *         }
     *
     * @param activity
     * @return
     */
    public static int getRealDpWidth(Activity activity) {
        DisplayMetrics metrics = activity.getResources().getDisplayMetrics();
        //해상도
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int dpi = metrics.densityDpi;
        return dpi;
    }

    public static int getRealWidthPixels(Activity activity) {
        DisplayMetrics metrics = activity.getResources().getDisplayMetrics();
        //픽셀
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int wPixels = metrics.widthPixels;
        return wPixels;
    }
}
