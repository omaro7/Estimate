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
import kr.co.goms.app.estimate.model.ItemBeanTB;
import kr.co.goms.module.common.util.FormatUtil;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.SectionHolder> implements Filterable {

    private final String TAG = ItemAdapter.class.getSimpleName();

    private Context mContext;
    private ArrayList<ItemBeanTB> mItemList;
    private ItemClickListener mItemClickListener;

    private ArrayList<ItemBeanTB> mItemListFull;
    public enum SEARCH_TYPE{
        TITLE,
    }
    public SEARCH_TYPE mSearchType = SEARCH_TYPE.TITLE;
    private boolean isLongPress = false;
    public interface ItemClickListener {
        void onItemClick(int position, ItemBeanTB sectionBeanS );
        void onItemLongClick(int position, ItemBeanTB sectionBeanS );
    }

    public class SectionHolder extends RecyclerView.ViewHolder {
        private LinearLayout lltItem;
        private TextView tvItemName, tvItemStd, tvItemUnit, tvItemPrice, tvItemRemark;
        public SectionHolder(View itemView, Context context) {
            super(itemView);
            lltItem = itemView.findViewById(R.id.llt_item);
            tvItemName = itemView.findViewById(R.id.tv_item_name);
            tvItemStd = itemView.findViewById(R.id.tv_item_std);
            tvItemUnit = itemView.findViewById(R.id.tv_item_unit);
            tvItemPrice = itemView.findViewById(R.id.tv_item_price);
            tvItemRemark = itemView.findViewById(R.id.tv_item_remark);
            setIsRecyclable(false);
        }
    }

    public ItemAdapter(Context context, ArrayList<ItemBeanTB> _SectionList, ItemClickListener itemClickListener) {
        this.mContext = context;
        this.mItemList = _SectionList;
        this.mItemClickListener = itemClickListener;
        this.mItemListFull = new ArrayList<>(_SectionList);
    }

    @Override
    public SectionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_item, parent, false);
        SectionHolder holder = new SectionHolder(itemView, mContext);
        return holder;

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(final SectionHolder holder, @SuppressLint("RecyclerView") final int position) {

        ItemBeanTB itemBeanTB = mItemList.get(position);
        String price = null;
        try {
            price = FormatUtil.money(itemBeanTB.getItem_price());
        }catch(Exception e){
            
        }
        
        holder.tvItemName.setText(itemBeanTB.getItem_name());
        holder.tvItemStd.setText(itemBeanTB.getItem_std());
        holder.tvItemUnit.setText(itemBeanTB.getItem_unit());
        holder.tvItemPrice.setText(price);
        holder.tvItemRemark.setText(itemBeanTB.getItem_remark());

        holder.lltItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isLongPress) {
                    mItemClickListener.onItemClick(position, mItemList.get(position));
                }
            }
        });

        holder.lltItem.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View v) {
                mItemClickListener.onItemLongClick(position, mItemList.get(position));
                isLongPress = true;
                return false;
            }
        });

        holder.lltItem.setOnTouchListener(new View.OnTouchListener() {
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

    public void setData(ArrayList<ItemBeanTB> data) {
        mItemList = data;
    }

    public ItemBeanTB getItem(final int position) {
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
            List<ItemBeanTB> filteredList = new ArrayList<ItemBeanTB>();
            if (charSequence == null || charSequence.length() == 0) {
                //검색값이 없으면 전체를 다 보여준다. 음..
                filteredList.addAll(mItemListFull);
            } else {
                String filterPattern = charSequence.toString().toLowerCase().trim();
                for (ItemBeanTB ItemBeanTB : mItemListFull) {

                    if(SEARCH_TYPE.TITLE.name().equalsIgnoreCase(mSearchType.name())) {
                        if (ItemBeanTB.getItem_name().toLowerCase().contains(filterPattern)) {
                            filteredList.add(ItemBeanTB);
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