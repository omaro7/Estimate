package kr.co.goms.app.estimate.model;

import android.os.Parcel;
import android.os.Parcelable;

public class EstimateBeanTB implements Parcelable {

    private String est_idx;					//견적idx
    private String com_idx;					//회사idx
    private String cli_idx;					//거래처idx
    private String est_date;				//견적일자
    private String est_effective_date;		//유효일자
    private String est_delivery_date;		//납기일자
    private String est_payment_condition;	//결제조건
    private String est_delivery_location;	//인도장소
    private String est_num;					//견적번호
    private String est_cli_name;			//거래처정보
    private String est_cli_tel;				//거래처 연락처
    private String est_cli_fax;				//거래처 팩스
    private String est_cli_zipcode;			//거래처 우편번호
    private String est_cli_address_01;		//거래처 주소 01
    private String est_cli_address_02;		//거래처 주소 02
    private String est_cli_manager_name;	//담당자 이름
    private String est_cli_remark;			//거래처 비고
    private String est_com_name;			//회사명
    private String est_com_ceo_name;		//회사대표자명
    private String est_com_biz_num;		//사업자번호
    private String est_com_email;			//이메일
    private String est_com_zipcode;		//회사우편번호
    private String est_com_address_01;		//주소01
    private String est_com_address_02;		//주소02
    private String est_tax_type;			//부가세 포함, 미포함
    private String est_total_price;			//총합계
    private String est_remark;				//견적서 비고
    private String est_regdate;				//견적서 비고

    public EstimateBeanTB() {
    }

