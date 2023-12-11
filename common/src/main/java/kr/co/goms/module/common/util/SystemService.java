

package kr.co.goms.module.common.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;
import android.view.WindowManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.NetworkInterface;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static kr.co.goms.module.common.util.StringUtil.isNull;
import static kr.co.goms.module.common.util.StringUtil.substr;

/**
 * 안드로이드 단말 제어기능
 *
 *
 * @(#)SystemService.java @ Copyright 2011 InfoBank Corporation. All rights
 *                        reserved.
 *
 */
public class SystemService
{
	/**
	 * 전화번호 가져오기
	 *
	 * @param context
	 * @return 폰번호
	 * @description 퍼미션 READ_PHONE_STATE
	 */
	public static String getPhoneNumber(Context context) {
		if(context == null) {
			return null;
		}
		try {
			TelephonyManager mTelephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			if (mTelephonyMgr == null) {
				return null;
			} else {
				if (mTelephonyMgr.getLine1Number() == null) {
					return null;
				} else {
					return mTelephonyMgr.getLine1Number();
				}
			}
		}catch(SecurityException e){	//권한설정 전에 호출 시, 에러 발생
			return null;
		}
	}

/*	public static String getTelephoneNum(Context context) {
		TelephonyManager telephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		String phoneNum = telephonyMgr.getLine1Number();
		Log.d("GOOGLE_TEL", "phoneNum >>>>>>>>>>>>>  "+ phoneNum);
		if (phoneNum != null) {
			if (phoneNum.startsWith("+")) phoneNum = "0" + phoneNum.substring(3);
		}
		return phoneNum;
	}*/

	/**
	 * OS 버전 가져오기
	 * @return OS 버전
	 * @description 퍼미션 READ_PHONE_STATE
	 */
	public static int getPhoneVersion() {
		int sdk_int = Build.VERSION.SDK_INT;
		String OsVER  		= Build.VERSION.RELEASE;
		sdk_int = Integer.parseInt(OsVER.substring(0,1));
		return sdk_int;
	}

	/**
	 * 어플리케이션 맥어드레스 구하기
	 * 
	 * @param context
	 *            컨텍스트
	 * @return 맥어드레스
	 */
	public static String getMacAddress(Context context) {
		String marshmallowMacAddress = "02:00:00:00:00:00";
		String result = null;
		if(Build.VERSION.SDK_INT < 23) {
			try {
				WifiManager wfManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
				if (!wfManager.isWifiEnabled()) {
					wfManager.setWifiEnabled(true);
					WifiInfo wifiInfo = wfManager.getConnectionInfo();
					result = wifiInfo.getMacAddress();
					wfManager.setWifiEnabled(false);
				} else {
					WifiInfo wifiInfo = wfManager.getConnectionInfo();
					result = wifiInfo.getMacAddress();
				}
			} catch (Exception e) {
				Log.e("ver.xml", "SystemService getMacAddress " + e.getMessage());
			}
//			return result;
		} else {
			try {
				List<NetworkInterface> interfaceList = Collections.list(NetworkInterface.getNetworkInterfaces());
				for(NetworkInterface networkInterface : interfaceList) {
					if(!networkInterface.getName().equalsIgnoreCase("wlan0")) {
						continue;
					}

					byte[] mac = networkInterface.getHardwareAddress();
					if(mac == null) {
						result = marshmallowMacAddress;
					}

					StringBuilder stringBuilder = new StringBuilder();
					for(int i = 0; i < mac.length; i++) {
						stringBuilder.append(String.format("%02X:", mac[i]));
					}

					if(stringBuilder.length() > 0) {
						stringBuilder.deleteCharAt(stringBuilder.length()-1);
					}
					result = stringBuilder.toString();
				}
			} catch (Exception e) {
				Log.e("ver.xml", "SystemService getMacAddress " + e.getMessage());
			}
		}
		return result;
	}

	/**
	 * UniqueId 구하기
	 * 
	 * @param context
	 *            컨텍스트
	 * @return UniqueId
	 */
	public static String getUniqueDeviceId(Context context) {
		String result = getPhoneNumber(context);
		if (result != null) {
			if (result.startsWith("82")) {
				result = "+" + result;
			}
			if (result.startsWith("+82")) {
				result = result.replace("+82", "0");
			}
		} else {
			result = getDeviceId(context);
		}

		return result;

	}

