package com.scian.smsstorm.data.bean;

public class SearchItem extends BaseItem {

    private int startTextIndex;
    private int endTextIndex;
    
    public SearchItem(String number){
        this.number = number;
    }

    public int getStartTextIndex() {
        return startTextIndex;
    }

    public void setStartTextIndex(int startTextIndex) {
        this.startTextIndex = startTextIndex;
    }

    public int getEndTextIndex() {
        return endTextIndex;
    }

    public void setEndTextIndex(int endTextIndex) {
        this.endTextIndex = endTextIndex;
    }

    public void refresh() {
        startTextIndex = 0;
        endTextIndex = 0;
    }

}
