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
import kr.co.goms.app.estimate.model.EstimateItemBeanTB;
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
        cv.put(EstimateDB.CompanyTable.COM_REGDATE, currDate);

        db.insert(EstimateDB.CompanyTable.COMPANY_TABLE, null, cv);
        cv.clear();

        db.close();
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
    public ArrayList<ItemBeanTB> insertItem(ItemBeanTB itemBeanTB) {
        Log.d(AppConstant.LOG_TAG, "insertCompany() START ====================== ");

        SQLiteDatabase db = getReadableDatabase();
        final String currDate = DateUtil.getDateTime03();
        final ContentValues cv = new ContentValues();

        cv.put(EstimateDB.ItemTable.ITEM_NAME, itemBeanTB.getItem_name());
        cv.put(EstimateDB.ItemTable.ITEM_STD, itemBeanTB.getItem_std());
        cv.put(EstimateDB.ItemTable.ITEM_UNIT, itemBeanTB.getItem_unit());
        cv.put(EstimateDB.ItemTable.ITEM_PRICE, itemBeanTB.getItem_price());
        cv.put(EstimateDB.ItemTable.ITEM_REMARK, itemBeanTB.getItem_remark());
        cv.put(EstimateDB.ItemTable.ITEM_REGDATE, currDate);

        db.insert(EstimateDB.ItemTable.ITEM_TABLE, null, cv);
        cv.clear();

        db.close();

        return getItemListData();
    }


    public ArrayList<ItemBeanTB> getItemListData() {
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + EstimateDB.ItemTable.ITEM_TABLE + " ORDER BY " + EstimateDB.ItemTable.ITEM_REGDATE + " DESC ", null);
        ArrayList<ItemBeanTB> companyList = new ArrayList<ItemBeanTB>();
        try{
            if (cursor.moveToFirst()) {
                do {
                    ItemBeanTB bean = new ItemBeanTB();

                    bean.setItem_idx(StringUtil.intToString(cursor.getInt(cursor.getColumnIndexOrThrow(EstimateDB.ItemTable.ITEM_IDX))));
                    bean.setItem_name(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.ItemTable.ITEM_NAME)));
                    bean.setItem_std(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.ItemTable.ITEM_STD)));
                    bean.setItem_unit(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.ItemTable.ITEM_UNIT)));
                    bean.setItem_price(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.ItemTable.ITEM_PRICE)));
                    bean.setItem_remark(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.ItemTable.ITEM_REMARK)));
                    bean.setItem_regdate(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.ItemTable.ITEM_REGDATE)));
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
            db.close();
        }
        return companyList;
    }

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
            db.close();
        }
        return clientList;
    }

    public void deleteClientData(int clientIdx) {
        SQLiteDatabase db = getReadableDatabase();
        String whereClause = EstimateDB.ClientTable.CLI_IDX + "=?";
        String[] whereArgs = new String[]{Integer.toString(clientIdx)}; // 실제 삭제할 id 값을 입력
        db.delete(EstimateDB.ClientTable.CLIENT_TABLE, whereClause, whereArgs);
        db.close();
    }

    /**
     * 견적서 입력하기
     * @param estimateBeanTB
     */
    public void insertEstimate(EstimateBeanTB estimateBeanTB) {
        Log.d(AppConstant.LOG_TAG, "insertEstimate() START ====================== ");

        SQLiteDatabase db = getReadableDatabase();

        final String currDate = DateUtil.getDateTime03();
        final ContentValues cv = new ContentValues();

        cv.put(EstimateDB.EstimateTable.EST_IDX, estimateBeanTB.getEst_idx());
        cv.put(EstimateDB.EstimateTable.COM_IDX, estimateBeanTB.getCom_idx());
        cv.put(EstimateDB.EstimateTable.CLI_IDX, estimateBeanTB.getCli_idx());
        cv.put(EstimateDB.EstimateTable.EST_DATE, estimateBeanTB.getEst_idx());
        cv.put(EstimateDB.EstimateTable.EST_EFFECTIVE_DATE, estimateBeanTB.getEst_effective_date());
        cv.put(EstimateDB.EstimateTable.EST_DELIVERY_DATE, estimateBeanTB.getEst_delivery_date());
        cv.put(EstimateDB.EstimateTable.EST_PAYMENT_CONDITION, estimateBeanTB.getEst_payment_condition());
        cv.put(EstimateDB.EstimateTable.EST_DELIVERY_LOCATION, estimateBeanTB.getEst_delivery_location());
        cv.put(EstimateDB.EstimateTable.EST_NUM, estimateBeanTB.getEst_num());
        cv.put(EstimateDB.EstimateTable.EST_CLI_NAME, estimateBeanTB.getEst_cli_name());
        cv.put(EstimateDB.EstimateTable.EST_CLI_TEL, estimateBeanTB.getEst_cli_tel());
        cv.put(EstimateDB.EstimateTable.EST_CLI_FAX, estimateBeanTB.getEst_cli_fax());
        cv.put(EstimateDB.EstimateTable.EST_CLI_ZIPCODE, estimateBeanTB.getEst_cli_zipcode());
        cv.put(EstimateDB.EstimateTable.EST_CLI_ADDRESS_01, estimateBeanTB.getEst_cli_address_01());
        cv.put(EstimateDB.EstimateTable.EST_CLI_ADDRESS_02, estimateBeanTB.getEst_cli_address_02());
        cv.put(EstimateDB.EstimateTable.EST_CLI_MANAGER_NAME, estimateBeanTB.getEst_cli_manager_name());
        cv.put(EstimateDB.EstimateTable.EST_CLI_REMARK, estimateBeanTB.getEst_cli_remark());
        cv.put(EstimateDB.EstimateTable.EST_COM_NAME, estimateBeanTB.getEst_com_name());
        cv.put(EstimateDB.EstimateTable.EST_COM_CEO_NAME, estimateBeanTB.getEst_com_ceo_name());
        cv.put(EstimateDB.EstimateTable.EST_COM_BIZ_NUM, estimateBeanTB.getEst_com_biz_num());
        cv.put(EstimateDB.EstimateTable.EST_COM_EMAIL, estimateBeanTB.getEst_com_email());
        cv.put(EstimateDB.EstimateTable.EST_COM_ZIPCODE, estimateBeanTB.getEst_com_zipcode());
        cv.put(EstimateDB.EstimateTable.EST_COM_ADDRESS_01, estimateBeanTB.getEst_com_address_01());
        cv.put(EstimateDB.EstimateTable.EST_COM_ADDRESS_02, estimateBeanTB.getEst_com_address_02());
        cv.put(EstimateDB.EstimateTable.EST_TAX_TYPE, estimateBeanTB.getEst_tax_type());
        cv.put(EstimateDB.EstimateTable.EST_TOTAL_PRICE, estimateBeanTB.getEst_total_price());
        cv.put(EstimateDB.EstimateTable.EST_REMARK, estimateBeanTB.getEst_remark());
        cv.put(EstimateDB.EstimateTable.EST_REGDATE, currDate);

        db.insert(EstimateDB.EstimateTable.ESTIMATE_TABLE, null, cv);
        cv.clear();

        db.close();
    }


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
                    bean.setEst_cli_zipcode(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.EstimateTable.EST_CLI_ZIPCODE)));
                    bean.setEst_cli_address_01(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.EstimateTable.EST_CLI_ADDRESS_01)));
                    bean.setEst_cli_address_02(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.EstimateTable.EST_CLI_ADDRESS_02)));
                    bean.setEst_cli_manager_name(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.EstimateTable.EST_CLI_MANAGER_NAME)));
                    bean.setEst_cli_remark(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.EstimateTable.EST_CLI_REMARK)));
                    bean.setEst_com_name(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.EstimateTable.EST_COM_NAME)));
                    bean.setEst_com_ceo_name(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.EstimateTable.EST_COM_CEO_NAME)));
                    bean.setEst_com_biz_num(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.EstimateTable.EST_COM_BIZ_NUM)));
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

    public void deleteEstimateData(int estimateIdx) {
        SQLiteDatabase db = getReadableDatabase();

        String whereClause = EstimateDB.EstimateTable.EST_IDX + "=?";
        String[] whereArgs = new String[]{Integer.toString(estimateIdx)}; // 실제 삭제할 id 값을 입력
        db.delete(EstimateDB.EstimateTable.ESTIMATE_TABLE, whereClause, whereArgs);

        String whereClauseItem = EstimateDB.EstimateItemTable.EST_IDX + "=?";
        String[] whereArgsItem = new String[]{Integer.toString(estimateIdx)}; // 실제 삭제할 id 값을 입력
        db.delete(EstimateDB.EstimateItemTable.ESTIMATE_ITEM_TABLE, whereClauseItem, whereArgsItem);

        db.close();
    }


    /**
     * 견적서 > 아이템 입력하기
     * @param estimateItemBeanTB
     */
    public void insertEstimateItem(EstimateItemBeanTB estimateItemBeanTB) {
        Log.d(AppConstant.LOG_TAG, "insertEstimateItem() START ====================== ");
        SQLiteDatabase db = getReadableDatabase();
        final ContentValues cv = new ContentValues();

        cv.put(EstimateDB.EstimateItemTable.EST_ITEM_IDX, estimateItemBeanTB.getEst_item_idx());
        cv.put(EstimateDB.EstimateItemTable.EST_IDX, estimateItemBeanTB.getEst_idx());
        cv.put(EstimateDB.EstimateItemTable.EST_ITEM_NO, estimateItemBeanTB.getEst_item_no());
        cv.put(EstimateDB.EstimateItemTable.ITEM_IDX, estimateItemBeanTB.getItem_idx());
        cv.put(EstimateDB.EstimateItemTable.EST_ITEM_NAME, estimateItemBeanTB.getEst_item_name());
        cv.put(EstimateDB.EstimateItemTable.EST_ITEM_QUANTITY, estimateItemBeanTB.getEst_item_quantity());
        cv.put(EstimateDB.EstimateItemTable.EST_ITEM_UNIT_PRICE, estimateItemBeanTB.getEst_item_unit_price());
        cv.put(EstimateDB.EstimateItemTable.EST_ITEM_PRICE, estimateItemBeanTB.getEst_item_price());
        cv.put(EstimateDB.EstimateItemTable.EST_ITEM_TAX_PRICE, estimateItemBeanTB.getEst_item_tax_price());
        cv.put(EstimateDB.EstimateItemTable.EST_ITEM_TOTAL_PRICE, estimateItemBeanTB.getEst_item_total_price());
        cv.put(EstimateDB.EstimateItemTable.EST_ITEM_REMARK, estimateItemBeanTB.getEst_item_remark());
        db.insert(EstimateDB.EstimateItemTable.ESTIMATE_ITEM_TABLE, null, cv);
        cv.clear();

        db.close();
    }


    public ArrayList<EstimateItemBeanTB> getEstimateItemListData(int estimateIdx) {
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + EstimateDB.EstimateItemTable.ESTIMATE_ITEM_TABLE + " WHERE " + EstimateDB.EstimateItemTable.EST_IDX + "=" + estimateIdx + " ORDER BY " + EstimateDB.EstimateItemTable.EST_ITEM_NO + " DESC ", null);
        ArrayList<EstimateItemBeanTB> estimateItemList = new ArrayList<EstimateItemBeanTB>();
        try{
            if (cursor.moveToFirst()) {
                do {
                    EstimateItemBeanTB bean = new EstimateItemBeanTB();

                    bean.setEst_item_idx(cursor.getString(cursor.getColumnIndexOrThrow(EstimateDB.EstimateItemTable.EST_ITEM_IDX)));
                    bean.setEst_idx(cursor.getString(cursor.getColumnIndexOrThrow((EstimateDB.EstimateItemTable.EST_IDX))));
                    bean.setEst_item_idx(cursor.getString(cursor.getColumnIndexOrThrow((EstimateDB.EstimateItemTable.EST_ITEM_NO))));
                    bean.setItem_idx(cursor.getString(cursor.getColumnIndexOrThrow((EstimateDB.EstimateItemTable.ITEM_IDX))));
                    bean.setEst_item_name(cursor.getString(cursor.getColumnIndexOrThrow((EstimateDB.EstimateItemTable.EST_ITEM_NAME))));
                    bean.setEst_item_quantity(cursor.getString(cursor.getColumnIndexOrThrow((EstimateDB.EstimateItemTable.EST_ITEM_QUANTITY))));
                    bean.setEst_item_unit_price(cursor.getString(cursor.getColumnIndexOrThrow((EstimateDB.EstimateItemTable.EST_ITEM_UNIT_PRICE))));
                    bean.setEst_item_price(cursor.getString(cursor.getColumnIndexOrThrow((EstimateDB.EstimateItemTable.EST_ITEM_PRICE))));
                    bean.setEst_item_tax_price(cursor.getString(cursor.getColumnIndexOrThrow((EstimateDB.EstimateItemTable.EST_ITEM_TAX_PRICE))));
                    bean.setEst_item_total_price(cursor.getString(cursor.getColumnIndexOrThrow((EstimateDB.EstimateItemTable.EST_ITEM_TOTAL_PRICE))));
                    bean.setEst_item_remark(cursor.getString(cursor.getColumnIndexOrThrow((EstimateDB.EstimateItemTable.EST_ITEM_REMARK))));

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
     * 해당 견적서 > 선택한 아이템을 제거함.
     * @param estimateItemIdx
     */
    public void deleteEstimateItemData(int estimateIdx, int estimateItemIdx) {
        SQLiteDatabase db = getReadableDatabase();

        String whereClauseItem = EstimateDB.EstimateItemTable.EST_IDX + "=?, " +EstimateDB.EstimateItemTable.EST_ITEM_IDX + "=?";
        String[] whereArgsItem = new String[]{Integer.toString(estimateIdx), Integer.toString(estimateItemIdx)}; // 실제 삭제할 id 값을 입력
        db.delete(EstimateDB.EstimateItemTable.ESTIMATE_ITEM_TABLE, whereClauseItem, whereArgsItem);

        db.close();
    }

}