	/**
	 * 어플리케이션 버전 코드구하기
	 * 
	 * @param context
	 * @return 버전코드
	 */
	public static int getAppVersionCode(Context context) {
		int result = 0;
		try {
			PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_META_DATA);
			if (pInfo != null) {
				result = pInfo.versionCode;
			}

		} catch (NameNotFoundException e) {
			Log.e("ver.xml", "SystemService getAppVersionCode " + e.getMessage());
		}
		return result;
	}

	/**
	 * 어플리케이션 버전명구하기
	 * 
	 * @param context
	 * @return 버전명
	 */
	public static String getAppVersionName(Context context) {
		String result = null;
		try {
			if(context != null) {
				PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_META_DATA);
				if (pInfo != null) {
					result = pInfo.versionName;
				}
			}
		} catch (NameNotFoundException e) {
			Log.e("ver.xml", "SystemService getAppVersionName " + e.getMessage());
		}
		return result;
	}

	/**
	 * 어플리케이션 버전 코드구하기
	 *
	 * @param context
	 * @return 버전코드
	 */
	public static int getOtherAppVersionCode(Context context, String packageName) {
		int result = 0;
		try {
			PackageInfo pInfo = context.getPackageManager().getPackageInfo(packageName, PackageManager.GET_META_DATA);
			if (pInfo != null) {
				result = pInfo.versionCode;
			}

		} catch (NameNotFoundException e) {
			Log.e("ver.xml", "SystemService getOtherAppVersionCode " + e.getMessage());
		}
		return result;
	}

	/**
	 * 
	 * 
	 * 핸드폰 통신사 구하기
	 * 
	 * @param context
	 * @return
	 */
	public static int getTelecom(Context context) {
		int result;
		TelephonyManager tm = null;
		tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		String TelephonCode = tm.getSimOperator().trim();
		if (TelephonCode.equals("45005"))
			result = 1;
		else if (TelephonCode.equals("45002") || TelephonCode.equals("45004") || TelephonCode.equals("45008"))
			result = 2;
		else if (TelephonCode.equals("45006") || TelephonCode.equals("45011"))
			result = 3;
		else {
			result = 2; // 휴대폰 가입자 확인이 안될시 100
		}
		return result;
	}

	/**
	 * 해상도 넓이구하기
	 * 
	 * @param context
	 * @return width
	 */
	public static int getDisplayWidth(Context context) {
		WindowManager mWMgr = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		return mWMgr.getDefaultDisplay().getWidth();
	}

	/**
	 * 해상도 높이구하기
	 * 
	 * @param context
	 * @return height
	 */
	public static int getDisplayHeight(Context context) {
		WindowManager mWMgr = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		return mWMgr.getDefaultDisplay().getHeight();
	}

	/**
	 * 유심번호 가져오기
	 * 
	 * @param context
	 * @return 유심ID
	 * @description 퍼미션 READ_PHONE_STATE
	 */
	public static String getUsimId(Context context) {
		try {
			TelephonyManager mTelephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			return mTelephonyMgr.getSubscriberId();
		} catch (Exception e) {
			return "";
		}

	}

	public static String getPhoneNum(Context context)
	{
		String phoneNum = getPhoneNumber(context);
		if(!isNull(phoneNum))
		{
			if (phoneNum.startsWith("+82")) phoneNum = phoneNum.replace("+82", "0");
			else if (phoneNum.startsWith("82")) phoneNum = phoneNum.replace("82", "0");
			phoneNum = "0"+ StringUtil.stringToInt(phoneNum);
		}
		else phoneNum = "";
				
		return phoneNum;
	}


	/**
	 * 디바이스ID 가져오기
	 * 
	 * @param context
	 * @return 디바이스ID
	 * @description 퍼미션 READ_PHONE_STATE
	 */
	public static String getDeviceId(Context context) {
		TelephonyManager mTelephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		return mTelephonyMgr.getDeviceId();
	}

	private static String sID = null;
	private static final String INSTALLATION = "INSTALLATION";

	public static String id(Context context) {
		if (sID == null)
		{
			File installation = new File(context.getFilesDir(), INSTALLATION);
			try {
				if (!installation.exists()) writeInstallationFile(installation, false);
				sID = readInstallationFile(installation);
			} catch (Exception e) {
				sID = "";
				throw new RuntimeException(e);
			}
		}
		return sID;
	}

	private static String readInstallationFile(File installation) throws IOException {
		RandomAccessFile f = null;
		byte[] bytes = null;
		try {
			f = new RandomAccessFile(installation, "r");
			bytes = new byte[(int) f.length()];
			f.readFully(bytes);
		} catch (Exception e) {
		} finally {
			try {
				if (f != null)
					f.close();
			} catch (Exception e2) {
			}
		}

		return new String(bytes);
	}
	private static void writeInstallationFile(File installation, boolean is) throws IOException {
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(installation, is);
			String id = UUID.randomUUID().toString();
			out.write(id.getBytes());
		} catch (Exception e) {
		} finally {
			try {
				if (out != null)
					out.close();
			} catch (Exception e2) {
			}
		}
	}

	/**
	 * Long 단위의 메모리를 메가(MB)단위로 해서 String 형태로 리턴한다
	 * @param size
	 * @return
	 */
	public static int formatSize(long size) {
		//String suffix = null;
		int chgValue = 0;

		if (size >= 1024) {
			//suffix = "KiB";
			size /= 1024;
			if (size >= 1024) {
				//suffix = "MiB";
				size /= 1024;
			}
		}

		StringBuilder resultBuffer = new StringBuilder(Long.toString(size));

		chgValue = StringUtil.stringToInt(resultBuffer.toString());
		return chgValue;
	}


	public static long getVerToNum(String ver)
	{
		if(isNull(ver)) return 0;

		String av[] = ver.split("\\.");
		String sr = "";
		for(int i=0;i<3;i++)
		{
			sr += StringUtil.padding((av.length>i?av[i]:"0"), "0", 3, true);
		}

		return StringUtil.stringToLong(sr);
	}

	public static String bundleGetString(Bundle bundle, String key, String def)
	{
		if(bundle == null) return def;
		if(bundle.containsKey(key)) return bundle.getString(key);
		return def;
	}

	public static boolean bundleGetBoolean(Bundle bundle, String key, boolean def)
	{
		if(bundle == null) return def;
		if(bundle.containsKey(key)) return bundle.getBoolean(key);
		return def;
	}

	public static int bundleGetInt(Bundle bundle, String key, int def)
	{
		if(bundle == null) return def;
		if(bundle.containsKey(key)) return bundle.getInt(key);
		return def;
	}

	/**
	 * 권한 : GET_ACCOUNTS
	 * 기능 : 이메일 얻는 기능
	 * 표시 권한 : 주소록
	 * 동작이 잘 되지 않으며, 권한에 대해 안내가 없으며, 중요도가 낮은 권한 및 기능이므로 폐기함. 2020.05.22 최원근.
	 * @param context
	 * @return
	 */
	@Deprecated
	public static String getSystemAccount(Context context) {
//		Account[] accounts = AccountManager.get(context).getAccountsByType("com.google");
		String returnGmail = "";

//		if(accounts.length > 0) {
//			returnGmail = accounts[0].name;
//		}

		return returnGmail;
	}

	public static boolean isConnectedNetworkStatus(Context context) {
		boolean retValue = false;

		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

		if (networkInfo != null) {
			if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI && networkInfo.isConnectedOrConnecting()) {
				// 와이파이 연결중
				retValue = true;
			} else // 모바일 네트워크 연결중
// 네트워크 오프라인
				retValue = networkInfo.getType() == ConnectivityManager.TYPE_MOBILE && networkInfo.isConnectedOrConnecting();
		} else {
			// 네트워크 null
			retValue = false;
		}

		return retValue;
	}

	/**
	 * SMS Retriver 의 수신 문자 파싱을 위한 앱 해시값 생성
	 */
	private static final String HASH_TYPE = "SHA-256";
	public static final int NUM_HASHED_BYTES = 9;
	public static final int NUM_BASE64_CHAR = 11;

	public static ArrayList<String> getAppSignatures(Context context) {
		ArrayList<String> appCodes = new ArrayList<>();

		try {
			// Get all package signatures for the current package
			String packageName = context.getPackageName();
			PackageManager packageManager = context.getPackageManager();
			Signature[] signatures = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES).signatures;

			// For each signature create a compatible hash
			for (Signature signature : signatures) {
				String hash = getHash(packageName, signature.toCharsString());
				if (hash != null) {
					appCodes.add(String.format("%s", hash));
				}
			}
		} catch (NameNotFoundException e) {
		}
		return appCodes;
	}

	private static String getHash(String packageName, String signature) {
		String appInfo = packageName + " " + signature;
		try {
			MessageDigest messageDigest = MessageDigest.getInstance(HASH_TYPE);
			// minSdkVersion이 19이상이면 체크 안해도 됨
			messageDigest.update(appInfo.getBytes(StandardCharsets.UTF_8));
			byte[] hashSignature = messageDigest.digest();

			// truncated into NUM_HASHED_BYTES
			hashSignature = Arrays.copyOfRange(hashSignature, 0, NUM_HASHED_BYTES);
			// encode into Base64
			String base64Hash = Base64.encodeToString(hashSignature, Base64.NO_PADDING | Base64.NO_WRAP);
			base64Hash = base64Hash.substring(0, NUM_BASE64_CHAR);
			return base64Hash;
		} catch (NoSuchAlgorithmException e) {
		}
		return null;
	}


	public static String SHA256(String str) {
		String SHA = "";
		if(isNull(str)) return SHA;

		try {
			MessageDigest sh = MessageDigest.getInstance("SHA-256");
			sh.update(str.getBytes());
			byte byteData[] = sh.digest();
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < byteData.length; i++) {
				sb.append(substr(Integer.toString((byteData[i] & 0xff) + 0x100, 16), 1));
			}
			SHA = sb.toString();

		} catch (NoSuchAlgorithmException e) {
			SHA = null;
		}
		return SHA;
	}

	public static String padding(String str, String pad, int len)
	{
		return padding(str, pad, len, false);
	}
	public static String padding(String str, String pad, int len, boolean isLeft)
	{
		String r = str;
		if(isNull(str)) str = "";
		if(isNull(pad) || len <= 0) return r;
		if(str.length() >= len) return r;
		if(pad.length() != 1) return r;

		len -= str.length();
		for(int i=0;i<len;i++)
		{
			r = (isLeft?pad:"") + r + (!isLeft?pad:"");
		}

		return r;
	}
}
