package kr.co.goms.app.estimate.db;

/**
 * Created by adphoto-jhhan on 2016-04-15.
 */
public class EstimateDB {

    /** Database name. */
    public static final String DB_NAME = "EstimateDB"; //DB Name

    public static final String DB_BACKUP_NAME = "EstimateDB_backup"; //DB Name

    /**
     * Hide the constructor, so that no one could create a new instance.
     */
    private EstimateDB() {

    }

    /**
     * 회원테이블입니다.
     */
    public static final class UserTable{
        public static final String USER_TABLE               = "UserTable";  //회원테이블
        public static final String USER_AD_ID     			= "user_ad_id"; //광고 AD_ID, Primary key
        public static final String USER_REGDATE             = "regdate";    //생성일
    }

    /**
     * 회사정보
     */
    public static final class CompanyTable {
        public static final String COMPANY_TABLE    = "CompanyTable";        //회사정보테이블
        public static final String COM_IDX          = "com_idx";          //회사 Primary key
        public static final String USER_AD_ID       = "user_ad_id";         //회원 광고 AD_ID
        public static final String COM_NAME    		= "com_name";       //회사명
        public static final String COM_BIZ_NUM      = "com_biz_num";    //사업자번호
        public static final String COM_CEO_NAME     = "com_ceo_name";   //대표명
        public static final String COM_UPTAE        = "com_uptae";      //업태
        public static final String COM_UPJONG       = "com_upjong";         //업종
        public static final String COM_MANAGER_NAME = "com_manager_name";   //매니저명
        public static final String COM_FAX_NUM		= "com_fax_num";        //팩스번호
        public static final String COM_HP_NUM       = "com_hp_num";         //핸드폰번호
        public static final String COM_EMAIL        = "com_email";          //이메일
        public static final String COM_ZIPCODE      = "com_zipcode";        //우편번호
        public static final String COM_ADDRESS_01   = "com_address_01";     //주소01
        public static final String COM_ADDRESS_02   = "com_address_02";     //주소02
        public static final String COM_ACCOUNT_NUM  = "com_account_num";    //계좌번호
        public static final String COM_STAMP_PATH   = "com_stamp_path";     //도장
        public static final String COM_LOGO_PATH    = "com_logo_path";      //로고
        public static final String COM_REGDATE    = "com_regdate";            //생성일
    }


    /**
     * 견적서 아이템들
     */
    public static final class ItemTable {
        public static final String ITEM_TABLE       = "ItemTable";          //견적서 아이템테이블
        public static final String ITEM_IDX         = "item_idx";           //아이템 Primary key
        public static final String ITEM_NAME        = "item_name";          //아이템 이름
        public static final String ITEM_STD         = "item_std";           //아이템 규격
        public static final String ITEM_UNIT    	= "item_unit";          //아이템 단위
        public static final String ITEM_PRICE    	= "item_price";         //아이템 판매가
        public static final String ITEM_REMARK      = "item_remark";        //아이템 비고
    }

