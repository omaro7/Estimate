package kr.co.goms.app.estimate.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import kr.co.goms.app.estimate.R;
import kr.co.goms.app.estimate.manager.GlideHelper;
import kr.co.goms.app.estimate.model.CompanyBeanTB;
import kr.co.goms.module.common.util.FormatUtil;

public class CompanyAdapter extends RecyclerView.Adapter<CompanyAdapter.SectionHolder> implements Filterable {

    private final String TAG = CompanyAdapter.class.getSimpleName();

    private Context mContext;
    private ArrayList<CompanyBeanTB> mCompanyList;
    private CompanyClickListener mCompanyClickListener;

    private ArrayList<CompanyBeanTB> mCompanyListFull;
    public enum SEARCH_TYPE{
        TITLE,
    }
    public SEARCH_TYPE mSearchType = SEARCH_TYPE.TITLE;
    private boolean isLongPress = false;
    public interface CompanyClickListener {
        void onCompanyClick(int position, CompanyBeanTB CompanyBeanTB );
        void onCompanyLongClick(int position, CompanyBeanTB CompanyBeanTB );
    }

    public class SectionHolder extends RecyclerView.ViewHolder {
        private LinearLayout lltCom;
        private TextView tvComName, tvComCeoName, tvComBizNum, tvComManagerName, tvComHpNum;
        private ImageView ivComMainYn;
        public SectionHolder(View itemView, Context context) {
            super(itemView);
            lltCom = itemView.findViewById(R.id.llt_com);
            tvComName = itemView.findViewById(R.id.tv_com_name);
            tvComCeoName = itemView.findViewById(R.id.tv_com_ceo_name);
            tvComBizNum = itemView.findViewById(R.id.tv_com_biz_num);
            tvComManagerName = itemView.findViewById(R.id.tv_com_manager_name);
            tvComHpNum = itemView.findViewById(R.id.tv_com_hp_num);
            ivComMainYn = itemView.findViewById(R.id.iv_com_main);
            setIsRecyclable(false);
        }
    }

    public CompanyAdapter(Context context, ArrayList<CompanyBeanTB> _CompanyList, CompanyClickListener CompanyClickListener) {
        this.mContext = context;
        this.mCompanyList = _CompanyList;
        this.mCompanyClickListener = CompanyClickListener;
        this.mCompanyListFull = new ArrayList<>(_CompanyList);
    }

    @Override
    public SectionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_company, parent, false);
        SectionHolder holder = new SectionHolder(itemView, mContext);
        return holder;

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(final SectionHolder holder, @SuppressLint("RecyclerView") final int position) {

        CompanyBeanTB CompanyBeanTB = mCompanyList.get(position);
        String bizNum = null;
        try {
            bizNum = FormatUtil.regnNo(CompanyBeanTB.getCom_biz_num());
        }catch(Exception e){
            
        }
        
        holder.tvComName.setText(CompanyBeanTB.getCom_name());
        holder.tvComCeoName.setText(CompanyBeanTB.getCom_ceo_name());
        holder.tvComBizNum.setText(bizNum);
        holder.tvComManagerName.setText(CompanyBeanTB.getCom_manager_name());
        holder.tvComHpNum.setText(FormatUtil.addHyphenToPhoneNumber(CompanyBeanTB.getCom_hp_num()));

        holder.ivComMainYn.setVisibility("Y".equalsIgnoreCase(CompanyBeanTB.getCom_main_yn())?View.VISIBLE:View.GONE);

        holder.lltCom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isLongPress) {
                    mCompanyClickListener.onCompanyClick(position, mCompanyList.get(position));
                }
            }
        });

        holder.lltCom.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View v) {
                mCompanyClickListener.onCompanyLongClick(position, mCompanyList.get(position));
                isLongPress = true;
                return false;
            }
        });

        holder.lltCom.setOnTouchListener(new View.OnTouchListener() {
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
        if (mCompanyList == null) {
            return 0;
        }
        return mCompanyList.size();
    }

    public void setData(ArrayList<CompanyBeanTB> data) {
        mCompanyList = data;
    }

    public CompanyBeanTB getItem(final int position) {
        return mCompanyList.get(position);
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
            List<CompanyBeanTB> filteredList = new ArrayList<CompanyBeanTB>();
            if (charSequence == null || charSequence.length() == 0) {
                //검색값이 없으면 전체를 다 보여준다. 음..
                filteredList.addAll(mCompanyListFull);
            } else {
                String filterPattern = charSequence.toString().toLowerCase().trim();
                for (CompanyBeanTB CompanyBeanTB : mCompanyListFull) {

                    if(SEARCH_TYPE.TITLE.name().equalsIgnoreCase(mSearchType.name())) {
                        if (CompanyBeanTB.getCom_name().toLowerCase().contains(filterPattern)) {
                            filteredList.add(CompanyBeanTB);
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
            mCompanyList.clear();
            mCompanyList.addAll((List) filterResults.values);
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