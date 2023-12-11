package kr.co.goms.module.common.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import kr.co.goms.module.common.R;
import kr.co.goms.module.common.model.OvalBean;
import kr.co.goms.module.common.util.FontUtil;
import kr.co.goms.module.common.util.StringUtil;

public class OvalSelectedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static String TAG = OvalSelectedAdapter.class.getSimpleName();
    private Context mContext;
    private ArrayList<OvalBean> mOvalList;

    private int mPreSelectPosition = -1;

    private static OvalClickListener mOvalClickListener;

    private FontUtil mFont;

    public static class OvalHolder extends RecyclerView.ViewHolder {

        RelativeLayout rltOval;
        ImageView ivOval;
        ImageView ivOvalCheck;

        public OvalHolder(View itemView) {
            super(itemView);
            rltOval = itemView.findViewById(R.id.rlt_oval);
            ivOval = itemView.findViewById(R.id.iv_oval);
            ivOvalCheck = itemView.findViewById(R.id.iv_oval_check);
        }

    }

    public OvalSelectedAdapter(Context context, ArrayList<OvalBean> ovalList, OvalClickListener ovalClickListener){
        mContext= context;
        mOvalList = ovalList;
        mOvalClickListener = ovalClickListener;

        mFont = new FontUtil(context);
    }

    public void setData(ArrayList<OvalBean> ovalList){
        mOvalList = ovalList;
    }

    public ArrayList<OvalBean> getData(){
        return mOvalList;
    }

    public void setOnItemClickListener(OvalClickListener clickListener) {
        this.mOvalClickListener = clickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_oval, parent, false);
            return new OvalHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
       
        final OvalBean bean = mOvalList.get(position);
        OvalHolder ovalHolder = (OvalHolder) holder;

        boolean isSelect = bean.isSelect();

        ovalHolder.rltOval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOvalClickListener.onItemClick(position, bean);
            }
        });

        if("0".equals(bean.getOval_seq())){
            ovalHolder.ivOval.setBackgroundResource(R.drawable.oval_red);
        }else if("1".equals(bean.getOval_seq())){
            ovalHolder.ivOval.setBackgroundResource(R.drawable.oval_yellow);
        }else if("2".equals(bean.getOval_seq())){
            ovalHolder.ivOval.setBackgroundResource(R.drawable.oval_green);
        }

        if(isSelect) {
            ovalHolder.ivOvalCheck.setVisibility(View.VISIBLE);

            if("2".equals(bean.getOval_seq())){
                ovalHolder.ivOvalCheck.setBackgroundResource(R.drawable.btn_checkbox_l_n);
            }else{
                ovalHolder.ivOvalCheck.setBackgroundResource(R.drawable.btn_checkbox_l_s);
            }

        } else {
            ovalHolder.ivOvalCheck.setVisibility(View.GONE);
        }

    }

//    @Override
//    public int getItemViewType(int position) {
//        return VIEW_TYPE_ITEM;
//    }

    public void addItem(OvalBean dataObj, int index) {
        mOvalList.add(dataObj);
        notifyItemInserted(index);
        notifyDataSetChanged();
    }

    public void deleteItem(int index) {
        mOvalList.remove(index);
        notifyItemRemoved(index);
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return mOvalList.size();
    }

    public interface OvalClickListener {
        void onItemClick(int position, OvalBean bean);
    }

    //선택된 Row 설정
    public void setSelectRow(int position) {

        if (mOvalList != null && position < getItemCount()) {
            //이전 선택되어 있는 Row 선택 제거
            if (mPreSelectPosition >= 0) {
                OvalBean preSelectItemData = mOvalList.get(mPreSelectPosition);
                preSelectItemData.setSelect(false);

                mOvalList.remove(mPreSelectPosition);
                mOvalList.add(mPreSelectPosition, preSelectItemData);
            }

            //현재 선택되어 있는 Row 선택 제거
            OvalBean selectItemData = mOvalList.get(position);
            selectItemData.setSelect(true);

            mOvalList.remove(position);
            mOvalList.add(position, selectItemData);

            //현재선택을 저장
            this.mPreSelectPosition = position;

            notifyDataSetChanged();
        }
    }

    public void setAllChecked(boolean isAll){
        ArrayList<OvalBean> tmpList = (ArrayList<OvalBean>)mOvalList.clone();
        if(mOvalList != null) {
            mOvalList.clear();
        }
        if(isAll){
            for(OvalBean ovalBean : tmpList){
                ovalBean.setSelect(true);
                mOvalList.add(ovalBean);
            }
        }else{
            for(OvalBean ovalBean : tmpList){
                ovalBean.setSelect(false);
                mOvalList.add(ovalBean);
            }
        }
        notifyDataSetChanged();
    }

    public boolean isAllChecked(){
        int iChecked = 0;
        int iAllChecked = mOvalList.size();
        for(OvalBean ovalBean : mOvalList){
            iChecked = ovalBean.isSelect()?iChecked + 1:0;
        }
        if(iChecked == iAllChecked){
            return true;
        }else{
            return false;
        }
    }

    public boolean isAllNoChecked(){
        int iChecked = 0;
        int iNoChecked = mOvalList.size();
        for(OvalBean ovalBean : mOvalList){
            iChecked = !ovalBean.isSelect()?iChecked + 1:0;
        }
        if(iChecked == iNoChecked){
            return true;
        }else{
            return false;
        }
    }

    public ArrayList<Integer> getCheckedWordColorList(){
        ArrayList<Integer> arrayList = new ArrayList<Integer>();
        for(OvalBean ovalBean : mOvalList){
            if (ovalBean.isSelect()) {
                arrayList.add(StringUtil.stringToInt(ovalBean.getOval_seq()));
            }
        }
        return arrayList;
    }
}
