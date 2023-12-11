package kr.co.goms.module.common.model;

public class WordPagerItem {
    private  String mWord;
    private  String mWordMean;

    public WordPagerItem(String word, String wordMean){
        mWord = word;
        mWordMean = wordMean;
    }

    public String getWord() {
        return mWord;
    }

    public void setWord(String mWord) {
        this.mWord = mWord;
    }

    public String getWordMean() {
        return mWordMean;
    }

    public void setWordMean(String mWordMean) {
        this.mWordMean = mWordMean;
    }
}