    /**
     * 거래처 정보
     */
    public static final class ClientTable {
        public static final String CLIENT_TABLE    = "ClientTable";        //거래처 테이블
        public static final String CLI_IDX          = "cli_idx";          //회사 Primary key
        public static final String CLI_NAME    		= "cli_name";       //회사명
        public static final String CLI_BIZ_NUM      = "cli_biz_num";    //사업자번호
        public static final String CLI_CEO_NAME     = "cli_ceo_name";   //대표명
        public static final String CLI_UPTAE        = "cli_uptae";      //업태
        public static final String CLI_UPJONG       = "cli_upjong";         //업종
        public static final String CLI_MANAGER_NAME = "cli_manager_name";   //매니저명
        public static final String CLI_FAX_NUM		= "cli_fax_num";        //팩스번호
        public static final String CLI_HP_NUM       = "cli_hp_num";         //핸드폰번호
        public static final String CLI_EMAIL        = "cli_email";          //이메일
        public static final String CLI_ZIPCODE      = "cli_zipcode";        //우편번호
        public static final String CLI_ADDRESS_01   = "cli_address_01";     //주소01
        public static final String CLI_ADDRESS_02   = "cli_address_02";     //주소02
        public static final String CLI_REMARK       = "cli_remark";         //비고
        public static final String CLI_REGDATE    = "cli_regdate";          //생성일
    }
    /**
     * 견적서 정보
     */
    public static final class EstimateTable {
        public static final String ESTIMATE_TABLE       = "EstimateTable";          //견적서 테이블
        public static final String EST_IDX				="est_idx";					//견적idx
        public static final String COM_IDX				="com_idx";					//회사idx
        public static final String CLI_IDX				="cli_idx";					//거래처idx
        public static final String EST_DATE				="est_date";				//견적일자
        public static final String EST_EFFECTIVE_DATE		="est_effective_date";  //유효일자
        public static final String EST_DELIVERY_DATE		="est_delivery_date";   //납기일자
        public static final String EST_PAYMENT_CONDITION	="est_payment_condition";	//결제조건
        public static final String EST_DELIVERY_LOCATION	="est_delivery_location";	//인도장소
        public static final String EST_NUM				="est_num";					//견적번호
        public static final String EST_CLI_NAME			="est_cli_name";			//거래처정보
        public static final String EST_CLI_TEL			="est_cli_tel";				//거래처 연락처
        public static final String EST_CLI_FAX			="est_cli_fax";				//거래처 팩스
        public static final String EST_CLI_ZIPCODE		="est_cli_zipcode";			//거래처 우편번호
        public static final String EST_CLI_ADDRESS_01	="est_cli_address_01";		//거래처 주소 01
        public static final String EST_CLI_ADDRESS_02	="est_cli_address_02";		//거래처 주소 02
        public static final String EST_CLI_MANAGER_NAME	="est_cli_manager_name";	//담당자 이름
        public static final String EST_CLI_REMARK		="est_cli_remark";			//거래처 비고
        public static final String EST_COM_NAME			="est_com_name";			//회사명
        public static final String EST_COM_CEO_NAME		="est_com_ceo_name";		//회사대표자명
        public static final String EST_COM_BIZ_NUM		="est_com_biz_num";		    //사업자번호
        public static final String EST_COM_EMAIL		="est_com_email";			//이메일
        public static final String EST_COM_ZIPCODE		="est_com_zipcode";	        //회사우편번호
        public static final String EST_COM_ADDRESS_01	="est_com_address_01";		//주소01
        public static final String EST_COM_ADDRESS_02	="est_com_address_02";		//주소02
        public static final String EST_TAX_TYPE			="est_tax_type";			//부가세 포함, 미포함
        public static final String EST_TOTAL_PRICE		="est_total_price";			//총합계
        public static final String EST_REMARK			="est_remark";				//견적서 비고
    }

    /**
     * 견적서 아이템 정보
     */
    public static final class EstimateItemTable {
        public static final String ESTIMATE_ITEM_TABLE = "EstimateItemTable";          //견적서 아이템 테이블
        public static final String EST_ITEM_IDX ="est_item_idx";					    //견적아이템 idx
        public static final String EST_IDX ="est_idx";						            //견적idx
        public static final String EST_ITEM_NO ="est_item_no";					        //견적item No
        public static final String ITEM_IDX ="item_idx";					            //견적item Idx
        public static final String EST_ITEM_NAME	 ="est_item_name";				    //견적item 이름
        public static final String EST_ITEM_QUANTITY ="est_item_quantity";			    //견적item수량
        public static final String EST_ITEM_UNIT_PRICE ="est_item_unit_price";			//견적item단가
        public static final String EST_ITEM_PRICE ="est_item_price";				    //견적item금액
        public static final String EST_ITEM_TAX_PRICE	 ="est_item_tax_price";			//견적item세액
        public static final String EST_ITEM_TOTAL_PRICE ="est_item_total_price";        //견적item총금액
        public static final String EST_ITEM_REMARK ="est_item_remark";				    //견적item비고
    }
}
