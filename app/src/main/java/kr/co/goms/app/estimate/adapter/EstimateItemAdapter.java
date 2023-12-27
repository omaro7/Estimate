package kr.co.goms.app.estimate.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
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
import kr.co.goms.module.common.util.GomsLog;
import kr.co.goms.module.common.util.StringUtil;

public class EstimateItemAdapter extends RecyclerView.Adapter<EstimateItemAdapter.ItemHolder> implements Filterable {

    private final String TAG = EstimateItemAdapter.class.getSimpleName();

    private Context mContext;
    private ArrayList<ItemBeanTB> mItemList;
    private ItemClickListener mItemClickListener;

    private ArrayList<ItemBeanTB> mItemListFull;
    public enum SEARCH_TYPE{
        TITLE,
    }
    public SEARCH_TYPE mSearchType = SEARCH_TYPE.TITLE;
    private boolean isLongPress = false;

    private boolean isVat = false;

    public interface ItemClickListener {
        void onItemClick(int position, ItemBeanTB sectionBeanS );
        void onItemLongClick(int position, ItemBeanTB sectionBeanS );
    }

    public class ItemHolder extends RecyclerView.ViewHolder {
        private LinearLayout lltItem, lltRemark, lltItemTaxPrice, lltItemTotal;
        private TextView tvItemName, tvItemStd, tvItemUnit, tvItemQuantity, tvItemUnitPrice, tvItemPrice, tvItemTaxPrice, tvItemTotalPrice, tvItemRemark;
        public ItemHolder(View itemView, Context context) {
            super(itemView);
            lltItem = itemView.findViewById(R.id.llt_item);
            lltRemark = itemView.findViewById(R.id.llt_remark);
            lltItemTaxPrice = itemView.findViewById(R.id.llt_item_tax_price);
            lltItemTotal = itemView.findViewById(R.id.llt_item_total);
            tvItemName = itemView.findViewById(R.id.tv_item_name);
            tvItemStd = itemView.findViewById(R.id.tv_item_std);
            tvItemUnit = itemView.findViewById(R.id.tv_item_unit);
            tvItemQuantity = itemView.findViewById(R.id.tv_item_quantity);
            tvItemUnitPrice = itemView.findViewById(R.id.tv_item_unit_price);
            tvItemPrice = itemView.findViewById(R.id.tv_item_price);
            tvItemTaxPrice = itemView.findViewById(R.id.tv_item_tax_price);
            tvItemTotalPrice = itemView.findViewById(R.id.tv_item_total_price);
            tvItemRemark = itemView.findViewById(R.id.tv_item_remark);
            setIsRecyclable(false);
        }
    }

    public EstimateItemAdapter(Context context, ArrayList<ItemBeanTB> _itemList, ItemClickListener itemClickListener) {
        this.mContext = context;
        this.mItemList = _itemList;
        this.mItemClickListener = itemClickListener;
        this.mItemListFull = new ArrayList<>(_itemList);
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_estimate_item, parent, false);
        ItemHolder holder = new ItemHolder(itemView, mContext);
        return holder;

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(final ItemHolder holder, @SuppressLint("RecyclerView") final int position) {

        ItemBeanTB itemBeanTB = mItemList.get(position);
        String unitPrice = null;
        try {
            unitPrice = FormatUtil.money(itemBeanTB.getItem_unit_price());
        }catch(Exception e){

        }

        String itemPrice = null;
        int iQuantity = 0;
        int iUnitPrice = 0;
        int iItemPrice = 0;
        try{
            iQuantity = StringUtil.stringToInt(itemBeanTB.getItem_quantity());
            iUnitPrice = StringUtil.stringToInt(itemBeanTB.getItem_unit_price());
            iItemPrice = iQuantity * iUnitPrice;
            itemPrice = FormatUtil.money(iItemPrice);
        }catch(Exception e){

        }
        
        holder.tvItemName.setText(itemBeanTB.getItem_name());
        //holder.tvItemStd.setText(itemBeanTB.getItem_std());
        holder.tvItemUnit.setText(itemBeanTB.getItem_unit());
        holder.tvItemQuantity.setText(itemBeanTB.getItem_quantity());
        holder.tvItemUnitPrice.setText(unitPrice);

        holder.tvItemRemark.setText(itemBeanTB.getItem_remark());

        if(isVat){
            holder.lltItemTaxPrice.setVisibility(View.VISIBLE);
            holder.lltItemTotal.setVisibility(View.VISIBLE);
            holder.tvItemPrice.setText(itemPrice);

            int iTaxPrice = (int)(iItemPrice / 10);
            String taxPrice = StringUtil.intToString(iTaxPrice);

            int iTotalPrice = iItemPrice + iTaxPrice;
            String totalPrice = StringUtil.intToString(iTotalPrice);

            try {
                taxPrice = FormatUtil.money(taxPrice);
                totalPrice = FormatUtil.money(totalPrice);
            }catch(Exception e){

            }

            holder.tvItemTaxPrice.setText(taxPrice);
            holder.tvItemTotalPrice.setText(totalPrice);
        }else{
            holder.lltItemTaxPrice.setVisibility(View.GONE);
            holder.lltItemTotal.setVisibility(View.GONE);
            holder.tvItemPrice.setText(itemPrice);
        }

        if(StringUtil.isEmpty(itemBeanTB.getItem_remark())){
            holder.lltRemark.setVisibility(View.GONE);
        }else{
            holder.lltRemark.setVisibility(View.VISIBLE);
        }

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

    public ArrayList<ItemBeanTB> getData(){
        return mItemList;
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

    /**
     * 임시 견적서 아이템 > 총 합계 금액
     * @return
     */
    public String getTotalPrice(){
        int iTotalPrice = 0;
        for(ItemBeanTB itemBeanTB : mItemList){
            if(isVat) {
                iTotalPrice = iTotalPrice + StringUtil.stringToInt(itemBeanTB.getItem_total_price());
            }else{
                iTotalPrice = iTotalPrice + StringUtil.stringToInt(itemBeanTB.getItem_price());
            }
        }
        Log.d(TAG, "getTotalPrice >>>> " + iTotalPrice);
        return StringUtil.intToString(iTotalPrice);
    }

    public void changeVatData(String vatYN) {
        GomsLog.d("VAT", "vatYN : " + vatYN);
        isVat = "Y".equalsIgnoreCase(vatYN);
        notifyDataSetChanged();
    }
}