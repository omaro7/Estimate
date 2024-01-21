package kr.co.goms.app.estimate.model;

import android.os.Parcel;
import android.os.Parcelable;

public class SpecificationBeanTB implements Parcelable {

    private String spec_idx;				//명세서idx
    private String est_idx;					//견적idx
    private String spec_date;				//거래일자
    private String spec_excel_path;			//견적서 엑셀 경로
    private String spec_regdate;				//명세서 생성일자
    private EstimateBeanTB estimateBeanTB;  //견적서 bean

    public SpecificationBeanTB() {
    }

    protected SpecificationBeanTB(Parcel in) {
        spec_idx = in.readString();
        est_idx = in.readString();
        spec_date = in.readString();
        spec_excel_path = in.readString();
        spec_regdate = in.readString();
        estimateBeanTB = in.readParcelable(SpecificationBeanTB.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(spec_idx);
        dest.writeString(est_idx);
        dest.writeString(spec_date);
        dest.writeString(spec_excel_path);
        dest.writeString(spec_regdate);
        dest.writeParcelable(estimateBeanTB, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SpecificationBeanTB> CREATOR = new Creator<SpecificationBeanTB>() {
        @Override
        public SpecificationBeanTB createFromParcel(Parcel in) {
            return new SpecificationBeanTB(in);
        }

        @Override
        public SpecificationBeanTB[] newArray(int size) {
            return new SpecificationBeanTB[size];
        }
    };

    public String getSpec_idx() {
        return spec_idx;
    }

    public void setSpec_idx(String spec_idx) {
        this.spec_idx = spec_idx;
    }

    public String getEst_idx() {
        return est_idx;
    }

    public void setEst_idx(String est_idx) {
        this.est_idx = est_idx;
    }

    public String getSpec_date() {
        return spec_date;
    }

    public void setSpec_date(String spec_date) {
        this.spec_date = spec_date;
    }

    public String getSpec_excel_path() {
        return spec_excel_path;
    }

    public void setSpec_excel_path(String spec_excel_path) {
        this.spec_excel_path = spec_excel_path;
    }

    public String getSpec_regdate() {
        return spec_regdate;
    }

    public void setSpec_regdate(String spec_regdate) {
        this.spec_regdate = spec_regdate;
    }

    public EstimateBeanTB getEstimateBeanTB() {
        return estimateBeanTB;
    }

    public void setEstimateBeanTB(EstimateBeanTB estimateBeanTB) {
        this.estimateBeanTB = estimateBeanTB;
    }
}