    protected EstimateBeanTB(Parcel in) {
        est_idx = in.readString();
        com_idx = in.readString();
        cli_idx = in.readString();
        est_date = in.readString();
        est_effective_date = in.readString();
        est_delivery_date = in.readString();
        est_payment_condition = in.readString();
        est_delivery_location = in.readString();
        est_num = in.readString();
        est_cli_name = in.readString();
        est_cli_tel = in.readString();
        est_cli_fax = in.readString();
        est_cli_zipcode = in.readString();
        est_cli_address_01 = in.readString();
        est_cli_address_02 = in.readString();
        est_cli_manager_name = in.readString();
        est_cli_remark = in.readString();
        est_com_name = in.readString();
        est_com_ceo_name = in.readString();
        est_com_biz_num = in.readString();
        est_com_email = in.readString();
        est_com_zipcode = in.readString();
        est_com_address_01 = in.readString();
        est_com_address_02 = in.readString();
        est_tax_type = in.readString();
        est_total_price = in.readString();
        est_remark = in.readString();
        est_regdate = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(est_idx);
        dest.writeString(com_idx);
        dest.writeString(cli_idx);
        dest.writeString(est_date);
        dest.writeString(est_effective_date);
        dest.writeString(est_delivery_date);
        dest.writeString(est_payment_condition);
        dest.writeString(est_delivery_location);
        dest.writeString(est_num);
        dest.writeString(est_cli_name);
        dest.writeString(est_cli_tel);
        dest.writeString(est_cli_fax);
        dest.writeString(est_cli_zipcode);
        dest.writeString(est_cli_address_01);
        dest.writeString(est_cli_address_02);
        dest.writeString(est_cli_manager_name);
        dest.writeString(est_cli_remark);
        dest.writeString(est_com_name);
        dest.writeString(est_com_ceo_name);
        dest.writeString(est_com_biz_num);
        dest.writeString(est_com_email);
        dest.writeString(est_com_zipcode);
        dest.writeString(est_com_address_01);
        dest.writeString(est_com_address_02);
        dest.writeString(est_tax_type);
        dest.writeString(est_total_price);
        dest.writeString(est_remark);
        dest.writeString(est_regdate);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<EstimateBeanTB> CREATOR = new Creator<EstimateBeanTB>() {
        @Override
        public EstimateBeanTB createFromParcel(Parcel in) {
            return new EstimateBeanTB(in);
        }

        @Override
        public EstimateBeanTB[] newArray(int size) {
            return new EstimateBeanTB[size];
        }
    };

    public String getEst_idx() {
        return est_idx;
    }

    public void setEst_idx(String est_idx) {
        this.est_idx = est_idx;
    }

    public String getCom_idx() {
        return com_idx;
    }

    public void setCom_idx(String com_idx) {
        this.com_idx = com_idx;
    }

    public String getCli_idx() {
        return cli_idx;
    }

    public void setCli_idx(String cli_idx) {
        this.cli_idx = cli_idx;
    }

    public String getEst_date() {
        return est_date;
    }

    public void setEst_date(String est_date) {
        this.est_date = est_date;
    }

    public String getEst_effective_date() {
        return est_effective_date;
    }

    public void setEst_effective_date(String est_effective_date) {
        this.est_effective_date = est_effective_date;
    }

    public String getEst_delivery_date() {
        return est_delivery_date;
    }

    public void setEst_delivery_date(String est_delivery_date) {
        this.est_delivery_date = est_delivery_date;
    }

    public String getEst_payment_condition() {
        return est_payment_condition;
    }

    public void setEst_payment_condition(String est_payment_condition) {
        this.est_payment_condition = est_payment_condition;
    }

    public String getEst_delivery_location() {
        return est_delivery_location;
    }

    public void setEst_delivery_location(String est_delivery_location) {
        this.est_delivery_location = est_delivery_location;
    }

    public String getEst_num() {
        return est_num;
    }

    public void setEst_num(String est_num) {
        this.est_num = est_num;
    }

    public String getEst_cli_name() {
        return est_cli_name;
    }

    public void setEst_cli_name(String est_cli_name) {
        this.est_cli_name = est_cli_name;
    }

    public String getEst_cli_tel() {
        return est_cli_tel;
    }

    public void setEst_cli_tel(String est_cli_tel) {
        this.est_cli_tel = est_cli_tel;
    }

    public String getEst_cli_fax() {
        return est_cli_fax;
    }

    public void setEst_cli_fax(String est_cli_fax) {
        this.est_cli_fax = est_cli_fax;
    }

    public String getEst_cli_zipcode() {
        return est_cli_zipcode;
    }

    public void setEst_cli_zipcode(String est_cli_zipcode) {
        this.est_cli_zipcode = est_cli_zipcode;
    }

    public String getEst_cli_address_01() {
        return est_cli_address_01;
    }

    public void setEst_cli_address_01(String est_cli_address_01) {
        this.est_cli_address_01 = est_cli_address_01;
    }

    public String getEst_cli_address_02() {
        return est_cli_address_02;
    }

    public void setEst_cli_address_02(String est_cli_address_02) {
        this.est_cli_address_02 = est_cli_address_02;
    }

    public String getEst_cli_manager_name() {
        return est_cli_manager_name;
    }

    public void setEst_cli_manager_name(String est_cli_manager_name) {
        this.est_cli_manager_name = est_cli_manager_name;
    }

    public String getEst_cli_remark() {
        return est_cli_remark;
    }

    public void setEst_cli_remark(String est_cli_remark) {
        this.est_cli_remark = est_cli_remark;
    }

    public String getEst_com_name() {
        return est_com_name;
    }

    public void setEst_com_name(String est_com_name) {
        this.est_com_name = est_com_name;
    }

    public String getEst_com_ceo_name() {
        return est_com_ceo_name;
    }

    public void setEst_com_ceo_name(String est_com_ceo_name) {
        this.est_com_ceo_name = est_com_ceo_name;
    }

    public String getEst_com_biz_num() {
        return est_com_biz_num;
    }

    public void setEst_com_biz_num(String est_com_biz_num) {
        this.est_com_biz_num = est_com_biz_num;
    }

    public String getEst_com_email() {
        return est_com_email;
    }

    public void setEst_com_email(String est_com_email) {
        this.est_com_email = est_com_email;
    }

    public String getEst_com_zipcode() {
        return est_com_zipcode;
    }

    public void setEst_com_zipcode(String est_com_zipcode) {
        this.est_com_zipcode = est_com_zipcode;
    }

    public String getEst_com_address_01() {
        return est_com_address_01;
    }

    public void setEst_com_address_01(String est_com_address_01) {
        this.est_com_address_01 = est_com_address_01;
    }

    public String getEst_com_address_02() {
        return est_com_address_02;
    }

    public void setEst_com_address_02(String est_com_address_02) {
        this.est_com_address_02 = est_com_address_02;
    }

    public String getEst_tax_type() {
        return est_tax_type;
    }

    public void setEst_tax_type(String est_tax_type) {
        this.est_tax_type = est_tax_type;
    }

    public String getEst_total_price() {
        return est_total_price;
    }

    public void setEst_total_price(String est_total_price) {
        this.est_total_price = est_total_price;
    }

    public String getEst_remark() {
        return est_remark;
    }

    public void setEst_remark(String est_remark) {
        this.est_remark = est_remark;
    }

    public String getEst_regdate() {
        return est_regdate;
    }

    public void setEst_regdate(String est_regdate) {
        this.est_regdate = est_regdate;
    }
}
