package kr.co.goms.module.admob;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.ArrayList;

import kr.co.goms.module.admob.util.Utils;

public class AdmobPrefs {

	public static final String EMPTY                          = "";
	//AES_KEY
	public static final String S_KEY                       	  = "s_key";
	//다이아 갯수
	public static final String DIA_CNT							= "dia_cnt";

	private static AdmobPrefs       self                           = null;
	private SharedPreferences  mPrefs;

	public static final String NOTI_SEQ						= "noti_seq";

	private AdmobPrefs(Context context) {
		mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
	}

	/**
	 * 최초 context는 반드시 필요함.
	 * 
	 * @param context
	 * @return
	 */
	public static AdmobPrefs getInstance(Context context) {
		if (self == null)
			self = new AdmobPrefs(context);
		return self;
	}

	/**
	 * 이후 부터는 이 메소드를 이용하여 싱글턴으로 객체를 사용.
	 * 
	 * @return
	 */
	public static AdmobPrefs getInstance() {
		return self;
	}

	public SharedPreferences getPrefs()
	{
		return mPrefs;
	}

	/**
	 * Default
	 * 
	 * @param pKey
	 * @return
	 */
	public String get(String pKey) {
		try {
			return get(pKey, EMPTY);
		} catch (Exception e) {
			return EMPTY;
		}
	}

	public boolean del(String pKey)
	{
		return mPrefs.edit().remove(pKey).commit();
	}

	/****************************************************************
	 * boolean
	 */
	public boolean put(String pKey, boolean pDefault)
	{
		return put(pKey, String.valueOf(pDefault));
	}

	public boolean get(String pKey, boolean pDefault) {
		try {
			return Boolean.valueOf(get(pKey, String.valueOf(pDefault)));
		} catch (Exception e) {
			return false;
		}
	}

	/******************************************************************
	 * int
	 */
	public boolean put(String pKey, int pDefault)
	{
		return put(pKey, String.valueOf(pDefault));
	}

	public int get(String pKey, int pDefault) {
		try {
			return Integer.valueOf(get(pKey, String.valueOf(pDefault)));
		} catch (Exception e) {
			return 0;
		}
	}
	
	/****************************************************************
	 * long
	 */
	public boolean put(String pKey, long pDefault)
	{
		return put(pKey, String.valueOf(pDefault));
	}

	public long get(String pKey, long pDefault) {
		try {
			return Long.valueOf(get(pKey, String.valueOf(pDefault)));
		} catch (Exception e) {
			return 0;
		}
	}
	

	/****************************************************************
	 * String
	 */
	public boolean put(String pKey, String pValue) {
		return mPrefs.edit().putString(pKey, Utils.encrypt(pValue, AdmobModule.mIvKey, AdmobModule.mEncryptKey)).commit();
	}

	public String get(String pKey, String pDefault) {
		String result = mPrefs.getString(pKey, pDefault);
		if (result == null || result.equals(pDefault)) return result;
		return Utils.decrypt(result, AdmobModule.mIvKey, AdmobModule.mEncryptKey);
	}

	public boolean prefsRemove(String key) {
		SharedPreferences.Editor nEditor= mPrefs.edit();
		nEditor.remove(key);
		return nEditor.commit();
	}
	
	public boolean putList(String pKey, ArrayList<String> vList) {
		SharedPreferences.Editor nEditor= mPrefs.edit();
		nEditor.putInt(pKey, vList.size()); /*sKey is an array*/ 

		for (int i = 0; i < vList.size(); i++) {
			nEditor.remove(pKey + "_" + i);
			nEditor.putString(pKey + "_" + i, vList.get(i));  
		}
		return nEditor.commit();
	}
	
	public ArrayList<String> getList(String pKey) {
		ArrayList<String> stringPath = new ArrayList<>();
		
	    int size = mPrefs.getInt(pKey, 0);  
	    for (int i = 0; i < size; i++) {
	        stringPath.add(mPrefs.getString(pKey + "_" + i, null));  
	    }
	    return stringPath;
	}


	/******************************************************************
	 * for Notification Save
	 */
	public boolean setNotificationInfo(String uid, String sid, String sensor_id, int pValue) {
		return mPrefs.edit().putInt(uid + sid + sensor_id, pValue).commit();
	}

	public int getNotificationInfo(String uid, String sid, String sensor_id, int pDefault) {
		return mPrefs.getInt(uid + sid + sensor_id, pDefault);
	}

	/******************************************************************
	 * for AES_KEY Save
	 */
	public boolean putKey(String pValue) {
		return mPrefs.edit().putString(AdmobPrefs.S_KEY, pValue).commit();
	}
	public String getKey(){
		return mPrefs.getString(AdmobPrefs.S_KEY, EMPTY);
	}

}
