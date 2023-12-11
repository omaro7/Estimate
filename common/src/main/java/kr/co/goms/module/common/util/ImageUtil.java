package kr.co.goms.module.common.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.WallpaperManager;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.BlurMaskFilter.Blur;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.location.Location;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Size;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.signature.ObjectKey;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import kr.co.goms.module.common.AppConstant;

public class ImageUtil {

	private static final String TAG = "ImageUtil";

	/** Minimum class memory class to use full-res photos */
	private final static long MIN_NORMAL_CLASS = 32;
	/** Minimum class memory class to use small photos */
	private final static long MIN_SMALL_CLASS = 24;

	public static enum ImageSize {
		EXTRA_SMALL,
		SMALL,
		NORMAL,
	}

	public static void clearBitmap(Bitmap bm) {
		bm.recycle();
		System.gc();
	}

	public static String createPhotoFilePath(String mTargetDirPath, String mTargetFileName){
		String mTempFilePath = mTargetDirPath + File.separator + mTargetFileName;

		File file = new File(mTempFilePath);
		try {
			file.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return mTempFilePath;
	}

	public static String savePhotoBitmap(Bitmap mBitmap, String mTargetDirPath, String mTargetFileName){
		String mTempFilePath = mTargetDirPath + File.separator + mTargetFileName;

		File file = new File(mTempFilePath);
		OutputStream out = null;

		try
		{
			file.createNewFile();
			out = new FileOutputStream(file);

			// Bitmap.CompressFormat.JPEG ???占쎌쓬
			mBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
			//Bitmap.createScaledBitmap(mBitmap, 480, 640, true);

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				out.flush();
				out.close();
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
		}

		return mTempFilePath;
	}

	public static void savePhotoBitmap(Bitmap mBitmap, String mTargetPath){

		File file = new File(mTargetPath);
		OutputStream out = null;
		try
		{
			file.createNewFile();
			out = new FileOutputStream(file);

			// Bitmap.CompressFormat.JPEG ???占쎌쓬
			// ??占쏙옙 ?占쎌쭏 ?占쏀깮 占?占쏙옙 (?占쎌옱 100%)
			mBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
			//Bitmap.createScaledBitmap(mBitmap, 480, 640, true);

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				out.flush();
				out.close();
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
		}

	}

	public static int GetResourceInt(Activity activity, String resourcePath){

		int resId;
		final Resources resources = activity.getResources();
		final String packageName = activity.getPackageName();

		resId = resources.getIdentifier(resourcePath, "drawable", packageName);

		return resId;
	}
	public static int GetResourceInt(Context context, String resourcePath){

		int resId;
		final Resources resources = context.getResources();
		final String packageName = context.getPackageName();

		resId = resources.getIdentifier(resourcePath, "drawable", packageName);

		return resId;
	}

	public static int[] getBitmapArray(Activity activity, int type, int resource){

		int THUMBNAIL_HEIGHT = 480;
		int THUMBNAIL_WIDTH = 640;

		if(type == 1){
    		/* Filter Image */
			THUMBNAIL_HEIGHT = 480;
			THUMBNAIL_WIDTH = 640;
		}

		if(type == 2){
			THUMBNAIL_HEIGHT = 40;
			THUMBNAIL_WIDTH = 40;
		}

		BitmapFactory.Options myOptions = new BitmapFactory.Options();
		myOptions.inDither = true;
		myOptions.inScaled = false;
		myOptions.inPreferredConfig = Config.ARGB_8888;//important
		//myOptions.inDither = false;
		myOptions.inPurgeable = true;

		Bitmap tempImage =  BitmapFactory.decodeResource(activity.getResources(), resource, myOptions);//important


		float ratio = (float) (THUMBNAIL_WIDTH/THUMBNAIL_HEIGHT);
		Bitmap filter_img = Bitmap.createScaledBitmap(tempImage, (int)(THUMBNAIL_WIDTH*ratio), THUMBNAIL_HEIGHT, false);

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		filter_img.compress(Bitmap.CompressFormat.JPEG, 100, out);

		int filter_width = filter_img.getWidth();
		int filter_height = filter_img.getHeight();

		int[] filter_data = new int[filter_width * filter_height];
		filter_img.getPixels(filter_data, 0, filter_width, 0, 0, filter_width, filter_height);

		try {
			out.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		return filter_data;
	}


	public Bitmap GetBitmap(Activity activity, int resource){

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inDither = true;
		options.inPurgeable = true;

		Bitmap tmp = null;

		try{
			tmp = BitmapFactory.decodeResource(activity.getResources(), resource, options);
		}catch(OutOfMemoryError e){
			options.inSampleSize = 2;
			tmp = BitmapFactory.decodeResource(activity.getResources(), resource, options);
		}

		return tmp;
	}


	public Bitmap GetScaledBitmap(Activity activity, int resource, int width, int height, boolean filter) {

		Bitmap tmp = GetBitmap(activity, resource);
		Bitmap img = Bitmap.createScaledBitmap(tmp, width, height, filter);
		//copy the image to be sure you are not using the same object as the tmp bitmap
		img=img.copy (Config.RGB_565,false);
		if (!tmp.isRecycled()) tmp.recycle();
		tmp = null;
		return img;
	}


	public static int getBitmapOfWidth( String fileName ){
		try {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(fileName, options);
			return options.outWidth;
		} catch(Exception e) {
			return 0;
		}
	}

	public static int getBitmapOfHeight( String fileName ){
		try {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(fileName, options);
			return options.outHeight;
		} catch(Exception e) {
			return 0;
		}
	}

	public static String getBitmapOfAspectRadio(String fileName){
		try {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(fileName, options);
			return getAspectRatio(options.outWidth, options.outHeight);
		} catch(Exception e) {
			return "";
		}
	}

	@SuppressLint("NewApi")
	public static Size getBitmapOfSizeWH(String fileName){
		try {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(fileName, options);

			return new Size(options.outWidth, options.outHeight);
		} catch(Exception e) {
			return new Size(0, 0);
		}
	}

	private static int greatestCommonFactor(int a, int b) {
		while( b > 0 ) {
			int temp = b;
			b = a % b;
			a = temp;
		}
		return a;
	}

	/**
	 * 비율 구하기
	 * @param width
	 * @param height
	 * @return
	 */
	public static String getAspectRatio(int width, int height) {
		int gcf = greatestCommonFactor(width, height);
		if( gcf > 0 ) {
			// had a Google Play crash due to gcf being 0!? Implies width must be zero
			width /= gcf;
			height /= gcf;
		}
		return width + ":" + height;
	}
	public static int getAspectRatioFirst(int width, int height) {
		int gcf = greatestCommonFactor(width, height);
		if( gcf > 0 ) {
			// had a Google Play crash due to gcf being 0!? Implies width must be zero
			width /= gcf;
		}
		return width;
	}
	public static int getAspectRatioSecond(int width, int height) {
		int gcf = greatestCommonFactor(width, height);
		if( gcf > 0 ) {
			height /= gcf;
		}
		return height;
	}

	public static int getAspectRatioHeight(int width, int height){
		int targetWidth = width;
		int gcf = greatestCommonFactor(width, height);
		if( gcf > 0 ) {
			// had a Google Play crash due to gcf being 0!? Implies width must be zero
			width /= gcf;
			height /= gcf;
		}

		int resultHeight = (targetWidth*height)/width;
		return resultHeight;
	}


	/**
	 * 회전시 비율이 반대로 처리됨
	 * @param width
	 * @param height
	 * @return
	 */
	public static int getAspectRatioRotateHeight(int width, int height){
		int targetWidth = width;
		int gcf = greatestCommonFactor(width, height);
		if( gcf > 0 ) {
			// had a Google Play crash due to gcf being 0!? Implies width must be zero
			width /= gcf;
			height /= gcf;
		}

		int resultHeight = (targetWidth*width)/height;
		return resultHeight;
	}



	public static Bitmap drawableToBitmap (Drawable drawable) {
		if (drawable instanceof BitmapDrawable) {
			return ((BitmapDrawable)drawable).getBitmap();
		}
		Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
		drawable.draw(canvas);

		return bitmap;
	}


	public static void savePngFile(Bitmap bmp, String strFilePath)
	{
		File file = new File(strFilePath);
		OutputStream out = null;

		try
		{
			file.createNewFile();
			out = new FileOutputStream(file);

			// Bitmap.CompressFormat.JPEG ???占쎌쓬
			// ??占쏙옙 ?占쎌쭏 ?占쏀깮 占?占쏙옙 (?占쎌옱 100%)
			bmp.compress(Bitmap.CompressFormat.PNG, 100, out);
			Bitmap.createScaledBitmap(bmp, 200, 200, true);

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				out.close();
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
		}
	}

	//CreateFolder()
	public static void createFolder(String targetFolder){
		GomsLog.d(TAG, "createFolder : " + targetFolder);
		try{
			String str = Environment.getExternalStorageState();
			if(str.equals(Environment.MEDIA_MOUNTED)){
				//String mTargetDirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + targetFolder;

				//Android 11(R 30) 이상일 때, private folder 접근이 어려워 public 다운로드 폴더 안에 해당 앱 LedApp/font 폴더 생성
				String mTargetDirPath = AppConstant.DEFAULT_EXTERNAL_STORAGE_DIRECTORY + targetFolder;

				GomsLog.d(TAG, "createFolder : " + mTargetDirPath);

				File file = new File(mTargetDirPath);
				if(!file.exists()){
					GomsLog.d(TAG, "createFolder OK");
					file.mkdirs();
				}else{
				}
			}else{
			}
		}catch(Exception e){
			e.getStackTrace();
		}
	}

	/**
	 * ?占쏙옙?占?? ??占쏙옙???占쎌씪??留뚮뱺??
	 * @return Uri
	 */
	public static Uri createSaveProfileFile(String path, String filename){
		Uri uri;
		//String path = "/sdcard/StrayCat/";
		//String filename = "Profile.jpg";
		//   storage/sdcard0/StrayCat/ is exist
		uri = Uri.fromFile(new File(Environment.getExternalStorageDirectory() + path, filename));
		return uri;
	}

	/**
	 * ?占쏙옙?占?? ??占쏙옙???占쎌씪??留뚮뱺??
	 * @return Uri
	 */
	public static void createTmpProfileFile(Bitmap profileTemp, String path, String filename){
		//String path = "/sdcard/StrayCat/";
		//String filename = "Profile.jpg";
		//   storage/sdcard0/StrayCat/ is exist
		File file = new File(Environment.getExternalStorageDirectory() + path, filename);
		savePngFile(profileTemp, file.getPath());

	}

	/**
	 * ?占쏙옙?占?? ??占쏙옙???占쎌씪??留뚮뱺??
	 * @return Uri
	 */
	public static Uri createSaveAssetProfileFile(String path, String filename){
		Uri uri;
		//String path = "/sdcard/StrayCat/";
		//String filename = "Profile.jpg";
		//   storage/sdcard0/StrayCat/ is exist
		uri = Uri.fromFile(new File(Environment.getExternalStorageDirectory() + path, filename));
		return uri;
	}

	/**
	 * Crop???占쏙옙?占?? ??占쏙옙???占쎌씪??留뚮뱺??
	 * @return Uri
	 */
	public static Uri createSaveCropFile(){
		Uri uri;
		String url = "tmpProfile_" + String.valueOf(System.currentTimeMillis()) + ".jpg";
		uri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), url));
		return uri;
	}

	/**     * ?占쎌씪 蹂듭궗
	 * @param srcFile : 蹂듭궗??File
	 * @param destFile : 蹂듭궗??File
	 * @return
	 */
	public static boolean copyFile(File srcFile, File destFile) {
		boolean result = false;
		try {
			InputStream in = new FileInputStream(srcFile);
			try {
				result = copyToFile(in, destFile);
			}
			finally  {
				in.close();
			}
		} catch (IOException e) {
			result = false;
		}
		return result;
	}

	/**
	 * Copy data from a source stream to destFile.
	 * Return true if succeed, return false if failed.
	 */
	private static boolean copyToFile(InputStream inputStream, File destFile) {
		try {
			OutputStream out = new FileOutputStream(destFile);
			try {
				byte[] buffer = new byte[4096];
				int bytesRead;
				while ((bytesRead = inputStream.read(buffer)) >= 0) {
					out.write(buffer, 0, bytesRead);
				}
			} finally {
				out.close();
			}
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	private static void recycleBitmap(ImageView iv) {
		Drawable d = iv.getDrawable();
		if (d instanceof BitmapDrawable) {
			Bitmap b = ((BitmapDrawable)d).getBitmap();
			b.recycle();
		}
		// ?占쎌옱濡쒖꽌??BitmapDrawable ?占쎌쇅??drawable ?占쎌뿉 ??占쏙옙 吏곸젒?占쎌씤 硫붾え占??占쎌젣??遺덌옙??占쏀븯??
		d.setCallback(null);
	}

	//Highlight image
	//http://xjaphx.wordpress.com/2011/06/20/image-processing-highlight-image-on-the-fly/
	public static Bitmap doHighlightImage(Bitmap src) {
		// create new bitmap, which will be painted and becomes result image
		Bitmap bmOut = Bitmap.createBitmap(src.getWidth() + 96, src.getHeight() + 96, Config.ARGB_8888);
		// setup canvas for painting
		Canvas canvas = new Canvas(bmOut);
		// setup default color
		canvas.drawColor(0, Mode.CLEAR);

		// create a blur paint for capturing alpha
		Paint ptBlur = new Paint();
		ptBlur.setMaskFilter(new BlurMaskFilter(15, Blur.NORMAL));
		int[] offsetXY = new int[2];
		// capture alpha into a bitmap
		Bitmap bmAlpha = src.extractAlpha(ptBlur, offsetXY);
		// create a color paint
		Paint ptAlphaColor = new Paint();
		ptAlphaColor.setColor(0xFFFFFFFF);
		// paint color for captured alpha region (bitmap)
		canvas.drawBitmap(bmAlpha, offsetXY[0], offsetXY[1], ptAlphaColor);
		// free memory
		bmAlpha.recycle();

		// paint the image source
		canvas.drawBitmap(src, 0, 0, null);

		// return out final image
		return bmOut;
	}

	//Inverted Image
	//http://xjaphx.wordpress.com/2011/06/20/image-processing-invert-image-on-the-fly/
	public static Bitmap doInvert(Bitmap src) {
		// create new bitmap with the same settings as source bitmap
		Bitmap bmOut = Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig());
		// color info
		int A, R, G, B;
		int pixelColor;
		// image size
		int height = src.getHeight();
		int width = src.getWidth();

		// scan through every pixel
		for (int y = 0; y < height; y++)
		{
			for (int x = 0; x < width; x++)
			{
				// get one pixel
				pixelColor = src.getPixel(x, y);
				// saving alpha channel
				A = Color.alpha(pixelColor);
				// inverting byte for each R/G/B channel
				R = 255 - Color.red(pixelColor);
				G = 255 - Color.green(pixelColor);
				B = 255 - Color.blue(pixelColor);
				// set newly-inverted pixel to output image
				bmOut.setPixel(x, y, Color.argb(A, R, G, B));
			}
		}

		// return final bitmap
		return bmOut;
	}

	//Grayscale
	//http://xjaphx.wordpress.com/2011/06/21/image-processing-grayscale-image-on-the-fly/
	public static Bitmap doGreyscale(Bitmap src) {
		// constant factors
		final double GS_RED = 0.299;
		final double GS_GREEN = 0.587;
		final double GS_BLUE = 0.114;

		// create output bitmap
		Bitmap bmOut = Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig());
		// pixel information
		int A, R, G, B;
		int pixel;

		// get image size
		int width = src.getWidth();
		int height = src.getHeight();

		// scan through every single pixel
		for(int x = 0; x < width; ++x) {
			for(int y = 0; y < height; ++y) {
				// get one pixel color
				pixel = src.getPixel(x, y);
				// retrieve color of all channels
				A = Color.alpha(pixel);
				R = Color.red(pixel);
				G = Color.green(pixel);
				B = Color.blue(pixel);
				// take conversion up to one single value
				R = G = B = (int)(GS_RED * R + GS_GREEN * G + GS_BLUE * B);
				// set new pixel color to output bitmap
				bmOut.setPixel(x, y, Color.argb(A, R, G, B));
			}
		}

		// return final image
		return bmOut;
	}

	//gamma correction
	//http://xjaphx.wordpress.com/2011/06/21/image-processing-gamma-correction-on-the-fly/
	//red 1.8, green 1.8 blue 1.8
	public static Bitmap doGamma(Bitmap src, double red, double green, double blue) {
		// create output image
		Bitmap bmOut = Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig());
		// get image size
		int width = src.getWidth();
		int height = src.getHeight();
		// color information
		int A, R, G, B;
		int pixel;
		// constant value curve
		final int    MAX_SIZE = 256;
		final double MAX_VALUE_DBL = 255.0;
		final int    MAX_VALUE_INT = 255;
		final double REVERSE = 1.0;

		// gamma arrays
		int[] gammaR = new int[MAX_SIZE];
		int[] gammaG = new int[MAX_SIZE];
		int[] gammaB = new int[MAX_SIZE];

		// setting values for every gamma channels
		for(int i = 0; i < MAX_SIZE; ++i) {
			gammaR[i] = (int)Math.min(MAX_VALUE_INT,
					(int)((MAX_VALUE_DBL * Math.pow(i / MAX_VALUE_DBL, REVERSE / red)) + 0.5));
			gammaG[i] = (int)Math.min(MAX_VALUE_INT,
					(int)((MAX_VALUE_DBL * Math.pow(i / MAX_VALUE_DBL, REVERSE / green)) + 0.5));
			gammaB[i] = (int)Math.min(MAX_VALUE_INT,
					(int)((MAX_VALUE_DBL * Math.pow(i / MAX_VALUE_DBL, REVERSE / blue)) + 0.5));
		}

		// apply gamma table
		for(int x = 0; x < width; ++x) {
			for(int y = 0; y < height; ++y) {
				// get pixel color
				pixel = src.getPixel(x, y);
				A = Color.alpha(pixel);
				// look up gamma
				R = gammaR[Color.red(pixel)];
				G = gammaG[Color.green(pixel)];
				B = gammaB[Color.blue(pixel)];
				// set new color to output bitmap
				bmOut.setPixel(x, y, Color.argb(A, R, G, B));
			}
		}

		// return final image
		return bmOut;
	}

	//Filter
	//http://xjaphx.wordpress.com/2011/06/21/image-processing-filter-color-channels/
	// red = green = blue = 150%
	public static Bitmap doColorFilter(Bitmap src, double red, double green, double blue) {
		// image size
		int width = src.getWidth();
		int height = src.getHeight();
		// create output bitmap
		Bitmap bmOut = Bitmap.createBitmap(width, height, src.getConfig());
		// color information
		int A, R, G, B;
		int pixel;

		// scan through all pixels
		for(int x = 0; x < width; ++x) {
			for(int y = 0; y < height; ++y) {
				// get pixel color
				pixel = src.getPixel(x, y);
				// apply filtering on each channel R, G, B
				A = Color.alpha(pixel);
				R = (int)(Color.red(pixel) * red);
				G = (int)(Color.green(pixel) * green);
				B = (int)(Color.blue(pixel) * blue);
				// set new color pixel to output bitmap
				bmOut.setPixel(x, y, Color.argb(A, R, G, B));
			}
		}

		// return final image
		return bmOut;
	}

	//sepia toning
	//http://xjaphx.wordpress.com/2011/06/21/image-processing-photography-sepia-toning-effect/
	// 0-255 for depth
	public static Bitmap createSepiaToningEffect(Bitmap src, int depth, double red, double green, double blue) {
		// image size
		int width = src.getWidth();
		int height = src.getHeight();
		// create output bitmap
		Bitmap bmOut = Bitmap.createBitmap(width, height, src.getConfig());
		// constant grayscale
		final double GS_RED = 0.3;
		final double GS_GREEN = 0.59;
		final double GS_BLUE = 0.11;
		// color information
		int A, R, G, B;
		int pixel;

		// scan through all pixels
		for(int x = 0; x < width; ++x) {
			for(int y = 0; y < height; ++y) {
				// get pixel color
				pixel = src.getPixel(x, y);
				// get color on each channel
				A = Color.alpha(pixel);
				R = Color.red(pixel);
				G = Color.green(pixel);
				B = Color.blue(pixel);
				// apply grayscale sample
				B = G = R = (int)(GS_RED * R + GS_GREEN * G + GS_BLUE * B);

				// apply intensity level for sepid-toning on each channel
				R += (depth * red);
				if(R > 255) { R = 255; }

				G += (depth * green);
				if(G > 255) { G = 255; }

				B += (depth * blue);
				if(B > 255) { B = 255; }

				// set new pixel color to output image
				bmOut.setPixel(x, y, Color.argb(A, R, G, B));
			}
		}

		// return final image
		return bmOut;
	}

	//decreasing color
	//http://xjaphx.wordpress.com/2011/06/21/image-processing-decreasing-color-depth/
	//bitOffset = 32, 64, 128
	public static Bitmap decreaseColorDepth(Bitmap src, int bitOffset) {
		// get image size
		int width = src.getWidth();
		int height = src.getHeight();
		// create output bitmap
		Bitmap bmOut = Bitmap.createBitmap(width, height, src.getConfig());
		// color information
		int A, R, G, B;
		int pixel;

		// scan through all pixels
		for(int x = 0; x < width; ++x) {
			for(int y = 0; y < height; ++y) {
				// get pixel color
				pixel = src.getPixel(x, y);
				A = Color.alpha(pixel);
				R = Color.red(pixel);
				G = Color.green(pixel);
				B = Color.blue(pixel);

				// round-off color offset
				R = ((R + (bitOffset / 2)) - ((R + (bitOffset / 2)) % bitOffset) - 1);
				if(R < 0) { R = 0; }
				G = ((G + (bitOffset / 2)) - ((G + (bitOffset / 2)) % bitOffset) - 1);
				if(G < 0) { G = 0; }
				B = ((B + (bitOffset / 2)) - ((B + (bitOffset / 2)) % bitOffset) - 1);
				if(B < 0) { B = 0; }

				// set pixel color to output bitmap
				bmOut.setPixel(x, y, Color.argb(A, R, G, B));
			}
		}

		// return final image
		return bmOut;
	}

	/*
	 *  contrast
	 *  100
	 *  http://xjaphx.wordpress.com/2011/06/21/image-processing-contrast-image-on-the-fly/
	 */
	public static Bitmap createContrast(Bitmap src, double value) {
		// image size
		int width = src.getWidth();
		int height = src.getHeight();
		// create output bitmap
		Bitmap bmOut = Bitmap.createBitmap(width, height, src.getConfig());
		// color information
		int A, R, G, B;
		int pixel;
		// get contrast value
		double contrast = Math.pow((100 + value) / 100, 2);

		// scan through all pixels
		for(int x = 0; x < width; ++x) {
			for(int y = 0; y < height; ++y) {
				// get pixel color
				pixel = src.getPixel(x, y);
				A = Color.alpha(pixel);
				// apply filter contrast for every channel R, G, B
				R = Color.red(pixel);
				R = (int)(((((R / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
				if(R < 0) { R = 0; }
				else if(R > 255) { R = 255; }

				G = Color.red(pixel);
				G = (int)(((((G / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
				if(G < 0) { G = 0; }
				else if(G > 255) { G = 255; }

				B = Color.red(pixel);
				B = (int)(((((B / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
				if(B < 0) { B = 0; }
				else if(B > 255) { B = 255; }

				// set new pixel color to output bitmap
				bmOut.setPixel(x, y, Color.argb(A, R, G, B));
			}
		}

		// return final image
		return bmOut;
	}

	/*
	 * Rotation
	 * Degree 45, 90, 135, 270
	 * http://xjaphx.wordpress.com/2011/06/22/image-processing-rotate-image-on-the-fly/
	 */
	public static Bitmap rotate(Bitmap src, float degree) {
		// create new matrix
		Matrix matrix = new Matrix();
		// setup rotation degree
		matrix.postRotate(degree);

		// return new bitmap rotated using matrix
		return Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
	}

	/*
	 * Brightness
	 * 60, -60
	 * http://xjaphx.wordpress.com/2011/06/22/image-processing-brightness-over-image/
	 */

	public static Bitmap doBrightness(Bitmap src, int value) {
		// image size
		int width = src.getWidth();
		int height = src.getHeight();
		// create output bitmap
		Bitmap bmOut = Bitmap.createBitmap(width, height, src.getConfig());
		// color information
		int A, R, G, B;
		int pixel;

		// scan through all pixels
		for(int x = 0; x < width; ++x) {
			for(int y = 0; y < height; ++y) {
				// get pixel color
				pixel = src.getPixel(x, y);
				A = Color.alpha(pixel);
				R = Color.red(pixel);
				G = Color.green(pixel);
				B = Color.blue(pixel);

				// increase/decrease each channel
				R += value;
				if(R > 255) { R = 255; }
				else if(R < 0) { R = 0; }

				G += value;
				if(G > 255) { G = 255; }
				else if(G < 0) { G = 0; }

				B += value;
				if(B > 255) { B = 255; }
				else if(B < 0) { B = 0; }

				// apply new pixel color to output bitmap
				bmOut.setPixel(x, y, Color.argb(A, R, G, B));
			}
		}

		// return final image
		return bmOut;
	}



	/*
	 * color-boost-up
	 * type(R,G,B) 1,2,3 / percent 67%
	 * http://xjaphx.wordpress.com/2011/06/23/image-processing-color-boost-up/
	 */
	public static Bitmap boost(Bitmap src, int type, float percent) {
		int width = src.getWidth();
		int height = src.getHeight();
		Bitmap bmOut = Bitmap.createBitmap(width, height, src.getConfig());

		int A, R, G, B;
		int pixel;

		for(int x = 0; x < width; ++x) {
			for(int y = 0; y < height; ++y) {
				pixel = src.getPixel(x, y);
				A = Color.alpha(pixel);
				R = Color.red(pixel);
				G = Color.green(pixel);
				B = Color.blue(pixel);
				if(type == 1) {
					R = (int)(R * (1 + percent));
					if(R > 255) R = 255;
				}
				else if(type == 2) {
					G = (int)(G * (1 + percent));
					if(G > 255) G = 255;
				}
				else if(type == 3) {
					B = (int)(B * (1 + percent));
					if(B > 255) B = 255;
				}
				bmOut.setPixel(x, y, Color.argb(A, R, G, B));
			}
		}
		return bmOut;
	}

	/*
	 * round-corner
	 * 45
	 * http://xjaphx.wordpress.com/2011/06/23/image-processing-image-with-round-corner/
	 */
	public static Bitmap roundCorner(Bitmap src, float round) {
		// image size
		int width = src.getWidth();
		int height = src.getHeight();
		// create bitmap output
		Bitmap result = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		// set canvas for painting
		Canvas canvas = new Canvas(result);
		canvas.drawARGB(0, 0, 0, 0);

		// config paint
		final Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setColor(Color.BLACK);

		// config rectangle for embedding
		final Rect rect = new Rect(0, 0, width, height);
		final RectF rectF = new RectF(rect);

		// draw rect to canvas
		canvas.drawRoundRect(rectF, round, round, paint);

		// create Xfer mode
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		// draw source image to canvas
		canvas.drawBitmap(src, rect, rect, paint);

		// return final image
		return result;
	}

	/*
	 * watermarking
	 *
	 * http://xjaphx.wordpress.com/2011/06/23/image-processing-watermarking-on-the-fly/
	 */
	public static Bitmap mark(Bitmap src, String watermark, Point location, int alpha, int size, boolean underline) {
		int w = src.getWidth();
		int h = src.getHeight();
		Bitmap result = Bitmap.createBitmap(w, h, src.getConfig());

		Canvas canvas = new Canvas(result);
		canvas.drawBitmap(src, 0, 0, null);

		Paint paint = new Paint();
		//paint.setColor(color);
		paint.setColor(Color.WHITE);
		paint.setAlpha(alpha);
		paint.setTextSize(size);
		paint.setAntiAlias(true);
		paint.setUnderlineText(underline);
		canvas.drawText(watermark, location.x, location.y, paint);

		return result;
	}

	/*
	 * flipping
	 * ?占쎌쭅, ?占쏀룊 / 1, 2
	 * http://xjaphx.wordpress.com/2011/06/26/image-processing-image-flipping-mirroring/
	 */

	public static Bitmap flip(Bitmap src, int type) {
		// create new matrix for transformation
		Matrix matrix = new Matrix();
		// if vertical
		if(type == 1) {
			// y = y * -1
			matrix.preScale(1.0f, -1.0f);
		}
		// if horizonal
		else if(type == 2) {
			// x = x * -1
			matrix.preScale(-1.0f, 1.0f);
			// unknown type
		} else {
			return null;
		}

		// return transformed image
		return Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
	}

	/*
	 * noise
	 * http://xjaphx.wordpress.com/2011/10/30/image-processing-flea-noise-effect/
	 */
	public static final int COLOR_MIN = 0x00;
	public static final int COLOR_MAX = 0xFF;

	public static Bitmap applyFleaEffect(Bitmap source) {
		// get image size
		int width = source.getWidth();
		int height = source.getHeight();
		int[] pixels = new int[width * height];
		// get pixel array from source
		source.getPixels(pixels, 0, width, 0, 0, width, height);
		// a random object
		Random random = new Random();

		int index = 0;
		// iteration through pixels
		for(int y = 0; y < height; ++y) {
			for(int x = 0; x < width; ++x) {
				// get current index in 2D-matrix
				index = y * width + x;
				// get random color
				int randColor = Color.rgb(random.nextInt(COLOR_MAX),
						random.nextInt(COLOR_MAX), random.nextInt(COLOR_MAX));
				// OR
				pixels[index] |= randColor;
			}
		}
		// output bitmap
		Bitmap bmOut = Bitmap.createBitmap(width, height, source.getConfig());
		bmOut.setPixels(pixels, 0, width, 0, 0, width, height);
		return bmOut;
	}

	/*
	 * black filter
	 * http://xjaphx.wordpress.com/2011/10/30/image-processing-black-filter-increasing-the-darkness/
	 */
	public static Bitmap applyBlackFilter(Bitmap source) {
		// get image size
		int width = source.getWidth();
		int height = source.getHeight();
		int[] pixels = new int[width * height];
		// get pixel array from source
		source.getPixels(pixels, 0, width, 0, 0, width, height);
		// random object
		Random random = new Random();

		int R, G, B, index = 0, thresHold = 0;
		// iteration through pixels
		for(int y = 0; y < height; ++y) {
			for(int x = 0; x < width; ++x) {
				// get current index in 2D-matrix
				index = y * width + x;
				// get color
				R = Color.red(pixels[index]);
				G = Color.green(pixels[index]);
				B = Color.blue(pixels[index]);
				// generate threshold
				thresHold = random.nextInt(COLOR_MAX);
				if(R < thresHold && G < thresHold && B < thresHold) {
					pixels[index] = Color.rgb(COLOR_MIN, COLOR_MIN, COLOR_MIN);
				}
			}
		}
		// output bitmap
		Bitmap bmOut = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		bmOut.setPixels(pixels, 0, width, 0, 0, width, height);
		return bmOut;
	}

	/*
	 * snow
	 * http://xjaphx.wordpress.com/2011/10/30/image-processing-snow-effect/
	 */
	public static Bitmap applySnowEffect(Bitmap source) {
		// get image size
		int width = source.getWidth();
		int height = source.getHeight();
		int[] pixels = new int[width * height];
		// get pixel array from source
		source.getPixels(pixels, 0, width, 0, 0, width, height);
		// random object
		Random random = new Random();

		int R, G, B, index = 0, thresHold = 50;
		// iteration through pixels
		for(int y = 0; y < height; ++y) {
			for(int x = 0; x < width; ++x) {
				// get current index in 2D-matrix
				index = y * width + x;
				// get color
				R = Color.red(pixels[index]);
				G = Color.green(pixels[index]);
				B = Color.blue(pixels[index]);
				// generate threshold
				thresHold = random.nextInt(COLOR_MAX);
				if(R > thresHold && G > thresHold && B > thresHold) {
					pixels[index] = Color.rgb(COLOR_MAX, COLOR_MAX, COLOR_MAX);
				}
			}
		}
		// output bitmap
		Bitmap bmOut = Bitmap.createBitmap(width, height, Config.RGB_565);
		bmOut.setPixels(pixels, 0, width, 0, 0, width, height);
		return bmOut;
	}

	/*
	 * shading
	 * 3?占쏀븯 占? -15000
	 * http://xjaphx.wordpress.com/2011/10/30/image-processing-shading-filter/
	 */
	public static Bitmap applyShadingFilter(Bitmap source, int shadingColor) {
		// get image size
		int width = source.getWidth();
		int height = source.getHeight();
		int[] pixels = new int[width * height];
		// get pixel array from source
		source.getPixels(pixels, 0, width, 0, 0, width, height);

		int index = 0;
		// iteration through pixels
		for(int y = 0; y < height; ++y) {
			for(int x = 0; x < width; ++x) {
				// get current index in 2D-matrix
				index = y * width + x;
				// AND
				pixels[index] &= shadingColor;
			}
		}
		// output bitmap
		Bitmap bmOut = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		bmOut.setPixels(pixels, 0, width, 0, 0, width, height);
		return bmOut;
	}

	/*
	 * saturation
	 * 1
	 * http://xjaphx.wordpress.com/2011/10/30/image-processing-saturation-filter/
	 */
	public static Bitmap applySaturationFilter(Bitmap source, int level) {
		// get image size
		int width = source.getWidth();
		int height = source.getHeight();
		int[] pixels = new int[width * height];
		float[] HSV = new float[3];
		// get pixel array from source
		source.getPixels(pixels, 0, width, 0, 0, width, height);

		int index = 0;
		// iteration through pixels
		for(int y = 0; y < height; ++y) {
			for(int x = 0; x < width; ++x) {
				// get current index in 2D-matrix
				index = y * width + x;
				// convert to HSV
				Color.colorToHSV(pixels[index], HSV);
				// increase Saturation level
				HSV[1] *= level;
				HSV[1] = (float) Math.max(0.0, Math.min(HSV[1], 1.0));
				// take color back
				pixels[index] |= Color.HSVToColor(HSV);
			}
		}
		// output bitmap
		Bitmap bmOut = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		bmOut.setPixels(pixels, 0, width, 0, 0, width, height);
		return bmOut;
	}

	/*
	 * Hue
	 * 1~10
	 * http://xjaphx.wordpress.com/2011/10/30/image-processing-hue-filter/
	 */
	public static Bitmap applyHueFilter(Bitmap source, int level) {
		// get image size
		int width = source.getWidth();
		int height = source.getHeight();
		int[] pixels = new int[width * height];
		float[] HSV = new float[3];
		// get pixel array from source
		source.getPixels(pixels, 0, width, 0, 0, width, height);

		int index = 0;
		// iteration through pixels
		for(int y = 0; y < height; ++y) {
			for(int x = 0; x < width; ++x) {
				// get current index in 2D-matrix
				index = y * width + x;
				// convert to HSV
				Color.colorToHSV(pixels[index], HSV);
				// increase Saturation level
				HSV[0] *= level;
				HSV[0] = (float) Math.max(0.0, Math.min(HSV[0], 360.0));
				// take color back
				pixels[index] |= Color.HSVToColor(HSV);
			}
		}
		// output bitmap
		Bitmap bmOut = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		bmOut.setPixels(pixels, 0, width, 0, 0, width, height);
		return bmOut;
	}

	/*
	 * reflection
	 * http://xjaphx.wordpress.com/2011/11/01/image-processing-image-reflection-effect/
	 */
	public static Bitmap applyReflection(Bitmap originalImage) {
		// gap space between original and reflected
		final int reflectionGap = 4;
		// get image size
		int width = originalImage.getWidth();
		int height = originalImage.getHeight();

		// this will not scale but will flip on the Y axis
		Matrix matrix = new Matrix();
		matrix.preScale(1, -1);

		// create a Bitmap with the flip matrix applied to it.
		// we only want the bottom half of the image
		Bitmap reflectionImage = Bitmap.createBitmap(originalImage, 0, height/2, width, height/2, matrix, false);

		// create a new bitmap with same width but taller to fit reflection
		Bitmap bitmapWithReflection = Bitmap.createBitmap(width, (height + height/2), Config.ARGB_8888);

		// create a new Canvas with the bitmap that's big enough for
		// the image plus gap plus reflection
		Canvas canvas = new Canvas(bitmapWithReflection);
		// draw in the original image
		canvas.drawBitmap(originalImage, 0, 0, null);
		// draw in the gap
		Paint defaultPaint = new Paint();
		canvas.drawRect(0, height, width, height + reflectionGap, defaultPaint);
		// draw in the reflection
		canvas.drawBitmap(reflectionImage,0, height + reflectionGap, null);

		// create a shader that is a linear gradient that covers the reflection
		Paint paint = new Paint();
		LinearGradient shader = new LinearGradient(0, originalImage.getHeight(), 0,
				bitmapWithReflection.getHeight() + reflectionGap, 0x70ffffff, 0x00ffffff,
				TileMode.CLAMP);
		// set the paint to use this shader (linear gradient)
		paint.setShader(shader);
		// set the Transfer mode to be porter duff and destination in
		paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
		// draw a rectangle using the paint with our linear gradient
		canvas.drawRect(0, height, width, bitmapWithReflection.getHeight() + reflectionGap, paint);

		return bitmapWithReflection;
	}

	//鰲믢만
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
				.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = pixels;

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}


	public static Bitmap getBitmapFromURL(String src) {
		try {
			URL url = new URL(src);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoInput(true);
			connection.connect();
			InputStream input = connection.getInputStream();
			Bitmap myBitmap = BitmapFactory.decodeStream(input);
			//Bitmap myBitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
			return myBitmap;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}


	public static Bitmap rotateBitmap(Bitmap bmp) {
		Matrix matrix = new Matrix();
		matrix.postRotate(270.0f); // 270
		return Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
	}


	public static void fadeInAnimation(ImageView fadeInImage){
		AlphaAnimation fadeIn = new AlphaAnimation(0, 1);
		fadeIn.setDuration(200);
		fadeInImage.startAnimation(fadeIn);
	}

	public static void fadeOutAnimation(final ImageView fadeOutImage){
		AlphaAnimation fadeOut = new AlphaAnimation(1, 0);
		fadeOut.setDuration(200);
		fadeOutImage.startAnimation(fadeOut);
		fadeOut.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {	}

			@Override
			public void onAnimationRepeat(Animation animation) {}

			@Override
			public void onAnimationEnd(Animation animation) {
				fadeOutImage.setVisibility(View.INVISIBLE);
			}
		});
	}

	/**
	 * @return true if the MimeType type is image
	 */
	public static boolean isImageMimeType(String mimeType) {
		return mimeType != null && mimeType.startsWith("image/");
	}

	/**
	 * Create a bitmap from a local URI
	 *
	 * @param resolver The ContentResolver
	 * @param uri The local URI
	 * @param maxSize The maximum size (either width or height)
	 *
	 * @return The new bitmap or null
	 */
	public static Bitmap createLocalBitmap(ContentResolver resolver, Uri uri, int maxSize) {
		InputStream inputStream = null;
		try {
			final BitmapFactory.Options opts = new BitmapFactory.Options();
			final Point bounds = getImageBounds(resolver, uri);

			inputStream = resolver.openInputStream(uri);
			opts.inSampleSize = Math.max(bounds.x / maxSize, bounds.y / maxSize);

			final Bitmap decodedBitmap = decodeStream(inputStream, null, opts);

			return decodedBitmap;

		} catch (FileNotFoundException exception) {
			// Do nothing - the photo will appear to be missing
		} catch (IOException exception) {
			// Do nothing - the photo will appear to be missing
		} catch (IllegalArgumentException exception) {
			// Do nothing - the photo will appear to be missing
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (IOException ignore) {
			}
		}
		return null;
	}

	public static int calculateInSampleSize(
			BitmapFactory.Options options, int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			// Calculate ratios of height and width to requested height and width
			final int heightRatio = Math.round((float) height / (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);

			// Choose the smallest ratio as inSampleSize value, this will guarantee
			// a final image with both dimensions larger than or equal to the
			// requested height and width.
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}

		return inSampleSize;
	}

	/*
     * mImageView.setImageBitmap(decodeSampledBitmapFromResource(getResources(), R.id.myimage, 100, 100));
     */
	public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
														 int reqWidth, int reqHeight) {

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(res, resId, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeResource(res, resId, options);
	}

	/**
	 * Wrapper around {@link BitmapFactory#decodeStream(InputStream, Rect,
	 * BitmapFactory.Options)} that returns {@code null} on {@link
	 * OutOfMemoryError}.
	 *
	 * @param is The input stream that holds the raw data to be decoded into a
	 *           bitmap.
	 * @param outPadding If not null, return the padding rect for the bitmap if
	 *                   it exists, otherwise set padding to [-1,-1,-1,-1]. If
	 *                   no bitmap is returned (null) then padding is
	 *                   unchanged.
	 * @param opts null-ok; Options that control downsampling and whether the
	 *             image should be completely decoded, or just is size returned.
	 * @return The decoded bitmap, or null if the image data could not be
	 *         decoded, or, if opts is non-null, if opts requested only the
	 *         size be returned (in opts.outWidth and opts.outHeight)
	 */
	public static Bitmap decodeStream(InputStream is, Rect outPadding, BitmapFactory.Options opts) {
		ByteArrayOutputStream out = null;
		try {
			out = new ByteArrayOutputStream();
			final byte[] buffer = new byte[4096];
			int n = is.read(buffer);
			while (n >= 0) {
				out.write(buffer, 0, n);
				n = is.read(buffer);
			}
			final byte[] bitmapBytes = out.toByteArray();

			// Determine the orientation for this image
			final int orientation = ExifUtil.getOrientation(bitmapBytes);
			final Bitmap originalBitmap =
					BitmapFactory.decodeByteArray(bitmapBytes, 0, bitmapBytes.length, opts);

			if (originalBitmap != null && orientation != 0) {
				final Matrix matrix = new Matrix();
				matrix.postRotate(orientation);
				return Bitmap.createBitmap(originalBitmap, 0, 0, originalBitmap.getWidth(),
						originalBitmap.getHeight(), matrix, true);
			}
			return originalBitmap;
		} catch (OutOfMemoryError oome) {
			return null;
		} catch (IOException ioe) {
			return null;
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					// Do nothing
				}
			}
		}
	}

	/**
	 * Gets the image bounds
	 *
	 * @param resolver The ContentResolver
	 * @param uri The uri
	 *
	 * @return The image bounds
	 */
	private static Point getImageBounds(ContentResolver resolver, Uri uri)
			throws IOException {
		final BitmapFactory.Options opts = new BitmapFactory.Options();
		InputStream inputStream = null;

		try {
			opts.inJustDecodeBounds = true;
			inputStream = resolver.openInputStream(uri);
			decodeStream(inputStream, null, opts);

			return new Point(opts.outWidth, opts.outHeight);
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (IOException ignore) {
			}
		}
	}

	private static Drawable getDrawableFromUrl(final String url) throws IOException, MalformedURLException {
		return Drawable.createFromStream(((InputStream) new URL(url).getContent()), "name");
	}

	@SuppressWarnings("unused")
	private Bitmap adjustOpacity(Bitmap bitmap, int opacity)
	{
		Bitmap mutableBitmap = bitmap.isMutable() ? bitmap : bitmap.copy(Config.ARGB_8888, true);
		Canvas canvas = new Canvas(mutableBitmap);
		int colour = (opacity & 0xFF) << 24;
		canvas.drawColor(colour, Mode.DST_IN);
		return mutableBitmap;
	}

	public static Uri saveMediaEntry(Context cx, String imagePath,String title,String description,double lati, double longi, long dateTaken,int orientation) {
		ContentValues v = new ContentValues();
		v.put(Images.Media.TITLE, title);
		v.put(Images.Media.DISPLAY_NAME, title);
		v.put(Images.Media.DESCRIPTION, description);
		v.put(Images.Media.DATE_ADDED, dateTaken);
		v.put(Images.Media.DATE_TAKEN, dateTaken);
		v.put(Images.Media.LATITUDE, lati);
		v.put(Images.Media.LONGITUDE, longi);
		v.put(Images.Media.DATE_MODIFIED, dateTaken) ;
		v.put(Images.Media.MIME_TYPE, "image/jpeg");
    	/*v.put(Images.Media.ORIENTATION, orientation);*/

		File f = new File(imagePath) ;
		File parent = f.getParentFile() ;
		String path = parent.toString().toLowerCase() ;
		String name = parent.getName().toLowerCase() ;
		v.put(Images.ImageColumns.BUCKET_ID, path.hashCode());
		v.put(Images.ImageColumns.BUCKET_DISPLAY_NAME, name);
		v.put(Images.Media.SIZE,f.length()) ;
		f = null ;

		v.put("_data",imagePath) ;
		ContentResolver c = cx.getContentResolver() ;
		return c.insert(Images.Media.EXTERNAL_CONTENT_URI, v);
	}

	public static Uri saveMediaEntry(Context cx, String imagePath,String title,String description,long dateTaken,double lati, double longi, int orientation,Location loc) {
		ContentValues v = new ContentValues();
		v.put(Images.Media.TITLE, title);
		v.put(Images.Media.DISPLAY_NAME, title);
		v.put(Images.Media.DESCRIPTION, description);
		v.put(Images.Media.DATE_ADDED, dateTaken);
		v.put(Images.Media.DATE_TAKEN, dateTaken);
		v.put(Images.Media.LATITUDE, lati);
		v.put(Images.Media.LONGITUDE, longi);
		v.put(Images.Media.DATE_MODIFIED, dateTaken) ;
		v.put(Images.Media.MIME_TYPE, "image/jpeg");
		v.put(Images.Media.ORIENTATION, orientation);

		File f = new File(imagePath) ;
		File parent = f.getParentFile() ;
		String path = parent.toString().toLowerCase() ;
		String name = parent.getName().toLowerCase() ;
		v.put(Images.ImageColumns.BUCKET_ID, path.hashCode());
		v.put(Images.ImageColumns.BUCKET_DISPLAY_NAME, name);
		v.put(Images.Media.SIZE,f.length()) ;
		f = null ;

		if( loc != null ) {
			v.put(Images.Media.LATITUDE, loc.getLatitude());
			v.put(Images.Media.LONGITUDE, loc.getLongitude());
		}
		v.put("_data",imagePath) ;
		ContentResolver c = cx.getContentResolver() ;
		return c.insert(Images.Media.EXTERNAL_CONTENT_URI, v);
	}

	public static Uri saveMediaEntry(Context cx, String imagePath, String title,String description, long dateTaken) {
		ContentValues v = new ContentValues();
		v.put(Images.Media.TITLE, title);
		v.put(Images.Media.DISPLAY_NAME, title);
		v.put(Images.Media.DESCRIPTION, description);
		v.put(Images.Media.DATE_ADDED, dateTaken);
		v.put(Images.Media.DATE_TAKEN, dateTaken);
		v.put(Images.Media.DATE_MODIFIED, dateTaken) ;
		v.put(Images.Media.MIME_TYPE, "image/jpeg");

		File f = new File(imagePath) ;
		File parent = f.getParentFile() ;
		String path = parent.toString().toLowerCase() ;
		String name = parent.getName().toLowerCase() ;
		v.put(Images.ImageColumns.BUCKET_ID, path.hashCode());
		v.put(Images.ImageColumns.BUCKET_DISPLAY_NAME, name);
		v.put(Images.Media.SIZE,f.length()) ;
		f = null ;

		v.put("_data",imagePath) ;
		ContentResolver c = cx.getContentResolver() ;
		return c.insert(Images.Media.EXTERNAL_CONTENT_URI, v);
	}

	public static Uri saveMediaEntry(Context cx, String imagePath, String title,String description, long dateTaken,int orientation) {
		ContentValues v = new ContentValues();
		v.put(Images.Media.TITLE, title);
		v.put(Images.Media.DISPLAY_NAME, title);
		v.put(Images.Media.DESCRIPTION, description);
		v.put(Images.Media.DATE_ADDED, dateTaken);
		v.put(Images.Media.DATE_TAKEN, dateTaken);
		v.put(Images.Media.DATE_MODIFIED, dateTaken) ;
		v.put(Images.Media.MIME_TYPE, "image/jpeg");
		v.put(Images.Media.ORIENTATION, orientation);

		File f = new File(imagePath) ;
		File parent = f.getParentFile() ;
		String path = parent.toString().toLowerCase() ;
		String name = parent.getName().toLowerCase() ;
		v.put(Images.ImageColumns.BUCKET_ID, path.hashCode());
		v.put(Images.ImageColumns.BUCKET_DISPLAY_NAME, name);
		v.put(Images.Media.SIZE,f.length()) ;
		f = null ;

		v.put("_data",imagePath) ;
		ContentResolver c = cx.getContentResolver() ;
		return c.insert(Images.Media.EXTERNAL_CONTENT_URI, v);
	}


	public static Uri saveMediaVideoEntry(Context cx, String imagePath, String title,String description, long dateTaken, String mimeType) {
		ContentValues v = new ContentValues();
		v.put(MediaStore.Video.Media.TITLE, title);
		v.put(MediaStore.Video.Media.DISPLAY_NAME, title);
		v.put(MediaStore.Video.Media.DESCRIPTION, description);
		v.put(MediaStore.Video.Media.DATE_ADDED, dateTaken);
		v.put(MediaStore.Video.Media.DATE_TAKEN, dateTaken);
		v.put(MediaStore.Video.Media.DATE_MODIFIED, dateTaken) ;
		v.put(MediaStore.Video.Media.MIME_TYPE, mimeType);
		v.put(MediaStore.Video.Media.ARTIST, AppConstant.APP_NAME);

		File f = new File(imagePath) ;
		File parent = f.getParentFile() ;
		String path = parent.toString().toLowerCase() ;
		String name = parent.getName().toLowerCase() ;
		v.put(MediaStore.Video.VideoColumns.BUCKET_ID, path.hashCode());
		v.put(MediaStore.Video.VideoColumns.BUCKET_DISPLAY_NAME, name);
		v.put(MediaStore.Video.Media.SIZE,f.length()) ;
		f = null ;

		v.put("_data",imagePath) ;
		ContentResolver c = cx.getContentResolver() ;
		return c.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, v);
	}

	public static Bitmap decodeSampledBitmapFromUri(String path, int reqWidth, int reqHeight) {

		Bitmap bm = null;
		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		bm = BitmapFactory.decodeFile(path, options);

		return bm;
	}

	public static int[] extractIntArrayFromImage(Resources res, int id, int thumb_width, int thumb_height) {

		BitmapFactory.Options myOptions = new BitmapFactory.Options();
		myOptions.inDither = true;
		myOptions.inScaled = false;
		myOptions.inPreferredConfig = Config.ARGB_8888;//important
		//myOptions.inDither = false;
		myOptions.inPurgeable = true;

		Bitmap tempImage =  BitmapFactory.decodeResource(res, id, myOptions);//important

		float ratio = (float) (thumb_width/thumb_height);
		Bitmap filter_img = Bitmap.createScaledBitmap(tempImage, (int)(thumb_width*ratio), thumb_height, false);

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		filter_img.compress(Bitmap.CompressFormat.JPEG, 100, out);

		int filter_width = filter_img.getWidth();
		int filter_height = filter_img.getHeight();

		int[] data = new int[filter_width * filter_height];
		filter_img.getPixels(data, 0, filter_width, 0, 0, filter_width, filter_height);

		return data;
	}

	public static int[] getBitmapToIntArray(Bitmap bitmap) {

		int filter_width = bitmap.getWidth();
		int filter_height = bitmap.getHeight();

		int[] data = new int[filter_width * filter_height];
		bitmap.getPixels(data, 0, filter_width, 0, 0, filter_width, filter_height);

		return data;
	}

	/**
	 * Bitmap?占쎌꽌 byte 諛곗뿴??異붿텧?占쎈떎.
	 * @param bitmap ?占쎈낯 鍮꾪듃占??占쏙옙?占?
	 * @return byte[] BGRA ?占쎌씠?? 1pixel??4byte
	 */
	public static byte[] extractByteArrayFromImage(Bitmap bitmap) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		// int[] ?占쎌씠??異붿텧(BGRA)
		int[] data = new int[width * height];
		bitmap.getPixels(data, 0, width, 0, 0, width, height);
		return convertIntArrayToByteArray(data, width, height);
	}

	/**
	 * 1?占쏙옙???4Byte占?Integer占??占쎌뼱?占쎈뒗 寃껋쓣 Byte占?占?占쏙옙
	 * @param rgb 1?占쏙옙???4Byte(int)占????占쏙옙? ?占쎌씠??諛곗뿴
	 * @param width
	 * @param height
	 * @return
	 */
	public static byte[] convertIntArrayToByteArray(int[] rgb, int width,
													int height) {

		try {
			// the size of the BMP file in bytes
			int fileSize = height * width * 4;

			ByteArrayOutputStream bytes = new ByteArrayOutputStream(fileSize);
			DataOutputStream out = new DataOutputStream(bytes);
			int colorValue = 0;
			for (int j = height - 1; j >= 0; j--) {
				for (int i = 0; i < width; i++) {
					colorValue = rgb[i + width * j];

					out.writeByte(colorValue & 0x000000FF);
					out.writeByte((colorValue >>> 8) & 0x000000FF);
					out.writeByte((colorValue >>> 16) & 0x000000FF);
					out.writeByte((colorValue >>> 24) & 0x000000FF);
				}
			}

			byte[] encodeBytes = bytes.toByteArray();
			bytes.close();
			return encodeBytes;
		} catch (IOException e) {
			return null;
		}
	}

	public static Bitmap getARGBImage(Activity activity, Bitmap bitmap)
	{

		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Config.ARGB_8888;

		int width = bitmap.getWidth();
		int height = bitmap.getHeight();

		Bitmap result = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		result.eraseColor(Color.BLACK);

		Canvas c = new Canvas(result);

		Bitmap alpha = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		int[] alphaPix = new int[width * height];
		bitmap.getPixels(alphaPix, 0, width, 0, 0, width, height);

		int count = width * height;
		for (int i = 0; i < count; ++i)
		{
			alphaPix[i] = alphaPix[i] << 8;
		}
		alpha.setPixels(alphaPix, 0, width, 0, 0, width, height);

		Paint alphaP = new Paint();
		alphaP.setAntiAlias(true);
		alphaP.setXfermode(new PorterDuffXfermode(Mode.DST_IN));

		c.drawBitmap(alpha, 0, 0, alphaP);

		alpha.recycle();

		return result;
	}

	public static boolean hasSDCard() { // SD????????
		String status = Environment.getExternalStorageState();
		return status.equals(Environment.MEDIA_MOUNTED);
	}
	public static String getSDCardPath() {
		File path = Environment.getExternalStorageDirectory();
		return path.getAbsolutePath();
	}

	public static Bitmap getImageFromURL(String imageURL){
		Bitmap imgBitmap = null;
		HttpURLConnection conn = null;
		BufferedInputStream bis = null;

		try
		{
			URL url = new URL(imageURL);
			conn = (HttpURLConnection)url.openConnection();
			conn.connect();

			int nSize = conn.getContentLength();
			bis = new BufferedInputStream(conn.getInputStream(), nSize);
			imgBitmap = BitmapFactory.decodeStream(bis);
		}
		catch (Exception e){
			e.printStackTrace();
		} finally{
			if(bis != null) {
				try {bis.close();} catch (IOException e) {}
			}
			if(conn != null ) {
				conn.disconnect();
			}
		}

		return imgBitmap;
	}

	public static Bitmap getBitmapFromView(View v, int width, int height) {
		Bitmap b = Bitmap.createBitmap( width, height, Config.ARGB_8888);
		Canvas c = new Canvas(b);
		v.layout(0, 0, width, height);
		v.draw(c);
		return b;
	}

	/*
     * Resizing image size
     */
	public static Bitmap decodeFile(String filePath, int WIDTH, int HIGHT) {
		try {

			File f = new File(filePath);

			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(f), null, o);

			final int REQUIRED_WIDTH = WIDTH;
			final int REQUIRED_HIGHT = HIGHT;
			int scale = 1;
			while (o.outWidth / scale / 2 >= REQUIRED_WIDTH
					&& o.outHeight / scale / 2 >= REQUIRED_HIGHT)
				scale *= 2;

			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}



	public static Bitmap rotateSimpleBitmap(Bitmap source, int angle)
	{
		Matrix matrix = new Matrix();
		matrix.postRotate(angle);
		return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
	}

	public static Bitmap rotateBitmap(Bitmap bitmap, int orientation) {

		Matrix matrix = new Matrix();
		switch (orientation) {
			case ExifInterface.ORIENTATION_NORMAL:	// 媛?줈 1 ?뺤긽
				return bitmap;
			case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
				matrix.setScale(-1, 1);
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:	// 媛?줈 3 ?뺤긽
				matrix.setRotate(180);
				//matrix.setRotate(270);
				break;
			case ExifInterface.ORIENTATION_FLIP_VERTICAL:
				matrix.setRotate(180);
				matrix.postScale(-1, 1);
				break;
			case ExifInterface.ORIENTATION_TRANSPOSE:
				matrix.setRotate(90);
				matrix.postScale(-1, 1);
				break;
			case ExifInterface.ORIENTATION_ROTATE_90:	// ?몃줈 6 鍮꾩젙??
				matrix.setRotate(90);
				break;
			case ExifInterface.ORIENTATION_TRANSVERSE:
				matrix.setRotate(-90);
				matrix.postScale(-1, 1);
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				matrix.setRotate(-90);
				break;
			default:
				return bitmap;
		}
		try {
			Bitmap bmRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
			bitmap.recycle();
			return bmRotated;
		}
		catch (OutOfMemoryError e) {
			e.printStackTrace();
			return null;
		}
	}

	public Uri getImageUri(Context inContext, Bitmap inImage) {
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
		String path = Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
		return Uri.parse(path);
	}

	public static String getRealPathFromURI(Context context, Uri contentUri) {
		Cursor cursor = null;
		try {
			String[] proj = { Images.Media.DATA};
			cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
			int column_index = cursor.getColumnIndexOrThrow(Images.Media.DATA);
			cursor.moveToFirst();
			return cursor.getString(column_index);
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
	}

	public static Uri getUriFromPath(Context context, String path){
		//String fileName= "file:///sdcard/DCIM/Camera/2013_07_07_12345.jpg";

		Uri fileUri = Uri.parse( path );
		String filePath = fileUri.getPath();

		Cursor cursor = null;
		try {
			cursor = context.getContentResolver().query(Images.Media.EXTERNAL_CONTENT_URI, null, "_data = '" + filePath + "'", null, null);
			cursor.moveToNext();
			@SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("_id"));
			Uri uri = ContentUris.withAppendedId(Images.Media.EXTERNAL_CONTENT_URI, id);
			return uri;
		}catch(CursorIndexOutOfBoundsException e){
			return null;
		}finally{
			if (cursor != null) {
				cursor.close();
			}
		}
	}

	private static Bitmap createBitmap(int _width, int _height){
		Bitmap bitmap = Bitmap.createBitmap(_width, _width, Config.ARGB_8888);
		return bitmap;

	}


	public static class ImageInfo {
		public final String filePath;
		public final String imageOrientation;

		public ImageInfo(String filePath, String imageOrientation) {
			this.filePath = filePath;

			if (imageOrientation == null) imageOrientation = "0";
			this.imageOrientation = imageOrientation;
		}
	}

	public static FileInputStream getSourceStream(Context context, Uri u) throws FileNotFoundException {
		FileInputStream out = null;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			ParcelFileDescriptor parcelFileDescriptor =
					context.getContentResolver().openFileDescriptor(u, "r");
			FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
			out = new FileInputStream(fileDescriptor);
		} else {
			out = (FileInputStream) context.getContentResolver().openInputStream(u);
		}
		return out;
	}

	/** getResizedBitmap method is used to Resized the Image according to custom width and height
	 * @param image
	 * @param newHeight (new desired height)
	 * @param newWidth (new desired Width)
	 * @return image (new resized image)
	 * */
	public static Bitmap getResizedBitmap(Bitmap image, int newHeight, int newWidth) {
		int width = image.getWidth();
		int height = image.getHeight();
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		// create a matrix for the manipulation
		Matrix matrix = new Matrix();
		// resize the bit map
		matrix.postScale(scaleWidth, scaleHeight);
		// recreate the new Bitmap
		Bitmap resizedBitmap = Bitmap.createBitmap(image, 0, 0, width, height,
				matrix, false);
		return resizedBitmap;
	}

	/**
	 * reduces the size of the image
	 * @param image
	 * @param maxSize
	 * @return
	 */
	public static Bitmap getResizedBitmap(Bitmap image, int maxSize) {
		int width = image.getWidth();
		int height = image.getHeight();

		float bitmapRatio = (float)width / (float) height;
		if (bitmapRatio > 0) {
			width = maxSize;
			height = (int) (width / bitmapRatio);
		} else {
			height = maxSize;
			width = (int) (height * bitmapRatio);
		}
		return Bitmap.createScaledBitmap(image, width, height, true);
	}

	// byte[] InputStream
	public static InputStream Byte2InputStream(byte[] b) {
		ByteArrayInputStream bais = new ByteArrayInputStream(b);
		return bais;
	}

	// InputStream byte[]
	public static byte[] InputStream2Bytes(InputStream is) {
		String str = "";
		byte[] readByte = new byte[1024];
		int readCount = -1;
		try {
			while ((readCount = is.read(readByte, 0, 1024)) != -1) {
				str += new String(readByte).trim();
			}
			return str.getBytes();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// Bitmap InputStream
	public static InputStream Bitmap2InputStream(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		InputStream is = new ByteArrayInputStream(baos.toByteArray());
		return is;
	}

	// Bitmap InputStream
	public static InputStream Bitmap2InputStream(Bitmap bm, int quality) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, quality, baos);
		InputStream is = new ByteArrayInputStream(baos.toByteArray());
		return is;
	}

	// InputStream Bitmap
	public static Bitmap InputStream2Bitmap(InputStream is) {
		return BitmapFactory.decodeStream(is);
	}

	// Drawable InputStream
	public static InputStream Drawable2InputStream(Drawable d) {
		Bitmap bitmap = drawable2Bitmap(d);
		return Bitmap2InputStream(bitmap);
	}

	// InputStream Drawable
	public static Drawable InputStream2Drawable(InputStream is) {
		Bitmap bitmap = InputStream2Bitmap(is);
		return bitmap2Drawable(bitmap);
	}

	// Drawable byte[]
	public static byte[] Drawable2Bytes(Drawable d) {
		Bitmap bitmap = drawable2Bitmap(d);
		return Bitmap2Bytes(bitmap);
	}

	// byte[] Drawable
	public static Drawable Bytes2Drawable(byte[] b) {
		Bitmap bitmap = Bytes2Bitmap(b);
		return bitmap2Drawable(bitmap);
	}

	// Bitmap byte[]
	public static byte[] Bitmap2Bytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}

	// byte[] Bitmap
	public static Bitmap Bytes2Bitmap(byte[] b) {
		if (b.length != 0) {
			return BitmapFactory.decodeByteArray(b, 0, b.length);
		}
		return null;
	}

	// Drawable Bitmap
	public static Bitmap drawable2Bitmap(Drawable drawable) {
		Bitmap bitmap = Bitmap
				.createBitmap(
						drawable.getIntrinsicWidth(),
						drawable.getIntrinsicHeight(),
						drawable.getOpacity() != PixelFormat.OPAQUE ? Config.ARGB_8888
								: Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
				drawable.getIntrinsicHeight());
		drawable.draw(canvas);
		return bitmap;
	}

	// Bitmap Drawable
	public static Drawable bitmap2Drawable(Bitmap bitmap) {
		BitmapDrawable bd = new BitmapDrawable(bitmap);
		Drawable d = (Drawable) bd;
		return d;
	}

	public static String getFileNameByUri(Context context, Uri uri)
	{
		String fileName="unknown";//default fileName
		Uri filePathUri = uri;
		if (uri.getScheme().toString().compareTo("content")==0)
		{
			Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
			if (cursor.moveToFirst())
			{
				int column_index = cursor.getColumnIndexOrThrow(Images.Media.DATA);//Instead of "MediaStore.Images.Media.DATA" can be used "_data"
				filePathUri = Uri.parse(cursor.getString(column_index));
				fileName = filePathUri.getLastPathSegment().toString();
			}
		}
		else if (uri.getScheme().compareTo("file")==0)
		{
			fileName = filePathUri.getLastPathSegment().toString();
		}
		else
		{
			fileName = fileName+"_"+filePathUri.getLastPathSegment();
		}
		return fileName;
	}

	/**
	 * Get a file path from a Uri. This will get the the path for Storage Access
	 * Framework Documents, as well as the _data field for the MediaStore and
	 * other file-based ContentProviders.
	 *
	 * @param context The context.
	 * @param uri The Uri to query.
	 * @author paulburke
	 */
	@SuppressLint("NewApi")
	public static String getAlbumPath(final Context context, final Uri uri) {

		final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

		// DocumentProvider
		if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
			// ExternalStorageProvider
			if (isExternalStorageDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				if ("primary".equalsIgnoreCase(type)) {
					return Environment.getExternalStorageDirectory() + "/" + split[1];
				}

				// TODO handle non-primary volumes
			}
			// DownloadsProvider
			else if (isDownloadsDocument(uri)) {

				final String id = DocumentsContract.getDocumentId(uri);
				final Uri contentUri = ContentUris.withAppendedId(
						Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

				return getDataColumn(context, contentUri, null, null);
			}
			// MediaProvider
			else if (isMediaDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				Uri contentUri = null;
				if ("image".equals(type)) {
					contentUri = Images.Media.EXTERNAL_CONTENT_URI;
				} else if ("video".equals(type)) {
					contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
				} else if ("audio".equals(type)) {
					contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
				}

				final String selection = "_id=?";
				final String[] selectionArgs = new String[] {
						split[1]
				};

				return getDataColumn(context, contentUri, selection, selectionArgs);
			}
		}
		// MediaStore (and general)
		else if ("content".equalsIgnoreCase(uri.getScheme())) {
			return getDataColumn(context, uri, null, null);
		}
		// File
		else if ("file".equalsIgnoreCase(uri.getScheme())) {
			return uri.getPath();
		}

		return null;
	}

	/**
	 * Get the value of the data column for this Uri. This is useful for
	 * MediaStore Uris, and other file-based ContentProviders.
	 *
	 * @param context The context.
	 * @param uri The Uri to query.
	 * @param selection (Optional) Filter used in the query.
	 * @param selectionArgs (Optional) Selection arguments used in the query.
	 * @return The value of the _data column, which is typically a file path.
	 */
	public static String getDataColumn(Context context, Uri uri, String selection,
									   String[] selectionArgs) {

		Cursor cursor = null;
		final String column = "_data";
		final String[] projection = {
				column
		};

		try {
			cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
					null);
			if (cursor != null && cursor.moveToFirst()) {
				final int column_index = cursor.getColumnIndexOrThrow(column);
				return cursor.getString(column_index);
			}
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return null;
	}


	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is ExternalStorageProvider.
	 */
	public static boolean isExternalStorageDocument(Uri uri) {
		return "com.android.externalstorage.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is DownloadsProvider.
	 */
	public static boolean isDownloadsDocument(Uri uri) {
		return "com.android.providers.downloads.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is MediaProvider.
	 */
	public static boolean isMediaDocument(Uri uri) {
		return "com.android.providers.media.documents".equals(uri.getAuthority());
	}

	// grab the name of the media from the Uri
	public static String getName(Context context, Uri uri)
	{
		String filename = null;

		try {
			String[] projection = { Images.Media.DISPLAY_NAME };
			Cursor cursor =  context.getContentResolver().query(uri, projection, null, null, null);

			if(cursor != null && cursor.moveToFirst()){
				int column_index = cursor.getColumnIndexOrThrow(Images.Media.DISPLAY_NAME);
				filename = cursor.getString(column_index);
			} else {
				filename = null;
			}
		} catch (Exception e) {
			Log.e(TAG, "Error getting file name: " + e.getMessage());
		}

		return filename;
	}

	public static Bitmap scaleBitmapResize(Context context, String imgFilePath) throws Exception, OutOfMemoryError{
		if(!FileUtil.isFileExist(imgFilePath)){
			throw new FileNotFoundException("File error : " + imgFilePath);
		}

		Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		int displayWidth = display.getWidth();
		int displayHeight = display.getHeight();

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPreferredConfig = Config.RGB_565;
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(imgFilePath, options);

		float widthScale = options.outWidth / displayWidth;
		float heightScale = options.outHeight / displayHeight;
		float scale = widthScale > heightScale ? widthScale : heightScale;

		if(scale >= 8)
			options.inSampleSize = 8;
		else if (scale >= 6)
			options.inSampleSize = 6;
		else if (scale >= 4)
			options.inSampleSize = 4;
		else if (scale >= 2)
			options.inSampleSize = 2;
		else
			options.inSampleSize = 1;
		options.inJustDecodeBounds = false;

		return BitmapFactory.decodeFile(imgFilePath, options);
	}

	public static String getRealPathFromURI2(Context context, Uri uri){
		String realPath = null;

		GomsLog.d(TAG, "sdk : " + Build.VERSION.SDK_INT);

		if (Build.VERSION.SDK_INT < 11)
			realPath = getRealPathFromURI_BelowAPI11(context, uri);

			// SDK >= 11 && SDK < 19
		else if (Build.VERSION.SDK_INT < 19)
			realPath = getRealPathFromURI_API11to18(context, uri);

			// SDK > 19 (Android 4.4)
		else
			realPath = getRealPathFromURI_API19(context, uri);

		return realPath;
	}

	@SuppressLint("NewApi")
	public static String getRealPathFromURI_API19(Context context, Uri uri){

		GomsLog.d(TAG, "uri.getScheme() : " + uri.getScheme());

		if( DocumentsContract.isDocumentUri(context, uri)) {

			GomsLog.d(TAG, "DocumentsContract file : " + uri.getPath());
			GomsLog.d(TAG, "DocumentsContract file : " + Uri.decode(uri.getPath()));

			String filePath = "";
			String wholeID = DocumentsContract.getDocumentId(uri);

			// Split at colon, use second item in the array
			String id = wholeID.split(":")[1];

			String[] column = { Images.Media.DATA };

			// where id is equal to
			String sel = Images.Media._ID + "=?";

			Cursor cursor = context.getContentResolver().query(Images.Media.EXTERNAL_CONTENT_URI,
					column, sel, new String[]{ id }, null);

			int columnIndex = cursor.getColumnIndex(column[0]);

			if (cursor.moveToFirst()) {
				filePath = cursor.getString(columnIndex);
			}
			cursor.close();

			return filePath;

		}else if ("content".equalsIgnoreCase(uri.getScheme())) {

			GomsLog.d(TAG, "uri.getAuthority() : " + uri.getAuthority());

			// Return the remote address
			if (isGooglePhotosUri(uri)){
				return uri.getLastPathSegment();
			}

			GomsLog.d(TAG, "Uri.decode(getDataColumn(context, uri, null, null): " + Uri.decode(getDataColumn(context, uri, null, null)));
			return Uri.decode(getDataColumn(context, uri, null, null));

		}else if ("file".equalsIgnoreCase(uri.getScheme())) {

			GomsLog.d(TAG, "file : " + uri.getPath());
			GomsLog.d(TAG, "file : " + Uri.decode(uri.getPath()));
			String returnPath = Uri.decode(uri.getPath());
			return returnPath;
		}

		return null;


	}

	public static boolean isGooglePhotosUri(Uri uri) {
		return "com.google.android.apps.photos.content".equals(uri.getAuthority());
	}

	@SuppressLint("NewApi")
	public static String getRealPathFromURI_API11to18(Context context, Uri contentUri) {

		String[] proj = { Images.Media.DATA };
		String result = null;

		CursorLoader cursorLoader = new CursorLoader(
				context,
				contentUri, proj, null, null, null);
		Cursor cursor = cursorLoader.loadInBackground();

		if(cursor != null){
			int column_index =
					cursor.getColumnIndexOrThrow(Images.Media.DATA);
			cursor.moveToFirst();
			result = cursor.getString(column_index);
		}
		return result;
	}

	public static String getRealPathFromURI_BelowAPI11(Context context, Uri contentUri){
		String[] proj = { Images.Media.DATA };
		Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
		int column_index
				= cursor.getColumnIndexOrThrow(Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}

	/** opengl 2.0 createBitmap
	 *  GLLayer.java???ъ슜
	 * */
	public static Bitmap createBitmap(final int[] pixels, final int width, final int height, final Config config) {
		final Paint paint = new Paint(Paint.FILTER_BITMAP_FLAG);
		paint.setColorFilter(new ColorMatrixColorFilter(new ColorMatrix(new float[] {
				0, 0, 1, 0, 0,
				0, 1, 0, 0, 0,
				1, 0, 0, 0, 0,
				0, 0, 0, 1, 0
		})));

		final Bitmap bitmap = Bitmap.createBitmap(width, height, config);
		final Canvas canvas = new Canvas(bitmap);

		final Matrix matrix = new Matrix();
		matrix.postScale(1.0f, -1.0f);
		matrix.postTranslate(0, height);
		canvas.concat(matrix);

		canvas.drawBitmap(pixels, 0, width, 0, 0, width, height, false, paint);

		return bitmap;
	}

	public static Bitmap decodeBitmapFromFile(String imagePath, int reqWidth, int reqHeight) {
		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(imagePath, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateSampleSize(options, reqWidth, reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(imagePath, options);
	}

	private static int calculateSampleSize(BitmapFactory.Options options, int reqHeight, int reqWidth) {

		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			final int halfHeight = height / 2;
			final int halfWidth = width / 2;

			// Calculate the largest inSampleSize value that is a power of 2 and
			// keeps both
			// height and width larger than the requested height and width.
			while ((halfHeight / inSampleSize) > reqHeight
					&& (halfWidth / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}
		}

		return inSampleSize;

	}

	/**
	 * Get thumbnail as a bitmap from a path given a resolution
	 * @param path
	 * @param thumbnailSize
	 * @return
	 */
	public static Bitmap getThumbnailBitmap(String path, int thumbnailSize) {
		BitmapFactory.Options bounds = new BitmapFactory.Options();
		bounds.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, bounds);
		if ((bounds.outWidth == -1) || (bounds.outHeight == -1)) {
			return null;
		}
		int originalSize = (bounds.outHeight > bounds.outWidth) ? bounds.outHeight
				: bounds.outWidth;
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inSampleSize = originalSize / thumbnailSize;
		return BitmapFactory.decodeFile(path, opts);
	}

	/**
	 * Use for decoding camera response data.
	 *
	 * Example call:
	 * Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
	 startActivityForResult(i, R.integer.PHOTO_SELECT_ACTION);
	 * @param data
	 * @param context
	 * @return
	 */
	public static Bitmap getBitmapFromCameraData(Intent data, Context context) {
		Uri selectedImage = data.getData();
		String[] filePathColumn = { Images.Media.DATA };
		Cursor cursor = context.getContentResolver().query(selectedImage, filePathColumn, null, null, null);
		cursor.moveToFirst();
		int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
		String picturePath = cursor.getString(columnIndex);
		cursor.close();
		return BitmapFactory.decodeFile(picturePath);
	}

	/**
	 * Fill hex string with "0" when hexString minor than F.
	 * @param hexString
	 * @return
	 */
	public static String beautyHexString(String hexString) {
		if (hexString.length() < 2) {
			return "0".concat(hexString);
		} else {
			return hexString;
		}
	}

	/**
	 * Find components of color of the bitmap at x, y.
	 * @param x Distance from left border of the View
	 * @param y Distance from top of the View
	 * @param view Touched surface on screen
	 */
	public static int findColor(View view, int x, int y) throws NullPointerException {

		int red = 0;
		int green = 0;
		int blue = 0;
		int color = 0;

		int offset = 1; // 3x3 Matrix
		int pixelsNumber = 0;

		int xImage = 0;
		int yImage = 0;

		// Get the bitmap from the view.
		ImageView imageView = (ImageView) view;
		BitmapDrawable bitmapDrawable = (BitmapDrawable) imageView.getDrawable();
		Bitmap imageBitmap = bitmapDrawable.getBitmap();

		// Calculate the target in the bitmap.
		xImage = (int) (x * ((double) imageBitmap.getWidth() / (double) imageView.getWidth()));
		yImage = (int) (y * ((double) imageBitmap.getHeight() / (double) imageView.getHeight()));

		// Average of pixels color around the center of the touch.
		for (int i = xImage - offset; i <= xImage + offset; i++) {
			for (int j = yImage - offset; j <= yImage + offset; j++) {
				try {
					color = imageBitmap.getPixel(i, j);
					red += Color.red(color);
					green += Color.green(color);
					blue += Color.blue(color);
					pixelsNumber += 1;
				} catch (Exception e) {
					// Log.w(TAG, "Error picking color!");
				}
			}
		}
		red = red / pixelsNumber;
		green = green / pixelsNumber;
		blue = blue / pixelsNumber;

		return Color.rgb(red, green, blue);
	}

	public static void findBitmapColor(View view, int x, int y) throws NullPointerException {
		// Get the bitmap from the view.
		ImageView imageView = (ImageView) view;
		BitmapDrawable bitmapDrawable = (BitmapDrawable) imageView.getDrawable();
		Bitmap imageBitmap = bitmapDrawable.getBitmap();
		int pixel = imageBitmap.getPixel(x,y);

		int redValue = Color.red(pixel);
		int blueValue = Color.blue(pixel);
		int greenValue = Color.green(pixel);

		GomsLog.d(TAG, "redValue : " + redValue);
		GomsLog.d(TAG, "blueValue : " + blueValue);
		GomsLog.d(TAG, "greenValue : " + greenValue);
	}

	public static Bitmap getWBIMG(Bitmap _bm, int _x, int _y) {
		int _imgW = _bm.getWidth();
		int _imgH = _bm.getHeight();

		int[] _mOrgRGBBuffer = new int[_imgW * _imgH];
		int[] _mOrgRBuffer = new int[_imgW * _imgH];
		int[] _mOrgGBuffer = new int[_imgW * _imgH];
		int[] _mOrgBBuffer = new int[_imgW * _imgH];

		int[] _mNewRGBBuffer = new int[_imgW * _imgH];

		// Create bitmap
		Bitmap _mNewIMG = Bitmap.createBitmap(_imgW, _imgH, Config.ARGB_8888);
		try {
			// 이미지 Pixel Color 얻기
			_bm.getPixels(_mOrgRGBBuffer, 0, _imgW, 0, 0, _imgW, _imgH);
		} catch(Exception e) {
		}

		// 목표 색상 획득 (영상내에서 터치한 위치의 RGB값을 얻어왔다고 가정)
		int _nColor = _bm.getPixel(_x, _y);
		int nR = Color.red(_nColor);
		int nG = Color.green(_nColor);
		int nB = Color.blue(_nColor);

		// RGB값 중 가장 큰 값을 maxValue로 (기준값으로 사용됨)
		int maxValue = Math.max(Math.max(nR, nG), nB);

		///////////////////////////////////////////////////////////////////////////////////////////////////////
		// 영상처리 (line 48 ~ line 68)
		///////////////////////////////////////////////////////////////////////////////////////////////////////
		// >>>>> Step 1. 화이트 밸런스 조정

		// 처리한 영상을 RGB 채널 별로 각각 분리
		// _nColor <== buffer

		// 각 채널의 모든 화소값에 기준값(maxValue)과 터치한 위치의 채널값의 차를 더하는 방법으로
		// R, G, B 값의 밸런스를 기준값을 참고하여 조정함
		int _vR = maxValue - nR;
		int _vG = maxValue - nG;
		int _vB = maxValue - nB;
		int _tR, _tG, _tB;
		for(int i = 0; i < _mOrgRGBBuffer.length; i++) {
			_tR = Color.red(_mOrgRGBBuffer[i]) + _vR;
			_tG = Color.green(_mOrgRGBBuffer[i]) + _vG;
			_tB = Color.blue(_mOrgRGBBuffer[i]) + _vB;
			if (_tR > 255) _mOrgRBuffer[i] = 255;
			else _mOrgRBuffer[i] = Color.red(_mOrgRGBBuffer[i]) + _vR;
			if (_tG > 255) _mOrgGBuffer[i] = 255;
			else _mOrgGBuffer[i] = Color.green(_mOrgRGBBuffer[i]) + _vG;
			if (_tB > 255) _mOrgBBuffer[i] = 255;
			else _mOrgBBuffer[i] = Color.blue(_mOrgRGBBuffer[i]) + _vB;
		}

		// >>>>> Step 2. 블랙 영역(Range) 보정
		// 화이트 밸런스를 조정하면서 변경된 블랙 영역을 선형변환을 통해 보정(x~255 range to 0~255 range)
		for(int i = 0; i < _mOrgRGBBuffer.length; i++) {
			_mNewRGBBuffer[i] = Color.rgb(
					// Red
					(int)Math.round(
							(255.0 *
									(_mOrgRBuffer[i] - (maxValue - nR)) /
									(float)(255.0 - (maxValue - nR))
							)
					),
					// Green
					(int)Math.round(
							(255.0 *
									(_mOrgGBuffer[i] - (maxValue - nG)) /
									(float)(255.0 - (maxValue - nG))
							)
					),
					// Blue
					(int)Math.round(
							(255.0 *
									(_mOrgBBuffer[i] - (maxValue - nB)) /
									(float)(255.0 - (maxValue - nB))
							)
					)
			);
		}

		_mNewIMG.setPixels(_mNewRGBBuffer, 0, _imgW, 0, 0, _imgW, _imgH);

		return _mNewIMG;
	}

	public static Bitmap getWBIMG(Bitmap _bm, int _color) {
		int _imgW = _bm.getWidth();
		int _imgH = _bm.getHeight();

		int[] _mOrgRGBBuffer = new int[_imgW * _imgH];
		int[] _mOrgRBuffer = new int[_imgW * _imgH];
		int[] _mOrgGBuffer = new int[_imgW * _imgH];
		int[] _mOrgBBuffer = new int[_imgW * _imgH];

		int[] _mNewRGBBuffer = new int[_imgW * _imgH];

		// Create bitmap
		Bitmap _mNewIMG = Bitmap.createBitmap(_imgW, _imgH, Config.ARGB_8888);
		try {
			// 이미지 Pixel Color 얻기
			_bm.getPixels(_mOrgRGBBuffer, 0, _imgW, 0, 0, _imgW, _imgH);
		} catch(Exception e) {
		}

		// 목표 색상 획득 (영상내에서 터치한 위치의 RGB값을 얻어왔다고 가정)
		int _nColor = _color;
		int nR = Color.red(_nColor);
		int nG = Color.green(_nColor);
		int nB = Color.blue(_nColor);

		// RGB값 중 가장 큰 값을 maxValue로 (기준값으로 사용됨)
		int maxValue = Math.max(Math.max(nR, nG), nB);

		///////////////////////////////////////////////////////////////////////////////////////////////////////
		// 영상처리 (line 48 ~ line 68)
		///////////////////////////////////////////////////////////////////////////////////////////////////////
		// >>>>> Step 1. 화이트 밸런스 조정

		// 처리한 영상을 RGB 채널 별로 각각 분리
		// _nColor <== buffer

		// 각 채널의 모든 화소값에 기준값(maxValue)과 터치한 위치의 채널값의 차를 더하는 방법으로
		// R, G, B 값의 밸런스를 기준값을 참고하여 조정함
		int _vR = maxValue - nR;
		int _vG = maxValue - nG;
		int _vB = maxValue - nB;
		int _tR, _tG, _tB;
		for(int i = 0; i < _mOrgRGBBuffer.length; i++) {
			_tR = Color.red(_mOrgRGBBuffer[i]) + _vR;
			_tG = Color.green(_mOrgRGBBuffer[i]) + _vG;
			_tB = Color.blue(_mOrgRGBBuffer[i]) + _vB;
			if (_tR > 255) _mOrgRBuffer[i] = 255;
			else _mOrgRBuffer[i] = Color.red(_mOrgRGBBuffer[i]) + _vR;
			if (_tG > 255) _mOrgGBuffer[i] = 255;
			else _mOrgGBuffer[i] = Color.green(_mOrgRGBBuffer[i]) + _vG;
			if (_tB > 255) _mOrgBBuffer[i] = 255;
			else _mOrgBBuffer[i] = Color.blue(_mOrgRGBBuffer[i]) + _vB;
		}

		// >>>>> Step 2. 블랙 영역(Range) 보정
		// 화이트 밸런스를 조정하면서 변경된 블랙 영역을 선형변환을 통해 보정(x~255 range to 0~255 range)
		for(int i = 0; i < _mOrgRGBBuffer.length; i++) {
			_mNewRGBBuffer[i] = Color.rgb(
					// Red
					(int)Math.round(
							(255.0 *
									(_mOrgRBuffer[i] - (maxValue - nR)) /
									(float)(255.0 - (maxValue - nR))
							)
					),
					// Green
					(int)Math.round(
							(255.0 *
									(_mOrgGBuffer[i] - (maxValue - nG)) /
									(float)(255.0 - (maxValue - nG))
							)
					),
					// Blue
					(int)Math.round(
							(255.0 *
									(_mOrgBBuffer[i] - (maxValue - nB)) /
									(float)(255.0 - (maxValue - nB))
							)
					)
			);
		}

		_mNewIMG.setPixels(_mNewRGBBuffer, 0, _imgW, 0, 0, _imgW, _imgH);

		return _mNewIMG;
	}

	/**
	 * Decode an image into a Bitmap, using sub-sampling if the hinted dimensions call for it.
	 * Does not crop to fit the hinted dimensions.
	 *
	 * @param src an encoded image
	 * @param w hint width in px
	 * @param h hint height in px
	 * @return a decoded Bitmap that is not exactly sized to the hinted dimensions.
	 */
	public static Bitmap decodeByteArray(byte[] src, int w, int h) {
		try {
			// calculate sample size based on w/h
			final BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true;
			BitmapFactory.decodeByteArray(src, 0, src.length, opts);
			if (opts.mCancel || opts.outWidth == -1 || opts.outHeight == -1) {
				return null;
			}
			opts.inSampleSize = Math.min(opts.outWidth / w, opts.outHeight / h);
			opts.inJustDecodeBounds = false;
			return BitmapFactory.decodeByteArray(src, 0, src.length, opts);
		} catch (Throwable t) {
			return null;
		}
	}
	/**
	 * Decode an image into a Bitmap, using sub-sampling if the desired dimensions call for it.
	 * Also applies a center-crop a la {@link ImageView.ScaleType#CENTER_CROP}.
	 *
	 * @param src an encoded image
	 * @param w desired width in px
	 * @param h desired height in px
	 * @return an exactly-sized decoded Bitmap that is center-cropped.
	 */
	public static Bitmap decodeByteArrayWithCenterCrop(byte[] src, int w, int h) {
		try {
			final Bitmap decoded = decodeByteArray(src, w, h);
			return centerCrop(decoded, w, h);
		} catch (Throwable t) {
			return null;
		}
	}
	/**
	 * Returns a new Bitmap copy with a center-crop effect a la
	 * {@link ImageView.ScaleType#CENTER_CROP}. May return the input bitmap if no
	 * scaling is necessary.
	 *
	 * @param src original bitmap of any size
	 * @param w desired width in px
	 * @param h desired height in px
	 * @return a copy of src conforming to the given width and height, or src itself if it already
	 *         matches the given width and height
	 */
	public static Bitmap centerCrop(final Bitmap src, final int w, final int h) {
		return crop(src, w, h, 0.5f, 0.5f);
	}
	/**
	 * Returns a new Bitmap copy with a crop effect depending on the crop anchor given. 0.5f is like
	 * {@link ImageView.ScaleType#CENTER_CROP}. The crop anchor will be be nudged
	 * so the entire cropped bitmap will fit inside the src. May return the input bitmap if no
	 * scaling is necessary.
	 *
	 *
	 * Example of changing verticalCenterPercent:
	 *   _________               _________
	 *  |             |              |              |
	 *  |             |              |_________|
	 *  |             |              |              |/___0.3f
	 *  |---------|              |_________|\
	 *  |             |<---0.5f  |               |
	 *  |---------|              |              |
	 *  |             |             |               |
	 *  |             |             |               |
	 *  |________|             |_________|
	 *
	 * @param src original bitmap of any size
	 * @param w desired width in px
	 * @param h desired height in px
	 * @param horizontalCenterPercent determines which part of the src to crop from. Range from 0
	 *                                .0f to 1.0f. The value determines which part of the src
	 *                                maps to the horizontal center of the resulting bitmap.
	 * @param verticalCenterPercent determines which part of the src to crop from. Range from 0
	 *                              .0f to 1.0f. The value determines which part of the src maps
	 *                              to the vertical center of the resulting bitmap.
	 * @return a copy of src conforming to the given width and height, or src itself if it already
	 *         matches the given width and height
	 */
	public static Bitmap crop(final Bitmap src, final int w, final int h,
							  final float horizontalCenterPercent, final float verticalCenterPercent) {
		if (horizontalCenterPercent < 0 || horizontalCenterPercent > 1 || verticalCenterPercent < 0
				|| verticalCenterPercent > 1) {
			throw new IllegalArgumentException(
					"horizontalCenterPercent and verticalCenterPercent must be between 0.0f and "
							+ "1.0f, inclusive.");
		}
		final int srcWidth = src.getWidth();
		final int srcHeight = src.getHeight();
		// exit early if no resize/crop needed
		if (w == srcWidth && h == srcHeight) {
			return src;
		}
		final Matrix m = new Matrix();
		final float scale = Math.max(
				(float) w / srcWidth,
				(float) h / srcHeight);
		m.setScale(scale, scale);
		final int srcCroppedW, srcCroppedH;
		int srcX, srcY;
		srcCroppedW = Math.round(w / scale);
		srcCroppedH = Math.round(h / scale);
		srcX = (int) (srcWidth * horizontalCenterPercent - srcCroppedW / 2);
		srcY = (int) (srcHeight * verticalCenterPercent - srcCroppedH / 2);
		// Nudge srcX and srcY to be within the bounds of src
		srcX = Math.max(Math.min(srcX, srcWidth - srcCroppedW), 0);
		srcY = Math.max(Math.min(srcY, srcHeight - srcCroppedH), 0);
		final Bitmap cropped = Bitmap.createBitmap(src, srcX, srcY, srcCroppedW, srcCroppedH, m,
				true /* filter */);

		return cropped;
	}

	/**
	 *  이미지 회전 함수
	 * @param src
	 * @param degree
	 * @return
	 */
	public static Bitmap rotateImage(Bitmap src, float degree) {
		// Matrix 객체 생성
		Matrix matrix = new Matrix();
		// 회전 각도 셋팅
		matrix.postRotate(degree);
		// 이미지와 Matrix 를 셋팅해서 Bitmap 객체 생성
		return Bitmap.createBitmap(src, 0, 0, src.getWidth(),src.getHeight(), matrix, true);
	}

	/**
	 * 이미지 반전 처리
	 * @param src
	 * @param flip 0: 좌우, 1: 상하
	 * @return
	 */
	public static Bitmap flipImage(Bitmap src, int flip) {
		// Matrix 객체 생성
		Matrix matrix = new Matrix();
		if(flip == 0){
			// 좌우 셋팅
			matrix.setScale(-1,1);
			matrix.postTranslate(src.getWidth(),0);
		}else{
			// 좌우 셋팅
			matrix.setScale(1,-1);
			matrix.postTranslate(0, src.getHeight());
		}

		// 이미지와 Matrix 를 셋팅해서 Bitmap 객체 생성
		return Bitmap.createBitmap(src, 0, 0, src.getWidth(),src.getHeight(), matrix, true);
	}

	private static final int MAX_HEIGHT = 1080;
	private static final int MAX_WIDTH = 1080;
	public static Bitmap decodeSampledBitmap(Context context, Uri selectedImage)
			throws IOException {

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		InputStream imageStream = context.getContentResolver().openInputStream(selectedImage);
		BitmapFactory.decodeStream(imageStream, null, options);
		imageStream.close();

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, MAX_WIDTH, MAX_HEIGHT);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		imageStream = context.getContentResolver().openInputStream(selectedImage);
		Bitmap img = BitmapFactory.decodeStream(imageStream, null, options);

		img = rotateImageIfRequired(context, img, selectedImage);
		return img;
	}

	/**
	 * Rotate an image if required.
	 * @param img
	 * @param selectedImage
	 * @return
	 */
	private static Bitmap rotateImageIfRequired(Context context,Bitmap img, Uri selectedImage) {

		// Detect rotation
		int rotation = getRotation(context, selectedImage);
		if (rotation != 0) {
			Matrix matrix = new Matrix();
			matrix.postRotate(rotation);
			Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
			img.recycle();
			return rotatedImg;
		}
		else{
			return img;
		}
	}

	/**
	 * Get the rotation of the last image added.
	 * @param context
	 * @param selectedImage
	 * @return
	 */
	public static int getRotation(Context context,Uri selectedImage) {

		int rotation = 0;
		ContentResolver content = context.getContentResolver();

		Cursor mediaCursor = content.query(Images.Media.EXTERNAL_CONTENT_URI,
				new String[] { "orientation", "date_added" },
				null, null, "date_added desc");

		if (mediaCursor != null && mediaCursor.getCount() != 0) {
			while(mediaCursor.moveToNext()){
				rotation = mediaCursor.getInt(0);
				break;
			}
		}
		mediaCursor.close();
		return rotation;
	}

	/**
	 * 배경 세팅하기
	 * @param activity
	 * @param bm
	 */
	public static void setWallPaper(Activity activity, Bitmap bm){
		DisplayMetrics metrics = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
		int height = metrics.heightPixels;
		int width = metrics.widthPixels;
		//Bitmap bitmap = Bitmap.createScaledBitmap(bm, width, height, true);

		WallpaperManager wallpaperManager = WallpaperManager.getInstance(activity.getApplicationContext());
		try {
			wallpaperManager.setBitmap(bm);
		} catch (IOException e) {
			e.printStackTrace();
		}
		wallpaperManager.suggestDesiredDimensions(width, height);
	}

	public static Bitmap getResizedScaleBitmap(Bitmap image, float ratio) {
		int width = (int)(image.getWidth() * ratio);
		int height = (int)(image.getHeight() * ratio);

		GomsLog.d(TAG, "getResizedScaleBitmap width : " + width);
		GomsLog.d(TAG, "getResizedScaleBitmap height : " + height);

		Bitmap resizedBitmap = Bitmap.createScaledBitmap(image,  width, height, false);
		return resizedBitmap;
	}

	public static float[] getPointerCoords(ImageView view, MotionEvent e) {
		final int index = e.getActionIndex();
		final float[] coords = new float[] { e.getX(index), e.getY(index) };
		Matrix matrix = new Matrix();
		view.getImageMatrix().invert(matrix);
		matrix.postTranslate(view.getScrollX(), view.getScrollY());
		matrix.mapPoints(coords);
		return coords;
	}

	private void setGlideImage(Context context, String path, ImageView iv, int glideRoundedCorner){
		Glide.with(context).asBitmap().load(path).addListener(new RequestListener<Bitmap>() {
			@Override
			public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
				return false;
			}

			@Override
			public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, com.bumptech.glide.load.DataSource dataSource, boolean isFirstResource) {
				iv.setImageBitmap(resource);
				return false;
			}

		})
		.diskCacheStrategy(DiskCacheStrategy.NONE)
		.signature(new ObjectKey(System.currentTimeMillis()))
		.transform(new RoundedCorners(glideRoundedCorner))
		.submit();
	}

	/**
	 *
	 * @param activity
	 * @param filename	// "file:///android_asset/stemp_qr_sungho.png"
	 * @param iv
	 */
	public static void getImageFromAssets(Activity activity, String filename, ImageView iv){
		try{
			AssetManager assetManager = activity.getResources().getAssets();
			InputStream inputStream = assetManager.open(filename);
			iv.setImageDrawable(Drawable.createFromStream(inputStream, null));
			inputStream.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	public static String getImagePathFromAssets(String fileName){
		return "file:///android_asset/" + fileName;
	}

	/**
	 * background가 drawable layer-list, shape 타입일 때, 색상만 변경하는 처리
	 * GradientDrawable로 사용권장. Radius 설정 가능
	 * LayerDrawable 시, Radius 설정없음.
	 * @param iv target ImageView
	 * @param color Color.parseColor("#FFDE03")
	 * @param radius 16
	 * @param drawable R.drawable.ui_circle
	 */
	public static void setImageShapeColor(Context context, ImageView iv, int color, int radius, int drawable){
		if(ContextCompat.getDrawable(context, drawable) instanceof GradientDrawable){   //<shape></shape> 타입
			GradientDrawable gradientDrawable = ((GradientDrawable) ContextCompat.getDrawable(context, drawable));
			gradientDrawable.setColorFilter(color, PorterDuff.Mode.SRC_IN);
			gradientDrawable.setCornerRadius(radius);
			iv.setImageDrawable(gradientDrawable);
		}else if(ContextCompat.getDrawable(context, drawable) instanceof LayerDrawable){    //<layer-list></layer-list> 타입, radius 설정없음
			LayerDrawable layerDrawable = ((LayerDrawable) ContextCompat.getDrawable(context, drawable));
			layerDrawable.setColorFilter(color, PorterDuff.Mode.SRC_IN);
			iv.setImageDrawable(layerDrawable);
		}
	}
	public static void setLinearLayoutShapeColor(Context context, ViewGroup viewGroup, int color, int radius, int drawable){
		if(ContextCompat.getDrawable(context, drawable) instanceof GradientDrawable){   //<shape></shape> 타입
			GradientDrawable gradientDrawable = ((GradientDrawable) ContextCompat.getDrawable(context, drawable));
			gradientDrawable.setColorFilter(color, PorterDuff.Mode.SRC_IN);
			gradientDrawable.setCornerRadius(radius);
			viewGroup.setBackground(gradientDrawable);
		}else if(ContextCompat.getDrawable(context, drawable) instanceof LayerDrawable){    //<layer-list></layer-list> 타입, radius 설정없음
			LayerDrawable layerDrawable = ((LayerDrawable) ContextCompat.getDrawable(context, drawable));
			layerDrawable.setColorFilter(color, PorterDuff.Mode.SRC_IN);
			viewGroup.setBackground(layerDrawable);
		}
	}
	public static void setTextViewShapeColor(Context context, View view, int color, int radius, int drawable){
		if(ContextCompat.getDrawable(context, drawable) instanceof GradientDrawable){   //<shape></shape> 타입
			GradientDrawable gradientDrawable = ((GradientDrawable) ContextCompat.getDrawable(context, drawable));
			gradientDrawable.setColorFilter(color, PorterDuff.Mode.SRC_IN);
			gradientDrawable.setCornerRadius(radius);
			view.setBackground(gradientDrawable);
		}else if(ContextCompat.getDrawable(context, drawable) instanceof LayerDrawable){    //<layer-list></layer-list> 타입, radius 설정없음
			LayerDrawable layerDrawable = ((LayerDrawable) ContextCompat.getDrawable(context, drawable));
			layerDrawable.setColorFilter(color, PorterDuff.Mode.SRC_IN);
			view.setBackground(layerDrawable);
		}
	}
	
	/**
	 * 이미지 파일의 확장자를 기반으로 이미지여부 판별
	 * @param str String str1 = "abc.png";
	 * @return
	 */
	public static boolean isImageExtension(String str){
		String regex = "([^\\s]+(\\.(?i)(jpe?g|png|gif|bmp))$)";

		// Compile the ReGex
		Pattern p = Pattern.compile(regex);

		// If the string is empty
		// return false
		if (str == null) {
			return false;
		}

		// Pattern class contains matcher() method
		// to find matching between given string
		// and regular expression.
		Matcher m = p.matcher(str);

		// Return if the string
		// matched the ReGex
		return m.matches();
	}

	public static String getImageFileExtension(String imagePath){
		String fileExtension = "";
		Path path = null;
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
			path = Paths.get(imagePath);
			// Get the file name as a string
			String fileName = path.getFileName().toString();

			// Get the file extension
			fileExtension = getFileExtension(fileName);
		}

		return fileExtension;
	}

	private static String getFileExtension(String fileName) {
		int dotIndex = fileName.lastIndexOf('.');
		if (dotIndex == -1 || dotIndex == fileName.length() - 1) {
			// No file extension found or the dot is at the end of the file name
			return "No Extension";
		} else {
			return fileName.substring(dotIndex + 1).toLowerCase();
		}
	}
}
