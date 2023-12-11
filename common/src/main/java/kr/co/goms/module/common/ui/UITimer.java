package kr.co.goms.module.common.ui;

import android.app.Activity;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import kr.co.goms.module.common.R;
import kr.co.goms.module.common.util.StringUtil;

public class UITimer {

    static final String LOG_TAG = UITimer.class.getSimpleName();

    static CountDownTimer countDownTimer = null;

    static int timeLimitSeconds;

    static boolean alive = false;
    static boolean isStart = true;
    static String mDefaultTimeTxt = "";

    public enum TIMER_TYPE{
        DEFAULT("40"),
        SAMPLE("40");
        private String mTime;

        TIMER_TYPE(String time) {
            mTime = time;
        }
        public String getTime() {
            return mTime;
        }
        public void setTime(String time) {
            this.mTime = time;
        }
    }

    /**
     *
     * @param activity
     * @param view
     * @param timeLimit
     * @param txtTimer      view.findViewById(R.id.txt_timer)
     * @param ivTimerAdd   view.findViewById(R.id.iv_timer_add)
     * @param event
     * @param timer_type
     */
    public static void timerBegin(Activity activity, View view, int timeLimit,
                                  TextView txtTimer,
                                  ImageView ivTimerAdd,
                                  final OnEvent event, TIMER_TYPE timer_type) {
        timerStop();

        if(timer_type == TIMER_TYPE.DEFAULT || timer_type == TIMER_TYPE.SAMPLE){
            timeLimit = 500000;
        } else {
//            timeLimit = 120000;
            timeLimit = 500000;
        }

        UITimer.timeLimitSeconds = timeLimit / 1000;

        final TextView textViewTimer = txtTimer;
        if(StringUtil.isEmpty(mDefaultTimeTxt)){
            mDefaultTimeTxt = minuteText(UITimer.timeLimitSeconds);
        }
        textViewTimer.setText(mDefaultTimeTxt);


        final ImageView timeAddButton = ivTimerAdd;
        timeAddButton.setVisibility(View.VISIBLE);
        timeAddButton.setAlpha(1f);
        timeAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UITimer.timeLimitSeconds = UITimer.timeLimitSeconds+Integer.parseInt(timer_type.getTime());
                timeAddButton.setClickable(false);
                textViewTimer.setClickable(false);
                timeAddButton.setAlpha(0.3f);
            }
        });

        textViewTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UITimer.timeLimitSeconds = UITimer.timeLimitSeconds+Integer.parseInt(timer_type.getTime());

//                textViewTimer.setText(minuteText(UITimer.timeLimitSeconds+Integer.parseInt(timer_type.getTime())));
                timeAddButton.setClickable(false);
                textViewTimer.setClickable(false);
//                timeAddButton.setB
                timeAddButton.setAlpha(0.3f);
            }
        });

        countDownTimer = new CountDownTimer(timeLimit, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if(true == alive) {
                    event.timeCheck(--UITimer.timeLimitSeconds);
                    textViewTimer.setText(minuteText(UITimer.timeLimitSeconds));
                    if(UITimer.timeLimitSeconds <= 0){
                        event.timeOver();
                        timerStop();
                    }

                }
            }

            @Override
            public void onFinish() {
                if(true == alive) {
                    countDownTimer.start();
//                    event.timeOver();
                }
            }
        };

        alive = true;
        if(isStart) {
            countDownTimer.start();
        }
        timeAddButton.setClickable(true);
        if(timer_type == TIMER_TYPE.DEFAULT) {
            timeAddButton.setBackgroundResource(R.drawable.btn_reload_w);
        } else {
            timeAddButton.setBackgroundResource(R.drawable.btn_reload);
        }
    }

    public static void timerStop() {
        alive = false;
        if(null != countDownTimer) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
    }

    public static boolean isIsStart() {
        return isStart;
    }

    public static void setIsStart(boolean isStart) {
        UITimer.isStart = isStart;
        countDownTimer.start();
    }

    public static String getDefaultTimeTxt() {
        return mDefaultTimeTxt;
    }

    public static void setDefaultTimeTxt(String mDefaultTimeTxt) {
        UITimer.mDefaultTimeTxt = mDefaultTimeTxt;
    }

    private static String minuteText(int seconds) {
        String result;

        int mm = seconds / 60;
        int ss = seconds % 60;

        /**
         * 2020/10/08
         * 초가 10초 이하로 내려가면 0이 붙게
         */
        String ss_ = "" + ss;

        if(1 >= ss_.length()) {
            ss_ = "0" + ss;
        }

        if(0 < mm) {
            result = "" + mm + ":" + ss_;
        } else {
            result = "0:" + ss_;
        }

        return result;
    }


    public interface OnEvent {
        void timeCheck(int timeRemainingSeconds);
        void timeOver();
    }
}
