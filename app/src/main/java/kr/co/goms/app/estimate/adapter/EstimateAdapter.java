package kr.co.goms.app.estimate.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import kr.co.goms.app.estimate.R;
import kr.co.goms.app.estimate.model.EstimateBeanTB;
import kr.co.goms.module.common.util.FormatUtil;
import kr.co.goms.module.common.util.GomsLog;

public class EstimateAdapter extends RecyclerView.Adapter<EstimateAdapter.SectionHolder> implements Filterable {

    private final String TAG = EstimateAdapter.class.getSimpleName();

    private Context mContext;
    private ArrayList<EstimateBeanTB> mItemList;
    private EstimateClickListener mEstimateClickListener;

    private ArrayList<EstimateBeanTB> mEstimateListFull;
    public enum SEARCH_TYPE{
        TITLE,
    }
    public SEARCH_TYPE mSearchType = SEARCH_TYPE.TITLE;
    private boolean isLongPress = false;

    public interface EstimateClickListener {
        void onEstimateClick(int position, EstimateBeanTB estimateBeanTB );
        void onEstimateLongClick(int position, EstimateBeanTB estimateBeanTB );
    }

    public class SectionHolder extends RecyclerView.ViewHolder {
        private LinearLayout lltEst;
        private TextView tvEstCliName, tvEstTotalPrice;
        public SectionHolder(View itemView, Context context) {
            super(itemView);
            lltEst = itemView.findViewById(R.id.llt_est);
            tvEstCliName = itemView.findViewById(R.id.tv_est_cli_name);
            tvEstTotalPrice = itemView.findViewById(R.id.tv_est_total_price);
            setIsRecyclable(false);
        }
    }

    public EstimateAdapter(Context context, ArrayList<EstimateBeanTB> _SectionList, EstimateClickListener clientClickListener) {
        this.mContext = context;
        this.mItemList = _SectionList;
        this.mEstimateClickListener = clientClickListener;
        this.mEstimateListFull = new ArrayList<>(_SectionList);
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

        EstimateBeanTB estimateBeanTB = mItemList.get(position);
        String totalPrice = null;
        try {
            totalPrice = FormatUtil.money(estimateBeanTB.getEst_total_price());
        }catch(Exception e){
            
        }
        
        holder.tvEstCliName.setText(estimateBeanTB.getEst_cli_name());
        holder.tvEstTotalPrice.setText(totalPrice);

        holder.lltEst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isLongPress) {
                    mEstimateClickListener.onEstimateClick(position, mItemList.get(position));
                }
            }
        });

        holder.lltEst.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View v) {
                mEstimateClickListener.onEstimateLongClick(position, mItemList.get(position));
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

    public void setData(ArrayList<EstimateBeanTB> data) {
        mItemList = data;
    }

    public EstimateBeanTB getItem(final int position) {
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
            List<EstimateBeanTB> filteredList = new ArrayList<EstimateBeanTB>();
            if (charSequence == null || charSequence.length() == 0) {
                //검색값이 없으면 전체를 다 보여준다. 음..
                filteredList.addAll(mEstimateListFull);
            } else {
                String filterPattern = charSequence.toString().toLowerCase().trim();
                for (EstimateBeanTB EstimateBeanTB : mEstimateListFull) {

                    if(SEARCH_TYPE.TITLE.name().equalsIgnoreCase(mSearchType.name())) {
                        if (EstimateBeanTB.getEst_cli_name().toLowerCase().contains(filterPattern)) {
                            filteredList.add(EstimateBeanTB);
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