package kr.co.goms.app.estimate.model;

import android.os.Parcel;
import android.os.Parcelable;

public class EstimateItemBeanTB implements Parcelable {

    private String est_item_idx;				//견적item 키값 Idx
    private String est_idx;						//견적idx
    private String est_item_no;					//견적item No
    private String item_idx;					//견적item Idx
    private String est_item_name;				//견적item 이름
    private String est_item_quantity;			//견적item수량
    private String est_item_unit_price;			//견적item단가
    private String est_item_price;				//견적item금액
    private String est_item_tax_price;			//견적item세액
    private String est_item_total_price;			//견적item총금액
    private String est_item_remark;				//견적item비고

    public EstimateItemBeanTB() {
    }

    protected EstimateItemBeanTB(Parcel in) {
        est_item_idx = in.readString();
        est_idx = in.readString();
        est_item_no = in.readString();
        item_idx = in.readString();
        est_item_name = in.readString();
        est_item_quantity = in.readString();
        est_item_unit_price = in.readString();
        est_item_price = in.readString();
        est_item_tax_price = in.readString();
        est_item_total_price = in.readString();
        est_item_remark = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(est_item_idx);
        dest.writeString(est_idx);
        dest.writeString(est_item_no);
        dest.writeString(item_idx);
        dest.writeString(est_item_name);
        dest.writeString(est_item_quantity);
        dest.writeString(est_item_unit_price);
        dest.writeString(est_item_price);
        dest.writeString(est_item_tax_price);
        dest.writeString(est_item_total_price);
        dest.writeString(est_item_remark);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<EstimateItemBeanTB> CREATOR = new Creator<EstimateItemBeanTB>() {
        @Override
        public EstimateItemBeanTB createFromParcel(Parcel in) {
            return new EstimateItemBeanTB(in);
        }

        @Override
        public EstimateItemBeanTB[] newArray(int size) {
            return new EstimateItemBeanTB[size];
        }
    };

    public String getEst_item_idx() {
        return est_item_idx;
    }

    public void setEst_item_idx(String est_item_idx) {
        this.est_item_idx = est_item_idx;
    }

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

    public String getEst_item_name() {
        return est_item_name;
    }

    public void setEst_item_name(String est_item_name) {
        this.est_item_name = est_item_name;
    }

    public String getEst_item_quantity() {
        return est_item_quantity;
    }

    public void setEst_item_quantity(String est_item_quantity) {
        this.est_item_quantity = est_item_quantity;
    }

    public String getEst_item_unit_price() {
        return est_item_unit_price;
    }

    public void setEst_item_unit_price(String est_item_unit_price) {
        this.est_item_unit_price = est_item_unit_price;
    }

    public String getEst_item_price() {
        return est_item_price;
    }

    public void setEst_item_price(String est_item_price) {
        this.est_item_price = est_item_price;
    }

    public String getEst_item_tax_price() {
        return est_item_tax_price;
    }

    public void setEst_item_tax_price(String est_item_tax_price) {
        this.est_item_tax_price = est_item_tax_price;
    }

    public String getEst_item_total_price() {
        return est_item_total_price;
    }

    public void setEst_item_total_price(String est_item_total_price) {
        this.est_item_total_price = est_item_total_price;
    }

    public String getEst_item_remark() {
        return est_item_remark;
    }

    public void setEst_item_remark(String est_item_remark) {
        this.est_item_remark = est_item_remark;
    }
}
