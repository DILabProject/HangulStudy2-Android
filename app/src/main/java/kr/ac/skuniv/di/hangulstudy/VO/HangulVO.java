package kr.ac.skuniv.di.hangulstudy.VO;

import com.google.gson.JsonArray;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by Juhyun on 2018-02-12.
 */

public class HangulVO {
    JsonArray word;
//    List<String> stroke;
    List<ArrayList<LetterVO>> stroke;

    public List<ArrayList<LetterVO>> getStroke() {
        return stroke;
    }

    public void setStroke(List<ArrayList<LetterVO>> stroke) {
        this.stroke = stroke;
    }
//    LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
//    private String str;
//
//    public char getPickedWord(int index){
//        str = str.replaceAll(" ", "");
//        str = "간다";
//
//
//
//        return str.charAt(index);
//
//    }

    public JsonArray getWord() {
        return word;
    }
    public void setWord(JsonArray word) {
        this.word = word;
    }
//    public List<String> getStroke() {
//        return stroke;
//    }
//    public void setStroke(List<String> stroke) {
//        this.stroke = stroke;
//    }
}