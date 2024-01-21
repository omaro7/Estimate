package kr.co.goms.app.estimate.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import kr.co.goms.app.estimate.R;
import kr.co.goms.app.estimate.model.EstimateBeanTB;
import kr.co.goms.app.estimate.model.SpecificationBeanTB;
import kr.co.goms.module.common.util.DateUtil;
import kr.co.goms.module.common.util.FormatUtil;
import kr.co.goms.module.common.util.GomsLog;
import kr.co.goms.module.common.util.StringUtil;

public class SpecificationAdapter extends RecyclerView.Adapter<SpecificationAdapter.SectionHolder> implements Filterable {

    private final String TAG = SpecificationAdapter.class.getSimpleName();

    private Context mContext;
    private ArrayList<SpecificationBeanTB> mItemList;
    private SpecificationClickListener mSpecificationClickListener;

    private ArrayList<SpecificationBeanTB> mSpecificationListFull;
    public enum SEARCH_TYPE{
        TITLE,
    }
    public SEARCH_TYPE mSearchType = SEARCH_TYPE.TITLE;
    private boolean isLongPress = false;

    public interface SpecificationClickListener {
        void onSpecificationClick(int position, SpecificationBeanTB specificationBeanTB );
        void onSpecificationExcelDownloadClick(int position, SpecificationBeanTB specificationBeanTB );
        void onSpecificationLongClick(int position, SpecificationBeanTB specificationBeanTB );
    }

    public class SectionHolder extends RecyclerView.ViewHolder {
        private LinearLayout lltEst, lltExcelDowload;
        private TextView tvEstDate, tvEstNum, tvEstCliName, tvEstTotalPrice;
        private Button btnExcelDownload;
        public SectionHolder(View itemView, Context context) {
            super(itemView);
            lltEst = itemView.findViewById(R.id.llt_est);
            lltExcelDowload = itemView.findViewById(R.id.llt_excel_download);
            tvEstDate = itemView.findViewById(R.id.tv_est_date);
            tvEstNum = itemView.findViewById(R.id.tv_est_num);
            tvEstCliName = itemView.findViewById(R.id.tv_est_cli_name);
            tvEstTotalPrice = itemView.findViewById(R.id.tv_est_total_price);
            btnExcelDownload = itemView.findViewById(R.id.btn_excel_download);
            setIsRecyclable(false);
        }
    }

    public SpecificationAdapter(Context context, ArrayList<SpecificationBeanTB> _SectionList, SpecificationClickListener clientClickListener) {
        this.mContext = context;
        this.mItemList = _SectionList;
        this.mSpecificationClickListener = clientClickListener;
        this.mSpecificationListFull = new ArrayList<>(_SectionList);
    }

    @Override
    public SectionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_estimate, parent, false);
        SectionHolder holder = new SectionHolder(itemView, mContext);
        return holder;

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(final SectionHolder holder, @SuppressLint("RecyclerView") final int position) {

        SpecificationBeanTB specificationBeanTB = mItemList.get(position);
        EstimateBeanTB estimateBeanTB = specificationBeanTB.getEstimateBeanTB();
        String totalPrice = null;
        try {
            totalPrice = FormatUtil.money(estimateBeanTB.getEst_total_price());
        }catch(Exception e){
            
        }

        holder.tvEstDate.setText(DateUtil.getConvertDateTimeToDate(estimateBeanTB.getEst_regdate(), "yyyy년 MM월 dd일"));
        holder.tvEstNum.setText(estimateBeanTB.getEst_num());
        holder.tvEstCliName.setText(estimateBeanTB.getEst_cli_name());
        holder.tvEstTotalPrice.setText(totalPrice);

        GomsLog.d(TAG, "onComplete() >> estimateBeanTB.getEst_excel_path() : " + estimateBeanTB.getEst_excel_path());

        if(StringUtil.isEmpty(estimateBeanTB.getEst_excel_path())){
            holder.lltExcelDowload.setVisibility(View.GONE);
        }else{
            holder.lltExcelDowload.setVisibility(View.VISIBLE);
        }

        holder.btnExcelDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isLongPress) {
                    mSpecificationClickListener.onSpecificationExcelDownloadClick(position, mItemList.get(position));
                }
            }
        });

        holder.lltEst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isLongPress) {
                    mSpecificationClickListener.onSpecificationClick(position, mItemList.get(position));
                }
            }
        });

        holder.lltEst.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View v) {
                mSpecificationClickListener.onSpecificationLongClick(position, mItemList.get(position));
                isLongPress = true;
                return false;
            }
        });

        holder.lltEst.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    isLongPress = false;
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mItemList == null) {
            return 0;
        }
        return mItemList.size();
    }

    public void setData(ArrayList<SpecificationBeanTB> data) {
        mItemList = data;
    }

    public SpecificationBeanTB getItem(final int position) {
        return mItemList.get(position);
    }

    /*-------------------------------------------------------
    //검색 처리 부분입니다.
     -------------------------------------------------------*/
    @Override
    public Filter getFilter() {
        return myFilter;
    }

    public Filter myFilter = new Filter() {

        //Automatic on background thread
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<SpecificationBeanTB> filteredList = new ArrayList<SpecificationBeanTB>();
            if (charSequence == null || charSequence.length() == 0) {
                //검색값이 없으면 전체를 다 보여준다. 음..
                filteredList.addAll(mSpecificationListFull);
            } else {
                String filterPattern = charSequence.toString().toLowerCase().trim();
                for (SpecificationBeanTB specificationBeanTB : mSpecificationListFull) {

                    if(SEARCH_TYPE.TITLE.name().equalsIgnoreCase(mSearchType.name())) {
                        if (specificationBeanTB.getEstimateBeanTB().getEst_cli_name().toLowerCase().contains(filterPattern)) {
                            filteredList.add(specificationBeanTB);
                        }
                    }else{

                    }

                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;
            return filterResults;
        }

        //Automatic on UI thread
        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            mItemList.clear();
            mItemList.addAll((List) filterResults.values);
            notifyDataSetChanged();
        }
    };

    public SEARCH_TYPE getSearchType() {
        return mSearchType;
    }

    public void setSearchType(SEARCH_TYPE mSearchType) {
        this.mSearchType = mSearchType;
    }

}