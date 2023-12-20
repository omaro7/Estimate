package kr.co.goms.module.common.util;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.format.Time;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtil {

	public static long getSystemCurrentTimeMillis(){
		return System.currentTimeMillis();
	}

	public static String getDatePhotoTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String currentDateandTime = sdf.format(new Date());
		return currentDateandTime;
	}

	public static String getMilliToDate(long milliseconds) {
		// get date time in custom format
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		return sdf.format(new Date(milliseconds));
	}


	public static String getMilliToDateFormat(long milliseconds, String format) {
		// get date time in custom format
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(new Date(milliseconds));
	}

	public static String getMilliToMonth(long milliseconds) {
		// get date time in custom format
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
		return sdf.format(new Date(milliseconds));
	}

	public static String getDateTime01() {
		// get date time in custom format
		SimpleDateFormat sdf = new SimpleDateFormat("[yyyy/MM/dd - HH:mm:ss]");
		return sdf.format(new Date());
	}

	public static String getDateTime02() {
		// get date time in custom format
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
		return sdf.format(new Date());
	}

	public static String getDateTime03() {
		// get date time in custom format
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(new Date());
	}

	public static String getTimeHMS() {
		// get date time in custom format
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		return sdf.format(new Date());
	}

	public static String getTimeHM() {
		// get date time in custom format
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		return sdf.format(new Date());
	}

	public static String getConvertTime(String date) {
		// get date time in custom format
		SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat outputFormat = new SimpleDateFormat("HH:mm");
		Date parsed = new Date();
		try {
			parsed = inputFormat.parse(date);
			String outputText = outputFormat.format(parsed);
			return outputText;
		} catch (ParseException e) {
			//e.printStackTrace();
			return "";
		}
	}

	public static String getConvertDateTimeToDate(String datetime, String dateFormat) {
		// get date time in custom format
		SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat outputFormat = new SimpleDateFormat(dateFormat);
		Date parsed = new Date();
		try {
			parsed = inputFormat.parse(datetime);
			String outputText = outputFormat.format(parsed);
			return outputText;
		} catch (ParseException e) {
			//e.printStackTrace();
			return "";
		}
	}

	/*
	 * getConvertDateTimeToAMPMDate("2016-03-22 11:19:40", "aa KK:mm", Locale.US);
	 */
	public static String getConvertDateTimeToAMPMDate(String datetime, String dateFormat, Locale locale) {
		// get date time in custom format
		SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat outputFormat = new SimpleDateFormat(dateFormat, locale);
		Date parsed = new Date();
		try {
			parsed = inputFormat.parse(datetime);
			String outputText = outputFormat.format(parsed);
			return outputText;
		} catch (ParseException e) {
			//e.printStackTrace();
			return "";
		}
	}

	/*
	 * format : "yyyy.MM.dd"
	 */
	public static String getConvertDateFormat(String date, String inFormat, String outFormat) {
		// get date time in custom format
		SimpleDateFormat inputFormat = new SimpleDateFormat(inFormat);
		SimpleDateFormat outputFormat = new SimpleDateFormat(outFormat);
		Date parsed = new Date();
		try {
			parsed = inputFormat.parse(date);
			String outputText = outputFormat.format(parsed);
			return outputText;
		} catch (ParseException e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * @param date "2016.08.04 12:07:14
	 * @param dateFormat "yyyy.MM.dd"
	 * @return
	 */
	public static String getConvertDate(String date, String dateFormat) {
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		return df.format(date);
	}

	public static String getConvertDateTime(String date) {
		DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		return df.format(date);
	}

	public static String getConvertData(Date date){
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd/ HH:mm:ss");
		String formattedDateString = formatter.format(date);
		return formattedDateString;
	}

	public static String getTomorrow(){
		Calendar c = new GregorianCalendar();
		c.add(Calendar.DAY_OF_MONTH, 1);
		SimpleDateFormat sdf = new SimpleDateFormat("[yyyy/MM/dd - HH:mm:ss]");
		return sdf.format(c.getTime());
	}

	public static String targetDate(){

		SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy, HH:mm");
		formatter.setLenient(false);

		Calendar now = Calendar.getInstance();
		int year = now.get(Calendar.YEAR);
		int month = now.get(Calendar.MONTH) + 1;
		int day = now.get(Calendar.DATE);

		int maxDay = now.getActualMaximum(Calendar.DAY_OF_MONTH);

		if(day == maxDay){
			month++;	//마지막 날이면 Month +1

			if(month == 12){
				year++;	//12월 마지막 날이면 year +1
			}
		}

		now.add(Calendar.DATE,1);
		int date = now.get(Calendar.DATE);

		//String TargetDate = "16.02.2013, 03:13";
		String TargetDate = Integer.toString(date) + "." + Integer.toString(month) + "." + Integer.toString(year) + ", 03:13";
		return TargetDate;

	}

	public static Date date311(){
		long MILLIS_IN_DAY = 86400000;
		//long TARGET_TIME = 11460000;		//03시 11분
		long TARGET_TIME = 11580000;		//03시 13분
		long TARGET_TOTAL_TIME = MILLIS_IN_DAY + 11460000;

		Date time = new Date(TARGET_TOTAL_TIME);
		return time;
	}

	public static long targetTime311(){
		long MILLIS_IN_DAY = 86400000;
		long TARGET_TIME = 11460000;		//03시 11분
		long TARGET_TOTAL_TIME = MILLIS_IN_DAY + 11460000;
		long currentTime = System.currentTimeMillis();
		long millisTillNow = currentTime % MILLIS_IN_DAY;
		long resultTime = MILLIS_IN_DAY - millisTillNow;
		return resultTime;
	}

	public static long targetTime(){
		long MILLIS_IN_DAY = 86400000;
		long TARGET_TIME = 11460000;		//03시 11분
		long TARGET_TOTAL_TIME = MILLIS_IN_DAY + 11460000;
		long currentTime = System.currentTimeMillis();
		long millisTillNow = currentTime % MILLIS_IN_DAY;
		long resultTime = TARGET_TOTAL_TIME - millisTillNow;
		return resultTime;
	}

	public static String milliToTime(long milliseconds){
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		sdf.setTimeZone(TimeZone.getTimeZone("GMT+0"));
		return sdf.format(new Date(milliseconds));
	}

	public static long longGetTime(){
		long currentTime = System.currentTimeMillis();
		return currentTime;
	}

	public static String formatTime(String time)
	{
		String fullTime= "";
		String[] sa = new String[2];

		if(time.length()>1)
		{
			Time t = new Time(Time.getCurrentTimezone());
			t.parse(time);
			// or t.setToNow();
			String formattedTime = t.format("%d.%m.%Y %H.%M.%S");
			int x = 0;

			for(String s : formattedTime.split("\\s",2))
			{
				System.out.println("Value = " + s);
				sa[x] = s;
				x++;
			}
			fullTime = "Date: " + sa[0] + " Time: " + sa[1];
		}
		else{
			fullTime = "No time data";
		}
		return fullTime;
	}


	/**
	 * 어플리케이션에서 날짜들은 yyyyMMdd 포맷(예: 20020622) 등의 스트링 타입으로 처리되어야 한다
	 * 이 클래스 변수의 값은 yyyyMMdd 로 초기화되어 있다
	 */
	private static final String DFLT_IN_FORMAT    = "yyyyMMdd";
	private static final String DFLT_OUT_FORMAT   = "yyyyMMdd";

	private static final int BEFORE=-1;
	private static final int AFTER=1;
	private static final int EQUAL=0;

	public DateUtil(){}


	public static String getCurrentDate()
	{
		String dateFormat = "yyyyMMdd";
		Format df = new SimpleDateFormat(dateFormat);
		return df.format(new Date());
	}

	public static String getCurrentDateTime()
	{
		String dateFormat = "yyyyMMddHHmmss";
		Format df = new SimpleDateFormat(dateFormat);
		return df.format(new Date());
	}


	public static String getCurrentDateTimeMilli()
	{
		String dateFormat = "yyMMddHHmmssS";
		Format df = new SimpleDateFormat(dateFormat);
		return df.format(new Date());
	}


	/**
	 * date 로 받은 날자가 그레고리안단력(양력 달력)에 존재하는 날자인지를 확인해준다
	 *
	 * @param date  검증받고 싶은 날자
	 * @return  실존하는 날자일 경우 true, 존재하지 않을 경우 false 를 리턴
	 * @throws ParseException 입력날자의 포맷이 틀렸을 경우 던져진다<br>
	 * 예를 들면 2002/02/04, 2002-05-06 등을 파라미터로 받을 경우 예외가 발생한다
	 */
	public static boolean isExist(String date) throws ParseException
	{
		try
		{
			stringToDate(date, DFLT_IN_FORMAT);
		}
		catch(ParseException pEx)
		{
			if( pEx instanceof NonExistDateException)
				return false;
			else
				throw pEx;
		}
		return true;
	}

	/**
	 * date로 받은 날자가 그레고리안단력(양력 달력)에 존재하는 날짜인지를 확인해준다
	 * 이 메소드는 yyyyMMdd뿐만 아니라 yyMMdd형태도 지원한다.
	 *
	 * @param date     검증받고 싶은 날자
	 * @param baseYear 기준 year, date가 yyMMdd인 경우에 baseYear보다 크다면
	 *                 19를 앞에 부치고 작다면 20를 부친다.
	 *
	 * @return  실존하는 날자일 경우 true, 존재하지 않을 경우 false 를 리턴
	 *
	 * @throws ParseException 입력날자의 포맷이 틀렸을 경우 던져진다<br>
	 *         예를 들면 2002/02/04, 2002-05-06 등을 파라미터로 받을 경우
	 *         예외가 발생한다.
	 */
	public static boolean isExist(String date, int baseYear)
			throws ParseException
	{
		if (date == null && date.length() < 6)
			return false;
		if (date != null && date.length() == 6)
		{
			int yy = Integer.parseInt(date.substring(0, 2));
			if (yy > baseYear) {
				date = 19 + date;
			} else {
				date = 20 + date;
			}
		}
		else if (date != null && date.length() == 8)
		{
			int yyyy = Integer.parseInt(date.substring(0, 4));
			if (yyyy > baseYear) {
				date = 19 + date.substring(2);
			} else {
				date = 20 + date.substring(2);
			}
		}
		return isExist(date);
	}

	/**
	 * 지정된 일자의 몇일 후 일자를 얻을 수 있다
	 * @param date  지정된 날짜
	 * @param day 몇일 후로 지정할 지 입력한다
	 * @return  몇일 후 일자를 리턴
	 * @throws ParseException
	 */
	public static String afterSpecDay(String date, int day) throws ParseException {  // 지정된 일자의 몇일후 날짜
		Calendar calendar = stringToCalendar(date);
		calendar.add(Calendar.DATE, day);     // calendar 자체는 변경이 되면 안되니...
		return printCalendar(calendar);
	}

	/**
	 * 지정된 일자의 몇년,몇개월,몇일 후 일자를 얻을 수 있다
	 * @param date
	 * @param year
	 * @param month
	 * @param day
	 * @return
	 */
	public static String afterSpecDay(String date, int year, int month, int day)throws ParseException
	{
		Calendar calendar = stringToCalendar(date);
		calendar.add(Calendar.YEAR, year);
		calendar.add(Calendar.MONTH, month);
		calendar.add(Calendar.DATE, day);     // calendar 자체는 변경이 되면 안되니...
		return printCalendar(calendar);
	}

	/**
	 * 지정된 일자의 몇일 전 일자를 얻을 수 있다
	 * @param date
	 * @param day
	 * @return
	 * @throws ParseException
	 */
	public static String beforeSpecDay(String date, int day) throws ParseException
	{ // 지정된 일자의 몇일 전 날짜
		Calendar calendar = stringToCalendar(date);
		calendar.add(Calendar.DATE, -day);
		return printCalendar(calendar);
	}

	/**
	 * 지정된 일자의 몇년,몇개월,몇일 전 일자를 얻을 수 있다
	 * @param date 지정된 일자
	 * @param year 몇년
	 * @param month 몇달
	 * @param day 몇일
	 * @return 이전 일자를 문자열 형태로 반환한다.
	 * @throws ParseException
	 */
	public static String beforeSpecDay(String date, int year, int month, int day) throws ParseException
	{ // 지정된 일자의 몇일 전 날짜
		Calendar calendar = stringToCalendar(date);
		calendar.add(Calendar.YEAR, -year);
		calendar.add(Calendar.MONTH, -month);
		calendar.add(Calendar.DATE, -day);
		return printCalendar(calendar);
	}

	/**
	 * 지정된 일자의 해당년도 총일수를 리턴한다
	 *
	 * @param date 지정된 일자를 나타내는 문자열
	 * @return int 지정된 일자의 해당년도 총 일수를 반환
	 * @throws ParseException
	 */
	public static int getDayOfYear(String date) throws ParseException
	{ // 지정된 일자의 해당년도의 총일 수
		Calendar calendar = stringToCalendar(date);
		if (((GregorianCalendar)calendar).isLeapYear(calendar.get(Calendar.YEAR)))
			return 366 ;
		else
			return 365 ;
	}

	/**
	 * 지정된 일자의 몇 개월 전 초일을 리턴한다
	 *
	 * @param date 지정된 일자
	 * @param month 전으로 세팅해야 할 몇달
	 * @return 지정된 일자의 몇 개월 전 초일을 리턴한다
	 * @throws ParseException
	 */
	public static String getFirstDayBeforeMonth(String date, int month) throws ParseException
	{// 지정된 일자의 몇개월 전 초일
		Calendar calendar = stringToCalendar(date);
		calendar.add(Calendar.MONTH, -month);                        // 몇개월 전으로 셋
		int firstDay = 1;
		calendar.set(Calendar.DATE, firstDay);                       // 초일로 셋
		return printCalendar(calendar);
	}

	/**
	 * 지정된 일자의 몇 개월 전 말일을 리턴한다
	 *
	 * @param date
	 * @param month
	 * @return
	 * @throws ParseException
	 */
	public static String getLastDayOfBeforeMonth(String date, int month) throws ParseException
	{ // 지정된 일자의 몇개월 전 말일
		Calendar calendar = stringToCalendar(date);

		calendar.add(Calendar.MONTH, -month);                        // 몇개월 전으로 셋
		int lastDay = calendar.getActualMaximum(Calendar.DATE);
		calendar.set(Calendar.DATE, lastDay);                       // 말일로 셋
		return printCalendar(calendar);
	}

	/**
	 * 지정된 일자의 몇 개월 후 초일을 리턴한다
	 *
	 * @param date
	 * @param month
	 * @return
	 * @throws ParseException
	 */
	public static String getFirstDayAfterMonth(String date, int month) throws ParseException
	{// 지정된 일자의 몇개월 후 초일
		Calendar calendar = stringToCalendar(date);

		calendar.add(Calendar.MONTH, month);                        // 몇개월 후로 셋
		int firstDay = 1;// 초일???
		calendar.set(Calendar.DATE, firstDay);                       // 초일로 셋
		return printCalendar(calendar);
	}

	/**
	 * 지정된 일자의 몇 개월 후 말일을 리턴한다
	 *
	 * @param date
	 * @param month
	 * @return
	 * @throws ParseException
	 */
	public static String getLastDayAfterMonth(String date, int month) throws ParseException
	{ // 지정된 일자의 몇개월 후 말일
		Calendar calendar = stringToCalendar(date);

		calendar.add(Calendar.MONTH, month);                        // 몇개월 후로 셋
		int lastDay = calendar.getActualMaximum(Calendar.DATE);
		calendar.set(Calendar.DATE, lastDay);                       // 말일로 셋
		return printCalendar(calendar);
	}

	/**
	 * 지정된 일자가 말일인지 검증한다
	 * @param date
	 * @return  말일이면 true, 말일이 아니면 false 를 리턴한다
	 * @throws ParseException
	 */
	public static boolean isLastOfMonth(String date) throws ParseException
	{ // 지정한 일자가 월말인지 체크
		Calendar calendar = stringToCalendar(date);
		return calendar.get(Calendar.DATE) == calendar.getActualMaximum(Calendar.DATE);
	}

	/**
	 * 지정된 두 일자간의 차일수
	 * @param fromDate  첫 번째 날자
	 * @param toDate    두 번째 날자
	 * @return 두일자의 차일수를 반환
	 * @throws ParseException
	 */
	public static int getBetweenDates(String fromDate, String toDate) throws ParseException
	{// 지정된 두 일자간의 차일수
		long betWeenTime = Math.abs(stringToDate(fromDate).getTime() - stringToDate(toDate).getTime());
		return (int) (betWeenTime / (1000*60*60*24));
	}

	/**
	 * 지정된 두 일자간의 차일수
	 * @param fromDate  첫 번째 날자
	 * @param toDate    두 번째 날자
	 * @param isMathAbs 절대값비교 여부( 첫번째 날이 두번째 날짜보다 클 경우 마이너스 기호 여부 체크
	 * @return 두일자의 차일수를 반환
	 * @throws ParseException
	 */
	public static int getBetweenDates(String fromDate, String toDate, boolean isMathAbs) throws ParseException
	{// 지정된 두 일자간의 차일수
		long betWeenTime = isMathAbs?Math.abs(stringToDate(fromDate).getTime() - stringToDate(toDate).getTime()):stringToDate(fromDate).getTime() - stringToDate(toDate).getTime();
		return (int) (betWeenTime / (1000*60*60*24));
	}

	/**
	 * 지정된 두 시간간의 차이 시간(초)
	 * @param fromDate(yyyyMMddHHmmss)  첫 번째 날자시간
	 * @param toDate(yyyyMMddHHmmss)    두 번째 날자시간
	 * @return 두시간의 차일시간을 반환
	 * @throws ParseException
	 */
	public static int getBetweenDateTimes(String fromDate, String toDate) throws ParseException
	{// 지정된 두 시간간의 차이 시간(초)
		long betWeenTime = Math.abs(stringToDate(fromDate, "yyyyMMddHHmmss").getTime() - stringToDate(toDate, "yyyyMMddHHmmss").getTime());
		return (int) (betWeenTime / (1000));
	}

	/**
	 * java.util.Calendar 인스턴스를 지정된 출력 포맷을 통해 스트링 타입으로 변환해준다.
	 * 현재 버전은 다양한 입출력 포맷을 지원하지 않으므로 calendarToString() 메소드와 같다<br>
	 * calendarToString() 은 입력포맷을 적용하여 스트링 타입으로 변환한다.   *
	 * @param cal
	 * @return
	 */
	private static String printCalendar(Calendar cal)
	{
		SimpleDateFormat formatter = new SimpleDateFormat(DFLT_OUT_FORMAT);
		Date date = cal.getTime();
		return formatter.format(date);
	}

	/**
	 * 현재 시간을 yyyyMMdd 타입으로 리턴해준다
	 * @param format "yyyyMMdd"
	 * @return
	 */
	public static String getToday(String format)
	{
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		return formatter.format(new Date());
	}

	/**
	 * 현재 시간으로 부터 내일을 yyyyMMdd 타입으로 리턴해준다
	 * @param format "yyyyMMdd"
	 * @return
	 */
	public static String getTomorrow(String format)
	{
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, 1);
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		return formatter.format(cal.getTime());
	}

	/**
	 * 현재 시간으로 부터 일주일을 yyyyMMdd 타입으로 리턴해준다
	 * @param format "yyyyMMdd"
	 * @return
	 */
	public static String getAfterWeekToday(String format)
	{
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, 7);
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		return formatter.format(cal.getTime());
	}

	/**
	 * 원하는 yyyyMMdd에서 하루를 더함.
	 * @param targetDate"20221111"
	 * @param format	"yyyyMMdd"
	 * @param addDay 추가할 날짜수. 다음날이면 1로 지정
	 * @return
	 */
	public static String getAddDayFromTarget(String targetDate, String format, int addDay){
		Calendar calendar = null;
		try {
			calendar = DateUtil.stringToCalendar(targetDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		calendar.add(Calendar.DATE, addDay);
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		return formatter.format(calendar.getTime());
	}

	public static Calendar getNowCalendar()
	{
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		return cal;
	}

	/**
	 * java.lang.String 타입을 java.util.Date 타입으로 변환한다
	 * @param s
	 * @return
	 * @throws ParseException
	 */
	public static Date stringToDate(String s) throws ParseException
	{
		return stringToDate(s, DFLT_IN_FORMAT);
	}

	/**
	 * java.lang.String 타입을 java.util.Date 타입으로 변환한다
	 * 지원하는 format은 yyMMdd와 yyyyMMddd이다.
	 *
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	public static Date stringToDate(String date, int baseYear) throws ParseException
	{
		if (date != null && date.length() == 6) {
			int yy = Integer.parseInt(date.substring(0, 2));
			if (yy > baseYear) {
				date = 19 + date;
			} else {
				date = 20 + date;
			}
		} else if (date != null && date.length() == 8) {
			int yyyy = Integer.parseInt(date.substring(0, 4));
			if (yyyy > baseYear) {
				date = 19 + date.substring(2);
			} else {
				date = 20 + date.substring(2);
			}
		}
		return stringToDate(date);
	}

	/**
	 * java.lang.String 타입을 java.util.Date 타입으로 변환한다.
	 * 변환하는 과정에서 DateFormat.parse() API 를 사용하므로 변환하는 중에 파리미터로 입력받은 s, 즉 날자의
	 * 포맷과 DateFormat 생성시 사용한 포맷이 동일한 지도 체크된다.
	 * 예를 들면 s 가 2000/02/05 일 경우 현재 버전에서는 입력포맷을 yyyyMMdd 만 사용하므로 두 포맷이
	 * 다르기 때문에 java.text.ParseException 이 발생한다<br>
	 * @param s
	 * @param inFormat
	 * @return
	 * @throws ParseException
	 */
	public static Date stringToDate(String s, String inFormat) throws ParseException
	{
		if ( s == null )
			throw new ParseException("date string is null", 0);
		if ( inFormat == null )
			throw new ParseException("format string is null", 0);

		SimpleDateFormat formatter = new SimpleDateFormat(inFormat);
		Date date = null;

		try {
			date = formatter.parse(s);
		}
		catch(ParseException e) {
			throw new ParseException(" wrong format or date :\"" + s +
					"\" with format \"" + inFormat + "\"", 0);
		}
		// ex) s가 2002/02/03 일때, formatter 가 디폴트로 yyyy/MM/dd 로 셋팅되어 있으므로 아래와 같은 로직이 가능하다
		// 항상 s 문자열의 포맷으로 formatter 를 생성해야 한다.
		if ( ! formatter.format(date).equals(s) )
			throw new NonExistDateException(
					"Out of bound date:\"" + s + "\" with format \"" + inFormat + "\"");

		return date;
	}

	/**
	 * java.util.Date 타입을  java.lang.String 타입으로 변환한다
	 * @param date
	 * @return
	 */
	public static String dateToString(Date date)
	{
		return dateToString(date, DFLT_IN_FORMAT);
	}
	/**
	 * java.util.Date 타입을  java.lang.String 타입으로 변환한다
	 * @param date
	 * @return
	 */
	public static String dateToString(Date date, String inFormat)
	{
		SimpleDateFormat formatter =  new SimpleDateFormat(inFormat);
		return formatter.format(date);
	}

	/**
	 * java.util.Date 타입을 java.util.Calendar 타입으로 변환한다
	 * @param date
	 * @return
	 */
	private static Calendar dateToCalendar(Date date)
	{
		Calendar cal = getNowCalendar();
		cal.setTime(date);
		return cal;
	}


	/**
	 * java.util.Calendar 타입을 java.util.Date 타입으로 변환한다
	 * @param cal
	 * @return
	 */
	public static Date calendarToDate(Calendar cal)
	{
		return cal.getTime();
	}

	/**
	 * java.lang.String 타입을 java.util.Calendar 타입으로 변환한다
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	public static Calendar stringToCalendar(String date) throws ParseException
	{
		return stringToCalendar(date,DFLT_IN_FORMAT);
	}
	/**
	 * java.lang.String 타입을 java.util.Calendar 타입으로 변환한다
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	public static Calendar stringToCalendar(String date, String inFormat) throws ParseException
	{
		Date transDate = stringToDate(date,inFormat);
		return dateToCalendar(transDate);
	}

	/**
	 * java.util.Calendar 타입을 java.lang.String 타입으로 변환한다
	 * @param cal
	 * @return
	 */
	public static String calendarToString(Calendar cal)
	{
		return calendarToString(cal,DFLT_IN_FORMAT);
	}

	/**
	 * java.util.Calendar 타입을 java.lang.String 타입으로 변환한다
	 * @param cal
	 * @return
	 */
	public static String calendarToString(Calendar cal, String inFormat)
	{
		return dateToString(cal.getTime(),inFormat);
	}

	/**
	 * 두개의 데이타 해당하는 문자열을 받아서 비교한후 나중 날짜를 문자열로 반환해 준다.
	 * @param firstData 비교할 첫번째 데이타
	 * @Param secondData 비교할 첫번째 데이타
	 * @return String 두 날짜 중 이후 날짜를 반환
	 */
	public static String getAfterDateWithCompare(String firstData, String secondData) throws ParseException
	{
		if(isExist(firstData) && isExist(secondData))
		{
			Calendar first = stringToCalendar(firstData);
			Calendar second = stringToCalendar(secondData);
			if(first.after(second))
			{
				return calendarToString(first);
			}
			else if(first.before(second))
			{
				return calendarToString(second);
			}
			return firstData;
		}
		else
		{
			throw new NonExistDateException(" wrong format or date: firstDataFormat : " +firstData +" seocondDataFormat : " + secondData);
		}
	}
	/**
	 * 두개의 데이타 해당하는 문자열을 받아서 비교한후 이전 날짜를 문자열로 반환해 준다.
	 * @param firstData 비교할 첫번째 데이타
	 * @Param secondData 비교할 첫번째 데이타
	 * @return String 두 날짜 중 이전 날짜를 반환
	 */
	public static String getBeforeDateWithCompare(String firstData, String secondData) throws ParseException
	{
		if(isExist(firstData) && isExist(secondData))
		{
			Calendar first = stringToCalendar(firstData);
			Calendar second = stringToCalendar(secondData);
			if(first.before(second))
			{
				return calendarToString(first);
			}
			else if(first.after(second))
			{
				return calendarToString(second);
			}
			return firstData;
		}
		else
		{
			throw new NonExistDateException(" wrong format or date: firstDataFormat : " +firstData +" seocondDataFormat : " + secondData);
		}
	}
	/**
	 * 두개의 데이타 해당하는 문자열을 받아서 비교한후 상수 값 형태로 반환한다.
	 * 0은 두 데이터가 동일, 1은 첫번째 데이터가 큰 경우, -1은 두번째 데이터가 큰 경우
	 * @param firstData 비교할 첫번째 데이타
	 * @Param secondData 비교할 첫번째 데이타
	 * @return int
	 */
	public static int compareBetweenDates(String firstData, String secondData) throws ParseException
	{
		if(isExist(firstData) && isExist(secondData))
		{
			Calendar first = stringToCalendar(firstData);
			Calendar second = stringToCalendar(secondData);
			if(first.before(second))
			{
				return DateUtil.BEFORE;
			}
			else if(first.after(second))
			{
				return DateUtil.AFTER;
			}
			return DateUtil.EQUAL;
		}
		else
		{
			throw new NonExistDateException(" wrong format or date: firstDataFormat : " +firstData +" seocondDataFormat : " + secondData);
		}
	}
	public static String getYear(String src)
	{
		String year = "";
		if(src!=null && src.length()>=8)
		{
			src = StringUtil.replace(src,"-","");
			year = src.substring(0,4);
		}
		return year;
	}
	public static String getMonth(String src)
	{
		String month = "";

		if (src != null && src.length() >= 8) {
			src = StringUtil.replace(src, "-", "");
			month = src.substring(4, 6);
		}
		return month;
	}

	public static String getDay(String src) {
		String day = "";

		if (src != null && src.length() >= 8) {
			src = StringUtil.replace(src, "-", "");
			day = src.substring(6);
		}
		return day;
	}

	public static String getDate(String src)
	{
		String date = "";
		if (src != null && src.length() >= 8) {
			src = StringUtil.replace(src, "-", "");
			date = src.substring(0,8);
		}
		return date;
	}

	public static String addOneDay(){
		Calendar now = Calendar.getInstance();
		now.add(Calendar.DATE,1);

		String dateFormat = "yyyyMMdd";
		Format df = new SimpleDateFormat(dateFormat);
		return df.format(now.getTime());
	}

	public static boolean checkOneDayBlock(String targetDate){

		if(StringUtil.isNull(targetDate)){
			return false;
		}

		int diff = 0;
		try {
			diff = getBetweenDates(targetDate, getCurrentDate());
		} catch (ParseException e) {
			e.printStackTrace();
		}

		if(diff <= 0){
			return true;	//하루동안 안보이게 함
		}else{
			return false;	//하루가 지났구나
		}

	}

	////일주일 1000 * 60 * 60 * 24 *7
	public static boolean checkTargetDateBlock(long blockDate){
		long oneDay = (long) ( 1000 * 60 * 60 * 24 );	//하루
		long targetDate = blockDate + oneDay;
		long diff = targetDate - getSystemCurrentTimeMillis();

		if(diff <= 0){
			return true;	//타켓날자보다 지났음. ex. 일주일이 지났네요
		}else{
			return false;	//타켓날짜보다 남았음. ex. 일주일이 지나지 않았네요
		}		
	}

	/**
	 * 타임스탬프를 초단위로 반환한다.
	 * @param date 타임스탬프 기준 일자, null이면 현시간으로 처리
	 * @return 초단위 타임스탬프
	 */
	public static long getTimeStamp(Date date)
	{
		long ret = 0;
		ret = getTimeStampMS(date) / 1000;
		return(ret);
	}
	/**
	 * 현시간의 타임스탬프를 초단위로 반환한다.
	 * @return 초단위 타임스탬프
	 */
	public static long getTimeStamp()
	{
		return(getTimeStamp(null));
	}
	/**
	 * 타임스탬프를 밀리초단위로 반환한다.
	 * @param date 타임스탬프 기준 일자, null이면 현시간으로 처리
	 * @return 밀리초단위 타임스탬프
	 */
	public static long getTimeStampMS(Date date)
	{
		long ret = 0;
		if(date == null) date = new Date();
		ret = date.getTime();
		return(ret);
	}

	public static int getCurrMonth(){
		Calendar now = Calendar.getInstance();
		int month = now.get(Calendar.MONTH) + 1;
		return month;
	}
	public static int getCurrYear(){
		Calendar now = Calendar.getInstance();
		int year = now.get(Calendar.YEAR);
		return year;
	}

	public static int getNextYear(){
		Calendar now = Calendar.getInstance();
		now.add(Calendar.YEAR, 1);
		int year = now.get(Calendar.YEAR);
		return year;
	}

	public static Date getRegDate() {
		Calendar cal = Calendar.getInstance();
		Date d = cal.getTime();
		return d;
	}

	/**
	 * "yyyyMMdd_HHmmssSSS"
	 * @param format
	 * @param time
	 * @return
	 */
	public static String curTimeString(String format, long time) {
		String s = null;
		SimpleDateFormat date = null;
		date = new SimpleDateFormat(format);
		s = date.format(new Date(time));
		return s;
	}
	
	private String setTimeTwoNumber(int num){
		if(num >= 10){
			return num + "";
		}else{
			return "0" + num;
		}
	}


	private void setDatePicker(Context context, TextView tvDate){

		Calendar calendar = Calendar.getInstance();

		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);

		DatePickerDialog datePickerDialog = new DatePickerDialog(context, android.R.style.Theme_Holo_Light_Dialog, new DatePickerDialog.OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
				long dateTime = view.getCalendarView().getDate();
				String sDateTime = DateUtil.curTimeString("yyyy-MM-dd", dateTime);
				tvDate.setText(sDateTime);
			}
		}, year, month, day);

		datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		datePickerDialog.show();
	}

	private void setTimePicker(Context context, TextView tvDate){

		int hourOfDay = DateUtil.getRegDate().getHours();
		int minute = DateUtil.getRegDate().getMinutes();

		//Theme_Holo_Light_Dialog
		//android.R.style.Theme_Material_Light_Dialog
		TimePickerDialog timePickerDialog = new TimePickerDialog(context, android.R.style.Theme_Holo_Light_Dialog, new TimePickerDialog.OnTimeSetListener() {
			@SuppressLint("SetTextI18n")
			@Override
			public void onTimeSet(TimePicker timePickerView, int hourOfDay, int minute) {
				Log.d("시간", "timePickerDialog >> onTimeSet() >> hourOfDay : " + hourOfDay);
				Log.d("시간", "timePickerDialog >> onTimeSet() >> minute : " + minute);
				tvDate.setText(setTimeTwoNumber(hourOfDay) + ":" + setTimeTwoNumber(minute));
			}
		}, hourOfDay, minute, false);
		timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		timePickerDialog.show();
	}

	/**
	 * setDateFormat(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH), ""));
	 * @param year
	 * @param monthOfYear
	 * @param dayOfMonth
	 * @param split (구분자)
	 * @return
	 */
	public static String setDateFormat(int year, int monthOfYear, int dayOfMonth, String split) {
		String strYear = year + "";
		String strMonthOfYear = monthOfYear + "";
		String strDayOfMonth = dayOfMonth + "";
		if (strMonthOfYear.length() == 1) {
			strMonthOfYear = "0" + strMonthOfYear;
		}
		if (strDayOfMonth.length() == 1) {
			strDayOfMonth = "0" + strDayOfMonth;
		}
		return strYear + split + strMonthOfYear + split + strDayOfMonth;
	}

	/**
	 * 문자열로된 일/시를 날짜형 데이터 Date로 반환한다.
	 * 입력 값에 따라서 날짜의 연산 수행 후 반환 하기도 한다.
	 * @param sTM 문자열로된 일시(1999-01-02 12:10:33)
	 * @param DTP 연산 기준 문자(y:년, m:월, d:일, h:시간, i:분, s:초)
	 * @param Diff 연산값 기준 문자열의 값에 가감을 할수 있다. 0이면 연산하지 않는다.
	 * @return 계산된 Date 값
	 */
	public static Date transStr2MS(String sTM, String DTP, int Diff)
	{
		Date r = new Date();

		if(!StringUtil.isNull(sTM))
		{
			int Y = 0, M = 0, D = 0, H = 0, I = 0, S = 0;

			Y = StringUtil.stringToInt(StringUtil.substr(sTM,0,4));
			M = StringUtil.stringToInt(StringUtil.substr(sTM,4,6));
			D = StringUtil.stringToInt(StringUtil.substr(sTM,6,8));
			if(sTM.length() >= 10) H = StringUtil.stringToInt(StringUtil.substr(sTM,8,10));
			if(sTM.length() >= 12) I = StringUtil.stringToInt(StringUtil.substr(sTM,10,12));
			if(sTM.length() >= 14) S = StringUtil.stringToInt(StringUtil.substr(sTM,12,14));

			Calendar cal = Calendar.getInstance();
			cal.set(Y, M-1, D, H, I, S);

			if(DTP.equals("y")) cal.add(Calendar.YEAR, Diff);
			if(DTP.equals("m")) cal.add(Calendar.MONTH, Diff);
			if(DTP.equals("d")) cal.add(Calendar.DATE, Diff);
			if(DTP.equals("h")) cal.add(Calendar.HOUR, Diff);
			if(DTP.equals("i")) cal.add(Calendar.MINUTE, Diff);
			if(DTP.equals("s")) cal.add(Calendar.SECOND, Diff);

			r = cal.getTime();
		}

		return(r);
	}

	/**
	 * 2023092->20230902로 8자리로 처리
	 * @param inputDateStr
	 * @return
	 */
	public static String displayDateYMD8(String inputDateStr){
		SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat outputDateFormat = new SimpleDateFormat("yyyyMMdd");

		try {
			Date date = inputDateFormat.parse(inputDateStr);
			String outputDateStr = outputDateFormat.format(date);
			return outputDateStr;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return inputDateStr;
	}
}
