package kr.co.goms.app.estimate.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ClientBeanTB implements Parcelable {
    private String cli_idx;          //회사 Primary key
    private String cli_name;       //거래처명
    private String cli_biz_num;    //사업자번호
    private String cli_ceo_name;   //대표명
    private String cli_uptae;      //업태
    private String cli_upjong;         //업종
    private String cli_manager_name;   //매니저명
    private String cli_fax_num;        //팩스번호
    private String cli_hp_num;         //핸드폰번호
    private String cli_email;          //이메일
    private String cli_zipcode;        //우편번호
    private String cli_address_01;     //주소01
    private String cli_address_02;     //주소02
    private String cli_remark;          //비고
    private String cli_regdate;            //생성일

    public ClientBeanTB() {
    }

    protected ClientBeanTB(Parcel in) {
        cli_idx = in.readString();
        cli_name = in.readString();
        cli_biz_num = in.readString();
        cli_ceo_name = in.readString();
        cli_uptae = in.readString();
        cli_upjong = in.readString();
        cli_manager_name = in.readString();
        cli_fax_num = in.readString();
        cli_hp_num = in.readString();
        cli_email = in.readString();
        cli_zipcode = in.readString();
        cli_address_01 = in.readString();
        cli_address_02 = in.readString();
        cli_remark = in.readString();
        cli_regdate = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(cli_idx);
        dest.writeString(cli_name);
        dest.writeString(cli_biz_num);
        dest.writeString(cli_ceo_name);
        dest.writeString(cli_uptae);
        dest.writeString(cli_upjong);
        dest.writeString(cli_manager_name);
        dest.writeString(cli_fax_num);
        dest.writeString(cli_hp_num);
        dest.writeString(cli_email);
        dest.writeString(cli_zipcode);
        dest.writeString(cli_address_01);
        dest.writeString(cli_address_02);
        dest.writeString(cli_remark);
        dest.writeString(cli_regdate);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ClientBeanTB> CREATOR = new Creator<ClientBeanTB>() {
        @Override
        public ClientBeanTB createFromParcel(Parcel in) {
            return new ClientBeanTB(in);
        }

        @Override
        public ClientBeanTB[] newArray(int size) {
            return new ClientBeanTB[size];
        }
    };

    public String getCli_idx() {
        return cli_idx;
    }

    public void setCli_idx(String cli_idx) {
        this.cli_idx = cli_idx;
    }

    public String getCli_name() {
        return cli_name;
    }

    public void setCli_name(String cli_name) {
        this.cli_name = cli_name;
    }

    public String getCli_biz_num() {
        return cli_biz_num;
    }

    public void setCli_biz_num(String cli_biz_num) {
        this.cli_biz_num = cli_biz_num;
    }

    public String getCli_ceo_name() {
        return cli_ceo_name;
    }

    public void setCli_ceo_name(String cli_ceo_name) {
        this.cli_ceo_name = cli_ceo_name;
    }

    public String getCli_uptae() {
        return cli_uptae;
    }

    public void setCli_uptae(String cli_uptae) {
        this.cli_uptae = cli_uptae;
    }

    public String getCli_upjong() {
        return cli_upjong;
    }

    public void setCli_upjong(String cli_upjong) {
        this.cli_upjong = cli_upjong;
    }

    public String getCli_manager_name() {
        return cli_manager_name;
    }

    public void setCli_manager_name(String cli_manager_name) {
        this.cli_manager_name = cli_manager_name;
    }

    public String getCli_fax_num() {
        return cli_fax_num;
    }

    public void setCli_fax_num(String cli_fax_num) {
        this.cli_fax_num = cli_fax_num;
    }

    public String getCli_hp_num() {
        return cli_hp_num;
    }

    public void setCli_hp_num(String cli_hp_num) {
        this.cli_hp_num = cli_hp_num;
    }

    public String getCli_email() {
        return cli_email;
    }

    public void setCli_email(String cli_email) {
        this.cli_email = cli_email;
    }

    public String getCli_zipcode() {
        return cli_zipcode;
    }

    public void setCli_zipcode(String cli_zipcode) {
        this.cli_zipcode = cli_zipcode;
    }

    public String getCli_address_01() {
        return cli_address_01;
    }

    public void setCli_address_01(String cli_address_01) {
        this.cli_address_01 = cli_address_01;
    }

    public String getCli_address_02() {
        return cli_address_02;
    }

    public void setCli_address_02(String cli_address_02) {
        this.cli_address_02 = cli_address_02;
    }

    public String getCli_remark() {
        return cli_remark;
    }

    public void setCli_remark(String cli_remark) {
        this.cli_remark = cli_remark;
    }

    public String getCli_regdate() {
        return cli_regdate;
    }

    public void setCli_regdate(String cli_regdate) {
        this.cli_regdate = cli_regdate;
    }
}
