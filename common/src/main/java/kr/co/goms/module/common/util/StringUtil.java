package kr.co.goms.module.common.util;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.UnderlineSpan;
import android.util.Base64;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import kr.co.goms.module.common.R;
import kr.co.goms.module.common.curvlet.Curvlet;

import static android.content.Context.CLIPBOARD_SERVICE;

public class StringUtil {
	
	public static boolean isBlank(String str) {
        return (str == null || str.trim().length() == 0);
    }	
	
    public static boolean isEmpty(CharSequence str) {
        return (str == null || str.length() == 0);
    }
    
	public static String booleanToString(boolean b){
		return b ? "true" : "false";
	}
	
	public static boolean stringToBoolean(String b){
		if(b == null){
			return false;
		}
		return "true".equalsIgnoreCase(b) ? true : false;
	}

	/*
	 * float to int
	 */
	public static int floatToInt(float floatValue){
		return (int)floatValue;
	}
	
	/*
	 * Int to String
	 */
	public static String intToString(Integer intValue){
		return String.valueOf(intValue);
	}
	/*
	 * Long to String
	 */
	public static String longToString(Long longValue){
		return String.valueOf(longValue);
	}
	public static String floatToString(Float floatValue){
		return String.valueOf(floatValue);
	}
	/*
	 * Double to String
	 */
	public static String doubleToString(Double doubleValue){
		return String.valueOf(doubleValue);
	}
	
	/*
	 * String to Int
	 */
	public static int stringToInt(String stringValue){
		
			    if (!TextUtils.isEmpty(stringValue)) {
			      try {
			        return Integer.parseInt(stringValue);
			      } catch (NumberFormatException e) {
			        // fall through
			      }
			    }
			    int defaultValue = 0;
				return defaultValue ;
	}
	
	/*
	 * String to Long
	 */
	public static Long stringToLong(String stringValue){
		return Long.parseLong(stringValue);
	}	
	
	/*
	 * String to Float
	 */
	public static Float stringToFloat(String stringValue){
		return Float.parseFloat(stringValue);
	}	
	
	
	/*
	 * String to double
	 */
	public static double stringToDouble(String stringValue){
		return Double.parseDouble(stringValue);
	}	
	
