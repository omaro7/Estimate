package kr.co.goms.module.common.transform;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.ViewPager;

import kr.co.goms.module.common.adapter.IWordPager;

public class ShadowTransformer implements ViewPager.OnPageChangeListener, ViewPager.PageTransformer {
    private ViewPager mViewPager;
    private IWordPager mAdapter;
    private float mLastOffset;
    private boolean mScalingEnabled;

    public ShadowTransformer(ViewPager viewPager, IWordPager cardAdater){
        mViewPager = viewPager;
        viewPager.addOnPageChangeListener(this);
        mAdapter = cardAdater;
    }

    public void enableScaling(boolean enable){
        if(mScalingEnabled && !enable){
            CardView currentCard = mAdapter.geCardViewAt(mViewPager.getCurrentItem());
            if(currentCard != null){
                currentCard.animate().scaleX(1);
                currentCard.animate().scaleY(1);
            }
        }else if(!mScalingEnabled && enable){
            CardView currentCard = mAdapter.geCardViewAt(mViewPager.getCurrentItem());
            if(currentCard != null){
                currentCard.animate().scaleX(1.1f);
                currentCard.animate().scaleY(1.f);
            }
        }
        mScalingEnabled = enable;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        int realCurrentPosition;
        int nextPosition;
        float baseElevation = mAdapter.getBaseElevation();
        float realOffset;
        boolean goingLeft = mLastOffset > positionOffset;

        if(goingLeft){
            realCurrentPosition = position + 1;
            nextPosition = position;
            realOffset = 1 - positionOffset;
        }else{
            nextPosition = position + 1;
            realCurrentPosition = position;
            realOffset = positionOffset;
        }
        if(nextPosition > mAdapter.getCount() - 1 || realCurrentPosition > mAdapter.getCount() - 1){
            return;
        }

        CardView currentCard = mAdapter.geCardViewAt(realCurrentPosition);

        if(currentCard != null){
            if(mScalingEnabled){
                currentCard.setScaleX((float)(1 + 0.1 * (1 - realOffset)));
                currentCard.setScaleY((float)(1 + 0.1 * (1 - realOffset)));
            }
            currentCard.setCardElevation((baseElevation + baseElevation *  (IWordPager.MAX_ELEVATION_FACTION -1) * (1 - realOffset)));
        }

        CardView nextCard = mAdapter.geCardViewAt(nextPosition);

        if(nextCard != null){
            if(mScalingEnabled){
                nextCard.setScaleX((float)(1 + 0.1 * (realOffset)));
                nextCard.setScaleY((float)(1 + 0.1 * (realOffset)));
            }
            nextCard.setCardElevation((baseElevation + baseElevation * (IWordPager.MAX_ELEVATION_FACTION - 1 ) * (realOffset)));
        }
        mLastOffset = positionOffset;
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void transformPage(@NonNull View page, float position) {

    }
}
