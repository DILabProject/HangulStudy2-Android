package kr.ac.skuniv.di.hangulstudy;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import kr.ac.skuniv.di.hangulstudy.VO.StudyListVO;
import kr.ac.skuniv.di.hangulstudy.VO.StudyVo;

public class ListViewAdapter extends BaseAdapter {
    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<StudyListVO> listViewItemList = new ArrayList<StudyListVO>() ;
    private TextView day;
    private TextView word;
    private ImageView check;

    // ListViewAdapter의 생성자
    public ListViewAdapter() {

    }

    // Adapter에 사용되는 데이터의 개수를 리턴.
    @Override
    public int getCount() {
        return listViewItemList.size() ;
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴.
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_item, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        day = (TextView) convertView.findViewById(R.id.day) ;
        word = (TextView) convertView.findViewById(R.id.word) ;
        check = (ImageView) convertView.findViewById(R.id.check);

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        StudyListVO listViewItem = listViewItemList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        day.setText(listViewItem.getDay());
        word.setText(listViewItem.getWord());

        return convertView;
    }

    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴.
    @Override
    public long getItemId(int position) {
        return position ;
    }

    // 지정한 위치(position)에 있는 데이터 리턴
    @Override
    public StudyListVO getItem(int position) {
        return listViewItemList.get(position);
    }

    // 아이템 데이터 추가를 위한 함수
    public void addItem(String day, String word) {
        StudyListVO item = new StudyListVO();

        item.setDay(day);
        item.setWord(word);

        listViewItemList.add(item);
    }

    public void add(StudyListVO studyListVO) {
        listViewItemList.add(studyListVO);
    }
}