package kr.co.goms.app.estimate.model;

import android.os.Parcel;
import android.os.Parcelable;

public class CompanyBeanTB implements Parcelable {
    private String com_idx;          //회사 Primary key
    private String user_ad_id;         //회원 광고 AD_ID
    private String com_name;       //회사명
    private String com_biz_num;    //사업자번호
    private String com_ceo_name;   //대표명
    private String com_uptae;      //업태
    private String com_upjong;         //업종
    private String com_manager_name;   //매니저명
    private String com_tel_num;        //전화번호
    private String com_fax_num;        //팩스번호
    private String com_hp_num;         //핸드폰번호
    private String com_email;          //이메일
    private String com_zipcode;        //우편번호
    private String com_address_01;     //주소01
    private String com_address_02;     //주소02
    private String com_account_num;    //계좌번호
    private String com_stamp_path;     //도장
    private String com_logo_path;      //로고
    private String com_main_yn;         //메인회사 여부
    private String com_regdate;            //생성일

    public CompanyBeanTB() {
    }


    protected CompanyBeanTB(Parcel in) {
        com_idx = in.readString();
        user_ad_id = in.readString();
        com_name = in.readString();
        com_biz_num = in.readString();
        com_ceo_name = in.readString();
        com_uptae = in.readString();
        com_upjong = in.readString();
        com_manager_name = in.readString();
        com_tel_num = in.readString();
        com_fax_num = in.readString();
        com_hp_num = in.readString();
        com_email = in.readString();
        com_zipcode = in.readString();
        com_address_01 = in.readString();
        com_address_02 = in.readString();
        com_account_num = in.readString();
        com_stamp_path = in.readString();
        com_logo_path = in.readString();
        com_main_yn = in.readString();
        com_regdate = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(com_idx);
        dest.writeString(user_ad_id);
        dest.writeString(com_name);
        dest.writeString(com_biz_num);
        dest.writeString(com_ceo_name);
        dest.writeString(com_uptae);
        dest.writeString(com_upjong);
        dest.writeString(com_manager_name);
        dest.writeString(com_tel_num);
        dest.writeString(com_fax_num);
        dest.writeString(com_hp_num);
        dest.writeString(com_email);
        dest.writeString(com_zipcode);
        dest.writeString(com_address_01);
        dest.writeString(com_address_02);
        dest.writeString(com_account_num);
        dest.writeString(com_stamp_path);
        dest.writeString(com_logo_path);
        dest.writeString(com_main_yn);
        dest.writeString(com_regdate);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CompanyBeanTB> CREATOR = new Creator<CompanyBeanTB>() {
        @Override
        public CompanyBeanTB createFromParcel(Parcel in) {
            return new CompanyBeanTB(in);
        }

        @Override
        public CompanyBeanTB[] newArray(int size) {
            return new CompanyBeanTB[size];
        }
    };

    public String getCom_idx() {
        return com_idx;
    }

    public void setCom_idx(String com_idx) {
        this.com_idx = com_idx;
    }

    public String getUser_ad_id() {
        return user_ad_id;
    }

    public void setUser_ad_id(String user_ad_id) {
        this.user_ad_id = user_ad_id;
    }

    public String getCom_name() {
        return com_name;
    }

    public void setCom_name(String com_name) {
        this.com_name = com_name;
    }

    public String getCom_biz_num() {
        return com_biz_num;
    }

    public void setCom_biz_num(String com_biz_num) {
        this.com_biz_num = com_biz_num;
    }

    public String getCom_ceo_name() {
        return com_ceo_name;
    }

    public void setCom_ceo_name(String com_ceo_name) {
        this.com_ceo_name = com_ceo_name;
    }

    public String getCom_uptae() {
        return com_uptae;
    }

    public void setCom_uptae(String com_uptae) {
        this.com_uptae = com_uptae;
    }

    public String getCom_upjong() {
        return com_upjong;
    }

    public void setCom_upjong(String com_upjong) {
        this.com_upjong = com_upjong;
    }

    public String getCom_manager_name() {
        return com_manager_name;
    }

    public void setCom_manager_name(String com_manager_name) {
        this.com_manager_name = com_manager_name;
    }

    public String getCom_tel_num() {
        return com_tel_num;
    }

    public void setCom_tel_num(String com_tel_num) {
        this.com_tel_num = com_tel_num;
    }

    public String getCom_fax_num() {
        return com_fax_num;
    }

    public void setCom_fax_num(String com_fax_num) {
        this.com_fax_num = com_fax_num;
    }

    public String getCom_hp_num() {
        return com_hp_num;
    }

    public void setCom_hp_num(String com_hp_num) {
        this.com_hp_num = com_hp_num;
    }

    public String getCom_email() {
        return com_email;
    }

    public void setCom_email(String com_email) {
        this.com_email = com_email;
    }

    public String getCom_zipcode() {
        return com_zipcode;
    }

    public void setCom_zipcode(String com_zipcode) {
        this.com_zipcode = com_zipcode;
    }

    public String getCom_address_01() {
        return com_address_01;
    }

    public void setCom_address_01(String com_address_01) {
        this.com_address_01 = com_address_01;
    }

    public String getCom_address_02() {
        return com_address_02;
    }

    public void setCom_address_02(String com_address_02) {
        this.com_address_02 = com_address_02;
    }

    public String getCom_account_num() {
        return com_account_num;
    }

    public void setCom_account_num(String com_account_num) {
        this.com_account_num = com_account_num;
    }

    public String getCom_stamp_path() {
        return com_stamp_path;
    }

    public void setCom_stamp_path(String com_stamp_path) {
        this.com_stamp_path = com_stamp_path;
    }

    public String getCom_logo_path() {
        return com_logo_path;
    }

    public void setCom_logo_path(String com_logo_path) {
        this.com_logo_path = com_logo_path;
    }

    public String getCom_main_yn() {
        return com_main_yn;
    }

    public void setCom_main_yn(String com_main_yn) {
        this.com_main_yn = com_main_yn;
    }

    public String getCom_regdate() {
        return com_regdate;
    }

    public void setCom_regdate(String com_regdate) {
        this.com_regdate = com_regdate;
    }
}
