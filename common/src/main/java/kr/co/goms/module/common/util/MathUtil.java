package kr.co.goms.module.common.util;

import java.util.Random;

import static java.lang.Math.pow;
import static java.lang.Math.round;

/**
 * Created by HJH on 2017-04-30.
 */

public class MathUtil {


    /**
     * 절상, 절하, 반올림 처리
     * @param strMode  - 수식
     * @param nCalcVal - 처리할 값(소수점 이하 데이터 포함)
     * @param nDigit   - 연산 기준 자릿수(오라클의 ROUND함수 자릿수 기준)
     *                   -2:십단위, -1:원단위, 0:소수점 1자리
     *                   1:소수점 2자리, 2:소수점 3자리, 3:소수점 4자리, 4:소수점 5자리 처리
     * @return int nCalcVal
     */
    public static int calcMath(String strMode, double nCalcVal, int nDigit) {
        if("CEIL".equals(strMode)) {  //절상
            if(nDigit < 0) {
                nDigit = -(nDigit);
                nCalcVal = Math.ceil(nCalcVal / pow(10, nDigit)) * pow(10, nDigit);
            } else {
                nCalcVal = Math.ceil(nCalcVal * pow(10, nDigit)) / pow(10, nDigit);
            }
        } else if("FLOOR".equals(strMode)) { //절하
            if(nDigit < 0) {
                nDigit = -(nDigit);
                nCalcVal = Math.floor(nCalcVal / pow(10, nDigit)) * pow(10, nDigit);
            } else {
                nCalcVal = Math.floor(nCalcVal * pow(10, nDigit)) / pow(10, nDigit);
            }
        } else {        //반올림
            if(nDigit < 0) {
                nDigit = -(nDigit);
                nCalcVal = round(nCalcVal / pow(10, nDigit)) * pow(10, nDigit);
            } else {
                nCalcVal = round(nCalcVal * pow(10, nDigit)) / pow(10, nDigit);
            }
        }
        return (int) nCalcVal;
    }

    public static  float range(final int percentage, final float start, final float end) {
        return (end - start) * percentage / 100.0f + start;
    }

    public static  int range(final int percentage, final int start, final int end) {
        return (end - start) * percentage / 100 + start;
    }

    public static int percentage(final float value, final float start, final float end){
        return (int) (100 * (value - end) / (end - start));
    }
    public static float percentage(final int value, final  int start, final int end){
        return 100 *  (value - end) / (end - start);
    }

    public static String randomToken(int len){
        String alphabet = new String("0123456789abcdefghijklmnopqrstuvwxyz"); //9
        int n = alphabet.length(); //10

        String result = new String();
        Random r = new Random(); //11

        for (int i=0; i<len; i++) //12
            result = result + alphabet.charAt(r.nextInt(n)); //13

        return result;
    }

    public static String generateToken(int len){
        String randomToken = randomToken(len);
        String token = randomToken.concat(DateUtil.getCurrentDateTime());
        return token;
    }

    public static int randInt(int min, int max) {
        Random rand = new Random();
        return rand.nextInt((max - min) + 1) + min;
    }

    public static int generateNumber(int length){
        String result = "";
        int random;
        while(true){
            random  = (int) ((Math.random() * (10 )));
            if(result.length() == 0 && random == 0){//when parsed this insures that the number doesn't start with 0
                random+=1;
                result+=random;
            }
            else if(!result.contains(Integer.toString(random))){//if my result doesn't contain the new generated digit then I add it to the result
                result+=Integer.toString(random);
            }
            if(result.length()>=length){//when i reach the number of digits desired i break out of the loop and return the final result
                break;
            }
        }
        return Integer.parseInt(result);
    }

}
