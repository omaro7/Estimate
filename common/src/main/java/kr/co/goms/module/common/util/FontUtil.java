package kr.co.goms.module.common.util;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FontUtil {

	private final String SpoqaRegular = "font/spoqahansansneoregular.otf";
	private final String SpoqaMedium = "font/spoqahansansneomedium.otf";
	private final String SpoqaLight = "font/spoqahansansneolight.otf";
	private final String SpoqaBold = "font/spoqahansansneobold.otf";
	private final String SpoqaThin = "font/spoqahansansneothin.otf";

	private static FontUtil fontUtil = null;
	private Typeface HanaM = null;

	private Typeface SpoqaRegularTypeface = null;
	private Typeface SpoqaMediumTypeface = null;
	private Typeface SpoqaLightTypeface = null;
	private Typeface SpoqaBoldTypeface = null;
	private Typeface SpoqaThinTypeface = null;

	public FontUtil(Context context) {
		SpoqaRegularTypeface = Typeface.createFromAsset(context.getAssets(), SpoqaRegular);
		SpoqaMediumTypeface = Typeface.createFromAsset(context.getAssets(), SpoqaMedium);
		SpoqaLightTypeface = Typeface.createFromAsset(context.getAssets(), SpoqaLight);
		SpoqaBoldTypeface = Typeface.createFromAsset(context.getAssets(), SpoqaBold);
		SpoqaThinTypeface = Typeface.createFromAsset(context.getAssets(), SpoqaThin);
	}

	public static FontUtil getInstance(Context context) {
		if (fontUtil == null) {
			fontUtil = new FontUtil(context);
		}
		return fontUtil;
	}

	public Typeface getHanaM() {
		return HanaM;
	}

	/**
	 * 폰트 일괄 적용
	 * 
	 * @return
	 */
	public static void setGlobalFont(ViewGroup root, Typeface typeface) 
	{
		for (int i = 0; i < root.getChildCount(); i++) 
		{
			View child = root.getChildAt(i);
			if (child instanceof TextView) {
				((TextView) child).setTypeface(typeface);
			} else if (child instanceof ViewGroup)
				setGlobalFont((ViewGroup) child, typeface);
		}
	}

	public static void setDefaultFont(View root) {
		if (root instanceof TextView) {
			((TextView) root).setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
		}
	}

	public Typeface getSpoqaRegular() {
		return SpoqaRegularTypeface;
	}

	public Typeface getSpoqaMedium() {
		return SpoqaMediumTypeface;
	}

	public Typeface getSpoqaLight() {
		return SpoqaLightTypeface;
	}

	public Typeface getSpoqaBold() {
		return SpoqaBoldTypeface;
	}

	public Typeface getSpoqaThin() {
		return SpoqaThinTypeface;
	}

}
