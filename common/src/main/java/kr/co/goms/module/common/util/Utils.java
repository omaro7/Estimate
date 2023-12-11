package kr.co.goms.module.common.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.nio.ByteOrder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils
{

	/**
	 * View 청소하기
	 */
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	@SuppressWarnings("deprecation")
	public static void recursiveRecycle(View root)
	{
		if (root == null)
			return;

		root.setBackground(null);

		if (root instanceof ViewGroup)
		{
			ViewGroup group = (ViewGroup) root;
			int count = group.getChildCount();
			for (int i = 0; i < count; i++)
			{
				recursiveRecycle(group.getChildAt(i));
			}

			if (!(root instanceof AdapterView))
			{
				group.removeAllViews();
			}

		}

		if (root instanceof ImageView)
		{
			((ImageView) root).setImageDrawable(null);
		}

		root = null;

		return;
	}

	public static void recursiveRecycle(List<WeakReference<View>> recycleList)
	{
		for (WeakReference<View> ref : recycleList)
		{
			recursiveRecycle(ref.get());
		}
	}


	/**
	 * Volley를 위한 파마리터 변경작업
	 * AES암호화 및 URL 인코딩
	 * 
	 * @param value
	 * @param key
	 * @return
	 */

	public static String encrypt(String value, String iv, String key)
	{
		String returnValue;
		try
		{
			returnValue = URLEncoder.encode(AES256Cipher.AES_Encode(value, iv, key), "UTF-8");
		}
		catch (Exception e)
		{
			returnValue = "";
		}

		return returnValue;
	}

	public static String encrypt2(String value, String iv, String key)
	{
		String returnValue;
		try
		{
			returnValue = AES256Cipher.AES_Encode(value, iv, key);
		}
		catch (Exception e)
		{
			returnValue = "";
		}

		return returnValue;
	}

	public static String decrypt(String value, String iv, String key)
	{
		String returnValue;
		try
		{
			returnValue = AES256Cipher.AES_Decode(URLDecoder.decode(value, "UTF-8"), iv, key);
		}
		catch (Exception e)
		{
			returnValue = "";
			e.printStackTrace();
		}

		return returnValue;
	}

	public static String decrypt2(String value, String iv, String key)
	{
		String returnValue;
		try
		{
			returnValue = AES256Cipher.AES_Decode(value, iv, key);
		}
		catch (Exception e)
		{
			returnValue = "";
			e.printStackTrace();
		}

		return returnValue;
	}

	/**
	 * 2개의 array 붙이기
	 * 
	 * @param first
	 * @param second
	 * @return
	 */
	public static <T> T[] concat(T[] first, T[] second)
	{
		int firstLen = first.length;
		int seconLen = second.length;
		T[] result = Arrays.copyOf(first, firstLen + seconLen);
		System.arraycopy(second, 0, result, firstLen, seconLen);
		return result;
	}

	/**
	 * hex 스트링을 바이트 어레이로 변경
	 * 
	 * @param s - hex string
	 * @return
	 */
	public static byte[] hexToByteArray(String s)
	{
		int len = s.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2)
			data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
		return data;
	}

	/**
	 * 바이트 어레이를 HEX 스트링으로 변경
	 * 
	 * @param ba
	 * @return
	 */
	public static String byteArrayToHex(byte[] ba)
	{
		if (ba == null || ba.length == 0)
			return null;
		int len = ba.length;
		StringBuffer sb = new StringBuffer(len * 2);
		String hexNumber;
		for (int x = 0; x < len; x++)
		{
			hexNumber = "0" + Integer.toHexString(0xff & ba[x]);
			sb.append(hexNumber.substring(hexNumber.length() - 2));
		}
		return sb.toString();
	}

	/**
	 * Decimal String to Hex String
	 * 
	 * @param dec
	 * @return
	 */
	public static String decToHex(String dec)
	{
		String hex;
		try
		{
			hex = Integer.toHexString(Integer.valueOf(dec));
			//MLog.w("==> hex : "+hex);
			if (hex.length() == 1) {
				//MLog.w("==> hex.length() == 1");
				hex = "0" + hex;
				//MLog.w("==> hex : "+hex);
			}
		}
		catch (Exception e)
		{
			return "";
		}

		return hex;
	}

	/**
	 * Byte Array to String
	 * 
	 * @param bytes
	 * @return
	 */
	public static String byteArrayToString(byte[] bytes)
	{
		StringBuilder sb = new StringBuilder(bytes.length * 2);
		for (byte b : bytes)
			sb.append(String.format(Locale.US, "%02X", b & 0xff));
		return sb.toString();
	}

	/**
	 * UCT 시간을 GMT 시간으로 변경
	 * 
	 * @param date
	 * @return
	 */
	public static Date utcToGmt(Date date)
	{
		TimeZone tz = TimeZone.getDefault();
		Date ret = new Date(date.getTime() - tz.getRawOffset());

		if (tz.inDaylightTime(ret))
		{
			Date dstDate = new Date(ret.getTime() - tz.getDSTSavings());

			if (tz.inDaylightTime(dstDate))
			{
				ret = dstDate;
			}
		}

		return ret;
	}

	/**
	 * int To InetAddress
	 * 
	 * @param hostAddress
	 * @return
	 */
	public static InetAddress intToInetAddress(int hostAddress)
	{
		InetAddress inetAddress;
		byte[] addressBytes = { (byte) (0xff & hostAddress >> 24), (byte) (0xff & (hostAddress >> 16)), (byte) (0xff & (hostAddress >> 8)),
				(byte) (0xff & hostAddress) };

		try
		{
			inetAddress = InetAddress.getByAddress(addressBytes);
		}
		catch (UnknownHostException e)
		{
			return null;
		}
		return inetAddress;
	}

	/**
	 * int to IP Address
	 * 
	 * @param addr
	 * @return
	 */
	public static String intToIp(int addr)
	{
		if (ByteOrder.nativeOrder().equals(ByteOrder.LITTLE_ENDIAN))
			addr = Integer.reverseBytes(addr);
		return ((addr >> 24) & 0xFF) + "." + ((addr >> 16) & 0xFF) + "." + ((addr >> 8) & 0xFF) + "." + (addr & 0xFF);
	}

	/**
	 * drawable 이미지 받아오기
	 * 
	 * @param context
	 * @param resource
	 * @return
	 */
	@SuppressWarnings("deprecation")
	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	public static Drawable getDrawable(Context context, int resource)
	{
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
			return context.getResources().getDrawable(resource, null);
		return context.getResources().getDrawable(resource);
	}

	/**
	 * URL Encode
	 * 
	 * @param data
	 * @return
	 */
	public static String setURLEncod(String data)
	{
		try
		{
			data = URLEncoder.encode(data, "UTF-8");
		}
		catch (UnsupportedEncodingException e)
		{
			//MLog.w(Log.getStackTraceString(e));
		}
		return data;
	}

	/**
	 * MIME 타입 구하기
	 * 
	 * @param url
	 * @return
	 */
	public static String getMimeType(String url)
	{
		String type = null;
		String extension = MimeTypeMap.getFileExtensionFromUrl(url);
		if (extension != null)
			type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);

		return type;
	}

	public static void dumpDB(Context context)
	{
		//dumpDB(context, SQLiteHelper.DB_NAME, Environment.getExternalStorageDirectory().toString() + File.separator + SQLiteHelper.DB_NAME);
	}

	/**
	 * DB 덤프뜨기
	 * 
	 * @param context
	 * @param dbName
	 * @param copyPath
	 */
	public static void dumpDB(Context context, String dbName, String copyPath)
	{

		//File f = new File("/data/data/" + context.getPackageName() + "/databases/" + dbName);
		File f = context.getDatabasePath(dbName);
		FileInputStream fis = null;
		FileOutputStream fos = null;

		try
		{
			fis = new FileInputStream(f);
			fos = new FileOutputStream(copyPath);
			while (true)
			{
				int i = fis.read();
				if (i != -1)
					fos.write(i);
				else
					break;

			}
			fos.flush();
			Toast.makeText(context, "DB dump OK", Toast.LENGTH_LONG).show();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			Toast.makeText(context, "DB dump ERROR", Toast.LENGTH_LONG).show();
		}
		finally
		{
			try
			{
				fos.close();
				fis.close();
			}
			catch (IOException ioe)
			{}
		}
	}

	/**
	 * Get ISO 3166-1 alpha-2 country code for this device (or null if not available)
	 * 
	 * @param context Context reference to get the TelephonyManager instance from
	 * @return country code or null
	 */
	public static String getUserCountry(Context context)
	{
		try
		{
			final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			final String simCountry = tm.getSimCountryIso();
			if (simCountry != null && simCountry.length() == 2)
			{
				// SIM country code is available
				return simCountry.toLowerCase(Locale.US);
			}
			else if (tm.getPhoneType() != TelephonyManager.PHONE_TYPE_CDMA)
			{
				// device is not 3G (would be unreliable)
				String networkCountry = tm.getNetworkCountryIso();
				if (networkCountry != null && networkCountry.length() == 2)
				{
					// network country code is available
					return networkCountry.toLowerCase(Locale.US);
				}
			}
		}
		catch (Exception e)
		{}
		return null;
	}

	public static boolean isValidPublicIp(String ip)
	{
		Inet4Address address;
		try
		{
			address = (Inet4Address) InetAddress.getByName(ip);
		}
		catch (UnknownHostException exception)
		{
			return false;
		}
		catch (ClassCastException exception)
		{
			//MLog.w("==> ClassCastException !!!!!!!!!!!!!!!!!!!");
			return false;
		}
		return !(address.isSiteLocalAddress() || address.isAnyLocalAddress() || address.isLinkLocalAddress() || address.isLoopbackAddress() || address
				.isMulticastAddress());
	}

	/**
	 * 디바이스 모델
	 * 
	 * @return
	 */
	public static String getDeviceName()
	{
		String manufacturer = Build.MANUFACTURER;
		String model = Build.MODEL;
		if (model.startsWith(manufacturer))
		{
			return capitalize(model);
		}
		else
		{
			return capitalize(manufacturer) + " " + model;
		}
	}

	/**
	 * 디바이스 모델명 조정 (없는것과 첫자 대문자)
	 * 
	 * @param s
	 * @return
	 */
	private static String capitalize(String s)
	{
		if (s == null || s.length() == 0)
		{
			return "";
		}
		char first = s.charAt(0);
		if (Character.isUpperCase(first))
		{
			return s;
		}
		else
		{
			return Character.toUpperCase(first) + s.substring(1);
		}
	}

	/**
	 * This method converts dp unit to equivalent pixels, depending on device density.
	 * 
	 * @param dp A value in dp (density independent pixels) unit. Which we need to convert into pixels
	 * @return A float value to represent px equivalent to dp depending on device density
	 */
	public static float convertDpToPixel(Context c, float dp)
	{
		return dp * (c.getResources().getDisplayMetrics().densityDpi / 160f);
	}


	/**
	 * 다른 앱에 접속( 없으면 마켓 으로 이동 )
	 * strings[0] key ,strings[1] value
	 */
	public static void OpenApp(Context c, String appName, String... putExtraKV){
		boolean isExist = false;
		//실행시키는 앱이 휴대폰에 있는지 검사
		PackageManager packageManager = c.getPackageManager();
		List<ResolveInfo> mApps;
		Intent mIntent = new Intent(Intent.ACTION_MAIN, null);
		mIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		//2018-11-26KH 왓치캅 오류 테스트관련
		//mIntent.addCategory(Intent.CATEGORY_DEFAULT);//플레이스토어가 나와버림...
		//
		mApps = packageManager.queryIntentActivities(mIntent, 0);
		//MLog.i(c.toString()+" "+appName + " "+ putExtraKV.toString());
		try {
			for (int i = 0; i < mApps.size(); i++) {
				if(mApps.get(i).activityInfo.packageName.startsWith(appName)){
					isExist = true;
					break;
				}
			}
		} catch (Exception e) {
			isExist = false;
		}

		try{
			if(isExist){
				//휴대폰에 앱이 있을경우
				ArrayList<String> k = new ArrayList<>();
				ArrayList<String> v = new ArrayList<>();
				for(int i = 0; i < putExtraKV.length; i++){
					//앱 전달하는 putExtra Key Value 구분
					if(i % 2 == 0){
						k.add(putExtraKV[i].toString());
					}else{
						v.add(putExtraKV[i]);
					}
				}

				//앱 실행
				Intent launchIntent = c.getPackageManager().getLaunchIntentForPackage(appName);
				if(k.size() == v.size()){
					for(int i = 0; i < k.size(); i++ ){
						launchIntent.putExtra(k.get(i), v.get(i));
					}
					launchIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				}else{
					if(k.size() != 0){
						//MLog.e("OpenApp putExtra Error !!!");
					}
				}
				c.startActivity(launchIntent);
			}else{
				//마켓 검색
				OpenMarket(c, appName);
			}
		}catch (NullPointerException e) {
			//MLog.w("OpenApp Error");
		}
	}

	public static void OpenMarket(Context c, String appName){
		Intent marketLaunch = new Intent(Intent.ACTION_VIEW);
		try{
			marketLaunch.setData(Uri.parse("market://details?id="+appName));
		}catch (android.content.ActivityNotFoundException anfe){
			marketLaunch.setData(Uri.parse("https://play.google.com/store/apps/details?id="+appName));
		}finally {
			c.startActivity(marketLaunch);
		}
	}

	public static boolean RegExNumber(String str) {
		boolean result = Pattern.matches("^[0-9]*$", str); //정규식 숫자
		if(str.length() > 0 && result ) {
			return true;
		}  
		return false;
	} 
	
	
	/**
	 * 화면 기본사이즈를 가져온다.
	 */
	public static int[] DisplayDifaultSize(Context context){
		int[] size = new int[2];
		Display clsDisplay = (((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay());
		Point clsSize = new Point();
		clsDisplay.getSize( clsSize );

		Rect rectgle= new Rect();
		Window window= ((Activity)context).getWindow();
		window.getDecorView().getWindowVisibleDisplayFrame(rectgle);
		int StatusBarHeight= rectgle.top;
		//int contentViewTop=window.findViewById(Window.ID_ANDROID_CONTENT).getTop();
		//int TitleBarHeight= contentViewTop - StatusBarHeight;
		size[0] = clsSize.x;
		size[1] = (clsSize.y - StatusBarHeight);
		
		return size;
	}


	/**
	 * dip 픽셀 변환
	 * @param context
	 * @param dipValue
	 * @return
	 */
	public static float dipToPixels(Context context, float dipValue) {
		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics);
	}

	/**
	 * Local Time to UTC
	 */
	public static String changeLocalToUTC(String local)
	{
		try
		{
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
			simpleDateFormat.setTimeZone(TimeZone.getDefault());
			Date myDate = simpleDateFormat.parse(local);
			simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
			return simpleDateFormat.format(myDate);
		}
		catch (Exception e)
		{
			return "0";
		}
	}
	/**
     * UTC Time to Local
     */
    public static String changeUtcToLocal(String utc)
    {
        try
        {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date myDate = simpleDateFormat.parse(utc);
            simpleDateFormat.setTimeZone(TimeZone.getDefault());
            return simpleDateFormat.format(myDate);
        }
        catch (Exception e)
        {
            return "0";
        }
    }

	/**
	 * 휴대폰 번호 포멧 자동 생성
	 * @param phoneNumber
	 * @return
	 */
	public static String makePhoneNumber(String phoneNumber) {
		String regEx = "(\\d{3})(\\d{3,4})(\\d{4})";

		if(!Pattern.matches(regEx, phoneNumber)) return null;

		return phoneNumber.replaceAll(regEx, "$1-$2-$3");
	}

	/**
	 * 이메일 포맷 체크
	 * @param email
	 * @return
	 */
	public static boolean checkEmail(String email){
		String regex = "^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(email);
		boolean isNormal = m.matches();
		return isNormal;
	}


	public static boolean checkHangul(String id){
		if(id.matches(".*[ㄱ-ㅎㅏ-ㅣ가-힣]+.*")) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean checkID(String id){
		String pattern = "^(?=.*\\d)[a-z0-9]{5,20}$";
		if (Pattern.matches(pattern, id)) {
			return true;
		}
		else {
			return false;
		}
	}

	public static boolean checkID1(String id){
		String pattern = "^[a-z]{5,20}$";
		if (Pattern.matches(pattern, id)) {
			return true;
		}
		else {
			return false;
		}
	}

	public static boolean checkPW(String id){
		String pattern = "^(?=.*\\d)[a-z0-9]{6,20}$";
		if (Pattern.matches(pattern, id)) {
			return true;
		}
		else {
			return false;
		}
	}

	public static boolean checkCellPhone(String phoneNum) {

		String pattern = "^\\d{3}-\\d{3,4}-\\d{4}$";
		if (Pattern.matches(pattern, phoneNum)) {
			return true;
		}
		else {
			return false;
		}
	}

	public static int getRandom(int tempCount){
		int random = (int) ((Math.random() * tempCount));
		return random;
	}

}
