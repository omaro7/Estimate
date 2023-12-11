package kr.co.goms.module.common.manager;

import android.util.Patterns;

import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import kr.co.goms.module.common.util.GomsLog;

/**
 * 사용법
 //PatternManager를 통한 해당 pattern으로 string 추출
 String result = PatternManager.I().getPatternData(originalData, targetData);
 String result = PatternManager.I().getPatternData(originalData, subData, targetData);
 */
public class PatternManager {

    static PatternManager mPatternManager;    //instance

    public PatternManager() {
    }

    public static PatternManager I(){
        if(mPatternManager == null){
            mPatternManager = new PatternManager();
        }
        return mPatternManager;
    }

    public void destroy() {

    }

    /**
     * @param originalData  //원문        "호랑이|고양이|원숭이|사자|고릴라";"
     * @param targetData    //찾을문구  "호랑이"
     * @return
     */
    public String getPatternData(String originalData, String targetData){

        Pattern pattern = Pattern.compile(targetData);
        Matcher matcher = pattern.matcher(originalData);

        String result = "";
        boolean isMatch = matcher.find();
        if (isMatch) {
            result = matcher.group();
        }
        return result;
    }

    /**
     * @param originalData  //원문                    "한*훈님 호랑이 이용금액 777,780원";"
     * @param subData       //원문중 중간그룹문구        "\\이용금액 ([0-9]+|[0-9]{1,3}(,[0-9]{3})*)(.[0-9]{1,})?원"
     * @param targetData    //찾을문구  "([0-9]+|[0-9]{1,3}(,[0-9]{3})*)(.[0-9]{1,})?원"
     * @return
     */
    public String getPatternData(String originalData, String subData, String targetData){

        Pattern pattern = Pattern.compile(subData);
        Matcher matcher = pattern.matcher(originalData);

        String result = "";
        boolean match2 = matcher.find();
        if (match2) {
            result = matcher.group();
            Pattern p = Pattern.compile(targetData);
            Matcher m = p.matcher(result);
            boolean isMatch = m.find();
            if(isMatch) {
                result = m.group();
            }
        }

        return result;
    }

    /**
     * 이메일 체크
     * @param email
     * @return
     */
    public boolean checkEmailPattern(String email) {
        boolean result = Patterns.EMAIL_ADDRESS.matcher(email).matches();
        GomsLog.d("ParserTest", "checkEmailPattern() emailAddress: " + email + ", result: " + result);
        return result;
    }

    /**
     * 전화번호 체크
     * @param phone
     * @return
     */
    public boolean checkPhonePattern(String phone) {
        boolean result = Patterns.PHONE.matcher(phone).matches();
        GomsLog.d("ParserTest", "checkPhonePattern() phone: " + phone + ", result: " + result);
        return result;
    }

    /**
     * 웹URL 체크
     * @param webUrl
     * @return
     */
    public boolean checkWebUrlPattern(String webUrl) {
        boolean result = Patterns.WEB_URL.matcher(webUrl).matches();
        GomsLog.d("ParserTest", "checkPhonePattern() webUrl: " + webUrl + ", result: " + result);
        return result;
    }

    /**
     * 영문여부 체크
     * @param engname
     * @return
     */
    public boolean checkIsEngNameFormat(String engname) {
        String trimstr = engname;
        trimstr = trimstr.replaceAll("\\p{Z}", "");
        String format = "^[a-zA-Z]*$";
        Pattern pattern = Pattern.compile(format);
        Matcher matcher = pattern.matcher(trimstr);
        boolean isNormal = matcher.matches();

        return isNormal;
    }

    /**
     * 1. 특수문자 있는지 체크
     * @param name
     * @return 있으면 true, 없으면 false
     */
    public boolean checkIsSpecialString(String name) {
        if(!name.matches("[0-9|a-z|A-Z|ㄱ-ㅎ|ㅏ-ㅣ|가-힝]*")) {
            return true;
        }else {
            return false;
        }
    }

    /**
     * 1. 공백 있는지 체크
     * @param name
     * @return 있으면 true, 없으면 false
     */
    public boolean checkIsSpace(String name) {
        for(int i=0; i <name.length(); i++) {
            if(name.charAt(i) == ' ') {
                return true;
            }
        }
        return false;
    }

    /**
     * 1. 첫번째 문자가 숫자인지 체크
     * @param name
     * @return 있으면 true, 없으면 false
     */
    public boolean checkIsFirstNum(String name) {
        if(name.charAt(0) < '0' || name.charAt(0) > '9') {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 문자열이 숫자인지 검사한다.
     * @param string 검사할 문자열
     * @return 숫자가 아니면 false를 리턴
     */
    public boolean checkDigitString(String string) {
        int strLen = string.length();
        for (int i = 0; i < strLen; i++) {
            char c = string.charAt(i);
            if (c != ' ' && !Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 문자열에 통화적용을 위해 컴마를 표기한다.
     * @param string 통화적용을 위한 문자열
     * @param ignoreZero 값이 0일 경우 공백을 리턴한다.
     * @return 통화적용이 된 문자열
     */
    public static String setComma(String string, boolean ignoreZero) {
        if (string.length() == 0) {
            return "";
        }
        try {
            if (string.contains(".")) {
                double value = Double.parseDouble(string);
                if (ignoreZero && value == 0) {
                    return "";
                }
                DecimalFormat format = new DecimalFormat("###,##0.00");
                return format.format(value);
            } else {
                long value = Long.parseLong(string);
                if (ignoreZero && value == 0) {
                    return "";
                }
                DecimalFormat format = new DecimalFormat("###,###");
                return format.format(value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return string;
    }

}
