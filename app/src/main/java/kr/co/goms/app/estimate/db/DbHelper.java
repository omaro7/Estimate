package kr.co.goms.app.estimate.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;

import kr.co.goms.app.estimate.AppConstant;
import kr.co.goms.app.estimate.model.ClientBeanTB;
import kr.co.goms.app.estimate.model.CompanyBeanTB;
import kr.co.goms.app.estimate.model.ItemBeanTB;
import kr.co.goms.module.common.util.DateUtil;
import kr.co.goms.module.common.util.StringUtil;

public class DbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;

    Context mContext;

    public SELECT_TYPE mSelectType = SELECT_TYPE.MAIN;
    public enum SELECT_TYPE{
        MAIN,
        FAVORITE,
        BLOCK,
        SITE,
    }

    public DbHelper(Context context) {
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
                EstimateDB.CompanyTable.COM_FAX_NUM , " VARCHAR, ",
                EstimateDB.CompanyTable.COM_HP_NUM , " VARCHAR, ",
                EstimateDB.CompanyTable.COM_EMAIL , " VARCHAR, ",
                EstimateDB.CompanyTable.COM_ZIPCODE , " VARCHAR, ",
                EstimateDB.CompanyTable.COM_ADDRESS_01 , " VARCHAR, ",
                EstimateDB.CompanyTable.COM_ADDRESS_02 , " VARCHAR, ",
                EstimateDB.CompanyTable.COM_ACCOUNT_NUM , " VARCHAR, ",
                EstimateDB.CompanyTable.COM_STAMP_PATH , " VARCHAR, ",
                EstimateDB.CompanyTable.COM_LOGO_PATH , " VARCHAR, ",
                EstimateDB.CompanyTable.COM_REGDATE , " VARCHAR "
                + ")"
        );
        try {
            pDb.execSQL(createCompanyTable);
        }catch(RuntimeException e){

        }

        String createItemTable = buildQuery(
                "CREATE TABLE",
                EstimateDB.ItemTable.ITEM_TABLE, "(",
                EstimateDB.ItemTable.ITEM_IDX, " INTEGER PRIMARY KEY AUTOINCREMENT, ",
                EstimateDB.ItemTable.ITEM_NAME, " VARCHAR, ",
                EstimateDB.ItemTable.ITEM_STD, " VARCHAR, ",
                EstimateDB.ItemTable.ITEM_UNIT, " VARCHAR, ",
                EstimateDB.ItemTable.ITEM_PRICE, " VARCHAR, ",
                EstimateDB.ItemTable.ITEM_REMARK, " VARCHAR "
                        + ")"
        );
        try {
            pDb.execSQL(createItemTable);
        }catch(RuntimeException e){

        }

        String createEstimateTable = buildQuery(
             "CREATE TABLE",
                EstimateDB.EstimateTable.ESTIMATE_TABLE, "(",
                EstimateDB.EstimateTable.EST_IDX	, " INTEGER PRIMARY KEY AUTOINCREMENT, ",				//견적IDX
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
                EstimateDB.EstimateTable.EST_CLI_ZIPCODE, " VARCHAR, ",			//거래처 우편번호
                EstimateDB.EstimateTable.EST_CLI_ADDRESS_01, " VARCHAR, ",		//거래처 주소 01
                EstimateDB.EstimateTable.EST_CLI_ADDRESS_02, " VARCHAR, ",		//거래처 주소 02
                EstimateDB.EstimateTable.EST_CLI_MANAGER_NAME, " VARCHAR, ",	//담당자 이름
                EstimateDB.EstimateTable.EST_CLI_REMARK, " VARCHAR, ",			//거래처 비고
                EstimateDB.EstimateTable.EST_COM_NAME, " VARCHAR, ",			//회사명
                EstimateDB.EstimateTable.EST_COM_CEO_NAME, " VARCHAR, ",		//회사대표자명
                EstimateDB.EstimateTable.EST_COM_BIZ_NUM, " VARCHAR, ",		    //사업자번호
                EstimateDB.EstimateTable.EST_COM_EMAIL, " VARCHAR, ",			//이메일
                EstimateDB.EstimateTable.EST_COM_ZIPCODE, " VARCHAR, ",		    //회사우편번호
                EstimateDB.EstimateTable.EST_COM_ADDRESS_01, " VARCHAR, ",		//주소01
                EstimateDB.EstimateTable.EST_COM_ADDRESS_02, " VARCHAR, ",		//주소02
                EstimateDB.EstimateTable.EST_TAX_TYPE, " VARCHAR, ",			//부가세 포함, 미포함
                EstimateDB.EstimateTable.EST_TOTAL_PRICE	, " VARCHAR, ",		//총합계
                EstimateDB.EstimateTable.EST_REMARK, " VARCHAR "				//견적서 비고
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
                EstimateDB.EstimateItemTable.EST_ITEM_QUANTITY, " INTEGER, ",			//견적ITEM수량
                EstimateDB.EstimateItemTable.EST_ITEM_UNIT_PRICE, " VARCHAR, ",			//견적ITEM단가
                EstimateDB.EstimateItemTable.EST_ITEM_PRICE, " VARCHAR, ",				//견적ITEM금액
                EstimateDB.EstimateItemTable.EST_ITEM_TAX_PRICE, " VARCHAR, ",			//견적ITEM세액
                EstimateDB.EstimateItemTable. EST_ITEM_TOTAL_PRICE, " VARCHAR, ",		//견적ITEM총금액
                EstimateDB.EstimateItemTable.EST_ITEM_REMARK, " VARCHAR, "				//견적ITEM비고
                        + ")"
        );
        try {
            pDb.execSQL(createEstimateItemTable);
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

    public void insertCompany(CompanyBeanTB companyBeanTB) {
        Log.d(AppConstant.LOG_TAG, "insertCompany() START ====================== ");

        SQLiteDatabase db = getReadableDatabase();
        
        final String currDate = DateUtil.getDateTime03();
        final ContentValues cv = new ContentValues();

        cv.put(EstimateDB.CompanyTable.USER_AD_ID, companyBeanTB.getUser_ad_id());
        cv.put(EstimateDB.CompanyTable.COM_NAME, companyBeanTB.getCom_ceo_name());
        cv.put(EstimateDB.CompanyTable.COM_BIZ_NUM, companyBeanTB.getCom_biz_num());
        cv.put(EstimateDB.CompanyTable.COM_CEO_NAME, companyBeanTB.getCom_ceo_name());
        cv.put(EstimateDB.CompanyTable.COM_UPTAE, companyBeanTB.getCom_uptae());
        cv.put(EstimateDB.CompanyTable.COM_UPJONG, companyBeanTB.getCom_upjong());
        cv.put(EstimateDB.CompanyTable.COM_MANAGER_NAME, companyBeanTB.getCom_manager_name());
        cv.put(EstimateDB.CompanyTable.COM_FAX_NUM, companyBeanTB.getCom_fax_num());
        cv.put(EstimateDB.CompanyTable.COM_HP_NUM, companyBeanTB.getCom_hp_num());
        cv.put(EstimateDB.CompanyTable.COM_EMAIL, companyBeanTB.getCom_email());
        cv.put(EstimateDB.CompanyTable.COM_ZIPCODE, companyBeanTB.getCom_zipcode());
        cv.put(EstimateDB.CompanyTable.COM_ADDRESS_01, companyBeanTB.getCom_address_01());
        cv.put(EstimateDB.CompanyTable.COM_ADDRESS_02, companyBeanTB.getCom_address_02());
        cv.put(EstimateDB.CompanyTable.COM_ACCOUNT_NUM, companyBeanTB.getCom_account_num());
        cv.put(EstimateDB.CompanyTable.COM_STAMP_PATH, companyBeanTB.getCom_stamp_path());
        cv.put(EstimateDB.CompanyTable.COM_LOGO_PATH, companyBeanTB.getCom_logo_path());
        cv.put(EstimateDB.CompanyTable.COM_REGDATE, currDate);

        db.insert(EstimateDB.CompanyTable.COMPANY_TABLE, null, cv);
        cv.clear();

        db.close();
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
                    bean.setCom_fax_num(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.CompanyTable.COM_FAX_NUM)));
                    bean.setCom_hp_num(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.CompanyTable.COM_HP_NUM)));
                    bean.setCom_email(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.CompanyTable.COM_EMAIL)));
                    bean.setCom_zipcode(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.CompanyTable.COM_ZIPCODE)));
                    bean.setCom_address_01(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.CompanyTable.COM_ADDRESS_01)));
                    bean.setCom_address_02(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.CompanyTable.COM_ADDRESS_02)));
                    bean.setCom_account_num(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.CompanyTable.COM_ACCOUNT_NUM)));
                    bean.setCom_stamp_path(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.CompanyTable.COM_STAMP_PATH)));
                    bean.setCom_logo_path(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.CompanyTable.COM_LOGO_PATH)));
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
     * 아이템 추가
     * @param itemBeanTB
     */
    public void insertItem(ItemBeanTB itemBeanTB) {
        Log.d(AppConstant.LOG_TAG, "insertCompany() START ====================== ");

        SQLiteDatabase db = getReadableDatabase();
        final ContentValues cv = new ContentValues();
        cv.put(EstimateDB.ItemTable.ITEM_NAME, itemBeanTB.getItem_name());
        cv.put(EstimateDB.ItemTable.ITEM_STD, itemBeanTB.getItem_std());
        cv.put(EstimateDB.ItemTable.ITEM_UNIT, itemBeanTB.getItem_unit());
        cv.put(EstimateDB.ItemTable.ITEM_PRICE, itemBeanTB.getItem_price());
        cv.put(EstimateDB.ItemTable.ITEM_REMARK, itemBeanTB.getItem_remark());

        db.insert(EstimateDB.ItemTable.ITEM_TABLE, null, cv);
        cv.clear();

        db.close();
    }

    /**
     * 거래처 입력하기
     * @param clientBeanTB
     */
    public void insertClient(ClientBeanTB clientBeanTB) {
        Log.d(AppConstant.LOG_TAG, "insertCompany() START ====================== ");

        SQLiteDatabase db = getReadableDatabase();

        final String currDate = DateUtil.getDateTime03();
        final ContentValues cv = new ContentValues();

        cv.put(EstimateDB.ClientTable.CLI_NAME, clientBeanTB.getCli_ceo_name());
        cv.put(EstimateDB.ClientTable.CLI_BIZ_NUM, clientBeanTB.getCli_biz_num());
        cv.put(EstimateDB.ClientTable.CLI_CEO_NAME, clientBeanTB.getCli_ceo_name());
        cv.put(EstimateDB.ClientTable.CLI_UPTAE, clientBeanTB.getCli_uptae());
        cv.put(EstimateDB.ClientTable.CLI_UPJONG, clientBeanTB.getCli_upjong());
        cv.put(EstimateDB.ClientTable.CLI_MANAGER_NAME, clientBeanTB.getCli_manager_name());
        cv.put(EstimateDB.ClientTable.CLI_FAX_NUM, clientBeanTB.getCli_fax_num());
        cv.put(EstimateDB.ClientTable.CLI_HP_NUM, clientBeanTB.getCli_hp_num());
        cv.put(EstimateDB.ClientTable.CLI_EMAIL, clientBeanTB.getCli_email());
        cv.put(EstimateDB.ClientTable.CLI_ZIPCODE, clientBeanTB.getCli_zipcode());
        cv.put(EstimateDB.ClientTable.CLI_ADDRESS_01, clientBeanTB.getCli_address_01());
        cv.put(EstimateDB.ClientTable.CLI_ADDRESS_02, clientBeanTB.getCli_address_02());
        cv.put(EstimateDB.ClientTable.CLI_REMARK, clientBeanTB.getCli_remark());
        cv.put(EstimateDB.ClientTable.CLI_REGDATE, currDate);

        db.insert(EstimateDB.ClientTable.CLIENT_TABLE, null, cv);
        cv.clear();

        db.close();
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
        }
        return clientList;
    }
}