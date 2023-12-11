package kr.co.goms.module.common.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import java.util.HashMap;

import androidx.appcompat.app.AppCompatActivity;

public class DisplayUtil {

	public static String TAG = DisplayUtil.class.getSimpleName();
	public static AppCompatActivity mActivity;

	public DisplayUtil(final AppCompatActivity activity){
		mActivity = activity;
	}

	public static HashMap<String, Integer> getDeviceDisplaySize(AppCompatActivity activity){
		WindowManager w = activity.getWindowManager();
		Display d = w.getDefaultDisplay();
		DisplayMetrics metrics = new DisplayMetrics();
		d.getMetrics(metrics);
		// since SDK_INT = 1;
		int widthPixels = metrics.widthPixels;
		int heightPixels = metrics.heightPixels;
		// includes window decorations (statusbar bar/menu bar)
		if (Build.VERSION.SDK_INT >= 14 && Build.VERSION.SDK_INT < 17)
			try {
				widthPixels = (Integer) Display.class.getMethod("getRawWidth").invoke(d);
				heightPixels = (Integer) Display.class.getMethod("getRawHeight").invoke(d);
			} catch (Exception ignored) {
			}
		// includes window decorations (statusbar bar/menu bar)
		if (Build.VERSION.SDK_INT >= 17)
			try {
				Point realSize = new Point();
				Display.class.getMethod("getRealSize", Point.class).invoke(d, realSize);
				widthPixels = realSize.x;
				heightPixels = realSize.y;
			} catch (Exception ignored) {
			}

		//Size size = new Size(widthPixels, heightPixels);

		HashMap<String, Integer> size = new HashMap<String, Integer>();
		size.put("width", widthPixels);
		size.put("height", heightPixels);

		return size;
	}

	/** 3: 4 = numerator(분자) : denominator(분모) */
	public static int getRadioHeight(int targetWidth, int numerator, int denominator){
		int resultHeight = Math.round((targetWidth*denominator)/numerator);
		return resultHeight;
	}

	/** 3: 4 = numerator(분자) : denominator(분모) */
	public static int getRadioWidth(int targetHeight, int numerator, int denominator){
		int resultWidth = (targetHeight*numerator)/denominator;
		return resultWidth;
	}

	/**
	 *  (original height / original width) x new width = new height
	 * @param targetWidth
	 * @param originalWidth
	 * @param originalHeight
	 * @return
	 */
	public static int getRadioHeightFromOrginalSize(int targetWidth, int originalWidth, int originalHeight){
		GomsLog.d(TAG, "getRadioHeightFromOrginalSize()");
		GomsLog.d(TAG, "targetWidth : " + targetWidth);
		GomsLog.d(TAG, "originalWidth : " + originalWidth);
		GomsLog.d(TAG, "originalHeight : " + originalHeight);

		int resultHeight = (int)(((float)originalHeight / (float)originalWidth) * (float)targetWidth);
		GomsLog.d(TAG, "getRadioHeightFromOrginalSize() resultHeight : " + resultHeight);
		return resultHeight;
	}

	/**
	 *
	 * @return
	 */
	public static int getStatusBarHeight(Context context) {
		int height;

		Resources myResources = context.getResources();
		int idStatusBarHeight = myResources.getIdentifier(
				"status_bar_height", "dimen", "android");
		if (idStatusBarHeight > 0) {
			height = context.getResources().getDimensionPixelSize(idStatusBarHeight);
		}else{
			height = 0;
		}

		return height;
	}

}
