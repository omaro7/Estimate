package kr.co.goms.module.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Andy on 2016/3/21.
 */
public final class TimeUtil {
    
    private TimeUtil() throws InstantiationException {
        throw new InstantiationException("This class is not for instantiation");
    }

    /**
     * @return "HH:mm:ss"
     */
    public static String getCurrTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        Date currentTime = new Date();
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    /**
     * @return "yyyy-MM-dd HH:mm:ss"
     */
    public static String getCurrDayTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date currentTime = new Date();
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    /**
     * @return "HH:mm"
     */
    public static String getTime() {
        String timeString = getCurrTime();
        String[] timeArr = timeString.split(":");
        String dateString = timeArr[0] + ":" + timeArr[1];
        return dateString;
    }
}
