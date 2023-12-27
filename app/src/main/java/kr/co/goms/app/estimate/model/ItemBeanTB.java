package kr.co.goms.app.estimate.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ItemBeanTB implements Parcelable {
    private String est_idx;						//견적idx
    private String est_item_no;					//견적item No
    private String item_idx;        // Primary key
    private String item_token;      //아이템 생성 토큰
    private String item_name;       //아이템 이름
    private String item_std;       //아이템 규격
    private String item_unit;       //아이템 단위
    private String item_quantity;   //아이템 수량
    private String item_unit_price; //아이템 단가
    private String item_price; //아이템 세액 단가
    private String item_tax_price; //아이템 세액 단가
    private String item_total_price;//아이템 합계금액
    private String item_remark;     //아이템 비고
    private String item_regdate;     //생성일

    public ItemBeanTB() {
    }

    protected ItemBeanTB(Parcel in) {
        est_idx = in.readString();
        est_item_no = in.readString();
        item_idx = in.readString();
        item_token = in.readString();
        item_name = in.readString();
        item_std = in.readString();
        item_unit = in.readString();
        item_quantity = in.readString();
        item_unit_price = in.readString();
        item_price = in.readString();
        item_tax_price = in.readString();
        item_total_price = in.readString();
        item_remark = in.readString();
        item_regdate = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(est_idx);
        dest.writeString(est_item_no);
        dest.writeString(item_idx);
        dest.writeString(item_token);
        dest.writeString(item_name);
        dest.writeString(item_std);
        dest.writeString(item_unit);
        dest.writeString(item_quantity);
        dest.writeString(item_unit_price);
        dest.writeString(item_price);
        dest.writeString(item_tax_price);
        dest.writeString(item_total_price);
        dest.writeString(item_remark);
        dest.writeString(item_regdate);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ItemBeanTB> CREATOR = new Creator<ItemBeanTB>() {
        @Override
        public ItemBeanTB createFromParcel(Parcel in) {
            return new ItemBeanTB(in);
        }

        @Override
        public ItemBeanTB[] newArray(int size) {
            return new ItemBeanTB[size];
        }
    };

    public String getEst_idx() {
        return est_idx;
    }

    public void setEst_idx(String est_idx) {
        this.est_idx = est_idx;
    }

    public String getEst_item_no() {
        return est_item_no;
    }

    public void setEst_item_no(String est_item_no) {
        this.est_item_no = est_item_no;
    }

    public String getItem_idx() {
        return item_idx;
    }

    public void setItem_idx(String item_idx) {
        this.item_idx = item_idx;
    }

    public String getItem_token() {
        return item_token;
    }

    public void setItem_token(String item_token) {
        this.item_token = item_token;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getItem_std() {
        return item_std;
    }

    public void setItem_std(String item_std) {
        this.item_std = item_std;
    }

    public String getItem_unit() {
        return item_unit;
    }

    public void setItem_unit(String item_unit) {
        this.item_unit = item_unit;
    }

    public String getItem_quantity() {
        return item_quantity;
    }

    public void setItem_quantity(String item_quantity) {
        this.item_quantity = item_quantity;
    }

    public String getItem_unit_price() {
        return item_unit_price;
    }

    public void setItem_unit_price(String item_unit_price) {
        this.item_unit_price = item_unit_price;
    }

    public String getItem_price() {
        return item_price;
    }

    public void setItem_price(String item_price) {
        this.item_price = item_price;
    }

    public String getItem_tax_price() {
        return item_tax_price;
    }

    public void setItem_tax_price(String item_tax_price) {
        this.item_tax_price = item_tax_price;
    }

    public String getItem_total_price() {
        return item_total_price;
    }

    public void setItem_total_price(String item_total_price) {
        this.item_total_price = item_total_price;
    }

    public String getItem_remark() {
        return item_remark;
    }

    public void setItem_remark(String item_remark) {
        this.item_remark = item_remark;
    }

    public String getItem_regdate() {
        return item_regdate;
    }

    public void setItem_regdate(String item_regdate) {
        this.item_regdate = item_regdate;
    }
}
