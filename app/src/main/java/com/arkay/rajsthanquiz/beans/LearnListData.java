package com.arkay.rajsthanquiz.beans;

/**
 * Created by arkayapps on 08/07/15.
 */
public class LearnListData {
    private int id;
    private String strData;
    private int numberCount;
    private LetLeranScore letLernScore;

    public LearnListData() {
    }

    public LearnListData(int id, String strData, int numberCound, LetLeranScore letLernScore) {
        this.id = id;
        this.strData = strData;
        this.numberCount = numberCound;
        this.letLernScore = letLernScore;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStrData() {
        return strData;
    }

    public void setStrData(String strData) {
        this.strData = strData;
    }

    public int getNumberCount() {
        return numberCount;
    }

    public void setNumberCount(int numberCount) {
        this.numberCount = numberCount;
    }

    public LetLeranScore getLetLernScore() {
        return letLernScore;
    }

    public void setLetLernScore(LetLeranScore letLernScore) {
        this.letLernScore = letLernScore;
    }
}
