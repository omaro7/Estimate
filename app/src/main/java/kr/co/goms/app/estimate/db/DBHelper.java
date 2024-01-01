package kr.co.goms.app.estimate.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import kr.co.goms.app.estimate.AppConstant;
import kr.co.goms.app.estimate.model.ClientBeanTB;
import kr.co.goms.app.estimate.model.CompanyBeanTB;
import kr.co.goms.app.estimate.model.EstimateBeanTB;
import kr.co.goms.app.estimate.model.ItemBeanTB;
import kr.co.goms.module.common.util.DateUtil;
import kr.co.goms.module.common.util.StringUtil;

public class DBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;

    Context mContext;

    public SELECT_TYPE mSelectType = SELECT_TYPE.MAIN;
    public enum SELECT_TYPE{
        MAIN,
        FAVORITE,
        BLOCK,
        SITE,
    }

    public DBHelper(Context context) {
        super(context, EstimateDB.DB_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase pDb) {
        Log.d(AppConstant.LOG_TAG, "onCreate");

        String createUserTable = buildQuery(
                "CREATE TABLE",
                EstimateDB.UserTable.USER_TABLE , "(",
                EstimateDB.UserTable.USER_AD_ID , " VARCHAR PRIMARY KEY, ",
                EstimateDB.UserTable.USER_REGDATE , " VARCHAR "
                        + ")"
        );
        try {
            pDb.execSQL(createUserTable);
        }catch(RuntimeException e){

        }

        String createCompanyTable = buildQuery(
                "CREATE TABLE",
                EstimateDB.CompanyTable.COMPANY_TABLE , "(",
                EstimateDB.CompanyTable.COM_IDX , " INTEGER PRIMARY KEY AUTOINCREMENT, ",
                EstimateDB.CompanyTable.USER_AD_ID , " VARCHAR, ",
                EstimateDB.CompanyTable.COM_NAME , " VARCHAR, ",
                EstimateDB.CompanyTable.COM_BIZ_NUM , " VARCHAR, ",
                EstimateDB.CompanyTable.COM_CEO_NAME , " VARCHAR, ",
                EstimateDB.CompanyTable.COM_UPTAE , " VARCHAR, ",
                EstimateDB.CompanyTable.COM_UPJONG , " VARCHAR, ",
                EstimateDB.CompanyTable.COM_MANAGER_NAME , " VARCHAR, ",
                EstimateDB.CompanyTable.COM_TEL_NUM , " VARCHAR, ",
                EstimateDB.CompanyTable.COM_FAX_NUM , " VARCHAR, ",
                EstimateDB.CompanyTable.COM_HP_NUM , " VARCHAR, ",
                EstimateDB.CompanyTable.COM_EMAIL , " VARCHAR, ",
                EstimateDB.CompanyTable.COM_ZIPCODE , " VARCHAR, ",
                EstimateDB.CompanyTable.COM_ADDRESS_01 , " VARCHAR, ",
                EstimateDB.CompanyTable.COM_ADDRESS_02 , " VARCHAR, ",
                EstimateDB.CompanyTable.COM_ACCOUNT_NUM , " VARCHAR, ",
                EstimateDB.CompanyTable.COM_STAMP_PATH , " VARCHAR, ",
                EstimateDB.CompanyTable.COM_LOGO_PATH , " VARCHAR, ",
                EstimateDB.CompanyTable.COM_MAIN_YN , " VARCHAR, ",
                EstimateDB.CompanyTable.COM_REGDATE , " VARCHAR "
                + ")"
        );
        try {
            pDb.execSQL(createCompanyTable);
        }catch(RuntimeException e){

        }

        String createClientTable = buildQuery(
                "CREATE TABLE",
                EstimateDB.ClientTable.CLIENT_TABLE , "(",
                EstimateDB.ClientTable.CLI_IDX , " INTEGER PRIMARY KEY AUTOINCREMENT, ",
                EstimateDB.ClientTable.CLI_NAME , " VARCHAR, ",
                EstimateDB.ClientTable.CLI_BIZ_NUM , " VARCHAR, ",
                EstimateDB.ClientTable.CLI_CEO_NAME , " VARCHAR, ",
                EstimateDB.ClientTable.CLI_UPTAE , " VARCHAR, ",
                EstimateDB.ClientTable.CLI_UPJONG , " VARCHAR, ",
                EstimateDB.ClientTable.CLI_MANAGER_NAME , " VARCHAR, ",
                EstimateDB.ClientTable.CLI_TEL_NUM , " VARCHAR, ",
                EstimateDB.ClientTable.CLI_FAX_NUM , " VARCHAR, ",
                EstimateDB.ClientTable.CLI_HP_NUM , " VARCHAR, ",
                EstimateDB.ClientTable.CLI_EMAIL , " VARCHAR, ",
                EstimateDB.ClientTable.CLI_ZIPCODE , " VARCHAR, ",
                EstimateDB.ClientTable.CLI_ADDRESS_01 , " VARCHAR, ",
                EstimateDB.ClientTable.CLI_ADDRESS_02 , " VARCHAR, ",
                EstimateDB.ClientTable.CLI_REMARK , " VARCHAR, ",
                EstimateDB.ClientTable.CLI_MAIN_YN , " VARCHAR, ",
                EstimateDB.ClientTable.CLI_REGDATE , " VARCHAR "
                        + ")"
        );
        try {
            pDb.execSQL(createClientTable);
        }catch(RuntimeException e){

        }

        String createItemTable = buildQuery(
                "CREATE TABLE",
                EstimateDB.ItemTable.ITEM_TABLE, "(",
                EstimateDB.ItemTable.ITEM_IDX, " INTEGER PRIMARY KEY AUTOINCREMENT, ",
                EstimateDB.ItemTable.ITEM_NAME, " VARCHAR, ",
                EstimateDB.ItemTable.ITEM_STD, " VARCHAR, ",
                EstimateDB.ItemTable.ITEM_UNIT, " VARCHAR, ",
                EstimateDB.ItemTable.ITEM_UNIT_PRICE, " VARCHAR, ",
                EstimateDB.ItemTable.ITEM_REMARK, " VARCHAR, ",
                EstimateDB.ItemTable.ITEM_REGDATE , " VARCHAR "
                        + ")"
        );
        try {
            pDb.execSQL(createItemTable);
        }catch(RuntimeException e){

        }

        String createEstimateTable = buildQuery(
             "CREATE TABLE",
                EstimateDB.EstimateTable.ESTIMATE_TABLE, "(",
                EstimateDB.EstimateTable.EST_IDX, " INTEGER PRIMARY KEY AUTOINCREMENT, ",				//견적IDX
                EstimateDB.EstimateTable.COM_IDX, " INTEGER, ",					//회사IDX
                EstimateDB.EstimateTable.CLI_IDX, " INTEGER, ",					//거래처IDX
                EstimateDB.EstimateTable.EST_DATE, " VARCHAR, ",				//견적일자
                EstimateDB.EstimateTable.EST_EFFECTIVE_DATE, " VARCHAR, ",		//유효일자
                EstimateDB.EstimateTable.EST_DELIVERY_DATE, " VARCHAR, ",		//납기일자
                EstimateDB.EstimateTable.EST_PAYMENT_CONDITION, " VARCHAR, ",	//결제조건
                EstimateDB.EstimateTable.EST_DELIVERY_LOCATION, " VARCHAR, ",	//인도장소
                EstimateDB.EstimateTable.EST_NUM, " VARCHAR, ",					//견적번호
                EstimateDB.EstimateTable.EST_CLI_NAME, " VARCHAR, ",			//거래처정보
                EstimateDB.EstimateTable.EST_CLI_TEL, " VARCHAR, ",				//거래처 연락처
                EstimateDB.EstimateTable.EST_CLI_FAX, " VARCHAR, ",				//거래처 팩스
                EstimateDB.EstimateTable.EST_CLI_HP, " VARCHAR, ",				//거래처 핸드폰
                EstimateDB.EstimateTable.EST_CLI_ZIPCODE, " VARCHAR, ",			//거래처 우편번호
                EstimateDB.EstimateTable.EST_CLI_ADDRESS_01, " VARCHAR, ",		//거래처 주소 01
                EstimateDB.EstimateTable.EST_CLI_ADDRESS_02, " VARCHAR, ",		//거래처 주소 02
                EstimateDB.EstimateTable.EST_CLI_MANAGER_NAME, " VARCHAR, ",	//담당자 이름
                EstimateDB.EstimateTable.EST_CLI_REMARK, " VARCHAR, ",			//거래처 비고
                EstimateDB.EstimateTable.EST_COM_NAME, " VARCHAR, ",			//공급자 회사명
                EstimateDB.EstimateTable.EST_COM_CEO_NAME, " VARCHAR, ",		//공급자 회사대표자명
                EstimateDB.EstimateTable.EST_COM_BIZ_NUM, " VARCHAR, ",		    //공급자 사업자번호
                EstimateDB.EstimateTable.EST_COM_UPTAE, " VARCHAR, ",		    //공급자 업태
                EstimateDB.EstimateTable.EST_COM_UPJONG, " VARCHAR, ",		    //공급자 업종
                EstimateDB.EstimateTable.EST_COM_MANAGER_NAME, " VARCHAR, ",    //공급자 담당자명
                EstimateDB.EstimateTable.EST_COM_EMAIL, " VARCHAR, ",			//공급자 이메일
                EstimateDB.EstimateTable.EST_COM_ZIPCODE, " VARCHAR, ",		    //공급자 회사우편번호
                EstimateDB.EstimateTable.EST_COM_ADDRESS_01, " VARCHAR, ",		//공급자 주소01
                EstimateDB.EstimateTable.EST_COM_ADDRESS_02, " VARCHAR, ",		//공급자 주소02
                EstimateDB.EstimateTable.EST_TAX_TYPE, " VARCHAR, ",			//부가세 포함, 미포함
                EstimateDB.EstimateTable.EST_TOTAL_PRICE	, " VARCHAR, ",		//총합계
                EstimateDB.EstimateTable.EST_REMARK, " VARCHAR, ",				//견적서 비고
                EstimateDB.EstimateTable.EST_REGDATE , " VARCHAR "
            + ")"
        );
        try {
            pDb.execSQL(createEstimateTable);
        }catch(RuntimeException e){

        }

        String createEstimateItemTable = buildQuery(
                "CREATE TABLE",
                EstimateDB.EstimateItemTable.ESTIMATE_ITEM_TABLE, "(",
                EstimateDB.EstimateItemTable.EST_ITEM_IDX, " INTEGER PRIMARY KEY AUTOINCREMENT, ",				//견적ITEM 키값
                EstimateDB.EstimateItemTable.EST_IDX, " INTEGER, ",					    //견적IDX
                EstimateDB.EstimateItemTable.ITEM_IDX, " INTEGER, ",					//견적ITEM IDX
                EstimateDB.EstimateItemTable.EST_ITEM_NO, " VARCHAR, ",					//견적ITEM NO
                EstimateDB.EstimateItemTable.EST_ITEM_NAME, " VARCHAR, ",				//견적ITEM 이름
                EstimateDB.EstimateItemTable.EST_ITEM_QUANTITY, " VARCHAR, ",			//견적ITEM수량
                EstimateDB.EstimateItemTable.EST_ITEM_STD, " VARCHAR, ",			    //견적ITEM규격
                EstimateDB.EstimateItemTable.EST_ITEM_UNIT, " VARCHAR, ",			    //견적ITEM단위
                EstimateDB.EstimateItemTable.EST_ITEM_UNIT_PRICE, " VARCHAR, ",			//견적ITEM단가
                EstimateDB.EstimateItemTable.EST_ITEM_PRICE, " VARCHAR, ",				//견적ITEM금액
                EstimateDB.EstimateItemTable.EST_ITEM_TAX_PRICE, " VARCHAR, ",			//견적ITEM세액
                EstimateDB.EstimateItemTable.EST_ITEM_TOTAL_PRICE, " VARCHAR, ",		//견적ITEM총금액
                EstimateDB.EstimateItemTable.EST_ITEM_REMARK, " VARCHAR "				//견적ITEM비고
                        + ")"
        );
        try {
            pDb.execSQL(createEstimateItemTable);
        }catch(RuntimeException e){

        }

        String createTempItemTable = buildQuery(
                "CREATE TABLE",
                EstimateDB.TempItemTable.TEMP_ITEM_TABLE, "(",
                EstimateDB.TempItemTable.TEMP_ITEM_IDX, " INTEGER PRIMARY KEY AUTOINCREMENT, ", //견적ITEM IDX
                EstimateDB.TempItemTable.TEMP_ITEM_TOKEN, " VARCHAR, ",					//견적ITEM 임시키값
                EstimateDB.TempItemTable.TEMP_ITEM_NO, " VARCHAR, ",					//견적ITEM NO
                EstimateDB.TempItemTable.TEMP_ITEM_NAME, " VARCHAR, ",				//견적ITEM 이름
                EstimateDB.TempItemTable.TEMP_ITEM_STD, " VARCHAR, ",			    //견적ITEM규격
                EstimateDB.TempItemTable.TEMP_ITEM_UNIT, " VARCHAR, ",			    //견적ITEM단위
                EstimateDB.TempItemTable.TEMP_ITEM_QUANTITY, " VARCHAR, ",			//견적ITEM수량
                EstimateDB.TempItemTable.TEMP_ITEM_UNIT_PRICE, " VARCHAR, ",        //견적ITEM단가금액
                EstimateDB.TempItemTable.TEMP_ITEM_PRICE, " VARCHAR, ",             //견적ITEM금액
                EstimateDB.TempItemTable.TEMP_ITEM_TAX_PRICE, " VARCHAR, ",         //견적ITEM세액금액
                EstimateDB.TempItemTable.TEMP_ITEM_TOTAL_PRICE, " VARCHAR, ",       //견적ITEM합계금액
                EstimateDB.TempItemTable.TEMP_ITEM_REMARK, " VARCHAR, ",            //견적ITEM비고
                EstimateDB.TempItemTable.TEMP_ITEM_REGDATE , " VARCHAR "            //견적ITEM생성일자
                        + ")"
        );
        try {
            pDb.execSQL(createTempItemTable);
        }catch(RuntimeException e){

        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(AppConstant.LOG_TAG, "onUpgrade");
        onCreate(db);
    }

    /**
     * Creates query.
     * @param pQueryWords
     *            part of the query.
     * @return query as String.
     */
    public static String buildQuery(final String... pQueryWords) {
        final StringBuilder sb = new StringBuilder();
        for (final String queryWord : pQueryWords) {
            if("'".equals(queryWord)){
                sb.append(queryWord);
            }else if("ORDER BY".equals(queryWord)){
                sb.append(" ").append(queryWord).append(" ");
            }else{
                sb.append(queryWord).append(" ");
            }
        }
        return sb.toString();
    }

    public boolean isTableExists(String tableName, final SQLiteDatabase pDb) {

        Cursor cursor = pDb.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '"+tableName+"'", null);
        if(cursor!=null) {
            if(cursor.getCount()>0) {
                cursor.close();
                return true;
            }
            cursor.close();
        }
        return false;
    }

    public ArrayList<CompanyBeanTB> insertCompany(CompanyBeanTB companyBeanTB) {
        Log.d(AppConstant.LOG_TAG, "insertCompany() START ====================== ");

        SQLiteDatabase db = getReadableDatabase();
        
        final String currDate = DateUtil.getDateTime03();
        final ContentValues cv = new ContentValues();

        cv.put(EstimateDB.CompanyTable.USER_AD_ID, companyBeanTB.getUser_ad_id());
        cv.put(EstimateDB.CompanyTable.COM_NAME, companyBeanTB.getCom_name());
        cv.put(EstimateDB.CompanyTable.COM_BIZ_NUM, companyBeanTB.getCom_biz_num());
        cv.put(EstimateDB.CompanyTable.COM_CEO_NAME, companyBeanTB.getCom_ceo_name());
        cv.put(EstimateDB.CompanyTable.COM_UPTAE, companyBeanTB.getCom_uptae());
        cv.put(EstimateDB.CompanyTable.COM_UPJONG, companyBeanTB.getCom_upjong());
        cv.put(EstimateDB.CompanyTable.COM_MANAGER_NAME, companyBeanTB.getCom_manager_name());
        cv.put(EstimateDB.CompanyTable.COM_TEL_NUM, companyBeanTB.getCom_tel_num());
        cv.put(EstimateDB.CompanyTable.COM_FAX_NUM, companyBeanTB.getCom_fax_num());
        cv.put(EstimateDB.CompanyTable.COM_HP_NUM, companyBeanTB.getCom_hp_num());
        cv.put(EstimateDB.CompanyTable.COM_EMAIL, companyBeanTB.getCom_email());
        cv.put(EstimateDB.CompanyTable.COM_ZIPCODE, companyBeanTB.getCom_zipcode());
        cv.put(EstimateDB.CompanyTable.COM_ADDRESS_01, companyBeanTB.getCom_address_01());
        cv.put(EstimateDB.CompanyTable.COM_ADDRESS_02, companyBeanTB.getCom_address_02());
        cv.put(EstimateDB.CompanyTable.COM_ACCOUNT_NUM, companyBeanTB.getCom_account_num());
        cv.put(EstimateDB.CompanyTable.COM_STAMP_PATH, companyBeanTB.getCom_stamp_path());
        cv.put(EstimateDB.CompanyTable.COM_LOGO_PATH, companyBeanTB.getCom_logo_path());
        cv.put(EstimateDB.CompanyTable.COM_MAIN_YN, companyBeanTB.getCom_main_yn());
        cv.put(EstimateDB.CompanyTable.COM_REGDATE, currDate);

        db.insert(EstimateDB.CompanyTable.COMPANY_TABLE, null, cv);
        cv.clear();

        db.close();
        return getCompanyListData();
    }

    public CompanyBeanTB getCompany(String comIdx) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + EstimateDB.CompanyTable.COMPANY_TABLE + " WHERE " + EstimateDB.CompanyTable.COM_IDX+ " = '" + comIdx + "' LIMIT 1", null);
        cursor.moveToFirst();
        try{
            CompanyBeanTB bean = new CompanyBeanTB();

            bean.setCom_idx(StringUtil.intToString(cursor.getInt(cursor.getColumnIndexOrThrow(EstimateDB.CompanyTable.COM_IDX))));
            bean.setCom_name(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.CompanyTable.COM_NAME)));
            bean.setCom_biz_num(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.CompanyTable.COM_BIZ_NUM)));
            bean.setCom_ceo_name(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.CompanyTable.COM_CEO_NAME)));
            bean.setCom_uptae(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.CompanyTable.COM_UPTAE)));
            bean.setCom_upjong(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.CompanyTable.COM_UPJONG)));
            bean.setCom_manager_name(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.CompanyTable.COM_MANAGER_NAME)));
            bean.setCom_tel_num(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.CompanyTable.COM_TEL_NUM)));
            bean.setCom_fax_num(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.CompanyTable.COM_FAX_NUM)));
            bean.setCom_hp_num(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.CompanyTable.COM_HP_NUM)));
            bean.setCom_email(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.CompanyTable.COM_EMAIL)));
            bean.setCom_zipcode(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.CompanyTable.COM_ZIPCODE)));
            bean.setCom_address_01(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.CompanyTable.COM_ADDRESS_01)));
            bean.setCom_address_02(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.CompanyTable.COM_ADDRESS_02)));
            bean.setCom_account_num(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.CompanyTable.COM_ACCOUNT_NUM)));
            bean.setCom_stamp_path(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.CompanyTable.COM_STAMP_PATH)));
            bean.setCom_logo_path(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.CompanyTable.COM_LOGO_PATH)));
            bean.setCom_main_yn(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.CompanyTable.COM_MAIN_YN)));
            bean.setCom_regdate(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.CompanyTable.COM_REGDATE)));

            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            return bean;
        }catch (NullPointerException | SQLiteException e){
            return null;
        } finally {
            if (!cursor.isClosed()) {
                cursor.close();
            }
        }
    }

    public ArrayList<CompanyBeanTB> getCompanyListData() {
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + EstimateDB.CompanyTable.COMPANY_TABLE + " ORDER BY " + EstimateDB.CompanyTable.COM_REGDATE + " DESC ", null);
        ArrayList<CompanyBeanTB> companyList = new ArrayList<CompanyBeanTB>();
        try{
            if (cursor.moveToFirst()) {
                do {
                    CompanyBeanTB bean = new CompanyBeanTB();

                    bean.setCom_idx(StringUtil.intToString(cursor.getInt(cursor.getColumnIndexOrThrow(EstimateDB.CompanyTable.COM_IDX))));
                    bean.setCom_name(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.CompanyTable.COM_NAME)));
                    bean.setCom_biz_num(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.CompanyTable.COM_BIZ_NUM)));
                    bean.setCom_ceo_name(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.CompanyTable.COM_CEO_NAME)));
                    bean.setCom_uptae(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.CompanyTable.COM_UPTAE)));
                    bean.setCom_upjong(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.CompanyTable.COM_UPJONG)));
                    bean.setCom_manager_name(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.CompanyTable.COM_MANAGER_NAME)));
                    bean.setCom_tel_num(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.CompanyTable.COM_TEL_NUM)));
                    bean.setCom_fax_num(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.CompanyTable.COM_FAX_NUM)));
                    bean.setCom_hp_num(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.CompanyTable.COM_HP_NUM)));
                    bean.setCom_email(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.CompanyTable.COM_EMAIL)));
                    bean.setCom_zipcode(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.CompanyTable.COM_ZIPCODE)));
                    bean.setCom_address_01(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.CompanyTable.COM_ADDRESS_01)));
                    bean.setCom_address_02(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.CompanyTable.COM_ADDRESS_02)));
                    bean.setCom_account_num(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.CompanyTable.COM_ACCOUNT_NUM)));
                    bean.setCom_stamp_path(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.CompanyTable.COM_STAMP_PATH)));
                    bean.setCom_logo_path(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.CompanyTable.COM_LOGO_PATH)));
                    bean.setCom_main_yn(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.CompanyTable.COM_MAIN_YN)));
                    bean.setCom_regdate(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.CompanyTable.COM_REGDATE)));
                    companyList.add(bean);

                } while (cursor.moveToNext());
            }
            if (!cursor.isClosed()) {
                cursor.close();
            }
        }catch (NullPointerException | SQLiteException e){
            return null;
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return companyList;
    }

    /**
     * 나의 회사 정보 업데이트하기
     * @param companyBeanTB
     * @return
     */
    public ArrayList<CompanyBeanTB> updateCompany(CompanyBeanTB companyBeanTB) {
        SQLiteDatabase db = getReadableDatabase();
        String whereClause = EstimateDB.CompanyTable.COM_IDX + "=?";
        String[] whereArgs = new String[]{companyBeanTB.getCom_idx()}; // 실제 삭제할 id 값을 입력

        final ContentValues cv = new ContentValues();

        cv.put(EstimateDB.CompanyTable.COM_NAME, companyBeanTB.getCom_name());
        cv.put(EstimateDB.CompanyTable.COM_BIZ_NUM, companyBeanTB.getCom_biz_num());
        cv.put(EstimateDB.CompanyTable.COM_CEO_NAME, companyBeanTB.getCom_ceo_name());
        cv.put(EstimateDB.CompanyTable.COM_UPTAE, companyBeanTB.getCom_uptae());
        cv.put(EstimateDB.CompanyTable.COM_UPJONG, companyBeanTB.getCom_upjong());
        cv.put(EstimateDB.CompanyTable.COM_MANAGER_NAME, companyBeanTB.getCom_manager_name());
        cv.put(EstimateDB.CompanyTable.COM_TEL_NUM, companyBeanTB.getCom_tel_num());
        cv.put(EstimateDB.CompanyTable.COM_FAX_NUM, companyBeanTB.getCom_fax_num());
        cv.put(EstimateDB.CompanyTable.COM_HP_NUM, companyBeanTB.getCom_hp_num());
        cv.put(EstimateDB.CompanyTable.COM_EMAIL, companyBeanTB.getCom_email());
        cv.put(EstimateDB.CompanyTable.COM_ZIPCODE, companyBeanTB.getCom_zipcode());
        cv.put(EstimateDB.CompanyTable.COM_ADDRESS_01, companyBeanTB.getCom_address_01());
        cv.put(EstimateDB.CompanyTable.COM_ADDRESS_02, companyBeanTB.getCom_address_02());
        cv.put(EstimateDB.CompanyTable.COM_ACCOUNT_NUM, companyBeanTB.getCom_account_num());
        cv.put(EstimateDB.CompanyTable.COM_STAMP_PATH, companyBeanTB.getCom_stamp_path());
        cv.put(EstimateDB.CompanyTable.COM_LOGO_PATH, companyBeanTB.getCom_logo_path());
        cv.put(EstimateDB.CompanyTable.COM_MAIN_YN, companyBeanTB.getCom_main_yn());

        //cv.put(EstimateDB.CompanyTable.USER_AD_ID, companyBeanTB.getUser_ad_id());
        //cv.put(EstimateDB.CompanyTable.COM_REGDATE, currDate);

        if("Y".equalsIgnoreCase(companyBeanTB.getCom_main_yn())){
            updateCompanyMainYN(db);
        }

        db.update(EstimateDB.CompanyTable.COMPANY_TABLE, cv, whereClause, whereArgs);
        cv.clear();
        db.close();

        return getCompanyListData();
    }

    /**
     * 메인 나의 회사 업데이트 하기
     * @param db
     */
    public void updateCompanyMainYN(SQLiteDatabase db){
        String whereClause = EstimateDB.CompanyTable.COM_MAIN_YN + "=?";
        String[] whereArgs = new String[]{"Y"}; // 이젠 MAIN_YN을 전부 N으로 변경

        final ContentValues cv = new ContentValues();
        cv.put(EstimateDB.CompanyTable.COM_MAIN_YN, "N");
        db.update(EstimateDB.CompanyTable.COMPANY_TABLE, cv, whereClause, whereArgs);
        cv.clear();
    }

    /**
     * 나의회사 정보 삭제하기
     * @param comIdx
     * @return
     */
    public ArrayList<CompanyBeanTB> deleteCompanyData(String comIdx) {
        SQLiteDatabase db = getReadableDatabase();
        String whereClause = EstimateDB.CompanyTable.COM_IDX + "=?";
        String[] whereArgs = new String[]{comIdx}; // 실제 삭제할 id 값을 입력
        db.delete(EstimateDB.CompanyTable.COMPANY_TABLE, whereClause, whereArgs);
        db.close();

        return getCompanyListData();
    }

    /**
     * 아이템 추가
     * @param itemBeanTB
     */
    public ArrayList<ItemBeanTB> insertItem(ItemBeanTB itemBeanTB) {
        Log.d(AppConstant.LOG_TAG, "insertCompany() START ====================== ");

        SQLiteDatabase db = getReadableDatabase();
        final String currDate = DateUtil.getDateTime03();
        final ContentValues cv = new ContentValues();

        cv.put(EstimateDB.ItemTable.ITEM_NAME, itemBeanTB.getItem_name());
        cv.put(EstimateDB.ItemTable.ITEM_STD, itemBeanTB.getItem_std());
        cv.put(EstimateDB.ItemTable.ITEM_UNIT, itemBeanTB.getItem_unit());
        cv.put(EstimateDB.ItemTable.ITEM_UNIT_PRICE, itemBeanTB.getItem_unit_price());
        cv.put(EstimateDB.ItemTable.ITEM_REMARK, itemBeanTB.getItem_remark());
        cv.put(EstimateDB.ItemTable.ITEM_REGDATE, currDate);

        db.insert(EstimateDB.ItemTable.ITEM_TABLE, null, cv);
        cv.clear();

        db.close();

        return getItemListData();
    }

    /**
     * 아이템 리스트 가져오기
     * @return
     */
    public ArrayList<ItemBeanTB> getItemListData() {
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + EstimateDB.ItemTable.ITEM_TABLE + " ORDER BY " + EstimateDB.ItemTable.ITEM_REGDATE + " DESC ", null);
        ArrayList<ItemBeanTB> itemList = new ArrayList<ItemBeanTB>();
        try{
            if (cursor.moveToFirst()) {
                do {
                    ItemBeanTB bean = new ItemBeanTB();

                    bean.setItem_idx(StringUtil.intToString(cursor.getInt(cursor.getColumnIndexOrThrow(EstimateDB.ItemTable.ITEM_IDX))));
                    bean.setItem_name(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.ItemTable.ITEM_NAME)));
                    bean.setItem_std(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.ItemTable.ITEM_STD)));
                    bean.setItem_unit(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.ItemTable.ITEM_UNIT)));
                    bean.setItem_unit_price(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.ItemTable.ITEM_UNIT_PRICE)));
                    bean.setItem_remark(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.ItemTable.ITEM_REMARK)));
                    bean.setItem_regdate(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.ItemTable.ITEM_REGDATE)));
                    itemList.add(bean);

                } while (cursor.moveToNext());
            }
            if (!cursor.isClosed()) {
                cursor.close();
            }
        }catch (NullPointerException | SQLiteException e){
            return null;
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            db.close();
        }
        return itemList;
    }

    /**
     * 아이템 삭제하기
     * @param itemIdx
     * @return
     */
    public ArrayList<ItemBeanTB> deleteItemData(String itemIdx) {
        SQLiteDatabase db = getReadableDatabase();
        String whereClause = EstimateDB.ItemTable.ITEM_IDX + "=?";
        String[] whereArgs = new String[]{itemIdx}; // 실제 삭제할 id 값을 입력
        db.delete(EstimateDB.ItemTable.ITEM_TABLE, whereClause, whereArgs);
        db.close();

        return getItemListData();
    }


    /**
     * 거래처 입력하기
     * @param clientBeanTB
     */
    public ArrayList<ClientBeanTB> insertClient(ClientBeanTB clientBeanTB) {
        Log.d(AppConstant.LOG_TAG, "insertCompany() START ====================== ");

        SQLiteDatabase db = getReadableDatabase();

        final String currDate = DateUtil.getDateTime03();
        final ContentValues cv = new ContentValues();

        cv.put(EstimateDB.ClientTable.CLI_NAME, clientBeanTB.getCli_name());
        cv.put(EstimateDB.ClientTable.CLI_BIZ_NUM, clientBeanTB.getCli_biz_num());
        cv.put(EstimateDB.ClientTable.CLI_CEO_NAME, clientBeanTB.getCli_ceo_name());
        cv.put(EstimateDB.ClientTable.CLI_UPTAE, clientBeanTB.getCli_uptae());
        cv.put(EstimateDB.ClientTable.CLI_UPJONG, clientBeanTB.getCli_upjong());
        cv.put(EstimateDB.ClientTable.CLI_MANAGER_NAME, clientBeanTB.getCli_manager_name());
        cv.put(EstimateDB.ClientTable.CLI_TEL_NUM, clientBeanTB.getCli_tel_num());
        cv.put(EstimateDB.ClientTable.CLI_FAX_NUM, clientBeanTB.getCli_fax_num());
        cv.put(EstimateDB.ClientTable.CLI_HP_NUM, clientBeanTB.getCli_hp_num());
        cv.put(EstimateDB.ClientTable.CLI_EMAIL, clientBeanTB.getCli_email());
        cv.put(EstimateDB.ClientTable.CLI_ZIPCODE, clientBeanTB.getCli_zipcode());
        cv.put(EstimateDB.ClientTable.CLI_ADDRESS_01, clientBeanTB.getCli_address_01());
        cv.put(EstimateDB.ClientTable.CLI_ADDRESS_02, clientBeanTB.getCli_address_02());
        cv.put(EstimateDB.ClientTable.CLI_REMARK, clientBeanTB.getCli_remark());
        cv.put(EstimateDB.ClientTable.CLI_MAIN_YN, clientBeanTB.getCli_main_yn());
        cv.put(EstimateDB.ClientTable.CLI_REGDATE, currDate);

        db.insert(EstimateDB.ClientTable.CLIENT_TABLE, null, cv);
        cv.clear();

        db.close();

        return getClientListData();
    }

    /**
     * 선택한 거래처 정보 가져오기
     * @param cliIdx
     * @return
     */
    public ClientBeanTB getClient(String cliIdx) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + EstimateDB.ClientTable.CLIENT_TABLE + " WHERE " + EstimateDB.ClientTable.CLI_IDX+ " = '" + cliIdx + "' LIMIT 1", null);
        cursor.moveToFirst();
        try{
            ClientBeanTB bean = new ClientBeanTB();

            bean.setCli_idx(StringUtil.intToString(cursor.getInt(cursor.getColumnIndexOrThrow(EstimateDB.ClientTable.CLI_IDX))));
            bean.setCli_name(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.ClientTable.CLI_NAME)));
            bean.setCli_biz_num(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.ClientTable.CLI_BIZ_NUM)));
            bean.setCli_ceo_name(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.ClientTable.CLI_CEO_NAME)));
            bean.setCli_uptae(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.ClientTable.CLI_UPTAE)));
            bean.setCli_upjong(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.ClientTable.CLI_UPJONG)));
            bean.setCli_manager_name(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.ClientTable.CLI_MANAGER_NAME)));
            bean.setCli_tel_num(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.ClientTable.CLI_TEL_NUM)));
            bean.setCli_fax_num(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.ClientTable.CLI_FAX_NUM)));
            bean.setCli_hp_num(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.ClientTable.CLI_HP_NUM)));
            bean.setCli_email(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.ClientTable.CLI_EMAIL)));
            bean.setCli_zipcode(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.ClientTable.CLI_ZIPCODE)));
            bean.setCli_address_01(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.ClientTable.CLI_ADDRESS_01)));
            bean.setCli_address_02(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.ClientTable.CLI_ADDRESS_02)));
            bean.setCli_main_yn(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.ClientTable.CLI_MAIN_YN)));
            bean.setCli_regdate(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.ClientTable.CLI_REGDATE)));

            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            return bean;
        }catch (NullPointerException | SQLiteException e){
            return null;
        } finally {
            if (!cursor.isClosed()) {
                cursor.close();
            }
        }
    }

    /**
     * 거래처 리스트 가져오기
     * @return
     */
    public ArrayList<ClientBeanTB> getClientListData() {
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + EstimateDB.ClientTable.CLIENT_TABLE + " ORDER BY " + EstimateDB.ClientTable.CLI_REGDATE + " DESC ", null);
        ArrayList<ClientBeanTB> clientList = new ArrayList<ClientBeanTB>();
        try{
            if (cursor.moveToFirst()) {
                do {
                    ClientBeanTB bean = new ClientBeanTB();

                    bean.setCli_idx(StringUtil.intToString(cursor.getInt(cursor.getColumnIndexOrThrow(EstimateDB.ClientTable.CLI_IDX))));
                    bean.setCli_name(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.ClientTable.CLI_NAME)));
                    bean.setCli_biz_num(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.ClientTable.CLI_BIZ_NUM)));
                    bean.setCli_ceo_name(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.ClientTable.CLI_CEO_NAME)));
                    bean.setCli_uptae(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.ClientTable.CLI_UPTAE)));
                    bean.setCli_upjong(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.ClientTable.CLI_UPJONG)));
                    bean.setCli_manager_name(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.ClientTable.CLI_MANAGER_NAME)));
                    bean.setCli_fax_num(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.ClientTable.CLI_FAX_NUM)));
                    bean.setCli_hp_num(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.ClientTable.CLI_HP_NUM)));
                    bean.setCli_email(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.ClientTable.CLI_EMAIL)));
                    bean.setCli_zipcode(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.ClientTable.CLI_ZIPCODE)));
                    bean.setCli_address_01(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.ClientTable.CLI_ADDRESS_01)));
                    bean.setCli_address_02(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.ClientTable.CLI_ADDRESS_02)));
                    bean.setCli_remark(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.ClientTable.CLI_REMARK)));
                    bean.setCli_main_yn(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.ClientTable.CLI_MAIN_YN)));
                    bean.setCli_regdate(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.ClientTable.CLI_REGDATE)));
                    clientList.add(bean);

                } while (cursor.moveToNext());
            }
            if (!cursor.isClosed()) {
                cursor.close();
            }
        }catch (NullPointerException | SQLiteException e){
            return null;
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            db.close();
        }
        return clientList;
    }

    /**
     * 거래처 정보 업데이트하기
     * @param clientBeanTB
     * @return
     */
    public ArrayList<ClientBeanTB> updateClient(ClientBeanTB clientBeanTB) {
        SQLiteDatabase db = getReadableDatabase();
        String whereClause = EstimateDB.ClientTable.CLI_IDX + "=?";
        String[] whereArgs = new String[]{clientBeanTB.getCli_idx()}; // 실제 삭제할 id 값을 입력

        final ContentValues cv = new ContentValues();

        cv.put(EstimateDB.ClientTable.CLI_NAME, clientBeanTB.getCli_name());
        cv.put(EstimateDB.ClientTable.CLI_BIZ_NUM, clientBeanTB.getCli_biz_num());
        cv.put(EstimateDB.ClientTable.CLI_CEO_NAME, clientBeanTB.getCli_ceo_name());
        cv.put(EstimateDB.ClientTable.CLI_UPTAE, clientBeanTB.getCli_uptae());
        cv.put(EstimateDB.ClientTable.CLI_UPJONG, clientBeanTB.getCli_upjong());
        cv.put(EstimateDB.ClientTable.CLI_MANAGER_NAME, clientBeanTB.getCli_manager_name());
        cv.put(EstimateDB.ClientTable.CLI_TEL_NUM, clientBeanTB.getCli_tel_num());
        cv.put(EstimateDB.ClientTable.CLI_FAX_NUM, clientBeanTB.getCli_fax_num());
        cv.put(EstimateDB.ClientTable.CLI_HP_NUM, clientBeanTB.getCli_hp_num());
        cv.put(EstimateDB.ClientTable.CLI_EMAIL, clientBeanTB.getCli_email());
        cv.put(EstimateDB.ClientTable.CLI_ZIPCODE, clientBeanTB.getCli_zipcode());
        cv.put(EstimateDB.ClientTable.CLI_ADDRESS_01, clientBeanTB.getCli_address_01());
        cv.put(EstimateDB.ClientTable.CLI_ADDRESS_02, clientBeanTB.getCli_address_02());
        cv.put(EstimateDB.ClientTable.CLI_REMARK, clientBeanTB.getCli_remark());
        cv.put(EstimateDB.ClientTable.CLI_MAIN_YN, clientBeanTB.getCli_main_yn());

        //cv.put(EstimateDB.ClientTable.CLI_REGDATE, currDate);

        if("Y".equalsIgnoreCase(clientBeanTB.getCli_main_yn())){
            updateClientMainYN(db);
        }

        db.update(EstimateDB.ClientTable.CLIENT_TABLE, cv, whereClause, whereArgs);
        cv.clear();
        db.close();

        return getClientListData();
    }

    /**
     * 메인거래처 업데이트 하기
     * @param db
     */
    public void updateClientMainYN(SQLiteDatabase db){
        String whereClause = EstimateDB.ClientTable.CLI_MAIN_YN + "=?";
        String[] whereArgs = new String[]{"Y"}; // 이젠 MAIN_YN을 전부 N으로 변경

        final ContentValues cv = new ContentValues();
        cv.put(EstimateDB.ClientTable.CLI_MAIN_YN, "N");
        db.update(EstimateDB.ClientTable.CLIENT_TABLE, cv, whereClause, whereArgs);
        cv.clear();
    }

    /**
     * 해당 거래처 삭제하기
     * @param clientIdx
     * @return
     */
    public ArrayList<ClientBeanTB> deleteClientData(String clientIdx) {
        SQLiteDatabase db = getReadableDatabase();
        String whereClause = EstimateDB.ClientTable.CLI_IDX + "=?";
        String[] whereArgs = new String[]{clientIdx}; // 실제 삭제할 id 값을 입력
        db.delete(EstimateDB.ClientTable.CLIENT_TABLE, whereClause, whereArgs);
        db.close();

        return getClientListData();
    }

    /**
     * 견적서 입력하기
     * @param estimateBeanTB
     * @param itemList
     */
    public ArrayList<EstimateBeanTB> insertEstimate(EstimateBeanTB estimateBeanTB, ArrayList<ItemBeanTB> itemList) {
        Log.d(AppConstant.LOG_TAG, "insertEstimate() START ====================== ");

        SQLiteDatabase db = getReadableDatabase();

        final String currDate = DateUtil.getDateTime03();
        final ContentValues cv = new ContentValues();

        //cv.put(EstimateDB.EstimateTable.EST_IDX, estimateBeanTB.getEst_idx());
        cv.put(EstimateDB.EstimateTable.COM_IDX, estimateBeanTB.getCom_idx());                              //회사키
        cv.put(EstimateDB.EstimateTable.CLI_IDX, estimateBeanTB.getCli_idx());                              //거래처키
        cv.put(EstimateDB.EstimateTable.EST_DATE, estimateBeanTB.getEst_date());                            //견적일자
        cv.put(EstimateDB.EstimateTable.EST_NUM, estimateBeanTB.getEst_num());                              //견적번호
        cv.put(EstimateDB.EstimateTable.EST_EFFECTIVE_DATE, estimateBeanTB.getEst_effective_date());        //유효일자
        cv.put(EstimateDB.EstimateTable.EST_DELIVERY_DATE, estimateBeanTB.getEst_delivery_date());          //납기일자
        cv.put(EstimateDB.EstimateTable.EST_PAYMENT_CONDITION, estimateBeanTB.getEst_payment_condition());  //결제조건
        cv.put(EstimateDB.EstimateTable.EST_DELIVERY_LOCATION, estimateBeanTB.getEst_delivery_location());  //인도장소
        cv.put(EstimateDB.EstimateTable.EST_CLI_NAME, estimateBeanTB.getEst_cli_name());
        cv.put(EstimateDB.EstimateTable.EST_CLI_MANAGER_NAME, estimateBeanTB.getEst_cli_manager_name());
        cv.put(EstimateDB.EstimateTable.EST_CLI_TEL, estimateBeanTB.getEst_cli_tel());
        cv.put(EstimateDB.EstimateTable.EST_CLI_FAX, estimateBeanTB.getEst_cli_fax());
        cv.put(EstimateDB.EstimateTable.EST_CLI_HP, estimateBeanTB.getEst_cli_hp());
        cv.put(EstimateDB.EstimateTable.EST_COM_EMAIL, estimateBeanTB.getEst_com_email());                  //회사이메일
        cv.put(EstimateDB.EstimateTable.EST_TAX_TYPE, estimateBeanTB.getEst_tax_type());                    //부가세여부
        cv.put(EstimateDB.EstimateTable.EST_TOTAL_PRICE, estimateBeanTB.getEst_total_price());              //총금액
        cv.put(EstimateDB.EstimateTable.EST_REMARK, estimateBeanTB.getEst_remark());                        //비고

        cv.put(EstimateDB.EstimateTable.EST_CLI_ZIPCODE, estimateBeanTB.getEst_cli_zipcode());
        cv.put(EstimateDB.EstimateTable.EST_CLI_ADDRESS_01, estimateBeanTB.getEst_cli_address_01());
        cv.put(EstimateDB.EstimateTable.EST_CLI_ADDRESS_02, estimateBeanTB.getEst_cli_address_02());
        cv.put(EstimateDB.EstimateTable.EST_CLI_REMARK, estimateBeanTB.getEst_cli_remark());

        cv.put(EstimateDB.EstimateTable.EST_COM_NAME, estimateBeanTB.getEst_com_name());
        cv.put(EstimateDB.EstimateTable.EST_COM_CEO_NAME, estimateBeanTB.getEst_com_ceo_name());
        cv.put(EstimateDB.EstimateTable.EST_COM_BIZ_NUM, estimateBeanTB.getEst_com_biz_num());
        cv.put(EstimateDB.EstimateTable.EST_COM_MANAGER_NAME, estimateBeanTB.getEst_com_manager_name());

        cv.put(EstimateDB.EstimateTable.EST_COM_ZIPCODE, estimateBeanTB.getEst_com_zipcode());
        cv.put(EstimateDB.EstimateTable.EST_COM_ADDRESS_01, estimateBeanTB.getEst_com_address_01());
        cv.put(EstimateDB.EstimateTable.EST_COM_ADDRESS_02, estimateBeanTB.getEst_com_address_02());

        cv.put(EstimateDB.EstimateTable.EST_REGDATE, currDate);

        long estIdx = db.insert(EstimateDB.EstimateTable.ESTIMATE_TABLE, null, cv);
        cv.clear();
        db.close();

        setEstimateItemData(StringUtil.longToString(estIdx),itemList);

        return getEstimateListData();
    }

    /**
     * 견적서  > 상품 리스트 저장하기
     * @param estIdx
     * @param itemList
     */
    public void setEstimateItemData(String estIdx, ArrayList<ItemBeanTB> itemList){
        int itemNo = 0;
        for(ItemBeanTB itemBeanTB : itemList) {
            itemBeanTB.setEst_idx(estIdx);
            itemBeanTB.setEst_item_no(StringUtil.intToString(itemNo));

            int quantity = StringUtil.stringToInt(itemBeanTB.getItem_quantity());
            int unitPrice = StringUtil.stringToInt(itemBeanTB.getItem_unit_price());
            int price = quantity * unitPrice;
            int taxPrice = (int)(price / 10);
            int iTotalPrice = price + taxPrice;

            itemBeanTB.setItem_price(StringUtil.intToString(price));            //금액
            itemBeanTB.setItem_tax_price(StringUtil.intToString(taxPrice));     //세액
            itemBeanTB.setItem_total_price(StringUtil.intToString(iTotalPrice));//총액

            insertEstimateItem(itemBeanTB);
            itemNo++;
        }
    }
    /**
     * 선택한 견적서 가져오기
     * @return
     */
    public EstimateBeanTB getEstimateData(String estIdx) {
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + EstimateDB.EstimateTable.ESTIMATE_TABLE + " WHERE " + EstimateDB.EstimateTable.EST_IDX + "=" + estIdx , null);
        cursor.moveToFirst();
        try{

            EstimateBeanTB bean = new EstimateBeanTB();

            bean.setEst_idx(StringUtil.intToString(cursor.getInt(cursor.getColumnIndexOrThrow(EstimateDB.EstimateTable.EST_IDX))));
            bean.setCom_idx(StringUtil.intToString(cursor.getInt(cursor.getColumnIndexOrThrow(EstimateDB.EstimateTable.COM_IDX))));
            bean.setCli_idx(StringUtil.intToString(cursor.getInt(cursor.getColumnIndexOrThrow(EstimateDB.EstimateTable.CLI_IDX))));
            bean.setEst_date(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.EstimateTable.EST_DATE)));
            bean.setEst_effective_date(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.EstimateTable.EST_EFFECTIVE_DATE)));
            bean.setEst_delivery_date(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.EstimateTable.EST_DELIVERY_DATE)));
            bean.setEst_payment_condition(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.EstimateTable.EST_PAYMENT_CONDITION)));
            bean.setEst_delivery_location(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.EstimateTable.EST_DELIVERY_LOCATION)));
            bean.setEst_num(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.EstimateTable.EST_NUM)));
            bean.setEst_cli_name(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.EstimateTable.EST_CLI_NAME)));
            bean.setEst_cli_tel(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.EstimateTable.EST_CLI_TEL)));
            bean.setEst_cli_fax(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.EstimateTable.EST_CLI_FAX)));
            bean.setEst_cli_hp(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.EstimateTable.EST_CLI_HP)));
            bean.setEst_cli_zipcode(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.EstimateTable.EST_CLI_ZIPCODE)));
            bean.setEst_cli_address_01(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.EstimateTable.EST_CLI_ADDRESS_01)));
            bean.setEst_cli_address_02(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.EstimateTable.EST_CLI_ADDRESS_02)));
            bean.setEst_cli_manager_name(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.EstimateTable.EST_CLI_MANAGER_NAME)));
            bean.setEst_cli_remark(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.EstimateTable.EST_CLI_REMARK)));
            bean.setEst_com_name(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.EstimateTable.EST_COM_NAME)));
            bean.setEst_com_ceo_name(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.EstimateTable.EST_COM_CEO_NAME)));
            bean.setEst_com_biz_num(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.EstimateTable.EST_COM_BIZ_NUM)));
            bean.setEst_com_uptae(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.EstimateTable.EST_COM_UPTAE)));
            bean.setEst_com_uptae(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.EstimateTable.EST_COM_UPJONG)));
            bean.setEst_com_manager_name(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.EstimateTable.EST_COM_MANAGER_NAME)));
            bean.setEst_com_email(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.EstimateTable.EST_COM_EMAIL)));
            bean.setEst_com_zipcode(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.EstimateTable.EST_COM_ZIPCODE)));
            bean.setEst_com_address_01(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.EstimateTable.EST_COM_ADDRESS_01)));
            bean.setEst_com_address_02(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.EstimateTable.EST_COM_ADDRESS_02)));
            bean.setEst_tax_type(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.EstimateTable.EST_TAX_TYPE)));
            bean.setEst_total_price(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.EstimateTable.EST_TOTAL_PRICE)));
            bean.setEst_remark(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.EstimateTable.EST_REMARK)));
            bean.setEst_regdate(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.EstimateTable.EST_REGDATE)));

            if (!cursor.isClosed()) {
                cursor.close();
            }

            return bean;
        }catch (NullPointerException | SQLiteException e){
            return null;
        } finally {
            if (!cursor.isClosed()) {
                cursor.close();
            }
            db.close();
        }
    }

    /**
     * 견적서 리스트
     * @return
     */
    public ArrayList<EstimateBeanTB> getEstimateListData() {
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + EstimateDB.EstimateTable.ESTIMATE_TABLE + " ORDER BY " + EstimateDB.EstimateTable.EST_REGDATE + " DESC ", null);
        ArrayList<EstimateBeanTB> estimateList = new ArrayList<EstimateBeanTB>();
        try{
            if (cursor.moveToFirst()) {
                do {
                    EstimateBeanTB bean = new EstimateBeanTB();

                    bean.setEst_idx(StringUtil.intToString(cursor.getInt(cursor.getColumnIndexOrThrow(EstimateDB.EstimateTable.EST_IDX))));
                    bean.setCom_idx(StringUtil.intToString(cursor.getInt(cursor.getColumnIndexOrThrow(EstimateDB.EstimateTable.COM_IDX))));
                    bean.setCli_idx(StringUtil.intToString(cursor.getInt(cursor.getColumnIndexOrThrow(EstimateDB.EstimateTable.CLI_IDX))));
                    bean.setEst_date(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.EstimateTable.EST_DATE)));
                    bean.setEst_effective_date(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.EstimateTable.EST_EFFECTIVE_DATE)));
                    bean.setEst_delivery_date(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.EstimateTable.EST_DELIVERY_DATE)));
                    bean.setEst_payment_condition(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.EstimateTable.EST_PAYMENT_CONDITION)));
                    bean.setEst_delivery_location(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.EstimateTable.EST_DELIVERY_LOCATION)));
                    bean.setEst_num(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.EstimateTable.EST_NUM)));
                    bean.setEst_cli_name(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.EstimateTable.EST_CLI_NAME)));
                    bean.setEst_cli_tel(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.EstimateTable.EST_CLI_TEL)));
                    bean.setEst_cli_fax(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.EstimateTable.EST_CLI_FAX)));
                    bean.setEst_cli_hp(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.EstimateTable.EST_CLI_HP)));
                    bean.setEst_cli_zipcode(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.EstimateTable.EST_CLI_ZIPCODE)));
                    bean.setEst_cli_address_01(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.EstimateTable.EST_CLI_ADDRESS_01)));
                    bean.setEst_cli_address_02(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.EstimateTable.EST_CLI_ADDRESS_02)));
                    bean.setEst_cli_manager_name(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.EstimateTable.EST_CLI_MANAGER_NAME)));
                    bean.setEst_cli_remark(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.EstimateTable.EST_CLI_REMARK)));
                    bean.setEst_com_name(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.EstimateTable.EST_COM_NAME)));
                    bean.setEst_com_ceo_name(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.EstimateTable.EST_COM_CEO_NAME)));
                    bean.setEst_com_biz_num(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.EstimateTable.EST_COM_BIZ_NUM)));
                    bean.setEst_com_manager_name(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.EstimateTable.EST_COM_MANAGER_NAME)));
                    bean.setEst_com_uptae(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.EstimateTable.EST_COM_UPTAE)));
                    bean.setEst_com_uptae(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.EstimateTable.EST_COM_UPJONG)));
                    bean.setEst_com_email(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.EstimateTable.EST_COM_EMAIL)));
                    bean.setEst_com_zipcode(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.EstimateTable.EST_COM_ZIPCODE)));
                    bean.setEst_com_address_01(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.EstimateTable.EST_COM_ADDRESS_01)));
                    bean.setEst_com_address_02(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.EstimateTable.EST_COM_ADDRESS_02)));
                    bean.setEst_tax_type(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.EstimateTable.EST_TAX_TYPE)));
                    bean.setEst_total_price(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.EstimateTable.EST_TOTAL_PRICE)));
                    bean.setEst_remark(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.EstimateTable.EST_REMARK)));
                    bean.setEst_regdate(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.EstimateTable.EST_REGDATE)));
                    estimateList.add(bean);

                } while (cursor.moveToNext());
            }
            if (!cursor.isClosed()) {
                cursor.close();
            }
        }catch (NullPointerException | SQLiteException e){
            return null;
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            db.close();
        }
        return estimateList;
    }

    /**
     * 거래처 업데이트하기
     * @param estimateBeanTB
     * @param itemList
     * @return
     */
    public ArrayList<EstimateBeanTB> updateEstimate(EstimateBeanTB estimateBeanTB, ArrayList<ItemBeanTB> itemList) {

        String estIdx = estimateBeanTB.getEst_idx();

        SQLiteDatabase db = getReadableDatabase();
        String whereClause = EstimateDB.EstimateTable.EST_IDX + "=?";
        String[] whereArgs = new String[]{estIdx}; // 실제 id 값을 입력

        final ContentValues cv = new ContentValues();

        cv.put(EstimateDB.EstimateTable.COM_IDX, estimateBeanTB.getCom_idx());                              //회사키
        cv.put(EstimateDB.EstimateTable.CLI_IDX, estimateBeanTB.getCli_idx());                              //거래처키
        cv.put(EstimateDB.EstimateTable.EST_DATE, estimateBeanTB.getEst_date());                            //견적일자
        //cv.put(EstimateDB.EstimateTable.EST_NUM, estimateBeanTB.getEst_num());                              //견적번호 > 수정 시, 견적번호는 그대로 유지
        cv.put(EstimateDB.EstimateTable.EST_EFFECTIVE_DATE, estimateBeanTB.getEst_effective_date());        //유효일자
        cv.put(EstimateDB.EstimateTable.EST_DELIVERY_DATE, estimateBeanTB.getEst_delivery_date());          //납기일자
        cv.put(EstimateDB.EstimateTable.EST_PAYMENT_CONDITION, estimateBeanTB.getEst_payment_condition());  //결제조건
        cv.put(EstimateDB.EstimateTable.EST_DELIVERY_LOCATION, estimateBeanTB.getEst_delivery_location());  //인도장소
        cv.put(EstimateDB.EstimateTable.EST_CLI_NAME, estimateBeanTB.getEst_cli_name());
        cv.put(EstimateDB.EstimateTable.EST_CLI_MANAGER_NAME, estimateBeanTB.getEst_cli_manager_name());
        cv.put(EstimateDB.EstimateTable.EST_CLI_TEL, estimateBeanTB.getEst_cli_tel());
        cv.put(EstimateDB.EstimateTable.EST_CLI_FAX, estimateBeanTB.getEst_cli_fax());
        cv.put(EstimateDB.EstimateTable.EST_CLI_HP, estimateBeanTB.getEst_cli_hp());
        cv.put(EstimateDB.EstimateTable.EST_COM_EMAIL, estimateBeanTB.getEst_com_email());                  //회사이메일
        cv.put(EstimateDB.EstimateTable.EST_TAX_TYPE, estimateBeanTB.getEst_tax_type());                    //부가세여부
        cv.put(EstimateDB.EstimateTable.EST_TOTAL_PRICE, estimateBeanTB.getEst_total_price());              //총금액
        cv.put(EstimateDB.EstimateTable.EST_REMARK, estimateBeanTB.getEst_remark());                        //비고

        cv.put(EstimateDB.EstimateTable.EST_CLI_ZIPCODE, estimateBeanTB.getEst_cli_zipcode());
        cv.put(EstimateDB.EstimateTable.EST_CLI_ADDRESS_01, estimateBeanTB.getEst_cli_address_01());
        cv.put(EstimateDB.EstimateTable.EST_CLI_ADDRESS_02, estimateBeanTB.getEst_cli_address_02());
        cv.put(EstimateDB.EstimateTable.EST_CLI_REMARK, estimateBeanTB.getEst_cli_remark());

        cv.put(EstimateDB.EstimateTable.EST_COM_NAME, estimateBeanTB.getEst_com_name());
        cv.put(EstimateDB.EstimateTable.EST_COM_CEO_NAME, estimateBeanTB.getEst_com_ceo_name());
        cv.put(EstimateDB.EstimateTable.EST_COM_BIZ_NUM, estimateBeanTB.getEst_com_biz_num());
        cv.put(EstimateDB.EstimateTable.EST_COM_UPTAE, estimateBeanTB.getEst_com_uptae());
        cv.put(EstimateDB.EstimateTable.EST_COM_UPJONG, estimateBeanTB.getEst_com_upjong());

        cv.put(EstimateDB.EstimateTable.EST_COM_ZIPCODE, estimateBeanTB.getEst_com_zipcode());
        cv.put(EstimateDB.EstimateTable.EST_COM_ADDRESS_01, estimateBeanTB.getEst_com_address_01());
        cv.put(EstimateDB.EstimateTable.EST_COM_ADDRESS_02, estimateBeanTB.getEst_com_address_02());

        //cv.put(EstimateDB.CompanyTable.USER_AD_ID, companyBeanTB.getUser_ad_id());
        //cv.put(EstimateDB.CompanyTable.COM_REGDATE, currDate);

        db.update(EstimateDB.EstimateTable.ESTIMATE_TABLE, cv, whereClause, whereArgs);
        cv.clear();
        db.close();

        //이전 상품 전체 삭제처리
        deleteEstimateItemData(estIdx);

        //이전 상품과 추가 등 변경된 상품리스트 저장하기
        setEstimateItemData(estIdx, itemList);

        return getEstimateListData();
    }

    /**
     * 해당 견적서의 삭제하기
     * @param estimateIdx
     * @return
     */
    public ArrayList<EstimateBeanTB> deleteEstimateData(String estimateIdx) {
        SQLiteDatabase db = getReadableDatabase();

        String whereClause = EstimateDB.EstimateTable.EST_IDX + "=?";
        String[] whereArgs = new String[]{estimateIdx}; // 실제 삭제할 id 값을 입력
        db.delete(EstimateDB.EstimateTable.ESTIMATE_TABLE, whereClause, whereArgs);

        String whereClauseItem = EstimateDB.EstimateItemTable.EST_IDX + "=?";
        String[] whereArgsItem = new String[]{estimateIdx}; // 실제 삭제할 id 값을 입력
        db.delete(EstimateDB.EstimateItemTable.ESTIMATE_ITEM_TABLE, whereClauseItem, whereArgsItem);

        db.close();

        return getEstimateListData();
    }

    /**
     * 견적서 > 아이템 입력하기
     * @param estimateItemBeanTB
     */
    public void insertEstimateItem(ItemBeanTB estimateItemBeanTB) {
        Log.d(AppConstant.LOG_TAG, "insertEstimateItem() START ====================== ");
        SQLiteDatabase db = getReadableDatabase();
        final ContentValues cv = new ContentValues();

        cv.put(EstimateDB.EstimateItemTable.EST_ITEM_IDX, estimateItemBeanTB.getItem_idx());
        cv.put(EstimateDB.EstimateItemTable.EST_IDX, estimateItemBeanTB.getEst_idx());
        cv.put(EstimateDB.EstimateItemTable.EST_ITEM_NO, estimateItemBeanTB.getEst_item_no());
        cv.put(EstimateDB.EstimateItemTable.ITEM_IDX, estimateItemBeanTB.getItem_idx());
        cv.put(EstimateDB.EstimateItemTable.EST_ITEM_NAME, estimateItemBeanTB.getItem_name());
        cv.put(EstimateDB.EstimateItemTable.EST_ITEM_QUANTITY, estimateItemBeanTB.getItem_quantity());
        cv.put(EstimateDB.EstimateItemTable.EST_ITEM_STD, estimateItemBeanTB.getItem_std());
        cv.put(EstimateDB.EstimateItemTable.EST_ITEM_UNIT, estimateItemBeanTB.getItem_unit());
        cv.put(EstimateDB.EstimateItemTable.EST_ITEM_UNIT_PRICE, estimateItemBeanTB.getItem_unit_price());
        cv.put(EstimateDB.EstimateItemTable.EST_ITEM_PRICE, estimateItemBeanTB.getItem_price());
        cv.put(EstimateDB.EstimateItemTable.EST_ITEM_TAX_PRICE, estimateItemBeanTB.getItem_tax_price());
        cv.put(EstimateDB.EstimateItemTable.EST_ITEM_TOTAL_PRICE, estimateItemBeanTB.getItem_total_price());
        cv.put(EstimateDB.EstimateItemTable.EST_ITEM_REMARK, estimateItemBeanTB.getItem_remark());
        db.insert(EstimateDB.EstimateItemTable.ESTIMATE_ITEM_TABLE, null, cv);
        cv.clear();

        db.close();
    }

    /**
     * 해당 견적서 > 상품리스트 가져오기
     * @param estIdx
     * @return
     */
    public ArrayList<ItemBeanTB> getEstimateItemListData(String estIdx) {
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + EstimateDB.EstimateItemTable.ESTIMATE_ITEM_TABLE + " WHERE " + EstimateDB.EstimateItemTable.EST_IDX + "=" + estIdx + " ORDER BY " + EstimateDB.EstimateItemTable.EST_ITEM_NO + " DESC ", null);
        ArrayList<ItemBeanTB> estimateItemList = new ArrayList<ItemBeanTB>();
        try{
            if (cursor.moveToFirst()) {
                do {
                    ItemBeanTB bean = new ItemBeanTB();

                    bean.setItem_idx(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.EstimateItemTable.EST_ITEM_IDX)));
                    bean.setEst_idx(cursor.getString(cursor.getColumnIndexOrThrow((EstimateDB.EstimateItemTable.EST_IDX))));
                    bean.setEst_item_no(cursor.getString(cursor.getColumnIndexOrThrow((EstimateDB.EstimateItemTable.EST_ITEM_NO))));
                    bean.setItem_idx(cursor.getString(cursor.getColumnIndexOrThrow((EstimateDB.EstimateItemTable.ITEM_IDX))));
                    bean.setItem_name(cursor.getString(cursor.getColumnIndexOrThrow((EstimateDB.EstimateItemTable.EST_ITEM_NAME))));
                    bean.setItem_quantity(cursor.getString(cursor.getColumnIndexOrThrow((EstimateDB.EstimateItemTable.EST_ITEM_QUANTITY))));
                    bean.setItem_unit(cursor.getString(cursor.getColumnIndexOrThrow((EstimateDB.EstimateItemTable.EST_ITEM_UNIT))));
                    bean.setItem_unit_price(cursor.getString(cursor.getColumnIndexOrThrow((EstimateDB.EstimateItemTable.EST_ITEM_UNIT_PRICE))));
                    bean.setItem_price(cursor.getString(cursor.getColumnIndexOrThrow((EstimateDB.EstimateItemTable.EST_ITEM_PRICE))));
                    bean.setItem_tax_price(cursor.getString(cursor.getColumnIndexOrThrow((EstimateDB.EstimateItemTable.EST_ITEM_TAX_PRICE))));
                    bean.setItem_total_price(cursor.getString(cursor.getColumnIndexOrThrow((EstimateDB.EstimateItemTable.EST_ITEM_TOTAL_PRICE))));
                    bean.setItem_remark(cursor.getString(cursor.getColumnIndexOrThrow((EstimateDB.EstimateItemTable.EST_ITEM_REMARK))));

                    estimateItemList.add(bean);

                } while (cursor.moveToNext());
            }
            if (!cursor.isClosed()) {
                cursor.close();
            }
        }catch (NullPointerException | SQLiteException e){
            return null;
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            db.close();
        }
        return estimateItemList;
    }

    /**
     * 해당 견적서 > 전체 아이템을 제거함.
     * @param estIdx
     */
    public void deleteEstimateItemData(String estIdx) {
        SQLiteDatabase db = getReadableDatabase();
        String whereClauseItem = EstimateDB.EstimateItemTable.EST_IDX + "=?";
        String[] whereArgsItem = new String[]{estIdx}; // 실제 삭제할 id 값을 입력
        db.delete(EstimateDB.EstimateItemTable.ESTIMATE_ITEM_TABLE, whereClauseItem, whereArgsItem);

        db.close();
    }


    /**
     * 해당 견적서 > 선택한 아이템을 제거함.
     * @param estIdx
     * @param estItemIdx
     */
    public ArrayList<ItemBeanTB>  deleteEstimateItemData(String estIdx, String estItemIdx) {
        SQLiteDatabase db = getReadableDatabase();

        String whereClauseItem = EstimateDB.EstimateItemTable.EST_IDX + "=? AND " +EstimateDB.EstimateItemTable.EST_ITEM_IDX + "=?";
        String[] whereArgsItem = new String[]{estIdx, estItemIdx}; // 실제 삭제할 id 값을 입력
        db.delete(EstimateDB.EstimateItemTable.ESTIMATE_ITEM_TABLE, whereClauseItem, whereArgsItem);

        db.close();

        return getEstimateItemListData(estIdx);
    }

    /**
     * 견적서 > 임시 아이템 입력하기
     * @param itemBeanTB
     */
    public ArrayList<ItemBeanTB> insertTempEstimateItem(ItemBeanTB itemBeanTB) {
        Log.d(AppConstant.LOG_TAG, "insertEstimateItem() START ====================== ");
        SQLiteDatabase db = getReadableDatabase();
        final ContentValues cv = new ContentValues();

        cv.put(EstimateDB.TempItemTable.TEMP_ITEM_TOKEN, itemBeanTB.getItem_token());
        cv.put(EstimateDB.TempItemTable.TEMP_ITEM_NAME, itemBeanTB.getItem_name());
        cv.put(EstimateDB.TempItemTable.TEMP_ITEM_STD, itemBeanTB.getItem_std());
        cv.put(EstimateDB.TempItemTable.TEMP_ITEM_UNIT, itemBeanTB.getItem_unit());
        cv.put(EstimateDB.TempItemTable.TEMP_ITEM_QUANTITY, itemBeanTB.getItem_quantity());
        cv.put(EstimateDB.TempItemTable.TEMP_ITEM_UNIT_PRICE, itemBeanTB.getItem_unit_price());
        cv.put(EstimateDB.TempItemTable.TEMP_ITEM_PRICE, itemBeanTB.getItem_price());
        cv.put(EstimateDB.TempItemTable.TEMP_ITEM_TAX_PRICE, itemBeanTB.getItem_tax_price());
        cv.put(EstimateDB.TempItemTable.TEMP_ITEM_TOTAL_PRICE, itemBeanTB.getItem_total_price());
        cv.put(EstimateDB.TempItemTable.TEMP_ITEM_REMARK, itemBeanTB.getItem_remark());
        db.insert(EstimateDB.TempItemTable.TEMP_ITEM_TABLE, null, cv);
        cv.clear();

        db.close();

        return getTempItemListData(itemBeanTB.getItem_token());
    }

    /**
     * 임시 토큰 > 해당 상품리스트 가져오기
     * @param token
     * @return
     */
    public ArrayList<ItemBeanTB> getTempItemListData(String token) {
        SQLiteDatabase db = getReadableDatabase();
        String[] whereArgs = new String[]{token}; // 실제 삭제할 id 값을 입력
        Cursor cursor = db.rawQuery("SELECT * FROM " + EstimateDB.TempItemTable.TEMP_ITEM_TABLE + " WHERE " + EstimateDB.TempItemTable.TEMP_ITEM_TOKEN + "=? ORDER BY " + EstimateDB.TempItemTable.TEMP_ITEM_REGDATE + " DESC ", whereArgs);
        ArrayList<ItemBeanTB> tempItemList = new ArrayList<ItemBeanTB>();
        try{
            if (cursor.moveToFirst()) {
                do {
                    ItemBeanTB bean = new ItemBeanTB();

                    bean.setItem_idx(StringUtil.intToString(cursor.getInt(cursor.getColumnIndexOrThrow(EstimateDB.TempItemTable.TEMP_ITEM_IDX))));
                    bean.setItem_token(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.TempItemTable.TEMP_ITEM_TOKEN)));
                    bean.setItem_name(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.TempItemTable.TEMP_ITEM_NAME)));
                    bean.setItem_std(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.TempItemTable.TEMP_ITEM_STD)));
                    bean.setItem_unit(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.TempItemTable.TEMP_ITEM_UNIT)));
                    bean.setItem_quantity(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.TempItemTable.TEMP_ITEM_QUANTITY)));
                    bean.setItem_unit_price(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.TempItemTable.TEMP_ITEM_UNIT_PRICE)));
                    bean.setItem_price(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.TempItemTable.TEMP_ITEM_PRICE)));
                    bean.setItem_tax_price(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.TempItemTable.TEMP_ITEM_TAX_PRICE)));
                    bean.setItem_total_price(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.TempItemTable.TEMP_ITEM_TOTAL_PRICE)));
                    bean.setItem_remark(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.TempItemTable.TEMP_ITEM_REMARK)));
                    bean.setItem_regdate(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.TempItemTable.TEMP_ITEM_REGDATE)));
                    tempItemList.add(bean);

                } while (cursor.moveToNext());
            }
            if (!cursor.isClosed()) {
                cursor.close();
            }
        }catch (NullPointerException | SQLiteException e){
            return null;
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            db.close();
        }
        return tempItemList;
    }

    /**
     * 임시저장 상품 리스트 삭제하기
     * @param itemIdx
     * @param itemToken
     * @return
     */
    public ArrayList<ItemBeanTB> deleteTempItemData(String itemIdx, String itemToken) {
        SQLiteDatabase db = getReadableDatabase();
        String whereClause = EstimateDB.TempItemTable.TEMP_ITEM_IDX + "=?";
        String[] whereArgs = new String[]{itemIdx}; // 실제 삭제할 id 값을 입력
        db.delete(EstimateDB.TempItemTable.TEMP_ITEM_TABLE, whereClause, whereArgs);
        db.close();

        return getTempItemListData(itemToken);
    }

    /**
     * 해당 테이블 삭제
     * @param table EstimateDB.EstimateItemTable.ESTIMATE_ITEM_TABLE
     */
    public void removeTableData(String table) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + table);
        db.close();
    }

    /**
     * EstimateDB.EstimateTable.ESTIMATE_TABLE
     * @param table
     */
    public void deleteTable(String table) {
        SQLiteDatabase db = this.getWritableDatabase();
        //db.execSQL("DROP TABLE IF EXISTS " + EstimateDB.EstimateItemTable.ESTIMATE_ITEM_TABLE);
        //db.execSQL("DROP TABLE IF EXISTS " + EstimateDB.TempItemTable.TEMP_ITEM_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + table);
        db.close();
    }

    public void createTable(){
        SQLiteDatabase db = this.getWritableDatabase();
        String createEstimateItemTable = buildQuery(
                "CREATE TABLE",
                EstimateDB.EstimateItemTable.ESTIMATE_ITEM_TABLE, "(",
                EstimateDB.EstimateItemTable.EST_ITEM_IDX, " INTEGER PRIMARY KEY AUTOINCREMENT, ",				//견적ITEM 키값
                EstimateDB.EstimateItemTable.EST_IDX, " INTEGER, ",					    //견적IDX
                EstimateDB.EstimateItemTable.ITEM_IDX, " INTEGER, ",					//견적ITEM IDX
                EstimateDB.EstimateItemTable.EST_ITEM_NO, " VARCHAR, ",					//견적ITEM NO
                EstimateDB.EstimateItemTable.EST_ITEM_NAME, " VARCHAR, ",				//견적ITEM 이름
                EstimateDB.EstimateItemTable.EST_ITEM_QUANTITY, " VARCHAR, ",			//견적ITEM수량
                EstimateDB.EstimateItemTable.EST_ITEM_STD, " VARCHAR, ",			    //견적ITEM규격
                EstimateDB.EstimateItemTable.EST_ITEM_UNIT, " VARCHAR, ",			    //견적ITEM단위
                EstimateDB.EstimateItemTable.EST_ITEM_UNIT_PRICE, " VARCHAR, ",			//견적ITEM단가
                EstimateDB.EstimateItemTable.EST_ITEM_PRICE, " VARCHAR, ",				//견적ITEM금액
                EstimateDB.EstimateItemTable.EST_ITEM_TAX_PRICE, " VARCHAR, ",			//견적ITEM세액
                EstimateDB.EstimateItemTable.EST_ITEM_TOTAL_PRICE, " VARCHAR, ",		//견적ITEM총금액
                EstimateDB.EstimateItemTable.EST_ITEM_REMARK, " VARCHAR "				//견적ITEM비고
                        + ")"
        );
        try {
            db.execSQL(createEstimateItemTable);
        }catch(RuntimeException e){

        }

        String createTempItemTable = buildQuery(
                "CREATE TABLE",
                EstimateDB.TempItemTable.TEMP_ITEM_TABLE, "(",
                EstimateDB.TempItemTable.TEMP_ITEM_IDX, " INTEGER PRIMARY KEY AUTOINCREMENT, ", //견적ITEM IDX
                EstimateDB.TempItemTable.TEMP_ITEM_TOKEN, " VARCHAR, ",					//견적ITEM 임시키값
                EstimateDB.TempItemTable.TEMP_ITEM_NO, " VARCHAR, ",					//견적ITEM NO
                EstimateDB.TempItemTable.TEMP_ITEM_NAME, " VARCHAR, ",				//견적ITEM 이름
                EstimateDB.TempItemTable.TEMP_ITEM_STD, " VARCHAR, ",			    //견적ITEM규격
                EstimateDB.TempItemTable.TEMP_ITEM_UNIT, " VARCHAR, ",			    //견적ITEM단위
                EstimateDB.TempItemTable.TEMP_ITEM_QUANTITY, " VARCHAR, ",			//견적ITEM수량
                EstimateDB.TempItemTable.TEMP_ITEM_UNIT_PRICE, " VARCHAR, ",        //견적ITEM단가금액
                EstimateDB.TempItemTable.TEMP_ITEM_PRICE, " VARCHAR, ",             //견적ITEM금액
                EstimateDB.TempItemTable.TEMP_ITEM_TAX_PRICE, " VARCHAR, ",         //견적ITEM세액금액
                EstimateDB.TempItemTable.TEMP_ITEM_TOTAL_PRICE, " VARCHAR, ",       //견적ITEM합계금액
                EstimateDB.TempItemTable.TEMP_ITEM_REMARK, " VARCHAR, ",            //견적ITEM비고
                EstimateDB.TempItemTable.TEMP_ITEM_REGDATE , " VARCHAR "            //견적ITEM생성일자
                        + ")"
        );
        try {
            db.execSQL(createTempItemTable);
        }catch(RuntimeException e){

        }finally {
            db.close();
        }
    }


    /**
     * 오늘자 견적번호 가져오기
     * @return
     */
    public String getEstimateNum() {
        SQLiteDatabase db = getReadableDatabase();
        String today = DateUtil.getCurrentDate();
        Cursor cursor = db.rawQuery("SELECT count(*) AS CNT FROM " + EstimateDB.EstimateTable.ESTIMATE_TABLE + " WHERE strftime('%Y%m%d', " + EstimateDB.EstimateTable.EST_REGDATE + ") = '" + today + "' LIMIT 1", null);
        cursor.moveToFirst();
        String estNum = "0001";
        try{
            String tmpCnt = String.valueOf(cursor.getColumnIndexOrThrow("CNT"));

            int currNum = StringUtil.stringToInt(tmpCnt);
            int createNum = currNum + 1;

            estNum = String.format("%04d", createNum);

            if (!cursor.isClosed()) {
                cursor.close();
            }
            return estNum;
        }catch (NullPointerException | SQLiteException e){
            return estNum;
        } finally {
            if (!cursor.isClosed()) {
                cursor.close();
            }
        }
    }
}