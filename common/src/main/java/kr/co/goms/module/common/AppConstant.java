package kr.co.goms.module.common;

import android.os.Environment;

import java.util.Arrays;
import java.util.List;

public class AppConstant {

    /* App Name */
	public final static String APP_NAME = "goms_common";

    public static final List<String> FILE_EXTN = Arrays.asList("jpg", "jpeg", "png");

    public static final String DEFAULT_EXTERNAL_STORAGE_DIRECTORY = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();

}