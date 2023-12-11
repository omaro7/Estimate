package kr.co.goms.module.common.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import kr.co.goms.module.common.R;
import kr.co.goms.module.common.model.TermBean;
import kr.co.goms.module.common.util.FontUtil;

public class TermAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static String TAG = TermAdapter.class.getSimpleName();
    private Context mContext;
    private ArrayList<TermBean> mTermList;

    private final int VIEW_TYPE_HEADER = 0;
    private final int VIEW_TYPE_ITEM = 1;
    private final int VIEW_TYPE_ITEM_NO = 2;

    public enum HEADER_TYPE{
        VIEW_TYPE_HEADER,
        VIEW_TYPE_ITEM,
        VIEW_TYPE_ITEM_NO
    }

    private int mPreSelectPosition = -1;

    private static TermClickListener mTermClickListener;

    private FontUtil mFont;

    public static class HeaderViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout rltTerm;
        CheckedTextView tvHeader;
        public HeaderViewHolder(View itemView) {
            super(itemView);
            rltTerm = itemView.findViewById(R.id.rlt_term);
            tvHeader = itemView.findViewById(R.id.tv_reg_all_check);
        }
    }

    public static class TermHolder extends RecyclerView.ViewHolder {

        RelativeLayout rltTerm;
        CheckedTextView tvTerm;
        ImageView ivTermArrow;

        public TermHolder(View itemView) {
            super(itemView);
            rltTerm = itemView.findViewById(R.id.rlt_term);
            tvTerm = itemView.findViewById(R.id.tv_agree);
            ivTermArrow = itemView.findViewById(R.id.iv_agree_arrow);
        }

    }

    public static class NoItemViewHolder extends RecyclerView.ViewHolder {
        public TextView tvNoItem;

        public NoItemViewHolder(View itemView) {
            super(itemView);
            tvNoItem = itemView.findViewById(R.id.tv_reg_all_check);
        }
    }

    public TermAdapter(Context context, ArrayList<TermBean> termList, TermClickListener termClickListener){
        mContext= context;
        mTermList = termList;
        mTermClickListener = termClickListener;

        mFont = new FontUtil(context);
    }

    public void setData(ArrayList<TermBean> termList){
        mTermList = termList;
    }

    public ArrayList<TermBean> getData(){
        return mTermList;
    }

    public void setOnItemClickListener(TermClickListener clickListener) {
        this.mTermClickListener = clickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == VIEW_TYPE_HEADER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_term_header, parent, false);
            return new HeaderViewHolder(view);
        }else if(viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_term, parent, false);
            return new TermHolder(view);
        }else{
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_term_header, parent, false);
            return new HeaderViewHolder(view);
        }
//        else {
//            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bank_card_item_empty, parent, false);
//            return new NoItemViewHolder(view);
//        }

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof HeaderViewHolder) {
            final TermBean bean = mTermList.get(position);
            boolean isSelect = bean.isSelect();

            HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
            headerViewHolder.tvHeader.setText(bean.getTerm_header_title());
            headerViewHolder.tvHeader.setChecked(isSelect);

            headerViewHolder.rltTerm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mTermClickListener.onItemClick(position, bean);
                }
            });

        }else if (holder instanceof TermHolder) {
            final TermBean bean = mTermList.get(position);
            TermHolder termHolder = (TermHolder) holder;

            boolean isSelect = bean.isSelect();

            termHolder.tvTerm.setText(bean.getTerm_title());

            termHolder.rltTerm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mTermClickListener.onItemClick(position, bean);
                }
            });

            termHolder.ivTermArrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mTermClickListener.onItemCommentClick(position, bean);
                }
            });

            termHolder.tvTerm.setChecked(isSelect);
            if(isSelect) {
                //termHolder.rltTerm.setBackgroundDrawable(mContext.getDrawable(R.drawable.shape_rounded_corner_select_line));
                termHolder.tvTerm.setTypeface(mFont.getSpoqaRegular(), Typeface.BOLD);
            } else {
                //termHolder.rltTerm.setBackgroundDrawable(mContext.getDrawable(R.drawable.shape_rounded_corner_gray_line));
                termHolder.tvTerm.setTypeface(mFont.getSpoqaRegular(), Typeface.NORMAL);
            }

        }else{

        }
    }

    @Override
    public int getItemViewType(int position) {

        final TermBean bean = mTermList.get(position);
        //header 여부
        if(bean.isHeader()){
            return VIEW_TYPE_HEADER;
        }else{
            //item 존재 여부
            return VIEW_TYPE_ITEM;
       }

    }

    public void addItem(TermBean dataObj, int index) {
        mTermList.add(dataObj);
        notifyItemInserted(index);
        notifyDataSetChanged();
    }

    public void deleteItem(int index) {
        mTermList.remove(index);
        notifyItemRemoved(index);
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return mTermList.size();
    }

    public interface TermClickListener {
        void onItemClick(int position, TermBean bean);
        void onItemCommentClick(int position, TermBean bean);
    }

    //선택된 Row 설정
    public void setSelectRow(int position) {

        if (mTermList != null && position < getItemCount()) {
            //이전 선택되어 있는 Row 선택 제거
            if (mPreSelectPosition >= 0) {
                TermBean preSelectItemData = mTermList.get(mPreSelectPosition);
                preSelectItemData.setSelect(false);

                mTermList.remove(mPreSelectPosition);
                mTermList.add(mPreSelectPosition, preSelectItemData);
            }

            //현재 선택되어 있는 Row 선택 제거
            TermBean selectItemData = mTermList.get(position);
            selectItemData.setSelect(true);

            mTermList.remove(position);
            mTermList.add(position, selectItemData);

            //현재선택을 저장
            this.mPreSelectPosition = position;

            notifyDataSetChanged();
        }
    }

}
