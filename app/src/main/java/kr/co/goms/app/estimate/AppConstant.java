package kr.co.goms.app.estimate;

public class AppConstant {

    public static final String APP_NAME = "estimate";
    public static final String APP_URI = "kr.co.goms.app.estimate";
    public static final String LOG_TAG = "ESTIMATE";
    /* App Config */
    public final static boolean APP_CONFIG_DEDUG = true;

    public static final String APP_JNI = "estimate-native-lib";

    public final static int PHOTO_WIDTH = 160;
    public final static int PHOTO_HEIGHT = 160;
    public static final String EST_PREFIX = "EST_PREFIX";
    public static final String EST_FOLDER = "EST_FOLDER";
    public static final String EST_FOLDER_DEFAULT = "/Estimate/Excel";

    public enum FRAGMENT_TAG{
        COMPANY_FORM,
        COMPANY_LIST,
        CLIENT_FORM,
        CLIENT_LIST,
        ESTIMATE_FORM,
        ESTIMATE_LIST,

    }

    public enum SAVE_EXCEL_TYPE{
        ESTIMATE("견적서"),       //견적서
        SPECIFICATION("거래명세서")   //거래명세서
        ;

        String name;
        SAVE_EXCEL_TYPE(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

}
