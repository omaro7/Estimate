package kr.co.goms.module.common.util;

import android.os.Looper;

/**
 * TimeStamp timeStamp = new TimeStamp("여기시작");
 * timeStamp.begin();
 * .....
 * timeStamp.end();
 *
 */
public class TimeStamp {
    private static String LOG_TAG = "TimeStamp";
    private static long startTime = 0L;
    private static long accumulateTime = 0L;
    private static boolean beginLogFlag = false;
    private static boolean endLogFlag = false;
    private static TimeStampLog timeStampLog;
    private long beginTime = 0L;
    private long endTime = 0L;
    private String title;

    public TimeStamp(String title) {
        if (0L == startTime) {
            startTime = System.currentTimeMillis();
        }

        this.title = title;
    }

    public static void setLogFlag(boolean begin, boolean end, TimeStampLog timeStamp) {
        beginLogFlag = begin;
        endLogFlag = end;
        timeStampLog = timeStamp;
    }

    public static void reset(String tag, String print) {
        startTime = 0L;
        accumulateTime = 0L;
        if (null != timeStampLog) {
            timeStampLog.output(tag, "reset : " + print);
        }

    }

    public void begin() {
        if (0L == this.beginTime) {
            this.beginTime = System.currentTimeMillis();
            accumulateTime = System.currentTimeMillis() - startTime;
            if (beginLogFlag) {
                if (Looper.myLooper() == Looper.getMainLooper()) {
                    this.log(LOG_TAG, this.title + " - begin : " + accumulateTime + " | Main");
                } else {
                    this.log(LOG_TAG, this.title + " - begin : " + accumulateTime + " | Sub");
                }
            }
        }

    }

    public void end() {
        if (0L == this.endTime) {
            this.endTime = System.currentTimeMillis();
            accumulateTime = this.endTime - startTime;
            long offsetTime = this.endTime - this.beginTime;
            if (endLogFlag) {
                if (Looper.myLooper() == Looper.getMainLooper()) {
                    this.log(LOG_TAG, this.title + " - end : " + accumulateTime + " | Main");
                } else {
                    this.log(LOG_TAG, this.title + " - end : " + accumulateTime + " | Sub");
                }
            }

            this.log(LOG_TAG, this.title + " - offset : " + offsetTime);
        }

    }

    private void log(String tag, String print) {
        if (null != timeStampLog) {
            timeStampLog.output(tag, print);
        }

    }

    public interface TimeStampLog {
        void output(String var1, String var2);
    }
}