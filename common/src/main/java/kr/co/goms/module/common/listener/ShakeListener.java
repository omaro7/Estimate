package kr.co.goms.module.common.listener;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.SystemClock;
import android.util.Log;

/**
 * Created by Administrator on 2018-10-18.
 * 중지 ShakeListener.getInstance(instance).stop();
 * 시작 ShakeListener.getInstance(instance).start();
 */

public class ShakeListener implements SensorEventListener {
    private String LOG_TAG = ShakeListener.class.getSimpleName();

    private Context mContext = null;
    private Activity act=null;
    private SensorManager sensorManager = null;
    private Sensor mAcclerometer;
    protected boolean enable = false;

    private long mShakeTime;
    private static final int SHAKE_SKIP_TIME = 1000;
    private static final float SHAKE_THRESHOLD_GRAVITY = 4.6f;

    private long[] mLastShakeTime = {0};

    /**
     * 사용 일시 정지 플래그
     */
    boolean pauseFlag = false;

    //20210609 HJH 싱글톤 처리
    @SuppressLint("StaticFieldLeak")
    private static ShakeListener instance;

    public static ShakeListener getInstance(Context context){
        if(instance == null){
            instance = new ShakeListener(context);
        }
        return instance;
    }


    public ShakeListener(Context context) {
        mContext = context;
        setInit();
    }

    /**
     * 사용 여부 확인
     */
    public void load() {
//        RbPreference mPref = new RbPreference(mContext);
//        boolean shakeIsOn = mPref.getValue(Constant.PREFERENCE_SHAKE_PAYMENT, false);
//
//        if(shakeIsOn){
//            start();
//        }else{
//            stop();
//        }
    }

    /**
     * 사용 설정
     */
    public void resume() {
        Log.d(LOG_TAG, "resume() >> 사용 가능 상태 변경");
        pauseFlag = false;
    }

    /**
     * 사용 일시 정지
     */
    public void pause() {
        Log.d(LOG_TAG, "pause() >> 일시 정지 상태 변경");
        pauseFlag = true;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(true == pauseFlag) {
            return;
        }

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float axisX = event.values[0];
            float axisY = event.values[1];
            float axisZ = event.values[2];

            float gravityX = axisX / SensorManager.GRAVITY_EARTH;
            float gravityY = axisY / SensorManager.GRAVITY_EARTH;
            float gravityZ = axisZ / SensorManager.GRAVITY_EARTH;

            Float f = (gravityX * gravityX) + (gravityY * gravityY) + (gravityZ * gravityZ);
            double squared = Math.sqrt(f.doubleValue());
            float gForce = (float) squared;

            if (gForce > SHAKE_THRESHOLD_GRAVITY) {

                // double shake block
                if ((SystemClock.elapsedRealtime() - mLastShakeTime[0]) < 700) {
                    return;
                }
                mLastShakeTime[0] = SystemClock.elapsedRealtime();

                shakePlay();
            }
        }
    }

    //흔들기 시, 해야 할 업무
    public void shakePlay(){

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void setInit() {
        sensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
        mAcclerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    public void start() {
        synchronized (sensorManager) {
            if(true == enable) {
                return;
            }

            sensorManager.registerListener(this, mAcclerometer, SensorManager.SENSOR_DELAY_NORMAL);
            enable = true;
        }
    }

    public void stop() {
        synchronized (sensorManager) {
            if(false == enable) {
                return;
            }

            sensorManager.unregisterListener(this);
            enable = false;
        }
    }
}