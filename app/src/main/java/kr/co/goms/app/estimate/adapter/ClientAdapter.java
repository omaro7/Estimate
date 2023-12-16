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
import kr.co.goms.app.estimate.model.ClientBeanTB;
import kr.co.goms.module.common.util.FormatUtil;

public class ClientAdapter extends RecyclerView.Adapter<ClientAdapter.SectionHolder> implements Filterable {

    private final String TAG = ClientAdapter.class.getSimpleName();

    private Context mContext;
    private ArrayList<ClientBeanTB> mItemList;
    private ClientClickListener mClientClickListener;

    private ArrayList<ClientBeanTB> mClientListFull;
    public enum SEARCH_TYPE{
        TITLE,
    }
    public SEARCH_TYPE mSearchType = SEARCH_TYPE.TITLE;
    private boolean isLongPress = false;
    public interface ClientClickListener {
        void onClientClick(int position, ClientBeanTB clientBeanTB );
        void onClientLongClick(int position, ClientBeanTB clientBeanTB );
    }

    public class SectionHolder extends RecyclerView.ViewHolder {
        private LinearLayout lltCli;
        private TextView tvCliName, tvCliCeoName, tvCliBizNum, tvCliManagerName, tvCliHpNum, tvCliRemark;
        public SectionHolder(View itemView, Context context) {
            super(itemView);
            lltCli = itemView.findViewById(R.id.llt_cli);
            tvCliName = itemView.findViewById(R.id.tv_cli_name);
            tvCliCeoName = itemView.findViewById(R.id.tv_cli_ceo_name);
            tvCliBizNum = itemView.findViewById(R.id.tv_cli_biz_num);
            tvCliManagerName = itemView.findViewById(R.id.tv_cli_manager_name);
            tvCliHpNum = itemView.findViewById(R.id.tv_cli_hp_num);
            tvCliRemark = itemView.findViewById(R.id.tv_cli_remark);
            setIsRecyclable(false);
        }
    }

    public ClientAdapter(Context context, ArrayList<ClientBeanTB> _SectionList, ClientClickListener clientClickListener) {
        this.mContext = context;
        this.mItemList = _SectionList;
        this.mClientClickListener = clientClickListener;
        this.mClientListFull = new ArrayList<>(_SectionList);
    }

    @Override
    public SectionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_client, parent, false);
        SectionHolder holder = new SectionHolder(itemView, mContext);
        return holder;

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(final SectionHolder holder, @SuppressLint("RecyclerView") final int position) {

        ClientBeanTB clientBeanTB = mItemList.get(position);
        String bizNum = null;
        try {
            bizNum = FormatUtil.regnNo(clientBeanTB.getCli_biz_num());
        }catch(Exception e){
            
        }
        
        holder.tvCliName.setText(clientBeanTB.getCli_name());
        holder.tvCliCeoName.setText(clientBeanTB.getCli_ceo_name());
        holder.tvCliBizNum.setText(bizNum);
        holder.tvCliManagerName.setText(clientBeanTB.getCli_manager_name());
        holder.tvCliHpNum.setText(clientBeanTB.getCli_hp_num());
        holder.tvCliRemark.setText(clientBeanTB.getCli_remark());

        holder.lltCli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isLongPress) {
                    mClientClickListener.onClientClick(position, mItemList.get(position));
                }
            }
        });

        holder.lltCli.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View v) {
                mClientClickListener.onClientLongClick(position, mItemList.get(position));
                isLongPress = true;
                return false;
            }
        });

        holder.lltCli.setOnTouchListener(new View.OnTouchListener() {
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

    public void setData(ArrayList<ClientBeanTB> data) {
        mItemList = data;
    }

    public ClientBeanTB getItem(final int position) {
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
            List<ClientBeanTB> filteredList = new ArrayList<ClientBeanTB>();
            if (charSequence == null || charSequence.length() == 0) {
                //검색값이 없으면 전체를 다 보여준다. 음..
                filteredList.addAll(mClientListFull);
            } else {
                String filterPattern = charSequence.toString().toLowerCase().trim();
                for (ClientBeanTB ClientBeanTB : mClientListFull) {

                    if(SEARCH_TYPE.TITLE.name().equalsIgnoreCase(mSearchType.name())) {
                        if (ClientBeanTB.getCli_name().toLowerCase().contains(filterPattern)) {
                            filteredList.add(ClientBeanTB);
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