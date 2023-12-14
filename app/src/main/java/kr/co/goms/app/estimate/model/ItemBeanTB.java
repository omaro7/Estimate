package kr.co.goms.app.estimate.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ItemBeanTB implements Parcelable {
    private String item_idx;        // Primary key
    private String item_name;       //아이템 이름
    private String item_std;       //아이템 단위
    private String item_unit;       //아이템 단위
    private String item_price;      //아이템 판매가
    private String item_remark;     //아이템 비고

    public ItemBeanTB() {
    }

    protected ItemBeanTB(Parcel in) {
        item_idx = in.readString();
        item_name = in.readString();
        item_std = in.readString();
        item_unit = in.readString();
        item_price = in.readString();
        item_remark = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(item_idx);
        dest.writeString(item_name);
        dest.writeString(item_std);
        dest.writeString(item_unit);
        dest.writeString(item_price);
        dest.writeString(item_remark);
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

    public String getItem_idx() {
        return item_idx;
    }

    public void setItem_idx(String item_idx) {
        this.item_idx = item_idx;
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

    public String getItem_price() {
        return item_price;
    }

    public void setItem_price(String item_price) {
        this.item_price = item_price;
    }

    public String getItem_remark() {
        return item_remark;
    }

    public void setItem_remark(String item_remark) {
        this.item_remark = item_remark;
    }
}
