package kr.co.goms.module.common.adapter;

import androidx.cardview.widget.CardView;

public interface IWordPager {
    int MAX_ELEVATION_FACTION = 8;
    float getBaseElevation();
    CardView geCardViewAt(int position);
    int getCount();
}
