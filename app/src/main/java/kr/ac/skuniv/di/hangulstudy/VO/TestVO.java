package kr.ac.skuniv.di.hangulstudy.VO;

import java.util.ArrayList;

//ExpandableListView를 위한 VO

public class TestVO {
    String day;
    ArrayList<String> word = new ArrayList<String>();

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public ArrayList<String> getWord() {
        return word;
    }

    public void setWord(ArrayList<String> word) {
        this.word = word;
    }
}
