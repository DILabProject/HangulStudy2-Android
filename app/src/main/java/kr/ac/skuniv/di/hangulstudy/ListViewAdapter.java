package kr.ac.skuniv.di.hangulstudy;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import kr.ac.skuniv.di.hangulstudy.VO.StudyListVO;
import kr.ac.skuniv.di.hangulstudy.VO.StudyVo;
import kr.ac.skuniv.di.hangulstudy.http.BringStudyList;

public class ListViewAdapter extends BaseAdapter {

    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    public ArrayList<StudyListVO> listViewItemList = new ArrayList<StudyListVO>() ;

    // ListViewAdapter의 생성자
    public ListViewAdapter() {

    }

    // Adapter에 사용되는 데이터의 개수를 리턴.
    @Override
    public int getCount() {
        return listViewItemList.size() ;
    }

    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴.
    @Override
    public long getItemId(int position) {
//        return position ;
        return 0;
    }

    // 지정한 위치(position)에 있는 데이터 리턴
    @Override
    public StudyListVO getItem(int position) {
        return listViewItemList.get(position);
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴.
    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        ViewHolder holder;

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_item, parent, false);

            // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
            TextView index = (TextView) convertView.findViewById(R.id.index) ;
            TextView word = (TextView) convertView.findViewById(R.id.word) ;
            ImageView check = (ImageView) convertView.findViewById(R.id.check);

            holder = new ViewHolder();
            holder.index = index;
            holder.word = word;
            holder.check = check;

            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder)convertView.getTag();
        }

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        StudyListVO listViewItem = listViewItemList.get(position);


        if(listViewItem != null){
            // 아이템 내 각 위젯에 데이터 반영

            ViewHolder viewHolder = (ViewHolder) convertView.getTag();
            viewHolder.index.setText(String.valueOf(position+1));
            viewHolder.word.setText(listViewItem.getWord());

            if(listViewItem.getCheckword().equals("1")){
                viewHolder.check.setImageResource(R.drawable.ic_check);
            }
            else
                viewHolder.check.setImageResource(View.INVISIBLE);
        }


        return convertView;
    }

    public class ViewHolder {
        public TextView index;
        public TextView word;
        public ImageView check;

    }

    public void addItem(StudyListVO studyListVO) {

        listViewItemList.add(studyListVO);
    }
}