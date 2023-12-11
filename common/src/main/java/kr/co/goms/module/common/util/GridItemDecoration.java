package kr.co.goms.module.common.util;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class GridItemDecoration extends RecyclerView.ItemDecoration{
    private int mSpacing;
    private int mTopSpacing;

    public GridItemDecoration(int mSpacing, int mTopSpacing) {
        this.mSpacing = mSpacing;
        this.mTopSpacing = mTopSpacing;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        int index = ((GridLayoutManager.LayoutParams) view.getLayoutParams()).getSpanIndex();
        int position = parent.getChildLayoutPosition(view);
        if(index == 0){
            outRect.right = mSpacing / 2;
        }else{
            outRect.left = mSpacing / 2;
        }

        if(position < 2){
            outRect.top = 0;
        }else{
            outRect.top = mTopSpacing;
        }
    }
}
