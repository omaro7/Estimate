package kr.co.goms.module.common.util;

import android.R.color;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.widget.ImageView;

public class ColorUtil {
	
	private final static String TAG = ColorUtil.class.getSimpleName();
	
	/**
	 * mLltMainSettingGrid.setBackgroundColor(Color.parseColor("#00000000"));	//투명
	 * @param color  "#00000000"
	 * @return
	 */
	public static int setParseColor(String color){
		return Color.parseColor(color);
	}
	
	
	/*
	  * Project position on ImageView to position on Bitmap
	  * return the color on the position 
	  */
	public static int getProjectedColor(ImageView iv, Bitmap bm, int x, int y) {
		if (x < 0 || y < 0 || x > iv.getWidth() || y > iv.getHeight()) {
			// outside ImageView
			return color.background_light;
		} else {
			int projectedX = (int) ((double) x * ((double) bm.getWidth() / (double) iv.getWidth()));
			int projectedY = (int) ((double) y * ((double) bm.getHeight() / (double) iv.getHeight()));

			return bm.getPixel(projectedX, projectedY);
		}
	}	
}
