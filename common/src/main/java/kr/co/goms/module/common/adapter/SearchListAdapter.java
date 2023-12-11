package kr.co.goms.module.common.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import kr.co.goms.module.common.R;
import kr.co.goms.module.common.model.SearchListBean;
import kr.co.goms.module.common.util.FontUtil;

public class SearchListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

    private static String TAG = SearchListAdapter.class.getSimpleName();
    private Context mContext;

    private ArrayList<SearchListBean> mFilterSearchList;
    private ArrayList<SearchListBean> mUnFilterSearchList;

    private final int VIEW_TYPE_ITEM = 1;

    private int mPreSelectPosition = -1;

    private static SearchListClickListener mSearchListClickListener;

    private FontUtil mFont;

    public static class SearchListHolder extends RecyclerView.ViewHolder {

        RelativeLayout rltTitle;
        TextView tvTitle;

        public SearchListHolder(View itemView) {
            super(itemView);
            rltTitle = itemView.findViewById(R.id.rlt_title);
            tvTitle = itemView.findViewById(R.id.tv_title);
        }

    }

    public SearchListAdapter(Context context, ArrayList<SearchListBean> searchList, SearchListClickListener SearchListClickListener){
        mContext= context;
        this.mFilterSearchList = searchList;
        this.mUnFilterSearchList = searchList;

        mSearchListClickListener = SearchListClickListener;

        mFont = new FontUtil(context);
    }

    public void setData(ArrayList<SearchListBean> termList){
        mFilterSearchList = termList;
    }

    public ArrayList<SearchListBean> getData(){
        return mFilterSearchList;
    }

    public void setOnItemClickListener(SearchListClickListener clickListener) {
        this.mSearchListClickListener = clickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_basic_title, parent, false);
        return new SearchListHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

            final SearchListBean bean = mFilterSearchList.get(position);
            SearchListHolder SearchListHolder = (SearchListHolder) holder;

            boolean isSelect = bean.isSelect();

            SearchListHolder.tvTitle.setText(bean.getList_title());

            SearchListHolder.rltTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mSearchListClickListener.onItemClick(position, bean);
                }
            });

    }

    @Override
    public int getItemViewType(int position) {
        final SearchListBean bean = mFilterSearchList.get(position);
        return VIEW_TYPE_ITEM;
    }

    public void addItem(SearchListBean dataObj, int index) {
        mFilterSearchList.add(dataObj);
        notifyItemInserted(index);
        notifyDataSetChanged();
    }

    public void deleteItem(int index) {
        mFilterSearchList.remove(index);
        notifyItemRemoved(index);
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return mFilterSearchList.size();
    }

    public interface SearchListClickListener {
        void onItemClick(int position, SearchListBean bean);
    }

    //선택된 Row 설정
    public void setSelectRow(int position) {

        if (mFilterSearchList != null && position < getItemCount()) {
            //이전 선택되어 있는 Row 선택 제거
            if (mPreSelectPosition >= 0) {
                SearchListBean preSelectItemData = mFilterSearchList.get(mPreSelectPosition);
                preSelectItemData.setSelect(false);

                mFilterSearchList.remove(mPreSelectPosition);
                mFilterSearchList.add(mPreSelectPosition, preSelectItemData);
            }

            //현재 선택되어 있는 Row 선택 제거
            SearchListBean selectItemData = mFilterSearchList.get(position);
            selectItemData.setSelect(true);

            mFilterSearchList.remove(position);
            mFilterSearchList.add(position, selectItemData);

            //현재선택을 저장
            this.mPreSelectPosition = position;

            notifyDataSetChanged();
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if(charString.isEmpty()){
                    mFilterSearchList = mUnFilterSearchList;
                }else{
                    ArrayList<SearchListBean> filteringList = new ArrayList<SearchListBean>();
                    for(SearchListBean searchListBeanS : mUnFilterSearchList){
                        if(searchListBeanS.getList_title().toLowerCase().contains(charString.toLowerCase())){
                            filteringList.add(searchListBeanS);
                        }
                    }
                    mFilterSearchList = filteringList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilterSearchList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mFilterSearchList = (ArrayList<SearchListBean>)filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

}