	public static String stringToUTF8(String src){
		String tmp = null;
		try {
			tmp = new String(src.getBytes(), "utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return tmp;
	}	
	
	public static String stringToURLEncoder(String src){
		try {
			return URLEncoder.encode(src, "utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static String uriToString(Uri uriValue){
		return uriValue.toString();
	}

	public static Uri stringToUri(String stringValue){
		return Uri.parse(stringValue);
	}

	public static String parameters2String(ArrayList<String> parameters) {
		StringBuffer stringBuffer = new StringBuffer("?");
		for (String parameter : parameters) {
			if (stringBuffer.length() > 1) {
				stringBuffer.append("&");
			}
			stringBuffer.append(parameter);
		}
		return stringBuffer.toString();
	}

	public static boolean isNull(String string) {
		if (string == null || string.length() == 0 || string.equals("null"))
			return true;
		else
			return false;
	}

	public static boolean isNotNull(String string) {
		if (string != null && string.length() > 0)
			return true;
		else
			return false;
	}

	public static String list2String(ArrayList<String> list) {
		if (list == null || list.size() == 0) {
			return "";
		} else {
			String string = "";
			for (String s : list) {
				string = string + s + ",";
			}
			return string.substring(0, string.length() - 1);
		}
	}
	
	public static ArrayList<String> string2List(String string){
		if (string == null || string.length() == 0 || string.equals("")) {
			return null;
		}
		else {
			ArrayList<String> list = new ArrayList<String>();
			String[] temp  =  string.split(",");
			for (int i = 0; i < temp.length; i++) {
				list.add(temp[i]);
			}
			return list;
		}
	}
	
	public static ArrayList<String> stringDelimiter2List(String string, String delimiter){
		if (string == null || string.length() == 0 || string.equals("")) {
			return null;
		}
		else {
			ArrayList<String> list = new ArrayList<String>();
			String[] temp  =  string.split(delimiter);
			for (int i = 0; i < temp.length; i++) {
				list.add(temp[i]);
			}
			return list;
		}
	}
	
	public static String getAddCommaScoreString(String stringScore){
		StringBuffer _score = new StringBuffer(stringScore);
		
		int j = 1;
		
		for (int i = 0; i < _score.length(); i++) {
			if(0 < _score.length()-(3*(j)+i)){
				_score.insert(_score.length()-(3*(j)+i),",");
				j++;
			}
			else
				break;
		}
		
		return _score.toString();
	}

	
	public static String jsonObject2String(JSONObject jsonObj){
		String string = jsonObj.toString();
		return string;
	}
	
	
	public static JSONObject string2JsonObject(String jsonString){
		JSONObject jsonObj = null;
		try {
			jsonObj = new JSONObject(jsonString);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonObj;
	}
	
	public static CharSequence textToHtml(String text){
		/*
		 * <string name="addting_title03_1"><![CDATA[""<font color="#9ee91d">%1$s</font>"" �Ե��� <br>¯¯�� �������� �Է��� �����?]]></string>
		 */
        CharSequence result = Html.fromHtml(text);
        return result;
	}
	
	public static CharSequence stringHtml(Context context, int resId, String text){
		/*
		 * <string name="addting_title03_1"><![CDATA[""<font color="#9ee91d">%1$s</font>"" �Ե��� <br>¯¯�� �������� �Է��� �����?]]></string>
		 */
        String tmp = context.getString(resId);
        String title = String.format(tmp, text);
        CharSequence result = Html.fromHtml(title);
        return result;
	}
	
	public static CharSequence stringHtml(Context context, int resId, String text01, String text02){
		/*
		 * <string name="addting_title03_1"><![CDATA[""<font color="#9ee91d">%1$s</font>"" �Ե��� <br>¯¯�� �������� �Է��� �����?]]></string>
		 */
        String tmp = context.getString(resId);
        String title = String.format(tmp, text01, text02);
        CharSequence result = Html.fromHtml(title);
        return result;
	}
	
	public static boolean isEditTextEmpty(EditText mInput){
		String str = mInput.getText().toString().trim();
		   if(str.length() == 0)
		      return true;
		   else
		      return false;
	}
	
	public static boolean isTextViewEmpty(TextView mInput){
		String str = mInput.getText().toString().trim();
		   if(str.length() == 0)
		      return true;
		   else
		      return false;
	}	
	
	public static boolean isEditTextEmpty(String mInput){
		String str = mInput.trim();
		   if(str.length() == 0)
		      return true;
		   else
		      return false;
	}
	
	public static String convertCharsequenceToString(CharSequence mInput){
		String str = mInput.toString();
		return str;
	}
	
	public static CharSequence convertStringToCharsequence(String mInput){
		CharSequence str = mInput;
		return str;
	}
	
	public static int getResourceId(String pDrawableName){
	    int resourceId= Resources.getSystem().getIdentifier(pDrawableName, "string", "android");
	    return resourceId;
	}
	
	public static String convertResIdToString(Context context, int resId){
		String tempString = context.getString(resId);
		return tempString;
	}

	/*
	 * Param string : /storage/sdcard0/GomsLayerCamera/goms_layer_37_3_21_37.5475429_127.051254_KR_20140216222512.jpg
	 */
	public static String getFindStringAndEnd(String string, String delimiter)
	{
		int startInt = string.lastIndexOf(delimiter)+1;        		
        String photoName = string.substring(startInt, string.length());
        return photoName;
	}
	
	/*
	 * Param string : "http://www.goms.co.kr"
	 */
	public static Uri convertStringToUri(String string)
	{
		Uri uri = Uri.parse(string);
        return (Uri) uri;
	}	
	
	// ------------------------------------------------------------- replacement

	/**
	 * Ư�� ���ڸ� Ư�� ���ڿ��� �ٲ��ݴϴ�.
	 *
	 * ���� ���, Ư�� ���ڸ� escape ó���ϰ��� �� �� �̿��� �� �ִ�.<br>
	 * <blockquote><pre>
	 * pstmt = con.prepareStatement(...);
	 * pstmt.setString(1, SringUtil.replace("bjkim's home", '\'', "''"));
	 * </pre></blockquote>
	 *
	 * @param source 	�� ���ڿ��̴�.
	 * @param ch		�ٲٰ��� �ϴ� �����̴�.
	 * @param replace	��ġ�ϰ��� �ϴ� ���ڿ��̴�.
	 *
	 * @see #replace(String source, char ch, String replace, int max)
	 */
	public static String replace(String source, char ch, String replace)
	{
		return replace(source, ch, replace, -1);
	}

	/**
	 * Ư�� ���ڸ� Ư�� ���ڿ��� �ٲ��ݴϴ�.
	 *
	 * @param source 	�� ���ڿ��̴�.
	 * @param ch		�ٲٰ��� �ϴ� �����̴�.
	 * @param replace	��ġ�ϰ��� �ϴ� ���ڿ��̴�.
	 * @param max		��� �ٲ� �������� ��Ÿ����. <code>-1</code>�̸�
	 *					��� �ٲ۴�
	 *
	 * @see #replace(String source, String original, String replace, int max)
	 */
	public static String replace(
		String source,
		char ch,
		String replace,
		int max)
	{
		return replace(source, ch + "", replace, max);
	}

	/**
	 * Ư�� ���ڿ��� Ư�� ���ڿ��� �ٲ��ݴϴ�.
	 *
	 * <blockquote><pre>
	 * String str = StringUtil.replace("Java \r\n is \r\n Wonderful",
	 *								   "\r\n", "<BR>");
	 * </pre></blockquote>
	 *
	 * @param source 	�� ���ڿ��̴�.
	 * @param original	�ٲٰ��� �ϴ� ���ڿ��Դϴ�.
	 * @param replace	��ġ�ϰ��� �ϴ� ���ڿ��̴�.
	 */
	public static String replace(
		String source,
		String original,
		String replace)
	{
		return replace(source, original, replace, -1);
	}

	/**
	 * Ư�� ���ڿ��� Ư�� ���ڿ��� �ٲ��ݴϴ�. ���ڿ��� null�̶�� null�� �����Ѵ�.
	 *
	 * @param source 	�� ���ڿ��̴�.
	 * @param original	�ٲٰ��� �ϴ� ���ڿ��Դϴ�.
	 * @param replace	��ġ�ϰ��� �ϴ� ���ڿ��̴�.
	 * @param max		��� �ٲܰ������� ��Ÿ����. <code>-1</code>�̸�
	 *					��� �ٲ۴�.
	 */
	public static String replace(
		String source,
		String original,
		String replace,
		int max)
	{
		if (null == source)
			return null;
		int nextPos = 0; // ���� position
		int currentPos = 0; // ���� position
		int len = original.length();
		StringBuffer result = new StringBuffer(source.length());
		while ((nextPos = source.indexOf(original, currentPos)) != -1)
		{
			result.append(source.substring(currentPos, nextPos));
			result.append(replace);
			currentPos = nextPos + len;
			if (--max == 0)
			{ // �ٲ� Ƚ���� �پ��ش�
				break;
			}
		}
		if (currentPos < source.length())
		{
			result.append(source.substring(currentPos));
		}
		return result.toString();
	}
	
	/**
	 * Character.toTitleCase()�� �̿��Ͽ� �빮�� �Ѵ�
	 *
	 */
	public static String toTitleCase(String str, int len)
	{
		if (null == str)
			return null;
		int strLen = str.length();
		int index = 0;
		StringBuffer sb = new StringBuffer(str.length());
		while ((index < len) && (index < strLen))
		{
			sb.append(Character.toTitleCase(str.charAt(index)));
			++index;
		}
		if (index < strLen)
		{
			sb.append(str.substring(index));
		}
		return sb.toString();
	}

	/**
	 * ù ���ڸ� �ҹ��ڷ� �ٲپ� �ش�. �Է� String�� null�̶��
	 * null�� �����Ѵ�.
	 *
	 * <blockquote><pre>
	 * String str = StringUtil.toFirstLowerCase("Java"); // result --> "java"
	 * </pre></blockquote>
	 */
	public static String toFirstLowerCase(String str)
	{
		return (
			null == str
				? null
				: str.substring(0, 1).toLowerCase() + str.substring(1));
	}
	
	/**
	 * String�� ������� �и����� ���ڿ��� �����Ѵ�.
	 *
	 * <blockquote><pre>
	 * String[] strs = StringUtil.split("Java is wonderful");
	 * // strs[0] = "Java"; strs[1] = "is"; strs[2] = "wonderful"
	 * </pre></blockquote>
	 *
	 * #see split(String str, String delim)
	 */
	public static String[] split(String str)
	{
		return split(str, " ", -1);
	}

	/**
	 * String�� Ư�� ���ڷ� �и��Ͽ� ���ڿ� �迭�� �����Ѵ�.
	 *
	 * #see split(String str, char delim, int max)
	 */
	public static String[] split(String str, char delim)
	{
		return split(str, delim + "", -1);
	}

	/**
	 * String�� Ư�� ���ڷ� �и��Ͽ� ���ڿ� �迭�� �����Ѵ�.
	 *
	 * @see #split(String str, String delim, int max)
	 */
	public static String[] split(String str, char delim, int max)
	{
		return split(str, delim + "", max);
	}

	/**
	 * String�� Ư�� ���ڿ��� �и����� ���ڿ� �迭�� �����Ѵ�.
	 *
	 * <blockquote><pre>
	 * String[] strs = StringUtil.split("a=b&b=c&c=d", "&");
	 * // strs[0] = "a=b"; strs[1] = "b=c"; strs[2] = "c=d"
	 * </pre></blockquote>
	 *
	 * @see #split(String str, String delim, int max)
	 */
	public static String[] split(String str, String delim)
	{
		return split(str, delim, -1);
	}

	/**
	 * String�� Ư�� ���ڿ��� Ư�� Ƚ����ŭ �и��ؼ� ���ڿ��� �����Ѵ�.
	 *
	 * @param str		�и��ϰ��� �ϴ� ���ڿ��� ��Ÿ����.
	 * @param delim		�и��ڸ� ��Ÿ����.
	 * @param max		�и� Ƚ���� ��Ÿ����. <code>-1</code>��
	 *					��ü�� �и��Ѵ�.
	 */
	public static String[] split(String str, String delim, int max)
	{
		int nextPos = 0;
		int currentPos = 0;
		int len = delim.length();
		List list = new ArrayList();
		while ((nextPos = str.indexOf(delim, currentPos)) != -1)
		{
			list.add(str.substring(currentPos, nextPos));
			currentPos = nextPos + len;
			if (--max == 0)
			{ // �и� Ƚ���� �پ��ش�
				break;
			}
		}
		if (currentPos < str.length())
		{
			list.add(str.substring(currentPos));
		}
		return (String[]) list.toArray(new String[0]);
	}	
	
	/**
	 * Ư�� ���̸�ŭ �ڸ��� ...��  �����Ѵ�.
	 * @param src
	 */
	public static String omit(String src, int len)
	{
		if(src==null || src.length()-1<len)
		{
			return src;
		}
		else
		{
			return src.substring(0,len)+"...";
		}
	}	
	
	public static String cutFirstLen(String src, int len)
	{
		if(src==null || src.length()-1<len)
		{
			return src;
		}
		else
		{
			return src.substring(0,len);
		}
	}		
	
	/** 5�ڸ�~13�ڸ����� */
	public static boolean checkIsInvitationCodeNumber(String src){
        String regexStr = "\\d{5,13}";
        Pattern pattern = Pattern.compile(regexStr);
        Matcher matcher = pattern.matcher(src);
        boolean isNumber = matcher.matches();     
		return isNumber;
	}
	
	public static <T> boolean checkArrayContainsData(final T[] array, final T v) {
	    if (v == null) {
	        for (final T e : array)
	            if (e == null)
	                return true;
	    } else {
	        for (final T e : array)
	            if (e == v || v.equals(e))
	                return true;
	    }

	    return false;
	}
	
	public static String convertTagShap(String tmp){
    	tmp = StringUtil.replace(tmp, ",", "#");
    	tmp = StringUtil.replace(tmp, "��", "#");
    	tmp = StringUtil.replace(tmp, "#", " #");
    	StringBuilder sb = new StringBuilder("#");
    	sb.append(tmp);
    	return sb.toString();
	}
	
	public static String convertColorIntToString(int color)
	{
		String strColor = String.format("#%06X", 0xFFFFFF & color);
		return strColor;
	}
	
	public static String removeLastChar(String str) {
        return str.substring(0,str.length()-1);
    }
	
	public static String convertMapToArray(Map map, String delim) {
		TreeMap<Integer, Integer> treeMap = new TreeMap<Integer, Integer>( map );
        Iterator<Integer> treeMapIter = treeMap.keySet().iterator();
        StringBuffer stringBuffer = new StringBuffer();
        
        while( treeMapIter.hasNext()) {
        	 
        	Integer key = treeMapIter.next();
        	Integer value = treeMap.get( key );
 
            stringBuffer.append(value);
			stringBuffer.append(",");
 
        }

        String tmp = StringUtil.removeLastChar(stringBuffer.toString());
        return tmp;
    }

	public static String convertArrayListToRange(ArrayList<Integer> range) {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("(");
		for (Integer integer : range) {
			stringBuffer.append(integer);
			stringBuffer.append(",");
		}

		String tmp = StringUtil.removeLastChar(stringBuffer.toString());

		StringBuffer sb = new StringBuffer();
		sb.append(tmp);
		sb.append(")");

		return sb.toString();	// {0,1,2}-> (0,1,2)로 처리
	}

	/**
	 * @param email
	 * @return
	 * if (!validateEmail(username)) {
	usernameWrapper.setError("Not a valid email address!");
	}
	 */
	public static boolean validateEmail(String email) {
		String EMAIL_PATTERN = "^[a-zA-Z0-9#_~!$&'()*+,;=:.\"(),:;<>@\\[\\]\\\\]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*$";
		Pattern emailPattern = Pattern.compile(EMAIL_PATTERN);
		Matcher emailMatcher = emailPattern.matcher(email);
		return emailMatcher.matches();
	}

	public static String substr(String src, int s, int l)
	{
		try
		{
			return src.substring(s, l);
		}
		catch(Exception e)
		{
			return "";
		}
	}
	public static String substr(String src, int s)
	{
		try
		{
			return src.substring(s);
		}
		catch(Exception e)
		{
			return "";
		}
	}

	/**
	 * 클립보드 복사하기
	 * @param activity
	 * @param copy
	 */
	public static void copyClipboard(Activity activity, String copy) {
		ClipboardManager myClipboard = (ClipboardManager) activity.getSystemService(CLIPBOARD_SERVICE);
		try {
			ClipData myClip = ClipData.newPlainText("text", copy);
			myClipboard.setPrimaryClip(myClip);
			Toast.makeText(activity, activity.getString(R.string.copy_ok), Toast.LENGTH_SHORT).show();
		} catch (NullPointerException e) {
			Toast.makeText(activity, activity.getString(R.string.copy_fail), Toast.LENGTH_SHORT).show();
		}
	}


	public static int getIndexOf(String src, String tar)
	{
		try
		{
			return src.indexOf(tar);
		}
		catch(Exception e)
		{
			return -1;
		}
	}
	public static int getIndexOf(String src, String tar, int start)
	{
		try
		{
			return src.indexOf(tar, start);
		}
		catch(Exception e)
		{
			return -1;
		}
	}

	/**
	 * 리스너 아규먼트 넘기기 위해서 ArrayList 객체를 생성한다.
	 * @param vals 추가할 아규먼트들...
	 * @return ArrayList 객체
	 */
	public static ArrayList<Object> getArrayAtArg(Object ... vals)
	{
		ArrayList<Object> arg = new ArrayList<Object>();
		for(Object val : vals) arg.add(val);
		return arg;
	}

	public static Bitmap bitmapDecodeBase64(String input) {
		byte[] decodedByte = Base64.decode(input, Base64.DEFAULT);
		return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
	}

	public static String bitmapEncodeBase64(Bitmap bitmap) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
		byte[] b = baos.toByteArray();

		return Base64.encodeToString(b, Base64.DEFAULT);
	}

	private static void underlineTextView(TextView textView, String text){
		// 문자 밑줄 작업
		SpannableStringBuilder ssb = new SpannableStringBuilder(text);
		ssb.setSpan(new UnderlineSpan(), 0, text.length(), 0);
		textView.setText(ssb);
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

	public static String getQueryParameter(Map<String, String> parameters) {
		Set<String> keys = parameters.keySet();
		StringBuilder query = new StringBuilder();
		String value = "";
		Iterator var4 = keys.iterator();

		while(var4.hasNext()) {
			String key = (String)var4.next();
			value = (String)parameters.get(key);
			if (key != null && value != null) {
				if (query.length() > 0) {
					query.append("&");
				}

				query.append(key);
				query.append("=");
				query.append(getPercentEncode(value));
			}
		}

		return query.toString();
	}

	public static String getPercentEncode(String str) {
		if (str == null) {
			return "";
		} else {
			String encodedStr = str;

			try {
				encodedStr = URLEncoder.encode(str, "UTF-8");
			} catch (UnsupportedEncodingException var3) {
				Log.d("ContentValues", "getPercentEncode::str [ " + str + " ]");
			}

			return encodedStr;
		}
	}

	/**
	 * url의 params을 Hashmap으로 변환
	 * @param url	store_idx=1&store_mb_idx=2
	 * @return
	 */
	public static HashMap<String, String> getParamListFromUrl(String url) {
		Uri uri = Uri.parse(url);

		HashMap<String, String> hashMap = new HashMap<>();

		Set<String> list = uri.getQueryParameterNames();
		for (String key : list) {
			String value = uri.getQueryParameter(key);
			GomsLog.d("Stemp", "key : " + key + ", value : " + value);  //store_idx=1&store_mb_idx=2
			hashMap.put(key, value);
		}

		return hashMap;
	}

	public static boolean isNumeric(String str) {
		if (str == null) {
			return false;
		}
		int sz = str.length();
		for (int i = 0; i < sz; i++) {
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 사업자 번호 유효성 검사
	 */
	private final static int[] LOGIC_NUM = {1, 3, 7, 1, 3, 7, 1, 3, 5, 1};

	public final static boolean isBizNumValid(String regNum) {

		if (!isNumeric(regNum) || regNum.length() != 10)
			return false;

		int sum = 0;
		int j = -1;
		for (int i = 0; i < 9; i++) {
			j = Character.getNumericValue(regNum.charAt(i));
			sum += j * LOGIC_NUM[i];
		}

		sum += (int) (Character.getNumericValue(regNum.charAt(8)) * 5 /10);

		int checkNum = (10 - sum % 10) % 10 ;
		return (checkNum == Character.getNumericValue(regNum.charAt(9)));
	}


}
