package kr.co.goms.module.common.transform;

import android.view.View;

import androidx.viewpager2.widget.ViewPager2;

public class ScalePageTransformer implements ViewPager2.PageTransformer {

    final float SCALE_MAX = 0.85f;

    @Override
    public void transformPage(View page, float position) {
        float scale = (position < 0)
                ? ((1 - SCALE_MAX) * position + 1)
                : ((SCALE_MAX - 1) * position + 1);
        if (position < 0) {
            page.setPivotX(page.getWidth());
            page.setPivotY(page.getHeight()/2);
        } else {
            page.setPivotX(0);
            page.setPivotY(page.getHeight()/2);
        }
        page.setScaleX(scale);
        page.setScaleY(scale);
    }
}
