package kr.co.goms.module.common.adapter;

import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;
import java.util.List;

import kr.co.goms.module.common.R;
import kr.co.goms.module.common.model.WordPagerItem;

public class WordPagerAdapter extends PagerAdapter implements IWordPager {

    private List<CardView> mViews;
    private List<WordPagerItem> mData;
    private float mBaseElevation;

    public WordPagerAdapter(){
        mViews = new ArrayList<>();
        mData = new ArrayList<>();
    }

    public void addWordPagerItem(WordPagerItem WordPagerItem){
        mViews.add(null);
        mData.add(WordPagerItem);
    }

    public float getBaseElevation(){
        return mBaseElevation;
    }

    @Override
    public CardView geCardViewAt(int position) {
        return mViews.get(position);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.item_word, container, false);
        container.addView(view);

        WordPagerItem item = mData.get(position);
        TextView tvWord = view.findViewById(R.id.tv_word);
        TextView tvWordMean = view.findViewById(R.id.tv_word_mean);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tvWord.setText(Html.fromHtml(item.getWord(), 0));
            tvWordMean.setText(Html.fromHtml(item.getWordMean(), 0));
        }else{
            tvWord.setText(item.getWord());
            tvWordMean.setText(item.getWordMean());
        }

        CardView cardView = (CardView) view.findViewById(R.id.cardView);
        if(mBaseElevation == 0){
            mBaseElevation = cardView.getCardElevation();
        }

        cardView.setMaxCardElevation(mBaseElevation * MAX_ELEVATION_FACTION);
        mViews.set(position, cardView);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object){
        container.removeView((View)object);
        mViews.set(position, null);
    }

}
