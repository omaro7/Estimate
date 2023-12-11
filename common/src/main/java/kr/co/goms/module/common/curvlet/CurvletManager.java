package kr.co.goms.module.common.curvlet;

import android.app.Activity;
import android.net.Uri;
import android.util.Log;

import java.util.HashMap;

import kr.co.goms.module.common.base.WaterCallBack;

public class CurvletManager  {
    private static final String LOG_TAG = "CurvletManager";
    static CurvletManager sInstance;
    private String scheme;
    HashMap<String, CurvletItemList> curvletList;

    private CurvletManager() {
    }

    public void create(String scheme) {
        Log.d("CurvletManager", "create");
        if (null == this.curvletList) {
            this.curvletList = new HashMap();
        }

        sInstance.addCurvlet("tel", (String)null, CurvletActionView.class);
        sInstance.addCurvlet("sms", (String)null, CurvletActionSendTo.class);
        sInstance.addCurvlet("mailto", (String)null, CurvletActionView.class);
        sInstance.addCurvlet("geo", (String)null, CurvletActionView.class);
        sInstance.addCurvlet("market", (String)null, CurvletActionView.class);
        sInstance.addCurvlet(scheme, "exit", CurvletExit.class);
    }

    private void destroy() {
        if (null != this.curvletList) {
            Log.d("CurvletManager", "destroy");
            this.curvletList.clear();
            this.curvletList = null;
        }

    }

    public static CurvletManager I() {
        if (null == sInstance) {
            Log.d("CurvletManager", "I()");
            sInstance = new CurvletManager();
        }

        return sInstance;
    }

    public static void D() {
        if (null != sInstance) {
            Log.d("CurvletManager", "D()");
            sInstance.destroy();
            sInstance = null;
        }

    }

    public void addCurvlet(String scheme, String host, Class curvlet) {
        if (null != this.curvletList) {
            if (null != scheme && 0 != scheme.length()) {
                if (null == host || 0 == host.length()) {
                    Log.e("CurvletManager", "addCurvlet host 값을 문자 null로 변환해 기록 함");
                    host = "null";
                }

                Log.d("CurvletManager", "addCurvlet scheme : " + scheme + " host : " + host);
                CurvletManager.CurvletItemList list = (CurvletManager.CurvletItemList)this.curvletList.get(scheme);
                if (null != list) {
                    Class<Curvlet> tempCurvlet = (Class)list.itemList.get(host);
                    if (null == tempCurvlet) {
                        list.itemList.put(host, curvlet);
                    }
                } else {
                    CurvletManager.CurvletItemList tempItemList = new CurvletManager.CurvletItemList();
                    tempItemList.itemList.put(host, curvlet);
                    this.curvletList.put(scheme, tempItemList);
                }
            } else {
                Log.e("CurvletManager", "addCurvlet scheme 값이 잘못 정의 됨");
            }
        }
    }

    public static boolean process(Activity activity, WaterCallBack callback, String url) {
        if (null == sInstance) {
            return false;
        } else {
            Log.d("CurvletManager", "process");
            return sInstance.onProcess(activity, callback, url);
        }
    }

    private boolean onProcess(Activity activity, WaterCallBack response, String request) {
        Log.d("CurvletManager", "onProcess");
        Uri uri = Uri.parse(request);
        String scheme = uri.getScheme();
        Log.d("CurvletManager", "onProcess url : " + request);
        CurvletManager.CurvletItemList list = (CurvletManager.CurvletItemList)this.curvletList.get(scheme);
        if (null != list) {
            Log.d("CurvletManager", "onProcess scheme : " + scheme);
            String host = uri.getHost();
            host = host + uri.getPath();
            Log.d("CurvletManager", "onProcess host + path : " + host);
            Class<Curvlet> tempCurvlet = (Class)list.itemList.get(host);
            if (null != tempCurvlet) {
                Log.d("CurvletManager", "onProcess host : " + host);
                return this.onCurvlet(activity, tempCurvlet, response, uri);
            }

            Log.d("CurvletManager", "onProcess find host don't");
            Class<Curvlet> nullCurvlet = (Class)list.itemList.get("null");
            if (null != nullCurvlet) {
                return this.onCurvlet(activity, nullCurvlet, response, uri);
            }
        } else {
            Log.d("CurvletManager", "onProcess find scheme don't");
        }

        Log.d("CurvletManager", "onProcess false");
        return false;
    }

    private void threadRun(boolean useFlag, final Curvlet curvlet, final Activity activity, final WaterCallBack response, final Uri request) {
        if (useFlag) {
            Thread thread = new Thread(new Runnable() {
                public void run() {
                    Log.d("CurvletManager", "onProcess - have curvlet (thread on)");
                    curvlet.process(activity, response, request);
                }
            });
            thread.setDaemon(true);
            thread.start();
        } else {
            Log.d("CurvletManager", "onProcess - have curvlet (thread off)");
            curvlet.process(activity, response, request);
        }

    }

    private boolean onCurvlet(final Activity activity, Class<Curvlet> curvletClass, final WaterCallBack response, final Uri request) {
        try {
            final Curvlet curvlet = (Curvlet)curvletClass.newInstance();
            String threadCheck = null;

            try {
                threadCheck = request.getQueryParameter("thread");
            } catch (Exception var8) {
            }

            if (curvlet.isEmpty(threadCheck)) {
                this.threadRun(true, curvlet, activity, response, request);
            } else if (threadCheck.equalsIgnoreCase("ui")) {
                activity.runOnUiThread(new Runnable() {
                    public void run() {
                        Log.d("CurvletManager", "onProcess - have curvlet");
                        curvlet.process(activity, response, request);
                    }
                });
            } else if (threadCheck.equalsIgnoreCase("off")) {
                this.threadRun(false, curvlet, activity, response, request);
            } else {
                this.threadRun(true, curvlet, activity, response, request);
            }

            return true;
        } catch (InstantiationException var9) {
            var9.printStackTrace();
        } catch (IllegalAccessException var10) {
            var10.printStackTrace();
        }

        return false;
    }

    public static class CurvletItemList {
        HashMap<String, Class<Curvlet>> itemList = new HashMap();

        public CurvletItemList() {
        }
    }
}
